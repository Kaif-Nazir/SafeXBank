package com.safexbank.safexbank.Models;

import javafx.beans.property.*;

public class AdminClient {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final StringProperty password;
    private final DoubleProperty checkingAcc;
    private final DoubleProperty savingsAcc;

    public AdminClient(int id, String firstName, String lastName, String payeeAddress, String password, double checkingAcc, double savingsAcc) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.payeeAddress = new SimpleStringProperty(payeeAddress);
        this.password = new SimpleStringProperty(password);
        this.checkingAcc = new SimpleDoubleProperty(checkingAcc);
        this.savingsAcc = new SimpleDoubleProperty(savingsAcc);
    }

    // Getters for all properties
    public IntegerProperty idProperty() { return id; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty payeeAddressProperty() { return payeeAddress; }
    public StringProperty passwordProperty() { return password; }
    public DoubleProperty checkingAccProperty() { return checkingAcc; }
    public DoubleProperty savingsAccProperty() { return savingsAcc; }
}

