package abalone;

import abalone.Board;
import abalone.Colour;

/**
 * Abstract class for a player.
 * 
 *
 */
public abstract class Player {

    // -- Instance variables -----------------------------------------

    private String name;
    private Colour colour;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Player object.
     * 
     * @requires name is not null
     * @requires colour is not null
     * @ensures the Name of this player will be name
     * @ensures the Colour of this player will be colour of the player's marble
     */
    public Player(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
    }

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the colour of the player's marble.
     */
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * Determines the field for the next move.
     * 
     * @requires board is not null and not full
     * @ensures the returned int is a valid field index and that field is empty
     * @param board the current game board
     * @return the player's choice
     */
    public abstract Move determineMove(Board board);

    // -- Commands ---------------------------------------------------

    /**
     * Makes a move on the board. <br>
     * 
     * @requires board is not null and not full
     * @param board the current board
     */
    public void makeMove(Board board) {
        Move move = determineMove(board);
        board.moveD(move);
    }

}
