package abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Board class that is used for interacting with the board. Contains methods for
 * making moves and checking if those moves are valid and related methods. This
 * class does not initialize any marbles on the board and subclasses are
 * expected to initialize the marbles.
 * 
 * @author Berke Guducu
 *
 */
public class Board {
    public static final int SIDE_LENGTH = 5;
    public static final int DIM = (SIDE_LENGTH * 2) - 1;
    protected Field[][] fields;
    protected ArrayList<Marble> outerRim;
    public static final int LOSERMARBLES = 6;
    protected ArrayList<Team> teams;

    /**
     * Initialize a board with colors black and white. Teams are not initialized,
     * used for extending classes.
     * 
     * @ensures all fields are initialized
     * @invariant all fields have correct coordinates
     * @invariant there are no duplicate marbles
     */
    public Board() {
        this(Colour.BLACK, Colour.WHITE);
        init();
    }

    /**
     * Initialize a board with given colors(first color at the bottom) and
     * initialize the teams.
     * 
     * @param c0 color of first player.
     * @param c1 color of second player.
     */
    public Board(Colour c0, Colour c1) {
        Colour[] cteam0 = { c0 };
        Colour[] cteam1 = { c1 };
        initTeams(cteam0, cteam1);
        init();
    }

    /**
     * Create a board with given fields, outer rim and teams. Can be used to reset
     * the board.
     * 
     * @param fields   to be used in the board.
     * @param outerRim to be used in the board.
     * @param teams    to be used in the board.
     */
    protected Board(Field[][] fields, ArrayList<Marble> outerRim, ArrayList<Team> teams) {
        this();
        this.fields = fields;
        this.outerRim = outerRim;
        this.teams = teams;
    }

    /**
     * Initialize the fields and the outer rim.
     * 
     * @ensures all fields and outerRim are initialized(are not null).
     */
    public void init() {
        outerRim = new ArrayList<Marble>();
        fields = new Field[DIM][DIM]; // rows, cols
        for (int i = 0; i < SIDE_LENGTH; i++) {
            fields[i] = new Field[(SIDE_LENGTH) + i];
            fields[DIM - i - 1] = new Field[(SIDE_LENGTH) + i];
        }
        giveRowAndCol();
    }

    /**
     * Initialize the teams with given colors.
     * 
     * @param c0 color of first team.
     * @param c1 color of second team.
     */
    protected void initTeams(Colour[] c0, Colour[] c1) {
        teams = new ArrayList<Team>();
        teams.add(new Team(c0));
        teams.add(new Team(c1));
    }

    /**
     * Initialize the teams with given 3 colors. Used in BoardFor3.
     * 
     * @param c0 color of team 0.
     * @param c1 color of team 1.
     * @param c2 color of team 2.
     */
    protected void initTeams(Colour c0, Colour c1, Colour c2) {
        teams = new ArrayList<Team>();
        Colour[] c0Arr = { c0 };
        Colour[] c1Arr = { c1 };
        Colour[] c2Arr = { c2 };
        teams.add(new Team(c0Arr));
        teams.add(new Team(c1Arr));
        teams.add(new Team(c2Arr));
    }

    /**
     * Reset this board. Reseting the fields, teams, and the outer rim.
     */
    public void reset() {
        init();
        for (Team t : teams) { // set team scores to 0
            t.setScore(0);
        }
    }

    /**
     * Get the fields of this board.
     * 
     * @return the fields.
     */
    public Field[][] getFields() {
        return fields;
    }

