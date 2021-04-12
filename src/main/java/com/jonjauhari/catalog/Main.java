package com.jonjauhari.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * JavaFX App
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Catalog");
        stage.setWidth(600);
        stage.setHeight(400);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/dashboardScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        VBox vbox = new VBox();
        List<Button> buttons = List.of(
                new Button("Add Exhibition"),
                new Button("Remove Exhibition"),
                new Button("Add Artifact"),
                new Button("Remove Artifact"),
                new Button("Add Topic"),
                new Button("Remove Topic")
        );
//        buttons.get(0).setOnAction(value -> System.out.println("sout"));
        vbox.getChildren().addAll(buttons);

        Label helloWorldLabel = new Label("Hello world!");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}