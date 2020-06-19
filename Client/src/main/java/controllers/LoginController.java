package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import services.IService;

import java.io.IOException;

public class LoginController {
    private IService server;
    //private TeacherController teacherController;
    //private Parent parent;


    public LoginController() {
    }

    @FXML
    TextField textboxUsername;

    @FXML
    TextField textboxPassword;

    @FXML
    Button buttonLogin;

    public void setService(IService service){
        this.server=service;
        init();
    }

    private void init(){

    }

    @FXML
    public void login() throws IOException{
        String username=textboxUsername.getText();
        String password=textboxPassword.getText();
        loginUser(username, password);
    }

    public void loginUser(String username, String password) throws IOException {
        Stage primaryStage=new Stage();

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/userView.fxml"));
        Parent root=loader.load();

        MainController mainController=loader.getController();
        User user= server.login(mainController,username,password);
        if (user == null){
            showErrorMessage("Datele introduse nu sunt corecte sau un joc este inceput deja!");
        }
        else {
            mainController.setUser(user);
            mainController.setService(server);

            primaryStage.setScene(new Scene(root, 700, 500));
            primaryStage.setTitle("USER " + username);
            primaryStage.show();
        }
    }

    private static void showErrorMessage(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }
}
