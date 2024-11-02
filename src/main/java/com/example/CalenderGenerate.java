package com.example;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CalenderGenerate {

    public static void createICalFile(JSONArray oldGames, JSONArray upcomingGames) throws Exception {
        StringBuilder icalContent = new StringBuilder();

        // iCalendar Header
        icalContent.append("BEGIN:VCALENDAR\n");
        icalContent.append("VERSION:2.0\n");
        icalContent.append("CALSCALE:GREGORIAN\n");
        icalContent.append("PRODID:-//Calender//iCal4j 1.0//EN\n");

        // Spiele aus oldGames und upcomingGames hinzufügen
        addGamesToICal(icalContent, oldGames);
        addGamesToICal(icalContent, upcomingGames);

        // iCalendar Footer
        icalContent.append("END:VCALENDAR\n");

        // Speichern der iCal-Datei
        try (FileOutputStream fout = new FileOutputStream("calenderOutput/newCal.ics");
             OutputStreamWriter writer = new OutputStreamWriter(fout, StandardCharsets.UTF_8)) {
            writer.write(icalContent.toString());
        }
    }

    private static void addGamesToICal(StringBuilder icalContent, JSONArray games) {
        for (int i = 0; i < games.length(); i++) {
            JSONObject game = games.getJSONObject(i);

            String homeTeam = game.getJSONObject("homeTeam").getString("name");
            String awayTeam = game.getJSONObject("awayTeam").getString("name");

            // Event Summary
            String summary;
            String homeScore = "";
            String awayScore = "";
            try {
                homeScore = String.valueOf(game.getJSONObject("homeScore").getInt("display"));
                awayScore = String.valueOf(game.getJSONObject("awayScore").getInt("display"));
            } catch (Exception ignored) {
            }

            if (homeScore.isEmpty() && awayScore.isEmpty()) {
                summary = homeTeam + " - " + awayTeam;
            } else {
                summary = homeTeam + " (" + homeScore + ") - " + awayTeam + " (" + awayScore + ")";
            }

            // Startzeitpunkt als ZonedDateTime
            long timestamp = game.getLong("startTimestamp");
            ZonedDateTime startDateTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp),
                    ZoneId.of("Europe/Istanbul")
            );

            // Endzeitpunkt
            ZonedDateTime endDateTime = startDateTime.plusHours(2);

            // Erstellen des VEVENT
            icalContent.append("BEGIN:VEVENT\n");
            icalContent.append("UID:").append(UUID.randomUUID().toString()).append("\n");
            icalContent.append("SUMMARY:").append(summary).append("\n");
            icalContent.append("DTSTART:").append(formatDateTime(startDateTime)).append("\n");
            icalContent.append("DTEND:").append(formatDateTime(endDateTime)).append("\n");
            icalContent.append("END:VEVENT\n");
        }
    }

    private static String formatDateTime(ZonedDateTime dateTime) {
        return dateTime.toString().replace("-", "").replace(":", "").substring(0, 15) + "Z";
    }
}
