package org.sertia.client.views.unauthorized;

import javafx.scene.control.Alert;
import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;

import java.util.ArrayList;
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

    protected boolean isPurchaseIdValid(String purchaseId) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);

        if (isStringMatchesPattern(purchaseId, p)) {
            return true;
        } else {
            userMistakes.add("Invalid purchase id, must be a number");
            return false;
        }
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
}
