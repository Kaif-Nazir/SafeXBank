package com.safexbank.safexbank.Views;

import com.safexbank.safexbank.Controllers.Admin.AdminController;
import com.safexbank.safexbank.Controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    private AccountType loginAccountType;


    // Client views
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane profileView;
    private AnchorPane accountsView;

    // Admin Views
    private AnchorPane createClientView;
    private AnchorPane clientsView;
    private AnchorPane depositView;
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;

    public ViewFactory() {
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    // Login Select

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }
    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    // Client Views Section

    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {return clientSelectedMenuItem;}
    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }
    public AnchorPane getProfileView() {
        if (profileView == null) {
            try {
                profileView = new FXMLLoader(getClass().getResource("/FXML/Client/Profile.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profileView;
    }
    public AnchorPane getAccountsView() {
        if (accountsView == null) {
            try{
                accountsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Accounts.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accountsView;
    }
    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("SafeX Bank");
        stage.show();
    }
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);

    }


    // Admin Views Section

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }
    public AnchorPane getCreateClientView() {
        if (createClientView == null) {
            try {
                createClientView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CreateClient.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createClientView;
    }
    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController controller = new AdminController();
        loader.setController(controller);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("SafeX Bank");
        stage.show();
    }
    public AnchorPane getClientsView() {
        if (clientsView == null) {
            try {
                clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientsView;
    }
    public AnchorPane getDepositView() {
        if (depositView == null) {
            try {
                depositView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposit.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return depositView;
    }



    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(true);
        stage.setTitle("SafeX Bank");
        stage.show();
    }
    public void closeStage(Stage stage) {
        stage.close();
    }

}
