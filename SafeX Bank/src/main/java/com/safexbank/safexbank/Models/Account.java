package com.safexbank.safexbank.Models;

import javafx.beans.property.*;

public class Account {

    private final IntegerProperty accountNumber;
    private final DoubleProperty checkingBalance;
    private final DoubleProperty savingsBalance;

    public Account(int accountNumber, double checkingBalance, double savingsBalance) {
        this.accountNumber = new SimpleIntegerProperty(this, "AccountNumber", accountNumber);
        this.checkingBalance = new SimpleDoubleProperty(this, "CheckingBalance", checkingBalance);
        this.savingsBalance = new SimpleDoubleProperty(this, "SavingsBalance", savingsBalance);

    }

    public IntegerProperty accountNumberProperty() { return accountNumber; }
    public DoubleProperty checkingBalanceProperty() { return checkingBalance; }
    public DoubleProperty savingsBalanceProperty() { return savingsBalance; }
}
