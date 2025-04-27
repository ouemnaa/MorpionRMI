package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameFactoryInterface extends Remote {
    Game createOrJoinGame() throws RemoteException;
}