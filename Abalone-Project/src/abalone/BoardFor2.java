package abalone;

/**
 * Board for 2 players.
 * 
 * @author Berke Guducu
 *
 */
public class BoardFor2 extends Board {

    /**
     * 2 Player board initializer for potential subclasses. Does not initialize
     * fields or marbles.
     */
    public BoardFor2() {

    }

    /**
     * Initialize a board for 2 players, setting up the marbles.
     * 
     * @param player1 color to play at the bottom
     * @param player2 color to play at the top
     */
    public BoardFor2(Colour player1, Colour player2) {
        assert player1 != null;
        assert player2 != null;
        Colour[] c0 = { player1 };
        Colour[] c1 = { player2 };
        initTeams(c0, c1);
        setUpMarbles();
    }

    @Override
    public void reset() {
        super.reset();
        setUpMarbles();
    }

    /**
     * Set up all the initial marbles in their positions.
     */
    public void setUpMarbles() {
        Colour color0 = teams.get(0).getColours()[0];
        Colour color1 = teams.get(1).getColours()[0];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                setField((char) ('A' + i), j, new Marble(color0));
                setFieldwIndex(DIM - 1 - i, j, new Marble(color1));
            }
        }
        setField('C', 2, new Marble(color0));
        setField('C', 3, new Marble(color0));
        setField('C', 4, new Marble(color0));
        setField((char) ('A' + DIM - 1 - 2), 4, new Marble(color1));
        setField((char) ('A' + DIM - 1 - 2), 5, new Marble(color1));
        setField((char) ('A' + DIM - 1 - 2), 6, new Marble(color1));
        System.out.println(toString());
    }

}
