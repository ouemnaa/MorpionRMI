package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {
    void joinGame(Player player, GameClient client) throws RemoteException;
    void makeMove(Player player, int row, int col) throws RemoteException;
    boolean rematch(Player player) throws RemoteException;
    boolean isFull() throws RemoteException;
}