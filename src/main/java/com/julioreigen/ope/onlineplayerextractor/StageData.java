package com.julioreigen.ope.onlineplayerextractor;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.julioreigen.ope.onlineplayerextractor.ExtractorController.ffmpegProcess;


public class StageData {
    private Stage st;
    private File lastDirectory;
    private final ExtractorController ec = new ExtractorController();
    private Label labelFileURL, labelOutput;
    private TextArea progressText;
    private TextField fileURL;
    private Button chooseOutputBt, openOutputDirBt, executeBt, stopBt;
    private ProgressBar progressBar;
    private FileChooser fileChooser;
    private File[] outputFile;
    private final AtomicBoolean isStoppedByUser = new AtomicBoolean(false);

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
        stopBt = new Button("Stop");

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

            File chosenFile = fileChooser.showSaveDialog(st);

            if (chosenFile != null) {
                outputFile[0] = getUniqueFile(chosenFile);
                lastDirectory = outputFile[0].getParentFile();
                labelOutput.setText("Output File: " + outputFile[0].getAbsolutePath());
                executeBt.setDisable(false);
                openOutputDirBt.setDisable(false);
            }
        });

        executeBt.setDisable(true);
        openOutputDirBt.setDisable(true);
        stopBt.setDisable(true);

        executeBt.setOnAction(e -> {
            isStoppedByUser.set(false);
            String input = fileURL.getText();
            if (outputFile[0] != null) {
                Task<Void> task = ec.createExtractionTask(input, outputFile[0].getAbsolutePath(), progressBar, progressText, stopBt, isStoppedByUser);
                stopBt.setDisable(false);
                new Thread(task).start();
            }
        });

        openOutputDirBt.setOnAction(e -> {
            if (outputFile[0] != null) {
                ec.openDir(outputFile[0]);
            }
        });

        stopBt.setOnAction(e -> {
            if (ffmpegProcess != null) {
                isStoppedByUser.set(true);
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    ffmpegProcess.descendants().forEach(ProcessHandle::destroyForcibly);
                    ffmpegProcess.destroyForcibly();
                } else {
                    ffmpegProcess.destroy();
                }
                stopBt.setDisable(true);
            }
        });

        st.setOnCloseRequest(e -> {
            if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
                ffmpegProcess.destroy();
            }
        });
    }

    private File getUniqueFile(File file) {
        String fileName = file.getName();
        String fileDir = file.getParent();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        int count = 1;
        File uniqueFile = new File(fileDir, fileName);

        while (uniqueFile.exists()) {
            uniqueFile = new File(fileDir, baseName + "_" + count + extension);
            count++;
        }

        return uniqueFile;
    }

    protected VBox getVbox(){
        HBox hbox = new HBox(10,  chooseOutputBt, openOutputDirBt);
        HBox hbox2 = new HBox(15, executeBt, stopBt);

        return new VBox(10, labelFileURL, fileURL, labelOutput, hbox, progressBar, hbox2, progressText);
    }
}
