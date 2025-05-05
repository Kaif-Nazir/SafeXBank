package com.safexbank.safexbank.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;
    private java.sql.ResultSet ResultSet;


        public DatabaseDriver() {
            try {
                // Connect to MySQL
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/safex", "root", "admin123");
                System.out.println("SQL Connection Successful");
                System.out.println("Welcome To Safex Bank");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Client Section
         */
        public ResultSet getClientData(String pAddress, String password) {
            ResultSet resultSet = null;
            try {
                String query = "SELECT * FROM Clients WHERE SafeXAddress=? AND Password=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, pAddress);
                statement.setString(2, password);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
        }
        public ResultSet getAccountData(String SafeXAddress) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT checking_acc, savings_acc , account_id FROM accounts WHERE ID =" +
                    " (Select ID from clients where SafeXAddress = ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, SafeXAddress);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  resultSet;
        }
        public boolean check_safex_address_exist(String safex){
        ResultSet resultSet = null;
        try {
            String query1 = "SELECT * FROM clients WHERE SafeXAddress = ?";
            PreparedStatement ps1 = conn.prepareStatement(query1);  // ✅ Fix is here
            ps1.setString(1, safex);
            resultSet = ps1.executeQuery();

            return resultSet.next();  // ✅ One-liner
        } catch (Exception e) {
            return false;
        }
    }
        public boolean doTransactions(String safex , double amount , int ID , boolean forceFail) throws SQLException {

            // 2. Check if "Force Fail" checkbox is selected
            conn.setAutoCommit(false);
            if (forceFail) {
                conn.rollback();
                return false;
            }

            ResultSet resultSet = null;
            try {

                // 1. Debit sender's account
                String debitQuery = "UPDATE accounts SET checking_acc = checking_acc - ? WHERE account_id = ?";
                PreparedStatement debit = conn.prepareStatement(debitQuery);
                debit.setDouble(1, amount);
                debit.setInt(2, ID);
                debit.executeUpdate();

                // 2. Credit receiver's account (by SafeXAddress → join to get account_id)
                String creditQuery =
                                "UPDATE accounts " +
                                "SET checking_acc = checking_acc + ? " +
                                "WHERE ID = ( " +
                                "SELECT ID FROM clients WHERE SafeXAddress = ?)";
                PreparedStatement credit = conn.prepareStatement(creditQuery);
                credit.setDouble(1, amount);
                credit.setString(2, safex);
                credit.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
                return true;
            } catch (Exception e) {
                conn.rollback();
                conn.setAutoCommit(true);
                e.printStackTrace();
                return false;
            }
        }
        public void move_amount_to_savings(int Id , double amount ){

            try{
                String move_amount = "Update accounts set checking_acc = checking_acc - ? ," +
                                     "savings_acc = savings_acc + ? where account_id = ? ";
                PreparedStatement move_amount_chk = conn.prepareStatement(move_amount);
                move_amount_chk.setDouble(1 , amount);
                move_amount_chk.setDouble(2 , amount);
                move_amount_chk.setInt(3 , Id);
                move_amount_chk.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        public void move_amount_to_checking(int Id , double amount ){

            try{
                String move_amount = "Update accounts set savings_acc = savings_acc - ? ," +
                                    "checking_acc = checking_acc + ? where account_id = ? ";
                PreparedStatement move_amount_chk = conn.prepareStatement(move_amount);
                move_amount_chk.setDouble(1 , amount);
                move_amount_chk.setDouble(2 , amount);
                move_amount_chk.setInt(3 , Id);
                move_amount_chk.executeUpdate();
                } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        public String getReceiverName(String trim) {
        String query = "SELECT FirstName FROM clients WHERE SafeXAddress = '" + trim + "'";
        ResultSet rs1 = null;
        try {
            Statement statement = conn.createStatement();
            rs1 = statement.executeQuery(query);

            if (rs1.next()) {
                return rs1.getString("FirstName");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
        public boolean doTransactions(String SafeXAddress , double amount ){

            String query = " update accounts set checking_acc = checking_acc+ ?  where " +
                            "ID = (select ID from clients where SafeXAddress = ?)";

            try{
                PreparedStatement ps1 = conn.prepareStatement(query);
                ps1.setDouble(1, amount);
                ps1.setString(2, SafeXAddress);
                ps1.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }


        /*
         * Admin Section
         */

        public ResultSet getAdminData(String username, String password) {
            ResultSet resultSet = null;
            try {
                String query = "SELECT * FROM admin WHERE Username=? AND Password=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
        }
        public boolean insertClientIntoDatabase(String fName, String lName, String password, String SafeXAddress,
                                                 double checkingAmount, double savingAmount) {
        String query = "INSERT INTO clients (FirstName, LastName, SafeXAddress , Password , Date ) VALUES (?, ?, ?, ? ,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, SafeXAddress);
            pstmt.setString(4, password);
            pstmt.setString(5, LocalDate.now().toString());


            pstmt.executeUpdate();

            int clientId = -1;
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                clientId = rs.getInt(1);
            }
            if (clientId == -1) {
                throw new SQLException("Failed to retrieve client ID.");
            }

            int accountId = clientId + 1000;
            String accountQuery = "INSERT INTO accounts (account_id, ID, checking_acc, savings_acc) VALUES (?, ?, ?, ?)";
            PreparedStatement accountStmt = conn.prepareStatement(accountQuery);
            accountStmt.setInt(1, accountId);
            accountStmt.setInt(2, clientId);
            accountStmt.setDouble(3, checkingAmount);
            accountStmt.setDouble(4, savingAmount);
            accountStmt.executeUpdate();

            return true; // Everything successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
        public ObservableList<AdminClient> getAllClients() {
        ObservableList<AdminClient> clientsList = FXCollections.observableArrayList();
        String sql = "SELECT clients.ID, clients.FirstName, clients.LastName, clients.SafeXAddress, clients.Password, accounts.checking_acc, accounts.savings_acc " +
                     "FROM clients INNER JOIN accounts ON clients.ID = accounts.ID";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                clientsList.add(new AdminClient(
                        rs.getInt("ID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("SafeXAddress"),
                        rs.getString("Password"),
                        rs.getDouble("checking_acc"),
                        rs.getDouble("savings_acc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsList;
    }

        public boolean deleteClientById(int id) {

//            String query1 = "Delete From accounts where ID = ?";
            String query2 = "Delete From clients where ID = ?";

            try{
//                PreparedStatement ps1 = conn.prepareStatement(query1);
                PreparedStatement ps2 = conn.prepareStatement(query2);

//                ps1.setInt(1, id);
                ps2.setInt(1, id);

//                ps1.executeUpdate();
                ps2.executeUpdate();
                return true;
        } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
}
