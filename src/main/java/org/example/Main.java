package org.example;

import org.example.geocoder.Coordinates;
import org.example.geocoder.Geocoder;
import org.example.weather_report.TemperatureInfo;
import org.example.weather_report.WeatherReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String GEO_API_KEY;
        String WEATHER_API_KEY;
        try {
            BufferedReader stream = new BufferedReader(new FileReader(".apikeys"));
            GEO_API_KEY = stream.readLine();
            WEATHER_API_KEY = stream.readLine();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);

        String city = "";
        while (city.trim().isEmpty()) {
            System.out.print("В каком городе хотите узнать погоду? > ");
            city = scanner.nextLine();
        }

        System.out.println("Выбранный город: " + city);

        Geocoder geocoder = new Geocoder(GEO_API_KEY);
        Coordinates coords = geocoder.getCityCoordinates(city);

        System.out.println(coords);

        int limit = 0;
        while (limit <= 0) {
            System.out.print("\nПрогноз на сколько дней вас интересует? > ");
            limit = scanner.nextInt();
        }
        scanner.close();

        WeatherReport weatherReport = new WeatherReport(WEATHER_API_KEY);
        String response = weatherReport.getForecast(coords, limit);

        System.out.println(response);
        TemperatureInfo info = weatherReport.getTempInfo(response);

        System.out.printf("Температура сейчас: %d C\n", info.currentTemp());
        System.out.printf("Средняя дневная температура на следующие %d дней составляет %.2f C\n", limit, info.avgTemp());
    }


}

//curl -H "Content-Type: application/json" https://geocode-maps.yandex.ru/1.x?apikey=74ccaac1-8d86-48e0-b84d-fc364b673c11&geocode=moscow&lang=en&format=json