package org.example;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String DATABASE_FILE_PATH = "database.db";
    private static Database instance;
    private Connection connection;

    private Database() {
        initializeDatabase();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() {
        try {
            // Проверка существования базы данных
            boolean databaseExists = new File(DATABASE_FILE_PATH).exists();
            if (!databaseExists) {
                // Подключение к базе данных
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_FILE_PATH);

                // Создание таблицы
                Statement statement = connection.createStatement();
                String createTableQuery = "CREATE TABLE IF NOT EXISTS weather_data ("
                        + "city TEXT,"
                        + "temperature REAL,"
                        + "wet REAL,"
                        + "speed_wind REAL,"
                        + "weather TEXT,"
                        + "pressure REAL"
                        + ")";
                statement.execute(createTableQuery);

                System.out.println("Таблица weather_data успешно создана!");

                // Закрытие соединения с базой данных
                statement.close();
            } else {
                // Если база данных уже существует, просто подключаемся к ней
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_FILE_PATH);

                System.out.println("Подключение к базе данных успешно установлено!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}