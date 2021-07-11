package org.sertia.client.views.authorized.sertia.manager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.controllers.ClientPriceChangeControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.response.GetMovieByIdResponse;
import org.sertia.contracts.price.change.ClientTicketType;
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

    public void renderForm(boolean isRender) {
        priceChangeRequestsAccordion.getPanes().clear();
        GetUnapprovedPriceChangeResponse response =
                ClientPriceChangeControl.getInstance().getAllOpenedPriceChangeRequests();

        if (response.isSuccessful) {
            initializeForm(response.unapprovedRequests, isRender);
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, APPROVE_PRICE_CHANGE_REQUEST, response.failReason);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderForm(false);
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
            responseToChangeRequest(false, priceChangeRequest.requestId);
        });
        resolveBtn.setOnMouseClicked(mouseEvent -> {
            responseToChangeRequest(true, priceChangeRequest.requestId);
        });


        VBox vBox = new VBox();
        // TODO: get movie name from ID
        Label movieName = new Label();

        if (priceChangeRequest.clientTicketType != ClientTicketType.Voucher) {
            GetMovieByIdResponse response = ClientCatalogControl.getInstance().requestMovieById(priceChangeRequest.movieId);
            if (!response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.ERROR, FETCH_MOVIE_ERROR, response.failReason);
            } else {
                movieName.setText(String.valueOf(response.movie.getName()));
            }

            // Adding the movie name box
            Label movieIdTitle = new Label();
            movieIdTitle.setText(MOVIE_NAME);
            HBox movieIdVerticalBox = getHboxForData(movieIdTitle, movieName);
            vBox.getChildren().addAll(movieIdVerticalBox);
        }

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
        vBox.getChildren().addAll(requestIdVerticalBox, newPriceVerticalBox, ticketTypeVbox);
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

    private void responseToChangeRequest(boolean isApproved, int requestId) {
        SertiaBasicResponse response = null;
        if (isApproved) {
            response = ClientPriceChangeControl.getInstance().tryApprovePriceChange(requestId);
        } else {
            response = ClientPriceChangeControl.getInstance().tryDisapprovePriceChange(requestId);
        }

        if (response != null && response.isSuccessful) {
            if(isApproved) {
                Utils.popAlert(Alert.AlertType.INFORMATION, APPROVE_PRICE_CHANGE_REQUEST, PRICE_CHANGE_REQUEST_APPROVAL_FINISHED_SUCCESSFULLY);
            } else {
                Utils.popAlert(Alert.AlertType.INFORMATION, APPROVE_PRICE_CHANGE_REQUEST, PRICE_CHANGE_REQUEST_DISAPPROVAL_FINISHED_SUCCESSFULLY);
            }
            renderForm(true);
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, APPROVE_PRICE_CHANGE_REQUEST, response.failReason);
        }
    }

    private void initializeForm(List<BasicPriceChangeRequest> requests, boolean isRender) {
        if (requests.isEmpty()) {
            if (isRender) {
                noRequestsToApproveLabel.setText(NO_NEW_REQUESTS);
            }
            noRequestsToApproveLabel.setVisible(true);
            priceChangeRequestsAccordion.setVisible(false);
        } else {
            requests.forEach(basicPriceChangeRequest ->
                    priceChangeRequestsAccordion.getPanes().add(parsePriceChangeRequest(basicPriceChangeRequest)));
        }
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
