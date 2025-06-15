package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.AuthService;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField adminCodeField;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("USER", "ADMIN"));
        roleComboBox.setValue("USER");
        roleComboBox.setOnAction(e -> {
            boolean isAdmin = "ADMIN".equals(roleComboBox.getValue());
            adminCodeField.setVisible(isAdmin);
            adminCodeField.setManaged(isAdmin);
        });
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();
            if (username.isEmpty() || password.isEmpty() || role == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
                alert.showAndWait();
                return;
            }
            if ("ADMIN".equals(role)) {
                String adminCode = adminCodeField.getText();
                if (!"1111".equals(adminCode)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid admin code.");
                    alert.showAndWait();
                    return;
                }
            }
            boolean success = AuthService.register(username, password, role);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful! You can now log in.");
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed. Username may already exist.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            alert.showAndWait();
        }
    }
}