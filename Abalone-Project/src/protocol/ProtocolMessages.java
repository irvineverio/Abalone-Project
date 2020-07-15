package protocol;

/**
 * Protocol for Networked Abalone Application.
 * 
 * @author Wim Kamerman & Irvine & Berke
 */
public class ProtocolMessages {

    /**
     * Delimiter used to separate arguments sent over the network.
     */
    public static final String DELIMITER = ";";

    /**
     * Separator used to separate 2 parameters in an argument (CSV) over the
     * network.
     */
    public static final String SEPARATOR = ",";

    /**
     * The following integers are used for the server-client communication to show
     * what commands went wrong, or went right.
     */
    public static final int SUCCESS = 200;
    public static final int MALFORMED_COMMAND = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    /**
     * The following Strings are both used by the TUI to receive user input, and the
     * server and client to distinguish messages.
     */
    public static final String CREATE_LOBBY = "CREATE_LOBBY";
    public static final String LIST_LOBBY = "LIST_LOBBY";
    public static final String JOIN_LOBBY = "JOIN_LOBBY";
    public static final String LEAVE_LOBBY = "LEAVE_LOBBY";
    public static final String READY_LOBBY = "READY_LOBBY";
    public static final String UNREADY_LOBBY = "UNREADY_LOBBY";
    public static final String LOBBY_CHANGE = "LOBBY_CHANGE";
    public static final String CONNECT = "CONNECT";
    public static final String GAME_START = "GAME_START";
    public static final String MOVE = "MOVE";
    public static final String GAME_FINISH = "GAME_FINISH";
    public static final String PLAYER_DEFEAT = "PLAYER_DEFEAT";
    public static final String FORFEIT = "FORFEIT";

    // only client not in server
    public static final String EXIT = "EXIT";
    // Extension for bonus
    // public static final String LIST_PLAYERS = "LIST_PLAYERS";
    // public static final String CHALLENGE = "CHALLENGE";
    // public static final String PM = "PM";
    public static final String LOBBY_MSG = "LOBBY_MSG";
    public static final String MSG_RECV = "MSG_RECV";
    // public static final String PM_RECV = "PM_RECV";
    // public static final String LEADERBOARD = "LEADERBOARD";

}
