package protocol;

import abalone.Move;
import server.User;

/**
 * Defines the methods that the Abalone Server should support. The results
 * should be returned to the client.
 * 
 * @author Berke
 */
public interface ServerProtocol {

    /**
     * Returns a String to be sent as a response to a Client CONNECT request,
     * ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER +
     * ProtocolMessages.SUCCESS + ProtocolMessages.DELIMITER;
     * 
     * @return String to be sent to client as a handshake response.
     */
    public String getHello(User user);

    /**
     * Create a lobby with the given name.
     * 
     * @requires guestName != null
     * @param lobbyName name of the lobby to be created.
     * @param size      the size of the lobby to be created (2-4)
     * @return textual result, to be shown to the user
     */
    public String createLobby(String lobbyName, int size);

    /**
     * List the available lobbies.
     * 
     * @return textual result, to be shown to the user
     */
    public String listLobby();

    /**
     * Given the name of a guest, the corresponding room is returned. The result is
     * returned as String and can be: - Parameter is wrong (guestName is null) -
     * Guest does not have a room - Guest has room + room number
     * 
     * @requires guestName != null
     * @param lobbyName  Name of the lobby.
     * @param playerName User to join the lobby.
     * @return textual result, to be shown to the user
     */
    public String joinLobby(String lobbyName, User playerName);

    /**
     * Leaves the lobby you are in.
     * 
     * @requires guestName != null
     * @param playerName Name of the guest.
     * @return textual protocol answer to be sent back to the client.
     */
    public String leaveLobby(User playerName);

    /**
     * Ready a lobby.
     * 
     * @param user that is setting them ready.
     * @return textual protocol answer.
     */
    public String readyLobby(User user);

    /**
     * Un-ready a user in lobby.
     * 
     * @param user that is unreadies.
     * @return textual protocol answer.
     */
    public String unreadyLobby(User user);

    /**
     * Make a move with the given user.
     * 
     * @param move to make.
     * @param user making the move.
     * @return protocol answer.
     */
    public String move(Move move, User user);

    /**
     * Forfeit a user.
     * 
     * @param user that forfeits.
     * @return protocol answer.
     */
    public String forfeit(User user);

    /**
     * Send a message in the lobby.
     * 
     * @param user sending the message.
     * @param msg  msg to send.
     * @return protocol answer to send back to client.
     */
    public String lobbyMsg(User user, String msg);

}
