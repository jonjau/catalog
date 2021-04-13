package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<Artifact> addArtifactChoiceBox;

    private Database database;
    private Exhibition exhibition;
    private ObservableList<Artifact> artifactSelections;

    public EditExhibitionController(Database database, Exhibition exhibition,
                                    ObservableList<Artifact> artifactSelections) {
        this.database = database;
        this.exhibition = exhibition;
        this.artifactSelections = artifactSelections;
    }

    @FXML
    public void initialize() {
        nameTextField.setText(exhibition.getName());
        descriptionTextArea.setText(exhibition.getDescription());
        addArtifactChoiceBox.setItems(artifactSelections);

        mainButton.setOnAction(e -> saveExhibition());
        addArtifactChoiceBox.setOnAction(e -> addArtifactToExhibition());

        var artifacts = FXCollections.observableArrayList(exhibition.getArtifacts());
        artifactListView.setItems(artifacts);
    }

    private void refreshArtifactsList() {
        this.exhibition = database.getExhibition(exhibition.getId());
        var artifacts = FXCollections.observableArrayList(exhibition.getArtifacts());
        artifactListView.setItems(artifacts);
    }

    private void saveExhibition() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        exhibition.setName(name);
        exhibition.setDescription(description);

        database.saveExhibition(exhibition);
    }

    private void addArtifactToExhibition() {
        Artifact artifact = addArtifactChoiceBox.getValue();
        System.out.println(artifact);
        exhibition.addArtifact(artifact);

        database.saveExhibition(exhibition);
        refreshArtifactsList();
    }
}