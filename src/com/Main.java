package com;

import com.config.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Paths;

public class Main extends Application {
    public static Configuration CONFIG;
    private static final String path = Paths.get("").toAbsolutePath().toString();
    private static final File configFile = new File(path.concat("\\").concat("src\\com\\").concat("config.yaml"));
    private final static InputStream embeddedConfigFileStream = Main.class.getResourceAsStream("config.yaml");
    private final static File userConfigFile = new File(Paths.get("").toAbsolutePath().toString().concat("/config.yaml"));
//    private static File configFile;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Card Generator");
        primaryStage.setScene(new Scene(root, 650, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        loadConfig();
        launch(args);
    }

    public static void loadConfig() {
        System.out.println("user file path : "+userConfigFile.toPath().toString());
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            if (userConfigFile.exists())
                CONFIG = objectMapper.readValue(userConfigFile, Configuration.class);
            else
                CONFIG = objectMapper.readValue(embeddedConfigFileStream, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream getConfigFile() throws FileNotFoundException {
        return new FileOutputStream(userConfigFile);
    }
}
