package com.example.cpuschedulergui.gui;

import com.example.cpuschedulergui.algorithms.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class CpuSchedulerDashboard {
    private final BorderPane root = new BorderPane();

    private final ComboBox<String> algorithmComboBox = new ComboBox<>();
    private final TextField inputFileField = new TextField();
    private final TextField outputFileField = new TextField();

    private final TextArea ganttChartArea = new TextArea();
    private final TextArea statsArea = new TextArea();

    public CpuSchedulerDashboard() {
        buildUI();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildUI() {
        root.setPadding(new Insets(15));

        Label title = new Label("CPU Scheduling Simulator");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        algorithmComboBox.getItems().addAll(
                "FCFS",
                "Preemptive SJF",
                "Nonpreemptive SJF",
                "Preemptive Priority",
                "Nonpreemptive Priority",
                "Round Robin",
                "Multilevel"
        );
        algorithmComboBox.setValue("FCFS");
        algorithmComboBox.setPrefWidth(240);

        inputFileField.setPromptText("Choose input file...");
        inputFileField.setEditable(false);

        outputFileField.setPromptText("Optional: choose output file...");
        outputFileField.setEditable(false);

        Button inputBrowseButton = new Button("Browse Input");
        inputBrowseButton.setOnAction(e -> chooseInputFile());

        Button outputBrowseButton = new Button("Browse Output");
        outputBrowseButton.setOnAction(e -> chooseOutputFile());

        Button clearOutputButton = new Button("Clear Output");
        clearOutputButton.setOnAction(e -> outputFileField.clear());

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit());

        GridPane topForm = new GridPane();
        topForm.setHgap(10);
        topForm.setVgap(10);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(90);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);

        topForm.getColumnConstraints().addAll(c1, c2);

        topForm.add(new Label("Algorithm:"), 0, 0);
        topForm.add(algorithmComboBox, 1, 0);

        topForm.add(new Label("Input file:"), 0, 1);
        topForm.add(inputFileField, 1, 1);
        topForm.add(inputBrowseButton, 2, 1);

        topForm.add(new Label("Output file:"), 0, 2);
        topForm.add(outputFileField, 1, 2);

        HBox outputButtons = new HBox(8, outputBrowseButton, clearOutputButton);
        outputButtons.setAlignment(Pos.CENTER_LEFT);
        topForm.add(outputButtons, 2, 2);

        VBox topBox = new VBox(12, title, topForm, submitButton);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("""
                -fx-background-color: #f8f8f8;
                -fx-border-color: #d9d9d9;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);

        ganttChartArea.setEditable(false);
        ganttChartArea.setWrapText(false);
        ganttChartArea.setPrefRowCount(14);
        ganttChartArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 18px;");

        statsArea.setEditable(false);
        statsArea.setWrapText(false);
        statsArea.setPrefRowCount(8);
        statsArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 18px;");

        TitledPane ganttPane = new TitledPane("Gantt Chart", ganttChartArea);
        ganttPane.setCollapsible(false);

        TitledPane summaryPane = new TitledPane("Summary", statsArea);
        summaryPane.setCollapsible(false);

        VBox centerBox = new VBox(12, ganttPane, summaryPane);
        VBox.setVgrow(ganttPane, Priority.ALWAYS);
        centerBox.setPadding(new Insets(10, 0, 0, 0));

        root.setTop(topBox);
        root.setCenter(centerBox);
    }

    private void chooseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Input File");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            inputFileField.setText(file.getAbsolutePath());
        }
    }

    private void chooseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Output File");
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            outputFileField.setText(file.getAbsolutePath());
        }
    }

    private void handleSubmit() {
        String algorithm = algorithmComboBox.getValue();
        String inputPathText = inputFileField.getText();
        String outputPathText = outputFileField.getText();

        if (inputPathText == null || inputPathText.isBlank()) {
            showError("Please choose an input file.");
            return;
        }

        Path inputPath = Path.of(inputPathText);
        Path outputPath = (outputPathText == null || outputPathText.isBlank())
                ? null
                : Path.of(outputPathText);

        try {
            SchedulerResult result = runSelectedAlgorithm(algorithm, inputPath, outputPath);

            ganttChartArea.setText(result.getGanttChart());
            statsArea.setText(buildStatisticsText(result));

        } catch (Exception exception) {
            exception.printStackTrace();
            showError("Failed to run scheduler:\n" + exception.getMessage());
        }
    }

    private String buildStatisticsText(SchedulerResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Total turnaround time: %d%n", result.getTotalTurnaroundTime()));
        stringBuilder.append(String.format("Average turnaround time: %.2f%n", result.getAverageTurnaroundTime()));
        stringBuilder.append(String.format("Total waiting time: %d%n", result.getTotalWaitingTime()));
        stringBuilder.append(String.format("Average waiting time: %.2f%n", result.getAverageWaitingTime()));
        stringBuilder.append(String.format("Total response time: %d%n", result.getTotalResponseTime()));
        stringBuilder.append(String.format("Average response time: %.2f%n", result.getAverageResponseTime()));
        stringBuilder.append(String.format("The number of context switches: %d%n", result.getNumberOfContextSwitches()));
        return stringBuilder.toString();
    }

    private SchedulerResult runSelectedAlgorithm(String algorithm, Path inputPath, Path outputPath) throws Exception {
        CpuScheduler cpuScheduler;
        String content = Files.readString(inputPath);
        switch (algorithm) {
            case "FCFS" -> cpuScheduler = SchedulerInputParser.parseFCFS(content);
            case "Preemptive SJF" -> cpuScheduler = SchedulerInputParser.parsePreemSJF(content);
            case "Nonpreemptive SJF" -> cpuScheduler = SchedulerInputParser.parseNonpreemSJF(content);
            case "Preemptive Priority" -> cpuScheduler = SchedulerInputParser.parsePreemPriority(content);
            case "Nonpreemptive Priority" -> cpuScheduler = SchedulerInputParser.parseNonpreemPriority(content);
            case "Round Robin" -> cpuScheduler = SchedulerInputParser.parseRoundRobin(content);
            case "Multilevel" -> cpuScheduler = SchedulerInputParser.parseMultilevel(content);
            default -> throw new RuntimeException("The algorithm is not implemented yet");
        }
        cpuScheduler.runCpuScheduler();
        System.out.println(GanttChart.draw(cpuScheduler.getEvents()));
        String ganttChart = GanttChart.draw(cpuScheduler.getEvents());

        SchedulerResult result = new SchedulerResult(
                ganttChart,
                cpuScheduler.getSumTurnaroundTime(),
                cpuScheduler.getAverageTurnaroundTime(),
                cpuScheduler.getSumWaitingTime(),
                cpuScheduler.getAverageWaitingTime(),
                cpuScheduler.getSumResponseTime(),
                cpuScheduler.getAverageResponseTime(),
                cpuScheduler.getNumberOfContextSwitches()
        );

        if (outputPath != null) {
            String summary = result.getGanttChart()
                    + System.lineSeparator()
                    + System.lineSeparator()
                    + buildStatisticsText(result);

            Files.writeString(outputPath, summary);
        }

        return result;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}