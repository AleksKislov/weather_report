package org.example.weather_report;

import org.example.geocoder.Coordinates;
import org.example.requester.Requester;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class WeatherReport {
    private final String BASE_URL = "https://api.weather.yandex.ru/v2/forecast";
    private final String API_KEY;

    public WeatherReport(String apiKey) {
        API_KEY = apiKey;
    }

    public String getForecast(Coordinates coords, int limit) {
        try {
            String responseBody = new Requester().send(
                    BASE_URL,
                    Map.of(
                            "lat", Double.toString(coords.latitude()),
                            "lon", Double.toString(coords.longitude()),
                            "limit", Integer.toString(limit)
                    ),
                    Map.of(
                            "X-Yandex-Weather-Key", API_KEY
                    )
            );

            return responseBody;
        } catch (Exception e) {
            System.out.println("[ERR] не смогли получить прогноз погоды: " + e.getMessage());
            return null;
        }
    }

    public TemperatureInfo getTempInfo(String json) {
        JSONObject obj = new JSONObject(json);
        int currentTemp = obj.getJSONObject("fact").getInt("temp");

        JSONArray forecasts = obj.getJSONArray("forecasts");

        int allDaysTemp = 0;
        for (Object forecastObj : forecasts) {
            JSONObject forecast = (JSONObject) forecastObj;
            int avgDayTemp = forecast
                    .getJSONObject("parts")
                    .getJSONObject("day")
                    .getInt("temp_avg");

            allDaysTemp += avgDayTemp;
        }

        float avgTemp = (float) allDaysTemp / forecasts.length();

        return new TemperatureInfo(currentTemp, avgTemp);
    }
}
