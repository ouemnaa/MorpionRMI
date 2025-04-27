package server;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class GameServer {
    public static void main(String[] args) {
        try {
            // Configuration de la sécurité
            System.setProperty("java.security.policy", "policies/server.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            
            // Création du registry intégré
            Registry registry = LocateRegistry.createRegistry(Constants.RMI_PORT);
            System.out.println("RMI Registry créé sur le port " + Constants.RMI_PORT);
            
            // Création de la factory
            GameFactory factory = new GameFactory();
            registry.rebind(Constants.SERVER_NAME, factory);
            System.out.println("GameFactory enregistrée dans le RMI Registry");
            
            // Configuration du codebase pour le chargement dynamique
            System.setProperty("java.rmi.server.codebase", Constants.CODEBASE_URL);
            System.out.println("Serveur prêt. URL du codebase: " + Constants.CODEBASE_URL);
        } catch (Exception e) {
            System.err.println("Erreur serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}