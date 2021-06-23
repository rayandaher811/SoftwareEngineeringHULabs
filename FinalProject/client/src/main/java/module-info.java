module org.sertia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.gson;
    requires org.json;
    requires org.joda.time;

    opens org.sertia.client.communication.messages to com.google.gson;
    opens org.sertia.client to javafx.fxml, com.google.gson;
    opens org.sertia.client.controllers to javafx.fxml, com.google.gson;

    exports org.sertia.client;
    exports org.sertia.client.controllers;
}