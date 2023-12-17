package org.example;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.weather.Weather;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButton implements ActionListener {
    private JTextField searchField;
    private JTextArea outputArea;


    public SearchButton(JTextField searchField, JTextArea outputArea) {
        this.searchField = searchField;
        this.outputArea = outputArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Create a new thread to perform the API request and database write operation
        Thread apiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform the API request and obtain the weather data
                OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("73c47404afee24ebcb253b86af8da321");
                final Weather weather = openWeatherClient
                        .currentWeather()
                        .single()
                        .byCityName(searchField.getText()) // Use the input from the searchField as the city name
                        .language(Language.RUSSIAN)
                        .unitSystem(UnitSystem.METRIC)
                        .retrieve()
                        .asJava();

                // Write the weather data to the database
                WeatherDataWriter weatherDataWriter = new WeatherDataWriter();
                weatherDataWriter.writeWeatherData(weather);

                // Update the outputArea with the weather data from the database
                String weatherData = weatherDataWriter.retrieveWeatherDataFromDatabase();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        outputArea.setText(weatherData);
                    }
                });
            }
        });

        // Start the API thread
        apiThread.start();
    }
}