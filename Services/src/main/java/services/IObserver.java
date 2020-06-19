package services;

import model.Game;
import model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IObserver extends Remote {
    void nextRound(Game game, List<User> users) throws RemoteException;
    void gameStart() throws  RemoteException;
    void actualGameStarted() throws RemoteException;
}
