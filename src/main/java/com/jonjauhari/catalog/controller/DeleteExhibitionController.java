package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField idTextField;

    private ExhibitionRepository exhibitionRepo;

    public DeleteExhibitionController(ExhibitionRepository exhibitionRepo) {
        this.exhibitionRepo = exhibitionRepo;
    }

    @FXML
    public void initialize() {
        mainButton.setOnAction(e -> deleteExhibitionClicked());
    }

    @FXML
    private void deleteExhibitionClicked() {

        long id = Long.parseLong(idTextField.getText());
        var toDelete = exhibitionRepo.findById(id);
        exhibitionRepo.delete(toDelete);
    }
}
