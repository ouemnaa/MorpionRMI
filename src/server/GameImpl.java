package server;
import shared.Game;
import shared.Player;
import shared.GameClient;
import shared.GameFactoryInterface;
import shared.Constants;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class GameImpl extends UnicastRemoteObject implements Game {
    private char[][] board;
    private Player[] players;
    private GameClient[] clients;
    private int currentPlayer;
    private boolean gameOver;
    
    public GameImpl() throws RemoteException {
        board = new char[3][3];
        players = new Player[2];
        clients = new GameClient[2];
        resetGame();
    }
    
    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = new Random().nextInt(2);
        gameOver = false;
        
        if (players[0] != null) players[0].setRematch(false);
        if (players[1] != null) players[1].setRematch(false);
    }
    
    @Override
    public synchronized void joinGame(Player player, GameClient client) throws RemoteException {
        if (players[0] == null) {
            players[0] = player;
            clients[0] = client;
            client.setSymbol('X');
            System.out.println(player.getName() + " a rejoint la partie (X)");
        } else if (players[1] == null) {
            players[1] = player;
            clients[1] = client;
            client.setSymbol('O');
            System.out.println(player.getName() + " a rejoint la partie (O)");
            
            // Démarrer le jeu
            clients[currentPlayer].yourTurn();
            System.out.println("La partie commence! " + players[currentPlayer].getName() + " commence.");
        }
    }
    
    @Override
    public synchronized void makeMove(Player player, int row, int col) throws RemoteException {
        if (gameOver || !players[currentPlayer].equals(player)) {
            return;
        }
        
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ') {
            clients[currentPlayer].yourTurn(); // Rejouer
            return;
        }
        
        board[row][col] = (currentPlayer == 0) ? 'X' : 'O';
        System.out.println(players[currentPlayer].getName() + " a joué en (" + row + "," + col + ")");
        
        // Vérifier victoire
        if (checkWin()) {
            gameOver = true;
            String result = "Le joueur " + players[currentPlayer].getName() + " a gagné!";
            System.out.println(result);
            notifyPlayers(result);
        } else if (isBoardFull()) {
            gameOver = true;
            String result = "Match nul!";
            System.out.println(result);
            notifyPlayers(result);
        } else {
            currentPlayer = 1 - currentPlayer;
            clients[currentPlayer].yourTurn();
            System.out.println("Tour de " + players[currentPlayer].getName());
        }
        
        // Mettre à jour les tableaux de tous les joueurs
        updateAllBoards();
    }
    
    private boolean checkWin() {
        // Vérifier les lignes
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        
        // Vérifier les colonnes
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }
        
        // Vérifier les diagonales
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        
        return false;
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void notifyPlayers(String result) throws RemoteException {
        for (GameClient client : clients) {
            if (client != null) {
                client.gameOver(result);
            }
        }
    }
    
    private void updateAllBoards() throws RemoteException {
        for (GameClient client : clients) {
            if (client != null) {
                client.updateBoard(board);
            }
        }
    }
    
    @Override
    public synchronized boolean rematch(Player player) throws RemoteException {
        if (players[0] != null && players[1] != null) {
            if (players[0].equals(player)) {
                players[0].setRematch(true);
            } else if (players[1].equals(player)) {
                players[1].setRematch(true);
            }
            
            if (players[0].wantsRematch() && players[1].wantsRematch()) {
                System.out.println("Nouvelle partie entre " + players[0].getName() + " et " + players[1].getName());
                resetGame();
                updateAllBoards();
                clients[currentPlayer].yourTurn();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isFull() throws RemoteException {
        return players[0] != null && players[1] != null;
    }
}