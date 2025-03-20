package com.reptilian;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
public class Main extends Application {
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;
    private final String DEFAULT_USER = "ReptilianHitman#EUW";

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        API api = new API();
        AtomicReference<List<String>> matchIDs = new AtomicReference<>();
        AtomicReference<String> userPuuid = new AtomicReference<>();
        Map<String, JsonNode> matchMap = new HashMap<>();

        TextArea usernameInput = new TextArea();
        usernameInput.setLayoutX(20);
        usernameInput.setLayoutY(20);
        usernameInput.setMaxWidth(248);
        usernameInput.setMinHeight(26);
        usernameInput.setMaxHeight(26);

        Button getMatchesButton = new Button("Get Matches");
        getMatchesButton.setLayoutX(280);
        getMatchesButton.setLayoutY(20);

        Button getMatchInfoButton = new Button("Get Match Info");
        getMatchInfoButton.setLayoutX(280);
        getMatchInfoButton.setLayoutY(60);
        getMatchInfoButton.setDisable(true);

        ListView<String> matchList = new ListView<>();
        matchList.setLayoutX(20);
        matchList.setLayoutY(60);

        Label matchInfo0 = new Label(infoToString(null, null, 0));
        matchInfo0.setLayoutX(400);
        matchInfo0.setLayoutY(20);

        Label matchInfo1 = new Label(infoToString(null, null, 1));
        matchInfo1.setLayoutX(600);
        matchInfo1.setLayoutY(20);

        pane.getChildren().add(usernameInput);
        pane.getChildren().add(getMatchesButton);
        pane.getChildren().add(getMatchInfoButton);
        pane.getChildren().add(matchList);
        pane.getChildren().add(matchInfo0);
        pane.getChildren().add(matchInfo1);

        usernameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                getMatchesButton.getOnAction().handle(null);
                return;
            }
            matchList.getItems().clear();
            getMatchInfoButton.setDisable(true);
            matchInfo0.setText(infoToString(null, null, 0));
            matchInfo1.setText(infoToString(null, null, 1));
        });

        getMatchesButton.setOnAction(event -> {
            String[] username = usernameInput.getLength() == 0 ? inputToUserTag(DEFAULT_USER) : inputToUserTag(usernameInput.getText());
            if (usernameInput.getLength() == 0) usernameInput.setText(DEFAULT_USER);

            try {
                userPuuid.set(api.getPUUID(username[0], username[1]));
                matchIDs.set(api.getMatches(userPuuid.get(), 100));
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            matchList.getItems().clear();

            for (int i = 0; i < matchIDs.get().size(); i++) {
                matchList.getItems().add(i + 1 + ": " + matchIDs.get().get(i));
            }

            getMatchInfoButton.setDisable(true);
        });

        matchList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                getMatchInfoButton.getOnAction().handle(null);
            }


            matchList.getOnMouseClicked().handle(null);
        });

        matchList.setOnMouseClicked(event -> {
            if (matchList.getSelectionModel().getSelectedItem() == null)
                return;

            if (matchMap.containsKey(matchIDs.get().get(matchList.getSelectionModel().getSelectedIndex()))) {
                matchInfo0.setText(infoToString(matchMap.get(matchIDs.get().get(matchList.getSelectionModel().getSelectedIndex())), userPuuid.get(), 0));
                matchInfo1.setText(infoToString(matchMap.get(matchIDs.get().get(matchList.getSelectionModel().getSelectedIndex())), userPuuid.get(), 1));
                getMatchInfoButton.setDisable(true);
            } else {
                matchInfo0.setText(infoToString(null, null, 0));
                matchInfo1.setText(infoToString(null, null, 1));
                getMatchInfoButton.setDisable(false);
            }
        });

        getMatchInfoButton.setOnAction(event -> {
            JsonNode node;

            try {
                node = api.getMatchInfo(matchIDs.get().get(matchList.getSelectionModel().getSelectedIndex()));
                matchMap.put(matchIDs.get().get(matchList.getSelectionModel().getSelectedIndex()), node);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            matchInfo0.setText(infoToString(node, userPuuid.get(), 0));
            matchInfo1.setText(infoToString(node, userPuuid.get(), 1));
            getMatchInfoButton.setDisable(true);
        });

        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("LeagueAPI");
        stage.show();
    }

    private String[] inputToUserTag(String input) {
        if (!input.contains("#")) throw new IllegalArgumentException();
        StringBuilder user = new StringBuilder();
        String tag;
        int i = 0;
        char c = input.charAt(i);

        while (c != '#') {
            user.append(c);
            i++;
            c = input.charAt(i);
        }

        tag = input.substring(i + 1);

        return new String[]{user.toString(), tag};
    }

    private String infoToString(JsonNode node, String puuid, int column) {
        switch (column) {
            case 0: {
                if (node == null || puuid == null) {
                    return """
                            Champion:
                            Result:
                            Kills:
                            Death:
                            Assits:
                            Role:
                            """;
                }

                JsonNode playerNode = null;
                JsonNode positionOpponent = null;
                ArrayNode players = (ArrayNode) node.get("metadata").get("participants");

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).asText().equals(puuid)) {
                        playerNode = node.get("info").get("participants").get(i);
                    }
                }

                assert playerNode != null;

                String playerChampion = playerNode.get("championName").asText();
                String result = playerNode.get("win").asBoolean() ? "Win" : "Loss";
                int playerKills = playerNode.get("kills").asInt();
                int playerDeaths = playerNode.get("deaths").asInt();
                int playerAssists = playerNode.get("assists").asInt();
                String playerPosition = playerNode.get("teamPosition").asText();
                for (int i = 0; i < players.size(); i++)
                    if (node.get("info").get("participants").get(i).get("teamPosition").asText().equals(playerPosition) && !node.get("info").get("participants").get(i).get("puuid").asText().equals(puuid))
                        positionOpponent = node.get("info").get("participants").get(i);
                String positionOpponentChampion = positionOpponent == null || positionOpponent.get("championName").asText().isEmpty() ? null : toReadable(positionOpponent.get("championName").asText());
                int positionGoldDifference = 0;

                return """
                        Champion:\t%s
                        Result:\t\t%s
                        Kills:\t\t%d
                        Death:\t\t%d
                        Assits:\t\t%d
                        Role:\t\t%s
                        """
                        .formatted(
                                toReadable(playerChampion),
                                result,
                                playerKills,
                                playerDeaths,
                                playerAssists,
                                toReadable(playerPosition
                                ));
            }
            case 1: {
                if (node == null || puuid == null) {
                    return """
                            Lane Opponent:
                            Gold Difference:
                            """;
                }

                JsonNode playerNode = null;
                JsonNode positionOpponent = null;
                ArrayNode players = (ArrayNode) node.get("metadata").get("participants");

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).asText().equals(puuid)) {
                        playerNode = node.get("info").get("participants").get(i);
                    }
                }

                assert playerNode != null;

                for (int i = 0; i < players.size(); i++)
                    if (node.get("info").get("participants").get(i).get("teamPosition").asText().equals(playerNode.get("teamPosition").asText()) && !node.get("info").get("participants").get(i).get("puuid").asText().equals(puuid)) {
                        positionOpponent = node.get("info").get("participants").get(i);
                        break;
                    }
                String positionOpponentChampion = positionOpponent == null || positionOpponent.get("championName").asText().isEmpty() ? null : toReadable(positionOpponent.get("championName").asText());
                int positionGoldDifference = positionOpponent != null ? playerNode.get("goldEarned").asInt() - positionOpponent.get("goldEarned").asInt() : 0;

                return """
                        Lane Opponent:\t%s
                        Gold Difference:\t%d
                        """
                        .formatted(
                                toReadable(positionOpponentChampion),
                                positionGoldDifference
                        );
            }
            default:
                return "";
        }
    }

    private String toReadable(String convert) {
        if (convert == null) return null;
        return switch (convert) {
            case "TOP" -> "Top";
            case "JUNGLE" -> "Jungle";
            case "MIDDLE" -> "Middle";
            case "BOTTOM" -> "Bottom";
            case "UTILITY" -> "Support";
            case "Belveth" -> "Bel'Veth";
            case "Chogath" -> "Cho'Gath";
            case "DrMundo" -> "Dr Mundo";
            case "JaravanIV" -> "Jarvan IV";
            case "KSante" -> "K'Sante";
            case "Kaisa" -> "Kai'Sa";
            case "Khazix" -> "Kha'Zix";
            case "Kogmaw" -> "Kog'Maw";
            case "Reksai" -> "Rek'Sai";
            case "TwistedFate" -> "Twisted Fate";
            case "Velkoz" -> "Vel'Koz";
            default -> convert;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}