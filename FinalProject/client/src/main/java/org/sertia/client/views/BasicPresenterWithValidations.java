package org.sertia.client.views;

import javafx.scene.control.Alert;
import org.sertia.client.views.Utils;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.sertia.client.Constants.*;

public abstract class BasicPresenterWithValidations {
    protected ArrayList<String> userMistakes;

    protected abstract boolean isDataValid();

    protected boolean isStringMatchesPattern(String txt, Pattern p) {
        if (txt == null || txt.isBlank() || txt.isEmpty()) {
            return false;
        }
        return p.matcher(txt).matches();
    }

    protected boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern p = Pattern.compile(emailRegex);

        if (isStringMatchesPattern(email, p)) {
            return true;
        } else {
            userMistakes.add(EMAIL_EXCEPTION_EXPANATION);
            return false;
        }
    }

    protected boolean isItNumber(String expectedToBeNumber, String msg) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);

        if (isStringMatchesPattern(expectedToBeNumber, p)) {
            return true;
        } else {
            userMistakes.add(msg);
            return false;
        }
    }

    protected boolean areDatesIncremental(LocalDate from, LocalDate to) {
        if (from.isBefore(to)) {
            return true;
        } else {
            userMistakes.add(DATES_RANGE_INVALID_ERROR_MSG);
            return false;
        }
    }

    protected boolean isItDouble(String expectedToBeNumber, String msg) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);

        if (isStringMatchesPattern(expectedToBeNumber, p)) {
            return true;
        } else {
            userMistakes.add(msg);
            return false;
        }
    }

    protected boolean isPurchaseIdValid(String purchaseId) {
        return isItNumber(purchaseId, INVALID_PURCHASE_ID);
    }

    protected boolean isIdCorrect(String id) {
        if (id == null || id.isBlank() || id.length() != 9) {
            userMistakes.add(ID_EXCEPTION_EXPLANATION);
            return false;
        }
        return true;
    }

    protected boolean isPhoneValid(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{10}$");

        if (isStringMatchesPattern(phoneNumber, p)) {
            return true;
        } else {
            userMistakes.add(PHONE_VALIDATION_EXPLANATION);
            return false;
        }
    }

    protected boolean isNameValid(String userName) {
        String regex = "^[A-Za-z]\\w{5,29}$";

        Pattern p = Pattern.compile(regex);

        if (isStringMatchesPattern(userName, p)) {
            return true;
        } else {
            userMistakes.add(INVALID_USERNAME);
            return false;
        }
    }

    protected boolean isStringNotEmpty(String data, String msg) {
        if (data == null || data.isBlank() || data.isEmpty()) {
            userMistakes.add(msg);
            return false;
        } else {
            return true;
        }
    }

    protected void notifyClient() {
        Utils.popAlert(Alert.AlertType.ERROR, INPUT_VALIDATIONS_ERROR, String.join("\n", userMistakes));
    }

    protected boolean isInputValid() {
        userMistakes = new ArrayList<>();
        if (isDataValid()) {
            return true;
        } else {
            notifyClient();
            return false;
        }
    }

    protected boolean isCreditCardCorrect(String creditCardNumber, CreditCardProvider creditCardProvider) {
        String regex = "^(?:(?<VISA>4[0-9]{12}(?:[0-9]{3})?)|" +
                "(?<MASTERCARD>5[1-5][0-9]{14})|" +
                "(?<AMEX>3[47][0-9]{13})|" +
                "(?<DINERS>3(?:0[0-5]|[68][0-9])?[0-9]{11}))$";

        Pattern pattern = Pattern.compile(regex);

        //Strip all hyphens
        String numberWithoutHyphens = creditCardNumber.replaceAll("-", "");

        //Match the card
        Matcher matcher = pattern.matcher(numberWithoutHyphens);

        try {
            return matcher.group(creditCardProvider.name()) != null;
        } catch (IllegalStateException e) {
            userMistakes.add(CREDIT_CARD_FAILURE);
            return false;
        }
    }

    protected boolean isFullNameValid(String fullName) {
        if (fullName.split(" ").length != 2) {
            userMistakes.add(FULL_NAME_EXCEPTION_EXPLANATION);
            return false;
        }
        return true;
    }
}
