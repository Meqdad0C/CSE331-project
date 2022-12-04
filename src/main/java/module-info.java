module com.example.one {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.one to javafx.fxml;
    exports com.example.one;
}