package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller responsible for the secondary view that allows users to delete exhibitions.
 */
public class DeleteExhibitionController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField idTextField;

    private ExhibitionRepository exhibitionRepo;

    /**
     * @param exhibitionRepo exhibition repo to delete exhibitions from
     */
    public DeleteExhibitionController(ExhibitionRepository exhibitionRepo) {
        this.exhibitionRepo = exhibitionRepo;
    }

    @FXML
    private void initialize() {
        mainButton.setOnAction(e -> deleteExhibitionClicked());
    }

    @FXML
    private void deleteExhibitionClicked() {

        long id = Long.parseLong(idTextField.getText());
        var toDelete = exhibitionRepo.findById(id);
        if (toDelete == null) {
            //  non-existent exhibition, do nothing
            return;
        }
        exhibitionRepo.delete(toDelete);
    }
}
