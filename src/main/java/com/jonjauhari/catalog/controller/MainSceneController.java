package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.Database;
import com.jonjauhari.catalog.model.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainSceneController {

    private Database database;

    @FXML
    private Button mainButton;

    public MainSceneController() {
        this.database = new Database();
    }

    @FXML
    private void buttonClicked() {
        System.out.println("Button clicked!");
        Artifact artifact = new Artifact("Old Vase", "Tutankhamun's Chamber pot");
        database.insertArtifact(artifact);
    }
}
