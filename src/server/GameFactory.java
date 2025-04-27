package server;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameFactory extends UnicastRemoteObject implements GameFactoryInterface {
    private List<Game> games;
    
    public GameFactory() throws RemoteException {
        games = new ArrayList<>();
    }
    
    @Override
    public synchronized Game createOrJoinGame() throws RemoteException {
        // Vérifier s'il y a une partie non pleine
        for (Game game : games) {
            if (!game.isFull()) {
                return game;
            }
        }
        
        // Créer une nouvelle partie
        Game newGame = new GameImpl();
        games.add(newGame);
        System.out.println("Nouvelle partie créée. Total: " + games.size());
        return newGame;
    }
}