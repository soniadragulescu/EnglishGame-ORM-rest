package services;

import model.Game;
import model.User;

import java.util.List;

public interface IService {
    User login(IObserver client, String username, String password);
    void logout(IObserver client, String username);
    void updateUser(String username, Integer score);
    void updateGame(String user, String word);
    Game getLastGame();
    void startGame();
    void actualGameStarted();
}
