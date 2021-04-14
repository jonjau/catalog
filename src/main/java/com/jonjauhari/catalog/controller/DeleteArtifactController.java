package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller responsible for the secondary view that allows users to delete artifacts.
 */
public class DeleteArtifactController {

    @FXML
    private TextField idTextField;

    @FXML
    private Button mainButton;

    private ArtifactRepository artifactRepo;

    /**
     * @param artifactRepo artifact repo to delete artifacts from
     */
    public DeleteArtifactController(ArtifactRepository artifactRepo) {
        this.artifactRepo = artifactRepo;
    }

    @FXML
    private void initialize() {
        mainButton.setOnAction(e -> deleteArtifactClicked());
    }

    @FXML
    private void deleteArtifactClicked() {

        long id = Long.parseLong(idTextField.getText());
        var toDelete = artifactRepo.findById(id);
        if (toDelete == null) {
            //  non-existent artifact, do nothing
            return;
        }
        artifactRepo.delete(toDelete);
    }
}
