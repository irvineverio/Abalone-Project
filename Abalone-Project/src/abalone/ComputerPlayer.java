package abalone;

import strategy.NaiveStrategy;
import strategy.Strategy;

/**
 * A class extended from Player in which maintains a computer player in Abalone
 * board game project.
 *
 * @author Berke Guducu
 */
public class ComputerPlayer extends Player {

    // -- Instance variables -----------------------------------------

    private Strategy strat;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new computer player object.
     * 
     * @requires colour of the marble is not null
     * @ensures the Marble colour of this computer player will be marble's colour
     * @param colour of the computer player's marble
     */
    public ComputerPlayer(Colour colour) {
        this(colour, new NaiveStrategy());
    }

    /**
     * Creates a new computer player object.
     * 
     * @requires strategy is not null
     * @requires marble's colour is not null
     * @ensures the Strategy of this computer player will be strategy
     * @ensures the Colour of marble of this computer player will be marble's colour
     * @param colour   of the computer player's marble
     * @param strategy of the computer player's strategy
     */
    public ComputerPlayer(Colour colour, Strategy strategy) {
        super(formatName(colour, strategy), colour);
        strat = strategy;
    }

    // -- Queries ---------------------------------------------------

    /**
     * Returns the name of the strategy.
     */
    public Strategy getStrategy() {
        return this.strat;
    }

    public void setStrategy(Strategy strategy) {
        strat = strategy;
    }

    // -- Commands ---------------------------------------------------

    /**
     * Returns the format name for the computer player's marble colour and strategy
     * into String that the user can read.
     * 
     * @param colour   of the computer player's marble
     * @param strategy of the computer player's
     * @return the string format name for the computer player's marble colour and
     *         its strategy
     */
    public static String formatName(Colour colour, Strategy strategy) {
        return strategy.getName() + " - " + colour.toString();
    }

    /**
     * Asks the user to input the field where to place the next marble. This is done
     * using the standard input/output.
     * 
     * @requires board is not null
     * @ensures the returned move is a valid move for marbles and the corresponding
     *          field is empty
     * @param board the game board
     * @return the player's chosen move
     */
    @Override
    public Move determineMove(Board board) {
        return strat.determineMove(board, getColour());
    }
}
