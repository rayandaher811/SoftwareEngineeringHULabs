package org.sertia.client.views.unauthorized.movies;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.global.MovieHolder;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetails implements Initializable {
    public TextField movieNameTxt;
    public TextField movieHebrewNameTxt;
    public TextField producerNameTxt;
    public TextField mainActorsTxt;
    public TextField movieDescriptionTxt;
    public VBox detailsPage;
    public ImageView movieImageView;
    public TextField movieTicketPrice;

    @FXML
    public void back() {
        try {
            App.setRoot("unauthorized/sertiaCatalogPresenter");
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
        movieTicketPrice.setText(String.valueOf(cinemaScreeningMovie.getTicketPrice()));
        setImageView(movie);
    }

    private void setImageView(ClientMovie movie) {
        String path = movie.getPosterImageUrl();
        Image image = new Image(path);
        movieImageView.setImage(image);
        movieImageView.setFitHeight(111.1);
        movieImageView.setFitWidth(111.1);
    }
}
