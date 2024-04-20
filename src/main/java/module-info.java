module com.example.view2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.view2 to javafx.fxml;
    exports com.example.view2;
}