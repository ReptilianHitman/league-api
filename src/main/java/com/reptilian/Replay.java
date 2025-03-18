package com.reptilian;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Replay extends Application {
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        API api = new API();

        TextArea matchIDInput = new TextArea();
        matchIDInput.setMaxWidth(140);
        matchIDInput.setMinHeight(26);
        matchIDInput.setMaxHeight(26);
        matchIDInput.setLayoutX(SCREEN_WIDTH / 2 - matchIDInput.getMaxWidth() / 2);
        matchIDInput.setLayoutY(SCREEN_HEIGHT / 2 - matchIDInput.getMaxHeight() / 2);

        pane.getChildren().addAll(matchIDInput);

        JsonNode timeline = api.getMatchTimeline("EUW1_7339417014");

        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Replay");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
