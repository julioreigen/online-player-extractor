package com.julioreigen.ope.onlineplayerextractor;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class StageData {
    private Stage st;
    private File lastDirectory;
    private final ExtractorController ec = new ExtractorController();
    private Label labelFileURL;
    private Label labelOutput;
    private TextArea progressText;
    private TextField fileURL;
    private Button chooseOutputBt;
    private Button openOutputDirBt;
    private Button executeBt;
    private ProgressBar progressBar;
    private FileChooser fileChooser;
    private File[] outputFile;

    public StageData() {
    }
    public StageData(Stage stage) {
        st = stage;
    }

    protected void configure(){
        labelFileURL = new Label("Video URL:");
        labelOutput = new Label("No output file selected");
        progressText = new TextArea("Progress will be shown here...");
        progressText.setPrefRowCount(1);
        progressText.setEditable(false);
        fileURL = new TextField();

        chooseOutputBt = new Button("Choose Output Location");
        openOutputDirBt = new Button("Open Output Directory");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        executeBt = new Button("Extract");

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MPEG file(*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter("Matroska Container(*.mkv)", "*.mkv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save as");
        fileChooser.setInitialFileName("Video.mp4");

        outputFile = new File[1];

        chooseOutputBt.setOnAction(e -> {
            if (lastDirectory != null)
                fileChooser.setInitialDirectory(lastDirectory);

            outputFile[0] = fileChooser.showSaveDialog(st);

            if (outputFile[0] != null) {
                lastDirectory = outputFile[0].getParentFile();
                labelOutput.setText("Output File: " + outputFile[0].getAbsolutePath());
                executeBt.setDisable(false);
                openOutputDirBt.setDisable(false);
            }
        });

        executeBt.setDisable(true);
        openOutputDirBt.setDisable(true);

        executeBt.setOnAction(e -> {
            String input = fileURL.getText();
            if (outputFile[0] != null) {
                Task<Void> task = ec.createExtractionTask(input, outputFile[0].getAbsolutePath(), progressBar, progressText);
                new Thread(task).start();
            }
        });

        openOutputDirBt.setOnAction(e -> {
            if (outputFile[0] != null) {
                ec.openDir(outputFile[0]);
            }
        });
    }

    protected VBox getVbox(){
        HBox hbox = new HBox(10, chooseOutputBt, openOutputDirBt);

        return new VBox(10, labelFileURL, fileURL, labelOutput, hbox, progressBar, executeBt, progressText);
    }
}
