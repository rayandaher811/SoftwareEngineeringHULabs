package org.sertia.client.controllers;

import javafx.fxml.FXML;
import org.sertia.client.ActiveUserData;
import org.sertia.client.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToCustomerView() throws IOException {
        App.setRoot("customerView");
    }

    @FXML
    private void switchToLogInView() throws IOException {
        App.setRoot("loginForm");
    }
}
