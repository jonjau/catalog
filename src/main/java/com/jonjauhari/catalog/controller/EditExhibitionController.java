package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private Database database;
    private Exhibition exhibition;

    public EditExhibitionController(Database database, Exhibition exhibition) {
        this.database = database;
        this.exhibition = exhibition;
    }

    @FXML
    public void initialize() {
        nameTextField.setText(exhibition.getName());
        descriptionTextArea.setText(exhibition.getDescription());
        mainButton.setOnAction(e -> saveExhibition());
    }

    @FXML
    private void saveExhibition() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        exhibition.setName(name);
        exhibition.setDescription(description);

        if (exhibition.getId() != null) {
            database.updateExhibition(exhibition);
        } else {
            database.insertExhibition(exhibition);
        }
    }
}
