
package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClient extends Remote {
    void yourTurn() throws RemoteException;
    void updateBoard(char[][] board) throws RemoteException;
    void gameOver(String result) throws RemoteException;
    void setSymbol(char symbol) throws RemoteException;
}