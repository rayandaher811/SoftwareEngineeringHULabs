package org.sertia.client.views.authorized;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientReportsControl;
import org.sertia.client.global.LoggedInUser;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.reports.response.ClientReportsResponse;
import org.sertia.contracts.user.login.UserRole;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportsViewPresenter implements Initializable {
    public Group paneData;
    public ComboBox reportsToShow;


    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserRole userRole = LoggedInUser.getInstance().getUserRole();
        ClientReportsResponse response = null;
        if (userRole == UserRole.BranchManager) {
            // TODO: how to know cinema id, it's not related to role
            response = ClientReportsControl.getInstance().getCinemaReports(1);
        } else if (userRole == UserRole.CinemaManager) {
            response = ClientReportsControl.getInstance().getSertiaReports();
        }

        if (!response.isSuccessful) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error fetching data from sertia server");
            errorAlert.setContentText(response.failReason);
            errorAlert.showAndWait();
        } else {
            ObservableList<ClientReport> clientReports = FXCollections.observableList(new ArrayList<>(response.reports));

            reportsToShow.setItems(clientReports);
            reportsToShow.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
                paneData.getChildren().clear();

                ArrayList<PieChart.Data> pieChartConvertedData = new ArrayList<>();
                ((ClientReport)observableValue.getValue()).reportEntries.forEach(reportEntry -> {
                    pieChartConvertedData.add(new PieChart.Data(reportEntry.fieldName, reportEntry.value));
                });

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart(xAxis,yAxis);

                bc.setTitle(((ClientReport)observableValue.getValue()).title);
                xAxis.setLabel("parameter");
                yAxis.setLabel("Value");
                ArrayList<XYChart.Series<String, Number>> seriesArrayList = new ArrayList<>();
                ((ClientReport)observableValue.getValue()).reportEntries.forEach(reportEntry -> {
                    XYChart.Series series = new XYChart.Series();
                    series.setName(reportEntry.fieldName);
                    series.getData().add(new XYChart.Data(reportEntry.fieldName, reportEntry.value));
                    seriesArrayList.add(series);
                });
                bc.getData().addAll(seriesArrayList);
                paneData.getChildren().add(bc);
            });
        }
    }
}
