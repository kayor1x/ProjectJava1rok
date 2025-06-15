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
import javafx.stage.Stage;


public class TournamentInfoController {
    @FXML
    private Label tournamentNameLabel;
    @FXML
    private Button backButton;

    @FXML
    private void backButtonHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TournamentSelection.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void setTournamentName(String name) {
        tournamentNameLabel.setText("Tournament: " + name);
    }
}

