package server;

import abalone.Board;
import abalone.BoardFor2;
import abalone.BoardFor3;
import abalone.BoardFor4;
import abalone.Colour;
import abalone.Move;
import abalone.Player;
import abalone.Team;

/**
 * Server game class that is used in the server. Keeps track of the games. The
 * first player starts at the top according to the protocol.
 * 
 * @author Berke Guducu
 *
 */
public class ServerGame {

    private Board board;

    private Player[] players;

    private int turnCount;

    public static final int TURNLIMIT = 96;

    /**
     * 0 to start the game at bottom 1 to start the game at top.
     */
    public static final int START_AT_TOP = 1;

    /**
     * Creates game with 2 players. First player is at top, clockwise.
     * 
     * @param p0 player 0
     * @param p1 player 1
     */
    public ServerGame(Player p0, Player p1) {
        board = new BoardFor2(p1.getColour(), p0.getColour());
        players = new Player[2];
        players[0 + START_AT_TOP] = p0;
        players[1 - START_AT_TOP] = p1;
        turnCount = 0;
    }

    /**
     * Create game with 3 players. First player is at top, clockwise.
     * 
     * @param p0 player 0
     * @param p1 player 1
     * @param p2 player 2
     */
    public ServerGame(Player p0, Player p1, Player p2) {
        // board = new BoardFor3(p0.getColour(), p1.getColour(), p2.getColour());
        board = new BoardFor3(p1.getColour(), p2.getColour(), p0.getColour());
        players = new Player[3];
        players[0 + START_AT_TOP] = p0;
        players[1 + START_AT_TOP] = p1;
        players[(2 + START_AT_TOP) % 3] = p2;
        turnCount = 0;
    }

    /**
     * Create game with 4 players.
     * 
     * @param p0 player 0
     * @param p1 player 1
     * @param p2 player 2
     * @param p3 player 3
     */
    public ServerGame(Player p0, Player p1, Player p2, Player p3) {
        // board = new BoardFor4(p0.getColour(), p1.getColour(), p2.getColour(),
        // p3.getColour());
        board = new BoardFor4(p2.getColour(), p3.getColour(), p0.getColour(), p1.getColour());
        players = new Player[4];
        players[0 + START_AT_TOP * 2] = p0;
        players[1 + START_AT_TOP * 2] = p1;
        players[(2 - (START_AT_TOP * 2) % 4)] = p2;
        players[(3 - START_AT_TOP * 2) % 4] = p3;
        turnCount = 0;
    }

    public boolean isOver() {
        return board.hasWinner() || turnCount >= TURNLIMIT;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Make a move on the board, add to the turn count.
     * 
     * @param move   Move to be made.
     * @param player Player making the move.
     * @return false if invalid move or not player's turn
     */
    public boolean makeMove(Move move, Player player) {
        // if not player's turn or not a valid move in the board
        if (getPlayerIndex(player) != getTurn() || !board.isValidMoveDForColour(move, player.getColour())) {
            return false;
        }
        board.moveD(move);
        turnCount++;
        return true;
    }

    /**
     * Get the winners of this game.
     * 
     * @return
     */
    public Player[] getWinners() {
        Player[] winners = null;
        if (board.getWinnerTeam() == null) { // no winners -> tie or team with most point
            Team winner = board.getMostPointTeam();
            if (winner == null) {
                return null;
            } else {
                winners = new Player[winner.getColours().length];
                for (int i = 0; i < winners.length; i++) {
                    winners[i] = this.getPlayer(winner.getColours()[i]);
                }
                return winners;
            }
        }
        Colour[] winnerColours = board.getWinnerTeam().getColours();
        if (winnerColours.length == 1) { // one winner, 2 or 3 player game
            winners = new Player[1];
            for (Player p : players) {
                if (p.getColour() == winnerColours[0]) { // if player color is in winner colors
                    winners[0] = p; // add p to winners
                    return winners;
                }
            }
        } else if (winnerColours.length == 2) { // two winners, 4 player game
            winners = new Player[2];
            for (Player p : players) {
                if (p.getColour() == winnerColours[0]) { // if player color is win color 0
                    winners[0] = p;
                }
                if (p.getColour() == winnerColours[1]) { // if player color is win color 1
                    winners[1] = p;
                }
            }
        }
        return winners;
    }

    /**
     * find the player with the given colour.
     * 
     * @param colour of this player.
     * @return
     */
    public Player getPlayer(Colour colour) {
        for (Player p : players) { // for every player
            if ((p.getColour().equals(colour))) { // if color same
                return p;
            }
        }
        return null;
    }

    /**
     * Get the index of the given player. 0 is top going clockwise.
     * 
     * @param player player to get the index of.
     * @return the player's index.
     */
    public int getPlayerIndex(Player player) {
        for (int i = 0; i < players.length; i++) {
            Player p = players[i];
            if (p.equals(player)) {
                return (i + (players.length / 2) * START_AT_TOP) % players.length;
            }
        }
        assert false;
        return -1;
    }

    /**
     * Get player with the given name.
     * 
     * @param name to search with.
     * @return the player found, null if not found.
     */
    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Check if it is a player's turn.
     * 
     * @param player to check.
     * @return true if players turn.
     */
    public boolean isPlayersTurn(Player player) {
        return (getPlayerIndex(player) == getTurn());
    }

    public int getTurn() {
        return turnCount % players.length;
    }
}
