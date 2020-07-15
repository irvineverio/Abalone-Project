package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import abalone.BoardFor4;
import abalone.Colour;
import abalone.Marble;
import abalone.Move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A general method testing for the BoardFor4 class with its rules.
 * 
 * @author Irvine Verio
 *
 */
public class BoardFor4Test {
    private static final int TEST_MARBLES_COUNT = 12;
    private BoardFor4 board;
    private Marble[] blackMarbles;
    private Marble[] whiteMarbles;
    private Marble[] redMarbles;
    private Marble[] greenMarbles;

    /**
     * Initializing a before each method.
     */
    @BeforeEach
    public void setUp() {
        board = new BoardFor4(Colour.WHITE, Colour.RED, Colour.BLACK, Colour.GREEN);
        blackMarbles = new Marble[TEST_MARBLES_COUNT];
        whiteMarbles = new Marble[TEST_MARBLES_COUNT];
        redMarbles = new Marble[TEST_MARBLES_COUNT];
        greenMarbles = new Marble[TEST_MARBLES_COUNT];
        for (int i = 0; i < TEST_MARBLES_COUNT; i++) {
            blackMarbles[i] = new Marble(Colour.BLACK);
            whiteMarbles[i] = new Marble(Colour.WHITE);
            redMarbles[i] = new Marble(Colour.RED);
            greenMarbles[i] = new Marble(Colour.GREEN);
        }
    }

    @Test
    public void testSetUpMarbles() {
        System.out.println(board.toString());
    }

