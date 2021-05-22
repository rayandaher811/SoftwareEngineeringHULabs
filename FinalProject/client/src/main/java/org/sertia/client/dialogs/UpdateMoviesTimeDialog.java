package org.sertia.client.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.sertia.client.ActiveUserData;
import org.sertia.client.pojos.ScreeningMovie;

import java.util.Collection;
import java.util.Optional;

public class UpdateMoviesTimeDialog extends AbstractInteractiveDialog {

    public static ActiveUserData loginAndGetUserData(Collection<ScreeningMovie> movies) {
        return doSomething(movies);
    }

    private static ActiveUserData doSomething(Collection<ScreeningMovie> movies) {
        String dialogTitle;
        String dialogHeaderText;
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Available Movies Dialog");
        dialog.setHeaderText("This is the available movies list");

// Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
        ButtonType closeButtonType = new ButtonType("סגור", ButtonBar.ButtonData.BACK_PREVIOUS);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int i = 0;
//        int j = 0;
        for (final ScreeningMovie m : movies){
            grid.add(new Label(m.getMovie().getName()), 0, i);
            i++;
        }

        dialog.getDialogPane().setContent(grid);
        Optional<Pair<String, String>> result = dialog.showAndWait();

        return new ActiveUserData("userName.get()", "role.get()");

    }
}
