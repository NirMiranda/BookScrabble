module com.example.newproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.newproject to javafx.fxml;
    exports com.example.newproject;
}