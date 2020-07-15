package test;

import static abalone.Board.DIM;
import static abalone.Board.SIDE_LENGTH;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import abalone.Board;
import abalone.Colour;
import abalone.Field;
import abalone.Marble;
import abalone.Move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A general method testing for the Board class.
 * 
 * @author Irvine Verio
 *
 */
public class BoardTest {
    private static final int TEST_MARBLES_COUNT = 7;
    private Board board;
    private Marble[] blackMarbles;
    private Marble[] whiteMarbles;

    /**
     * Initializing before each tests.
     */
    @BeforeEach
    public void setUp() {
        board = new Board();
        blackMarbles = new Marble[TEST_MARBLES_COUNT];
        whiteMarbles = new Marble[TEST_MARBLES_COUNT];
        for (int i = 0; i < TEST_MARBLES_COUNT; i++) {
            blackMarbles[i] = new Marble(Colour.BLACK);
            whiteMarbles[i] = new Marble(Colour.WHITE);
        }
    }

    @Test
    public void testSetUp() {
        board.init();
    }

    @Test
    public void testToString() {
        // printOut(board.toString());
    }

    @Test
    public void testBoardDeepCopy() {
        board.setField('A', 0, blackMarbles[0]);
        Board deepCopyBoard = board.deepCopy(); // gives null pointer exception
        deepCopyBoard.setField('A', 0, whiteMarbles[0]);
        deepCopyBoard.setField('B', 1, blackMarbles[0]);
        deepCopyBoard.setField('C', 2, blackMarbles[1]);

        // testing the board itself
        assertEquals(blackMarbles[0], board.getField('A', 0).getMarble());

        // testing the board deep copy
        assertEquals(whiteMarbles[0], deepCopyBoard.getField('A', 0).getMarble());

        // testing the board deep copy black marbles pushes out white marbles
        deepCopyBoard.moveD('B', 1, 'C', 2, 4);
        assertTrue(deepCopyBoard.getOuterRim().contains(whiteMarbles[0]));
        assertEquals(blackMarbles[1], deepCopyBoard.getField('B', 1).getMarble());
        assertEquals(blackMarbles[0], deepCopyBoard.getField('A', 0).getMarble());
    }

    @Test
    public void testLetterToRow() {
        assertEquals(0, board.letterToRow('a'));
        assertEquals(0, board.letterToRow('A'));
        assertEquals(DIM - 1, board.letterToRow('i'));
        assertEquals(DIM - 1, board.letterToRow('I'));
    }

    @Test
    public void testNumberField() {
        int nsSpace = 0;
        for (int i = 0; i < (SIDE_LENGTH - 1); i++) {
            nsSpace += SIDE_LENGTH + i;
        }
        nsSpace *= 2;
        nsSpace += DIM;
        assertEquals(nsSpace, board.numberOfFields());
    }

    @Test
    public void testGetAndSetField() {
        board.setField('A', 0, (blackMarbles[0]));
        assertEquals(Colour.BLACK, board.getField('A', 0).getMarble().getColour());
        board.setField('B', 0, (blackMarbles[1]));
        assertEquals(Colour.BLACK, board.getField('B', 0).getMarble().getColour());
        board.setField('G', 8, (blackMarbles[2]));
        assertEquals(Colour.BLACK, board.getField('G', 8).getMarble().getColour());
        board.setField('G', 8, (blackMarbles[3]));
        assertEquals(Colour.BLACK, board.getField('G', 8).getMarble().getColour());
        printOut(board.toString());
    }

    @Test
    public void testCoordGetAndSetField() {
        board.setField('A', 0, (blackMarbles[0]));
        assertEquals(Colour.BLACK, board.getField('A', 0).getMarble().getColour());
        board.setField('A', 0, whiteMarbles[0]);
        assertEquals(Colour.WHITE, board.getField('A', 0).getMarble().getColour());
        board.setField('I', 4, blackMarbles[1]);
        assertEquals(Colour.BLACK, board.getField('I', 4).getMarble().getColour());
    }

    @Test
    public void testFieldRowAndCol() {
        assertEquals('A', board.getField('A', 0).getRow());
        assertEquals(0, board.getField('A', 0).getCol());
    }

    // is valid w index should be private

    @Test
    public void testIsFieldCoord() {
        assertTrue(board.isFieldCoord('a', 0));
        assertTrue(board.isFieldCoord('e', 0));
        assertTrue(board.isFieldCoord('f', 1));
        assertTrue(board.isFieldCoord('g', 2));
        assertTrue(board.isFieldCoord('g', 3));
        assertTrue(board.isFieldCoord('h', 3));
        assertTrue(board.isFieldCoord('i', 4));
        assertFalse(board.isFieldCoord('i', 0));
    }

    @Test
    public void testGetNeighbours() {
        Field[] neighbours = board.getNeighbors('A', 0);
        assertEquals(board.getField('B', 0), neighbours[0]);
        assertEquals(board.getField('B', 1), neighbours[1]);
        assertEquals(board.getField('A', 1), neighbours[2]);
        assertEquals(null, neighbours[3]);
        assertEquals(null, neighbours[4]);
        assertEquals(null, neighbours[5]);
        Field[] neighbours2 = board.getNeighbors('G', 4);
        // //printOut(neighbours2);
        assertEquals(board.getField('H', 4), neighbours2[0]);
        assertEquals(board.getField('H', 5), neighbours2[1]);
        assertEquals(board.getField('G', 5), neighbours2[2]);
        assertEquals(board.getField('F', 4), neighbours2[3]);
        assertEquals(board.getField('F', 3), neighbours2[4]);
        assertEquals(board.getField('G', 3), neighbours2[5]);

        Field[] neighbours3 = board.getNeighbors('F', 4);
        // //printOut(neighbours2);
        assertEquals(board.getField('G', 4), neighbours3[0]);
        assertEquals(board.getField('G', 5), neighbours3[1]);
        assertEquals(board.getField('F', 5), neighbours3[2]);
        assertEquals(board.getField('E', 4), neighbours3[3]);
        assertEquals(board.getField('E', 3), neighbours3[4]);
        assertEquals(board.getField('F', 3), neighbours3[5]);

    }

