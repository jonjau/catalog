package com.jonjauhari.catalog;

import com.jonjauhari.catalog.controller.DashboardController;
import com.jonjauhari.catalog.repository.ArtifactRepository;
import com.jonjauhari.catalog.repository.Database;
import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;


/**
 * JavaFX App
 */
public class Main extends Application {

    private ExhibitionRepository exhibitionRepo;
    private ArtifactRepository artifactRepo;

    @Override
    public void start(Stage stage) throws IOException {

        initializeDataSources();

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/dashboardScene.fxml");
        loader.setController(new DashboardController(artifactRepo, exhibitionRepo));
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        stage.setTitle("Catalog");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void initializeDataSources() throws IOException {
        Properties properties = loadConfig();
        var database = new Database(
                properties.getProperty("DB_URL"),
                properties.getProperty("DB_USERNAME"),
                properties.getProperty("DB_PASSWORD")
        );
        exhibitionRepo = new ExhibitionRepository(database);
        artifactRepo = new ArtifactRepository(database, exhibitionRepo);
    }

    private Properties loadConfig() throws IOException {
        Properties properties = new Properties();
        // Read properties
        try (var inStream = getClass().getResourceAsStream("/application.properties")) {
            properties.load(inStream);
        }
        return properties;
    }

    public static void main(String[] args) {
        launch();
    }

}