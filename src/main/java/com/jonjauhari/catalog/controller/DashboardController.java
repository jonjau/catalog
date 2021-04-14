package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    private Database database;

    @FXML
    private Pane detailsPane;

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private ListView<Exhibition> exhibitionListView;

    @FXML
    public void initialize() {
        refreshArtifacts();
        refreshExhibitions();

        artifactListView.setOnMouseClicked(e -> {
            editArtifactClicked();
        });
        exhibitionListView.setOnMouseClicked(e -> {
            editExhibitionClicked();
        });
    }

    public DashboardController() {
        this.database = new Database();
    }

    @FXML
    private void refreshArtifacts() {
        List<Artifact> inStorage = database.getAllArtifacts().stream().filter(
                artifact -> artifact.getLocation() == null
        ).collect(Collectors.toList());

        var artifacts = FXCollections.observableArrayList(inStorage);
        artifactListView.setItems(artifacts);
    }

    @FXML
    private void refreshExhibitions() {
        var exhibitions = FXCollections.observableArrayList(database.getAllExhibitions());
        exhibitionListView.setItems(exhibitions);
    }

    private void changeDetailsPane(String FXMLFilePath, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource(FXMLFilePath);
            loader.setController(controller);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            detailsPane.getChildren().clear();
            detailsPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editArtifactClicked() {
        // null pointer error if clicked outside listitems
        var selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(database,
                selectedArtifact));
    }

    @FXML
    private void addArtifactClicked() {
        var newArtifact = new Artifact("", "", new Dimensions(10,10,10), 1);
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(database,
                newArtifact));
    }

    @FXML
    private void deleteArtifactClicked() {
        changeDetailsPane("/deleteArtifactScene.fxml", new DeleteArtifactController(database));
    }

    @FXML
    private void editExhibitionClicked() {
        var selectedExhibition = exhibitionListView.getSelectionModel().getSelectedItem();
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(database,
                selectedExhibition, artifactSelections));
    }

    @FXML
    private void addExhibitionClicked() {
        var newExhibition = new Exhibition("", "");
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(database,
                newExhibition, artifactSelections));
    }

    @FXML
    private void deleteExhibitionClicked() {
        changeDetailsPane("/deleteExhibitionScene.fxml", new DeleteExhibitionController(database));
    }
}
