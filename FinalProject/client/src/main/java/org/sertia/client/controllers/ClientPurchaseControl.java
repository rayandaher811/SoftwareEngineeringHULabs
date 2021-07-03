package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.*;

import java.time.LocalDateTime;

public class ClientPurchaseControl extends ClientControl {

    public ClientSeatMapResponse getScreeningSeatMap(int screeningId) {
        return client.request(new GetScreeningSeatMap(screeningId), ClientSeatMapResponse.class);
    }

    public ScreeningPaymentResponse purchaseScreeningTicketsWithSeats(ScreeningTicketWithSeatsRequest request) {
        return client.request(request, ScreeningPaymentResponse.class);
    }

    public ScreeningPaymentResponse purchaseScreeningTicketsWithCovid(ScreeningTicketWithCovidRequest request) {
        return client.request(request, ScreeningPaymentResponse.class);
    }

    public SertiaBasicResponse cancelScreeningTicket(int purchaseId) {
        return client.request(new CancelScreeningTicketRequest(purchaseId), SertiaBasicResponse.class);
    }

    public StreamingPaymentResponse purchaseStreaming(StreamingPaymentRequest request) {
        return client.request(request, StreamingPaymentResponse.class);
    }

    public SertiaBasicResponse cancelStreamingTicket(int purchaseId) {
        return client.request(new CancelStreamingTicketRequest(purchaseId), StreamingPaymentResponse.class);
    }

    public VoucherPaymentResponse purchaseVoucher(BasicPriceChangeRequest request) {
        return client.request(request, VoucherPaymentResponse.class);
    }

    public VoucherBalanceResponse requestVoucherBalance(int voucherId) {
        return client.request(new VoucherBalanceRequest(voucherId), VoucherBalanceResponse.class);
    }
}