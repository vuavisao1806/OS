module com.deadlock {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.deadlock to javafx.fxml;
    exports com.deadlock;
}