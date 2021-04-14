package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller responsible for the secondary view that allows users to edit an artifact.
 */
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

    /**
     * @param artifactRepo artifact repo to get/set artifact data from and display
     * @param artifact the artifact currently being edited in this view
     */
    public EditArtifactController(ArtifactRepository artifactRepo, Artifact artifact) {
        this.artifactRepo = artifactRepo;
        this.artifact = artifact;
    }

    @FXML
    private void initialize() {
        // set display values on fields
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

    /**
     * @param spinner the spinner to initialise (setting properties)
     * @param startVal the value that the spinner starts at
     */
    private void initializeSpinner(Spinner<Double> spinner, double startVal) {
        spinner.setEditable(true);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.1, 10000, startVal, 0.1));
        enableScrollControl(spinner);
    }

    /**
     * Enable mouse scroll to increment or decrement a spinner's value
     */
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
        // extract values from view, then save the artifact on the repo
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

        // recalculate and update the volume field
        volumeTextField.setText(String.valueOf(artifact.getVolume()));
    }
}
