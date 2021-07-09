package org.sertia.client.views.unauthorized.purchase;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.global.SeatsHolder;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithSeatsRequest;
import org.sertia.contracts.screening.ticket.request.VoucherPurchaseRequest;
import org.sertia.contracts.screening.ticket.response.VoucherPaymentResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;

public class PrepaidTicketsPurchaseView extends PaymentViewPresenter{

    public TextField cardHolderPhoneTxt;
    public TextField cardHolderEmailTxt;
    public TextField cardHolderName;
    public TextField cardHolderId;
    public ComboBox creditCardProviderCombo;
    public ComboBox expirationMonthCombo;
    public ComboBox expirationYearCombo;
    public Label topLabel;

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
            SertiaBasicResponse response = ClientPurchaseControl.getInstance().purchaseVoucher(request);
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
        initializeMonthCombo();
        initializeYearCombo();
        boolean isLinkRequest = MovieHolder.getInstance().isOnlineLinkPurchaseRequest();
        if (isLinkRequest) {
            System.out.println("BG link");
        } else {
            System.out.println("BG phisical ticket");
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
        boolean isIdNumberCorrect = isIdCorrcet();
        return isCardHolderNameValid && isCreditCardProviderValid /*&& isCreditCardCorrect*/
                && isCvvCorrect && isEmailCorrect && isPhoneCorrcet && isIdNumberCorrect;
    }

    private boolean isCvvCorrect() {
        if (cvv.getText() == null || cvv.getText().isBlank() || cvv.getText().isEmpty() || cvv.getText().length() != 3) {
            userMistakes.add("Please fill your cvv, it's in size 3 exactly");
            return false;
        }
        return true;
    }

    private boolean isIdCorrcet() {
        if (cardHolderId.getText() == null || cardHolderId.getText().isBlank() || cardHolderId.getText().isEmpty() || cardHolderId.getText().length() != 9) {
            userMistakes.add("Please fill your ID number, it should be in length of 9");
            return false;
        }
        return true;
    }

    private boolean isCreditCardProviderCorrect() {
        if (creditCardProviderCombo.getSelectionModel().getSelectedItem() == null || creditCardProviderCombo.getSelectionModel().getSelectedItem().toString().isEmpty() || creditCardProviderCombo.getSelectionModel().getSelectedItem().toString().isBlank()) {
            userMistakes.add("Please specify credit card provider from the list");
            return false;
        }
        if (CreditCardProvider.valueOf(creditCardProviderCombo.getSelectionModel().getSelectedItem().toString()) != null) {
            return true;
        }
        return false;
    }

    private void initializeMonthCombo() {
        ObservableList<Integer> months = expirationMonthCombo.getItems();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
    }

    private void initializeYearCombo() {
        ObservableList<Integer> years = expirationYearCombo.getItems();
        for (int i = YearMonth.now().getYear(); i <= YearMonth.now().getYear() + 6; i++) {
            years.add(i);
        }
    }

    protected boolean isFullNameValid(String fullName) {
        if (fullName.split(" ").length != 2) {
            userMistakes.add("Please write down your full name, first name + last name");
            return false;
        }
        return true;
    }
}