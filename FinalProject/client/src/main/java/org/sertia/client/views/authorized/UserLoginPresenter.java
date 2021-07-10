package org.sertia.client.views.authorized;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientUserLoginController;
import org.sertia.client.global.LoggedInUser;
import org.sertia.client.views.Utils;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.response.LoginResult;

public class UserLoginPresenter {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;

    private boolean isInputValid(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            if (userName.isEmpty() && password.isEmpty())
                Utils.popAlert(Alert.AlertType.ERROR, "Missing required fields alert", "Must insert both user name and password");
            else if (userName.isEmpty())
                Utils.popAlert(Alert.AlertType.ERROR, "Missing required fields alert", "Must insert user name");
            else
                Utils.popAlert(Alert.AlertType.ERROR, "Missing required fields alert", "Must insert password");
            return false;
        }
        return true;
    }

    @FXML
    private void login() {
        String userName = userNameTextField.getText();
        String password = passwordField.getText();

        if (isInputValid(userName, password)) {
            if (isUserAuthorized(userName, password)) {
                try {
                    App.setRoot("authorized/employeesForm");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Login Failed", "Failed to log in, user name or password is incorrect");
            }
        }
    }

    private boolean isUserAuthorized(String userName, String password) {
        LoginResult result = ClientUserLoginController.getInstance().login(new LoginCredentials(userName, password));
        if (!result.isSuccessful) {
            return false;
        }
        if (result.userRole != UserRole.None) {
            LoggedInUser.setConnectionStatus(result.userRole, result.managerCinema);
            return true;
        }
        return false;
    }

    @FXML
    public void back() {
        ClientUserLoginController.getInstance().logout();
        LoggedInUser.onDisconnection();
        try {
            App.setRoot("unauthorized/primary");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}