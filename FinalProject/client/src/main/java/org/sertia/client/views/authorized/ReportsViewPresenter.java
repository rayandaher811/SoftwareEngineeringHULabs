package org.sertia.client.views.authorized;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.sertia.client.controllers.ClientReportsControl;
import org.sertia.client.global.LoggedInUser;
import org.sertia.contracts.reports.response.ClientReportsResponse;
import org.sertia.contracts.user.login.UserRole;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportsViewPresenter implements Initializable {
    public void back(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserRole userRole = LoggedInUser.getInstance().getUserRole();
        ClientReportsResponse response = null;
        if (userRole == UserRole.BranchManager) {
            // TODO: how to know cinema id, it's not related to role
            response = ClientReportsControl.getInstance().getCinemaReports(1);
        } else if (userRole == UserRole.CinemaManager) {
            response = ClientReportsControl.getInstance().getSertiaReports();
        }

        if (!response.isSuccessful) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error fetching data from sertia server");
            errorAlert.setContentText(response.failReason);
            errorAlert.showAndWait();
        } else {
            System.out.println("BGBG");
        }
    }
}
