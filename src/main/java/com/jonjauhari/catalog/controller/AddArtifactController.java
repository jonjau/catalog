package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddArtifactController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private Database database;

    public AddArtifactController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> addArtifactClicked());
    }

    @FXML
    private void addArtifactClicked() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();
        var artifact = new Artifact(name, description);
        database.insertArtifact(artifact);
    }
}
