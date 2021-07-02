package org.sertia.client.views.unauthorized.purchase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OnlineMovieLinkPresenter implements Initializable {
    @FXML
    public ComboBox movieToWatch;

    @FXML
    public void toMainMenu() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<SertiaMovie> catalog = ClientCatalogControl.getInstance().getOnlineMovies();

        ObservableList<String> ticketTypes = FXCollections.observableList((catalog.stream().map(sertiaMovie -> sertiaMovie.getMovieDetails().getName()).collect(Collectors.toList())));
        movieToWatch.setItems(ticketTypes);
    }

    @FXML
    public void buyLink(){
        // TODO: FIXME
        ClientPurchaseControl.purchaseStreaming(1);
    }
}
