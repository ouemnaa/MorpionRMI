package client;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import shared.GameClient;
public class GameClientImpl extends UnicastRemoteObject implements GameClient {
    private ClientGUI gui;
    
    public GameClientImpl(ClientGUI gui) throws RemoteException {
        this.gui = gui;
    }
    
    @Override
    public void yourTurn() throws RemoteException {
        gui.yourTurn();
    }
    
    @Override
    public void updateBoard(char[][] board) throws RemoteException {
        gui.updateBoard(board);
    }
    
    @Override
    public void gameOver(String result) throws RemoteException {
        gui.gameOver(result);
    }
    
    @Override
    public void setSymbol(char symbol) throws RemoteException {
        gui.setMySymbol(symbol);
    }
}