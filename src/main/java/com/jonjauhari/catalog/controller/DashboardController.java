package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

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

        exhibitionListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Exhibition item, boolean empty) {
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
    private void refreshArtifacts() {
        var artifacts = FXCollections.observableArrayList(database.getAllArtifacts());
        artifactListView.setItems(artifacts);
    }

    @FXML
    private void refreshExhibitions() {
        var exhibitions = FXCollections.observableArrayList(database.getAllExhibitions());
        System.out.println(exhibitions);
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
        var selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(database,
                selectedArtifact));
    }

    @FXML
    private void addArtifactClicked() {
        var newArtifact = new Artifact("", "");
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
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(database,
                selectedExhibition));
    }

    @FXML
    private void addExhibitionClicked() {
        var newExhibition = new Exhibition("", "");
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(database,
                newExhibition));
    }

    @FXML
    private void deleteExhibitionClicked() {
        changeDetailsPane("/deleteExhibitionScene.fxml", new DeleteExhibitionController(database));
    }
}
