package com.jonjauhari.catalog.controller;

import com.jonjauhari.catalog.repository.ArtifactRepository;
import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import com.jonjauhari.catalog.model.Exhibition;
import com.jonjauhari.catalog.repository.ExhibitionRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for the main dashboard view which allows users to view, edit and add new
 * artifacts and exhibitions. It will load up secondary views on one of its panes as needed to
 * accommodate that functionality.
 */
public class DashboardController {

    @FXML
    private Pane detailsPane;

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private ListView<Exhibition> exhibitionListView;

    @FXML
    private Button addArtifactButton;

    @FXML
    private Button deleteArtifactButton;

    @FXML
    private Button addExhibitionButton;

    @FXML
    private Button deleteExhibitionButton;

    @FXML
    private Button refreshArtifactsButton;

    @FXML
    private Button refreshExhibitionsButton;

    private ArtifactRepository artifactRepo;
    private ExhibitionRepository exhibitionRepo;

    /**
     * @param artifactRepo artifact repo to get/set artifact data from and display
     * @param exhibitionRepo exhibition repo to get/set exhibition data from and display
     */
    public DashboardController(ArtifactRepository artifactRepo,
                               ExhibitionRepository exhibitionRepo) {
        this.artifactRepo = artifactRepo;
        this.exhibitionRepo = exhibitionRepo;
    }

    @FXML
    private void initialize() {
        // fetch data from repos to display on view
        refreshArtifacts();
        refreshExhibitions();

        // set up listeners
        artifactListView.setOnMouseClicked(e -> editArtifactClicked());
        exhibitionListView.setOnMouseClicked(e -> editExhibitionClicked());
        addArtifactButton.setOnAction(e -> addArtifactClicked());
        deleteArtifactButton.setOnAction(e -> deleteArtifactClicked());
        addExhibitionButton.setOnAction(e -> addExhibitionClicked());
        deleteExhibitionButton.setOnAction(e -> deleteExhibitionClicked());
        refreshArtifactsButton.setOnAction(e -> refreshArtifacts());
        refreshExhibitionsButton.setOnAction(e -> refreshExhibitions());
    }

    @FXML
    private void refreshArtifacts() {
        // artifacts to be displayed on the list are those not being displayed in any exhibitions
        List<Artifact> inStorage = artifactRepo.findAll().stream().filter(
                artifact -> artifact.getLocation() == null
        ).collect(Collectors.toList());

        var artifacts = FXCollections.observableArrayList(inStorage);
        artifactListView.setItems(artifacts);
    }

    @FXML
    private void refreshExhibitions() {
        // fetch all exhibition data from repo and display on view
        var exhibitions = FXCollections.observableArrayList(exhibitionRepo.findAll());
        exhibitionListView.setItems(exhibitions);
    }

    /**
     * Clear, then load up a view from an FXML file, assigning a controller to that view.
     * If loading the FXML file fails, this function is a no-op (it dumps the stack trace)
     * @param FXMLFilePath path to the view to load into the details pane
     * @param controller the controller to assign the new view
     */
    private void changeDetailsPane(String FXMLFilePath, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource(FXMLFilePath);
            loader.setController(controller);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            detailsPane.getChildren().clear();
            detailsPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editArtifactClicked() {
        var selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();
        if (selectedArtifact == null) {
            // user clicked outside list items: don't do anything
            return;
        }
        // load up view and controller to edit the selected artifact
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(artifactRepo,
                selectedArtifact));
    }

    @FXML
    private void addArtifactClicked() {
        // load up view and controller to edit this artifact, newly created with defaults
        var newArtifact = new Artifact("", "", new Dimensions(0.5, 0.5, 0.5), 1);
        changeDetailsPane("/editArtifactScene.fxml", new EditArtifactController(artifactRepo,
                newArtifact));
    }

    @FXML
    private void deleteArtifactClicked() {
        // load up view and controller to delete artifacts
        changeDetailsPane("/deleteArtifactScene.fxml", new DeleteArtifactController(artifactRepo));
    }

    @FXML
    private void editExhibitionClicked() {
        var selectedExhibition = exhibitionListView.getSelectionModel().getSelectedItem();
        if (selectedExhibition == null) {
            // clicked outside listitems: don't do anything
            return;
        }
        // load up view and controller to edit the selected exhibition
        // also inject the list of artifacts that can potentially be added to the exhibition
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(exhibitionRepo,
                selectedExhibition, artifactSelections));
    }

    @FXML
    private void addExhibitionClicked() {
        // load up view and controller to edit this exhibition, newly created with defaults
        var newExhibition = new Exhibition("", "");
        var artifactSelections = artifactListView.getItems();
        changeDetailsPane("/editExhibitionScene.fxml", new EditExhibitionController(exhibitionRepo,
                newExhibition, artifactSelections));
    }

    @FXML
    private void deleteExhibitionClicked() {
        // load up view and controller to delete exhibitions
        changeDetailsPane("/deleteExhibitionScene.fxml",
                new DeleteExhibitionController(exhibitionRepo));
    }
}
