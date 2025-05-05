package com.safexbank.safexbank.Controllers.Client;

import com.safexbank.safexbank.Models.AdminClient;
import com.safexbank.safexbank.Models.Client;
import com.safexbank.safexbank.Models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class ProfileController implements Initializable {

    public Model model = Model.getInstance();

    public Button refresh_btn;
    public FontAwesomeIconView refresh_font_icon;
    public Label id_label;
    public Label fname_label;
    public Label lname_label;
    public Label payee_address_label;
    public Label password_label;

    public Label checking_acc_label;
    public Label savings_acc_label;
    public Label account_id;
    public Button delete_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        set_value();
        delete_btn.setOnAction(this::delete);
    }
    private void set_value(){

        id_label.textProperty().bind( model.getClient().client_idProperty());
        fname_label.textProperty().bind(model.getClient().firstNameProperty());
        lname_label.textProperty().bind(model.getClient().lastNameProperty());
        payee_address_label.textProperty().bind(model.getClient().pAddressProperty());
        password_label.textProperty().bind( model.getClient().client_passwordProperty());

        account_id.textProperty().bind(model.getAccount().accountNumberProperty().asString());
        checking_acc_label.textProperty().bind(model.getAccount().checkingBalanceProperty().asString());
        savings_acc_label.textProperty().bind(model.getAccount().savingsBalanceProperty().asString());

    }

    public void delete(ActionEvent event){
            showDeleteConfirmation();
    }
    private void showDeleteConfirmation() {
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
                deleteClient();
            } else {
                // Do nothing if the user clicked No (effectively cancels the delete)
                alert.close();
            }
        });
    }
    public void deleteClient(){

        String ID = Model.getInstance().getClient().client_idProperty().get();
        Model.getInstance().getDatabaseDriver().deleteClientById(Integer.parseInt(ID));

        Stage stage = (Stage) delete_btn.getScene().getWindow();
        // Close the client window
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show login window
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set clent login success flag to false
        Model.getInstance().setClientLoginSuccessFlag(false);

    }
}
    
