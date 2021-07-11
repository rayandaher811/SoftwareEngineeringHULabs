package org.sertia.client.views.unauthorized.purchase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.contracts.screening.ticket.response.TicketCancellationResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class CancelPurhcasePresenter extends BasicPresenterWithValidations implements Initializable {
    @FXML
    public TextField purchaseId;
    @FXML
    public TextField clientId;
    @FXML
    public ComboBox<PurchaseType> purchaseTypeCombo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<PurchaseType> purchaseTypes = FXCollections.observableArrayList();
        purchaseTypes.add(new PurchaseType(ClientTicketType.Screening, SCREENING));
        purchaseTypes.add(new PurchaseType(ClientTicketType.Streaming, STREAMING));
        purchaseTypeCombo.setItems(purchaseTypes);
    }

    @Override
    protected boolean isDataValid() {
        return isPurchaseIdValid(purchaseId.getText()) &&
                isIdCorrect(clientId.getText()) &&
                isPurchaseTypeValid();
    }

    @FXML
    private void backToClientsView() throws IOException {
        App.setRoot("unauthorized/primary");
    }

    @FXML
    private void cancelPurchase() {
        if (isInputValid()) {
            TicketCancellationResponse response;
            ClientPurchaseControl clientPurchaseControl = ClientPurchaseControl.getInstance();
            String requestClientId = clientId.getText();
            int requestPurchaseID = Integer.parseInt(purchaseId.getText());
            ClientTicketType purchaseType = purchaseTypeCombo.getSelectionModel().getSelectedItem().ticketType;
            if (purchaseType == ClientTicketType.Screening) {
                response = clientPurchaseControl.cancelScreeningTicket(requestPurchaseID, requestClientId);
            } else {
                response = clientPurchaseControl.cancelStreamingTicket(requestPurchaseID, requestClientId);
            }

            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, CANCEL_PURCHASE, CANCEL_PURCHASE_ENDED_SUCCESSFULLY + response.refundAmount);
                try {
                    App.setRoot("unauthorized/primary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, CANCEL_PURCHASE, response.failReason);
            }
        }
    }

    private boolean isPurchaseTypeValid() {
        if (purchaseTypeCombo.getSelectionModel().getSelectedItem() == null || purchaseTypeCombo.getSelectionModel().getSelectedItem().toString().isEmpty() || purchaseTypeCombo.getSelectionModel().getSelectedItem().toString().isBlank()) {
            userMistakes.add(PLEASE_CHOOSE_PURCHASE_OPTION);
            return false;
        }

        return true;
    }

    private class PurchaseType {
        public ClientTicketType ticketType;
        public String displayName;

        public PurchaseType(ClientTicketType ticketType, String displayName) {
            this.ticketType = ticketType;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
