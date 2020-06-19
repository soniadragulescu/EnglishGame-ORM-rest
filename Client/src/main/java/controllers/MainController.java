package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Game;
import model.User;
import services.IObserver;
import services.IService;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    private IService service;
    private User user;

    public MainController() throws RemoteException {
    }

    public void setUser(User user){
        this.user=user;
    }

    public void setService(IService service){
        this.service=service;
        init();
    }

    @FXML
    Label labelWait;

    @FXML
    Label labelCategory;

    @FXML
    Label labelScore;

    @FXML
    Label labelRound;

    @FXML
    TextField textboxWord;

    @FXML
    Button buttonStart;

    @FXML
    Button buttonLogout;

    @FXML
    Button buttonSendWord;



    public void init(){
        buttonStart.setVisible(false);

        labelRound.setVisible(false);
        labelCategory.setVisible(false);
        labelScore.setVisible(false);
        textboxWord.setVisible(false);
        buttonSendWord.setVisible(false);
    }

    @FXML
    public void logout(){
        this.service.logout(this,this.user.getUsername());
        Platform.exit();
    }


    @Override
    public void gameStart(){
        labelWait.setVisible(false);

        buttonStart.setVisible(true);
    }

    @FXML
    public void startGame(){
        buttonStart.setVisible(false);

        labelCategory.setVisible(true);
        labelScore.setVisible(true);
        labelRound.setVisible(true);
        textboxWord.setVisible(true);
        buttonSendWord.setVisible(true);

        service.actualGameStarted();
    }

    @Override
    public void actualGameStarted() throws RemoteException {
        buttonStart.setVisible(false);

        labelCategory.setVisible(true);
        labelScore.setVisible(true);
        labelRound.setVisible(true);
        textboxWord.setVisible(true);
        buttonSendWord.setVisible(true);
    }


    private static void showErrorMessage(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }

    @FXML
    public void sendWord() {
        String word=textboxWord.getText();
        service.updateGame( this.user.getUsername(),word);
        buttonSendWord.setVisible(false);
    }

    @Override
    public void nextRound(Game game, List<User> users) throws RemoteException {
        User newuser=this.user;
        if(game.getRound()<=4){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Integer round=game.getRound();
                    String nextCategory=game.getCategorii().get(round);
                    for(User u:users) {
                        if (u.getUsername().equals(newuser.getUsername()))
                            newuser.setScore(u.getScore());
                    }

                    labelRound.setText("Round: "+round.toString());
                    labelCategory.setText("Category: "+nextCategory);
                    labelScore.setText("Score: "+newuser.getScore().toString());

                    buttonSendWord.setVisible(true);
                }
            });
        }
        this.user.setScore(newuser.getScore());
        if(game.getRound()>4){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showErrorMessage("S-au gatat cele 5 runde, ai obtinut "+user.getScore().toString());
                }
            });
//            service.logout(this, newuser.getUsername());
//            Platform.exit();
        }

    }
}
