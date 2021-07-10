package org.sertia.client.views.unauthorized.purchase;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.screening.ticket.response.VoucherBalanceResponse;

import java.io.IOException;

public class ByPrepaidCardFormPresenter {
    public TextField voucherIdTxt;

    public void payWithVoucher(ActionEvent actionEvent) {
        if (isInputValid()) {
            VoucherBalanceResponse response =
                    ClientPurchaseControl.getInstance().requestVoucherBalance(Integer.parseInt(voucherIdTxt.getText()));
            if (response.isSuccessful) {
                int currentBalance = response.balance;
                if (currentBalance < Integer.parseInt(voucherIdTxt.getText())) {
                    Utils.popAlert(Alert.AlertType.ERROR, "Voucher balance overflow", "Sorry, you don't have enough tickets left on this voucher, current balance is: " + currentBalance + ", you requested to buy " + Integer.parseInt(voucherIdTxt.getText() + " tickets"));
                } else {
                    // TODO: complete purchase
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Voucher balance check", "Failed to get balance for your voucher, " + response.failReason);
            }

        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Payment with voucher", "Please insert valid voucher id");
        }
    }

    private boolean isInputValid() {
        try {
            Integer.parseInt(voucherIdTxt.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void toMain(ActionEvent actionEvent) {
        try {
            App.setRoot("unauthorized/payment/selectionMethodForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
