package org.sertia.client.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.sertia.client.App;

import java.io.IOException;

public class ClientComplaintsControl {

    @FXML
    private TextField complaintData;

    @FXML
    public void toMainMenu() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    public void publishComplaint(){
        System.out.println("Complaint is: " + complaintData.getText());
    }

}