package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.request.CancelStreamingTicketRequest;
import org.sertia.contracts.screening.ticket.request.StreamingPaymentRequest;
import org.sertia.contracts.screening.ticket.response.StreamingPaymentResponse;
import org.sertia.server.bl.Services.CreditCardService;
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

public class StreamingTicketController extends Reportable {
    private final ICreditCardService creditCardService;
    public StreamingTicketController(ICreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    public StreamingPaymentResponse purchaseStreamingTicket(StreamingPaymentRequest request) {
        Streaming streaming = getStreaming(request.movieId).orElse(null);
        StreamingPaymentResponse response = new StreamingPaymentResponse(true);
        if (streaming == null) {
            return Utils.createFailureResponse(response, "no streaming exist for movie " + request.movieId);
        }

        StreamingLink streamingLink = new StreamingLink();
        CustomerPaymentDetails paymentDetails = Utils.getPaymentDetails(request);
        streamingLink.setCustomerPaymentDetails(paymentDetails);
        LocalDateTime startTime = request.startTime;
        streamingLink.setActivationStart(startTime);
        int availabilityDays = 1 + request.extraDays;
        streamingLink.setActivationEnd(startTime.plusDays(availabilityDays));
        streamingLink.setPaidPrice(streaming.getExtraDayPrice() * availabilityDays);
        streamingLink.setLink("http://Sertia/link=" + UUID.randomUUID());
        streamingLink.setPurchaseDate(LocalDateTime.now());
        streamingLink.setMovie(streaming);

        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            int purchaseId = (int) session.save(streamingLink);
            response.purchaseId = purchaseId;
            response.startTime = streamingLink.getActivationStart();
            response.endTime = streamingLink.getActivationEnd();
            response.price = streamingLink.getPaidPrice();
            response.streamingLink = streamingLink.getLink();
            CustomerNotifier.getInstance().notify(request.cardHolderEmail, getStreamingMail(response));

            return response;
        } catch (RuntimeException exception) {
            return Utils.createFailureResponse(response, "failed to purchase link");
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

    public SertiaBasicResponse cancelStreamingTicket(CancelStreamingTicketRequest request) {
        int streamingTickerId = request.streamingId;
        return DbUtils.getById(StreamingLink.class, streamingTickerId).map(streamingTicket -> {
            if (getHoursToLinkActivation(streamingTicket) < 1) {
                return new SertiaBasicResponse(false)
                        .setFailReason("can't cancel link that starts in less than an hour");
            }

            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                session.delete(streamingTicket);
                creditCardService.refund(streamingTicket.getCustomerPaymentDetails(), streamingTicket.getPaidPrice() / 2, RefundReason.StreamingService);
                return new SertiaBasicResponse(true);
            } catch (RuntimeException exception) {
                return new SertiaBasicResponse(false)
                        .setFailReason("failed to delete streaming ticket");
            }
        }).orElseGet(() ->
                new SertiaBasicResponse(false)
                        .setFailReason("no streaming with id " + streamingTickerId));
    }

    private long getHoursToLinkActivation(StreamingLink streamingLink) {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), streamingLink.getActivationStart());
    }

    private void notifyClientsRegardingStreamingLinks() {

    }

    private String getStreamingMail(StreamingPaymentResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" :מזהה רכישה").append(response.purchaseId).append("\n")
        .append(" :שעת התחלה").append(response.startTime).append("\n")
        .append(" :תשלום סופי").append(response.price).append("\n")
        .append(" :לינק").append(response.streamingLink);

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