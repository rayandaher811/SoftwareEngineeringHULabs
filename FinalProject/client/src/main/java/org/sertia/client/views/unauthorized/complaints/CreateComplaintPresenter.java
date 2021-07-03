package org.sertia.client.views.unauthorized.complaints;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientComplaintControl;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateComplaintPresenter extends BasicPresenterWithValidations implements Initializable {

    @FXML
    private TextField nameTxtField;
    @FXML
    public TextField phoneTxTextField;

    @FXML
    public TextField emailTxTextField;
    @FXML
    private TextField complaintData;
    @FXML
    public TextField purchaseIdTextField;

    @FXML
    public ComboBox ticketTypeField;


    protected ArrayList<String> userMistakes;
    @FXML
    public void toMainMenu() throws IOException {
        App.setRoot("unauthorized/primary");
    }

    @FXML
    public void publishComplaint() {
        if (!userMistakes.isEmpty())
            userMistakes.clear();

        if (isDataValid()){
            ClientComplaintControl.getInstance().tryCreateComplaint(nameTxtField.getText(),
                    phoneTxTextField.getText(), emailTxTextField.getText(), complaintData.getText(), Integer.parseInt(purchaseIdTextField.getText()), ClientTicketType.valueOf((String) ticketTypeField.getSelectionModel().getSelectedItem()));
        } else {
            maybeNotifyClient();
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isNameValid();
        boolean isPhoneValid = isPhoneValid();
        boolean isEmailValid = isEmailValid();
        boolean isPurchaseIdValid = isPurchaseIdValid();
        boolean isTicketTypeValid = isTicketTypeValid();
        boolean isComplaintValid = isComplaintValid();
        return isNameValid && isPhoneValid && isEmailValid && isPurchaseIdValid && isTicketTypeValid && isComplaintValid;
    }

    private boolean isComplaintValid() {
        String complaint = complaintData.getText();
        if (complaint == null || complaint.isBlank() || complaint.isEmpty()){
            userMistakes.add("Must write complaint.. that's the whole concept XD");
            return false;
        } else {
            return true;
        }
    }

    private boolean isStringMatchesPattern(String txt, Pattern p) {
        if (txt == null || txt.isBlank() || txt.isEmpty()){
            return false;
        }
        return p.matcher(txt).matches();
    }

    private boolean isNameValid(){
        String regex = "^[A-Za-z]\\w{5,29}$";

        Pattern p = Pattern.compile(regex);
        String userName = nameTxtField.getText();

        if (isStringMatchesPattern(userName, p)) {
            return true;
        } else {
            userMistakes.add("Invalid user name, expecting user name longer than 5 shorter than 29, without numbers or spaces");
            return false;
        }
    }

    private boolean isPhoneValid(){
        Pattern p = Pattern.compile("^\\d{10}$");

        String phoneNumber = phoneTxTextField.getText();

        if (isStringMatchesPattern(phoneNumber, p)){
            return true;
        } else {
            userMistakes.add("Invalid phone number, expecting 10 digits number, without -");
            return false;
        }
    }

    private boolean isEmailValid(){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern p = Pattern.compile(emailRegex);
        String email = emailTxTextField.getText();

        if (isStringMatchesPattern(email, p)){
            return true;
        } else {
            userMistakes.add("Invalid email. it must contain latin characters");
            return false;
        }
    }

    private boolean isPurchaseIdValid(){
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        String purchaseId = purchaseIdTextField.getText();
        if (isStringMatchesPattern(purchaseId, p)){
            return true;
        } else {
            userMistakes.add("Invalid purchase id, must be a number");
            return false;
        }
    }

    private boolean isTicketTypeValid() {
        Object ticketType = ticketTypeField.getSelectionModel().getSelectedItem();
        if (ticketType == null || ((String)ticketType).isEmpty() || ((String)ticketType).isBlank()){
            userMistakes.add("Invalid ticket type");
            return false;
        }
        return true;
    }

    private void maybeNotifyClient(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> ticketTypes = FXCollections.observableList(Arrays.stream(ClientTicketType.values()).map(clientTicketType -> clientTicketType.toString()).collect(Collectors.toList()));
        ticketTypeField.setItems(ticketTypes);
        userMistakes = new ArrayList<>();
    }
}