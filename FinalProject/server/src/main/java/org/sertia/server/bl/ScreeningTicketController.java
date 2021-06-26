package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.request.CancelScreeningTicketRequest;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.ClientSeatMapResponse;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;
import org.sertia.contracts.screening.ticket.response.VoucherBalanceResponse;
import org.sertia.contracts.screening.ticket.response.VoucherPaymentResponse;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import java.util.*;
import java.util.stream.Collectors;

public class ScreeningTicketController implements Reportable {

    private final CovidRegulationsController covidRegulationsController;

    public ScreeningTicketController(CovidRegulationsController covidRegulationsController) {
        this.covidRegulationsController = covidRegulationsController;
    }

    public SertiaBasicResponse buyTicketWithRegulations(ScreeningTicketWithCovidRequest request) {
        ClientSeatMapResponse seatMapForScreening = getSeatMapForScreening(request.screeningId);
        try {
            Set<HallSeat> seats = automaticChoseSeats(seatMapForScreening, request.numberOfSeats);
            return purchaseTicketsForScreening(createScreeningTicketWithSeatsRequest(request, seats));
        } catch (IllegalArgumentException exception) {
            System.out.println("Failed to buy tickets with regulations");
            return new ScreeningPaymentResponse(false)
                    .setFailReason("automatic seats choosing failed");
        }
    }

    public SertiaBasicResponse buyTicketWithSeatChose(ScreeningTicketWithSeatsRequest request) {
        if (!isPaymentRequestValid(request)) {
            return new ScreeningPaymentResponse(false)
                    .setFailReason("Payment details are invalid");
        }

        return purchaseTicketsForScreening(request);
    }

