package exceptions;

/**
 * Move format exception to be thrown when a move to be formatted is in wrong
 * format.
 * 
 * @author berke
 *
 */
public class MoveFormatException extends Exception {

    private static final long serialVersionUID = 6910118687575460679L;

    public MoveFormatException(String msg) {
        super(msg);
    }

}
