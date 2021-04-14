package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;
import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller responsible for the secondary view that allows users to edit an exhibition.
 */
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

    /**
     * @param exhibitionRepo exhibition repo to fetch and update exhibition data
     * @param exhibition the exhibition currently being edited in this view
     * @param artifactSelections a list of artifacts that may be added to this exhibition
     */
    public EditExhibitionController(ExhibitionRepository exhibitionRepo, Exhibition exhibition,
                                    ObservableList<Artifact> artifactSelections) {
        this.exhibitionRepo = exhibitionRepo;
        this.exhibition = exhibition;
        this.artifactSelections = artifactSelections;
    }

    @FXML
    private void initialize() {
        // set display values on fields
        nameTextField.setText(exhibition.getName());
        descriptionTextArea.setText(exhibition.getDescription());
        addArtifactChoiceBox.setItems(artifactSelections);

        // set up listeners
        mainButton.setOnAction(e -> saveExhibition());
        addArtifactChoiceBox.setOnAction(e -> addArtifactToExhibition());
        deleteArtifactChoiceBox.setOnAction(e -> deleteArtifactFromExhibition());

        refreshArtifactsList();
    }

    private void refreshArtifactsList() {
        // if this exhibition is yet to be inserted to the database, there is nothing to reset
        if (exhibition.getId() != null) {
            this.exhibition = exhibitionRepo.findById(exhibition.getId());
        }
        // artifacts to be displayed on the list are those being displayed in this exhibition
        var artifacts = FXCollections.observableArrayList(exhibition.getArtifacts());
        artifactListView.setItems(artifacts);
        deleteArtifactChoiceBox.setItems(artifacts);
    }

    private void saveExhibition() {
        // extract values from view, then save the exhibition on the repo
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        exhibition.setName(name);
        exhibition.setDescription(description);

        exhibitionRepo.save(exhibition);
    }

    private void addArtifactToExhibition() {
        // extract values from view, then save the exhibition, with added artifacts, on the repo
        Artifact artifact = addArtifactChoiceBox.getValue();
        exhibition.addArtifact(artifact);

        exhibitionRepo.save(exhibition);
        refreshArtifactsList();
    }

    private void deleteArtifactFromExhibition() {
        Artifact artifact = deleteArtifactChoiceBox.getValue();
        if (artifact == null) {
            // this implies a non-user triggered action: do nothing
            return;
        }
        // delete the artifact from this exhibition then save that updated exhibition
        exhibition.deleteArtifact(artifact);
        exhibitionRepo.save(exhibition);
        refreshArtifactsList();
    }
}
