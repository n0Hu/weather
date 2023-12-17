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
    //если нет данных в бд то записать
    public void writeWeatherData(Weather weather) {
        try {
            String insertDataQuery = "INSERT INTO weather_data (city, temperature, wet, speed_wind, weather, pressure) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = this.connection.prepareStatement(insertDataQuery);
            insertStatement.setString(1, weather.getLocation().getName().toString());
            insertStatement.setDouble(2, weather.getTemperature().getValue());
            insertStatement.setDouble(3, weather.getHumidity().getValue());
            insertStatement.setDouble(4, weather.getWind().getSpeed());
            insertStatement.setString(5, weather.getWeatherState().toString());
            insertStatement.setDouble(6, weather.getAtmosphericPressure().getValue());
            insertStatement.executeUpdate();

            System.out.println("Данные успешно записаны в базу данных!");

            // Закрытие соединения с базой данных
            insertStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //если есть данные в бд то обновить
    public String retrieveWeatherDataFromDatabase() {
        StringBuilder weatherData = new StringBuilder();
        try {
            String selectDataQuery = "SELECT * FROM weather_data";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectDataQuery);

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                double temperature = resultSet.getDouble("temperature");
                double humidity = resultSet.getDouble("wet");
                double windSpeed = resultSet.getDouble("speed_wind");
                String weatherState = resultSet.getString("weather");
                double pressure = resultSet.getDouble("pressure");

                // Формирование строки с данными погоды
                String weatherInfo = String.format("City: %s, Temperature: %.2f°C, Humidity: %.2f%%, " +
                                "Wind Speed: %.2f m/s, Weather: %s, Pressure: %.2f hPa\n",
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
