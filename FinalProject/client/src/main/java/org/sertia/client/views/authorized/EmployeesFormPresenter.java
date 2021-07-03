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
    private void updateMovie() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    @FXML
    private void priceChangeRequestsApprovals() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    @FXML
    private void statisticsView() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
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
    private void addMovie() throws IOException {
        App.setRoot("authorized/media.manager/addNewMovieData");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserRole userRole = LoggedInUser.getInstance().getUserRole();
        switch (userRole) {
            case MediaManager -> initializeMediaManagerView();
            case BranchManager -> initializeSertiaManagerView();
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
        editMoviesScreeningTimeBtn.setText(EDIT_PLAYING_TIME);
        editMoviesScreeningTimeBtn.setOnMouseClicked(mouseEvent -> {
            try {
                updateMovie();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        editMoviesScreeningTimeBtn.setMinWidth(200.0);
        Button addMovieBtn = new Button();
        addMovieBtn.setText(ADD_NEW_MOVIES);
        addMovieBtn.setMinWidth(200.0);
        addMovieBtn.setOnMouseClicked(mouseEvent -> {
            try {
                addMovie();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button removeMovieBtn = new Button();
        removeMovieBtn.setText(REMOVE_MOVIES);
        removeMovieBtn.setMinWidth(200.0);
        removeMovieBtn.setOnMouseClicked(mouseEvent -> {
            try {
                removeMovie();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button changeTicketsPriceBtn = new Button();
        changeTicketsPriceBtn.setText(EDIT_TICKETS_PRICE);
        changeTicketsPriceBtn.setMinWidth(200.0);
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