package org.example.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.example.ActiveUserData;
import org.example.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToCustomerView() throws IOException {
        App.setRoot("customerView");
    }

    @FXML
    private void popUpLoginDialog() throws IOException {
//        App.setRoot("loginForm");
        System.out.println("Need to open login form");
        ActiveUserData activeUserData = LoginController.loginAndGetUserData();
        App.setRoot("employeesForm");
    }
}
