package com.julioreigen.ope.onlineplayerextractor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorController {
    public ExtractorController() {
    }
    protected static Process ffmpegProcess;

    protected Task<Void> createExtractionTask(String inputFile, String outputFile, ProgressBar progressBar, TextArea progressText, Button stopButton, AtomicBoolean isStoppedByUser) {
        return new Task<>() {
            @Override
            protected Void call() {
                try {
                    String os = System.getProperty("os.name").toLowerCase();

                    ProcessBuilder processBuilder = getProcessBuilder(os, inputFile, outputFile);
                    ffmpegProcess = processBuilder.start();


                    BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream()));
                    String line;
                    Pattern durationPattern = Pattern.compile("(?<=Duration: )[^,]*");
                    Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");

                    String duration = null;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        String finalLine = line;
                        Platform.runLater(() -> progressText.setText(finalLine));

                        if (duration == null) {
                            Matcher matcher = durationPattern.matcher(line);
                            if (matcher.find()) {
                                duration = matcher.group();
                            }
                        }

                        if (duration != null) {
                            Matcher matcher = timePattern.matcher(line);
                            if (matcher.find()) {
                                String currentTime = matcher.group();
                                double progress = calculateProgress(duration, currentTime);
                                Platform.runLater(() -> progressBar.setProgress(progress));
                            }
                        }
                    }
                    int exitCode = ffmpegProcess.waitFor();
                    if (exitCode == 0 && !isStoppedByUser.get()) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Extraction completed!");
                            alert.setHeaderText(null);
                            alert.setContentText("Video was successfully extracted to the output directory!");
                            alert.showAndWait();
                        });
                    } else if (!isStoppedByUser.get()) {
                        Platform.runLater(() -> {
                            System.out.println(isStoppedByUser.get());
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Extraction Error");
                            alert.setHeaderText(null);
                            alert.setContentText("An error ocurred! Maybe your URL doesn't work.\nLook at the logs in the bottom!\nExit code: " + exitCode);
                            alert.showAndWait();
                     });
                    }
                } catch (IOException | InterruptedException e) {
                    if (!isStoppedByUser.get()) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Exception Occurred");
                            alert.setHeaderText(null);
                            alert.setContentText("An error occurred during the OPE process:\n" + e.getMessage());
                            alert.showAndWait();
                        });
                    }
                }finally {
                    stopButton.setDisable(true);
                }
                return null;
            }
        };
    }

    private static ProcessBuilder getProcessBuilder(String os, String inputFile, String outputFile) {
        String command = "ffmpeg -protocol_whitelist file,http,https,tcp,tls,crypto -i \"" + inputFile + "\" -c copy \"" + outputFile + "\"";
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            processBuilder = new ProcessBuilder("bash", "-c", command);
        }

        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    private double calculateProgress(String duration, String currentTime) {
        double totalSeconds = timeToSeconds(duration);
        double currentSeconds = timeToSeconds(currentTime);
        return currentSeconds / totalSeconds;
    }

    private double timeToSeconds(String time) {
        if (time == null || time.isEmpty()) {
            return 0.0;
        }
        String[] parts = time.split(":");
        double hours = Double.parseDouble(parts[0]);
        double minutes = Double.parseDouble(parts[1]);
        double seconds = Double.parseDouble(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }

    public void openDir(File file){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")){
            try {
                Runtime.getRuntime().exec("explorer /select, " + file.getParent());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (os.contains("mac")) {
            try {
                Runtime.getRuntime().exec("open -R " + file.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Runtime.getRuntime().exec("xdg-open " + file.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
