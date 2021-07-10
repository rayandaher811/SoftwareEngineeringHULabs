package org.sertia.client.views.unauthorized;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.screening.ticket.response.VoucherBalanceResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CurrentPrePaidBalance extends BasicPresenterWithValidations implements Initializable {

    public TextField voucherIdTxt;
    public TextField balanceTxt;

    @FXML
    private void checkBalance() {
        if (isInputValid()) {
            VoucherBalanceResponse response =
                    ClientPurchaseControl.getInstance().requestVoucherBalance(Integer.parseInt(voucherIdTxt.getText()));
            if (!response.isSuccessful) {
                if (response.isSuccessful) {
                    Utils.popAlert(Alert.AlertType.INFORMATION, "Buying prepaid tickets from sertia system", "Prepaid tickets bought successfully!");
                } else {
                    Utils.popAlert(Alert.AlertType.ERROR, "Buying prepaid tickets from sertia system", response.failReason);
                }
            } else {
                balanceTxt.setText(String.valueOf(response.balance));
            }
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("unauthorized/primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        voucherIdTxt.setFocusTraversable(false);
        balanceTxt.setFocusTraversable(true);
    }

    @Override
    protected boolean isDataValid() {
        boolean isIdValid = isPurchaseIdValid(voucherIdTxt.getText());
        return isIdValid;
    }
}
