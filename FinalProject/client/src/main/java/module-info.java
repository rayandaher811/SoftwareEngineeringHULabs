module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.gson;
    requires org.json;

    opens org.sertia.client.pojos to com.google.gson;
    opens org.sertia.client.communication.messages to com.google.gson;
    opens org.sertia.client to javafx.fxml, com.google.gson;
    opens org.sertia.client.controllers to javafx.fxml, com.google.gson;

    exports org.sertia.client;
    exports org.sertia.client.controllers;
}