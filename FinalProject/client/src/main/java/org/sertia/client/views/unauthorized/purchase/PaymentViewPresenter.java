package org.sertia.client.views.unauthorized.purchase;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.contracts.movies.catalog.ClientMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentViewPresenter implements Initializable {

    public int cardHolderName;
    public int cardNumber;
    public int cardExpirationDate;
    public int cvv;
    public int payButton;


    /**
     * @param purchaseSessionId
     */
    public void onCreate(String purchaseSessionId) {
        // TODO - implement PaymentView.onCreate
        throw new UnsupportedOperationException();
    }

    @FXML
    public void back() {
        boolean isLinkRequest = MovieHolder.getInstance().isOnlineLinkPurchaseRequest();

        try {
            if (isLinkRequest) {
                App.setRoot("unauthorized/onlineWatchLinkForm");
            } else {
                App.setRoot("unauthorized/purchaseMovieTickets");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean isLinkRequest = MovieHolder.getInstance().isOnlineLinkPurchaseRequest();
        if (isLinkRequest) {
            System.out.println("BG link");
        } else {
            System.out.println("BG phisical ticket");
        }
    }
}