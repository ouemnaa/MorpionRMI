package client;

import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private Game game;
    private Player player;
    private GameClientImpl clientImpl;
    private char mySymbol;
    private boolean myTurn;

    // Cute calm theme 🎨
    private final Color backgroundColor = new Color(245, 245, 255); // soft lavender
    private final Color buttonColor = new Color(255, 255, 255); // pure white
    private final Color hoverColor = new Color(220, 230, 255); // baby blue hover
    private final Color xColor = new Color(100, 150, 255); // pastel blue
    private final Color oColor = new Color(255, 150, 200); // pastel pink

    public ClientGUI(String playerName) {
        super("Morpion - " + playerName);
        this.player = new Player(playerName);
        initializeGUI();
        connectToServer();
    }

    private void initializeGUI() {
        getContentPane().setBackground(backgroundColor);
        setLayout(new GridLayout(3, 3, 15, 15)); // spacing between buttons
        setUndecorated(false);

        // Add nice padding around grid
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = createStyledButton();
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> {
                    if (myTurn) {
                        makeMove(row, col);
                    }
                });
                add(buttons[i][j]);
            }
        }

        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createStyledButton() {
        JButton button = new JButton();
        button.setFont(new Font("Quicksand", Font.BOLD, 50));
        button.setForeground(Color.GRAY);
        button.setBackground(buttonColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 255), 2, true)); // soft border
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setBorder(BorderFactory.createLineBorder(new Color(170, 170, 255), 2, true));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(buttonColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 255), 2, true));
            }
        });

        return button;
    }

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT);
            GameFactoryInterface factory = (GameFactoryInterface) registry.lookup(Constants.SERVER_NAME);

            clientImpl = new GameClientImpl(this);
            game = factory.createOrJoinGame();
            game.joinGame(player, clientImpl);
        } catch (Exception e) {
            showMessage("Erreur de connexion: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
            showMessage("Erreur lors du coup: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBoard(char[][] board) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        buttons[i][j].setText("");
                        buttons[i][j].setEnabled(myTurn);
                    } else {
                        buttons[i][j].setText(String.valueOf(board[i][j]));
                        buttons[i][j].setEnabled(false);
                        if (board[i][j] == 'X') {
                            buttons[i][j].setForeground(xColor);
                        } else if (board[i][j] == 'O') {
                            buttons[i][j].setForeground(oColor);
                        }
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
            int option = JOptionPane.showOptionDialog(this,
                    result + "\nVoulez-vous rejouer?",
                    "Partie Terminée",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Oui", "Non"},
                    "Oui");

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
            showMessage("C'est votre tour ! Vous jouez les " + mySymbol, "Votre Tour", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void showMessage(String message, String title, int type) {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", Color.DARK_GRAY);
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
