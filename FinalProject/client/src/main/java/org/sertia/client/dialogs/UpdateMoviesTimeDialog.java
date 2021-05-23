package org.sertia.client.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.sertia.client.ActiveUserData;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.UpdateMovieScreeningTime;
import org.sertia.client.global.LoggedInUser;
import org.sertia.client.pojos.Movie;
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
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Movies Dialog");
        dialog.setHeaderText("This is the available movies list");

        ButtonType closeButtonType = new ButtonType("סגור", ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType, submitButtonType);

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
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get().getText().equals("Submit")){
            Movie m = new Movie("A", "");
            ScreeningMovie screeningMovie = new ScreeningMovie(m, 1223, 2);

            UpdateMovieScreeningTime request = new UpdateMovieScreeningTime(LoggedInUser.getInstance().getUuid(), screeningMovie, DateTime.now());
            ServerCommunicationHandler.getInstance().requestMovieScreeningTimeChange(request);
            return new ActiveUserData("bbbb", "gggg");
        }

        return new ActiveUserData("userName.get()", "role.get()");

    }
}
