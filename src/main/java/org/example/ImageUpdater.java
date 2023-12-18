package org.example;

import javax.swing.*;
import java.awt.*;

public class ImageUpdater {
    public static void updateImage(JLabel imageLabel, String weatherStatus) {
        String imagePath = "overcastlday_sun.png";

        switch (weatherStatus) {
            case "Clouds":
                imagePath = "clouds.png";
                break;
            case "Clear":
                imagePath = "clear_sun.png";
                break;
            case "Snow":
                imagePath = "snow.png";
                break;
            case "Rain":
                imagePath = "rain_cloud.png";
                break;
            default:
                imagePath = "fail.png";
                break;
        }

        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
        imageLabel.setIcon(resizedImageIcon);
    }
}
