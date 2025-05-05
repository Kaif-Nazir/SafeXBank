package com.safexbank.safexbank.Controllers.Admin;

import com.safexbank.safexbank.Models.AdminClient;
import com.safexbank.safexbank.Models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

    public TableView<AdminClient> clientsTable;

    public TableColumn<AdminClient, Number> idCol;
    public TableColumn<AdminClient, String> firstNameCol;
    public TableColumn<AdminClient, String> lastNameCol;
    public TableColumn<AdminClient, String> payeeAddressCol;
    public TableColumn<AdminClient, String> passwordCol;
    public TableColumn<AdminClient, Number> checkingAccCol;
    public TableColumn<AdminClient, Number> savingsAccCol;
    public Button refresh_btn;
    public FontAwesomeIconView refresh_font_icon;

    public TableColumn deleteAccCol;

    public TableColumn<AdminClient, Void> actionCol = new TableColumn<>("Action");
    private ObservableList<AdminClient> clientsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        firstNameCol.setCellValueFactory(data -> data.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(data -> data.getValue().lastNameProperty());
        payeeAddressCol.setCellValueFactory(data -> data.getValue().payeeAddressProperty());
        passwordCol.setCellValueFactory(data -> data.getValue().passwordProperty());
        checkingAccCol.setCellValueFactory(data -> data.getValue().checkingAccProperty());
        savingsAccCol.setCellValueFactory(data -> data.getValue().savingsAccProperty());


        deleteAccCol.setCellFactory(param -> new TableCell<AdminClient, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.getStyleClass().add("delete-btn");  // Add the CSS class here
                deleteButton.setOnAction(event -> {
                    AdminClient client = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(client);
//                    deleteClient(client);  // call your delete method
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        refresh_btn.setOnAction(this::refresh);
        clientsTable.setItems(Model.getInstance().getDatabaseDriver().getAllClients());
    }

    private void refresh(ActionEvent event){
        RotateTransition rotate = new RotateTransition(Duration.millis(500), refresh_font_icon); // assuming `refresh_icon` is your FontAwesome icon
        rotate.setByAngle(360);  // rotate 360 degrees
        rotate.setCycleCount(1);  // one full rotation
        rotate.setInterpolator(Interpolator.LINEAR); // smooth interpolation
        rotate.play();
        clientsTable.setItems(Model.getInstance().getDatabaseDriver().getAllClients());

    }
    private void deleteClient(AdminClient client) {
        boolean success = Model.getInstance().getDatabaseDriver().deleteClientById(client.idProperty().get());
        if (success) {
            clientsTable.setItems(Model.getInstance().getDatabaseDriver().getAllClients()); // Refresh after delete
        }
    }
    private void showDeleteConfirmation(AdminClient client) {
        // Create a confirmation dialog with Yes/No options
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this client?");
        alert.setContentText("This action cannot be undone.");

        // Customizing the alert to only show Yes and No buttons (removes the Cancel button)
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for user input
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Proceed with deletion if the user clicked Yes
                deleteClient(client);
            } else {
                // Do nothing if the user clicked No (effectively cancels the delete)
                alert.close();
            }
        });
    }
}