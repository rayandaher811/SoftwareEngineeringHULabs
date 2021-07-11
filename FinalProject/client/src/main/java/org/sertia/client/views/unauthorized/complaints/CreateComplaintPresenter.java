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
import org.sertia.client.views.TicketType;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.sertia.client.Constants.*;

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
    public ComboBox<TicketType> ticketTypeField;

    @FXML
    public void toMainMenu() {
        try {
            App.setRoot("unauthorized/primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void publishComplaint() {
        if (!userMistakes.isEmpty())
            userMistakes.clear();
        if (isInputValid()) {
            SertiaBasicResponse response = ClientComplaintControl.getInstance()
                    .tryCreateComplaint(nameTxtField.getText(),
                            phoneTxTextField.getText(),
                            emailTxTextField.getText(),
                            complaintData.getText(),
                            Integer.parseInt(purchaseIdTextField.getText()),
                            ticketTypeField.getSelectionModel().getSelectedItem().ticketType,
                            clientIdTxt.getText());

            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, CREATE_COMPLAINT, COMLAINT_SENT_SUCCESSFULLY);
                try {
                    App.setRoot("unauthorized/primary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, CREATE_COMPLAINT, response.failReason);
            }
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isStringNotEmpty(nameTxtField.getText(), FULL_NAME_EXCEPTION_EXPLANATION);
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        boolean isPurchaseIdValid = isPurchaseIdValid(purchaseIdTextField.getText());
        boolean isComplaintValid = isStringNotEmpty(complaintData.getText(), COMPLAINT_DESCRIPTION_MISSING);
        boolean isIdValid = isIdCorrect(clientIdTxt.getText());
        return isNameValid && isPhoneValid && isEmailValid &&
                isPurchaseIdValid  && isComplaintValid && isIdValid;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TicketType> ticketTypes = TicketType.getTypes();
        ticketTypeField.setItems(ticketTypes);
        userMistakes = new ArrayList<>();
    }
}