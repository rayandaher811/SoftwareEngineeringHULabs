module org.sertia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.gson;
    requires org.json;
    requires org.joda.time;
    requires org.sertia.contracts;

    opens org.sertia.client.communication.messages to com.google.gson;
    opens org.sertia.client to javafx.fxml, com.google.gson;
    opens org.sertia.client.controllers to javafx.fxml, com.google.gson;
    opens org.sertia.client.views to javafx.fxml, com.google.gson;

    exports org.sertia.client;
    exports org.sertia.client.controllers;
    exports org.sertia.client.views;
    exports org.sertia.client.boxes;
    opens org.sertia.client.boxes to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.available.movies;
    opens org.sertia.client.views.available.movies to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.complaints;
    opens org.sertia.client.views.complaints to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.purchase;
    opens org.sertia.client.views.purchase to com.google.gson, javafx.fxml;
}