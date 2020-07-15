package abalone;

import exceptions.MoveFormatException;
import utils.TextIO;

/**
 * A human player to use in the games. Can ask for input.
 *
 */
public class HumanPlayer extends Player {

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new human player object.
     * 
     * @requires name is not null
     * @requires colour of the marble is not null
     * @ensures the Name of this player will be name
     * @ensures the Colour of marble of this player will be marble's colour
     */
    public HumanPlayer(String name, Colour colour) {
        super(name, colour);
    }

    // -- Commands ---------------------------------------------------

    /**
     * Asks the user to input the field where to place the next marble. This is done
     * using the standard input/output.
     * 
     * @requires board is not null
     * @ensures the returned int is a valid field index and the corresponding field
     *          is empty
     * @param board the game board
     * @return the player's chosen field
     */
    @Override
    public Move determineMove(Board board) {
        // asking for an input String
        String prompt = "> " + getName() + " (" + getColour().toString() + ")" + ", what is your choice? \n"
                + "The input should be in format example: 'A1,B1,1'";

        System.out.println(prompt);
        // view:

        Move move = parseMove();

        // check if the move 'A1,B1,1' is a valid move with isValidMoveD() method
        boolean valid = board.isValidMoveDForColour(move, getColour()); // direction
        while (!valid) {
            System.out.println("ERROR: move: " + move + " is not a valid move.");
            System.out.println(prompt);
            move = parseMove();
            valid = board.isValidMoveDForColour(move, getColour());
        }
        return move;

    }

    /**
     * Ask user for move and keep asking until a decent move is provided.
     * 
     * @return the Move that has been obtained from the user.
     */
    private Move parseMove() {
        Move move = null;
        while (move == null) {
            try {
                String choice = TextIO.getlnString(); // vie
                move = Move.parse(choice);
            } catch (MoveFormatException e) {
                System.out.println("Invalid format:" + e.getMessage() + "try again");
            }
        }
        return move;
    }
}
