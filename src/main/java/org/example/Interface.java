package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {
    private JFrame frame;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea outputArea;
    private JLabel imageLabel;
    private DatabaseButton databaseButton;

    public Interface() {
        frame = new JFrame("Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setSize(1280, 720);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Поиск");

        JPanel centerPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        JPanel outputPanel = new JPanel(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        String imagePath = "snow.png";
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
        imageLabel.setIcon(resizedImageIcon);

        JPanel buttonPanel = new JPanel();
        databaseButton = new DatabaseButton("Search in DB");

        // Добавляем слушателей на элементы =>
        SearchButton changeSearchButton = new SearchButton(searchField, outputArea);
        searchButton.addActionListener(changeSearchButton);

        // Добавление элементов в панели =>
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        imagePanel.add(imageLabel);
        centerPanel.add(outputPanel, BorderLayout.CENTER);
        centerPanel.add(imagePanel, BorderLayout.WEST);
        buttonPanel.add(databaseButton);

        // Добавление панелей на экран =>
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.pack();

        int frameWidth = frame.getSize().width;
        int outputPanelHeight = outputPanel.getPreferredSize().height;
        int imagePanelHeight = imagePanel.getPreferredSize().height;
        int verticalGap = (frame.getSize().height - outputPanelHeight - imagePanelHeight) / 3;
        centerPanel.setBorder(BorderFactory.createEmptyBorder(verticalGap, 0, verticalGap, 0));
        frame.setMinimumSize(frame.getSize());
    }

    private void showDatabaseSearchDialog() {
        JFrame dialogFrame = new JFrame("Search in DB");
        dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialogFrame.setLayout(new BorderLayout());
        dialogFrame.setSize(300, 100);
        dialogFrame.setLocationRelativeTo(frame);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Enter search query:");
        JTextField queryField = new JTextField(10);
        JButton searchInDbButton = new JButton("Поиск");


        inputPanel.add(label);
        inputPanel.add(queryField);
        inputPanel.add(searchInDbButton);

        //searchButton = new SearchButton("Поиск");
        //searchButton.addActionPerformed(new SearchButton(searchButton));

        dialogFrame.add(inputPanel, BorderLayout.CENTER);
        dialogFrame.setVisible(true);
    }

    private void performDatabaseSearch(String query) {
        // Здесь обрабатывается запрос в базу данных и обновляются данные в outputArea
        String result = "Результат поиска по запросу \"" + query + "\"";
        outputArea.setText(result); // Обновляем данные в outputArea
    }




    private class DatabaseButton extends JButton {
        public DatabaseButton(String text) {
            super(text);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showDatabaseSearchDialog();
                }
            });
        }
    }
    private class searchInDbButton extends JButton {
        public searchInDbButton(String text) {
            super(text);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Interface::new);
    }
}