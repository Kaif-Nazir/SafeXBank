package com.safexbank.safexbank.Controllers.Client;

import com.safexbank.safexbank.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController extends ListCell<Transaction>{

    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label reciever_lbl;
    public Label amount_lbl;

    private FXMLLoader loader;


    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/FXML/Client/TransactionCell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Fill data
            sender_lbl.setText(transaction.getSender());
            reciever_lbl.setText(transaction.getReceiver());
            trans_date_lbl.setText(transaction.getDate());
            amount_lbl.setText("₹ " + transaction.getAmount());


            setGraphic(loader.getRoot());
        }
    }
}
