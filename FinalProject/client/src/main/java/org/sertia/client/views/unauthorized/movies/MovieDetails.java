package org.sertia.client.views.unauthorized.movies;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;
import org.sertia.contracts.movies.catalog.ClientMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetails extends BasicPresenter implements Initializable {
    @FXML
    private VBox detailsPage;

    @FXML
    private void back() {
        try {
            App.setRoot("moviesCatalogPresenter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientMovie movie = MovieHolder.getInstance().getMovie();

        ImageView imageView = getImageView(movie);
        Text title = new Text("שם הסרט:" + movie.getName());
        Text actors = new Text("שחקנים:" + movie.getMainActorName());
        Text desc = new Text("תיאור:" + movie.getDescription());
        Text prod = new Text("מפיק:" + movie.getProducerName());
        Button back = new Button();
        back.setText("חזרה");
        back.setOnMouseClicked(mouseEvent -> back());
        detailsPage.getChildren().addAll(title, actors, prod, desc, imageView, back);
    }

    private ImageView getImageView(ClientMovie movie) {
        String path;
        if (movie.getPosterImageUrl() == null)
            path = "https://assets2.rockpapershotgun.com/shrek-again.jpg/BROK/resize/880%3E/format/jpg/quality/80/shrek-again.jpg";
        else
            path = movie.getPosterImageUrl();
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);//movieposter
        imageView.setFitHeight(111.1);
        imageView.setFitWidth(111.1);
        return imageView;
    }
}
