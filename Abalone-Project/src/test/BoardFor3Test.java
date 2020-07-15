package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import abalone.BoardFor3;
import abalone.Colour;
import abalone.Marble;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A general method testing for the BoardFor3 class with its rules.
 * 
 * @author Irvine Verio
 *
 */
public class BoardFor3Test {
    private static final int TEST_MARBLES_COUNT = 6;
    private BoardFor3 board;
    private Marble[] blackMarbles;
    private Marble[] whiteMarbles;
    private Marble[] redMarbles;

    /**
     * Initializing a before each method.
     */
    @BeforeEach
    public void setUp() {
        board = new BoardFor3(Colour.WHITE, Colour.RED, Colour.BLACK);
        blackMarbles = new Marble[TEST_MARBLES_COUNT];
        whiteMarbles = new Marble[TEST_MARBLES_COUNT];
        redMarbles = new Marble[TEST_MARBLES_COUNT];
        for (int i = 0; i < TEST_MARBLES_COUNT; i++) {
            blackMarbles[i] = new Marble(Colour.BLACK);
            whiteMarbles[i] = new Marble(Colour.WHITE);
            redMarbles[i] = new Marble(Colour.RED);
        }
    }

    @Test
    public void testSetUpMarbles() {
        System.out.println(board.toString());
    }

    @Test
    public void testMoveD1() {
        // in-line move 1 marble
        board.moveD('B', 0, 'B', 0, 0);
        assertTrue(board.getField('B', 0).getMarble() == null);
        assertFalse(board.getField('C', 0).getMarble() == null);
        // side-step move
        board.moveD('C', 0, 'C', 0, 2);
        assertTrue(board.getField('C', 0).getMarble() == null);
        assertFalse(board.getField('C', 1).getMarble() == null);
    }

    @Test
    public void testMoveD2() {
        // in-line move 2 marbles
        // head move 2 marbles
        board.moveD('B', 1, 'A', 1, 0);
        assertTrue(board.getField('A', 1).getMarble() == null);
        assertFalse(board.getField('B', 1).getMarble() == null);
        assertFalse(board.getField('C', 1).getMarble() == null);
        // tail move 2 marbles
        board.moveD('B', 1, 'C', 1, 0);
        assertTrue(board.getField('B', 1).getMarble() == null);
        assertFalse(board.getField('C', 1).getMarble() == null);
        assertFalse(board.getField('D', 1).getMarble() == null);
        // side-step move
        board.moveD('D', 1, 'C', 1, 2);
        assertTrue(board.getField('D', 1).getMarble() == null);
        assertTrue(board.getField('C', 1).getMarble() == null);
        assertFalse(board.getField('D', 2).getMarble() == null);
        assertFalse(board.getField('C', 2).getMarble() == null);
    }

