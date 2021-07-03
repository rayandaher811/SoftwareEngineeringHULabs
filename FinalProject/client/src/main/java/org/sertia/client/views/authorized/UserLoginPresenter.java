package org.sertia.client.views.authorized;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientUserLoginController;
import org.sertia.client.global.LoggedInUser;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.response.LoginResult;

import java.io.IOException;

public class UserLoginPresenter {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;


    private void popAlert(String missingFieldName) {
        Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
        missingKeyFieldsAlert.setTitle("Missing required fields alert");
        missingKeyFieldsAlert.setContentText(missingFieldName);
        missingKeyFieldsAlert.show();
    }

    private boolean isInputValid(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            if (userName.isEmpty() && password.isEmpty())
                popAlert("Must insert both user name and password");
            else if (userName.isEmpty())
                popAlert("Must insert user name");
            else
                popAlert("Must insert password");
            return false;
        }
        return true;
    }

    private void alertForWronCredentials() {
        Alert wrongCredantialsAlert = new Alert(Alert.AlertType.ERROR);
        wrongCredantialsAlert.setTitle("Login Failed");
        wrongCredantialsAlert.setContentText("Failed to log in, user name or password is incorrect");
        wrongCredantialsAlert.show();
    }

    @FXML
    private void login() throws IOException {
        String userName = userNameTextField.getText();
        String password = passwordField.getText();

        if (isInputValid(userName, password)) {
            System.out.println("username: " + userName + " , password: " + password);
            if (isUserAuthorized(userName, password)) {
                App.setRoot("authorized/employeesForm");
            } else {
                alertForWronCredentials();
            }
        }
    }

    private boolean isUserAuthorized(String userName, String password) {
        LoginResult result = ClientUserLoginController.getInstance().login(new LoginCredentials(userName, password));
        if(!result.isSuccessful){
            return false;
        }
        if (result.userRole != UserRole.None) {
            LoggedInUser.setConnectionStatus(userName, result.userRole);
            return true;
        }
        return false;
    }

    @FXML
    private void back() throws IOException {
        LoggedInUser.onDisconnection();
        App.setRoot("unauthorized/primary");
    }
}