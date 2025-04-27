package client;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import shared.Game; // Import Game class
import shared.Player; // Import Player class

public class ClientGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private Game game;
    private Player player;
    private GameClientImpl clientImpl;
    private char mySymbol;
    private boolean myTurn;
    
    public ClientGUI(String playerName) {
        super("Morpion - " + playerName);
        this.player = new Player(playerName);
        initializeGUI();
        connectToServer();
    }
    
    private void initializeGUI() {
        setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setEnabled(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (myTurn) {
                            makeMove(row, col);
                        }
                    }
                });
                add(buttons[i][j]);
            }
        }
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT);
            GameFactoryInterface factory = (GameFactoryInterface) registry.lookup(Constants.SERVER_NAME);
            
            clientImpl = new GameClientImpl(this);
            game = factory.createOrJoinGame();
            game.joinGame(player, clientImpl);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void makeMove(int row, int col) {
        try {
            buttons[row][col].setEnabled(false);
            game.makeMove(player, row, col);
            myTurn = false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du coup: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateBoard(char[][] board) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(board[i][j] == ' ' ? "" : String.valueOf(board[i][j]));
                    buttons[i][j].setEnabled(board[i][j] == ' ' && myTurn);
                    
                    // Colorer les cases
                    if (board[i][j] == 'X') {
                        buttons[i][j].setForeground(Color.BLUE);
                    } else if (board[i][j] == 'O') {
                        buttons[i][j].setForeground(Color.RED);
                    }
                }
            }
        });
    }
    
    public void setMySymbol(char symbol) {
        this.mySymbol = symbol;
    }
    
    public void gameOver(String result) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, result, "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
            int option = JOptionPane.showConfirmDialog(this, "Voulez-vous rejouer?", "Rejouer?", 
                JOptionPane.YES_NO_OPTION);
            try {
                if (option == JOptionPane.YES_OPTION) {
                    game.rematch(player);
                } else {
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public void yourTurn() {
        SwingUtilities.invokeLater(() -> {
            myTurn = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().isEmpty()) {
                        buttons[i][j].setEnabled(true);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "C'est votre tour! Vous jouez les " + mySymbol, 
                "Votre tour", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}