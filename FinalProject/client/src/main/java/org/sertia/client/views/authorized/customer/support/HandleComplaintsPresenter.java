package org.sertia.client.views.authorized.customer.support;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientComplaintControl;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static org.sertia.client.Constants.*;

public class HandleComplaintsPresenter implements Initializable {

    public Button backBtn;
    public Accordion complaintsAccordion;

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TitledPane clientOpenComplaintToTiledPane(ClientOpenComplaint clientOpenComplaint) {
        TitledPane titledPane = new TitledPane();
        HBox hBox = new HBox();

        VBox resolveOrDeclineVBox = new VBox();
        resolveOrDeclineVBox.setAlignment(Pos.CENTER);
        HBox declineResolveHbox = new HBox();
        declineResolveHbox.setAlignment(Pos.CENTER);
        TextField refundAmount = new TextField();
        refundAmount.setPromptText(REFUND_EMOUNT);
        refundAmount.setAlignment(Pos.CENTER);
        Button resolveBtn = new Button();
        resolveBtn.setText(RESOLVE_COMPLAINT_BTN_TXT);
        resolveBtn.setMinWidth(100);
        resolveBtn.setMaxWidth(100);
        resolveBtn.setStyle("-fx-background-color: #00ff00");
        Button declineBtn = new Button();
        declineBtn.setText(REJECT_COMPLAINT_BTN_TXT);
        declineBtn.setMinWidth(100);
        declineBtn.setMaxWidth(100);
        declineBtn.setStyle("-fx-background-color: #ff1500");
        declineResolveHbox.getChildren().addAll(resolveBtn, declineBtn);
        resolveOrDeclineVBox.getChildren().addAll(declineResolveHbox, refundAmount);
        declineBtn.setOnMouseClicked(mouseEvent -> resolveComplaint(false, clientOpenComplaint.complaintId, refundAmount.getText()));
        resolveBtn.setOnMouseClicked(mouseEvent -> resolveComplaint(true, clientOpenComplaint.complaintId, refundAmount.getText()));

        VBox vBox = new VBox();
        Label clientName = new Label();
        clientName.setText(clientOpenComplaint.customerName);
        Label customerPhoneNumber = new Label();
        customerPhoneNumber.setText(clientOpenComplaint.customerPhoneNumber);
        Label customerEmail = new Label();
        customerEmail.setText(clientOpenComplaint.customerEmail);
//        Label creationDate = new Label();
//        creationDate.setText(clientOpenComplaint.creationDate.toString());
        Label description = new Label();
        description.setText(clientOpenComplaint.description);
        Label ticketType = new Label();
        ticketType.setText(clientOpenComplaint.ticketType.name());
        vBox.getChildren().addAll(clientName, customerPhoneNumber, customerEmail, description, ticketType);
        hBox.getChildren().addAll(vBox, resolveOrDeclineVBox);
        titledPane.setContent(hBox);
        titledPane.setText(clientOpenComplaint.customerName);
        return titledPane;
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    private void resolveComplaint(boolean isResolved, int complaintId, String refundAmount) {
        SertiaBasicResponse response = null;
        if (isResolved) {
            if (refundAmount != null && isNumber(refundAmount)) {
                response = ClientComplaintControl.getInstance().tryCloseComplaint(complaintId);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Refund amount error");
                errorAlert.setContentText("Please insert a number for refund");
                errorAlert.showAndWait();
            }
        } else {
            response = ClientComplaintControl.getInstance().tryCloseComplaint(complaintId);
        }

        if (response != null) {
            if (response.isSuccessful) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Refund process alert");
                errorAlert.setContentText("Closed complaint successfully!");
                errorAlert.showAndWait();
                back();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Refund process error");
                errorAlert.setContentText(response.failReason);
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ClientOpenComplaint> openedComplaints = ClientComplaintControl.getInstance().getOpenedComplaints();

        openedComplaints.add(getComplaint("bg"));
        openedComplaints.add(getComplaint("gg"));
        openedComplaints.add(getComplaint("aa"));
        openedComplaints.add(getComplaint("yy"));

        openedComplaints.forEach(openedComplaint ->
                complaintsAccordion.getPanes().add(clientOpenComplaintToTiledPane(openedComplaint)));
    }


    private ClientOpenComplaint getComplaint(String customerName) {
        return new ClientOpenComplaint(customerName, "0542256705", "bg@gmail.com", "dfkasodmasdmaskimd", 2, ClientTicketType.Screening);
    }
}
