package org.sertia.client.views.unauthorized.purchase.movie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.NumberOfTicketsHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.global.SeatsHolder;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.response.ClientSeatMapResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SeatChooseView implements Initializable {

    public Button applyBtn;
    @FXML
    private GridPane movietheater;
    private static int num_row;
    private static int num_seats_in_row;
    private boolean[][] seatMap;
    private int amoutOfTickets;
    private List<Button> seats;
    private List<String> order;
    private List<HallSeat> hallSeats;

    @FXML
    public void toMain() throws IOException {
        App.setRoot("unauthorized/movie/screeningOrderDataSelection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyBtn.setDisable(true);
        amoutOfTickets = NumberOfTicketsHolder.getInstance().getNumberOfTickets();
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        ClientSeatMapResponse seatMapResponse =
                ClientPurchaseControl.getInstance().getScreeningSeatMap(screening.getScreeningId());
        hallSeats = seatMapResponse.getHallSeats();
        num_row = 0;
        num_seats_in_row = 0;

        for (HallSeat seat : hallSeats) {
            num_row = Math.max(num_row, seat.getRow());
            num_seats_in_row = Math.max(num_seats_in_row, seat.getNumberInRow());
        }

        try {
            seats = new ArrayList<>();
            order = new ArrayList<>();
            seatMap = new boolean[num_row + 1][num_seats_in_row + 1];
            for (HallSeat seat : hallSeats) {
                seatMap[seat.getRow()][seat.getNumberInRow()] = !seat.isTaken;
            }
            for (int indexRow = 0; indexRow < num_row; indexRow++) {
                for (int indexSeat = 0; indexSeat < num_seats_in_row; indexSeat++) {
                    Button seat = new Button(String.valueOf(indexSeat + 1));
                    if (seatMap[indexRow][indexSeat]) {
                        seat.setStyle("-fx-background-color: #00ff00");
                    } else {
                        seat.setStyle("-fx-background-color: #FF0000");
                        seat.setDisable(true);
                    }
                    seat.setOnAction((ActionEvent e) -> {
                        if (seat.getStyle() == "-fx-background-color: #FF0000") {
                            seat.setStyle("-fx-background-color: #00ff00");
                            order.remove(seat.getId());
                            amoutOfTickets++;
                            applyBtn.setDisable(true);
                            continueSelection();
                        } else {
                            seat.setStyle("-fx-background-color: #FF0000");
                            amoutOfTickets--;
                            order.add(seat.getId());
                            if ((amoutOfTickets) == 0) {
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
            System.out.println(e);
        }

    }

    private void disableAllButtons() {
        for (Button b : seats) {
            if (b.getStyle() == "-fx-background-color: #00ff00")
                b.setDisable(true);
        }
        applyBtn.setDisable(false);
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

    private int getSeatId(int row, int seatInRow) {
        return hallSeats
                .stream()
                .filter(hallSeat -> hallSeat.getNumberInRow() == seatInRow && hallSeat.getRow() == row)
                .findFirst()
                .get().getId();
    }

    @FXML
    private void resetOrderSelection() {
        for (Button btn : seats) {
            int seatRow = getSeatRow(btn.getId());
            int seatCol = getSeatCol(btn.getId());
            if (seatMap[seatRow][seatCol]) {
                btn.setDisable(false);
                if (seatMap[seatRow][seatCol]) {
                    btn.setStyle("-fx-background-color: #00ff00");
                }
                amoutOfTickets = NumberOfTicketsHolder.getInstance().getNumberOfTickets();
                order.clear();
            }
        }
        applyBtn.setDisable(true);
    }

    @FXML
    private void proceed() {
        List<Integer> seatsIds = new ArrayList<>();
        order.forEach(s -> seatsIds.add(getSeatId(getSeatRow(s), getSeatCol(s))));

        SeatsHolder.getInstance().setSeatsIdsList(seatsIds);
        try {
            App.setRoot("unauthorized/payment/selectionMethodForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSeatRow(String id) {
        return Integer.parseInt(id.split("_")[2]);
    }

    private int getSeatCol(String id) {
        return Integer.parseInt(id.split("_")[4]);
    }

}