package org.sertia.client.views.authorized;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsViewPresenter implements Initializable {
    public Pane paneData;
    public ComboBox movieToRemove;
    public Pane pane;
    public Group group;
    private HashMap<String, SertiaMovie> movieNameToId;


    public void back(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        UserRole userRole = LoggedInUser.getInstance().getUserRole();
//        ClientReportsResponse response = null;
//        if (userRole == UserRole.BranchManager) {
//            // TODO: how to know cinema id, it's not related to role
//            response = ClientReportsControl.getInstance().getCinemaReports(1);
//        } else if (userRole == UserRole.CinemaManager) {
//            response = ClientReportsControl.getInstance().getSertiaReports();
//        }
//
//        if (!response.isSuccessful) {
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setTitle("Error fetching data from sertia server");
//            errorAlert.setContentText(response.failReason);
//            errorAlert.showAndWait();
//        } else {
//            System.out.println("BGBG");
//        }
        pane.setPrefWidth(300);
        pane.setPrefHeight(300);



        List<SertiaMovie> catalog = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        movieNameToId = new HashMap<>();
        catalog.forEach(sertiaMovie -> movieNameToId.put(sertiaMovie.getMovieDetails().getName(), sertiaMovie));
        ObservableList<String> ticketTypes = FXCollections.observableList(new ArrayList<>(movieNameToId.keySet()));
        movieToRemove.setItems(ticketTypes);
        movieToRemove.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            pane.setPrefWidth(300);
            pane.setPrefHeight(300);
            paneData.getChildren().clear();
            group.getChildren().clear();
            if (t1.equals("captin America")){
                Circle circle = new Circle();
                //Setting the properties of the circle
                circle.setCenterX(300.0f);
                circle.setCenterY(135.0f);
                circle.setRadius(100.0f);
                //Setting other properties
                circle.setFill(Color.TURQUOISE);
                circle.setStrokeWidth(8.0);
                circle.setStroke(Color.DARKSLATEGREY);
                paneData.getChildren().add(circle);
            } else if (t1.equals("Antman")) {
                Circle circle = new Circle();
                //Setting the properties of the circle
                circle.setCenterX(300.0f);
                circle.setCenterY(135.0f);
                circle.setRadius(100.0f);
                //Setting other properties
                circle.setFill(Color.DARKCYAN);
                circle.setStrokeWidth(8.0);
                circle.setStroke(Color.DARKSLATEGREY);
                paneData.getChildren().add(circle);
            } else if (t1.equals("The expendables")){
                Circle circle = new Circle();
                //Setting the properties of the circle
                circle.setCenterX(300.0f);
                circle.setCenterY(135.0f);
                circle.setRadius(100.0f);
                //Setting other properties
                circle.setFill(Color.BLUE);
                circle.setStrokeWidth(8.0);
                circle.setStroke(Color.DARKSLATEGREY);
                paneData.getChildren().add(circle);
            }
        });
    }
}
