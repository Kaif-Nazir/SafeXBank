package com.safexbank.safexbank;

import com.safexbank.safexbank.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        System.out.println("Running Application !!!");
        launch(args);
    }
}
