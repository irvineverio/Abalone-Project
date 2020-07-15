package abalone;

/**
 * Board for 4 players.
 * @author Berke Guducu
 *
 */
public class BoardFor4 extends BoardFor2 {

    /**
     * Initialize Board for 4. Set up the marbles and teams.
     * 
     * @param c0 bottom color
     * @param c1 left color
     * @param c2 top color
     * @param c3 right color
     */
    public BoardFor4(Colour c0, Colour c1, Colour c2, Colour c3) {
        Colour[] team1 = { c0, c2 };
        Colour[] team2 = { c1, c3 };
        initTeams(team1, team2);
        setUpMarbles();

    }

    /**
     * Set up marbles on the board.
     */
    @Override
    public void setUpMarbles() {
        Colour color0 = teams.get(0).getColours()[0];
        Colour color1 = teams.get(1).getColours()[0];
        Colour color2 = teams.get(0).getColours()[1];
        Colour color3 = teams.get(1).getColours()[1];
        // colors for color 0:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < (fields[i].length - 1 - (i * 2)); j++) {
                setField((char) ('A' + i), j + 1 + i, new Marble(color0)); // bottom
                setField((char) ('I' - i), SIDE_LENGTH - 1 + j, new Marble(color2)); // top
                setField((char) ('A' + SIDE_LENGTH - 1 - j), i, new Marble(color1)); // left
                setField((char) ('A' + SIDE_LENGTH - 1 + j), DIM - 1 - i, new Marble(color3)); // right
            }
        }
        System.out.println(toString());
    }

    @Override
    public void reset() {
        super.reset();
        setUpMarbles();
    }

    @Override
    public boolean isValidMoveDForColour(Move move, Colour colour) {
        if (!isValidMoveD(move)) { // also checks if all marbles are from same team
            return false;
        }
        // at least one marble should be the same color as the one making the move
        Field head = move.getHead(this);
        Field tail = move.getTail(this);
        // Field dest = move.getDest(this);
        if (head.equals(tail)) { // moving one marble
            return head.getMarble().getColour().equals(colour);
        } else if (isColumn(head.getRow(), head.getCol(), tail.getRow(), tail.getCol())) { // moving two marbles
            if (!(head.getMarble().getColour().equals(colour))) {
                return false;
            }
        } else {
            // head(marble that moves first) should be the same color as given
            if (!(head.getMarble().getColour().equals(colour))) {
                return false;
            }
        }
        return true;
    }
}
