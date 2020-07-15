package abalone;

import abalone.HumanPlayer;
import strategy.BetterStrategy;
import strategy.MiniMaxStrategy;
import utils.TextIO;

/**
 * Abalone class to play games locally with both computer and human players.
 * 
 * @author Irvine & Berke
 *
 */
public class Abalone {
    static Game game;

    /**
     * Main method to launch games.
     * 
     * @param args not used.
     */
    public static void main(String[] args) {
        Player[] players;
        int plCount;
        do {
            System.out.println("How many players? (2-4)");
            plCount = TextIO.getlnInt();
        } while (plCount > 4 || plCount < 2);
        players = new Player[plCount];
        for (int i = 0; i < plCount; i++) {
            System.out.println("Please enter the name of the player" + i
                    + "(\n type cp for computer player \n sp for smart computer player): ");
            String namep = TextIO.getlnString();
            System.out.println("Player" + i + "is" + namep);
            if (namep.contains("cp")) {
                System.out.println("It's a computer player");
                players[i] = new ComputerPlayer(Colour.values()[i], new BetterStrategy());
            } else if (namep.contains("sp")) {
                System.out.println("It's a smart computer player");
                int depth;
                do {
                    System.out.println("What depth should the computer player use (1-3) \n "
                            + "note that at depth 3 moves can take up to a minute");
                    depth = TextIO.getlnInt();
                } while (depth > 3 || depth < 1);
                players[i] = new ComputerPlayer(Colour.values()[i], new MiniMaxStrategy(depth));
            } else {
                players[i] = new HumanPlayer(namep, Colour.values()[i]);
            }
        }
        Game game = new Game(players);
        System.out.println("Play " + game);
        game.start();
    }
}
