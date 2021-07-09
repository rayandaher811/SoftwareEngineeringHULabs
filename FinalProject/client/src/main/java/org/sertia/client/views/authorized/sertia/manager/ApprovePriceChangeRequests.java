package org.sertia.client.views.authorized.sertia.manager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientComplaintControl;
import org.sertia.client.controllers.ClientPriceChangeControl;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.responses.GetUnapprovedPriceChangeResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class ApprovePriceChangeRequests implements Initializable {

    public Accordion priceChangeRequestsAccordion;
    public Label noRequestsToApproveLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GetUnapprovedPriceChangeResponse response =
                ClientPriceChangeControl.getInstance().getAllOpenedPriceChangeRequests();

        if (response.isSuccessful) {
            initializeForm(response.unapprovedRequests);
        } else {
            notifyUserForError(response.failReason);
        }
    }

    private TitledPane parsePriceChangeRequest(BasicPriceChangeRequest priceChangeRequest) {
        TitledPane titledPane = new TitledPane();
        HBox hBox = new HBox();

        HBox declineResolveHbox = new HBox();
        declineResolveHbox.setAlignment(Pos.CENTER);
        Button resolveBtn = new Button();
        resolveBtn.setText(APPROVE_CHANGE_REQUEST);
        resolveBtn.setMinWidth(130);
        resolveBtn.setMaxWidth(130);
        resolveBtn.setStyle("-fx-background-color: #00ff00");
        Button declineBtn = new Button();
        declineBtn.setText(DENY_CHANGE_REQUEST);
        declineBtn.setMinWidth(130);
        declineBtn.setMaxWidth(130);
        declineBtn.setStyle("-fx-background-color: #ff1500");
        declineResolveHbox.getChildren().addAll(resolveBtn, declineBtn);
        declineBtn.setOnMouseClicked(mouseEvent -> {
            responseToChangeRequest(false, priceChangeRequest.requestId, priceChangeRequest.newPrice);
        });
        resolveBtn.setOnMouseClicked(mouseEvent -> {
            responseToChangeRequest(true, priceChangeRequest.requestId, priceChangeRequest.newPrice);
        });


        VBox vBox = new VBox();
        // TODO: get movie name from ID
        Label movieId = new Label();
        movieId.setText(String.valueOf(priceChangeRequest.movieId));
        Label movieIdTitle = new Label();
        movieIdTitle.setText(MOVIE_NAME);
        HBox movieIdVerticalBox = getHboxForData(movieIdTitle, movieId);
        Label requestId = new Label();
        requestId.setText(String.valueOf(priceChangeRequest.requestId));
        Label requestIdTitle = new Label();
        requestIdTitle.setText(REQUEST_ID);
        HBox requestIdVerticalBox = getHboxForData(requestIdTitle, requestId);
        Label newPrice = new Label();
        newPrice.setText(String.valueOf(priceChangeRequest.newPrice));
        Label newPriceTitle = new Label();
        newPriceTitle.setText(NEW_PRICE);
        HBox newPriceVerticalBox = getHboxForData(newPriceTitle, newPrice);
        newPrice.setText(String.valueOf(priceChangeRequest.newPrice));

        Label ticketType = new Label();
        ticketType.setText(TICKET_TYPE_TRANSLATIONS.get(priceChangeRequest.clientTicketType.name()));
        Label ticketTypeTitle = new Label();
        ticketTypeTitle.setText(TICKET_TYPE);
        HBox ticketTypeVbox = getHboxForData(ticketTypeTitle, ticketType);
        vBox.getChildren().addAll(movieIdVerticalBox, requestIdVerticalBox, newPriceVerticalBox, ticketTypeVbox);
        hBox.getChildren().addAll(vBox, declineResolveHbox);
        titledPane.setContent(hBox);
        titledPane.setText(priceChangeRequest.userName);
        return titledPane;
    }

    private HBox getHboxForData(Label title, Label value) {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(value, title);
        return hBox;
    }

    private void responseToChangeRequest(boolean isApproved, int requestId, double newPrice) {
        SertiaBasicResponse response = null;
        if (isApproved) {
            // TODO: PRICE??
            response = ClientPriceChangeControl.getInstance().tryApprovePriceChange(requestId/*, newPrice*/);
        } else {
            response = ClientPriceChangeControl.getInstance().tryDisapprovePriceChange(requestId);
        }

        if (response != null && response.isSuccessful) {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setTitle("Price change request approval");
            errorAlert.setContentText("Price change request has been approved and successfully changed price!");
            errorAlert.showAndWait();
            back();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Price change request approval");
            errorAlert.setContentText(response.failReason);
            errorAlert.showAndWait();
        }
    }

    private void initializeForm(List<BasicPriceChangeRequest> requests) {
        if (requests.isEmpty()) {
            noRequestsToApproveLabel.setVisible(true);
            priceChangeRequestsAccordion.setVisible(false);
        } else {
            requests.forEach(basicPriceChangeRequest ->
                    priceChangeRequestsAccordion.getPanes().add(parsePriceChangeRequest(basicPriceChangeRequest)));
        }
    }

    private void notifyUserForError(String failReason) {
        Alert.AlertType type = Alert.AlertType.ERROR;
        Alert errorAlert = new Alert(type);
        errorAlert.setTitle("Buying prepaid tickets from sertia system");
        errorAlert.setContentText(failReason);
        errorAlert.showAndWait();
    }

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
