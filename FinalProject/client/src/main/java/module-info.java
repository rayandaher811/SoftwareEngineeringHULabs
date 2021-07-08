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
    exports org.sertia.client.views.unauthorized.movies;
    opens org.sertia.client.views.unauthorized.movies to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.unauthorized.complaints;
    opens org.sertia.client.views.unauthorized.complaints to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.unauthorized.purchase;
    opens org.sertia.client.views.unauthorized.purchase to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.unauthorized.didntuse;
    opens org.sertia.client.views.unauthorized.didntuse to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.authorized;
    opens org.sertia.client.views.authorized to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.unauthorized.purchase.movie;
    opens org.sertia.client.views.unauthorized.purchase.movie to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.unauthorized;
    opens org.sertia.client.views.unauthorized to com.google.gson, javafx.fxml;
    exports org.sertia.client.views.authorized.media.manager;
    opens org.sertia.client.views.authorized.media.manager to com.google.gson, javafx.fxml;
    opens org.sertia.client.views.authorized.customer.support to com.google.gson, javafx.fxml;
}