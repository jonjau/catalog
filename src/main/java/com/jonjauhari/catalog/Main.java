package com.jonjauhari.catalog;

import com.jonjauhari.catalog.model.Artifact;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


/**
 * JavaFX App
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Catalog");
        FXMLLoader loader = new FXMLLoader();
        System.out.println("get" + getClass());
        URL xmlUrl = getClass().getResource("/dashboardScene.fxml");
        System.out.println(xmlUrl);
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}