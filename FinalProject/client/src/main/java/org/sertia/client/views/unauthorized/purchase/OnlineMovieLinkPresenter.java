package org.sertia.client.views.unauthorized.purchase;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
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
        App.setRoot("unauthorized/moviesCatalogPresenter");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientMovie movie = MovieHolder.getInstance().getMovie();
        movieName.setText(movie.getName());
    }

    @FXML
    public void buyLink() {
        if (isInputValid()) {
            try {
                // TODO: FIXME
                //ClientPurchaseControl.purchaseStreaming(1);
                App.setRoot("unauthorized/PaymentView");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isNameValid(nameTxtField.getText());
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        return isEmailValid && isPhoneValid && isNameValid;
    }
}
