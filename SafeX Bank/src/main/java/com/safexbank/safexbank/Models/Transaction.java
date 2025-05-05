package com.safexbank.safexbank.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaction {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final StringProperty amount;
    private final ObjectProperty<LocalDate> date;

    public Transaction(String sender, String receiver, String amount, LocalDate date) {
        this.sender = new SimpleStringProperty(this, "sender", sender);
        this.receiver = new SimpleStringProperty(this, "Receiver", receiver);
        this.amount = new SimpleStringProperty(this, "Amount", amount);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
    }

    public String getSender() {
        return sender.get();
    }
    public String getReceiver() {
        return receiver.get();
    }
    public String getAmount() {
        return amount.get();
    }
    public String getDate() {
        return date.get().toString();
    }

}
