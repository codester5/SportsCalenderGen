package com.example;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class CalenderAPI {
    private static final String API_URL_oldMatches = System.getenv("API_URL_oldMatches"); // Basis-URL
    private static final String API_URL_upcomingMatches = System.getenv("API_URL_upcomingMatches"); // Basis-URL
    private static final String API_KEY = System.getenv("API_KEY"); // API-Key aus Umgebungsvariablen lesen
    private static final String API_HOST = System.getenv("API_HOST"); // API-Host
    private static final String TEAM_ID = System.getenv("TEAM_ID"); // Team-ID

    public static JSONArray getOldGames() throws Exception {
        String urlString_oldMatches = API_URL_oldMatches + "?teamId=" + TEAM_ID + "&pageIndex=0";
        StringBuilder responseOld = readAPIResponse(urlString_oldMatches);
        JSONObject jsonResponseOld = new JSONObject(responseOld.toString());
        return jsonResponseOld.getJSONArray("events");
    }

    public static JSONArray getUpcomingGames() throws Exception {
        String urlString_upcomingMatches = API_URL_upcomingMatches + "?teamId=" + TEAM_ID + "&pageIndex=0";
        StringBuilder responseNew = readAPIResponse(urlString_upcomingMatches);
        JSONObject jsonResponseNew = new JSONObject(responseNew.toString());
        return jsonResponseNew.getJSONArray("events");
    }

    private static StringBuilder readAPIResponse(String urlString) throws Exception {
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-rapidapi-key", API_KEY);
            connection.setRequestProperty("x-rapidapi-host", API_HOST);
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to get data from API: " + responseCode);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed URL: " + e.getMessage());
        }
    }
}
