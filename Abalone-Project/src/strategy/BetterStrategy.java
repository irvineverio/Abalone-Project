package strategy;

import abalone.Board;
import abalone.Colour;
import abalone.Field;
import abalone.Marble;
import abalone.Move;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class implementation from Strategy for a dumb strategy in Abalone board
 * game project.
 *
 */
public class BetterStrategy implements Strategy {

    private String name = "BetterStrategy";

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the strategy.
     */
    @Override
    public String getName() {
        return this.name;
    }

    // -- Commands ---------------------------------------------------

    /**
     * Determine a valid move in the board by choosing a random move.
     * 
     * @param board that holds fields for marbles
     * @return a valid move in which the marble would be moved to
     */
    @Override
    public Move determineMove(Board board, Colour colour) {
        // get all color fields
        Random random = new Random();
        ArrayList<Move> allMoves = getAllMoves(board, colour);
        return allMoves.get(random.nextInt(allMoves.size())); // return random move
    }

    protected static ArrayList<Move> getAllMoves(Board board, Colour colour) {
        Field[][] fields = board.getFields();
        ArrayList<Field> playerFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) { // for all fields
                Marble marble = fields[i][j].getMarble();
                if (marble != null && marble.getColour().equals(colour)) { // find matching colors
                    playerFields.add(fields[i][j]);
                }
            }
        }
        // get all moves(with max number of marbles)
        // does not get all side-step moves for optimization
        Move move;
        ArrayList<Move> allMoves = new ArrayList<>();
        for (Field f : playerFields) { // for all the player fields
            Field currentField = f;
            Field field2 = currentField; // second field in the Move
            for (int d = 0; d < 6; d++) { // try all directions
                Field nextField = board.getNeighbor(currentField.getRow(), currentField.getCol(), d);
                if (nextField != null && nextField.getMarble() != null && // if not null and from same team
                        board.getTeam(nextField.getMarble().getColour()).equals(board.getTeam(colour))) {
                    field2 = nextField;
                    nextField = board.getNeighbor(nextField.getRow(), nextField.getCol(), d);
                    if (nextField != null && nextField.getMarble() != null && // if not null and from same team
                            board.getTeam(nextField.getMarble().getColour()).equals(board.getTeam(colour))) {
                        field2 = nextField;
                    }
                }
                for (int dd = 0; dd < 1; dd++) { // get side step moves as well
                    move = new Move(currentField.getRow(), currentField.getCol(), field2.getRow(), field2.getCol(),
                            (d + dd * 3) % 6); // get move to the other side as well
                    if (board.isValidMoveDForColour(move, colour)) {
                        allMoves.add(move);
                    }
                }
            }
        }
        return allMoves;
    }

}