package org.sertia.client.controllers;

import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.screening.ticket.response.ClientSeatMapResponse;

public class ClientPurchaseControl extends ClientControl {

    public void getScreeningSeatMap(String screeningId) {
        // TODO - implement ClientPurchaseControl.getScreeningSeatMap
        throw new UnsupportedOperationException();
    }

    public void onPaymentResponse(int ClientPaymentResponse) {
        // TODO - implement ClientPurchaseControl.onPaymentResponse
        throw new UnsupportedOperationException();
    }

    public void cancelScreeningTicket(int purcahseId) {
        // TODO - implement ClientPurchaseControl.cancelScreeningTicket
        throw new UnsupportedOperationException();
    }

    public void purchaseStreaming(int ClientStreamingPaymentRequest) {
        // TODO - implement ClientPurchaseControl.purchaseStreaming
        throw new UnsupportedOperationException();
    }

    public void cancelStreamingTicket(int purchaseId) {
        // TODO - implement ClientPurchaseControl.cancelStreamingTicket
        throw new UnsupportedOperationException();
    }

    public void purchaseVoucher(BasicPriceChangeRequest payment) {
        // TODO - implement ClientPurchaseControl.purchaseVoucher
        throw new UnsupportedOperationException();
    }

    public void requestVoucherBalance(String voucherId) {
        // TODO - implement ClientPurchaseControl.requestVoucherBalance
        throw new UnsupportedOperationException();
    }

    public void useVoucher(String voucherId) {
        // TODO - implement ClientPurchaseControl.useVoucher
        throw new UnsupportedOperationException();
    }
}