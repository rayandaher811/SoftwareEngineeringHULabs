package org.sertia.client.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetails extends BasicPresenter implements Initializable {
    @FXML private VBox detailesPage;
    public int movieTitle=1;
    public int moviePoster=1;
    public int leadingActors=1;
    public int description=1;
    public int producer=1;
    public boolean isComingSoon=true;
    public int purchaseStreamButton;
    public int newPriceTextBox;
    public int reuqestPriceChangeButton;
    public int removeMovieButton;
    public int createStreamButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String path = "https://assets2.rockpapershotgun.com/shrek-again.jpg/BROK/resize/880%3E/format/jpg/quality/80/shrek-again.jpg";
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);//movieposter
        imageView.setFitHeight(111.1);
        imageView.setFitWidth(111.1);
        detailesPage.getChildren().add(imageView);
        Text title = new Text("שם הסרט:"+movieTitle);
        Text actors = new Text("שחקנים:"+leadingActors);
        Text desc = new Text("תיאור:"+description);
        Text prod = new Text("מפיק:"+producer);
        Text issoon = new Text("האם בקרוב?:"+isComingSoon);
        detailesPage.getChildren().add(actors);
        detailesPage.getChildren().add(desc);
        detailesPage.getChildren().add(prod);
        detailesPage.getChildren().add(issoon);
    }
}
