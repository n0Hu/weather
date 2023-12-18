/*
package org.example;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.weather.Weather;

public class CustomOpenWeatherMapClient {
    public static void main(String[] args) {
        OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("73c47404afee24ebcb253b86af8da321");
        final Weather weather = openWeatherClient
                .currentWeather()
                .single()
                .byCityName("Minsk")
                .language(Language.RUSSIAN)
                .unitSystem(UnitSystem.METRIC)
                .retrieve()
                .asJava();

        WeatherDataWriter weatherDataWriter = new WeatherDataWriter();
        weatherDataWriter.writeWeatherData(weather);
    }
}*/
