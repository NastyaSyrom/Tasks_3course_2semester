package com.example.taskweb;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import java.io.IOException;


import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Класс HelloController
 */
public class HelloController {
    public WebView webview;
    public HTMLEditor htmleditor;
    public TextArea textarea;
    public TextField fileURL;

    /**
     * Метод onOpen для открития hml
     * @param actionEvent
     * @throws FileNotFoundException
     */
    public void onOpen(ActionEvent actionEvent) throws FileNotFoundException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(textarea.getScene().getWindow());
        if (file != null) {
            load(file);
        }
    }

    public void onOpenJS(ActionEvent actionEvent) throws FileNotFoundException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите CSS-файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSS Files", "*.css"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Чтение содержимого CSS-файла
            String cssFile = selectedFile.getAbsolutePath();
            StringBuilder cssContent = new StringBuilder();
            try (Scanner scanner = new Scanner(new FileInputStream(cssFile), StandardCharsets.UTF_8);) {
                while (scanner.hasNextLine()) {
                    cssContent.append(scanner.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                // Обработка ошибки
            }

            // Загрузка CSS-файла
            String html = "<html><head><style>" + cssContent + "</style></head><body><hr></body></html>";
            System.out.println(html);
            htmleditor.setHtmlText(html);
            webview.getEngine().loadContent(html);
            textarea.setText(html);
        }
    }

    //загрузка из выбранного файла
    private void load (File file){
        try (Scanner input = new Scanner(file, StandardCharsets.UTF_8)) {
            StringBuilder htmlContent = new StringBuilder();
            while (input.hasNextLine()) {
                htmlContent.append(input.nextLine()).append("\n");
            }
            String html = htmlContent.toString();
            htmleditor.setHtmlText(html);
            webview.getEngine().loadContent(html);
            textarea.setText(html);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод onChange для внесения изменений
     * @param actionEvent
     */
    //внесение изменений в редакторе
    public void onChange(ActionEvent actionEvent) {
        textarea.setText(htmleditor.getHtmlText());
        webview.getEngine().loadContent(htmleditor.getHtmlText());
    }

    /**
     * Метод onSave для сохранения
     * @param actionEvent
     */
    public void onSave(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(textarea.getScene().getWindow());
        if (file != null) {
            String content = htmleditor.getHtmlText();
            saveToFile(content, file);
        }

    }
    private void saveToFile(String content, File file) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод onURL для открытия URL
     * @param actionEvent
     */
    public void onURL(ActionEvent actionEvent) {
        String url = fileURL.getText();
        if (isValidUrl(url)) {
            try {
                URL pageUrl = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(pageUrl.openStream()));
                StringBuilder htmlContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line).append("\n");
                }
                reader.close();
                String html = htmlContent.toString();
                htmleditor.setHtmlText(html);
                webview.getEngine().loadContent(html);
                textarea.setText(html);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidUrl(String url) {
        // Add your URL validation logic here
        return url != null && !url.isEmpty();

    }



}