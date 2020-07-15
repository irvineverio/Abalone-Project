package server;

/**
 * Represents a user in the server.
 * 
 * @author Berke Guducu
 *
 */
public class User {
    private String name;
    private boolean ready;
    private AbaloneClientHandler handler;
    // private int score;

    User(String name, AbaloneClientHandler handler) {
        this.name = name;
        this.handler = handler;
        ready = false;
    }

    public boolean getReady() {
        return ready;
    }

    public void setReady(boolean isReady) {
        ready = isReady;
    }

    public String getName() {
        return name;
    }

    public AbaloneClientHandler getHandler() {
        return handler;
    }
}
