package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteExhibitionController {

    @FXML
    private TextField idTextField;

    @FXML
    private void deleteExhibitionClicked() {

        var db = new Database();
        long id = Long.parseLong(idTextField.getText());
        var toDelete = db.getExhibition(id);
        db.deleteExhibition(toDelete);
    }
}
