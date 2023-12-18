package org.example;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.weather.Weather;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButton implements ActionListener {
    private JLabel imageLabel;
    private JTextField searchField;
    private JTextArea outputArea;
    private String weatherStatus;


    public SearchButton(JTextField searchField, JTextArea outputArea, JLabel imageLabel, String weatherStatus) {
        this.searchField = searchField;
        this.outputArea = outputArea;
        this.imageLabel = imageLabel;
        this.weatherStatus = weatherStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Проверка на пустую строку
        if (searchField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Поле поиска пустое", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Создаем запрос
        Thread apiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Запрос API
                OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("73c47404afee24ebcb253b86af8da321");
                final Weather weather = openWeatherClient
                        .currentWeather()
                        .single()
                        .byCityName(searchField.getText())
                        .language(Language.RUSSIAN)
                        .unitSystem(UnitSystem.METRIC)
                        .retrieve()
                        .asJava();

                // Запись в бд
                WeatherDataWriter weatherDataWriter = new WeatherDataWriter();
                weatherDataWriter.writeWeatherData(weather);

                // Обновляем поле outputArea
                String weatherData = weatherDataWriter.retrieveWeatherDataFromDatabase(searchField.getText());
                String[] lines = weatherData.split("\n");

                for (String line : lines) {
                    if (line.contains("Погода: ")) {
                        String weatherLine = line.replace("Погода: ", "").trim();
                        int startIndex = weatherLine.indexOf("Weather state: ") + "Weather state: ".length();
                        int endIndex = weatherLine.indexOf("(");
                        weatherStatus = weatherLine.substring(startIndex, endIndex).trim();
                        break;

                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        outputArea.setText(weatherData);
                        searchField.setText("");
                        ImageUpdater.updateImage(imageLabel, weatherStatus);

                    }
                });
            }
        });

        // Запуск API
        apiThread.start();
    }
}