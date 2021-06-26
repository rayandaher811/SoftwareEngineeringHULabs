package org.sertia.client.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.sertia.client.App;
import org.sertia.client.boxes.SeatBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SeatChooseView extends BasicView implements Initializable {

    public int hallName;
    public int continueToPaymentButton;

    @FXML
    private TableView hallSeatsMap;

    @FXML
    public void toMain() throws IOException {
        App.setRoot("movieTicketPurchaseForm");
    }


    private SeatBox[][] generateBySize(int row, int col) {
        SeatBox[][] hall = new SeatBox[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                hall[i][j] = new SeatBox(1, i, j, false, true);
            }
        }
        return hall;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // need to figure out the nummber of rows and column in hall, and initialize seats
        int numberOfRows = 10;
        int numberOfColumns = 10;
    }
}