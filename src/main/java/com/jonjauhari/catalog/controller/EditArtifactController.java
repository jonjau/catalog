package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditArtifactController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private Database database;
    private Artifact artifact;

    public EditArtifactController(Database database, Artifact artifact) {
        this.database = database;
        this.artifact = artifact;
    }

    @FXML
    public void initialize() {
        nameTextField.setText(artifact.getName());
        descriptionTextArea.setText(artifact.getDescription());
        mainButton.setOnAction(e -> saveArtifact());
    }

    @FXML
    private void saveArtifact() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        artifact.setName(name);
        artifact.setDescription(description);

        if (artifact.getId() != null) {
            database.updateArtifact(artifact);
        } else {
            database.insertArtifact(artifact);
        }
    }
}
