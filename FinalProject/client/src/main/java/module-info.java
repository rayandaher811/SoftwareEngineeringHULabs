module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example to javafx.fxml;
    opens org.example.controllers to javafx.fxml;

    exports org.example;
    exports org.example.controllers;
}