package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddArtifactController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button mainButton;

    @FXML
    private void addArtifactClicked() {

        var db = new Database();
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        var artifact = new Artifact(name, description);

        db.insertArtifact(artifact);
    }
}
