package com.safexbank.safexbank.Controllers.Admin;

import com.safexbank.safexbank.Models.Model;
import com.safexbank.safexbank.Models.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class DepositController implements Initializable {

    public TextField pAddress_fld;
    public TextField amount_fld;
    public Button deposit_btn;

    public Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deposit_btn.setOnAction(this::validateAndSend);
    }

    private void validateAndSend(ActionEvent event) {

        String payeeAddress = pAddress_fld.getText().trim();
        String amountText = amount_fld.getText().trim();

        if (!model.getDatabaseDriver().check_safex_address_exist(payeeAddress)){
            showTransactionFailedPopup("SafeX address does not exist.");
            return;
        }

        if (payeeAddress.isEmpty() || amountText.isEmpty()) {
            showTransactionFailedPopup("Please fill in all fields.");
            return;
        }

        if (Double.parseDouble(amountText) < 0) {
            showTransactionFailedPopup("Amount must be positive.");
            return;
        }

        boolean success = false;
        success = model.getDatabaseDriver().doTransactions(payeeAddress , Double.parseDouble(amountText));
        if(success){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transaction Successful");
            alert.setHeaderText("✅ Transaction Successful");
            alert.setContentText("The transaction is processed. The money has been Credited.");
            alert.showAndWait();
        }

    }
    private void showTransactionFailedPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transaction Failed");
        alert.setHeaderText("❌ Transaction Unsuccessful");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
