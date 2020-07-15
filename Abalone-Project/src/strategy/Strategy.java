package strategy;

import abalone.Board;
import abalone.Colour;
import abalone.Move;

/**
 * An interface for a strategy to be used by computer players.
 *
 */
public interface Strategy {

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the strategy.
     * 
     * @return the name of the strategy.
     */
    public String getName();

    // -- Commands ---------------------------------------------------

    /**
     * Returns an index for a specific move of player marble's that would be moved
     * inside the board in the class implementation It takes board as an argument.
     * 
     * @param board that holds fields for marbles
     * @return the move in which the marble would be moved to
     */
    public Move determineMove(Board board, Colour colour);
}
