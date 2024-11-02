package com.example;
import org.json.JSONArray;

public class Main {
    public static void main(String[] args) {
        try {
            // Spiele von der API abrufen
            JSONArray oldMatches = CalenderAPI.getOldGames();
            JSONArray upcomingMatches = CalenderAPI.getUpcomingGames();

            // iCal-Datei erstellen mit alten und kommenden Spielen
            CalenderGenerate.createICalFile(oldMatches, upcomingMatches);
            System.out.println("iCal-Datei wurde erfolgreich erstellt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
