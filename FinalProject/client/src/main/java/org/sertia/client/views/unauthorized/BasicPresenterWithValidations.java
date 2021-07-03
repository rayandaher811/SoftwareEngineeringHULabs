package org.sertia.client.views.unauthorized;

import javafx.scene.control.Alert;
import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;
import org.sertia.contracts.screening.ticket.request.CreditCardProvider;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasicPresenterWithValidations extends BasicPresenter {
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
            userMistakes.add("Invalid email. it must contain latin characters");
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

    protected boolean isPurchaseIdValid(String purchaseId) {
        return isItNumber(purchaseId, "Invalid purchase id, must be a number");
    }

    protected boolean isPhoneValid(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{10}$");

        if (isStringMatchesPattern(phoneNumber, p)) {
            return true;
        } else {
            userMistakes.add("Invalid phone number, expecting 10 digits number, without -");
            return false;
        }
    }

    protected boolean isNameValid(String userName) {
        String regex = "^[A-Za-z]\\w{5,29}$";

        Pattern p = Pattern.compile(regex);

        if (isStringMatchesPattern(userName, p)) {
            return true;
        } else {
            userMistakes.add("Invalid user name, expecting user name longer than 5 shorter than 29, without numbers or spaces");
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
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Input validations failure");
        errorAlert.setContentText(String.join("\n", userMistakes));
        errorAlert.show();
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
            userMistakes.add("Please re-type your credit card number and provider");
            return false;
        }
    }
}
