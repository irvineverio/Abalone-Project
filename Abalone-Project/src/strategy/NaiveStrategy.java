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
public class NaiveStrategy implements Strategy {

    private String name = "Naive";
    public static final int SIDE_LENGTH = 5;
    public static final int DIM = (SIDE_LENGTH * 2) - 1;

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
     * Determine a valid move in the board by choosing a random field which belongs
     * to this color.
     * 
     * @param board that holds fields for marbles
     * @return a valid move in which the marble would be moved to
     */
    @Override
    public Move determineMove(Board board, Colour colour) {
        Random random = new Random();
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
        Move move;
        do { // find random move
            int randomInteger = random.nextInt(playerFields.size());
            Field randomField = playerFields.get(randomInteger);
            int direction = random.nextInt(6);
            move = new Move(randomField.getRow(), randomField.getCol(), randomField.getRow(), randomField.getCol(),
                    direction);
        } while (!board.isValidMoveDForColour(move, colour));
        return move; // return random move
    }

}