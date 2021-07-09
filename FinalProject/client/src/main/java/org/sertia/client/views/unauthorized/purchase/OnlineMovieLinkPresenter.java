package org.sertia.client.views.unauthorized.purchase;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.global.ClientDataHolder;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineMovieLinkPresenter extends BasicPresenterWithValidations implements Initializable {
    @FXML
    public TextField movieName;
    public TextField nameTxtField;
    public TextField phoneTxTextField;
    public TextField emailTxTextField;

    @FXML
    public void back() throws IOException {
        App.setRoot("unauthorized/sertiaCatalogPresenter");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (ClientDataHolder.getInstance().isInitialized()) {
            nameTxtField.setText(ClientDataHolder.getInstance().getClientName());
            phoneTxTextField.setText(ClientDataHolder.getInstance().getPhone());
            emailTxTextField.setText(ClientDataHolder.getInstance().getEmail());
        }
        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        movieName.setText(movie.getMovieDetails().getName());
    }

    @FXML
    public void buyLink() {
        if (isInputValid()) {
            try {
                ClientDataHolder.getInstance().setClientData(nameTxtField.getText(), emailTxTextField.getText(), phoneTxTextField.getText());
                App.setRoot("unauthorized/payment/byCreditCardForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isStringNotEmpty(nameTxtField.getText(), "Please insert your name");
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        return isEmailValid && isPhoneValid && isNameValid;
    }
}
