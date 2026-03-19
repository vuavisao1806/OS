module com.example.cpuschedulergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.cpuschedulergui to javafx.fxml;
    exports com.example.cpuschedulergui;
    exports com.example.cpuschedulergui.gui;
    opens com.example.cpuschedulergui.gui to javafx.fxml;
}