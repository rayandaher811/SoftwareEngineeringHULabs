package org.sertia.client.views.unauthorized.purchase;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class PaymentViewPresenter extends BasicPresenterWithValidations implements Initializable {

    public TextField creditCardNumber;
    public TextField cardHolderName;
    public ComboBox expirationMonthCombo;
    public ComboBox expirationYearCombo;
    public ComboBox creditCardProviderCombo;
    public TextField cvv;
    public Label topLabel;

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

    @FXML
    public void pay() {
        if (isInputValid()) {
            System.out.println("BGBG request payment and book ");
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
        boolean isCreditCardCorrect = false;
        if (isCreditCardProviderValid) {
            isCreditCardCorrect = isCreditCardCorrect(creditCardNumber.getText(),
                    CreditCardProvider.valueOf(creditCardProviderCombo.getSelectionModel().getSelectedItem().toString()));
        }
        boolean isCvvCorrect = isCvvCorrect();
        return isCardHolderNameValid && isCreditCardProviderValid && isCreditCardCorrect && isCvvCorrect;
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
}