package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.*;

import java.util.Collections;

public class ClientPurchaseControl extends ClientControl {

    public ClientSeatMapResponse getScreeningSeatMap(int screeningId) {
        ClientSeatMapResponse response = client.request(new GetScreeningSeatMap(screeningId), ClientSeatMapResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        ClientSeatMapResponse failedResponse = new ClientSeatMapResponse(false, Collections.emptyList());
        failedResponse.failReason = "קבלת תמונת מושבים נכשלה";

        return failedResponse;
    }

    public ScreeningPaymentResponse purchaseScreeningTicketsWithSeats(ScreeningTicketWithSeatsRequest request) {
        ScreeningPaymentResponse response = client.request(request, ScreeningPaymentResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        return failedScreeningPaymentResponse(response.failReason);
    }

    public ScreeningPaymentResponse purchaseScreeningTicketsWithCovid(ScreeningTicketWithCovidRequest request) {
        ScreeningPaymentResponse response = client.request(request, ScreeningPaymentResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        return failedScreeningPaymentResponse(response.failReason);
    }

    public SertiaBasicResponse cancelScreeningTicket(int purchaseId) {
        return client.request(new CancelScreeningTicketRequest(purchaseId), SertiaBasicResponse.class);
    }

    public StreamingPaymentResponse purchaseStreaming(StreamingPaymentRequest request) {
        StreamingPaymentResponse response = client.request(request, StreamingPaymentResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        StreamingPaymentResponse failedResponse = new StreamingPaymentResponse(false);
        failedResponse.failReason = response.failReason;

        return failedResponse;
    }

    public SertiaBasicResponse cancelStreamingTicket(int purchaseId) {
        return client.request(new CancelStreamingTicketRequest(purchaseId), SertiaBasicResponse.class);
    }

    public VoucherPaymentResponse purchaseVoucher(VoucherPurchaseRequest request) {
        VoucherPaymentResponse response = client.request(request, VoucherPaymentResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        VoucherPaymentResponse failedResponse = new VoucherPaymentResponse(false);
        failedResponse.failReason = response.failReason;

        return failedResponse;
    }

    public VoucherBalanceResponse requestVoucherBalance(int voucherId) {
        VoucherBalanceResponse response = client.request(new VoucherBalanceRequest(voucherId), VoucherBalanceResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        VoucherBalanceResponse failedResponse = new VoucherBalanceResponse(false);
        failedResponse.failReason = response.failReason;

        return failedResponse;
    }

    private ScreeningPaymentResponse failedScreeningPaymentResponse(String reason) {
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(false);
        response.failReason = reason;

        return response;
    }
}