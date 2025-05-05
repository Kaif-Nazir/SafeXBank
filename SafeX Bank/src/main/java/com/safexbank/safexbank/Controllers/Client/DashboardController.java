package com.safexbank.safexbank.Controllers.Client;

import com.safexbank.safexbank.Models.Model;
import com.safexbank.safexbank.Models.Transaction;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    public Model model = Model.getInstance();
    public Label f_name_lbl;
    public Label savings_acc_num;
    public Label savings_bal;
    public Label checking_acc_num;
    public Label checking_bal;
    public TextField payee_fld;
    public TextField amount_fld;
    public Button sent_money_btn;
    public Button refresh_btn;
    public CheckBox chk_box_rollback;
    public FontAwesomeIconView refresh_icon;

    public ListView transaction_listview;
    public ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    public Label current_date;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        f_name_lbl.textProperty().bind(Bindings.concat("Hi, ", model.getClient().firstNameProperty(), " ", model.getClient().lastNameProperty()));
        checking_acc_num.textProperty().bind(model.getAccount().accountNumberProperty().asString());
        savings_acc_num.textProperty().bind(model.getAccount().accountNumberProperty().asString());
        checking_bal.textProperty().bind(model.getAccount().checkingBalanceProperty().asString());
        savings_bal.textProperty().bind(model.getAccount().savingsBalanceProperty().asString());
        current_date.setText(LocalDate.now().toString());
        //        bal_set();

        sent_money_btn.setOnAction(this::validateAndSend);
        refresh_btn.setOnAction(this::refresh);

        transaction_listview.setItems(transactionList);
        transaction_listview.setCellFactory(listView -> new TransactionCellController());
    }


    private void refresh(ActionEvent event) {
        RotateTransition rotate = new RotateTransition(Duration.millis(500), refresh_icon); // assuming `refresh_icon` is your FontAwesome icon
        rotate.setByAngle(360);  // rotate 360 degrees
        rotate.setCycleCount(1);  // one full rotation
        rotate.setInterpolator(Interpolator.LINEAR); // smooth interpolation
        rotate.play();
//        bal_set();
        String payeeAddress = model.getClient().pAddressProperty().get(); // get current payeeAddress
        ResultSet resultSet = model.getDatabaseDriver().getAccountData(payeeAddress);

        try {
            if (resultSet.next()) {
                double checkingBalance = resultSet.getDouble("checking_acc");
                double savingsBalance = resultSet.getDouble("savings_acc");

                // UPDATE PROPERTIES (this will auto-refresh UI because of bindings)
                model.getAccount().checkingBalanceProperty().set(checkingBalance);
                model.getAccount().savingsBalanceProperty().set(savingsBalance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void validateAndSend(ActionEvent event) {
        String payeeAddress = payee_fld.getText().trim();
        String amountText = amount_fld.getText().trim();
        String senderAccountId = checking_acc_num.getText().trim();
        int ID = Integer.parseInt(senderAccountId);

        if (payeeAddress.isEmpty() || amountText.isEmpty()) {
            showTransactionFailedPopup("Please fill in all fields.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showTransactionFailedPopup("Invalid amount entered.");
            return;
        }

        if (amount <= 0) {
            showTransactionFailedPopup("Amount must be positive.");
            return;
        }

        double currentBalance = Double.parseDouble(checking_bal.getText());
        if (amount > currentBalance) {
            showTransactionFailedPopup("Insufficient balance.");
            return;
        }

        if (!model.getDatabaseDriver().check_safex_address_exist(payeeAddress)) {
            showTransactionFailedPopup("SafeX address does not exist.");
            return;
        }

        boolean success = false;
        try {
            success = model.getDatabaseDriver().doTransactions(payeeAddress, amount, ID, chk_box_rollback.isSelected());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!chk_box_rollback.isSelected()) {
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transaction Successful");
                alert.setHeaderText("✅ Transaction Successful");
                alert.setContentText("The transaction is processed. The money has been debited.");
                alert.showAndWait();
                String receiverName = model.getDatabaseDriver().getReceiverName(payee_fld.getText().trim());
                transactionList.add(new Transaction(model.getClient().firstNameProperty().get(), receiverName, amount_fld.getText(), LocalDate.now()));
//                bal_set(); // refresh balances
                refresh_btn.fire();
            } else {
                showTransactionFailedPopup("Transaction failed due to a database error.");
            }
        } else {
            showProcessingPopup("Rollback is in Progress!" , 0.6D);
//            showTransactionFailedPopup("Transaction failed Money is Not Debited");
        }
    }
    private void showTransactionFailedPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transaction Failed");
        alert.setHeaderText("❌ Transaction Unsuccessful");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showTransactionRollbackedPopup() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rollback");
        alert.setHeaderText("Transaction Unsuccessful");
        alert.setContentText("Transaction failed Money is Not Debited");
        alert.show();
    }
    public void showProcessingPopup(String message, double seconds) {
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setTitle("Processing");

        // Create a ProgressIndicator (the spinning circle)
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(60, 60);

        // Create a Label for the message
        Label processingLabel = new Label(message);

        // Layout
        VBox root = new VBox(20, progressIndicator, processingLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, 200);
        loadingStage.setScene(scene);
        loadingStage.setResizable(false);
        loadingStage.show();

        // Timer to close the popup after given seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(event -> {
            loadingStage.close();

            // 👇 After slight delay, call whatever function you want
            PauseTransition miniDelay = new PauseTransition(Duration.millis(200)); // 200ms gap
            miniDelay.setOnFinished(e -> {
                showTransactionRollbackedPopup(); // 🔥 Your next method automatically called
            });
            miniDelay.play();
        });
        delay.play();
    }
    /* private void bal_set(){
             String id = model.getClient().client_idProperty().get();
             ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientAccountData(id);
             try{
             while(resultSet.next()){
                 String chk_bal = resultSet.getString("checking_acc");
                 String sav_bal = resultSet.getString("savings_acc");
                 String acc_num = resultSet.getString("account_id");
                 checking_bal.setText(chk_bal);
                 savings_bal.setText(sav_bal);
                 checking_acc_num.setText(acc_num);
                 savings_acc_num.setText(acc_num);

             }
             }catch(Exception e){
                 e.printStackTrace();
             }
     }*/
}