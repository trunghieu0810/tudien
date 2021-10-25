module com.example.tudien2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires en.us;
    requires freetts;
    requires java.sql;


    opens com.example.tudien2 to javafx.fxml;
    exports com.example.tudien2;
}