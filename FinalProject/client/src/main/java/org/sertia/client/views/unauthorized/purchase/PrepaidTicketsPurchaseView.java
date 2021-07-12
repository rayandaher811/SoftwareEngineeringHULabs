package org.sertia.client.views.unauthorized.purchase;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;
import org.sertia.contracts.screening.ticket.request.VoucherPurchaseRequest;
import org.sertia.contracts.screening.ticket.response.GetVoucherInfoResponse;
import org.sertia.contracts.screening.ticket.response.VoucherPaymentResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class PrepaidTicketsPurchaseView extends ByCreditCardFormPresenter {

    public TextField cardHolderPhoneTxt;
    public TextField cardHolderEmailTxt;
    public TextField cardHolderName;
    public TextField cardHolderId;
    public ComboBox creditCardProviderCombo;
    public ComboBox expirationMonthCombo;
    public ComboBox expirationYearCombo;
    public Label topLabel;
    public TextField futureBalanceTxt;
    public TextField voucherPrice;
    public Label futurePrice;
    public Label futureBalanceLabel;

    @FXML
    public void toMain() throws IOException {
        App.setRoot("unauthorized/primary");
    }

    @FXML
    public void pay() {
        if (isInputValid()) {
            VoucherPurchaseRequest request = new VoucherPurchaseRequest(cardHolderId.getText(),
                    cardHolderName.getText(),
                    creditCardNumber.getText(),
                    cardHolderEmailTxt.getText(),
                    cardHolderPhoneTxt.getText(),
                    cvv.getText(),
                    LocalDateTime.of(Integer.parseInt(expirationYearCombo.getSelectionModel().getSelectedItem().toString()),
                            Integer.parseInt(expirationMonthCombo.getSelectionModel().getSelectedItem().toString()),
                            1, 0, 0));
            VoucherPaymentResponse response = ClientPurchaseControl.getInstance().purchaseVoucher(request);
            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, BUYING_PREPAID_TICKETS_FROM_SERTIA, VOUCHER_ID + response.voucherId);
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, BUYING_PREPAID_TICKETS_FROM_SERTIA, response.failReason);
            }

            try {
                App.setRoot("unauthorized/primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topLabel.setFocusTraversable(true);
        creditCardProviderCombo.getItems().addAll(List.of(CreditCardProvider.values()));
        expirationMonthCombo.getItems().addAll(MONTHS);
        expirationYearCombo.getItems().addAll(YEARS);
        GetVoucherInfoResponse response = ClientPurchaseControl.getInstance().getVouchersInfo();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, VOUCHER_INFO_ERROR, COULDNT_FETCH_VOUCHER_DATA + response.failReason);
        } else {
            futureBalanceTxt.setVisible(true);
            futureBalanceLabel.setVisible(true);
            futureBalanceTxt.setText(String.valueOf(response.initialBalance));
            voucherPrice.setVisible(true);
            futurePrice.setVisible(true);
            voucherPrice.setText(String.valueOf(response.price));
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isCardHolderNameValid = isFullNameValid(cardHolderName.getText());
        boolean isCreditCardProviderValid = isCreditCardProviderCorrect();
//        boolean isCreditCardCorrect = false;
        if (isCreditCardProviderValid) {
//            isCreditCardCorrect = isCreditCardCorrect(creditCardNumber.getText(),
//                    CreditCardProvider.valueOf(creditCardProviderCombo.getSelectionModel().getSelectedItem().toString()));
        }
        boolean isCvvCorrect = isCvvCorrect();
        boolean isEmailCorrect = isEmailValid(cardHolderEmailTxt.getText());
        boolean isPhoneCorrcet = isPhoneValid(cardHolderPhoneTxt.getText());
        boolean isIdNumberCorrect = isIdCorrect(cardHolderId.getText());
        return isCardHolderNameValid && isCreditCardProviderValid /*&& isCreditCardCorrect*/
                && isCvvCorrect && isEmailCorrect && isPhoneCorrcet && isIdNumberCorrect;
    }
}
