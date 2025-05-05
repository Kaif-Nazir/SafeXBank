package com.safexbank.safexbank.Controllers.Admin;

import com.safexbank.safexbank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {

    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public TextField ch_amount_fld;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Label error_lbl;

    public Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pAddress_box.setOnAction(event -> {
            if (pAddress_box.isSelected()) {
                String generatedAddress = generateRandomPayeeAddress();
                pAddress_lbl.setText(generatedAddress);
            } else {
                pAddress_lbl.setText("");
            }
        });

        create_client_btn.setOnAction(event -> createClient());

    }
    private String generateRandomPayeeAddress() {

        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder address;
        do {
            address = new StringBuilder(); // prefix "PA-"
            for (int i = 0; i < 4; i++) {
                int randomIndex = (int) (Math.random() * chars.length());
                address.append(chars.charAt(randomIndex));
            }
            address.append("@safex");
        }while(model.getDatabaseDriver().check_safex_address_exist(address.toString()));
        return address.toString();

    }
    private void createClient() {
        String fName = fName_fld.getText().trim();
        String lName = lName_fld.getText().trim();
        String password = password_fld.getText().trim();
        String payeeAddress = pAddress_lbl.getText().trim();
        boolean hasPayeeAddress = pAddress_box.isSelected();

        // Validate inputs
        if (fName.isEmpty() || lName.isEmpty() || password.isEmpty()) {
            error_lbl.setText("All fields must be filled!");
            return;
        }
        if (!hasPayeeAddress || payeeAddress.isEmpty()) {
            error_lbl.setText("Generate SafeX Address first!");
            return;
        }

        double checkingAmount = 0;
        double savingAmount = 0;


            try {
                checkingAmount = Double.parseDouble(ch_amount_fld.getText().trim());
                if (checkingAmount < 0) {
                    error_lbl.setText("Checking account amount must be positive!");
                    return;
                }
                savingAmount = Double.parseDouble(sv_amount_fld.getText().trim());
                if (savingAmount < 0) {
                    error_lbl.setText("Saving account amount must be positive!");
                    return;
                }
            } catch (NumberFormatException e) {
                error_lbl.setText("Invalid checking amount!");
                return;
            }

        // Insert into database
        boolean success =  model.getDatabaseDriver().insertClientIntoDatabase
        (fName, lName, password, payeeAddress , checkingAmount, savingAmount);

        if(success){
            showSuccessBox("Successfull");
        }

    }
    private void showSuccessBox(String successfull) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(successfull);
        alert.setHeaderText("Client Successfully Created ");
        alert.setContentText("✅ Client Successfully Added");
        alert.showAndWait();


    }
    private void clearFields() {
        fName_fld.clear();
        lName_fld.clear();
        password_fld.clear();
        pAddress_lbl.setText("");
        ch_amount_fld.clear();
        sv_amount_fld.clear();
    }
}