    /**
     * Set row and column values of the fields. Used in init().
     */
    private void giveRowAndCol() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                char rowChar = (char) (i + 65);
                fields[DIM - 1 - i][j] = (new Field(rowChar, j));
                fields[DIM - 1 - i][j].setRowAndCol(rowChar, colToColCoord(rowChar, j));
            }
        }
    }

    /**
     * Create a deep copy of this board with identical teams, outer rim, and fields.
     * 
     * @return the deep copy.
     */
    public Board deepCopy() {
        // copy fields
        Field[][] fieldsCopy = new Field[DIM][DIM];
        for (int i = 0; i < SIDE_LENGTH; i++) {
            fieldsCopy[i] = new Field[(SIDE_LENGTH) + i];
            fieldsCopy[DIM - i - 1] = new Field[(SIDE_LENGTH) + i];
        }
        // copy field coordinates
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                char rowChar = (char) (i + 65);
                fieldsCopy[DIM - 1 - i][j] = (new Field(rowChar, j));
                fieldsCopy[DIM - 1 - i][j].setRowAndCol(rowChar, colToColCoord(rowChar, j));
            }
        }
        // copy marbles
        for (int i = 0; i < DIM; i++) { // rows
            for (int j = 0; j < fields[i].length; j++) { // cols
                if (fields[i][j].getMarble() != null) {
                    fieldsCopy[i][j].setMarble(fields[i][j].getMarble());
                }
            }
        }
        // copy outer rim
        ArrayList<Marble> outerRimCopy = new ArrayList<Marble>();
        Iterator<Marble> it = outerRim.iterator();
        while (it.hasNext()) {
            outerRimCopy.add(it.next());
        }
        // team copy
        ArrayList<Team> teamsCopy = new ArrayList<Team>();
        for (Team t : teams) {
            Team newTeam = new Team(t.getColours());
            newTeam.setScore(t.getScore());
            teamsCopy.add(newTeam);
        }

        Board copy = new Board(fieldsCopy, outerRimCopy, teamsCopy);
        return copy;
    }

    /**
     * Convert column indexes(0-8) to column coordinates(0-8).
     * 
     * @param rowChar row character(A-I).
     * @param col     column index.
     * @requires rowChar >= 'A' && rowChar <= 'A'+DIM
     * @requires col >= 0 && col < DIM
     * @ensures colCoord >= 0 && colCoord < DIM
     * @return
     */
    private int colToColCoord(char rowChar, int col) {
        int colCoord = col;
        int row = letterToRow(rowChar);
        if (row > SIDE_LENGTH - 1) {
            colCoord = col + (DIM - fields[row].length);
        }
        return colCoord;

    }

    public ArrayList<Marble> getOuterRim() {
        return outerRim;
    }

    /**
     * Get a field with given row and col index.
     * 
     * @requires row to be a valid index in spaces.
     * @requires col to be a valid index in that row.
     * @param row index of row (starting from bottom).
     * @param col index of column.
     * @return
     */
    public Field getField(int row, int col) {
        // row is reversed as index 0 should be at the bottom
        return fields[DIM - row - 1][col];
    }

    /**
     * Get field with the coordinate system.
     * 
     * @requires row to be a valid index
     * @requires colCoord to be
     * @param rowChar  the row coordinate.
     * @param colCoord column's coordinate.
     * @return the field with given coordinates
     */
    public Field getField(char rowChar, int colCoord) {
        int row = letterToRow(rowChar);
        int col = coordToColIndex(rowChar, colCoord);
        return getField(row, col);

    }

    /**
     * Convert coordinate column values to column index value.
     * 
     * @requires rowChar to be a valid index.
     * @param rowChar  the row coordinate.
     * @param colCoord the column coordinate.
     * @return
     */
    protected int coordToColIndex(char rowChar, int colCoord) {
        int col = colCoord;
        int row = letterToRow(rowChar);
        if (row > SIDE_LENGTH - 1) {
            col = colCoord - (DIM - fields[row].length);
        }
        return col;
    }

    /**
     * Set field using row index(0 at bottom) and col index(always starting at 0).
     * 
     * @requires row and col to be valid indexes
     * @param row    row index.
     * @param col    column index.
     * @param marble marble to be put in the field.
     */
    protected void setFieldwIndex(int row, int col, Marble marble) {
        fields[DIM - row - 1][col].setMarble(marble);
        ;
    }

    /**
     * Set field with row letter and colCoord(row I starts at 4).
     * 
     * @param row      coordinate of row.
     * @param colCoord coordinate of column.
     * @param marble   marble to be put in the field.
     */
    public void setField(char row, int colCoord, Marble marble) {
        setFieldwIndex(letterToRow(row), coordToColIndex(row, colCoord), marble);
    }

    /**
     * Turns a letter for a row to an index(int). A -> 0 B -> 2 I -> 8.
     * 
     * @param rowChar the row coordinate.
     * @return the index of the row.
     */
    public int letterToRow(char rowChar) {
        return (Character.toUpperCase(rowChar) - 64 - 1);
    }

    /**
     * Get the total number of fields on the board.
     * 
     * @return the total number of spaces on the board
     */
    public int numberOfFields() {
        int nspaces = 0;
        for (int i = 0; i < fields.length; i++) { // for every row
            nspaces += fields[i].length;
        }
        return nspaces;

    }

    /**
     * Check if a row and column index responds to a valid space on the board.
     * 
     * @param row index of row.
     * @param col index of column.
     * @return
     */
    public boolean isFieldwIndex(int row, int col) {
        return (row < DIM && row >= 0 && col < fields[row].length && col >= 0);
    }

    /**
     * Check if a row letter and column responds to a valid space on the board.
     * 
     * @param rowChar row coordinate.
     * @param col     column index.
     * @return
     */
    public boolean isFieldwIndexChar(char rowChar, int col) {
        char upper = Character.toUpperCase(rowChar);
        return ((upper >= 65 && upper <= 90)) && isFieldwIndex(letterToRow(rowChar), col);
    }

    /**
     * Check if a coordinate is a field on the board.
     * 
     * @param rowChar  row coordinate.
     * @param colCoord column coordinate.
     * @return
     */
    public boolean isFieldCoord(char rowChar, int colCoord) {
        return letterToRow(rowChar) >= 0 && letterToRow(rowChar) < DIM && colCoord < DIM && colCoord >= 0
                && isFieldwIndexChar(rowChar, coordToColIndex(rowChar, colCoord));
    }

    /**
     * Gets the adjacent field of a field. 0 is top left, going clockwise.
     * 
     * @requires row and column to be valid indexes
     * @param rowChar  row coordinate.
     * @param colCoord column coordinate.
     * @return
     */
    public Field[] getNeighbors(char rowChar, int colCoord) {
        Field[] neighbours = { getFieldwCoordSafe((char) (rowChar + 1), colCoord), // top left
                getFieldwCoordSafe((char) (rowChar + 1), colCoord + 1), // top right
                getFieldwCoordSafe((char) (rowChar), colCoord + 1), // mid right
                getFieldwCoordSafe((char) (rowChar - 1), colCoord), // bottom right
                getFieldwCoordSafe((char) (rowChar - 1), colCoord - 1), // bottom left
                getFieldwCoordSafe((char) (rowChar), colCoord - 1) };// mid left

        return neighbours;
    }

    /**
     * Get neighbor using the neighbor no. 0 is top left, going clockwise.
     * 
     * @requires neighborNo > 0 && neighborNo<6
     * @param neighborNo the number of neighbor 0-5.
     * @return the neighboring field.
     */
    public Field getNeighbor(char rowChar, int colCoord, int neighborNo) {
        return getNeighbors(rowChar, colCoord)[neighborNo];
    }

    /**
     * getField using coordinates but returns null if it is not valid index.
     * 
     * @param rowChar  row coordinate.
     * @param colCoord column coordinate.
     * @return the field or null if not found
     */
    private Field getFieldwCoordSafe(char rowChar, int colCoord) {
        if (!isFieldCoord(rowChar, colCoord)) {
            return null;
        }
        return getField(rowChar, colCoord);
    }

    /**
     * Check if 2 fields are adjacent to each other.
     * 
     * @param rowLetter  row coordinate 1.
     * @param colCoord   column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @return true if they are next to each other.
     */
    public boolean isColumn(char rowLetter, int colCoord, char rowLetter2, int colCoord2) {
        Field field1 = getField(rowLetter, colCoord);
        Field field2 = getField(rowLetter2, colCoord2);
        Field[] neighbours = getNeighbors(rowLetter, colCoord);
        if (field1.equals(field2)) {
            return false;
        }
        for (Field f : neighbours) {
            if (f != null && f.equals(field2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if 3 fields form a column(a connected straight line).
     * 
     * @requires the field to be valid and unique
     * @requires the provided fields to be ordered.
     * @param row1 row coordinate 1.
     * @param col1 column coordinate 1.
     * @param row2 row coordinate 2.
     * @param col2 column coordinate 2.
     * @param row3 row coordinate 3.
     * @param col3 column coordinate 3.
     * @return true if 3 of them form a column
     */
    public boolean isColumn(char row1, int col1, char row2, int col2, char row3, int col3) {
        return (this.isColumn(row1, col1, row2, col2) && isColumn(row2, col2, row3, col3)
                && this.isOnSameLine(row1, col1, row3, col3)) && !isColumn(row1, col1, row3, col3);
    }

    /**
     * Check if two fields are on the same line using coordinates.
     * 
     * @requires both rowLetters to be in range A-I or a-i
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @return
     */
    public boolean isOnSameLine(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2) {
        if (rowLetter1 == rowLetter2) {
            return true;
        } else if (colCoord1 == colCoord2) {
            return true;
        } else { // example: c2 and d3
            int rowDiff = rowLetter1 - rowLetter2;
            int colDiff = colCoord1 - colCoord2;
            return rowDiff == colDiff;
        }
    }

    /**
     * Check is 3 fields are on the same line.
     * 
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @param rowLetter3 row coordinate 3.
     * @param colCoord3  column coordinate 3.
     * @return
     */
    private boolean isOnSameLine(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2, char rowLetter3,
            int colCoord3) {
        // can't use the other isOnSameLine method as 3 dots can be in the same line in
        // pairs of 2.
        if ((rowLetter1 == rowLetter2) && (rowLetter2 == rowLetter3)) {
            return true;
        } else if ((colCoord1 == colCoord2) && (colCoord2 == colCoord3)) {
            return true;
        } else { // example: c2, d3, e4
            int rowDiff12 = rowLetter1 - rowLetter2;
            int colDiff12 = colCoord1 - colCoord2;
            int rowDiff23 = rowLetter3 - rowLetter2;
            int colDiff23 = colCoord3 - colCoord2;
            return rowDiff12 == colDiff12 && rowDiff23 == colDiff23;
        }
    }

    /**
     * Move 1 marble(can't push). Can move marbles to outerRim.
     * 
     * @requires to be a valid move
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowDest    the row to move into.
     * @param colDest    the column to move into.
     * @return
     */
    public void move(char rowLetter1, int colCoord1, char rowDest, int colDest) {
        Marble marble = getField(rowLetter1, colCoord1).getMarble();
        getField(rowLetter1, colCoord1).setMarble(null);
        getField(rowDest, colDest).setMarble(marble);
    }

    /**
     * Move 2 marbles without pushing.
     * 
     * @requires to be a valid move. The first marble provided should be the one
     *           that is intended to end up in the Destination field.
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @param rowDest    row coordinate of destination.
     * @param colDest    column coordinate of destination.
     */
    public void move(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2, char rowDest, int colDest) {
        char headRow = rowLetter1;
        int headCol = colCoord1;
        char backRow = rowLetter2;
        int backCol = colCoord2;
        // select correct head
        if (!isColumn(rowLetter1, colCoord1, rowDest, colDest)) {
            headRow = rowLetter2;
            headCol = colCoord2;
            backRow = rowLetter1;
            backCol = colCoord1;
        }

        // push move if there is a marble in the way, should be in the same line
        if (getField(rowDest, colDest).getMarble() != null) {
            int neighborNumber = getNeighborNumber(headRow, headCol, rowDest, colDest);
            Field pushedField = this.getNeighbor(rowDest, colDest, neighborNumber);
            if (pushedField == null) {
                getTeam(getField(headRow, headCol).getMarble().getColour()).addScore();
                outerRim.add(getField(rowDest, colDest).getMarble());
                getField(rowDest, colDest).setMarble(null);
            } else {
                move(rowDest, colDest, pushedField.getRow(), pushedField.getCol());
            }
            move(headRow, headCol, backRow, backCol, rowDest, colDest);
        } else if (this.isOnSameLine(rowDest, colDest, headRow, headCol, backRow, backCol)) { // if in-line move
            // if in-line move
            // set the correct head row if it was in reverse order
            move(headRow, headCol, rowDest, colDest);
            move(backRow, backCol, headRow, headCol);
            return;
        } else { // side step move
            int neighborNo = getNeighborNumber(headRow, headCol, rowDest, colDest);
            Field dest2 = this.getNeighbor(backRow, backCol, neighborNo);
            move(headRow, headCol, rowDest, colDest);
            move(backRow, backCol, dest2.getRow(), dest2.getCol());
        }

    }

    /**
     * Move 3 marbles.
     * 
     * @requires coordinates to be in order.
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @param rowLetter3 row coordinate 3.
     * @param colCoord3  column coordinate 3.
     * @param rowDest    row coordinate of destination.
     * @param colDest    the column to move into.
     */
    public void move(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2, char rowLetter3, int colCoord3,
            char rowDest, int colDest) {
        char headRow = rowLetter1;
        int headCol = colCoord1;
        char midRow = rowLetter2;
        int midCol = colCoord2;
        char backRow = rowLetter3;
        int backCol = colCoord3;

        if (!isColumn(rowLetter1, colCoord1, rowDest, colDest)) { // if coord1 is not head
            if (isColumn(rowLetter3, colCoord3, rowDest, colDest)) {
                headRow = rowLetter3;
                headCol = colCoord3;
                backRow = rowLetter1;
                backCol = colCoord1;
            }
        }

        int neighborNo = this.getNeighborNumber(headRow, headCol, rowDest, colDest);
        // select correct head

        // push move
        if (getField(rowDest, colDest).getMarble() != null) {
            // if the second field to be pushed is out of play area
            Field secondDest = this.getNeighbor(rowDest, colDest, neighborNo);
            // if there is no second field after or a field without a marble
            if (secondDest == null || secondDest.getMarble() == null) {
                move(headRow, headCol, midRow, midCol, rowDest, colDest);
                move(backRow, backCol, midRow, midCol);
            } else { // if there is a second destination field with a marble
                Field thirdDest = this.getNeighbors(secondDest.getRow(), secondDest.getCol())[neighborNo];
                if (thirdDest == null) {
                    // push marble out
                    this.outerRim.add(secondDest.getMarble());
                    // add to the score of the other team
                    getTeam(getField(headRow, headCol).getMarble().getColour()).addScore();
                    // empty old field
                    this.setField(secondDest.getRow(), secondDest.getCol(), null);
                } else {
                    this.move(secondDest.getRow(), secondDest.getCol(), thirdDest.getRow(), thirdDest.getCol());
                }
                this.move(headRow, headCol, midRow, midCol, backRow, backCol, rowDest, colDest);

            }
        } else if (this.isOnSameLine(headRow, headCol, midRow, midCol, rowDest, colDest)) {
            // in-line move
            move(headRow, headCol, midRow, midCol, rowDest, colDest);
            move(backRow, backCol, midRow, midCol);
        } else {
            // side step move
            move(headRow, headCol, midRow, midCol, rowDest, colDest);
            Field destField = getNeighbor(backRow, backCol, neighborNo);
            assert destField != null;
            move(backRow, backCol, destField.getRow(), destField.getCol());
        }

    }

    /**
     * Check if a move with one marble is valid.
     * 
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowDest    row coordinate of destination.
     * @param colDest    col coordinate of destination.
     * @return
     */
    public boolean isValidMove(char rowLetter1, int colCoord1, char rowDest, int colDest) {
        return isFieldCoord(rowLetter1, colCoord1) && isFieldCoord(rowDest, colDest) // both valid field
                && isColumn(rowLetter1, colCoord1, rowDest, colDest) // are next to each other
                && (getField(rowDest, colDest).getMarble() == null // have marbles
                        && getField(rowLetter1, colCoord1).getMarble() != null);
    }

    /**
     * Check if a move with 2 marbles is valid. Other board classes should check if
     * the colors are correct.
     * 
     * @requires the marbles to be correct colors
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @param rowDest    row coordinate of destination.
     * @param colDest    the column to move into.
     * @return
     */
    public boolean isValidMove(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2, char rowDest,
            int colDest) {
        // select correct head
        char headRow = rowLetter1;
        int headCol = colCoord1;
        char backRow = rowLetter2;
        int backCol = colCoord2;
        if (!isColumn(rowLetter1, colCoord1, rowDest, colDest)) {
            headRow = rowLetter2;
            headCol = colCoord2;
            backRow = rowLetter1;
            backCol = colCoord1;
        }
        // valid coordinates
        boolean check1 = isFieldCoord(headRow, headCol) && isFieldCoord(backRow, backCol)
                && isFieldCoord(rowDest, colDest)
                // has marbles
                && getField(headRow, headCol).getMarble() != null && getField(backRow, backCol).getMarble() != null
                // are next to each other
                && isColumn(headRow, headCol, backRow, backCol)
                // head is next to destination
                && (isColumn(headRow, headCol, rowDest, colDest) || isColumn(backRow, backCol, rowDest, colDest));
        if (!check1) {
            return false;
        } else {
            int moveDirection = this.getNeighborNumber(headRow, headCol, rowDest, colDest);
            if (isColumn(backRow, backCol, headRow, headCol, rowDest, colDest)) { // in line move
                if (getField(rowDest, colDest).getMarble() == null) {
                    return true; // valid if not pushing
                }
                Field pushedInto = getNextInLine(headRow, headCol, rowDest, colDest);
                if (pushedInto != null) {
                    return pushedInto.getMarble() == null;
                }
            } else { // side step move
                Field sideStepNeighbor = this.getNeighbor(backRow, backCol, moveDirection);

                return getNeighbor(headRow, headCol, moveDirection) != null
                        && getNeighbor(headRow, headCol, moveDirection).getMarble() == null
                        && getField(rowDest, colDest).getMarble() == null && sideStepNeighbor != null
                        && isValidMove(backRow, backCol, sideStepNeighbor.getRow(), sideStepNeighbor.getCol());
            }

        }
        return true;
    }

    /**
     * Check if a move with 3 marbles is valid. Does not check if the marbles being
     * pushed are the same color. Checks if the destination marble is a different
     * color.
     * 
     * @param rowLetter1 row coordinate 1.
     * @param colCoord1  column coordinate 1.
     * @param rowLetter2 row coordinate 2.
     * @param colCoord2  column coordinate 2.
     * @param rowLetter3 row coordinate 3.
     * @param colCoord3  column coordinate 3.
     * @param rowDest    row coordinate of destination.
     * @param colDest    the column to move into.
     * @return
     */
    public boolean isValidMove(char rowLetter1, int colCoord1, char rowLetter2, int colCoord2, char rowLetter3,
            int colCoord3, char rowDest, int colDest) {
        // select correct head
        char headRow = rowLetter1;
        int headCol = colCoord1;
        char backRow = rowLetter3;
        int backCol = colCoord3;
        if (!isColumn(rowLetter1, colCoord1, rowDest, colDest)) {
            headRow = rowLetter3;
            headCol = colCoord3;
            backRow = rowLetter1;
            backCol = colCoord1;
        }
        // valid coordinates
        boolean check1 = isFieldCoord(headRow, headCol) && isFieldCoord(backRow, colCoord2)
                && isFieldCoord(rowLetter3, backCol) && isFieldCoord(rowDest, colDest)
                // has marbles
                && getField(headRow, headCol).getMarble() != null && getField(rowLetter2, colCoord2).getMarble() != null
                && getField(backRow, backCol).getMarble() != null
                // are next to each other
                && isColumn(headRow, headCol, rowLetter2, colCoord2, backRow, backCol)
                // head is next to destination
                && (isColumn(headRow, headCol, rowDest, colDest) || isColumn(backRow, backCol, rowDest, colDest));
        if (!check1) {
            return false;
        } else {
            int moveDirection = this.getNeighborNumber(headRow, headCol, rowDest, colDest);
            if (isColumn(rowLetter2, colCoord2, headRow, headCol, rowDest, colDest)) { // in line move
                Field dest2 = getNextInLine(headRow, headCol, rowDest, colDest);
                if (dest2 != null) { // not pushing one marble off
                    // marble color should not be same
                    if (getField(rowDest, colDest).getMarble() != null && dest2.getMarble() != null) {
                        // pushing 2 marbles
                        Field dest3 = getNextInLine(rowDest, colDest, dest2.getRow(), dest2.getCol());
                        // last field being affected is either not a field or has no marble
                        return dest3 == null || dest3.getMarble() == null;
                    } else { // pushing one marble or not pushing any marbles
                        return isValidMove(headRow, headCol, rowLetter2, colCoord2, rowDest, colDest);
                    }
                }
            } else { // side step move
                Field sideStepNeighbor = this.getNeighbor(rowLetter2, colCoord2, moveDirection);
                return getNeighbor(rowLetter1, colCoord1, moveDirection) != null
                        && getNeighbor(rowLetter1, colCoord1, moveDirection).getMarble() == null
                        && getNeighbor(rowLetter3, colCoord3, moveDirection) != null
                        && getNeighbor(rowLetter3, colCoord3, moveDirection).getMarble() == null
                        && getField(rowDest, colDest).getMarble() == null && sideStepNeighbor != null
                        && sideStepNeighbor.getMarble() == null
                        && isValidMove(rowLetter2, colCoord2, sideStepNeighbor.getRow(), sideStepNeighbor.getCol());
            }

        }
        return true;
    }

    /**
     * Get the next field in the same direction: back->head. Get head's next field.
     * 
     * @param rowBack row coordinate of the field in the back.
     * @param colBack column coordinate of the field in the back.
     * @param rowHead row coordinate of the field in the front.
     * @param colHead column coordinate of the field in the front.
     * @return
     */
    protected Field getNextInLine(char rowBack, int colBack, char rowHead, int colHead) {
        return getNeighbor(rowHead, colHead, getNeighborNumber(rowBack, colBack, rowHead, colHead));
    }

    /**
     * Make a move using two fields and direction. If moving one marble, choose same
     * field twice, if moving 3 marble choose the first and the last marble.
     * 
     * @requires moveD to be a valid move
     * @param rowHead   row coordinate 1.
     * @param colHead   column coordinate 1.
     * @param rowTail   row coordinate 2.
     * @param colTail   column coordinate 2.
     * @param direction 0 is top left, goes clockwise, 5 is left
     */
    public void moveD(char rowHead, int colHead, char rowTail, int colTail, int direction) {
        Field head = getField(rowHead, colHead);
        Field tail = getField(rowTail, colTail);
        Field dest = getNeighbor(rowHead, colHead, direction);
        if (tail.equals(dest)) { // direction pointing at itself
            moveD(rowTail, colTail, rowHead, colHead, direction); // swap head and tail
            return;
        }
        if (head.equals(tail)) { // moving one marble
            move(rowHead, colHead, dest.getRow(), dest.getCol());
            return;
        } else if (isColumn(rowHead, colHead, rowTail, colTail)) { // moving two marbles
            move(rowHead, colHead, rowTail, colTail, dest.getRow(), dest.getCol());
            return;
        } else { // moving 3 marbles
            Field mid = getMidOf2(rowHead, colHead, rowTail, colTail);
            if (mid.equals(dest)) { // direction pointing at itself
                moveD(rowTail, colTail, rowHead, colHead, direction); // swap head and tail
                return;
            }
            move(rowHead, colHead, // head
                    mid.getRow(), mid.getCol(), // tail
                    tail.getRow(), tail.getCol(), // tail
                    dest.getRow(), // dest
                    dest.getCol());
            return;
        }
    }

    /**
     * Make move with direction using a move object.
     * 
     * @requires move to a valid move.
     * @param move move to be performed
     */
    public void moveD(Move move) {
        moveD(move.getHead().getRow(), move.getHead().getCol(), // head
                move.getTail().getRow(), move.getTail().getCol(), // tail
                move.getDirection()); // direction
    }

    /**
     * Check if a move is valid with direction and 2 fields.
     * 
     * @param rowHead   row coordinate 1.
     * @param colHead   column coordinate 1.
     * @param rowTail   row coordinate 2.
     * @param colTail   column coordinate 2.
     * @param direction 0 is top left, goes clockwise, 5 is left
     * @return true if it is a valid move.
     */
    public boolean isValidMoveD(char rowHead, int colHead, char rowTail, int colTail, int direction) {
        if (!isFieldCoord(rowHead, colHead) || !isFieldCoord(rowTail, colTail)) {
            return false;
        }
        Field head = getField(rowHead, colHead);
        Field tail = getField(rowTail, colTail);
        Field dest = getNeighbor(rowHead, colHead, direction); // can be null
        if (dest == null) {
            return false;
        }
        if (head.equals(tail)) {
            // moving one marble
            if (!isValidMove(rowHead, colHead, dest.getRow(), dest.getCol())) {
                return false;
            }
        } else if (isColumn(rowHead, colHead, rowTail, colTail)) { // moving two marbles
            if (tail.equals(dest)) { // direction is back on itself
                return (isValidMoveD(rowTail, colTail, rowHead, colHead, direction));
            }
            if (!isValidMove(rowHead, colHead, rowTail, colTail, dest.getRow(), dest.getCol())) {
                return false;
            }
        } else { // moving 3 marbles
            Field mid = getMidOf2(rowHead, colHead, rowTail, colTail); // get the marble in the middle
            if (dest.equals(mid)) { // direction is back on itself
                return (isValidMoveD(rowTail, colTail, rowHead, colHead, direction));
            }
            if (!(mid != null && isValidMove(rowHead, colHead, mid.getRow(), mid.getCol(), rowTail, colTail,
                    dest.getRow(), dest.getCol()))) {
                return false;
            }
        }
        // Color and team check
        Colour headColour = head.getMarble().getColour();
        Colour tailColour = tail.getMarble().getColour();
        if (!head.equals(tail) && !isColumn(rowHead, colHead, rowTail, colTail)) {
            // moving 3 marbles
            Colour midColour = getMidOf2(rowHead, colHead, rowTail, colTail).getMarble().getColour();
            if (!(isOnSameTeam(midColour, headColour))) {
                return false; // team of the middle should be the same as the head
            }
        }
        if (dest.getMarble() != null) { // if sumito
            Colour destColour = dest.getMarble().getColour();
            if (isOnSameTeam(tailColour, destColour)) { // can't push own team's marble
                return false;
            }
            Field dest2 = getNextInLine(rowHead, colHead, dest.getRow(), dest.getCol());
            if (dest2 != null && dest2.getMarble() != null) { // pushing two marbles off:
                // can't push own team's marble
                if (this.getTeam(dest2.getMarble().getColour()).equals(this.getTeam(headColour))) {
                    return false;
                }
            }
        }
        return isOnSameTeam(tailColour, headColour); // head and tail should be on the same team
    }

    /**
     * Check if a move is valid with a Move object.
     * 
     * @param move the move to be performed.
     * @return true if it is a valid move.
     */
    public boolean isValidMoveD(Move move) {
        return isValidMoveD(move.getHead().getRow(), move.getHead().getCol(), // head
                move.getTail().getRow(), move.getTail().getCol(), // row
                move.getDirection()); // direction
    }

    /**
     * Check if a move is valid for the color making the move. Should override in
     * BoardFor4 as anyone from the team can make side step moves.
     * 
     * @param move   Move to be checked.
     * @param colour Color to be checked.
     * @return
     */
    public boolean isValidMoveDForColour(Move move, Colour colour) {
        if (!isValidMoveD(move)) {
            return false;
        }
        // head should be the same color as the one making the move
        return colour == move.getHead(this).getMarble().getColour();
    }

    /**
     * Get the field in the middle of two other fields.
     * 
     * @requires
     * @param row1 row coordinate 1.
     * @param col1 column coordinate 1.
     * @param row2 row coordinate 2.
     * @param col2 column coordinate 2.
     * @return
     */
    protected Field getMidOf2(char row1, int col1, char row2, int col2) {
        Field mid = null;
        for (int i = 0; i < 6; i++) {
            Field neighborOpp = getNeighbor(row2, col2, (i + 3) % 6);
            if (neighborOpp != null && neighborOpp.equals(getNeighbor(row1, col1, i))) {
                mid = getNeighbor(row1, col1, i);
            }
        }
        return mid;
    }

    /**
     * Return the number(0-5) of the neighbor.
     * 
     * @param headRow     row coordinate of main field.
     * @param headCol     head coordinate of main field.
     * @param rowNeighbor row coordinate of the neighbor.
     * @param colNeighbor column coordinate of the neighbor.
     * @assert neighborNo >= 0 && neighborNo < 6
     * @return the neighbor number.
     */
    private int getNeighborNumber(char headRow, int headCol, char rowNeighbor, int colNeighbor) {
        int neighborNo = -1;
        Field[] neighbors = this.getNeighbors(headRow, headCol);
        for (int i = 0; i < neighbors.length; i++) {
            if ((getField(rowNeighbor, colNeighbor).equals(neighbors[i]))) {
                neighborNo = i;
            }
        }
        assert (neighborNo != -1);
        return neighborNo;
    }

    /**
     * Get the team of a player given a color.
     * 
     * @param colour of the player to be checked.
     * @return team of this player.
     */
    public Team getTeam(Colour colour) {
        Team team = null;
        for (Team t : teams) {
            for (Colour c : t.getColours()) {
                if (c.equals(colour)) {
                    team = t;
                }
            }
        }
        return team;

    }

    /**
     * Get the teams that are not the given team.
     * 
     * @param team team that is excluded.
     * @return teams that are not the given team.
     */
    public ArrayList<Team> getOtherTeams(Team team) {
        ArrayList<Team> ts = new ArrayList<>();
        for (Team t : teams) {
            if (!(t.equals(team))) { // if not same team
                ts.add(t);
            }
        }
        return ts;
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }

    /**
     * Check if two colors are on the same team.
     * 
     * @param c1 color 1.
     * @param c2 color 2.
     * @return
     */
    protected boolean isOnSameTeam(Colour c1, Colour c2) {
        return getTeam(c1).equals(getTeam(c2));
    }

    /**
     * Get the winner team.
     * 
     * @return
     */
    public Team getWinnerTeam() {
        Team winner = null;
        for (Team t : teams) {
            if (t.getScore() >= LOSERMARBLES) {
                winner = t;
            }
        }
        return winner;
    }

    /**
     * Get the team with the most points.
     * 
     * @return
     */
    public Team getMostPointTeam() {
        Team winner = null;
        int maxScore = -1;
        for (Team t : teams) {
            if (t.getScore() > maxScore) {
                maxScore = t.getScore();
                winner = t;
            }
        }
        int teamCount = 0;
        for (Team t : teams) { // if multiple teams with max points return nul
            if (t.getScore() == maxScore) {
                maxScore = t.getScore();
                teamCount++;
            }
        }
        if (teamCount > 1) {
            return null;
        }
        return winner;
    }

    /**
     * Check if the board has a winner.
     * 
     * @return true if there is a winner.
     */
    public boolean hasWinner() {
        if (getWinnerTeam() == null) {
            return false;
        }
        return true;

    }

    /**
     * Check if a given color is in the team that has won.
     * 
     * @param colour to be checked.
     * @return true if the color is in the winner team.
     */
    public boolean isWinner(Colour colour) {
        return (getTeam(colour).equals(getWinnerTeam()));
    }

    /**
     * Show all fields' coordinates as text to help the user.
     * 
     * @return all fields' coordinates in a string representation.
     */
    public String coordHelp() {
        String result = "";
        for (int i = 0; i < fields.length; i++) { // for every row
            String frontSpace = " ".repeat(DIM - fields[i].length);
            String rowResult = frontSpace + (char) (fields.length - i + 64) + " "; // add letter row representation
            for (int j = 0; j < fields[i].length; j++) { // for every column in row
                rowResult += (fields[i][j].toString().charAt(2) + " "); // char at 2 is the column number
            }
            result += String.format("%5s", rowResult + "\n");
        }
        // number representation
        result += "Out:" + Arrays.toString(outerRim.toArray());
        return result;
    }

    /**
     * Show the string representation of the board.
     */
    @Override
    public String toString() {
        String result = "\n"; // start on new line
        // add col numbers top
        result += " ".repeat(SIDE_LENGTH - 1);
        for (int i = 0; i < SIDE_LENGTH; i++) { // add col numbers
            result += (SIDE_LENGTH - 1 + i) + " ";
        }
        result += "\n" + " ".repeat(SIDE_LENGTH) + "↘ ".repeat(SIDE_LENGTH);
        result += "\n";
        // add rows and fields
        for (int i = 0; i < fields.length; i++) { // for every row
            String frontSpace = " ".repeat(DIM - fields[i].length);
            String rowResult = frontSpace + (char) (fields.length - i + 64) + " "; // add letter row representation
            for (int j = 0; j < fields[i].length; j++) { // for every column in row
                // get the first char(which has color characters) from string representation
                rowResult += (fields[i][j].toString().charAt(0) + " ");
            }
            result += String.format("%5s", rowResult + "\n");
        }
        // add col numbers bottom
        result += " ".repeat(SIDE_LENGTH + 2);
        for (int i = 0; i < SIDE_LENGTH; i++) { // add col numbers
            result += (i) + " ";
        }
        result += "\n";
        // add number representation
        result += "Out:" + Arrays.toString(outerRim.toArray());
        return result;
    }

}
