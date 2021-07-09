package org.sertia.client.views.unauthorized.purchase;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
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
                    Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                    missingKeyFieldsAlert.setTitle("Voucher balance overflow");
                    missingKeyFieldsAlert.setContentText("Sorry, you don't have enough tickets left on this voucher, current balance is: " + currentBalance + ", you requested to buy " + Integer.parseInt(voucherIdTxt.getText() + " tickets"));
                    missingKeyFieldsAlert.show();
                } else {
                    // TODO: complete purchase
                }
            } else {
                Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                missingKeyFieldsAlert.setTitle("Voucher balance check");
                missingKeyFieldsAlert.setContentText("Failed to get balance for your voucher, " + response.failReason);
                missingKeyFieldsAlert.show();
            }

        } else {
            notifyClient();
        }
    }

    private void notifyClient() {
        Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
        missingKeyFieldsAlert.setTitle("Payment with voucher");
        missingKeyFieldsAlert.setContentText("Please insert valid voucher id");
        missingKeyFieldsAlert.show();
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
    /*
        @FXML
    private void checkBalance(){
        if (isInputValid()) {

            if (!response.isSuccessful) {
                Alert.AlertType type;
                String msg = "";
                if (response.isSuccessful){
                    type = Alert.AlertType.INFORMATION;
                    msg = "Prepaid tickets bought successfully!";
                } else {
                    type = Alert.AlertType.ERROR;
                    msg = response.failReason;
                }
                Alert errorAlert = new Alert(type);
                errorAlert.setTitle("Buying prepaid tickets from sertia system");
                errorAlert.setContentText(msg);
                errorAlert.showAndWait();
            } else {
                balanceTxt.setText(String.valueOf(response.balance));
            }
        }
    }
     */
}
