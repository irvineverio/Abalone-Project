package abalone;

/**
 * Holds field's row, col, and marble.
 * 
 * @author Berke Guducu
 *
 */
public class Field {
    private Marble marble = null;
    private char row = ' ';
    private int col = -1;

    public Field(Marble marble) {
        this.marble = marble;
    }

    public Field(char rowChar, int col) {
        this.row = rowChar;
        this.col = col;
    }

    public char getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Marble getMarble() {
        return this.marble;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    /**
     * Create a deep copy of the board.
     * 
     * @return
     */
    public Field deepCopy() {
        Field copy = new Field(this.row, this.col);
        if (this.marble == null) {
            copy.setMarble(null);
            return copy;
        }
        copy.setMarble(this.marble.deepCopy());
        return copy;
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof Field)) {
            return false;
        }
        return ((Field) ob).getRow() == this.row // same row
                && ((Field) ob).getCol() == this.col; // same column

    }

    @Override
    public String toString() {
        if (marble == null) {
            return "+" + this.row + this.col;
        }
        return this.marble.toString() + this.row + this.col;
    }

    public void setRowAndCol(char row, int col) {
        this.row = row;
        this.col = col;
    }
}
