package abalone;

/**
 * Board for 3 players.
 * 
 * @author Berke Guducu
 *
 */
public class BoardFor3 extends Board {

    /**
     * Initialize board for 3.
     * 
     * @param player1 bottom color
     * @param player2 top left color
     * @param player3 top right color
     */
    public BoardFor3(Colour player1, Colour player2, Colour player3) {
        assert player1 != null;
        assert player2 != null;
        assert player3 != null;
        initTeams(player1, player2, player3);
        setUpMarbles();
    }

    @Override
    public void reset() {
        super.reset();
        setUpMarbles();
    }

    /**
     * Set up the marbles on the board.
     */
    public void setUpMarbles() {
        Colour color0 = teams.get(0).getColours()[0];
        Colour color1 = teams.get(1).getColours()[0];
        Colour color2 = teams.get(2).getColours()[0];
        // colors for color 0:
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                setField((char) ('A' + i), j, new Marble(color0));
                setField((char) ('I' - j), DIM - 1 - i, new Marble(color1));
                setField((char) ('I' - j), SIDE_LENGTH - 1 + i - j, new Marble(color2));
            }
        }
        System.out.println(toString());
    }

}
