package org.sertia.client.views.purchase.movie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import org.sertia.client.App;
import org.sertia.client.boxes.SeatBox;
import org.sertia.client.views.didntuse.BasicPresenter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SeatChooseView extends BasicPresenter implements Initializable {

    public int hallName;
    public int continueToPaymentButton;
    @FXML
    private GridPane movietheater;
    private static final int num_row = 5;
    private static final int num_seats_in_row = 5;
    private boolean[][] seatMap;
    private static int amoutOfTickets = 3;
    private List<Button> seats;
    private List<String> order;

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
        try {
            seats = new ArrayList<Button>();
            order = new ArrayList<String>();
            seatMap = new boolean[][]{{true, true, false, true, false}, {true, true, false, true, false}, {true, true, false, true, false}, {true, true, false, true, false}, {true, true, false, true, false}};
            for (int indexRow = 0; indexRow < num_row; indexRow++) {
                for (int indexSeat = 0; indexSeat < num_seats_in_row; indexSeat++) {
                    Button seat = new Button("" + (indexSeat + 1));
                    if (seatMap[indexRow][indexSeat])//available
                        seat.setStyle("-fx-background-color: #00ff00");
                    else {
                        seat.setStyle("-fx-background-color: #FF0000");
                        seat.setDisable(true);
                    }
                    seat.setOnAction((ActionEvent e) -> {
                        if (seat.getStyle() == "-fx-background-color: #FF0000") {
                            seat.setStyle("-fx-background-color: #00ff00");
                            order.remove(seat.getId());
                            amoutOfTickets++;
                            continueSelection();
                        } else {
                            seat.setStyle("-fx-background-color: #FF0000");
                            amoutOfTickets--;
                            order.add(seat.getId());
                            if ((amoutOfTickets) == 0) {
                                System.out.println("No more tickets");
                                disableAllButtons();
                            }
                        }


                    });
                    seat.setId("btn_r_" + indexRow + "_s_" + indexSeat);
                    movietheater.add(seat, indexSeat, indexRow);
                    seats.add(seat);
                }
            }
        } catch (Exception e) {

        }

    }

    private void disableAllButtons() {
        for (Button b : seats) {
            if (b.getStyle() == "-fx-background-color: #00ff00")
                b.setDisable(true);
        }
        System.out.println(order.toString());
    }

    private void continueSelection() {
        for (Button b : seats) {
            int seatRow = Integer.parseInt(b.getId().split("_")[2]);
            int seatCol = Integer.parseInt(b.getId().split("_")[4]);
            if (b.isDisabled() && seatMap[seatRow][seatCol]) {
                b.setDisable(false);
            }
        }
    }

    @FXML
    private void resetOrderSelection() {
        for (Button b : seats) {
            int seatRow = Integer.parseInt(b.getId().split("_")[2]);
            int seatCol = Integer.parseInt(b.getId().split("_")[4]);
            if (seatMap[seatRow][seatCol]) {
                b.setDisable(false);
                if (seatMap[seatRow][seatCol]) {
                    b.setStyle("-fx-background-color: #00ff00");
                }
                amoutOfTickets = 3;
                order.clear();
            }
        }
    }
}