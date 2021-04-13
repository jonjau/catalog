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

    @FXML
    private void deleteArtifactClicked() {

        // FIXME: non-existent artifact!
        var db = new Database();
        long id = Long.parseLong(idTextField.getText());
        var toDelete = db.getArtifact(id);
        db.deleteArtifact(toDelete);
    }
}