    @Test
    public void testMoveD3() {
        // in-line move 3 marbles
        // head move 3 marbles
        board.setField('C', 3, whiteMarbles[0]);
        board.moveD('C', 3, 'A', 3, 0);
        assertTrue(board.getField('A', 3).getMarble() == null);
        assertFalse(board.getField('B', 3).getMarble() == null);
        assertFalse(board.getField('C', 3).getMarble() == null);
        assertFalse(board.getField('D', 3).getMarble() == null);
        // tail move 3 marbles
        board.moveD('B', 3, 'D', 3, 0);
        assertTrue(board.getField('B', 3).getMarble() == null);
        assertFalse(board.getField('C', 3).getMarble() == null);
        assertFalse(board.getField('D', 3).getMarble() == null);
        assertFalse(board.getField('E', 3).getMarble() == null);
        // side-step move
        board.moveD('E', 3, 'C', 3, 2);
        assertTrue(board.getField('E', 3).getMarble() == null);
        assertTrue(board.getField('D', 3).getMarble() == null);
        assertTrue(board.getField('C', 3).getMarble() == null);
        assertFalse(board.getField('E', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        assertFalse(board.getField('C', 4).getMarble() == null);
    }

    @Test
    public void testMoveD2Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, whiteMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        board.moveD('C', 5, 'C', 4, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, whiteMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        board.moveD('C', 4, 'C', 5, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        board.reset();
    }

    @Test
    public void testMoveD3Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, whiteMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        board.setField('C', 3, whiteMarbles[2]);
        board.moveD('C', 5, 'C', 3, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 4).getMarble());
        board.reset();
        // push 2 marbles with same colour until out with head
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, blackMarbles[1]);
        board.setField('C', 4, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 2, whiteMarbles[2]);
        board.moveD('C', 4, 'C', 2, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(blackMarbles[1], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 4).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 3).getMarble());
        board.reset();
        // push 2 marbles with different colour until out with head
        board.setField('C', 6, redMarbles[0]);
        board.setField('C', 5, blackMarbles[0]);
        board.setField('C', 4, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 2, whiteMarbles[2]);
        board.moveD('C', 4, 'C', 2, 2);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 4).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 3).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, whiteMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        board.setField('C', 3, whiteMarbles[2]);
        board.moveD('C', 3, 'C', 5, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 4).getMarble());
        board.reset();
        // push 2 marbles with same colour until out with tail
        board.setField('C', 6, blackMarbles[0]);
        board.setField('C', 5, blackMarbles[1]);
        board.setField('C', 4, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 2, whiteMarbles[2]);
        board.moveD('C', 2, 'C', 4, 2);
        assertTrue(board.getOuterRim().contains(blackMarbles[0]));
        assertEquals(blackMarbles[1], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 4).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 3).getMarble());
        board.reset();
        // push 2 marbles with different colour until out with tail
        board.setField('C', 6, redMarbles[0]);
        board.setField('C', 5, blackMarbles[0]);
        board.setField('C', 4, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 2, whiteMarbles[2]);
        board.moveD('C', 2, 'C', 4, 2);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(blackMarbles[0], board.getField('C', 6).getMarble());
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 4).getMarble());
        assertEquals(whiteMarbles[2], board.getField('C', 3).getMarble());
        board.reset();
    }

    @Test
    public void testIsValidMoveD1() {
        // move a marble to a correct position
        assertTrue(board.isValidMoveD('B', 0, 'B', 0, 0));
        assertTrue(board.isValidMoveD('B', 0, 'B', 0, 1));
        // move a marble to a wrong position
        assertFalse(board.isValidMoveD('A', 0, 'A', 0, 0));
        assertFalse(board.isValidMoveD('A', 0, 'A', 0, 5));
    }

    @Test
    public void testIsValidMoveD2() {
        // in-line move
        // move 2 marbles to a correct position with head
        assertTrue(board.isValidMoveD('B', 0, 'A', 0, 0));
        assertTrue(board.isValidMoveD('B', 1, 'A', 0, 1));
        // move 2 marbles to a wrong position with head
        assertFalse(board.isValidMoveD('B', 0, 'A', 0, 5));
        assertFalse(board.isValidMoveD('A', 0, 'B', 0, 4));
        // push 1 marble with head
        board.setField('C', 1, blackMarbles[0]);
        assertTrue(board.isValidMoveD('B', 1, 'A', 1, 0));
        // push 1 marble out with head
        board.setField('C', 6, blackMarbles[1]);
        board.setField('C', 5, whiteMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        assertTrue(board.isValidMoveD('C', 5, 'C', 4, 2));

        // move 2 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException

        // move 2 marbles to a correct position with tail
        assertTrue(board.isValidMoveD('A', 0, 'B', 0, 0));
        assertTrue(board.isValidMoveD('A', 0, 'B', 1, 1));
        // move 2 marbles to a wrong position with tail
        assertFalse(board.isValidMoveD('A', 0, 'B', 0, 5));
        assertFalse(board.isValidMoveD('B', 0, 'A', 0, 4));
        // push 1 marble with tail
        assertTrue(board.isValidMoveD('A', 1, 'B', 1, 0));
        // push 1 marble out with tail
        assertTrue(board.isValidMoveD('C', 4, 'C', 5, 2));
        // move 2 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException

        // side-step move
        // move 2 marbles to a correct position
        board.moveD('B', 3, 'A', 3, 0);
        board.moveD('C', 3, 'B', 3, 0);
        assertTrue(board.isValidMoveD('D', 3, 'C', 3, 5));
        // move 2 marbles to a wrong position
        assertFalse(board.isValidMoveD('B', 0, 'A', 0, 1));
        assertFalse(board.isValidMoveD('B', 0, 'A', 0, 2));

        // invalid moves
        // moving a friend's marble
        // moving a marble that is stuck in the middle of the same colour
        board.setField('C', 2, blackMarbles[2]);
        board.setField('D', 2, whiteMarbles[2]);
        assertFalse(board.isValidMoveD('B', 2, 'A', 2, 0));
        // push 2 marbles -> invalid
        board.setField('D', 4, blackMarbles[3]);
        assertFalse(board.isValidMoveD('B', 4, 'A', 4, 0));
        // push 3 marbles -> invalid
        board.setField('D', 5, blackMarbles[4]);
        assertFalse(board.isValidMoveD('B', 4, 'A', 4, 0));
    }

    @Test
    public void testIsValidMoveD3() {
        // in-line move
        // move 3 marbles to a correct position with head
        board.setField('C', 3, whiteMarbles[0]);
        assertTrue(board.isValidMoveD('C', 3, 'A', 3, 0));
        assertTrue(board.isValidMoveD('C', 3, 'A', 1, 1));
        // move 3 marbles to a wrong position with head
        assertFalse(board.isValidMoveD('A', 2, 'A', 0, 2));
        assertFalse(board.isValidMoveD('A', 2, 'A', 0, 4));
        // push 1 marble with head
        board.setField('D', 3, blackMarbles[0]);
        assertTrue(board.isValidMoveD('C', 3, 'A', 3, 0));
        // push 1 marble out with head
        board.setField('C', 4, whiteMarbles[1]);
        board.setField('C', 5, whiteMarbles[2]);
        board.setField('C', 6, blackMarbles[1]);
        assertTrue(board.isValidMoveD('C', 5, 'C', 3, 2));
        // push 2 marbles same colours with head
        board.setField('E', 3, blackMarbles[2]);
        assertTrue(board.isValidMoveD('C', 3, 'A', 3, 0));
        // push 2 marbles same colours out with head
        board.setField('C', 5, blackMarbles[3]);
        board.setField('C', 2, whiteMarbles[3]);
        assertTrue(board.isValidMoveD('C', 4, 'C', 2, 2));
        // push 2 marbles different colours with head
        board.setField('E', 3, redMarbles[1]);
        assertTrue(board.isValidMoveD('C', 3, 'A', 3, 0));
        // push 2 marbles different colours out with head
        board.setField('C', 6, redMarbles[1]);
        assertTrue(board.isValidMoveD('C', 4, 'C', 2, 2));
        board.reset();
        // move 3 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException

        // move 3 marbles to a correct position with tail
        board.setField('C', 3, whiteMarbles[0]);
        assertTrue(board.isValidMoveD('A', 3, 'C', 3, 0));
        assertTrue(board.isValidMoveD('A', 1, 'C', 3, 1));
        // move 3 marbles to a wrong position with tail
        assertFalse(board.isValidMoveD('A', 0, 'A', 2, 2));
        assertFalse(board.isValidMoveD('A', 0, 'A', 2, 4));
        // push 1 marble with tail
        board.setField('D', 3, blackMarbles[0]);
        assertTrue(board.isValidMoveD('A', 3, 'C', 3, 0));
        // push 1 marble out with tail
        board.setField('C', 4, whiteMarbles[1]);
        board.setField('C', 5, whiteMarbles[2]);
        board.setField('C', 6, blackMarbles[1]);
        assertTrue(board.isValidMoveD('C', 3, 'C', 5, 2));
        // push 2 marbles same colours with tail
        board.setField('E', 3, blackMarbles[2]);
        assertTrue(board.isValidMoveD('A', 3, 'C', 3, 0));
        // push 2 marbles same colours out with tail
        board.setField('C', 5, blackMarbles[3]);
        board.setField('C', 2, whiteMarbles[3]);
        assertTrue(board.isValidMoveD('C', 2, 'C', 4, 2));
        // push 2 marbles different colours with tail
        board.setField('E', 3, redMarbles[1]);
        assertTrue(board.isValidMoveD('A', 3, 'C', 3, 0));
        // push 2 marbles different colours out with tail
        board.setField('C', 6, redMarbles[1]);
        assertTrue(board.isValidMoveD('C', 4, 'C', 2, 2));
        board.reset();
        // move 3 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException

        // side-step move
        // move 3 marbles to a correct position
        assertTrue(board.isValidMoveD('B', 1, 'B', 3, 0));
        assertTrue(board.isValidMoveD('B', 1, 'B', 3, 1));
        // move 3 marbles to a wrong position
        assertFalse(board.isValidMoveD('B', 1, 'B', 3, 3));
        assertFalse(board.isValidMoveD('B', 1, 'B', 3, 4));

        // invalid move
        // moving a friend's marble
        // moving a marble that is stuck in the middle of the same colour
        board.setField('C', 6, whiteMarbles[0]);
        board.setField('C', 5, blackMarbles[0]);
        board.setField('C', 4, whiteMarbles[1]);
        board.setField('C', 3, whiteMarbles[2]);
        board.setField('C', 2, whiteMarbles[3]);
        assertFalse(board.isValidMoveD('C', 4, 'C', 2, 2));
        // push 3 marbles -> invalid
        board.setField('D', 4, blackMarbles[1]);
        board.setField('E', 4, blackMarbles[2]);
        board.setField('F', 4, blackMarbles[3]);
        assertFalse(board.isValidMoveD('C', 4, 'A', 4, 0));
    }

    @Test
    public void testIsWinner() {
        // test that the marble that has pushed off 6 marbles won
        // push out 4 black marbles
        board.setField('C', 2, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 4, whiteMarbles[2]);
        board.setField('C', 0, blackMarbles[0]);
        board.setField('C', 1, blackMarbles[1]);
        board.moveD('C', 2, 'C', 4, 5);
        board.moveD('C', 1, 'C', 3, 5);
        board.moveD('C', 2, 'C', 0, 2);
        board.moveD('C', 3, 'C', 1, 2);
        board.setField('C', 0, blackMarbles[2]);
        board.setField('C', 1, blackMarbles[3]);
        board.moveD('C', 2, 'C', 4, 5);
        board.moveD('C', 1, 'C', 3, 5);
        board.moveD('C', 2, 'C', 0, 2);
        board.moveD('C', 3, 'C', 1, 2);
        // push out 2 red marbles
        board.setField('C', 6, redMarbles[0]);
        board.setField('C', 5, redMarbles[1]);
        board.moveD('C', 4, 'C', 2, 2);
        board.moveD('C', 5, 'C', 3, 2);
        // test that the white marbles has won
        assertTrue(board.isWinner(Colour.WHITE));
        assertFalse(board.isWinner(Colour.BLACK));
        assertFalse(board.isWinner(Colour.RED));
    }

    @Test
    public void testHasWinner() {
        // test that the marble that has pushed off 6 marbles won
        // push out 4 black marbles
        board.setField('C', 2, whiteMarbles[0]);
        board.setField('C', 3, whiteMarbles[1]);
        board.setField('C', 4, whiteMarbles[2]);
        board.setField('C', 0, blackMarbles[0]);
        board.setField('C', 1, blackMarbles[1]);
        board.moveD('C', 2, 'C', 4, 5);
        board.moveD('C', 1, 'C', 3, 5);
        board.moveD('C', 2, 'C', 0, 2);
        board.moveD('C', 3, 'C', 1, 2);
        board.setField('C', 0, blackMarbles[2]);
        board.setField('C', 1, blackMarbles[3]);
        board.moveD('C', 2, 'C', 4, 5);
        board.moveD('C', 1, 'C', 3, 5);
        board.moveD('C', 2, 'C', 0, 2);
        board.moveD('C', 3, 'C', 1, 2);
        // push out 2 red marbles
        board.setField('C', 6, redMarbles[0]);
        board.setField('C', 5, redMarbles[1]);
        board.moveD('C', 4, 'C', 2, 2);
        board.moveD('C', 5, 'C', 3, 2);
        // test that the white marbles has won
        assertTrue(board.hasWinner());
    }
}