package org.sertia.client.views;

import javafx.scene.control.Alert;

public class Utils {

    public static void popAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.getDialogPane().setMinHeight(250);
        alert.getDialogPane().setMaxHeight(250);
        alert.getDialogPane().setMinWidth(250);
        alert.getDialogPane().setMinWidth(250);
        alert.showAndWait();
    }
}
