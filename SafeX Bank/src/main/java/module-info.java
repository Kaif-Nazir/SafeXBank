module com.mazebank.mazebank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.safexbank.safexbank to javafx.fxml;
    exports com.safexbank.safexbank;
    exports com.safexbank.safexbank.Controllers;
    exports com.safexbank.safexbank.Controllers.Admin;
    exports com.safexbank.safexbank.Controllers.Client;
    exports com.safexbank.safexbank.Models;
    exports com.safexbank.safexbank.Views;
}