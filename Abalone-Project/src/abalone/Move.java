package abalone;

import exceptions.MoveFormatException;
import protocol.ProtocolMessages;

/**
 * Provides a way to represent moves. The fields hold the row and column values
 * and don't belong to a board and don't have marbles.
 * 
 *
 */
public class Move {
    private static final int START_COL = 1;
    private Field head;
    private Field tail;
    private int direction;

    /**
     * Construct a move with 2 row and column coordinates and direction.
     * 
     * @param rowHead   row coordinate 1.
     * @param colHead   column coordinate 1.
     * @param rowTail   row coordinate 2.
     * @param colTail   column coordinate 2.
     * @param direction the direction to be moved in(0-5) 0 is top left.
     */
    public Move(char rowHead, int colHead, char rowTail, int colTail, int direction) {
        head = new Field(rowHead, colHead);
        tail = new Field(rowTail, colTail);
        this.direction = direction;
    }

    public Field getHead() {
        return head;
    }

    public Field getHead(Board board) {
        return board.getField(head.getRow(), head.getCol());
    }

    public Field getTail() {
        return tail;
    }

    public Field getTail(Board board) {
        return board.getField(tail.getRow(), tail.getCol());
    }

    public int getDirection() {
        return direction;
    }

    /**
     * Get the move type of this move.Moving 1,2, or 3 marbles.
     * 
     * @requires move to be valid in board
     * @param board Board to get the move type.
     * @return 1 if moving one marble, 2 if moving 2 marbles, 3 if moving 3 marbles.
     */
    public int moveType(Board board) {
        if (head.equals(tail)) { // moving one marble
            return 1;
        } else if (board.isColumn(head.getRow(), head.getCol(), tail.getRow(), tail.getCol())) { // moving two marbles
            return 2;
        } else {
            return 3;
        }
    }

    public Field getDirectionField(Board board) {
        return board.getNeighbor(head.getRow(), head.getCol(), direction); // can be null
    }

    /**
     * Return a copy of this move with the head and tail reversed.
     * 
     * @return
     */
    public Move inverseMove() {
        return new Move(this.getTail().getRow(), this.getTail().getCol(), this.getHead().getRow(),
                this.getHead().getCol(), this.getDirection());
    }

    /**
     * Parse move in the form: [A0,B1,3] .
     * 
     * @param move Move in the form: [A0,B1,3]
     * @return
     * 
     * @throws MoveFormatException if move format is invalid.
     */
    public static Move parse(String move) throws MoveFormatException {
        Move result = null;
        try {
            String[] moveArr = move.split(ProtocolMessages.SEPARATOR);
            char rowHead = moveArr[0].charAt(0);
            int colHead = Integer.parseInt(moveArr[0].charAt(1) + "");
            char rowTail = moveArr[1].charAt(0);
            int colTail = Integer.parseInt(moveArr[1].charAt(1) + "");
            int direction = Integer.parseInt(moveArr[2].charAt(0) + "");
            if (direction < 0 || direction > 5) {
                throw new MoveFormatException("Direction should be between 0-6, it was:" + direction);
            }
            result = new Move(rowHead, colHead, rowTail, colTail, direction);
        } catch (Exception e) { 
            throw new MoveFormatException(e.getMessage());
        }

        assert result != null;
        return result;

    }

    /**
     * Parse move with indexes starting from 1 in form: [A1,B2,3].
     * 
     * @param move      string to be parsed.
     * @param seperator seperator to use
     * @param startsAt  index to start at. protocol starts at 1.
     * @return the move
     * 
     * @throws MoveFormatException if move format is invalid.
     */
    public static Move parseWithOne(String move, String seperator, int startsAt) throws MoveFormatException {
        Move result = null;
        try {
            String[] moveArr = move.split(seperator);
            char rowHead = moveArr[0].charAt(0);
            int colHead = Integer.parseInt(moveArr[0].charAt(1) + "") - (startsAt);
            char rowTail = moveArr[1].charAt(0);
            int colTail = Integer.parseInt(moveArr[1].charAt(1) + "") - (startsAt);
            int direction = Integer.parseInt(moveArr[2].charAt(0) + "");
            if (direction < 0 || direction > 5) {
                throw new MoveFormatException("Direction should be between 0-6, it was:" + direction);
            }
            result = new Move(rowHead, colHead, rowTail, colTail, direction);
        } catch (Exception e) { 
            throw new MoveFormatException(e.getMessage());
        }

        assert result != null;
        return result;

    }

    /**
     * Parse given string in protocol format as a move.
     * 
     * @requires move to be in format: 5,A;5,B;1; (col,row,col,row,direction) cols
     *           start from 1
     * @param move to be parsed.
     * @return
     * 
     * @throws MoveFormatException if a move is in invalid format.
     * 
     */
    public static Move parseProtocol(String move) throws MoveFormatException {
        Move result = null;
        try {
            String[] moveArr = move.split(ProtocolMessages.DELIMITER);
            String[] head = moveArr[0].split(ProtocolMessages.SEPARATOR);
            int colHead = Integer.parseInt(head[0].charAt(0) + "") - START_COL; // -1 as starts from 1
            char rowHead = head[1].charAt(0);
            String[] tail = moveArr[1].split(ProtocolMessages.SEPARATOR);
            int colTail = Integer.parseInt(tail[0].charAt(0) + "") - START_COL; // -1 as starts from 1
            char rowTail = tail[1].charAt(0);
            int direction = Integer.parseInt(moveArr[2].charAt(0) + "");
            if (direction < 0 || direction > 5) {
                throw new MoveFormatException("Direction should be between 0-6, it was:" + direction);
            }
            result = new Move(rowHead, colHead, rowTail, colTail, direction);
        } catch (Exception e) {
            throw new MoveFormatException(e.getMessage());
        }

        assert result != null;
        return result;

    }

    /**
     * Inverse of parseProtocol. Turns this move to a string in protocol formatting.
     * 
     * @return protocol representation of this move.
     */
    public String protocolFormat() { // format: 5,A;5,B;1;
        return (getHead().getCol() + START_COL) + ProtocolMessages.SEPARATOR + getHead().getRow() // First marble
                + ProtocolMessages.DELIMITER + (getTail().getCol() + START_COL) + ProtocolMessages.SEPARATOR
                + getTail().getRow() // Second
                // marble
                + ProtocolMessages.DELIMITER + getDirection();
    }

    @Override
    public String toString() {
        return head.toString().substring(1) + "," + tail.toString().substring(1) + "," + direction;
    }

}
