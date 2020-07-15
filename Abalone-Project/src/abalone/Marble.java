package abalone;

/**
 * Represents a marble in a board.
 * 
 * @author Berke Guducu
 *
 */
public class Marble {
    private Colour colour;

    public Marble(Colour color) {
        this.colour = color;
    }

    public Colour getColour() {
        return this.colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Marble deepCopy() {
        return new Marble(this.colour);
    }

    @Override
    public String toString() {
        return this.colour.toString();
    }
}
