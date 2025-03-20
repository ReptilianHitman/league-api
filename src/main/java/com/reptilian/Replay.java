package com.reptilian;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;

public class Replay extends Application {
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        API api = new API();

        // Largest x and y positions = ~14.5k

        TextArea matchIDInput = new TextArea();
        matchIDInput.setMaxWidth(140);
        matchIDInput.setMinHeight(26);
        matchIDInput.setMaxHeight(26);
        matchIDInput.setLayoutX(SCREEN_WIDTH / 2f - matchIDInput.getMaxWidth() / 2);
        matchIDInput.setLayoutY(SCREEN_HEIGHT / 2f - matchIDInput.getMaxHeight() / 2);

        pane.getChildren().addAll(matchIDInput);

        AtomicReference<JsonNode> timeline = new AtomicReference<>();

        matchIDInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (matchIDInput.getText().isEmpty())
                    matchIDInput.setText("EUW1_7339417014");
                else
                    matchIDInput.setText(matchIDInput.getText().trim());

                try {
                    timeline.set(api.getMatchTimeline("EUW1_7339417014"));
                } catch (URISyntaxException | InterruptedException | IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Replay");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