    public SertiaBasicResponse cancelTicket(CancelScreeningTicketRequest request) {
        return DbUtils.getById(ScreeningTicket.class, request.ticketId)
                .map(screeningTicket -> {
                    try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                        session.delete(screeningTicket);
                    } catch (RuntimeException e) {
                        System.out.println("couldn't delete ticket");
                        return new SertiaBasicResponse(false)
                                .setFailReason("couldn't delete ticket");
                    }

                    return new SertiaBasicResponse(true);
                }).orElse(new SertiaBasicResponse(false)
                        .setFailReason("ticket doesn't exist"));
    }

    public SertiaBasicResponse getSeatMapForScreening(GetScreeningSeatMap request) {
        return getSeatMapForScreening(request.screeningId);
    }

    public SertiaBasicResponse buyVoucher(BasicPaymentRequest paymentDetails) {
        VouchersInfo vouchersInfo = DbUtils.getById(VouchersInfo.class, 1).orElse(null);
        if (vouchersInfo == null) {
            return new SertiaBasicResponse(false).setFailReason("no voucher info in system");
        }

        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            TicketsVoucher voucher = new TicketsVoucher();
            voucher.setCustomerPaymentDetails(getPaymentDetails(paymentDetails));
            voucher.setTicketsBalance(vouchersInfo.getVoucherInitialBalance());
            int voucherId = (int) session.save(voucher);

            return new VoucherPaymentResponse(false)
                    .setVoucherId(voucherId);
        } catch (RuntimeException exception) {
            return new VoucherPaymentResponse(false)
                    .setFailReason("couldn't purchase voucher");
        }
    }

    public SertiaBasicResponse getVoucherBalance(VoucherBalanceRequest request) {
        return DbUtils.getById(TicketsVoucher.class, request.voucherId).map(ticketsVoucher -> {
            VoucherBalanceResponse response = new VoucherBalanceResponse(true);
            response.balance = ticketsVoucher.getTicketsBalance();

            return response;
        }).orElseGet(() -> {
            VoucherBalanceResponse response = new VoucherBalanceResponse(false);
            response.setFailReason("voucher doesn't exist");

            return response;
        });
    }

    public SertiaBasicResponse useVoucher(UseVoucherRequest request) {
        return DbUtils.getById(TicketsVoucher.class, request.voucherId).map(ticketsVoucher -> {
            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                if (ticketsVoucher.getTicketsBalance() == 1) {
                    session.delete(ticketsVoucher);
                }

                ticketsVoucher.setTicketsBalance(ticketsVoucher.getTicketsBalance() - 1);
                session.save(ticketsVoucher);
                return new SertiaBasicResponse(true);
            } catch (RuntimeException exception) {
                return new SertiaBasicResponse(false).setFailReason("failed to update voucher");
            }
        }).orElseGet(() -> {
            SertiaBasicResponse response = new SertiaBasicResponse(false);
            response.setFailReason("voucher doesn't exist");
            return response;
        });
    }

    @Override
    public ClientReport[] createSertiaReports() {
        return new ClientReport[0];
    }

    @Override
    public ClientReport[] createCinemaReports(String cinemaId) {
        return new ClientReport[0];
    }

    private boolean isPaymentRequestValid(BasicPaymentRequest clientPaymentRequest) {
        return true;
    }

    private SertiaBasicResponse purchaseTicketsForScreening(ScreeningTicketWithSeatsRequest request) {
        ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse(true);
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Set<ScreeningTicket> screeningTickets = DbUtils.getById(Screening.class, request.screeningId)
                    .map(screening -> request.chosenSeats.stream()
                            .map(hallSeat -> createScreeningTicket(hallSeat, screening, session))
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet());

            CustomerPaymentDetails paymentDetails = getPaymentDetails(request);
            session.save(paymentDetails);
            for (ScreeningTicket screeningTicket : screeningTickets) {
                screeningTicket.setPaymentInfo(paymentDetails);
                int ticketId = (int) session.save(screeningTicket);
                org.sertia.server.dl.classes.HallSeat seat = screeningTicket.getSeat();
                HallSeat purchasedSeat = new HallSeat(seat.getId());
                purchasedSeat.isTaken = true;
                purchasedSeat.row = seat.getRowNumber();
                purchasedSeat.numberInRow = seat.getNumberInRow();
                paymentResponse.addTicket(ticketId, purchasedSeat);
            }

        } catch (RuntimeException exception) {
            return new ScreeningPaymentResponse(false)
                    .setFailReason("Problem during purchase process");
        }

        return paymentResponse;
    }

    private ClientSeatMapResponse getSeatMapForScreening(int screeningId) {
        final List<HallSeat> hallSeatList = new ArrayList<>();

        DbUtils.getById(Screening.class, screeningId).ifPresent(screening ->
        {
            Set<Integer> takenSeats =
                    screening.getTickets().stream()
                            .map(ScreeningTicket::getSeat)
                            .map(org.sertia.server.dl.classes.HallSeat::getId)
                            .collect(Collectors.toSet());

            screening.getHall().getSeats().stream().map(hallSeat -> {
                HallSeat clientHallSeat = new HallSeat(hallSeat.getId());
                clientHallSeat.row = hallSeat.getRowNumber();
                clientHallSeat.numberInRow = hallSeat.getNumberInRow();
                clientHallSeat.isTaken = takenSeats.contains(hallSeat.getId());
                return clientHallSeat;
            }).forEach(hallSeatList::add);
        });

        return new ClientSeatMapResponse(true, hallSeatList);
    }

    private ScreeningTicket createScreeningTicket(int hallSeat, Screening screening, Session session) {
        ScreeningTicket ticket = new ScreeningTicket();
        org.sertia.server.dl.classes.HallSeat requestedSeat =
                DbUtils.getById(org.sertia.server.dl.classes.HallSeat.class, hallSeat)
                        .orElseThrow(() -> new IllegalArgumentException("seat doesn't exist"));

        if (!isSeatFree(screening.getId(), hallSeat, session)) {
            throw new IllegalArgumentException("seat isn't free");
        }

        ticket.setScreening(screening);
        ticket.setVoucher(false);
        ticket.setPaidPrice(screening.getScreenableMovie().getTicketPrice());
        ticket.setSeat(requestedSeat);
        return ticket;
    }

    private Set<HallSeat> automaticChoseSeats(ClientSeatMapResponse seatMap, int numberOfSeats) {
        Set<HallSeat> seats = new HashSet<>();

        for (HallSeat hallSeat : seatMap.hallSeats) {
            if (!hallSeat.isTaken) {
                seats.add(hallSeat);
                numberOfSeats--;
                if (numberOfSeats == 0) {
                    return seats;
                }
            }
        }

        throw new IllegalArgumentException("can't choose seats for desired screening");
    }

    private boolean isSeatFree(int screeningId, int seatId, Session session) {
        Query query = session
                .createQuery("from ScreeningTicket where seat_seat_id = :seat and screening_screening_id = :screening");
        query.setParameter("seat", seatId);
        query.setParameter("screening", screeningId);
        return query.list().isEmpty();
    }

    private ScreeningTicketWithSeatsRequest createScreeningTicketWithSeatsRequest(ScreeningTicketWithCovidRequest request, Set<HallSeat> seats) {
        ScreeningTicketWithSeatsRequest screeningTicketRequest = new ScreeningTicketWithSeatsRequest();
        screeningTicketRequest.cardHolderId = request.cardHolderId;
        screeningTicketRequest.creditCardNumber = request.creditCardNumber;
        screeningTicketRequest.cardHolderName = request.cardHolderName;
        screeningTicketRequest.cardHolderPhone = request.cardHolderPhone;
        screeningTicketRequest.cardHolderEmail = request.cardHolderEmail;
        screeningTicketRequest.expirationDate = request.expirationDate;
        screeningTicketRequest.cvv = request.cvv;
        screeningTicketRequest.screeningId = request.screeningId;
        screeningTicketRequest.chosenSeats = seats.stream().map(HallSeat::getId).collect(Collectors.toList());

        return screeningTicketRequest;
    }

    private CustomerPaymentDetails getPaymentDetails(BasicPaymentRequest paymentRequest) {
        CustomerPaymentDetails paymentDetails = new CustomerPaymentDetails();
        paymentDetails.setPayerId(paymentRequest.cardHolderId);
        paymentDetails.setFullName(paymentRequest.cardHolderName);
        paymentDetails.setExperationDate(new Date(paymentRequest.expirationDate.getMillis()));
        paymentDetails.setPaymentMethod(PaymentMethod.Credit);
        paymentDetails.setCreditNumber(paymentRequest.creditCardNumber);
        paymentDetails.setCvv(paymentRequest.cvv);
        paymentDetails.setEmail(paymentRequest.cardHolderEmail);
        paymentDetails.setPhoneNumber(paymentRequest.cardHolderPhone);

        return paymentDetails;
    }
}