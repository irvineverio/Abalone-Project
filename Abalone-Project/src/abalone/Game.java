package abalone;

import utils.TextIO;

/**
 * Game to be used in local games.
 * 
 * @author Berke Guducu
 *
 */
public class Game {

    private Board board;

    private Player[] players;

    private int turnCount;

    public static final int TURNLIMIT = 96;

    /**
     * Creates game with 2 players.
     * 
     * @param p0 player 0 color.
     * @param p1 player 1 color.
     */
    public Game(Player p0, Player p1) {
        board = new BoardFor2(p0.getColour(), p1.getColour());
        players = new Player[2];
        players[0] = p0;
        players[1] = p1;
    }

    /**
     * Initialize a game with an array of players.
     * 
     * @requires players.length >= 2 && players.length <= 4
     * @param players array of players for the game.
     */
    public Game(Player[] players) {
        this.players = players.clone();
        if (players.length == 2) {
            board = new BoardFor2(players[0].getColour(), players[1].getColour());
        } else if (players.length == 3) {
            board = new BoardFor3(players[0].getColour(), players[1].getColour(), players[2].getColour());
        } else {
            board = new BoardFor4(players[0].getColour(), players[1].getColour(), players[2].getColour(),
                    players[3].getColour());
        }
    }

    /**
     * Create game with 3 players.
     * 
     * @param p0 player 0 color
     * @param p1 player 1 color
     * @param p2 player 2 color
     */
    // TODO check if moves are clockwise
    public Game(Player p0, Player p1, Player p2) {
        board = new BoardFor3(p0.getColour(), p1.getColour(), p2.getColour());
        players = new Player[3];
        players[0] = p0;
        players[1] = p1;
        players[2] = p2;
    }

    /**
     * Create game with 4 players.
     * 
     * @param p0 player 0 color.
     * @param p1 player 1 color.
     * @param p2 player 2 color.
     * @param p3 player 3 color.
     */
    public Game(Player p0, Player p1, Player p2, Player p3) {
        board = new BoardFor4(p0.getColour(), p1.getColour(), p2.getColour(), p3.getColour());
        // TODO implement
        players = new Player[3];
        players[0] = p0;
        players[1] = p1;
        players[2] = p2;
        players[3] = p3;
    }

    /**
     * Start the game.
     */
    public void start() {
        boolean contGame = true;
        while (contGame) {
            reset();
            turnCount = 0;
            play();
            System.out.println("another game?");
            contGame = TextIO.getlnBoolean();
        }
    }

    private void play() {
        System.out.println(board.coordHelp());
        System.out.println(board.toString());
        while (!(board.hasWinner()) && turnCount <= TURNLIMIT) { // while no loser or not turn-out
            // player i turn
            for (int i = 0; i < players.length; i++) {
                if (board.hasWinner()) {
                    String winnerNames = getWinnerName();
                    System.out.println("winners: " + winnerNames);
                    break;
                } else if (turnCount == TURNLIMIT) { // tie
                    if (board.getMostPointTeam() == null) {
                        System.out.println("It's a tie");
                    } else {
                        System.out.println("Winner=" + getWinnerName());
                    }
                    break;
                }
                players[i].makeMove(board);
                System.out.println(board.toString() + "turn" + (turnCount + 1));
                System.out.println("team 1 points:" + board.getTeams().get(0).getScore());
                System.out.println("team 2 points:" + board.getTeams().get(1).getScore());
            }
            turnCount++;
            // end of one turn
        }
    }

    /**
     * Get the names of the winners.
     * 
     * @return winner names in a String.
     */
    private String getWinnerName() {
        String winnerNames = "";
        Team winnerTeam;
        if (board.hasWinner()) {
            winnerTeam = board.getWinnerTeam();
        } else {
            winnerTeam = board.getMostPointTeam();
        }
        for (Colour c : winnerTeam.getColours()) {
            for (Player p : players) {
                if (p.getColour().equals(c)) {
                    winnerNames += p.getName();
                }
            }
        }
        return winnerNames;
    }

    /**
     * Reset the game by resetting the board.
     */
    private void reset() {
        board.reset();
    }

    // TODO create toSting method
}
