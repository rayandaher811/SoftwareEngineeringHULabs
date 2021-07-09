package org.sertia.client.views.authorized;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.global.LoggedInUser;
import org.sertia.contracts.user.login.UserRole;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class EmployeesFormPresenter implements Initializable {

    @FXML
    private VBox buttonsVbox;
    @FXML
    private Label topLabel;

    @FXML
    private void logOut() throws IOException {
        LoggedInUser.onDisconnection();
        App.setRoot("unauthorized/primary");
    }

    @FXML
    private void updateStreamingTime() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    @FXML
    private void addOrRemoveMovie() throws IOException {
        App.setRoot("authorized/media.manager/addOrRemoveMovie");
    }

    @FXML
    private void priceChangeRequestsApprovals() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    @FXML
    private void editTavSagolRegulations() throws IOException {
        App.setRoot("authorized/customer.support/updateTavSagolRegulations");
    }

    @FXML
    private void statisticsView() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    @FXML
    private void handleComplaints() throws IOException {
        App.setRoot("authorized/customer.support/handleComplaints");
    }


    @FXML
    private void updateTicketsPrice() throws IOException {
        App.setRoot("authorized/media.manager/changeMovieTicketPrice");
    }

    @FXML
    private void removeMovie() throws IOException {
        App.setRoot("authorized/media.manager/removeMovie");
    }

    @FXML
    private void addOrRemoveStreaming() throws IOException {
        App.setRoot("authorized/media.manager/addOrRemoveStreaming");
    }

    @FXML
    private void addMovie() throws IOException {
        App.setRoot("authorized/media.manager/addNewMovieData");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserRole userRole = LoggedInUser.getInstance().getUserRole();
        switch (userRole) {
            case MediaManager -> initializeMediaManagerView();
            case BranchManager -> initializeSertiaManagerView();
            case CinemaManager -> initializeCinemaManagerView();
            case CostumerSupport -> initializeCustomerSupportView();
        }
        topLabel.setText(getHelloSentenceByRole(userRole) + ", " + HELLO_SENTENCE);
    }

    private String getHelloSentenceByRole(UserRole userRole) {
        switch (userRole){
            case BranchManager:
                return BRANCH_MANAGER;
            case MediaManager:
                return MEDIA_MANAGER;
            case CinemaManager:
                return CINEMA_MANAGER;
            case CostumerSupport:
                return CUSTOMER_SUPPORT;
            default:
                return "";
        }
    }

    private void initializeCinemaManagerView() {
        Button priceChangeRequestsApproval = new Button();
        priceChangeRequestsApproval.setText(PRICE_CHANGE_APPROVAL);
        priceChangeRequestsApproval.setOnMouseClicked(mouseEvent -> {
            try {
                priceChangeRequestsApprovals();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        priceChangeRequestsApproval.setMinWidth(200.0);
        Button statisticsView = new Button();
        statisticsView.setText(STATISTICS_VIEW);
        statisticsView.setMinWidth(200.0);
        statisticsView.setOnMouseClicked(mouseEvent -> {
            try {
                statisticsView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttonsVbox.getChildren().addAll(priceChangeRequestsApproval, statisticsView);
    }

    private void initializeCustomerSupportView() {
        Button priceChangeRequestsApproval = new Button();
        priceChangeRequestsApproval.setText(UPDATE_TAV_SAGOL_REGULATIONS);
        priceChangeRequestsApproval.setOnMouseClicked(mouseEvent -> {
            try {
                editTavSagolRegulations();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        priceChangeRequestsApproval.setMinWidth(200.0);
        Button statisticsView = new Button();
        statisticsView.setText(HADNLE_COMLAINTS);
        statisticsView.setMinWidth(200.0);
        statisticsView.setOnMouseClicked(mouseEvent -> {
            try {
                handleComplaints();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttonsVbox.getChildren().addAll(priceChangeRequestsApproval, statisticsView);
    }

    private void initializeSertiaManagerView() {
        Button priceChangeRequestsApproval = new Button();
        priceChangeRequestsApproval.setText(PRICE_CHANGE_APPROVAL);
        priceChangeRequestsApproval.setOnMouseClicked(mouseEvent -> {
            try {
                priceChangeRequestsApprovals();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        priceChangeRequestsApproval.setMinWidth(200.0);
        Button statisticsView = new Button();
        statisticsView.setText(STATISTICS_VIEW);
        statisticsView.setMinWidth(200.0);
        statisticsView.setOnMouseClicked(mouseEvent -> {
            try {
                statisticsView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttonsVbox.getChildren().addAll(priceChangeRequestsApproval, statisticsView);
    }

    private void initializeMediaManagerView() {
        Button editMoviesScreeningTimeBtn = new Button();
        editMoviesScreeningTimeBtn.setText(ADD_OR_DELETE_SCREENING);
        editMoviesScreeningTimeBtn.setOnMouseClicked(mouseEvent -> {
            try {
                addOrRemoveMovie();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        editMoviesScreeningTimeBtn.setMinWidth(220);
        editMoviesScreeningTimeBtn.setMaxWidth(220);
        Button addMovieBtn = new Button();
        addMovieBtn.setText(ADD_OR_DELETE_STREAMING);
        addMovieBtn.setMinWidth(220);
        addMovieBtn.setMinWidth(220);
        addMovieBtn.setOnMouseClicked(mouseEvent -> {
            try {
                addOrRemoveStreaming();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button removeMovieBtn = new Button();
        removeMovieBtn.setText(EDIT_PLAYING_TIME);
        removeMovieBtn.setMinWidth(220);
        removeMovieBtn.setMaxWidth(220);
        removeMovieBtn.setOnMouseClicked(mouseEvent -> {
            try {
                updateStreamingTime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button changeTicketsPriceBtn = new Button();
        changeTicketsPriceBtn.setText(EDIT_TICKETS_PRICE);
        changeTicketsPriceBtn.setMinWidth(220);
        changeTicketsPriceBtn.setMaxWidth(220);
        changeTicketsPriceBtn.setOnMouseClicked(mouseEvent -> {
            try {
                updateTicketsPrice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttonsVbox.getChildren().addAll(editMoviesScreeningTimeBtn, addMovieBtn, removeMovieBtn, changeTicketsPriceBtn);
    }
}