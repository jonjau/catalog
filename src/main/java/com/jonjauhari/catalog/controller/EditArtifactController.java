package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditArtifactController {

    @FXML
    private Button mainButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Spinner<Double> lengthSpinner;

    @FXML
    private Spinner<Double> widthSpinner;

    @FXML
    private Spinner<Double> heightSpinner;

    @FXML
    private Spinner<Double> weightSpinner;

    @FXML
    private TextField volumeTextField;

    private ArtifactRepository artifactRepo;
    private Artifact artifact;

    public EditArtifactController(ArtifactRepository artifactRepo, Artifact artifact) {
        this.artifactRepo = artifactRepo;
        this.artifact = artifact;
    }

    @FXML
    public void initialize() {
        nameTextField.setText(artifact.getName());
        descriptionTextArea.setText(artifact.getDescription());
        volumeTextField.setText(String.valueOf(artifact.getVolume()));

        mainButton.setOnAction(e -> saveArtifact());

        var dimensions = artifact.getDimensions();
        initializeSpinner(lengthSpinner, dimensions.length);
        initializeSpinner(widthSpinner, dimensions.width);
        initializeSpinner(heightSpinner, dimensions.height);
        initializeSpinner(weightSpinner, artifact.getWeight());
    }

    private void initializeSpinner(Spinner<Double> spinner, double startVal) {
        spinner.setEditable(true);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.1, 10000, startVal, 0.1));
        enableScrollControl(spinner);
    }

    private <T> void enableScrollControl(Spinner<T> spinner) {
        spinner.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                spinner.increment();
            } else if (event.getDeltaY() < 0) {
                spinner.decrement();
            }
        });
    }

    @FXML
    private void saveArtifact() {
        var name = nameTextField.getText();
        var description = descriptionTextArea.getText();
        var length = lengthSpinner.getValue();
        var width = widthSpinner.getValue();
        var height = heightSpinner.getValue();
        var weight = weightSpinner.getValue();

        artifact.setName(name);
        artifact.setDescription(description);
        artifact.setDimensions(new Dimensions(length, width, height));
        artifact.setWeight(weight);

        artifactRepo.save(artifact);

        volumeTextField.setText(String.valueOf(artifact.getVolume()));
    }
}
