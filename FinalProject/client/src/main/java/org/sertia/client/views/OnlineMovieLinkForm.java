package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class OnlineMovieLinkForm {

    @FXML
    public void toMain() throws IOException {
        App.setRoot("primary");
    }
}
