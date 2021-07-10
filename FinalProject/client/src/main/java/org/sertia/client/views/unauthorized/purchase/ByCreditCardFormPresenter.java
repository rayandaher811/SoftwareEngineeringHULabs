package org.sertia.client.views.unauthorized.purchase;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.*;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithCovidRequest;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithSeatsRequest;
import org.sertia.contracts.screening.ticket.request.StreamingPaymentRequest;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;
import org.sertia.contracts.screening.ticket.response.StreamingPaymentResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ByCreditCardFormPresenter extends BasicPresenterWithValidations implements Initializable {

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
    public Label expirationLabel;
    public Label monthLabel;
    public VBox monthVbox;
    public HBox expirationHbox;
    public VBox yearVbox;
    public Label yearLabel;

    private boolean isBuyingStreamingLink;

    @FXML
    public void back() {
        try {
            if (isBuyingStreamingLink) {
                App.setRoot("unauthorized/onlineLink/onlineWatchLinkForm");
            } else {
                App.setRoot("unauthorized/payment/selectionMethodForm");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void purchaseWithCovid() {
        ScreeningTicketWithCovidRequest request = new ScreeningTicketWithCovidRequest(cardHolderId.getText(),
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

        if (response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.INFORMATION, "Buying from sertia system", buildSuccessfulScreeningTicketPurchasingMessage(response));
            try {
                App.setRoot("unauthorized/primary");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Buying from sertia system", response.failReason);
        }
    }

    private void purchaseInNormalTime() {
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
        
        if (response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.INFORMATION, "Buying from sertia system", buildSuccessfulScreeningTicketPurchasingMessage(response));
            try {
                App.setRoot("unauthorized/primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Buying from sertia system", response.failReason);
        }
    }

    private void purchaseStreamingLink() {
        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        BuyOnlineScreeningLinkDataHolder buyOnlineScreeningLinkDataHolder = BuyOnlineScreeningLinkDataHolder.getInstance();
        if (buyOnlineScreeningLinkDataHolder.isInitialized()) {
            StreamingPaymentRequest streamingPaymentRequest =
                    new StreamingPaymentRequest(cardHolderId.getText(),
                            cardHolderName.getText(),
                            creditCardNumber.getText(),
                            cardHolderEmailTxt.getText(),
                            cardHolderPhoneTxt.getText(),
                            cvv.getText(),
                            buyOnlineScreeningLinkDataHolder.getStartDateTime(),
                            movie.getMovieId(),
                            buyOnlineScreeningLinkDataHolder.getStartDateTime(),
                            buyOnlineScreeningLinkDataHolder.getNumberOfDaysForRental());
            StreamingPaymentResponse response = ClientPurchaseControl.getInstance().purchaseStreaming(streamingPaymentRequest);

            if (response.isSuccessful) {
                String msg = "You streaming link info:\n" +
                                "Streaming link: " + response.streamingLink + "\n" +
                        "Start time - End time: " + response.startTime + " - " + response.endTime;
                Utils.popAlert(Alert.AlertType.INFORMATION, "Buying streaming link", msg);
                BuyOnlineScreeningLinkDataHolder.getInstance().clear();
                try {
                    App.setRoot("unauthorized/primary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Buying from sertia system", response.failReason);
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Fatal server error", "client data for streaming link is not set");
        }
    }

    @FXML
    public void pay() {
        if (isInputValid()) {
            if (isBuyingStreamingLink) {
                purchaseStreamingLink();
            } else if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
                purchaseWithCovid();
            } else {
                purchaseInNormalTime();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (BuyOnlineScreeningLinkDataHolder.getInstance().isInitialized()) {
            cardHolderName.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getClientName());
            cardHolderName.setEditable(false);
            cardHolderEmailTxt.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getEmail());
            cardHolderEmailTxt.setEditable(false);
            cardHolderPhoneTxt.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getPhone());
            cardHolderPhoneTxt.setEditable(false);
        }
        topLabel.setFocusTraversable(true);
        creditCardProviderCombo.getItems().addAll(List.of(CreditCardProvider.values()));
        initializeMonthCombo();
        initializeYearCombo();
        isBuyingStreamingLink = MovieHolder.getInstance().isOnlineLinkPurchaseRequest();
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

    private boolean isCvvCorrect() {
        if (cvv.getText() == null || cvv.getText().isBlank() || cvv.getText().isEmpty() || cvv.getText().length() != 3) {
            userMistakes.add("Please fill your cvv, it's in size 3 exactly");
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

    private String buildSuccessfulScreeningTicketPurchasingMessage(ScreeningPaymentResponse response){
        String msg =  "You've purchased a screening ticket/s and here is it's info:\n"+
                "Cinema name: " + response.cinemaName + "\n" +
                "Movie name: " + response.movieName + "\n" +
                "Hall number: " + response.hallNumber + "\n" +
                "Screening time: " + response.screeningTime + "\n" +
                "Tickets : <Ticket Id> = <row, number in row>\n";

        // Inserting it's tickets info
        for (Map.Entry<Integer, HallSeat> set:
                response.ticketIdToSeat.entrySet()) {
            msg += set.getKey() + " = " +set.getValue().row + ", " + set.getValue().getNumberInRow() + "\n";
        }

        return msg;
    }
}