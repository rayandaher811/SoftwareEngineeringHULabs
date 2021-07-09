package org.sertia.client.views.unauthorized.complaints;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientComplaintControl;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CreateComplaintPresenter extends BasicPresenterWithValidations implements Initializable {

    public TextField clientIdTxt;
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

    @FXML
    public void toMainMenu() throws IOException {
        App.setRoot("unauthorized/primary");
    }

    @FXML
    public void publishComplaint() {
        if (!userMistakes.isEmpty())
            userMistakes.clear();

        if (isDataValid()){
            SertiaBasicResponse response = ClientComplaintControl.getInstance()
                    .tryCreateComplaint(nameTxtField.getText(),
                                        phoneTxTextField.getText(),
                                        emailTxTextField.getText(),
                                        complaintData.getText(),
                                        Integer.parseInt(purchaseIdTextField.getText()),
                                        ClientTicketType.valueOf((String) ticketTypeField.getSelectionModel().getSelectedItem()),
                                        clientIdTxt.getText());
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
            if (response.isSuccessful) {
                try {
                    App.setRoot("unauthorized/primary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isStringNotEmpty(nameTxtField.getText(), "Customer name is missing");
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        boolean isPurchaseIdValid = isPurchaseIdValid(purchaseIdTextField.getText());
        boolean isTicketTypeValid = isTicketTypeValid();
        boolean isComplaintValid = isStringNotEmpty(complaintData.getText(),
                "Must write complaint.. that's the whole concept XD");
        boolean isIdValid = isIdCorrcet(clientIdTxt.getText());
        return isNameValid && isPhoneValid && isEmailValid &&
                isPurchaseIdValid && isTicketTypeValid && isComplaintValid && isIdValid;
    }

    private boolean isTicketTypeValid() {
        Object ticketType = ticketTypeField.getSelectionModel().getSelectedItem();
        if (ticketType == null || ((String) ticketType).isEmpty() || ((String) ticketType).isBlank()) {
            userMistakes.add("Invalid ticket type");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> ticketTypes = FXCollections.observableList(Arrays.stream(ClientTicketType.values()).map(clientTicketType -> clientTicketType.toString()).collect(Collectors.toList()));
        ticketTypeField.setItems(ticketTypes);
        userMistakes = new ArrayList<>();
    }
}