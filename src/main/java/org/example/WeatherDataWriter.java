package org.example;

import com.github.prominence.openweathermap.api.model.weather.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeatherDataWriter {
    Database database;
    Connection connection;

    public WeatherDataWriter() {
        this.database = Database.getInstance();
        this.connection = this.database.getConnection();
    }
    //Запись в бд и обновление
    public void writeWeatherData(Weather weather) {
        try {
            String city = weather.getLocation().getName().toString();
            if (isCityExists(city)) {
                updateWeatherData(weather);
                System.out.println("Данные успешно обновлены в базе данных для города: " + city);
            } else {
                insertWeatherData(weather);
                System.out.println("Данные успешно записаны в базу данных для нового города: " + city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isCityExists(String city) throws SQLException {
        String selectCityQuery = "SELECT city FROM weather_data WHERE city = ?";
        PreparedStatement selectCityStatement = connection.prepareStatement(selectCityQuery);
        selectCityStatement.setString(1, city);

        ResultSet resultSet = selectCityStatement.executeQuery();
        boolean cityExists = resultSet.next();
        resultSet.close();
        selectCityStatement.close();

        return cityExists;
    }

    private void updateWeatherData(Weather weather) throws SQLException {
        String updateDataQuery = "UPDATE weather_data SET temperature = ?, wet = ?, speed_wind = ?, " +
                "weather = ?, pressure = ? WHERE city = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateDataQuery);
        updateStatement.setDouble(1, weather.getTemperature().getValue());
        updateStatement.setDouble(2, weather.getHumidity().getValue());
        updateStatement.setDouble(3, weather.getWind().getSpeed());
        updateStatement.setString(4, weather.getWeatherState().toString());
        updateStatement.setDouble(5, weather.getAtmosphericPressure().getValue());
        updateStatement.setString(6, weather.getLocation().getName().toString());
        updateStatement.executeUpdate();

        updateStatement.close();
    }

    private void insertWeatherData(Weather weather) throws SQLException {
        String insertDataQuery = "INSERT INTO weather_data (city, temperature, wet, speed_wind, weather, pressure) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertDataQuery);
        insertStatement.setString(1, weather.getLocation().getName().toString());
        insertStatement.setDouble(2, weather.getTemperature().getValue());
        insertStatement.setDouble(3, weather.getHumidity().getValue());
        insertStatement.setDouble(4, weather.getWind().getSpeed());
        insertStatement.setString(5, weather.getWeatherState().toString());
        insertStatement.setDouble(6, weather.getAtmosphericPressure().getValue());
        insertStatement.executeUpdate();

        insertStatement.close();
    }
    //Вывод
    public String retrieveWeatherDataFromDatabase(String city) {
        StringBuilder weatherData = new StringBuilder();
        try {
            String selectDataQuery = "SELECT * FROM weather_data WHERE city = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectDataQuery);
            selectStatement.setString(1, city);

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                double temperature = resultSet.getDouble("temperature");
                double humidity = resultSet.getDouble("wet");
                double windSpeed = resultSet.getDouble("speed_wind");
                String weatherState = resultSet.getString("weather");
                double pressure = resultSet.getDouble("pressure");

                // Формирование строки с данными погоды
                String weatherInfo = String.format("\nГород: %s\nТемпература: %.2f°C\nВлажность: %.2f%%\n" +
                                "Скорость ветра: %.2f м/с\nПогода: %s\nДавление: %.2f гПа\n",
                        city, temperature, humidity, windSpeed, weatherState, pressure);

                weatherData.append(weatherInfo);
            }

            // Закрытие соединения с базой данных
            resultSet.close();
            selectStatement.close();
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherData.toString();
    }
}
