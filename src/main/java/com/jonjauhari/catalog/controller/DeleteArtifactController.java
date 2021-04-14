package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteArtifactController {

    @FXML
    private TextField idTextField;

    @FXML
    private Button mainButton;

    private ArtifactRepository artifactRepo;

    public DeleteArtifactController(ArtifactRepository artifactRepo) {
        this.artifactRepo = artifactRepo;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> deleteArtifactClicked());
    }

    @FXML
    private void deleteArtifactClicked() {

        // FIXME: non-existent artifact!
        long id = Long.parseLong(idTextField.getText());
        var toDelete = artifactRepo.findById(id);
        artifactRepo.delete(toDelete);
    }
}
