package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;


public class AddOrRemoveMoviePresenter {

    @FXML
    public void addNewMovie() {
        try {
            App.setRoot("authorized/media.manager/addNewMovieData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeMovie() {
        try {
            App.setRoot("authorized/media.manager/removeMovie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
