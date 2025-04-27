package client;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;

import javax.swing.JOptionPane;

public class ClientLauncher {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "policies/client.policy");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        String playerName = JOptionPane.showInputDialog("Entrez votre nom:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            new ClientGUI(playerName);
        } else {
            System.out.println("Un nom est requis pour jouer");
            System.exit(1);
        }
    }
}