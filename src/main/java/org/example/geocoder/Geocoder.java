package org.example.geocoder;

import okhttp3.HttpUrl;
import org.example.requester.Requester;
import org.json.JSONObject;

import java.util.Map;

public class Geocoder {
    private final String BASE_URL = "https://geocode-maps.yandex.ru/1.x";
    private final String API_KEY;

    public Geocoder(String apiKey) {
        API_KEY = apiKey;
    }
    public Coordinates getCityCoordinates(String cityName) {
        try {
            String responseBody = new Requester().send(
                    BASE_URL,
                    Map.of(
                            "apikey", API_KEY,
                            "format", "json",
                            "lang", "en",
                            "geocode", cityName
                    ),
                    Map.of()
            );

            JSONObject json = new JSONObject(responseBody);
            String pos = json.getJSONObject("response")
                    .getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember")
                    .getJSONObject(0)
                    .getJSONObject("GeoObject")
                    .getJSONObject("Point")
                    .getString("pos");

            String[] coords = pos.split(" ");
            return new Coordinates(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        } catch (Exception e) {
            System.out.println("[ERR] не смогли получить координаты: " + e.getMessage());
            return null;
        }
    }

}
