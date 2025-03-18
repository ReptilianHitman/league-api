package com.reptilian;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.StreamSupport;

public class API {
    private final String API_KEY = System.getenv("RIOT_API_KEY");
    private final String BASE_URL = "https://europe.api.riotgames.com/";
    private HttpClient client;
    private HttpRequest request;

    public API() {
        client = HttpClient.newHttpClient();
    }

    public String getPUUID(String user, String tag) throws URISyntaxException, IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "riot/account/v1/accounts/by-riot-id/" + user + "/" + tag))
                .header("X-Riot-Token", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode node = new ObjectMapper().readTree(response.body());

        return node.get("puuid").asText();
    }

    public List<String> getMatches(String puuid, int count) throws URISyntaxException, IOException, InterruptedException {
        return getMatches(puuid, 0, count);
    }
    public List<String> getMatches(String user, String tag, int count) throws URISyntaxException, IOException, InterruptedException {
        return getMatches(user, tag, 0, count);
    }
    public List<String> getMatches(String user, String tag, int start, int count) throws URISyntaxException, IOException, InterruptedException {
        return getMatches(this.getPUUID(user, tag), start, count);
    }
    public List<String> getMatches(String puuid, int start, int count) throws URISyntaxException, IOException, InterruptedException {
        start = Math.max(start, 0);
        count = Math.max(Math.min(count, 100), 0);

        request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count))
                .header("X-Riot-Token", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayNode array = (ArrayNode) new ObjectMapper().readTree(response.body());

        return StreamSupport.stream(array.spliterator(), false).map(JsonNode::asText).toList();
    }

    public String getLastMatch(String user, String tag) throws URISyntaxException, IOException, InterruptedException {
        return getLastMatch(this.getPUUID(user, tag));
    }
    public String getLastMatch(String puuid) throws URISyntaxException, IOException, InterruptedException {
        return getMatches(puuid, 1).getFirst();
    }

    public JsonNode getMatchInfo(String matchID) throws URISyntaxException, IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "lol/match/v5/matches/" + matchID))
                .header("X-Riot-Token", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper().readTree(response.body());
    }

    public JsonNode getMatchTimeline(String matchID) throws URISyntaxException, IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "lol/match/v5/matches/" + matchID + "/timeline"))
                .header("X-Riot-Token", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper().readTree(response.body());
    }
}
