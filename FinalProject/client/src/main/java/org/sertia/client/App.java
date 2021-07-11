package org.sertia.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sertia.client.communication.SertiaClient;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("unauthorized/primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void run(String[] args) {
        SertiaClient client;
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        if (SertiaClient.initializeClientServerConnection(host, port)) {
            client = SertiaClient.getInstance();
            try {
                client.openConnection();
                launch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.exit(-1);
    }
}