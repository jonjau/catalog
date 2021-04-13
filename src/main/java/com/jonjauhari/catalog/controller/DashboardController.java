package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class DashboardController {

    private Database database;

    @FXML
    private Button addArtifactButton;

    @FXML
    private Button deleteArtifactButton;

    @FXML
    private Button refreshArtifactsButton;

    @FXML
    private Pane detailsPane;

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private ListView<Exhibition> exhibitionListView;

    @FXML
    public void initialize() {
        refreshList();
        artifactListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Artifact item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " | " + item.getName() + " | " + item.getDescription());
                }
            }
        });
    }

    public DashboardController() {
        this.database = new Database();
    }

    @FXML
    private void refreshList() {
        var artifacts = FXCollections.observableArrayList(database.getAllArtifacts());
        System.out.println(Arrays.toString(artifacts.toArray()));
        artifactListView.setItems(artifacts);
    }

    @FXML
    private void addArtifactClicked() {

        var artifact = new Artifact("asdsad", "asd");
        database.insertArtifact(artifact);

        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/addArtifactScene.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            detailsPane.getChildren().clear();
            detailsPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteArtifactClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/deleteArtifactScene.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            detailsPane.getChildren().clear();
            detailsPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
