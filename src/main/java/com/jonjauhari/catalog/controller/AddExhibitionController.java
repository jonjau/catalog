package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Exhibition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private Database database;

    public AddExhibitionController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> addExhibitionClicked());
    }

    @FXML
    private void addExhibitionClicked() {

        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();

        var exhibition = new Exhibition(name, description);
        database.insertExhibition(exhibition);
    }
}
