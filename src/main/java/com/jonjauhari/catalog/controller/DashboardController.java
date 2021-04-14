package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import com.jonjauhari.catalog.model.Exhibition;
import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Pane detailsPane;

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private ListView<Exhibition> exhibitionListView;

    @FXML
    private Button addArtifactButton;

    @FXML
    private Button deleteArtifactButton;

    @FXML
    private Button addExhibitionButton;

    @FXML
    private Button deleteExhibitionButton;

    @FXML
    private Button refreshArtifactsButton;

    @FXML
    private Button refreshExhibitionsButton;

    @FXML
    public void initialize() {
        refreshArtifacts();
        refreshExhibitions();

        artifactListView.setOnMouseClicked(e -> editArtifactClicked());
        exhibitionListView.setOnMouseClicked(e -> editExhibitionClicked());
        addArtifactButton.setOnAction(e -> addArtifactClicked());
        deleteArtifactButton.setOnAction(e -> deleteArtifactClicked());
        addExhibitionButton.setOnAction(e -> addExhibitionClicked());
        deleteExhibitionButton.setOnAction(e -> deleteExhibitionClicked());
        refreshArtifactsButton.setOnAction(e -> refreshArtifacts());
        refreshExhibitionsButton.setOnAction(e -> refreshExhibitions());
    }

    private ArtifactRepository artifactRepo;
    private ExhibitionRepository exhibitionRepo;

    public DashboardController(ArtifactRepository artifactRepo,
                               ExhibitionRepository exhibitionRepo) {
        this.artifactRepo = artifactRepo;
        this.exhibitionRepo = exhibitionRepo;
    }

    @FXML
    private void refreshArtifacts() {
        List<Artifact> inStorage = artifactRepo.findAll().stream().filter(
                artifact -> artifact.getLocation() == null
        ).collect(Collectors.toList());

        var artifacts = FXCollections.observableArrayList(inStorage);
        artifactListView.setItems(artifacts);
    }

    @FXML
    private void refreshExhibitions() {
        var exhibitions = FXCollections.observableArrayList(exhibitionRepo.findAll());
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
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(artifactRepo,
                selectedArtifact));
    }

    @FXML
    private void addArtifactClicked() {
        var newArtifact = new Artifact("", "", new Dimensions(10, 10, 10), 1);
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(artifactRepo,
                newArtifact));
    }

    @FXML
    private void deleteArtifactClicked() {
        changeDetailsPane("/deleteArtifactScene.fxml", new DeleteArtifactController(artifactRepo));
    }

    @FXML
    private void editExhibitionClicked() {
        var selectedExhibition = exhibitionListView.getSelectionModel().getSelectedItem();
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(exhibitionRepo,
                selectedExhibition, artifactSelections));
    }

    @FXML
    private void addExhibitionClicked() {
        var newExhibition = new Exhibition("", "");
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(exhibitionRepo,
                newExhibition, artifactSelections));
    }

    @FXML
    private void deleteExhibitionClicked() {
        changeDetailsPane("/deleteExhibitionScene.fxml",
                new DeleteExhibitionController(exhibitionRepo));
    }
}
