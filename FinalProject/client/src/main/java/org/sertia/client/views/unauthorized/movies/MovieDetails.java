package org.sertia.client.views.unauthorized.movies;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetails extends BasicPresenter implements Initializable {
    public TextField movieNameTxt;
    public TextField movieHebrewNameTxt;
    public TextField producerNameTxt;
    public TextField mainActorsTxt;
    public TextField movieDescriptionTxt;
    public VBox detailsPage;
    public ImageView movieImageView;

    @FXML
    private void back() {
        try {
            App.setRoot("unauthorized/moviesCatalogPresenter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CinemaScreeningMovie cinemaScreeningMovie = MovieHolder.getInstance().getCinemaScreeningMovie();
        ClientMovie movie = cinemaScreeningMovie.getMovieDetails();
        movieNameTxt.setText(movie.getName());
        movieHebrewNameTxt.setText(movie.getHebrewName());
        producerNameTxt.setText(movie.getProducerName());
        mainActorsTxt.setText(movie.getMainActorName());
        movieDescriptionTxt.setText(movie.getDescription());
        setImageView(movie);
//        detailsPage.getChildren().addAll(movieNameTxt, movieHebrewNameTxt, producerNameTxt,
//                mainActorsTxt, movieDescriptionTxt, movieImageView);
    }

    private void setImageView(ClientMovie movie) {
        String path;
//        if (movie.getPosterImageUrl() == null)
        path = "https://assets2.rockpapershotgun.com/shrek-again.jpg/BROK/resize/880%3E/format/jpg/quality/80/shrek-again.jpg";
//        else
//            path = movie.getPosterImageUrl();
        Image image = new Image(path);
        movieImageView.setImage(image);
        movieImageView.setFitHeight(111.1);
        movieImageView.setFitWidth(111.1);
    }
}
