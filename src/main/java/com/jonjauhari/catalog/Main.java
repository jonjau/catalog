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
 * Main JavaFX App
 */
public class Main extends Application {

    // Data sources for the application
    private ExhibitionRepository exhibitionRepo;
    private ArtifactRepository artifactRepo;

    @Override
    public void start(Stage stage) throws IOException {

        initializeDataSources();

        // Load the main dashboard view, injecting the data sources to the controller in charge
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/dashboardScene.fxml");
        loader.setController(new DashboardController(artifactRepo, exhibitionRepo));
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        // display the scene on a stage (window)
        stage.setTitle("Catalog");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Sets up a database connection, reading the URL and credentials from a config file
     * called "application.properties", then initialises the repositories with that connection.
     *
     * @throws IOException If there was a failure in loading the config file
     */
    private void initializeDataSources() throws IOException {
        // Read in properties from file
        Properties properties = new Properties();
        try (var inStream = getClass().getResourceAsStream("/application.properties")) {
            properties.load(inStream);
        }
        var database = new Database(
                properties.getProperty("DB_URL"),
                properties.getProperty("DB_USERNAME"),
                properties.getProperty("DB_PASSWORD")
        );
        exhibitionRepo = new ExhibitionRepository(database);
        artifactRepo = new ArtifactRepository(database, exhibitionRepo);
    }

    public static void main(String[] args) {
        launch();
    }
}