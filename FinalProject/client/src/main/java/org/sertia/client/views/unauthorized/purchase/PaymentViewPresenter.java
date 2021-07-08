package org.sertia.client.views.unauthorized.purchase;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.NumberOfTicketsHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.global.SeatsHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;
import org.sertia.contracts.screening.ticket.request.PaymentMethod;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithCovidRequest;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithSeatsRequest;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;
import org.sertia.contracts.screening.ticket.response.VoucherBalanceResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentViewPresenter extends BasicPresenterWithValidations implements Initializable {

    public TextField creditCardNumber;
    public TextField cardHolderName;
    public ComboBox expirationMonthCombo;
    public ComboBox expirationYearCombo;
    public ComboBox creditCardProviderCombo;
    public TextField cvv;
    public Label topLabel;
    public TextField cardHolderId;
    public TextField cardHolderEmailTxt;
    public TextField cardHolderPhoneTxt;
    public ChoiceBox paymentMehod;
    public Label expirationLabel;
    public Label monthLabel;
    public VBox monthVbox;
    public HBox expirationHbox;
    public VBox yearVbox;
    public Label yearLabel;
    public TextField voucherIdTxt;
    public TextField balanceTxt;
    public Button checkBalanceBtn;
    public Group creditCardFieldsGroup;
    public Group prepaidTicketsFieldsGroup;

    @FXML
    public void back() {
        boolean isLinkRequest = MovieHolder.getInstance().isOnlineLinkPurchaseRequest();

        try {
            if (isLinkRequest) {
                App.setRoot("unauthorized/onlineWatchLinkForm");
            } else {
                App.setRoot("unauthorized/purchaseMovieTickets");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void purchaseWithCovid() {
        ScreeningTicketWithCovidRequest request =
                new ScreeningTicketWithCovidRequest(cardHolderId.getText(),
                        cardHolderName.getText(),
                        creditCardNumber.getText(),
                        cardHolderEmailTxt.getText(),
                        cardHolderPhoneTxt.getText(),
                        cvv.getText(),
                        LocalDateTime.of(Integer.parseInt(expirationYearCombo.getSelectionModel().getSelectedItem().toString()),
                                Integer.parseInt(expirationMonthCombo.getSelectionModel().getSelectedItem().toString()),
                                1, 0, 0),
                        NumberOfTicketsHolder.getInstance().getNumberOfTickets(),
                        ScreeningHolder.getInstance().getScreening().getScreeningId());
        ScreeningPaymentResponse response =
                ClientPurchaseControl.getInstance().purchaseScreeningTicketsWithCovid(request);
        Alert.AlertType type;
        String msg = "";
        if (response.isSuccessful){
            type = Alert.AlertType.INFORMATION;
            msg = "operation ended successfully!";
        } else {
            type = Alert.AlertType.ERROR;
            msg = response.failReason;
        }
        Alert errorAlert = new Alert(type);
        errorAlert.setTitle("Buying from sertia system");
        errorAlert.setContentText(msg);
        errorAlert.showAndWait();
    }

    private void purchaseInNormalTime(){
        List<Integer> selectedSeats = SeatsHolder.getInstance().getUserSelection();
        ScreeningTicketWithSeatsRequest screeningTicketWithSeatsRequest =
                new ScreeningTicketWithSeatsRequest(cardHolderId.getText(),
                        cardHolderName.getText(),
                        creditCardNumber.getText(),
                        cardHolderEmailTxt.getText(),
                        cardHolderPhoneTxt.getText(),
                        cvv.getText(),
                        LocalDateTime.of(Integer.parseInt(expirationYearCombo.getSelectionModel().getSelectedItem().toString()),
                                Integer.parseInt(expirationMonthCombo.getSelectionModel().getSelectedItem().toString()),
                                1, 0, 0),
                        selectedSeats,
                        ScreeningHolder.getInstance().getScreening().getScreeningId());
        ScreeningPaymentResponse response =
                ClientPurchaseControl.getInstance().purchaseScreeningTicketsWithSeats(screeningTicketWithSeatsRequest);
        Alert.AlertType type;
        String msg = "";
        if (response.isSuccessful){
            type = Alert.AlertType.INFORMATION;
            msg = "operation ended successfully!";
        } else {
            type = Alert.AlertType.ERROR;
            msg = response.failReason;
        }
        Alert errorAlert = new Alert(type);
        errorAlert.setTitle("Buying from sertia system");
        errorAlert.setContentText(msg);
        errorAlert.showAndWait();

    }

    @FXML
    public void pay() {
        if (isInputValid()) {
            if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
                purchaseWithCovid();
            } else {
                purchaseInNormalTime();
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

        paymentMehod.setFocusTraversable(false);
        paymentMehod.getItems().addAll(Arrays.stream(PaymentMethod.values()).map(paymentMethod -> paymentMethod.label).collect(Collectors.toList()));

        paymentMehod.getSelectionModel().selectedIndexProperty().addListener(this::onChange);
        creditCardFieldsGroup.setVisible(false);
        prepaidTicketsFieldsGroup.setVisible(false);

    }

    private void setPrepaidTicketsFieldsVisibility(boolean requestedVisibility) {
        voucherIdTxt.setVisible(requestedVisibility);
        checkBalanceBtn.setVisible(requestedVisibility);
        checkBalanceBtn.setVisible(requestedVisibility);
    }
    private void setCreditCardsFieldsVisibility(boolean requestedVisibility) {
        cardHolderName.setVisible(requestedVisibility);
        cardHolderId.setVisible(requestedVisibility);
        cardHolderEmailTxt.setVisible(requestedVisibility);
        cardHolderPhoneTxt.setVisible(requestedVisibility);
        creditCardNumber.setVisible(requestedVisibility);
        creditCardProviderCombo.setVisible(requestedVisibility);
        expirationLabel.setVisible(requestedVisibility);
        monthVbox.setVisible(requestedVisibility);
        monthLabel.setVisible(requestedVisibility);
        expirationMonthCombo.setVisible(requestedVisibility);
        yearVbox.setVisible(requestedVisibility);
        yearLabel.setVisible(requestedVisibility);
        expirationYearCombo.setVisible(requestedVisibility);
        cvv.setVisible(requestedVisibility);
    }

    private void onChange(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        PaymentMethod chosenPaymentMethod = PaymentMethod.values()[t1.intValue()];
        switch (chosenPaymentMethod){
            case CREDIT_CARD -> onCreditCardSelection();
            case PREPAID_TICKET -> onPrepaidSelection();
        }
    }

    private void onCreditCardSelection() {
        creditCardFieldsGroup.setVisible(true);
        prepaidTicketsFieldsGroup.setVisible(false);
    }

    private void onPrepaidSelection() {
        creditCardFieldsGroup.setVisible(false);
        prepaidTicketsFieldsGroup.setVisible(true);
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

    @FXML
    private void checkBalance(){
        if (isInputValid()) {
            VoucherBalanceResponse response =
                    ClientPurchaseControl.getInstance().requestVoucherBalance(Integer.parseInt(voucherIdTxt.getText()));
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
}