package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;
import com.jonjauhari.catalog.repository.ExhibitionRepository;
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

    @FXML
    private ChoiceBox<Artifact> deleteArtifactChoiceBox;

    private ExhibitionRepository exhibitionRepo;
    private Exhibition exhibition;
    private ObservableList<Artifact> artifactSelections;

    public EditExhibitionController(ExhibitionRepository exhibitionRepo, Exhibition exhibition,
                                    ObservableList<Artifact> artifactSelections) {
        this.exhibitionRepo = exhibitionRepo;
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
        deleteArtifactChoiceBox.setOnAction(e -> deleteArtifactFromExhibition());

        refreshArtifactsList();

    }

    private void refreshArtifactsList() {
        if (exhibition.getId() != null) {
            this.exhibition = exhibitionRepo.findById(exhibition.getId());
        }
        var artifacts = FXCollections.observableArrayList(exhibition.getArtifacts());
        artifactListView.setItems(artifacts);
        deleteArtifactChoiceBox.setItems(artifacts);
    }

    private void saveExhibition() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        exhibition.setName(name);
        exhibition.setDescription(description);

        exhibitionRepo.save(exhibition);
    }

    private void addArtifactToExhibition() {
        Artifact artifact = addArtifactChoiceBox.getValue();
        exhibition.addArtifact(artifact);

        exhibitionRepo.save(exhibition);
        refreshArtifactsList();
    }

    private void deleteArtifactFromExhibition() {
        Artifact artifact = deleteArtifactChoiceBox.getValue();
        if (artifact == null) {
            return;
        }

        exhibition.deleteArtifact(artifact);
        exhibitionRepo.save(exhibition);
        refreshArtifactsList();
    }
}
