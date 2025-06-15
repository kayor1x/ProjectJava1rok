package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;
import services.AuthService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;
import models.Tournaments;
import javafx.collections.FXCollections;
import models.Organizer;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TournamentSelectionController implements Initializable {
    @FXML
    private Label choiceLabel;
    @FXML
    private ListView<String> listTournaments;
    @FXML
    private Label userInfoLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Button addTournamentButton;
    @FXML
    private Button removeTournamentButton;
    @FXML
    private Button editTournamentButton;

    @FXML
    private ComboBox<String> organizerComboBox;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private TextField locationField;


    String selectedTournament;

    private String formatTournamentDetails(Tournaments t) {
        return String.format("Name: %s\nOrganizer: %s\nStart Date: %s\nEnd Date: %s\nPrize Pool: %s\nType: %s\nLocation: %s",
                t.getName(),
                t.getOrganizer() != null ? t.getOrganizer().getName() : "N/A",
                t.getStartDate() != null ? t.getStartDate().toString() : "N/A",
                t.getEndDate() != null ? t.getEndDate().toString() : "N/A",
                t.getPrizePool() != null ? t.getPrizePool() : "N/A",
                t.getType() != null ? t.getType() : "N/A",
                t.getLocation() != null ? t.getLocation() : "N/A");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tournaments> query = session.createQuery("FROM models.Tournaments", Tournaments.class);
            List<Tournaments> tournaments = query.list();
            for (Tournaments t : tournaments) {
                String details = formatTournamentDetails(t);
                listTournaments.getItems().add(details);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load tournaments: " + e.getMessage());
            alert.showAndWait();
        }

        User user = AuthService.getCurrentUser();
        if (user != null) {
            String userInfo = user.getUsername();
            if ("ADMIN".equals(user.getRole().toString())) {
                userInfo += " (Admin)";
                addTournamentButton.setVisible(true);
                addTournamentButton.setManaged(true);
                removeTournamentButton.setVisible(true);
                removeTournamentButton.setManaged(true);
                editTournamentButton.setVisible(true);
                editTournamentButton.setManaged(true);
            } else {
                addTournamentButton.setVisible(false);
                addTournamentButton.setManaged(false);
                removeTournamentButton.setVisible(false);
                removeTournamentButton.setManaged(false);
                editTournamentButton.setVisible(false);
                editTournamentButton.setManaged(false);
            }
            userRoleLabel.setText(userInfo);
        }

        listTournaments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTournament = newValue.split("\n")[0].substring("Name: ".length());
                choiceLabel.setText(selectedTournament);
            } else {
                selectedTournament = null;
                choiceLabel.setText("No tournament selected");
            }
        });
    }

    @FXML
    private void handleStartButton(ActionEvent event) {
        try {
            if (selectedTournament != null && !selectedTournament.isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TournamentInfo.fxml"));
                Parent root = loader.load();
                TournamentInfoController controller = loader.getController();
                controller.setTournamentName(selectedTournament);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                choiceLabel.setText("Please select a tournament!");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            services.AuthService.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddTournament(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Tournament");
        dialog.setHeaderText("Enter tournament details:");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField prizePoolField = new TextField();
        prizePoolField.setPromptText("Prize Pool");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        ComboBox<String> organizerComboBox = new ComboBox<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Organizer> organizers = session.createQuery("FROM models.Organizer", Organizer.class).list();
            organizerComboBox.setItems(FXCollections.observableArrayList(
                    organizers.stream().map(Organizer::getName).toList()
            ));
        } catch (Exception e) {
            organizerComboBox.setPromptText("No organizers found");
        }
        organizerComboBox.setPromptText("Select Organizer");

        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>();
        typeChoiceBox.getItems().addAll("Online", "Offline");
        typeChoiceBox.setValue("Online");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        locationField.setVisible(false);
        locationField.setManaged(false);

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isOffline = "Offline".equals(newVal);
            locationField.setVisible(isOffline);
            locationField.setManaged(isOffline);
        });

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Organizer:"), 0, 1);
        grid.add(organizerComboBox, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("End Date:"), 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(new Label("Prize Pool:"), 0, 4);
        grid.add(prizePoolField, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeChoiceBox, 1, 5);
        grid.add(new Label("Location:"), 0, 6);
        grid.add(locationField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    Tournaments tournament = new Tournaments();
                    tournament.setName(nameField.getText());
                    String organizerName = organizerComboBox.getValue();
                    Organizer selectedOrganizer = null;
                    if (organizerName != null) {
                        selectedOrganizer = session.createQuery("FROM models.Organizer WHERE name = :name", Organizer.class)
                                .setParameter("name", organizerName)
                                .uniqueResult();
                    }
                    tournament.setOrganizer(selectedOrganizer);
                    tournament.setStartDate(startDatePicker.getValue());
                    tournament.setEndDate(endDatePicker.getValue());
                    tournament.setPrizePool(prizePoolField.getText());
                    tournament.setType(typeChoiceBox.getValue());
                    if ("Offline".equals(typeChoiceBox.getValue())) {
                        tournament.setLocation(locationField.getText());
                    } else {
                        tournament.setLocation(null);
                    }
                    session.persist(tournament);
                    session.getTransaction().commit();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add tournament: " + e.getMessage());
                    alert.showAndWait();
                }
                listTournaments.getItems().clear();
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Query<Tournaments> query = session.createQuery("FROM models.Tournaments", Tournaments.class);
                    List<Tournaments> tournaments = query.list();
                    for (Tournaments t : tournaments) {
                        listTournaments.getItems().add(formatTournamentDetails(t));
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load tournaments: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void handleRemoveTournament(ActionEvent event) {
        if (selectedTournament != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();
                Query<Tournaments> query = session.createQuery("FROM models.Tournaments WHERE name = :name", Tournaments.class);
                query.setParameter("name", selectedTournament);
                Tournaments tournament = query.uniqueResult();
                if (tournament != null) {
                    session.remove(tournament);
                    session.getTransaction().commit();

                    String selectedDetails = listTournaments.getSelectionModel().getSelectedItem();
                    listTournaments.getItems().remove(selectedDetails);
                    choiceLabel.setText("Tournament removed.");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not remove tournament: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleEditTournament(ActionEvent event) {
        if (selectedTournament == null || selectedTournament.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a tournament to edit.");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Tournament");
        dialog.setHeaderText("Edit tournament details:");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField prizePoolField = new TextField();
        prizePoolField.setPromptText("Prize Pool");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");
        InputStream iconStream = getClass().getResourceAsStream("/figures/dotaIcon.png");
        if (iconStream != null) {
            ImageView icon = new ImageView(new Image(iconStream));
            icon.setFitHeight(48);
            icon.setFitWidth(48);
            dialog.setGraphic(icon);
        }
        ComboBox<String> organizerComboBox = new ComboBox<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Organizer> organizers = session.createQuery("FROM models.Organizer", Organizer.class).list();
            organizerComboBox.setItems(FXCollections.observableArrayList(
                    organizers.stream().map(Organizer::getName).toList()
            ));
        } catch (Exception e) {
            organizerComboBox.setPromptText("No organizers found");
        }
        organizerComboBox.setPromptText("Select Organizer");
        grid.add(new Label("Organizer:"), 0, 1);
        grid.add(organizerComboBox, 1, 1);
        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>();
        typeChoiceBox.getItems().addAll("Online", "Offline");
        typeChoiceBox.setValue("Online");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        locationField.setVisible(false);
        locationField.setManaged(false);
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isOffline = "Offline".equals(newVal);
            locationField.setVisible(isOffline);
            locationField.setManaged(isOffline);
        });
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("End Date:"), 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(new Label("Prize Pool:"), 0, 4);
        grid.add(prizePoolField, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeChoiceBox, 1, 5);
        grid.add(new Label("Location:"), 0, 6);
        grid.add(locationField, 1, 6);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    Query<Tournaments> query = session.createQuery("FROM models.Tournaments WHERE name = :name", Tournaments.class);
                    query.setParameter("name", selectedTournament);
                    Tournaments tournament = query.uniqueResult();
                    if (tournament != null) {
                        tournament.setName(nameField.getText());
                        String organizerName = organizerComboBox.getValue();
                        Organizer selectedOrganizer = null;
                        if (organizerName != null) {
                            selectedOrganizer = session.createQuery("FROM models.Organizer WHERE name = :name", Organizer.class)
                                    .setParameter("name", organizerName)
                                    .uniqueResult();
                        }
                        tournament.setOrganizer(selectedOrganizer);
                        tournament.setStartDate(startDatePicker.getValue());
                        tournament.setEndDate(endDatePicker.getValue());
                        tournament.setPrizePool(prizePoolField.getText());
                        tournament.setType(typeChoiceBox.getValue());
                        if ("Offline".equals(typeChoiceBox.getValue())) {
                            tournament.setLocation(locationField.getText());
                        } else {
                            tournament.setLocation(null);
                        }
                        session.merge(tournament);
                        session.getTransaction().commit();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not edit tournament: " + e.getMessage());
                    alert.showAndWait();
                }
                listTournaments.getItems().clear();
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Query<models.Tournaments> query = session.createQuery("FROM models.Tournaments", models.Tournaments.class);
                    List<models.Tournaments> tournaments = query.list();
                    for (Tournaments t : tournaments) {
                        listTournaments.getItems().add(formatTournamentDetails(t));
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load tournaments: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });
        try {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Tournaments> query = session.createQuery("FROM models.Tournaments WHERE name = :name", Tournaments.class);
                query.setParameter("name", selectedTournament);
                Tournaments tournament = query.uniqueResult();
                if (tournament != null) {
                    nameField.setText(tournament.getName());
                    prizePoolField.setText(tournament.getPrizePool());
                    startDatePicker.setValue(tournament.getStartDate());
                    endDatePicker.setValue(tournament.getEndDate());
                    organizerComboBox.setValue(tournament.getOrganizer() != null ? tournament.getOrganizer().getName() : null);
                    typeChoiceBox.setValue(tournament.getType());
                    if ("Offline".equals(tournament.getType())) {
                        locationField.setText(tournament.getLocation());
                        locationField.setVisible(true);
                        locationField.setManaged(true);
                    } else {
                        locationField.setVisible(false);
                        locationField.setManaged(false);
                    }
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load tournament details: " + e.getMessage());
            alert.showAndWait();
        }
        dialog.showAndWait();
    }
}
