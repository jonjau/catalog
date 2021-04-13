package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField idTextField;

    private Database database;

    public DeleteExhibitionController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> deleteExhibitionClicked());
    }

    @FXML
    private void deleteExhibitionClicked() {

        long id = Long.parseLong(idTextField.getText());
        var toDelete = database.getExhibition(id);
        database.deleteExhibition(toDelete);
    }
}
