package com.safexbank.safexbank.Controllers.Client;

import com.safexbank.safexbank.Models.Model;
import com.safexbank.safexbank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button account_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addListener();
    }

    private void addListener() {
        dashboard_btn.setOnAction(event -> onDashboard());
        profile_btn.setOnAction(event -> onProfile());
        account_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogout());
        report_btn.setOnAction(event -> soon());
    }

    private void onProfile() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    private void onDashboard() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    private void onAccounts() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        // Close the client window
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show login window
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set clent login success flag to false
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

    private void soon(){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Coming Soon");
            alert.setHeaderText("Feature Unavailable !!!");
            alert.setContentText("This feature will be implemented in a future update.");
            alert.showAndWait();
        }

}

