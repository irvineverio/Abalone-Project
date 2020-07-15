package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import abalone.BoardFor2;
import abalone.Colour;
import abalone.Marble;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A general method testing for the BoardFor2 class with its rules.
 * 
 * @author Irvine Verio
 *
 */
public class BoardFor2Test {
    private static final int TEST_MARBLES_COUNT = 8;
    private BoardFor2 board;
    private Marble[] blackMarbles;
    private Marble[] whiteMarbles;

    /**
     * Initializing before each method.
     */
    @BeforeEach
    public void setUp() {
        board = new BoardFor2(Colour.BLACK, Colour.WHITE);
        blackMarbles = new Marble[TEST_MARBLES_COUNT];
        whiteMarbles = new Marble[TEST_MARBLES_COUNT];
        for (int i = 0; i < TEST_MARBLES_COUNT; i++) {
            blackMarbles[i] = new Marble(Colour.BLACK);
            whiteMarbles[i] = new Marble(Colour.WHITE);
        }
    }

    @Test
    public void testSetUpMarbles() {
        System.out.println(board.toString());
    }

    @Test
    public void testMoveD1() {
        // in-line move 1 marble
        board.moveD('G', 4, 'G', 4, 4);
        assertTrue(board.getField('G', 4).getMarble() == null);
        assertFalse(board.getField('F', 3).getMarble() == null);
        // side-step move
        board.moveD('F', 3, 'F', 3, 2);
        assertTrue(board.getField('F', 3).getMarble() == null);
        assertFalse(board.getField('F', 4).getMarble() == null);
    }

    @Test
    public void testMoveD2() {
        // in-line move 2 marbles
        // head move 2 marbles
        board.moveD('G', 4, 'H', 5, 4);
        assertTrue(board.getField('H', 5).getMarble() == null);
        assertFalse(board.getField('G', 4).getMarble() == null);
        assertFalse(board.getField('F', 3).getMarble() == null);
        // tail move 2 marbles
        board.moveD('H', 6, 'G', 6, 3);
        assertTrue(board.getField('H', 6).getMarble() == null);
        assertFalse(board.getField('G', 6).getMarble() == null);
        assertFalse(board.getField('F', 6).getMarble() == null);
        // side-step move
        board.moveD('G', 6, 'F', 6, 2);
        assertTrue(board.getField('G', 6).getMarble() == null);
        assertTrue(board.getField('F', 6).getMarble() == null);
        assertFalse(board.getField('G', 7).getMarble() == null);
        assertFalse(board.getField('F', 7).getMarble() == null);
    }

    @Test
    public void testMoveD3() {
        // in-line move 3 marbles
        // head move 3 marbles
        board.moveD('G', 4, 'I', 4, 3);
        assertTrue(board.getField('I', 4).getMarble() == null);
        assertFalse(board.getField('H', 4).getMarble() == null);
        assertFalse(board.getField('G', 4).getMarble() == null);
        assertFalse(board.getField('F', 4).getMarble() == null);
        // tail move 3 marbles
        board.moveD('H', 4, 'F', 4, 3);
        assertTrue(board.getField('H', 4).getMarble() == null);
        assertFalse(board.getField('G', 4).getMarble() == null);
        assertFalse(board.getField('F', 4).getMarble() == null);
        assertFalse(board.getField('E', 4).getMarble() == null);
        // side-step move
        board.moveD('E', 4, 'G', 4, 4);
        assertTrue(board.getField('E', 4).getMarble() == null);
        assertTrue(board.getField('F', 4).getMarble() == null);
        assertTrue(board.getField('G', 4).getMarble() == null);
        assertFalse(board.getField('D', 3).getMarble() == null);
        assertFalse(board.getField('E', 3).getMarble() == null);
        assertFalse(board.getField('F', 3).getMarble() == null);
    }

    @Test
    public void testMoveD2Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('F', 8, blackMarbles[0]);
        board.setField('G', 8, blackMarbles[1]);
        board.setField('E', 8, whiteMarbles[0]);
        board.moveD('F', 8, 'G', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('E', 8).getMarble());
        assertEquals(blackMarbles[1], board.getField('F', 8).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('F', 8, blackMarbles[0]);
        board.setField('G', 8, blackMarbles[1]);
        board.setField('E', 8, whiteMarbles[0]);
        board.moveD('G', 8, 'F', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('E', 8).getMarble());
        assertEquals(blackMarbles[1], board.getField('F', 8).getMarble());
        board.reset();
    }

