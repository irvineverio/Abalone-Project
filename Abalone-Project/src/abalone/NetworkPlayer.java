package abalone;

/**
 * Network player that does not require user input and is only used to hold the
 * name and color.
 *
 */
public class NetworkPlayer extends Player {

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new network player object.
     * 
     * @requires name is not null
     * @requires color of the marble is not null
     * @ensures the Name of this player will be name
     * @ensures the Colour of marble of this player will be marble's colour
     */
    public NetworkPlayer(String name, Colour colour) {
        super(name, colour);
    }

    // -- Commands ---------------------------------------------------

    @Override
    public Move determineMove(Board board) {
        return null;
    }
}
