package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {
    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private HBox welcomeBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private void handleStart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load the 2_TMS.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/figures/dotaWelcome.png")));
        icon.setFitHeight(64);
        icon.setFitWidth(64);
        icon.setPreserveRatio(true);
        if (welcomeBox != null && welcomeBox.getChildren().size() > 0) {
            welcomeBox.getChildren().add(0, icon);
        }
        if (welcomeLabel != null) {
            welcomeLabel.setWrapText(true);
            welcomeLabel.setMaxWidth(400);
        }
    }
}
