module com.processsynchronization {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.processsynchronization to javafx.fxml;
    exports com.processsynchronization;
}