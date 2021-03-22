package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private TextField mainTextBox;

    @FXML
    private TextArea helloCountTxtArea;

    private boolean shouldShowText;
    private int clicksCounter;
    @FXML
    private void sayHello() {
        clicksCounter++;
        helloCountTxtArea.setText(String.valueOf(clicksCounter));
        shouldShowText = !shouldShowText;

        if (shouldShowText)
            mainTextBox.setText("Hello World!");
        else
            mainTextBox.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shouldShowText = false;
        clicksCounter = 0;
    }
}
