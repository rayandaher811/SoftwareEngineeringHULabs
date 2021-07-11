package org.sertia.client.controllers;

import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.*;

import java.util.Collections;

public class ClientPurchaseControl extends ClientControl {

    private static final ClientPurchaseControl instance = new ClientPurchaseControl();

    public static ClientPurchaseControl getInstance() {
        return instance;
    }

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

    public TicketCancellationResponse cancelScreeningTicket(int purchaseId, String userId) {
        return client.request(new CancelScreeningTicketRequest(purchaseId, userId), TicketCancellationResponse.class);
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

    public TicketCancellationResponse cancelStreamingTicket(int purchaseId, String userId) {
        return client.request(new CancelStreamingTicketRequest(purchaseId, userId), TicketCancellationResponse.class);
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

    public VoucherBalanceResponse requestVoucherBalance(int voucherId, String clientId) {
        VoucherBalanceResponse response = client.request(new VoucherBalanceRequest(voucherId, clientId), VoucherBalanceResponse.class);
        if (response.isSuccessful) {
            return response;
        }

        VoucherBalanceResponse failedResponse = new VoucherBalanceResponse(false);
        failedResponse.failReason = response.failReason;

        return failedResponse;
    }

    public GetVoucherInfoResponse getVouchersInfo(){
        return client.request(new GetVoucherInfoRequest(), GetVoucherInfoResponse.class);
    }

    private ScreeningPaymentResponse failedScreeningPaymentResponse(String reason) {
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(false);
        response.failReason = reason;

        return response;
    }
}