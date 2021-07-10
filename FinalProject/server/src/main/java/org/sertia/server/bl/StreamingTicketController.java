package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.request.CancelStreamingTicketRequest;
import org.sertia.contracts.screening.ticket.request.StreamingPaymentRequest;
import org.sertia.contracts.screening.ticket.response.GetStreamingByLinkResponse;
import org.sertia.contracts.screening.ticket.response.StreamingPaymentResponse;
import org.sertia.contracts.screening.ticket.response.TicketCancellationResponse;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.Services.CustomerNotifier;
import org.sertia.server.bl.Services.ICreditCardService;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.RefundReason;
import org.sertia.server.dl.classes.Streaming;
import org.sertia.server.dl.classes.StreamingLink;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StreamingTicketController extends Reportable {
    private final ICreditCardService creditCardService;
    private final ScheduledExecutorService streamingNotifier;

    public StreamingTicketController(ICreditCardService creditCardService) {
        this.creditCardService = creditCardService;
        streamingNotifier = Executors.newSingleThreadScheduledExecutor();
        streamingNotifier.scheduleAtFixedRate(
                this::notifyClientsRegardingStreamingLinks,
                0,
                1,
                TimeUnit.MINUTES);
    }

    public StreamingPaymentResponse purchaseStreamingTicket(StreamingPaymentRequest request) {
        Streaming streaming = getStreaming(request.movieId).orElse(null);
        StreamingPaymentResponse response = new StreamingPaymentResponse(true);
        if (streaming == null) {
            return Utils.createFailureResponse(response, "לא קיימת חבילת צפייה עבור הסרט " + request.movieId);
        }

        StreamingLink streamingLink = new StreamingLink();
        CustomerPaymentDetails paymentDetails = Utils.getPaymentDetails(request);
        streamingLink.setCustomerPaymentDetails(paymentDetails);
        LocalDateTime startTime = request.startTime;
        streamingLink.setActivationStart(startTime);
        int availabilityDays = request.extraDays;
        streamingLink.setActivationEnd(startTime.plusDays(availabilityDays));
        streamingLink.setPaidPrice(streaming.getExtraDayPrice() * availabilityDays);
        streamingLink.setLink("http://Sertia/link=" + UUID.randomUUID());
        streamingLink.setPurchaseDate(LocalDateTime.now());
        streamingLink.setMovie(streaming);

        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            session.saveOrUpdate(streamingLink.getCustomerPaymentDetails());
            int purchaseId = (int) session.save(streamingLink);
            response.purchaseId = purchaseId;
            response.startTime = streamingLink.getActivationStart();
            response.endTime = streamingLink.getActivationEnd();
            response.price = streamingLink.getPaidPrice();
            response.streamingLink = streamingLink.getLink();
            CustomerNotifier.getInstance().notify(request.cardHolderEmail, getStreamingMail(response));

            return response;
        } catch (RuntimeException exception) {
            return Utils.createFailureResponse(response, "רכישת ");
        }
    }

    public GetStreamingByLinkResponse getStreamingByLink(String link){
        GetStreamingByLinkResponse response = new GetStreamingByLinkResponse(false);

        try {
            List<StreamingLink> links = DbUtils.getAll(StreamingLink.class);
            LocalDateTime now = LocalDateTime.now();
            boolean linkFound = false;

            for (StreamingLink streamingLink : links) {
                if(streamingLink.getLink().equals(link)) {
                    linkFound = true;

                    // Checking the link's activity
                    if(now.isAfter(streamingLink.getActivationStart()) && streamingLink.getActivationEnd().isAfter(now)) {
                        response.setSuccessful(true);
                        response.movieName = streamingLink.getMovie().getMovie().getName();
                        break;
                    } else {
                        response.setSuccessful(false);
                        response.setFailReason("The link is not active, its activation duration are " + streamingLink.getActivationStart() + " - " + streamingLink.getActivationEnd());
                        break;
                    }
                }
            }

            if(!linkFound)
                response.setFailReason("There are no such link");

            return response;
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            throw new SertiaException("We couldn't get your link due internal technical issues.");
        }
    }

    private Optional<Streaming> getStreaming(int movieId) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Query query = session
                    .createQuery("from Streaming where movie_id = :movieId");
            query.setParameter("movieId", movieId);
            return Optional.ofNullable((Streaming) query.list().get(0));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    public TicketCancellationResponse cancelStreamingTicket(CancelStreamingTicketRequest request) {
        int streamingTickerId = request.streamingId;
        TicketCancellationResponse response = new TicketCancellationResponse(true);
        return DbUtils.getById(StreamingLink.class, streamingTickerId).map(streamingTicket -> {
            if (!streamingTicket.getCustomerPaymentDetails().getPayerId().equals(request.userId)) {
                return null;
            }

            if (getHoursToLinkActivation(streamingTicket) < 1) {
                return Utils.createFailureResponse(response, "לא ניתן לבטל חבילת צפייה פחות משעה לפני הפיכתה לזמינה");

            }

            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                session.beginTransaction();
                session.delete(streamingTicket);
                session.flush();
                session.getTransaction().commit();
                double amount = streamingTicket.getPaidPrice() / 2;
                creditCardService.refund(streamingTicket.getCustomerPaymentDetails(), amount, RefundReason.StreamingService);
                response.refundAmount = amount;
                return response;
            } catch (RuntimeException exception) {
                return Utils.createFailureResponse(response, "ביטול רכישה נכשל, פנה לשירות לקוחות");
            }
        }).orElseGet(() ->
                Utils.createFailureResponse(response, "חבילת צפייה עם הפרטים הנוכחיים אינה קיימת"));
    }

    private long getHoursToLinkActivation(StreamingLink streamingLink) {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), streamingLink.getActivationStart());
    }

    private void notifyClientsRegardingStreamingLinks() {
        System.out.println("notifying links " + LocalDateTime.now());
        DbUtils.getAll(StreamingLink.class).forEach(streamingLink -> {
            long between = ChronoUnit.MINUTES.between(LocalDateTime.now().withSecond(0), streamingLink.getActivationStart().withSecond(0));
            if (between == 59) {
                CustomerNotifier.getInstance().notify(
                        streamingLink.getCustomerPaymentDetails().getEmail(),
                        "הלינק לחבילת הצפייה שלך  זמין בעוד שעה");
            }
        });
    }

    private String getStreamingMail(StreamingPaymentResponse response) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(response.purchaseId).append(" :מזהה רכישה").append("\n")
                .append(response.startTime).append(" :שעת התחלה").append("\n")
                .append(response.endTime).append(" :שעת סיום").append("\n")
                .append(response.price).append(" :תשלום סופי").append("\n")
                .append(response.streamingLink).append(" :לינק");

        return stringBuilder.toString();
    }

    @Override
    public List<ClientReport> createSertiaReports() {
        return Collections.emptyList();
    }

    @Override
    public List<ClientReport> createCinemaReports(int cinemaId) {
        return Collections.emptyList();
    }
}