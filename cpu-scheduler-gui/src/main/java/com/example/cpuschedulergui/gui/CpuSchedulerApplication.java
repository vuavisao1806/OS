package com.example.cpuschedulergui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CpuSchedulerApplication extends Application {

    @Override
    public void start(Stage stage) {
        CpuSchedulerDashboard dashboard = new CpuSchedulerDashboard();

        Scene scene = new Scene(dashboard.getRoot(), 1100, 760);
        stage.setTitle("CPU Scheduling Simulator");
        stage.setScene(scene);
        stage.show();
    }
}