    @Test
    public void testMoveD3Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('G', 8, blackMarbles[0]);
        board.setField('F', 8, blackMarbles[1]);
        board.setField('E', 8, whiteMarbles[0]);
        board.moveD('F', 8, 'H', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('F', 8).getMarble());
        assertEquals(blackMarbles[1], board.getField('E', 8).getMarble());
        board.reset();
        // push 2 marbles until out with head
        board.setField('G', 8, blackMarbles[0]);
        board.setField('F', 8, whiteMarbles[0]);
        board.setField('E', 8, whiteMarbles[1]);
        board.moveD('G', 8, 'I', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[1]));
        assertEquals(blackMarbles[0], board.getField('F', 8).getMarble());
        assertEquals(whiteMarbles[0], board.getField('E', 8).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('G', 8, blackMarbles[0]);
        board.setField('F', 8, blackMarbles[1]);
        board.setField('E', 8, whiteMarbles[0]);
        board.moveD('H', 8, 'F', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('F', 8).getMarble());
        assertEquals(blackMarbles[1], board.getField('E', 8).getMarble());
        board.reset();
        // push 2 marbles until out with tail
        board.setField('G', 8, blackMarbles[0]);
        board.setField('F', 8, whiteMarbles[0]);
        board.setField('E', 8, whiteMarbles[1]);
        board.moveD('I', 8, 'G', 8, 3);
        assertTrue(board.getOuterRim().contains(whiteMarbles[1]));
        assertEquals(blackMarbles[0], board.getField('F', 8).getMarble());
        assertEquals(whiteMarbles[0], board.getField('E', 8).getMarble());
        board.reset();
    }

    @Test
    public void testIsValidMoveD1() {
        // move a marble to a correct position
        assertTrue(board.isValidMoveD('G', 4, 'G', 4, 3));
        assertTrue(board.isValidMoveD('G', 4, 'G', 4, 4));
        // move a marble to a wrong position
        assertFalse(board.isValidMoveD('G', 4, 'G', 4, 0));
        assertFalse(board.isValidMoveD('G', 4, 'G', 4, 1));
    }

    @Test
    public void testIsValidMoveD2() {
        // in-line move
        // move 2 marbles to a correct position with head
        assertTrue(board.isValidMoveD('G', 4, 'H', 4, 3));
        assertTrue(board.isValidMoveD('G', 4, 'H', 5, 4));
        // move 2 marbles to a wrong position with head
        assertFalse(board.isValidMoveD('H', 4, 'G', 4, 0));
        assertFalse(board.isValidMoveD('H', 5, 'G', 4, 1));
        // push 1 marble with head
        board.setField('G', 8, blackMarbles[0]);
        assertTrue(board.isValidMoveD('H', 8, 'I', 8, 3));
        // push 1 marble out with head
        board.setField('G', 7, whiteMarbles[0]);
        assertTrue(board.isValidMoveD('G', 7, 'G', 6, 2));

        // move 2 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException

        // move 2 marbles to a correct position with tail
        assertTrue(board.isValidMoveD('H', 4, 'G', 4, 3));
        assertTrue(board.isValidMoveD('H', 5, 'G', 4, 4));
        // move 2 marbles to a wrong position with tail
        assertFalse(board.isValidMoveD('G', 4, 'H', 4, 0));
        assertFalse(board.isValidMoveD('G', 4, 'H', 5, 1));
        // push 1 marble with tail
        assertTrue(board.isValidMoveD('I', 8, 'H', 8, 3));
        // push 1 marble out with tail
        assertTrue(board.isValidMoveD('G', 6, 'G', 7, 2));
        // move 2 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException

        // side-step move
        // move 2 marbles to a correct position
        assertTrue(board.isValidMoveD('G', 4, 'H', 4, 4));
        // move 2 marbles to a wrong position
        assertFalse(board.isValidMoveD('G', 4, 'H', 4, 1));
        assertFalse(board.isValidMoveD('G', 4, 'H', 4, 2));

        // invalid moves
        // moving a friend's marble
        assertFalse(board.isValidMoveD('H', 4, 'G', 4, 0));
        // moving a marble that is stuck in the middle of the same colour
        board.setField('F', 8, blackMarbles[0]);
        board.setField('E', 8, whiteMarbles[1]);
        assertFalse(board.isValidMoveD('G', 8, 'H', 8, 3));
        // push 2 marbles -> invalid
        board.setField('F', 5, whiteMarbles[2]);
        board.setField('E', 5, whiteMarbles[3]);
        assertFalse(board.isValidMoveD('G', 5, 'H', 5, 3));
        // push 3 marbles -> invalid
        board.setField('D', 5, whiteMarbles[4]);
        assertFalse(board.isValidMoveD('G', 5, 'H', 5, 3));
    }

    @Test
    public void testIsValidMoveD3() {
        // in-line move
        // move 3 marbles to a correct position with head
        assertTrue(board.isValidMoveD('G', 4, 'I', 4, 3));
        assertTrue(board.isValidMoveD('G', 4, 'I', 6, 4));
        // move 3 marbles to a wrong position with head
        board.setField('F', 4, whiteMarbles[0]);
        assertFalse(board.isValidMoveD('H', 4, 'F', 4, 0));
        board.setField('F', 3, whiteMarbles[1]);
        assertFalse(board.isValidMoveD('H', 5, 'F', 3, 1));
        // push 1 marble with head
        board.setField('G', 8, whiteMarbles[2]);
        board.setField('F', 8, blackMarbles[0]);
        assertTrue(board.isValidMoveD('G', 8, 'I', 8, 3));
        // push 1 marble out with head
        board.setField('F', 5, whiteMarbles[3]);
        board.setField('F', 6, whiteMarbles[4]);
        board.setField('F', 7, whiteMarbles[5]);
        assertTrue(board.isValidMoveD('F', 7, 'F', 5, 2));
        // push 2 marbles with head
        board.setField('E', 8, blackMarbles[1]);
        assertTrue(board.isValidMoveD('G', 8, 'I', 8, 3)); // -> dont know why throw and index out of bounds
        // push 2 marbles out with head
        assertTrue(board.isValidMoveD('G', 8, 'I', 8, 3));
        // move 3 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException
        board.reset();

        // move 3 marbles to a correct position with tail
        assertTrue(board.isValidMoveD('I', 4, 'G', 4, 3));
        assertTrue(board.isValidMoveD('I', 6, 'G', 4, 4));
        // move 3 marbles to a wrong position with tail
        board.setField('F', 4, whiteMarbles[0]);
        assertFalse(board.isValidMoveD('F', 4, 'H', 4, 0));
        board.setField('F', 3, whiteMarbles[1]);
        assertFalse(board.isValidMoveD('F', 3, 'H', 5, 1));
        // push 1 marble with tail
        board.setField('G', 8, whiteMarbles[2]);
        board.setField('F', 8, blackMarbles[0]);
        assertTrue(board.isValidMoveD('I', 8, 'G', 8, 3));
        // push 1 marble out with tail
        board.setField('F', 5, whiteMarbles[3]);
        board.setField('F', 6, whiteMarbles[4]);
        board.setField('F', 7, whiteMarbles[5]);
        assertTrue(board.isValidMoveD('F', 5, 'F', 7, 2));
        // push 2 marbles with tail
        assertTrue(board.isValidMoveD('I', 8, 'G', 8, 3));
        // push 2 marbles out with tail
        assertTrue(board.isValidMoveD('I', 8, 'G', 8, 3));
        // move 3 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException
        board.reset();

        // side-step move
        // move 3 marbles to a correct position
        board.setField('F', 4, whiteMarbles[6]);
        assertTrue(board.isValidMoveD('F', 4, 'H', 4, 4));
        // move 3 marbles to a wrong position
        assertFalse(board.isValidMoveD('F', 4, 'H', 4, 1));
        assertFalse(board.isValidMoveD('F', 4, 'H', 4, 2));

        // invalid move
        // moving a friend's marble
        assertFalse(board.isValidMoveD('H', 6, 'H', 4, 2));
        // moving a marble that is stuck in the middle of the same colour
        board.setField('G', 2, whiteMarbles[7]);
        board.setField('G', 3, blackMarbles[2]);
        assertFalse(board.isValidMoveD('G', 4, 'G', 6, 5)); // -> still return true
        // push 3 marbles -> invalid
        board.setField('E', 4, blackMarbles[3]);
        board.setField('D', 4, blackMarbles[4]);
        assertFalse(board.isValidMoveD('F', 4, 'H', 4, 3));
    }
}