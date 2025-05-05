package com.safexbank.safexbank.Controllers.Client;

import com.safexbank.safexbank.Models.Model;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {

    public Model model = Model.getInstance();
    public Label ch_acc_num;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_cv_btn;

    public Label ch_acc_date;
    public Label sv_acc_date;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//            bal_set();
        ch_acc_num.textProperty().bind(model.getAccount().accountNumberProperty().asString());
        sv_acc_num.textProperty().bind(model.getAccount().accountNumberProperty().asString());
        ch_acc_bal.textProperty().bind(model.getAccount().checkingBalanceProperty().asString());
        sv_acc_bal.textProperty().bind(model.getAccount().savingsBalanceProperty().asString());

        ch_acc_date.setText(LocalDate.now().toString());
        sv_acc_date.setText(LocalDate.now().toString());

        trans_to_cv_btn.setOnAction(this::move_amount_to_checking_acc);
        trans_to_sv_btn.setOnAction(this::move_amount_to_savings_acc);
    }

    /*private void bal_set(){
        String id = model.getClient().client_idProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientAccountData(id);
        try{
            while(resultSet.next()){
                String chk_bal = resultSet.getString("checking_acc");
                String sav_bal = resultSet.getString("savings_acc");
                String acc_num = resultSet.getString("account_id");
                ch_acc_bal.setText(chk_bal);
                sv_acc_bal.setText(sav_bal);
                ch_acc_num.setText(acc_num);
                sv_acc_num.setText(acc_num);


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
    private void move_amount_to_savings_acc(ActionEvent event) {

        String accountID = ch_acc_num.getText();
        int acc_ID = Integer.parseInt(accountID);

        String amount = amount_to_sv.getText();
        double entered_amount = Double.parseDouble(amount);
        double amount_in_account = Double.parseDouble(ch_acc_bal.getText());

        if(entered_amount < 0){
            showTransactionFailedPopup("Amount Must Be Positive");
            return;
        }
        if(entered_amount > amount_in_account ){
            showTransactionFailedPopup("Insufficient balance.");
            return;
        }
        model.getDatabaseDriver().move_amount_to_savings(acc_ID , entered_amount);
//        bal_set();
        refreshAccountData();
    }
    private void move_amount_to_checking_acc(ActionEvent event){
        String accountID = sv_acc_num.getText();
        int acc_ID = Integer.parseInt(accountID);

        String amount = amount_to_ch.getText();
        double entered_amount = Double.parseDouble(amount);
        double amount_in_account = Double.parseDouble(sv_acc_bal.getText());

        if(entered_amount < 0){
            showTransactionFailedPopup("Amount Must Be Positive");
            return;
        }
        if(entered_amount > amount_in_account){
            showTransactionFailedPopup("Insufficient balance.");
            return;
        }
        model.getDatabaseDriver().move_amount_to_checking(acc_ID , entered_amount);
//        bal_set();
        refreshAccountData();
    }
    private void showTransactionFailedPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transaction Failed");
        alert.setHeaderText("❌ Transaction Unsuccessful");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void refreshAccountData() {
        String payeeAddress = model.getClient().pAddressProperty().get();
        ResultSet resultSet = model.getDatabaseDriver().getAccountData(payeeAddress);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transaction Success");
        alert.setHeaderText("✅ Transaction Successful");
        alert.setContentText("Successful");
        alert.showAndWait();

        try {
            if (resultSet.next()) {
                double checkingBalance = resultSet.getDouble("checking_acc");
                double savingsBalance = resultSet.getDouble("savings_acc");
                int accountId = resultSet.getInt("account_id");

                model.getAccount().checkingBalanceProperty().set(checkingBalance);
                model.getAccount().savingsBalanceProperty().set(savingsBalance);
                model.getAccount().accountNumberProperty().set(accountId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
