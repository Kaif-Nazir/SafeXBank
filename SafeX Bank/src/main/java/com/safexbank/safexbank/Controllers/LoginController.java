package com.safexbank.safexbank.Controllers;

import com.safexbank.safexbank.Models.Model;
import com.safexbank.safexbank.Views.AccountType;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public TextField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label payee_address_lbl;
    public TextField payee_address_field;
    public TextField passowrd_show_fld;
    public Button eye_show;

    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener((observable) -> setAcc_selector());
        passowrd_show_fld.setVisible(false);
        passowrd_show_fld.setManaged(false);
        passowrd_show_fld.textProperty().bindBidirectional(password_fld.textProperty());
        eye_show.setOnAction(event -> onclicked());
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(payee_address_field.getText(), password_fld.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();
                Model.getInstance().set_accountData(payee_address_field.getText());
                // Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                payee_address_field.setText("");
                password_fld.setText("");
                error_lbl.setText("No such Login Credentials");
                hidePassword();
            }
        } else {
            // Evaluate Admin Login Credentials
            Model.getInstance().evaluateAdminCred(payee_address_field.getText(), password_fld.getText());
            if (Model.getInstance().getAdminLoginSuccesFlag()) {
                Model.getInstance().getViewFactory().showAdminWindow();
                // Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                payee_address_field.setText("");
                password_fld.setText("");
                error_lbl.setText("No such Login Credentials");
                hidePassword();
            }
        }
    }

    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        // Change Payee Address Label accordingly
        if (acc_selector.getValue() == AccountType.ADMIN) {
            payee_address_lbl.setText("Username: ");
        } else {
            payee_address_lbl.setText("SafeX Address:");
        }

    }

    private void onclicked() {

        FontAwesomeIconView icon = (FontAwesomeIconView) eye_show.getGraphic();
        icon.setGlyphName(isPasswordVisible ? "EYE_SLASH" : "EYE");

        if (!isPasswordVisible) {
            // Show password
            passowrd_show_fld.setText(password_fld.getText());
            passowrd_show_fld.setVisible(true);
            passowrd_show_fld.setManaged(true);
            password_fld.setVisible(false);
            password_fld.setManaged(false);
        } else {
            // Hide password
            password_fld.setText(passowrd_show_fld.getText());
            passowrd_show_fld.setVisible(false);
            passowrd_show_fld.setManaged(false);
            password_fld.setVisible(true);
            password_fld.setManaged(true);
        }
        isPasswordVisible = !isPasswordVisible;
    }
    private void hidePassword(){
        isPasswordVisible = false;
        FontAwesomeIconView icon = (FontAwesomeIconView) eye_show.getGraphic();
        icon.setGlyphName("EYE_SLASH");
        passowrd_show_fld.setVisible(false);
        passowrd_show_fld.setManaged(false);
        password_fld.setVisible(true);
        password_fld.setManaged(true);
    }
}
