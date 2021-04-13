package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddExhibitionController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private void addExhibitionClicked() {

        var db = new Database();
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        var exhibition = new Exhibition(name, description);
        db.insertExhibition(exhibition);
    }
}
