package com.safexbank.safexbank.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Client {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final StringProperty client_id;
    private final StringProperty client_password;
    private final ObjectProperty<LocalDate> dateCreated;

    public Client(String fName, String lName, String pAddress, String client_Id, String client_password , LocalDate date) {
        this.firstName = new SimpleStringProperty(this, "FirstName", fName);
        this.lastName = new SimpleStringProperty(this, "LastName", lName);
        this.payeeAddress = new SimpleStringProperty(this, "Payee Address", pAddress);
        this.client_id = new SimpleStringProperty(this , "ID" , client_Id);
        this.client_password = new SimpleStringProperty(this, "Password" , client_password);
        this.dateCreated = new SimpleObjectProperty<>(this, "Date", date);
    }

    public StringProperty firstNameProperty() {return firstName;}
    public StringProperty lastNameProperty() {return lastName;}
    public StringProperty client_idProperty() {return client_id;}
    public StringProperty client_passwordProperty() {return client_password;}
    public StringProperty pAddressProperty() {return payeeAddress;}
    public ObjectProperty<LocalDate> dateProperty() {return dateCreated;}
}
