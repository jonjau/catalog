package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteArtifactController {

    @FXML
    private TextField idTextField;

    @FXML
    private Button mainButton;

    private Database database;
    public DeleteArtifactController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> deleteArtifactClicked());
    }

    @FXML
    private void deleteArtifactClicked() {

        // FIXME: non-existent artifact!
        long id = Long.parseLong(idTextField.getText());
        var toDelete = database.getArtifact(id);
        database.deleteArtifact(toDelete);
    }
}
