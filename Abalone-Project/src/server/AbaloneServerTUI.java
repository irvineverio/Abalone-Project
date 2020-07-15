package server;

import java.io.PrintWriter;

import utils.TextIO;

/**
 * Hotel Server TUI for user input and user messages.
 * 
 * @author Wim Kamerman
 */
public class AbaloneServerTUI implements AbaloneServerView {

    /** The PrintWriter to write messages to. */
    private PrintWriter console;

    /**
     * Constructs a new HotelServerTUI. Initializes the console.
     */
    public AbaloneServerTUI() {
        console = new PrintWriter(System.out, true);
    }

    @Override
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public String getString(String question) {
        // To be implemented
        showMessage(question);
        String answer = TextIO.getln();
        return answer;
    }

    @Override
    public int getInt(String question) {
        // To be implemented
        showMessage(question);
        int answer = TextIO.getInt();
        return answer;
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        boolean answer = TextIO.getlnBoolean(); 
        return answer;
    }

}
