package com.safexbank.safexbank.Models;

import com.safexbank.safexbank.Views.AccountType;
import com.safexbank.safexbank.Views.ViewFactory;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Model {

    // Constructors
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    // Client Data Section
    private final Client client;
    private final Account account;
    private boolean clientLoginSuccessFlag;

    // Admin Data Section
    private boolean adminLoginSuccesFlag;

    private Model() {

        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();

        // Client Data section
        this.clientLoginSuccessFlag = false;
        this.client = new Client("","","", "","",null);
        this.account = new Account(0 ,0 ,0);

        // Admin Data section
        this.adminLoginSuccesFlag = false;
    }
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
     }

    /*
    * Client Method Section
    * */    
    public boolean getClientLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }
    public void setClientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }
    public Client getClient() {
        return client;
    }
    public Account getAccount() {
        return account;
    }
    public void evaluateClientCred(String safeXaddress, String password) {
        ResultSet resultSet = databaseDriver.getClientData(safeXaddress, password);
        try {
            if (resultSet.next()){
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.pAddressProperty().set(resultSet.getString("SafeXAddress"));
                this.client.client_passwordProperty().set(resultSet.getString("Password"));
                this.client.client_idProperty().set(resultSet.getString("ID"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateProperty().set(date);
                this.clientLoginSuccessFlag = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void set_accountData(String SafeXAddress){
        ResultSet resultSet = databaseDriver.getAccountData(SafeXAddress);
        try {
            if (resultSet.next()){
                this.account.accountNumberProperty().set(resultSet.getInt("account_id"));
                this.account.checkingBalanceProperty().set(resultSet.getInt("checking_acc"));
                this.account.savingsBalanceProperty().set(resultSet.getInt("savings_acc"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    * Admin Method Section
    */

    public boolean getAdminLoginSuccesFlag() {
        return this.adminLoginSuccesFlag;
    }
    public void setAdminLoginSuccesFlag(boolean adminLoginSuccesFlag) {
        this.adminLoginSuccesFlag = adminLoginSuccesFlag;
    }
    public void evaluateAdminCred(String username, String password) {
        ResultSet resultSet = getDatabaseDriver().getAdminData(username, password);
        try {
            if(resultSet.next()) {
                this.adminLoginSuccesFlag = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

