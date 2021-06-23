package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class PrepaidTicketsPurchaseView {

    @FXML
    public void toMain() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    public void buy() throws IOException {
        System.out.println("buy and save purchase of prepaid tickets");
        App.setRoot("primary");
    }
}
