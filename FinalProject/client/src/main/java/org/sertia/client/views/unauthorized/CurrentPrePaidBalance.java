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

import static org.sertia.client.Constants.PREPAID_BALANCE_CHECK;
import static org.sertia.client.Constants.PREPAID_BALANCE_CHECK_FAILED;

public class CurrentPrePaidBalance extends BasicPresenterWithValidations implements Initializable {

    public TextField voucherIdTxt;
    public TextField voucherBuyerId;
    public TextField balanceTxt;

    @FXML
    private void checkBalance() {
        if (isInputValid()) {
            VoucherBalanceResponse response =
                    ClientPurchaseControl.getInstance().requestVoucherBalance(Integer.parseInt(voucherIdTxt.getText()), voucherBuyerId.getText());
            if (!response.isSuccessful) {
                balanceTxt.setText("");
                Utils.popAlert(Alert.AlertType.ERROR, PREPAID_BALANCE_CHECK, PREPAID_BALANCE_CHECK_FAILED + response.failReason);
            } else {
                balanceTxt.setText(String.valueOf(response.balance));
            }
        }
    }

    @FXML
    private void back() {
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
        return isPurchaseIdValid(voucherIdTxt.getText()) &&
                isIdCorrect(voucherBuyerId.getText());
    }
}