    @Test
    public void testMoveD1() {
        // in-line move 1 marble
        board.moveD('C', 4, 'C', 4, 0);
        assertTrue(board.getField('C', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        // side-step move
        board.moveD('D', 4, 'D', 4, 2);
        assertTrue(board.getField('D', 4).getMarble() == null);
        assertFalse(board.getField('D', 5).getMarble() == null);
    }

    @Test
    public void testMoveD2() {
        // in-line move 2 marbles
        // head move 2 marbles of the same colour
        board.moveD('C', 4, 'B', 4, 0);
        assertTrue(board.getField('B', 4).getMarble() == null);
        assertFalse(board.getField('C', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        // tail move 2 marbles of the same colour
        board.moveD('C', 4, 'D', 4, 0);
        assertTrue(board.getField('C', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        assertFalse(board.getField('E', 4).getMarble() == null);

        // move 2 marbles of different colour ->
        // can only move by moving its own marbles with the head is the color that can
        // move its way
        board.setField('F', 4, blackMarbles[0]);
        board.setField('F', 5, whiteMarbles[0]);
        board.moveD('F', 5, 'F', 4, 5);
        assertTrue(board.getField('F', 5).getMarble() == null);
        assertEquals(blackMarbles[0], board.getField('F', 3).getMarble());
        assertEquals(whiteMarbles[0], board.getField('F', 4).getMarble());
        assertFalse(board.getField('F', 3).getMarble() == null);
        assertFalse(board.getField('F', 4).getMarble() == null);

        board.moveD('F', 3, 'F', 4, 2);
        assertTrue(board.getField('F', 3).getMarble() == null);
        assertEquals(blackMarbles[0], board.getField('F', 4).getMarble());
        assertEquals(whiteMarbles[0], board.getField('F', 5).getMarble());
        assertFalse(board.getField('F', 4).getMarble() == null);
        assertFalse(board.getField('F', 5).getMarble() == null);

        // side-step move of the same colour
        board.moveD('D', 4, 'E', 4, 5);
        assertTrue(board.getField('D', 4).getMarble() == null);
        assertTrue(board.getField('E', 4).getMarble() == null);
        assertFalse(board.getField('D', 3).getMarble() == null);
        assertFalse(board.getField('E', 3).getMarble() == null);
        // side-step move of the different colour
        board.moveD('F', 5, 'F', 4, 5);
        board.moveD('F', 4, 'F', 3, 5);
        board.moveD('F', 3, 'F', 2, 0);
        assertTrue(board.getField('F', 2).getMarble() == null);
        assertTrue(board.getField('F', 3).getMarble() == null);
        assertFalse(board.getField('G', 2).getMarble() == null);
        assertFalse(board.getField('G', 3).getMarble() == null);
    }

    @Test
    public void testMoveD3() {
        // in-line move 3 marbles
        // head move 3 marbles of the same colour
        board.moveD('C', 4, 'A', 4, 0);
        assertTrue(board.getField('A', 4).getMarble() == null);
        assertFalse(board.getField('B', 4).getMarble() == null);
        assertFalse(board.getField('C', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        // tail move 3 marbles of the same colour
        board.moveD('B', 4, 'D', 4, 0);
        assertTrue(board.getField('B', 4).getMarble() == null);
        assertFalse(board.getField('C', 4).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        assertFalse(board.getField('E', 4).getMarble() == null);

        // move 3 marbles of different colour ->
        // can only move by moving its own marbles with the head is the color that can
        // move its way
        board.moveD('E', 4, 'E', 4, 2);
        board.setField('D', 5, blackMarbles[0]);
        board.setField('C', 5, blackMarbles[1]);
        board.moveD('E', 5, 'C', 5, 3);
        assertTrue(board.getField('E', 5).getMarble() == null);
        assertEquals(blackMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 5).getMarble());
        assertFalse(board.getField('D', 5).getMarble() == null);
        assertFalse(board.getField('C', 5).getMarble() == null);
        assertFalse(board.getField('B', 5).getMarble() == null);

        board.moveD('B', 5, 'D', 5, 0);
        assertTrue(board.getField('B', 5).getMarble() == null);
        assertEquals(blackMarbles[0], board.getField('D', 5).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 5).getMarble());
        assertFalse(board.getField('C', 5).getMarble() == null);
        assertFalse(board.getField('D', 5).getMarble() == null);
        assertFalse(board.getField('E', 5).getMarble() == null);

        // side-step move of the same colour
        board.moveD('E', 5, 'E', 5, 5);
        board.moveD('C', 4, 'E', 4, 0);
        board.moveD('D', 4, 'F', 4, 5);
        assertTrue(board.getField('D', 4).getMarble() == null);
        assertTrue(board.getField('E', 4).getMarble() == null);
        assertTrue(board.getField('F', 4).getMarble() == null);
        assertFalse(board.getField('D', 3).getMarble() == null);
        assertFalse(board.getField('E', 3).getMarble() == null);
        assertFalse(board.getField('F', 3).getMarble() == null);
        // side-step move of the different colour
        board.setField('D', 5, blackMarbles[2]);
        board.setField('E', 5, blackMarbles[3]);
        board.setField('F', 5, whiteMarbles[0]);
        board.moveD('D', 5, 'F', 5, 5);
        assertTrue(board.getField('D', 5).getMarble() == null);
        assertTrue(board.getField('E', 5).getMarble() == null);
        assertTrue(board.getField('F', 5).getMarble() == null);
        assertFalse(board.getField('D', 4).getMarble() == null);
        assertFalse(board.getField('E', 4).getMarble() == null);
        assertFalse(board.getField('F', 4).getMarble() == null);
    }

    @Test
    public void testMoveD2Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        board.moveD('D', 5, 'E', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        board.moveD('C', 5, 'D', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        board.moveD('E', 5, 'D', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        board.moveD('D', 5, 'C', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        board.reset();

        // push 1 marble until out with different colour from same team
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, blackMarbles[0]);
        board.setField('C', 5, redMarbles[0]);
        Move movecolourblack = new Move('E', 5, 'D', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack, Colour.BLACK));
        board.moveD('D', 5, 'E', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('D', 5).getMarble());
        Move movecolourblack1 = new Move('D', 5, 'C', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack1, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack1, Colour.BLACK));
        board.moveD('C', 5, 'D', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('C', 5).getMarble());
        board.reset();
    }

    @Test
    public void testMoveD3Sumito() {
        // push (sumito) move on opponent's marble
        // push 1 marble until out with head
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('F', 5, whiteMarbles[2]);
        board.setField('C', 5, redMarbles[0]);
        board.moveD('D', 5, 'F', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('E', 5).getMarble());
        board.moveD('C', 5, 'E', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('D', 5).getMarble());
        board.reset();

        // push 2 marbles with same colour until out with head
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('F', 5, whiteMarbles[2]);
        board.setField('C', 5, redMarbles[0]);
        board.setField('B', 5, greenMarbles[0]);
        board.moveD('D', 5, 'F', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('E', 5).getMarble());
        assertTrue(board.getOuterRim().contains(greenMarbles[0]));
        board.moveD('C', 5, 'E', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('D', 5).getMarble());
        board.reset();

        // push 1 marble until out with tail
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('F', 5, whiteMarbles[2]);
        board.setField('C', 5, redMarbles[0]);
        board.moveD('F', 5, 'D', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('E', 5).getMarble());
        board.moveD('E', 5, 'C', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('D', 5).getMarble());
        board.reset();
        // push 2 marbles with same colour until out with tail
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, whiteMarbles[1]);
        board.setField('F', 5, whiteMarbles[2]);
        board.setField('C', 5, redMarbles[0]);
        board.setField('B', 5, greenMarbles[0]);
        board.moveD('F', 5, 'D', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('D', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('E', 5).getMarble());
        assertTrue(board.getOuterRim().contains(greenMarbles[0]));
        board.moveD('E', 5, 'C', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(whiteMarbles[1], board.getField('C', 5).getMarble());
        assertEquals(whiteMarbles[2], board.getField('D', 5).getMarble());
        board.reset();

        // push 1 marble until out with different colour from same team
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, blackMarbles[0]);
        board.setField('F', 5, blackMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        Move movecolourblack = new Move('F', 5, 'D', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack, Colour.BLACK));
        board.moveD('D', 5, 'F', 5, 3);
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('D', 5).getMarble());
        assertEquals(blackMarbles[1], board.getField('E', 5).getMarble());
        Move movecolourblack1 = new Move('E', 5, 'C', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack1, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack1, Colour.BLACK));
        board.moveD('C', 5, 'E', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('C', 5).getMarble());
        board.reset();
        // push 2 marbles until out with different colour from same team
        board.setField('D', 5, whiteMarbles[0]);
        board.setField('E', 5, blackMarbles[0]);
        board.setField('F', 5, blackMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        board.setField('B', 5, greenMarbles[0]);
        Move movecolourblack2 = new Move('F', 5, 'D', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack2, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack2, Colour.BLACK));
        board.moveD('D', 5, 'F', 5, 3);
        assertTrue(board.getOuterRim().contains(greenMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('C', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('D', 5).getMarble());
        assertEquals(blackMarbles[1], board.getField('E', 5).getMarble());
        Move movecolourblack3 = new Move('E', 5, 'C', 5, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack3, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblack3, Colour.BLACK));
        board.moveD('C', 5, 'E', 5, 3);
        assertTrue(board.getOuterRim().contains(redMarbles[0]));
        assertEquals(whiteMarbles[0], board.getField('B', 5).getMarble());
        assertEquals(blackMarbles[0], board.getField('C', 5).getMarble());
        board.reset();
    }

    @Test
    public void testIsValidMoveD1() {
        // move its own marble to a correct position
        assertTrue(board.isValidMoveD('C', 4, 'C', 4, 0));
        assertTrue(board.isValidMoveD('C', 4, 'C', 4, 1));
        // move its own marble to a wrong position
        assertFalse(board.isValidMoveD('A', 4, 'A', 4, 0));
        assertFalse(board.isValidMoveD('A', 4, 'A', 4, 4));

        // invalid move
        // moving teammate marble to another position -> ask how to check this
        Move movecolourwhite = new Move('C', 3, 'C', 3, 0);
        assertTrue(board.isValidMoveDForColour(movecolourwhite, Colour.WHITE));
        assertFalse(board.isValidMoveDForColour(movecolourwhite, Colour.BLACK));
    }

    @Test
    public void testIsValidMoveD2() {
        // in-line move
        // move 2 marbles to a correct position with head
        assertTrue(board.isValidMoveD('C', 3, 'B', 3, 0));
        assertTrue(board.isValidMoveD('C', 3, 'B', 2, 1));
        // move 2 marbles to a wrong position with head
        assertFalse(board.isValidMoveD('C', 3, 'B', 3, 4));
        assertFalse(board.isValidMoveD('C', 3, 'B', 2, 2));
        // push 1 marble with head of 1 colour
        board.setField('H', 3, redMarbles[0]);
        board.setField('G', 3, blackMarbles[0]);
        board.setField('F', 3, blackMarbles[1]);
        assertTrue(board.isValidMoveD('G', 3, 'F', 3, 0));
        // push 1 marble out with head of 1 colour
        board.setField('I', 8, redMarbles[1]);
        assertTrue(board.isValidMoveD('I', 7, 'I', 6, 2));
        // move 2 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException

        // move 2 marbles to a correct position with tail
        assertTrue(board.isValidMoveD('B', 3, 'C', 3, 0));
        assertTrue(board.isValidMoveD('B', 2, 'C', 3, 1));
        // move 2 marbles to a wrong position with tail
        assertFalse(board.isValidMoveD('B', 3, 'C', 3, 4));
        assertFalse(board.isValidMoveD('B', 2, 'C', 3, 2));
        // push 1 marble with tail
        assertTrue(board.isValidMoveD('F', 3, 'G', 3, 0));
        // push 1 marble out with tail
        assertTrue(board.isValidMoveD('I', 7, 'I', 6, 2));
        // move 2 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException

        // side-step move
        board.moveD('C', 4, 'B', 4, 0);
        board.moveD('D', 4, 'C', 4, 0);
        // move 2 marbles to a correct position
        assertTrue(board.isValidMoveD('E', 4, 'D', 4, 2));
        assertTrue(board.isValidMoveD('E', 4, 'D', 4, 5));
        // move 2 marbles to a wrong position
        assertFalse(board.isValidMoveD('E', 4, 'D', 4, 4));
        board.moveD('E', 4, 'D', 4, 2);
        assertFalse(board.isValidMoveD('E', 5, 'D', 5, 1));
        board.reset();

        // move 2 marbles of different colour ->
        // can only move by moving its own marbles with the head is the color that can
        // move its way
        board.moveD('C', 4, 'C', 4, 0);
        board.setField('E', 4, blackMarbles[2]);
        Move movecolourwhite = new Move('D', 4, 'E', 4, 0);
        Move movecolourblack = new Move('E', 4, 'D', 4, 3);
        // test for colour white
        assertTrue(board.isValidMoveDForColour(movecolourwhite, Colour.WHITE));
        assertFalse(board.isValidMoveDForColour(movecolourblack, Colour.WHITE));
        // test for colour black
        assertTrue(board.isValidMoveDForColour(movecolourblack, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwhite, Colour.BLACK));

        // push 1 marble with different colour
        board.moveD('E', 4, 'D', 4, 2);
        board.setField('C', 5, redMarbles[0]);
        Move movecolourblack1 = new Move('E', 5, 'D', 5, 3);
        assertTrue(board.isValidMoveD('E', 5, 'D', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblack1, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblack1, Colour.WHITE));
        board.moveD('E', 5, 'D', 5, 3);

        // push 1 marble out with different colour
        Move movecolourblack2 = new Move('D', 5, 'C', 5, 3);
        assertTrue(board.isValidMoveD('D', 5, 'C', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblack2, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblack2, Colour.WHITE));
        board.reset();

        // invalid moves
        // move teammate marble to another position
        Move movecolourblack3 = new Move('G', 4, 'H', 4, 3);
        assertTrue(board.isValidMoveDForColour(movecolourblack3, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblack3, Colour.WHITE));
        // move its own and teammate marble to a wrong position
        board.moveD('C', 4, 'C', 4, 0);
        board.setField('E', 4, blackMarbles[3]);
        board.moveD('D', 4, 'E', 4, 2);
        Move movecolourwrong = new Move('D', 5, 'E', 5, 1);
        assertFalse(board.isValidMoveD('D', 5, 'E', 5, 1));
        assertFalse(board.isValidMoveDForColour(movecolourwrong, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwrong, Colour.WHITE));
        Move movecolourwrong1 = new Move('D', 5, 'E', 5, 2);
        assertFalse(board.isValidMoveD('D', 5, 'E', 5, 2));
        assertFalse(board.isValidMoveDForColour(movecolourwrong1, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwrong1, Colour.WHITE));
        // moving a teammate marble that is stuck in the middle of the same colour
        board.setField('F', 5, blackMarbles[6]);
        board.setField('G', 6, whiteMarbles[3]);
        board.setField('H', 7, blackMarbles[7]);
        assertTrue(board.isValidMoveD('F', 5, 'H', 7, 1));
        // moving an enemy marble that is stuck in the middle of the same colour
        board.setField('D', 6, redMarbles[1]);
        board.setField('D', 7, whiteMarbles[1]);
        board.setField('D', 4, blackMarbles[4]);
        Move moveblackstuck = new Move('D', 4, 'D', 5, 2);
        assertFalse(board.isValidMoveDForColour(moveblackstuck, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(moveblackstuck, Colour.WHITE));
        // push 2 marbles -> invalid
        board.setField('F', 3, redMarbles[2]);
        board.setField('G', 3, greenMarbles[0]);
        board.setField('E', 3, whiteMarbles[2]);
        board.setField('D', 3, blackMarbles[5]);
        Move moveinvalidblack = new Move('D', 3, 'E', 3, 0);
        assertFalse(board.isValidMoveD('D', 3, 'E', 3, 0));
        assertFalse(board.isValidMoveDForColour(moveinvalidblack, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(moveinvalidblack, Colour.WHITE));
        // push 3 marbles -> invalid
        board.setField('H', 3, redMarbles[3]);
        Move moveinvalidblack2 = new Move('D', 3, 'E', 3, 0);
        assertFalse(board.isValidMoveD('D', 3, 'E', 3, 0));
        assertFalse(board.isValidMoveDForColour(moveinvalidblack2, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(moveinvalidblack2, Colour.WHITE));
        // push a teammate marble -> invalid
        board.setField('D', 3, whiteMarbles[3]);
        board.setField('C', 2, whiteMarbles[4]);
        board.setField('B', 1, whiteMarbles[5]);
        board.setField('A', 0, blackMarbles[8]);
        assertFalse(board.isValidMoveD('D', 3, 'B', 1, 4));
        // push with opponent marble
        board.setField('C', 2, redMarbles[4]);
        assertFalse(board.isValidMoveD('D', 3, 'B', 1, 4));
        // invalid: moving non-existence marble
        board.reset();
        assertFalse(board.isValidMoveD('H', 0, 'H', 1, 0));
        // invalid: moving non-existence marble with isValidMove()
        assertFalse(board.isValidMove('I', 8, 'I', 7, 'I', 6));
    }

    @Test
    public void testIsValidMoveD3() {
        // in-line move
        // move 3 marbles to a correct position with head
        assertTrue(board.isValidMoveD('C', 3, 'A', 3, 0));
        assertTrue(board.isValidMoveD('C', 3, 'A', 1, 1));
        // move 3 marbles to a wrong position with head
        assertFalse(board.isValidMoveD('C', 3, 'A', 3, 4));
        assertFalse(board.isValidMoveD('C', 3, 'A', 2, 2));
        // push 1 marble with head of 1 colour
        board.setField('G', 3, redMarbles[0]);
        board.setField('F', 3, blackMarbles[0]);
        board.setField('E', 3, blackMarbles[1]);
        board.setField('D', 3, blackMarbles[2]);
        assertTrue(board.isValidMoveD('F', 3, 'D', 3, 0));
        // push 1 marble out with head of 1 colour
        board.setField('I', 8, redMarbles[1]);
        assertTrue(board.isValidMoveD('I', 7, 'I', 5, 2));
        // push 2 marbles same colours with head
        board.setField('H', 3, redMarbles[1]);
        assertTrue(board.isValidMoveD('F', 3, 'D', 3, 0));
        // push 2 marbles same colours out with head
        assertTrue(board.isValidMoveD('F', 3, 'D', 3, 0));
        // move 3 marbles that are invalid with head
        // assertFalse(board.isValidMoveD('G', 4, 'H', '6', 4)); -> will throw
        // IndexOutOfBoundException

        // move 3 marbles to a correct position with tail
        assertFalse(board.isValidMoveD('A', 3, 'C', 3, 0));
        assertTrue(board.isValidMoveD('A', 2, 'C', 4, 1));
        // move 3 marbles to a wrong position with tail
        assertFalse(board.isValidMoveD('A', 3, 'C', 3, 4));
        assertFalse(board.isValidMoveD('A', 2, 'C', 3, 2));
        // push 1 marble with tail of 1 colour
        assertTrue(board.isValidMoveD('D', 3, 'F', 3, 0));
        // push 1 marble out with tail of 1 colour
        assertTrue(board.isValidMoveD('I', 5, 'I', 7, 2));
        // push 2 marbles same colours with tail
        assertTrue(board.isValidMoveD('D', 3, 'F', 3, 0));
        // push 2 marbles same colours out with tail
        assertTrue(board.isValidMoveD('D', 3, 'F', 3, 0));
        board.reset();
        // move 3 marbles that are invalid with tail
        // assertFalse(board.isValidMoveD('H', '6', 'G', 4, 4)); -> will throw
        // IndexOutOfBoundException

        // side-step move
        board.moveD('C', 4, 'A', 4, 0);
        board.moveD('D', 4, 'B', 4, 0);
        board.moveD('E', 4, 'C', 4, 0);
        // move 3 marbles to a correct position
        assertTrue(board.isValidMoveD('F', 4, 'D', 4, 2));
        assertTrue(board.isValidMoveD('F', 4, 'D', 4, 5));
        // move 3 marbles to a wrong position
        assertFalse(board.isValidMoveD('E', 4, 'D', 4, 4));
        board.moveD('F', 4, 'D', 4, 2);
        assertFalse(board.isValidMoveD('F', 5, 'D', 5, 1));
        board.reset();

        // move 3 marbles of different colour ->
        // can only move by moving its own marbles with the head is the color that can
        // move its way
        board.moveD('C', 4, 'C', 4, 0);
        board.setField('E', 4, blackMarbles[2]);
        board.setField('F', 4, blackMarbles[3]);
        Move movecolourwhite = new Move('D', 4, 'F', 4, 0);
        Move movecolourwhitetail = new Move('F', 4, 'D', 4, 0);
        board.setField('G', 4, null);
        // test for colour white
        assertTrue(board.isValidMoveDForColour(movecolourwhite, Colour.WHITE));
        assertFalse(board.isValidMoveDForColour(movecolourwhitetail, Colour.WHITE));
        Move movecolourblack = new Move('F', 4, 'D', 4, 3);
        Move movecolourblacktail = new Move('D', 4, 'F', 4, 3);
        assertFalse(board.isValidMoveDForColour(movecolourblack, Colour.WHITE));
        assertTrue(board.isValidMoveDForColour(movecolourblacktail, Colour.WHITE));
        // test for colour black
        assertTrue(board.isValidMoveDForColour(movecolourblack, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwhite, Colour.BLACK));
        assertTrue(board.isValidMoveDForColour(movecolourwhitetail, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblacktail, Colour.BLACK));
        // push 1 marble with different colour
        board.moveD('F', 4, 'D', 4, 2);
        board.setField('C', 5, redMarbles[0]);
        Move movecolourblackpush = new Move('F', 5, 'D', 5, 3);
        assertTrue(board.isValidMoveD('F', 5, 'D', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblackpush, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblackpush, Colour.WHITE));
        board.moveD('F', 5, 'D', 5, 3);
        // push 1 marble out with different colour
        Move movecolourblackpush1 = new Move('E', 5, 'C', 5, 3);
        assertTrue(board.isValidMoveD('E', 5, 'C', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblackpush1, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblackpush1, Colour.WHITE));
        board.reset();

        // push 2 marbles with different colour
        board.moveD('C', 4, 'C', 4, 0);
        board.setField('E', 4, blackMarbles[4]);
        board.setField('F', 4, blackMarbles[5]);
        board.moveD('F', 4, 'D', 4, 2);
        board.setField('C', 5, redMarbles[1]);
        board.setField('B', 5, greenMarbles[0]);
        Move movecolourblackpush2 = new Move('F', 5, 'D', 5, 3);
        assertTrue(board.isValidMoveD('F', 5, 'D', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblackpush2, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblackpush2, Colour.WHITE));
        board.moveD('F', 5, 'D', 5, 3);
        // push 2 marbles out with different colour
        Move movecolourblackpush3 = new Move('E', 5, 'C', 5, 3);
        assertTrue(board.isValidMoveD('E', 5, 'C', 5, 3));
        assertTrue(board.isValidMoveDForColour(movecolourblackpush3, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblackpush3, Colour.WHITE));
        board.reset();

        // invalid moves
        // move teammate marble to another position
        Move movecolourblack1 = new Move('G', 4, 'I', 4, 3);
        assertTrue(board.isValidMoveDForColour(movecolourblack1, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourblack1, Colour.WHITE));
        // move its own and teammate marble to a wrong position
        board.moveD('C', 4, 'C', 4, 0);
        board.setField('E', 4, blackMarbles[6]);
        board.setField('F', 4, blackMarbles[7]);
        board.moveD('D', 4, 'F', 4, 2);
        Move movecolourwrong = new Move('D', 5, 'F', 5, 1);
        assertFalse(board.isValidMoveD('D', 5, 'F', 5, 1));
        assertFalse(board.isValidMoveDForColour(movecolourwrong, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwrong, Colour.WHITE));
        Move movecolourwrong1 = new Move('D', 5, 'F', 5, 2);
        assertFalse(board.isValidMoveD('D', 5, 'F', 5, 2));
        assertFalse(board.isValidMoveDForColour(movecolourwrong1, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(movecolourwrong1, Colour.WHITE));
        // moving a teammate marble that is stuck in the middle of the same colour ->
        // ask first
        // board.setField('C', 2, blackMarbles[2]);
        // board.setField('D', 2, whiteMarbles[2]);
        // assertFalse(board.isValidMoveD('B', 2, 'A', 2, 0));
        // moving an enemy marble that is stuck in the middle of the same colour
        board.setField('D', 6, redMarbles[2]);
        board.setField('D', 7, whiteMarbles[0]);
        board.setField('D', 4, blackMarbles[8]);
        board.setField('D', 3, blackMarbles[9]);
        Move moveblackstuck = new Move('D', 3, 'D', 5, 2);
        assertFalse(board.isValidMoveDForColour(moveblackstuck, Colour.BLACK));
        assertFalse(board.isValidMoveDForColour(moveblackstuck, Colour.WHITE));
        // push 3 marbles -> invalid
        board.setField('F', 3, redMarbles[3]);
        board.setField('H', 3, redMarbles[4]);
        board.setField('G', 3, greenMarbles[1]);
        board.setField('E', 3, whiteMarbles[2]);
        Move moveinvalidwhite = new Move('C', 3, 'E', 3, 0);
        assertFalse(board.isValidMoveD('C', 3, 'E', 3, 0));
        assertFalse(board.isValidMoveDForColour(moveinvalidwhite, Colour.WHITE));
        assertFalse(board.isValidMoveDForColour(moveinvalidwhite, Colour.BLACK));
        // invalid: moving non-existence marble with isValidMove()
        assertFalse(board.isValidMove('I', 8, 'I', 7, 'I', 6, 'I', 5));
    }

    @Test
    public void testIsWinner() {
        // initializing marbles
        board.setField('F', 5, whiteMarbles[0]);
        board.setField('E', 5, blackMarbles[0]);
        board.setField('D', 5, blackMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        board.setField('B', 5, greenMarbles[4]);

        // test that the marble team that has pushed off 6 marbles won
        // team marbles push out 3 red marbles
        // team marbles push out 3 green marbles
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // moving white and black marbles back again so it can push again
        board.moveD('B', 5, 'D', 5, 0);
        board.moveD('C', 5, 'E', 5, 0);
        board.setField('C', 5, redMarbles[1]);
        board.setField('B', 5, greenMarbles[1]);
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // moving white and black marbles back again so it can push again
        board.moveD('B', 5, 'D', 5, 0);
        board.moveD('C', 5, 'E', 5, 0);
        board.setField('C', 5, redMarbles[2]);
        board.setField('B', 5, greenMarbles[2]);
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // test that the white and black team has won
        assertTrue(board.isWinner(Colour.WHITE) && board.isWinner(Colour.BLACK));
        assertFalse(board.isWinner(Colour.RED) && board.isWinner(Colour.GREEN));
    }

    @Test
    public void testHasWinner() {
        // initializing marbles
        board.setField('F', 5, whiteMarbles[0]);
        board.setField('E', 5, blackMarbles[0]);
        board.setField('D', 5, blackMarbles[1]);
        board.setField('C', 5, redMarbles[0]);
        board.setField('B', 5, greenMarbles[0]);

        // test that the marble team that has pushed off 6 marbles won
        // team marbles push out 3 red marbles
        // team marbles push out 3 green marbles
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // moving white and black marbles back again so it can push again
        board.moveD('B', 5, 'D', 5, 0);
        board.moveD('C', 5, 'E', 5, 0);
        board.setField('C', 5, redMarbles[1]);
        board.setField('B', 5, greenMarbles[1]);
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // moving white and black marbles back again so it can push again
        board.moveD('B', 5, 'D', 5, 0);
        board.moveD('C', 5, 'E', 5, 0);
        board.setField('C', 5, redMarbles[2]);
        board.setField('B', 5, greenMarbles[2]);
        // pushing 1 marble red and 1 marble green out
        board.moveD('F', 5, 'D', 5, 3);
        board.moveD('E', 5, 'C', 5, 3);
        // test that the white and black team has won
        board.hasWinner();
    }
}