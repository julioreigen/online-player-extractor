package com.julioreigen.ope.onlineplayerextractor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StageData sd = new StageData(primaryStage);
        sd.configure();
        Scene scene;

        if (System.getProperty("os.name").toLowerCase().contains("win"))
            scene = new Scene(sd.getVbox(), 450, 250);
        else
            scene = new Scene(sd.getVbox(), 650, 250);


        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Icon.png"))));
        primaryStage.setTitle("Online Player Extractor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
