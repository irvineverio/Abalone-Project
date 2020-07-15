package abalone;

/**
 * Represents the teams in a board.
 * 
 * @author Berke Guducu
 *
 */
public class Team {
    private Colour[] colours;
    private int score;

    /**
     * Construct team for one with given color.
     * 
     */
    public Team(Colour colour) {
        colours = new Colour[1];
        colours[0] = colour;
        score = 0;
    }

    /**
     * Construct team for two with given colors.
     * 
     */
    public Team(Colour colour1, Colour colour2) {
        colours = new Colour[2];
        colours[0] = colour1;
        colours[1] = colour2;
        score = 0;
    }

    /**
     * Team constructor with color array.
     * 
     * @param colour array of Colours
     */
    public Team(Colour[] colour) {
        this.colours = colour;
        score = 0;
    }

    public Colour[] getColours() {
        return this.colours;
    }

    public void setColours(Colour[] colours) {
        this.colours = colours;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public void addScore() {
        this.score++;
    }
}