    @Test
    public void testIsColumn2() {
        assertTrue(board.isColumn('A', 0, 'B', 0));
        assertTrue(board.isColumn('B', 0, 'A', 0));
        assertTrue(board.isColumn('E', 0, 'F', 1));
        assertTrue(board.isColumn('F', 1, 'E', 0));
    }

    @Test
    public void testIsColumn3() {
        assertTrue(board.isColumn('A', 0, 'B', 0, 'C', 0));
        assertTrue(board.isColumn('C', 0, 'B', 0, 'A', 0));
        assertFalse(board.isColumn('A', 0, 'B', 0, 'D', 0));
        assertTrue(board.isColumn('E', 0, 'F', 1, 'G', 2));
        assertTrue(board.isColumn('G', 2, 'F', 1, 'E', 0));
    }

    @Test
    public void testMove1() {
        // initialize marble
        board.setField('A', 0, blackMarbles[0]);
        // in-line move
        board.move('A', 0, 'A', 1);
        assertTrue(board.getField('A', 0).getMarble() == null);
        assertEquals(board.getField('A', 1).getMarble(), blackMarbles[0]);
        // side-step move
        board.move('A', 1, 'A', 1, 'B', 1);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
    }

    @Test
    public void testMove2() {
        // initialize marble
        board.setField('A', 0, blackMarbles[1]);
        board.setField('B', 1, blackMarbles[0]);
        printOut(board.toString());
        // in-line move
        // interchangeable move for tail
        board.move('A', 0, 'B', 1, 'C', 2);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 1).getMarble());
        assertTrue(board.getField('A', 0).getMarble() == null);
        // interchangeable move for head
        board.move('C', 2, 'B', 1, 'D', 3);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('D', 3).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 2).getMarble());
        assertTrue(board.getField('B', 1).getMarble() == null);
        // side-step move
        board.move('C', 2, 'D', 3, 'B', 2);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('C', 3).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 2).getMarble());
    }

    @Test
    public void testMove2Sumito() {
        // push(sumito) move
        // initialize marbles
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        Marble pushedMarble = whiteMarbles[0];
        board.setField('C', 2, marble2);
        board.setField('B', 1, marble1);
        board.setField('A', 0, pushedMarble);
        printOut(board.toString());
        // sumito move interchangeable for head
        board.move('B', 1, 'C', 2, 'A', 0);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble));
        assertEquals(marble2, board.getField('B', 1).getMarble());
        assertEquals(marble1, board.getField('A', 0).getMarble());
        // sumito move interchangeable for tail
        board.move('A', 0, 'B', 1, 'C', 2);
        Marble pushedMarble2 = whiteMarbles[1];
        board.setField('A', 0, pushedMarble2);
        board.move('C', 2, 'B', 1, 'A', 0);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble2));
        assertEquals(marble2, board.getField('B', 1).getMarble());
        assertEquals(marble1, board.getField('A', 0).getMarble());
    }

    @Test
    public void testMove3() {
        // initialize marbles
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        Marble marble3 = blackMarbles[2];
        board.setField('D', 3, marble1);
        board.setField('C', 2, marble2);
        board.setField('B', 1, marble3);
        printOut(board.toString());
        // in-line move
        // interchangeable move for head
        board.move('D', 3, 'C', 2, 'B', 1, 'E', 4);
        printOut(board.toString());
        assertEquals(marble1, board.getField('E', 4).getMarble());
        assertEquals(marble2, board.getField('D', 3).getMarble());
        assertEquals(marble3, board.getField('C', 2).getMarble());
        // interchangeable move for tail
        board.move('C', 2, 'D', 3, 'E', 4, 'F', 5); // still cannot be executed since from tail
        printOut(board.toString());
        assertEquals(marble1, board.getField('F', 5).getMarble());
        assertEquals(marble2, board.getField('E', 4).getMarble());
        assertEquals(marble3, board.getField('D', 3).getMarble());
        // side step move
        board.move('F', 5, 'E', 4, 'D', 3, 'G', 5);
        printOut(board.toString());
        assertEquals(marble1, board.getField('G', 5).getMarble());
        assertEquals(marble2, board.getField('F', 4).getMarble());
        assertEquals(marble3, board.getField('E', 3).getMarble());
    }

    @Test
    public void testMove3Sumito() {
        // initializing marbles
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        Marble marble3 = blackMarbles[2];
        Marble outedMarble = whiteMarbles[0];
        board.setField('D', 3, marble3);
        board.setField('C', 2, marble2);
        board.setField('B', 1, marble1);
        board.setField('A', 0, outedMarble);
        printOut(board.toString());
        // pushing with head
        // push out opponent's marble
        board.move('B', 1, 'C', 2, 'D', 3, 'A', 0);
        assertTrue(board.getOuterRim().contains(outedMarble));
        assertEquals(marble1, board.getField('A', 0).getMarble());
        assertEquals(marble2, board.getField('B', 1).getMarble());
        assertEquals(marble3, board.getField('C', 2).getMarble());
        printOut(board.toString());
        // pushing opponent's 1 marble
        Marble pushedMarble1 = whiteMarbles[1];
        board.setField('D', 3, pushedMarble1);
        board.move('C', 2, 'B', 1, 'A', 0, 'D', 3);
        printOut(board.toString());
        assertEquals(pushedMarble1, board.getField('E', 4).getMarble());
        assertEquals(marble3, board.getField('D', 3).getMarble());
        assertEquals(marble2, board.getField('C', 2).getMarble());
        assertEquals(marble1, board.getField('B', 1).getMarble());
        // pushing opponent's 2 marbles
        Marble pushedMarble2 = whiteMarbles[2];
        board.setField('F', 5, pushedMarble2);
        printOut(board.toString());
        board.move('D', 3, 'C', 2, 'B', 1, 'E', 4);
        printOut(board.toString());
        assertEquals(pushedMarble2, board.getField('G', 6).getMarble());
        assertEquals(pushedMarble1, board.getField('F', 5).getMarble());
        assertEquals(marble3, board.getField('E', 4).getMarble());
        assertEquals(marble2, board.getField('D', 3).getMarble());
        assertEquals(marble1, board.getField('C', 2).getMarble());
        // pushing until 2 opponent's marbles go out
        board.move('E', 4, 'D', 3, 'C', 2, 'F', 5);
        printOut(board.toString());
        board.move('F', 5, 'E', 4, 'D', 3, 'G', 6);
        printOut(board.toString());
        board.move('G', 6, 'F', 5, 'E', 4, 'H', 7);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble2));
        board.move('H', 7, 'G', 6, 'F', 5, 'I', 8);
        assertTrue(board.getOuterRim().contains(pushedMarble1));
        printOut(board.toString());
        board.reset();

        // pushing with tail
        // initializing marbles
        Marble marble4 = blackMarbles[3];
        Marble marble5 = blackMarbles[4];
        Marble marble6 = blackMarbles[5];
        Marble pushedMarble3 = whiteMarbles[3];
        board.setField('D', 3, marble6);
        board.setField('C', 2, marble5);
        board.setField('B', 1, marble4);
        board.setField('A', 0, pushedMarble3);
        printOut(board.toString());

        // push out opponent's marble
        board.move('D', 3, 'C', 2, 'B', 1, 'A', 0);
        assertTrue(board.getOuterRim().contains(pushedMarble3));
        assertEquals(marble4, board.getField('A', 0).getMarble());
        assertEquals(marble5, board.getField('B', 1).getMarble());
        assertEquals(marble6, board.getField('C', 2).getMarble());
        printOut(board.toString());
        // pushing opponent's 1 marble
        Marble pushedMarble4 = whiteMarbles[4];
        board.setField('D', 3, pushedMarble4);
        board.move('A', 0, 'B', 1, 'C', 2, 'D', 3);
        printOut(board.toString());
        assertEquals(pushedMarble4, board.getField('E', 4).getMarble());
        assertEquals(marble6, board.getField('D', 3).getMarble());
        assertEquals(marble5, board.getField('C', 2).getMarble());
        assertEquals(marble4, board.getField('B', 1).getMarble());
        // pushing opponent's 2 marbles
        Marble pushedMarble5 = whiteMarbles[5];
        board.setField('F', 5, pushedMarble5);
        printOut(board.toString());
        board.move('D', 3, 'C', 2, 'B', 1, 'E', 4);
        printOut(board.toString());
        assertEquals(pushedMarble5, board.getField('G', 6).getMarble());
        assertEquals(pushedMarble4, board.getField('F', 5).getMarble());
        assertEquals(marble6, board.getField('E', 4).getMarble());
        assertEquals(marble5, board.getField('D', 3).getMarble());
        assertEquals(marble4, board.getField('C', 2).getMarble());
        // pushing until 2 opponent's marbles go out
        board.move('C', 2, 'D', 3, 'E', 4, 'F', 5);
        printOut(board.toString());
        board.move('D', 3, 'E', 4, 'F', 5, 'G', 6);
        printOut(board.toString());
        board.move('E', 4, 'F', 5, 'G', 6, 'H', 7);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble5));
        board.move('F', 5, 'G', 6, 'H', 7, 'I', 8);
        assertTrue(board.getOuterRim().contains(pushedMarble4));
        printOut(board.toString());

        // assert
    }

    @Test
    public void testIsValidMove1() {
        // not valid move
        assertFalse(board.isValidMove('A', 0, 'A', -1));
        assertFalse(board.isValidMove('A', 1, 'A', 0));

        // valid move
        board.setField('B', 1, blackMarbles[0]);
        assertTrue(board.isValidMove('B', 1, 'A', 1));

        // not valid to push 1 on 1
        board.setField('A', 1, blackMarbles[0]);
        assertFalse(board.isValidMove('B', 1, 'A', 1));
    }

    @Test
    public void testIsValidMove2() {
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        board.setField('B', 1, marble1);
        board.setField('C', 2, marble2);
        assertTrue(board.isValidMove('B', 1, 'C', 2, 'A', 0)); // interchangeable in-line head
        assertTrue(board.isValidMove('B', 1, 'C', 2, 'D', 3)); // interchangeable in-line tail

        Marble pushMarble = new Marble(Colour.WHITE);
        board.setField('A', 0, pushMarble);
        assertTrue(board.isValidMove('B', 1, 'C', 2, 'A', 0)); // push 1 marble with head
        assertTrue(board.isValidMove('C', 2, 'B', 1, 'A', 0)); // push 1 marble with tail

        Marble opMarble = new Marble(Colour.WHITE);
        board.setField('D', 3, opMarble);
        printOut(board.toString());
        assertTrue(board.isValidMove('C', 2, 'B', 1, 'D', 3)); // push 1 marble
        board.setField('E', 4, opMarble);
        printOut(board.toString());
        assertFalse(board.isValidMove('C', 2, 'B', 1, 'D', 3)); // invalid: push 2 marbles
        // side step moves
        assertTrue(board.isValidMove('C', 2, 'B', 1, 'C', 1)); // side step left
        assertTrue(board.isValidMove('C', 2, 'B', 1, 'C', 3)); // side step right
        board.move('C', 2, 'B', 1, 'C', 3);
        printOut(board.toString());
        assertFalse(board.isValidMove('C', 3, 'B', 2, 'D', 3)); // invalid: side step push
        assertTrue(board.isValidMove('C', 3, 'B', 2, 'B', 3)); // side-step bottom right
        board.setField('B', 3, new Marble(Colour.WHITE));
        assertFalse(board.isValidMove('C', 3, 'B', 2, 'B', 3)); // invalid: side step push
        assertFalse(board.isValidMove('I', 8, 'I', 7, 'I', 6)); // invalid: not valid coordinate
    }

    @Test
    public void testIsValidMove3() {
        // set marbles to fields
        board.setField('D', 3, blackMarbles[0]);
        board.setField('E', 4, blackMarbles[1]);
        board.setField('F', 5, blackMarbles[2]);
        assertTrue(board.isValidMove('D', 3, 'E', 4, 'F', 5, 'C', 2)); // interchangeable in-line head
        assertTrue(board.isValidMove('D', 3, 'E', 4, 'F', 5, 'G', 6)); // interchangeable in-line tail

        board.setField('C', 2, whiteMarbles[0]);
        assertTrue(board.isValidMove('D', 3, 'E', 4, 'F', 5, 'C', 2)); // interchangeable push 1 head
        assertTrue(board.isValidMove('F', 5, 'E', 4, 'D', 3, 'C', 2)); // interchangeable push 1 tail

        board.setField('B', 1, whiteMarbles[1]);
        assertTrue(board.isValidMove('D', 3, 'E', 4, 'F', 5, 'C', 2)); // interchangeable push 2 head
        assertTrue(board.isValidMove('F', 5, 'E', 4, 'D', 3, 'C', 2)); // interchangeable push 2 tail

        board.setField('A', 0, whiteMarbles[2]);
        assertFalse(board.isValidMove('D', 3, 'E', 4, 'F', 5, 'C', 2)); // invalid: push 3

        board.setField('C', 2, blackMarbles[3]);
        printOut(board.toString());
        assertTrue(board.isValidMove('C', 2, 'D', 3, 'E', 4, 'B', 1)); // interchangeable push 2 out head
        assertTrue(board.isValidMove('E', 4, 'D', 3, 'C', 2, 'B', 1)); // interchangeable push 2 out tail

        board.setField('B', 1, blackMarbles[4]);
        assertTrue(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'A', 0)); // interchangeable push 1 out head
        assertTrue(board.isValidMove('D', 3, 'C', 2, 'B', 1, 'A', 0)); // interchangeable push 1 out tail

        // assertFalse(board.isValidMove('C', 2, 'D', 3, 'E', 4, 'B', 1)); // push same
        // color

        assertTrue(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'A', 1)); // side-step to the left
        assertTrue(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'C', 1)); // side-step to the right

        board.setField('A', 1, whiteMarbles[3]);
        assertFalse(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'A', 1)); // invalid: side-step with pushing
        board.setField('C', 1, whiteMarbles[4]);
        printOut(board.toString());
        assertFalse(board.isValidMove('D', 3, 'C', 2, 'B', 1, 'D', 2)); // invalid: side-step with pushing
        assertFalse(board.isValidMove('E', 4, 'D', 3, 'C', 2, 'E', 4)); // invalid: side-step with pushing
        assertFalse(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'C', 1)); // invalid: side-step with pushing
        assertFalse(board.isValidMove('I', 8, 'I', 7, 'I', 6, 'I', 5)); // invalid: not valid coordinate
    }

    @Test
    public void testMoveD1() {
        // initialize marble
        board.setField('A', 0, blackMarbles[0]);
        // in-line move
        board.moveD('A', 0, 'A', 0, 2);
        assertTrue(board.getField('A', 0).getMarble() == null);
        assertEquals(board.getField('A', 1).getMarble(), blackMarbles[0]);
        // side-step move
        board.moveD('A', 1, 'A', 1, 0);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
    }

    @Test
    public void testMoveD2() {
        // initialize marbles
        board.setField('A', 0, blackMarbles[1]);
        board.setField('B', 1, blackMarbles[0]);
        printOut(board.toString());
        // in-line move
        // interchangeable move head
        board.moveD('B', 1, 'A', 0, 1);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 1).getMarble());
        assertTrue(board.getField('A', 0).getMarble() == null);
        // interchangeable move tail
        board.moveD('C', 2, 'B', 1, 4); // still problem with interchangeable
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
        assertEquals(blackMarbles[1], board.getField('A', 0).getMarble());
        assertTrue(board.getField('C', 2).getMarble() == null);
        // side-step move
        board.moveD('A', 0, 'B', 1, 0);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('C', 1).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 0).getMarble());
    }

    @Test
    public void testMoveD2Sumito() {
        // initializing marble
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        Marble pushedMarble = whiteMarbles[0];
        board.setField('B', 1, marble1);
        board.setField('C', 2, marble2);
        board.setField('A', 0, pushedMarble);
        printOut(board.toString());
        // in-line push
        // interchangeable push 1 head
        board.moveD('B', 1, 'C', 2, 4);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble));
        assertEquals(marble1, board.getField('A', 0).getMarble());
        assertEquals(marble2, board.getField('B', 1).getMarble());
        board.reset();

        // interchangeable push 1 tail
        Marble marble3 = blackMarbles[2];
        Marble marble4 = blackMarbles[3];
        Marble pushedMarble2 = whiteMarbles[1];
        board.setField('B', 1, marble3);
        board.setField('C', 2, marble4);
        board.setField('A', 0, pushedMarble2);
        printOut(board.toString());
        // in-line push
        board.moveD('C', 2, 'B', 1, 4);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble2));
        assertEquals(marble3, board.getField('A', 0).getMarble());
        assertEquals(marble4, board.getField('B', 1).getMarble());
    }

    @Test
    public void testMoveD3() {
        // in-line move
        board.setField('A', 0, blackMarbles[2]);
        board.setField('B', 1, blackMarbles[1]);
        board.setField('C', 2, blackMarbles[0]);
        printOut(board.toString());
        // in-line move
        // interchangeable with head
        board.moveD('C', 2, 'A', 0, 1);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('D', 3).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[2], board.getField('B', 1).getMarble());
        // interchangeable with tail
        board.moveD('D', 3, 'B', 1, 4);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 1).getMarble());
        assertEquals(blackMarbles[2], board.getField('A', 0).getMarble());
        // side step move
        board.moveD('C', 2, 'A', 0, 0);
        printOut(board.toString());
        assertEquals(blackMarbles[0], board.getField('D', 2).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 1).getMarble());
        assertEquals(blackMarbles[2], board.getField('B', 0).getMarble());
    }

    @Test
    public void testMoveD3Sumito() {
        // pushing with head
        // initializing marbles
        Marble marble1 = blackMarbles[0];
        Marble marble2 = blackMarbles[1];
        Marble marble3 = blackMarbles[2];
        Marble outedMarble = whiteMarbles[0];
        board.setField('D', 3, marble3);
        board.setField('C', 2, marble2);
        board.setField('B', 1, marble1);
        board.setField('A', 0, outedMarble);
        printOut(board.toString());
        // push out opponent's marble
        board.moveD('B', 1, 'D', 3, 4);
        assertTrue(board.getOuterRim().contains(outedMarble)); // checking the whitemarble is out
        assertEquals(marble1, board.getField('A', 0).getMarble());
        assertEquals(marble2, board.getField('B', 1).getMarble());
        assertEquals(marble3, board.getField('C', 2).getMarble());
        printOut(board.toString());
        // pushing opponent's 1 marble
        Marble pushedMarble1 = whiteMarbles[1];
        board.setField('D', 3, pushedMarble1);
        board.moveD('C', 2, 'A', 0, 1);
        printOut(board.toString());
        assertEquals(pushedMarble1, board.getField('E', 4).getMarble());
        assertEquals(marble3, board.getField('D', 3).getMarble());
        assertEquals(marble2, board.getField('C', 2).getMarble());
        assertEquals(marble1, board.getField('B', 1).getMarble());
        // pushing opponent's 2 marbles
        Marble pushedMarble2 = whiteMarbles[2];
        board.setField('F', 5, pushedMarble2);
        printOut(board.toString());
        board.moveD('D', 3, 'B', 1, 1);
        printOut(board.toString());
        assertEquals(pushedMarble2, board.getField('G', 6).getMarble());
        assertEquals(pushedMarble1, board.getField('F', 5).getMarble());
        assertEquals(marble3, board.getField('E', 4).getMarble());
        assertEquals(marble2, board.getField('D', 3).getMarble());
        assertEquals(marble1, board.getField('C', 2).getMarble());
        // pushing until 2 opponent's marbles go out
        board.moveD('E', 4, 'C', 2, 1);
        printOut(board.toString());
        board.moveD('F', 5, 'D', 3, 1);
        printOut(board.toString());
        board.moveD('G', 6, 'E', 4, 1);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble2));
        board.moveD('H', 7, 'F', 5, 1);
        assertTrue(board.getOuterRim().contains(pushedMarble1));
        printOut(board.toString());
        board.reset();

        // pushing with tails
        // initializing marbles
        Marble marble4 = blackMarbles[3];
        Marble marble5 = blackMarbles[4];
        Marble marble6 = blackMarbles[5];
        Marble outedMarble2 = whiteMarbles[3];
        board.setField('D', 3, marble6);
        board.setField('C', 2, marble5);
        board.setField('B', 1, marble4);
        board.setField('A', 0, outedMarble2);
        printOut(board.toString());
        // push out opponent's marble
        board.moveD('D', 3, 'B', 1, 4);
        assertTrue(board.getOuterRim().contains(outedMarble2)); // checking the whitemarble is out
        assertEquals(marble4, board.getField('A', 0).getMarble());
        assertEquals(marble5, board.getField('B', 1).getMarble());
        assertEquals(marble6, board.getField('C', 2).getMarble());
        printOut(board.toString());
        // pushing opponent's 1 marble
        Marble pushedMarble3 = whiteMarbles[4];
        board.setField('D', 3, pushedMarble3);
        board.moveD('A', 0, 'C', 2, 1);
        printOut(board.toString());
        assertEquals(pushedMarble3, board.getField('E', 4).getMarble());
        assertEquals(marble6, board.getField('D', 3).getMarble());
        assertEquals(marble5, board.getField('C', 2).getMarble());
        assertEquals(marble4, board.getField('B', 1).getMarble());
        // pushing opponent's 2 marbles
        Marble pushedMarble4 = whiteMarbles[5];
        board.setField('F', 5, pushedMarble4);
        printOut(board.toString());
        board.moveD('B', 1, 'D', 3, 1);
        printOut(board.toString());
        assertEquals(pushedMarble4, board.getField('G', 6).getMarble());
        assertEquals(pushedMarble3, board.getField('F', 5).getMarble());
        assertEquals(marble6, board.getField('E', 4).getMarble());
        assertEquals(marble5, board.getField('D', 3).getMarble());
        assertEquals(marble4, board.getField('C', 2).getMarble());
        // pushing until 2 opponent's marbles go out
        board.moveD('C', 2, 'E', 4, 1);
        printOut(board.toString());
        board.moveD('D', 3, 'F', 5, 1);
        printOut(board.toString());
        board.moveD('E', 4, 'G', 6, 1);
        printOut(board.toString());
        assertTrue(board.getOuterRim().contains(pushedMarble4));
        board.moveD('F', 5, 'H', 7, 1);
        assertTrue(board.getOuterRim().contains(pushedMarble3));
        printOut(board.toString());

        // assert
    }

    @Test
    public void testMoveDWithMoveParameter1() { // for 1 marble
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        printOut(board.toString());
        Move movecorrect = new Move('A', 0, 'A', 0, 1);

        // move marbles to a correct position
        board.moveD(movecorrect);
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
    }

    @Test
    public void testMoveDWithMoveParameter2() { // for 2 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        printOut(board.toString());
        Move movecorrecthead = new Move('B', 1, 'A', 0, 1);

        // move marbles to a correct position with head
        board.moveD(movecorrecthead);
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 2).getMarble());

        // move marbles to a correct position with tail
        Move movecorrecttail = new Move('B', 1, 'C', 2, 1);
        board.moveD(movecorrecttail);
        assertEquals(blackMarbles[0], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[1], board.getField('D', 3).getMarble());
    }

    @Test
    public void testMoveDWithMoveParameter3() { // for 3 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        board.setField('C', 2, blackMarbles[2]);
        printOut(board.toString());
        Move movecorrecthead = new Move('C', 2, 'A', 0, 1);
        // Move movewrong = new Move('A', 0, 'C', 2, 4); -> throws
        // IndexOutOfBoundsException

        // move marbles to a correct position with head
        board.moveD(movecorrecthead);
        assertEquals(blackMarbles[0], board.getField('B', 1).getMarble());
        assertEquals(blackMarbles[1], board.getField('C', 2).getMarble());
        assertEquals(blackMarbles[2], board.getField('D', 3).getMarble());

        // move marbles to a correct position with tail
        Move movecorrecttail = new Move('D', 3, 'B', 1, 4);
        board.moveD(movecorrecttail);
        assertEquals(blackMarbles[0], board.getField('A', 0).getMarble());
        assertEquals(blackMarbles[1], board.getField('B', 1).getMarble());
        assertEquals(blackMarbles[2], board.getField('C', 2).getMarble());
    }

    @Test
    public void testIsValidMoveD1() {
        // move 1 marble
        board.setField('D', 3, blackMarbles[0]);
        System.out.println(board.toString());
        assertTrue(board.isValidMoveD('D', 3, 'D', 3, 4)); // in-line
        assertTrue(board.isValidMoveD('D', 3, 'D', 3, 1)); // in-line

        board.setField('C', 2, whiteMarbles[0]);
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 4)); // invalid: push1
        board.setField('B', 1, whiteMarbles[1]);
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 4)); // invalid: push 2
        board.setField('A', 0, whiteMarbles[2]);
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 4)); // invalid: push 3

        // assertFalse(board.isValidMove('C', 2, 'D', 3, 'E', 4, 'B', 1)); // push same
        // color -> check in boardfor2,3,4
        printOut(board.toString());
        assertTrue(board.isValidMoveD('D', 3, 'D', 3, 3)); // side-step to the left
        board.setField('C', 3, whiteMarbles[3]);
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 3)); // invalid: side-step left with pushing
        board.setField('E', 3, whiteMarbles[4]);
        System.out.println(board.toString());
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 0)); // invalid: side-step right with pushing
        board.setField('D', 2, whiteMarbles[5]);
        assertFalse(board.isValidMoveD('D', 3, 'D', 3, 5)); // invalid: side-step side with pushing
        board.setField('I', 8, whiteMarbles[6]);
        assertFalse(board.isValidMoveD('I', 8, 'I', 8, 2)); // invalid: going to out of field
        assertFalse(board.isValidMoveD('I', 8, 'I', 7, 2)); // invalid: moving non-exist marbles
    }

    @Test
    public void testIsValidMoveD2() {
        // move 2 marbles
        board.setField('D', 3, blackMarbles[0]);
        board.setField('E', 4, blackMarbles[1]);
        System.out.println(board.toString());
        assertTrue(board.isValidMoveD('D', 3, 'E', 4, 4)); // interchangeable in-line with head
        assertTrue(board.isValidMoveD('D', 3, 'E', 4, 1)); // interchangeable in-line with tail

        board.setField('C', 2, whiteMarbles[0]);
        assertTrue(board.isValidMoveD('D', 3, 'E', 4, 4)); // interchangeable push 1 with head
        assertTrue(board.isValidMoveD('E', 4, 'D', 3, 4)); // interchangeable push 1 with tail

        board.setField('B', 1, whiteMarbles[1]);
        assertFalse(board.isValidMoveD('D', 3, 'E', 4, 4)); // invalid: push 2

        board.setField('A', 0, whiteMarbles[2]);
        assertFalse(board.isValidMoveD('D', 3, 'E', 4, 4)); // invalid: push 3
        board.setField('C', 2, blackMarbles[3]);
        printOut(board.toString());

        board.setField('B', 1, blackMarbles[2]);
        board.setField('C', 2, blackMarbles[3]);
        assertTrue(board.isValidMoveD('B', 1, 'C', 2, 4)); // interchangeable push 1 out with head
        assertTrue(board.isValidMoveD('C', 2, 'B', 1, 4)); // interchangeable push 1 out with tail

        // assertFalse(board.isValidMove('C', 2, 'D', 3, 'E', 4, 'B', 1)); // push same
        // color -> check in boardfor2,3,4
        assertTrue(board.isValidMoveD('B', 1, 'C', 2, 3)); // side-step to the left
        board.setField('A', 1, whiteMarbles[3]);
        assertFalse(board.isValidMoveD('B', 1, 'C', 2, 3)); // invalid: side-step left with pushing
        board.setField('C', 1, whiteMarbles[4]);
        System.out.println(board.toString());
        assertFalse(board.isValidMoveD('B', 1, 'C', 2, 0)); // invalid: side-step right with pushing
        board.setField('B', 0, whiteMarbles[5]);
        assertFalse(board.isValidMoveD('B', 1, 'C', 2, 5)); // invalid: side-step side with pushing
        assertFalse(board.isValidMoveD('H', 0, 'H', 1, 0)); // invalid: moving non-existence marble
    }

    @Test
    public void testIsValidMoveD3() {
        // move 3 marbles
        board.setField('D', 3, blackMarbles[0]);
        board.setField('E', 4, blackMarbles[1]);
        board.setField('F', 5, blackMarbles[2]);
        System.out.println(board.toString());
        assertTrue(board.isValidMoveD('D', 3, 'F', 5, 4)); // interchangeable in-line with head
        assertTrue(board.isValidMoveD('D', 3, 'F', 5, 1)); // interchangeable in-line with tail

        board.setField('C', 2, whiteMarbles[0]);
        assertTrue(board.isValidMoveD('D', 3, 'F', 5, 4)); // interchangeable push 1 with head
        assertTrue(board.isValidMoveD('F', 5, 'D', 3, 4)); // interchangeable push 1 with tail

        board.setField('B', 1, whiteMarbles[1]);
        assertTrue(board.isValidMoveD('D', 3, 'F', 5, 4)); // interchangeable push 2 with head
        assertTrue(board.isValidMoveD('F', 5, 'D', 3, 4)); // interchangeable push 2 with tail

        board.setField('A', 0, whiteMarbles[2]);
        assertFalse(board.isValidMoveD('D', 3, 'F', 5, 4)); // invalid: push 3
        board.setField('C', 2, blackMarbles[3]);
        printOut(board.toString());

        assertTrue(board.isValidMoveD('C', 2, 'E', 4, 4)); // interchangeable push 2 out with head
        assertTrue(board.isValidMoveD('E', 4, 'C', 2, 4)); // interchangeable push 2 out with tail

        board.setField('B', 1, blackMarbles[4]);
        assertTrue(board.isValidMoveD('B', 1, 'D', 3, 4)); // push 1 out
        // assertFalse(board.isValidMove('C', 2, 'D', 3, 'E', 4, 'B', 1)); // push same
        // color -> check in boardfor2,3,4
        assertTrue(board.isValidMoveD('B', 1, 'D', 3, 3)); // side-step to the left
        board.setField('A', 1, whiteMarbles[3]);
        assertFalse(board.isValidMoveD('B', 1, 'D', 3, 3)); // invalid: side-step left with pushing
        board.setField('C', 1, whiteMarbles[4]);
        System.out.println(board.toString());
        assertFalse(board.isValidMoveD('B', 1, 'D', 3, 0)); // invalid: side-step right with pushing
        assertFalse(board.isValidMoveD('C', 2, 'E', 4, 5)); // invalid: side-step side with pushing
        // assertFalse(board.isValidMove('B', 1, 'C', 2, 'D', 3, 'C', 1)); // same as
        // the first one

    }

    @Test
    public void testIsValidMoveDWithMoveParameter1() { // for 1 marble
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        System.out.println(board.toString());
        Move movecorrect = new Move('A', 0, 'A', 0, 1);
        Move movewrong = new Move('A', 0, 'A', 0, 4);

        // move 1 marble to a correct position
        assertTrue(board.isValidMoveD(movecorrect));

        // move 1 marble to a wrong position
        assertFalse(board.isValidMoveD(movewrong));
    }

    @Test
    public void testIsValidMoveDWithMoveParameter2() { // for 2 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        System.out.println(board.toString());
        Move movecorrecthead = new Move('B', 1, 'A', 0, 1);
        Move movecorrecttail = new Move('A', 0, 'B', 1, 1);
        Move movewronghead = new Move('A', 0, 'B', 1, 4);

        // move 2 marbles to a correct position with head
        assertTrue(board.isValidMoveD(movecorrecthead));

        // move 2 marbles to a correct position with tail
        assertTrue(board.isValidMoveD(movecorrecttail));

        // move 2 marbles to a wrong position with head
        assertFalse(board.isValidMoveD(movewronghead));

        // move 2 marbles to a wrong position with tail
        Move movewrongtail = new Move('B', 1, 'A', 0, 4);
        assertFalse(board.isValidMoveD(movewrongtail));
    }

    @Test
    public void testIsValidMoveDWithMoveParameter3() { // for 3 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        board.setField('C', 2, blackMarbles[2]);
        System.out.println(board.toString());
        Move movecorrecthead = new Move('C', 2, 'A', 0, 1);
        Move movecorrecttail = new Move('A', 0, 'C', 2, 1);
        Move movewronghead = new Move('A', 0, 'C', 2, 4);

        // move 3 marbles to a correct position with head
        assertTrue(board.isValidMoveD(movecorrecthead));

        // move 3 marbles to a correct position with tail
        assertTrue(board.isValidMoveD(movecorrecttail));

        // move 3 marbles to a wrong position with head
        assertFalse(board.isValidMoveD(movewronghead));

        // move 3 marbles to a wrong position with tail
        Move movewrongtail = new Move('C', 2, 'A', 0, 4);
        assertFalse(board.isValidMoveD(movewrongtail));
    }

    @Test
    public void testIsValidMoveDForColour1() { // for 1 marble
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        System.out.println(board.toString());
        Move movecorrect = new Move('A', 0, 'A', 0, 1);
        Move movewrong = new Move('A', 0, 'A', 0, 4);

        // move 1 marble to a correct position with the correct colour
        assertTrue(board.isValidMoveDForColour(movecorrect, Colour.BLACK));

        // move 1 marble to a wrong position with the correct colour
        assertFalse(board.isValidMoveDForColour(movewrong, Colour.BLACK));

        // move 1 marble to a correct position with the wrong colour
        assertFalse(board.isValidMoveDForColour(movecorrect, Colour.WHITE));

        // move 1 marble to a wrong position with the wrong colour
        assertFalse(board.isValidMoveDForColour(movewrong, Colour.WHITE));

    }

    @Test
    public void testIsValidMoveDForColour2() { // for 2 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        System.out.println(board.toString());
        Move movecorrecthead = new Move('B', 1, 'A', 0, 1);
        Move movecorrecttail = new Move('A', 0, 'B', 1, 1);
        Move movewronghead = new Move('A', 0, 'B', 1, 4);

        // move 2 marbles to a correct position with head and the right colour
        assertTrue(board.isValidMoveDForColour(movecorrecthead, Colour.BLACK));

        // move 2 marbles to a correct position with tail and the right colour
        assertTrue(board.isValidMoveDForColour(movecorrecttail, Colour.BLACK));

        // move 2 marbles to a wrong position with head and the right colour
        assertFalse(board.isValidMoveDForColour(movewronghead, Colour.BLACK));

        // move 2 marbles to a wrong position with tail and the right colour
        Move movewrongtail = new Move('B', 1, 'A', 0, 4);
        assertFalse(board.isValidMoveDForColour(movewrongtail, Colour.BLACK));

        // move 2 marbles to a correct position with head and the wrong colour
        assertFalse(board.isValidMoveDForColour(movecorrecthead, Colour.WHITE));

        // move 2 marbles to a correct position with tail and the wrong colour
        assertFalse(board.isValidMoveDForColour(movecorrecttail, Colour.WHITE));

        // move 2 marbles to a wrong position with head and the wrong colour
        assertFalse(board.isValidMoveDForColour(movewronghead, Colour.WHITE));

        // move 2 marbles to a wrong position with tail and the wrong colour
        assertFalse(board.isValidMoveDForColour(movewrongtail, Colour.WHITE));
    }

    @Test
    public void testIsValidMoveDForColour3() { // for 3 marbles
        // initialize marbles
        board.setField('A', 0, blackMarbles[0]);
        board.setField('B', 1, blackMarbles[1]);
        board.setField('C', 2, blackMarbles[2]);
        System.out.println(board.toString());
        Move movecorrecthead = new Move('C', 2, 'A', 0, 1);
        Move movecorrecttail = new Move('A', 0, 'C', 2, 1);
        Move movewronghead = new Move('A', 0, 'C', 2, 4);

        // move 3 marbles to a correct position with head and the right colour
        assertTrue(board.isValidMoveDForColour(movecorrecthead, Colour.BLACK));

        // move 3 marbles to a correct position with tail and the right colour
        assertTrue(board.isValidMoveDForColour(movecorrecttail, Colour.BLACK));

        // move 3 marbles to a wrong position with head and the right colour
        assertFalse(board.isValidMoveDForColour(movewronghead, Colour.BLACK));

        // move 3 marbles to a wrong position with tail and the right colour
        Move movewrongtail = new Move('C', 2, 'A', 0, 4);
        assertFalse(board.isValidMoveDForColour(movewrongtail, Colour.BLACK));

        // move 3 marbles to a correct position with head and the wrong colour
        assertFalse(board.isValidMoveDForColour(movecorrecthead, Colour.WHITE));

        // move 3 marbles to a correct position with tail and the wrong colour
        assertFalse(board.isValidMoveDForColour(movecorrecttail, Colour.WHITE));

        // move 3 marbles to a wrong position with head and the wrong colour
        assertFalse(board.isValidMoveDForColour(movewronghead, Colour.WHITE));

        // move 3 marbles to a wrong position with tail and the wrong colour
        assertFalse(board.isValidMoveDForColour(movewrongtail, Colour.WHITE));
    }

    @Test
    public void testIsWinner() {
        // test that both does not win nor lose in the first init
        board.init();
        assertFalse(board.isWinner(Colour.BLACK));
        assertFalse(board.isWinner(Colour.WHITE));
        board.reset();

        // initializing marbles
        board.setField('B', 1, blackMarbles[0]);
        board.setField('A', 0, blackMarbles[1]);
        board.setField('C', 2, whiteMarbles[0]);
        board.setField('D', 3, whiteMarbles[1]);
        board.setField('E', 4, whiteMarbles[2]);
        // test that a marble has win after pushing 6 marbles
        // pushing 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // moving white marbles back so it can push black marbles again, pushed 2
        // marbles
        board.moveD('C', 2, 'A', 0, 1);
        board.moveD('D', 3, 'B', 1, 1);
        board.setField('A', 0, blackMarbles[2]);
        board.setField('B', 1, blackMarbles[3]);
        // pushing another 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // moving white marbles back so it can push black marbles again, pushed 4
        // marbles
        board.moveD('C', 2, 'A', 0, 1);
        board.moveD('D', 3, 'B', 1, 1);
        board.setField('A', 0, blackMarbles[4]);
        board.setField('B', 1, blackMarbles[5]);
        // pushing another 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // white marbles has pushed 6 marbles out
        assertTrue(board.isWinner(Colour.WHITE));
        assertFalse(board.isWinner(Colour.BLACK));
    }

    @Test
    public void testHasWinner() {
        // test that no marbles lose
        board.init();
        assertFalse(board.hasWinner());
        board.reset();

        // initializing marbles
        board.setField('B', 1, blackMarbles[0]);
        board.setField('A', 0, blackMarbles[1]);
        board.setField('C', 2, whiteMarbles[0]);
        board.setField('D', 3, whiteMarbles[1]);
        board.setField('E', 4, whiteMarbles[2]);
        // test that a marble has win after pushing 6 marbles
        // pushing 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // moving white marbles back so it can push black marbles again, pushed 2
        // marbles
        board.moveD('C', 2, 'A', 0, 1);
        board.moveD('D', 3, 'B', 1, 1);
        board.setField('A', 0, blackMarbles[2]);
        board.setField('B', 1, blackMarbles[3]);
        // pushing another 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // moving white marbles back so it can push black marbles again, pushed 4
        // marbles
        board.moveD('C', 2, 'A', 0, 1);
        board.moveD('D', 3, 'B', 1, 1);
        board.setField('A', 0, blackMarbles[4]);
        board.setField('B', 1, blackMarbles[5]);
        // pushing another 2 marbles out
        board.moveD('C', 2, 'E', 4, 4);
        board.moveD('B', 1, 'D', 3, 4);
        // white marbles has pushed 6 marbles out
        assertTrue(board.hasWinner());
    }

    private void printOut(String str) {
        // System.out.println(str);
    }

}