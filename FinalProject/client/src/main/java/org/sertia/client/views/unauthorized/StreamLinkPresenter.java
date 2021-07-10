package org.sertia.client.views.unauthorized;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.screening.ticket.response.GetStreamingByLinkResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class StreamLinkPresenter extends BasicPresenterWithValidations {
    public TextField linkUrlTxt;

    public void checkAvailability(ActionEvent actionEvent) {
        if (isInputValid()) {
            GetStreamingByLinkResponse response =
                    ClientCatalogControl.getInstance().getStreamingByLink(linkUrlTxt.getText());
            if (!response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.ERROR, STREAMING_LINK_AVAILABILITY_CHECKER, response.failReason);
            } else {
                Utils.popAlert(Alert.AlertType.INFORMATION, STREAMING_LINK_AVAILABILITY_CHECKER,
                        YOUR_STREAMING_LINK_FOR_MOVIE + response.movieName + IS_AVAILABLE);
                back();
            }
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("unauthorized/primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isLinkUrlValid = isStringNotEmpty(linkUrlTxt.getText(), MISSING_LINK_ERROR);
        return isLinkUrlValid;
    }
}
