package protocol;

import abalone.Move;
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;

/**
 * Defines the methods that the Abalone Client should support.
 * 
 * @author Wim Kamerman & Berke Guducu
 */
public interface ClientProtocol {

    /**
     * Handles the following server-client handshake: 1. Client sends
     * ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + USERNAME to server 2.
     * Server returns one line containing ProtocolMessages.CONNECT +
     * ProtocolMessages.DELIMITER + ProtocolMessages.SUCCESS <br>
     * This method sends the CONNECT message and checks whether the server response
     * is valid (must contain CONNECT and ProtocolMessages.SUCCESS). - If the
     * response does not contain connect, this method throws a ProtocolException. If
     * the response does not contain success another user name must be tried - If
     * the response is valid, a welcome message including the is forwarded to the
     * view.
     * 
     * @throws ServerUnavailableException if IO errors occur.
     * @throws ProtocolException          if the server response is invalid.
     */
    public void handleHello() throws ServerUnavailableException, ProtocolException;

    /**
     * Send a message to create a lobby in the server. Send
     * ProtocolMessages.CREATE_LOBBY + ProtocolMessages.DELIMITER + NAME +
     * ProtocolMessages.DELIMITER + SIZE + ProtocolMessages.DELIMITER to the server.
     * 
     * @param name name of the lobby to create.
     * @param size the size of the lobby to be created.
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void createLobby(String name, int size) throws ServerUnavailableException;

    /**
     * Send a message to the server to list all the lobbies. Send
     * ProtocolMessages.LIST_LOBBY to the server.
     * 
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void listLobby() throws ServerUnavailableException;

    /**
     * Send a message to join a lobby with the give name to the server.Send
     * ProtocolMessages.JOIN_LOBBY to the server.
     * 
     * @param name of the lobby to join.
     * @throws ServerUnavailableException if the message fails be sent to the server.
     */
    public void joinLobby(String name) throws ServerUnavailableException;

    /**
     * Send a leave lobby message to leave the lobby you are in. Send
     * ProtocolMessages.LEAVE_LOBBY to the server.
     * 
     * @throws ServerUnavailableException if the message fails be sent to the server.
     */
    public void leaveLobby() throws ServerUnavailableException;

    /**
     * Send a ready lobby message to ready yourself in the lobby you are in. Send
     * ProtocolMessages.READY_LOBBY to the server.
     * 
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void readyLobby() throws ServerUnavailableException;

    /**
     * Send a unReady lobby message to unReady yourself in the lobby you are in.
     * Send ProtocolMessages.UNREADY_LOBBY to the server.
     * 
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void unreadyLobby() throws ServerUnavailableException;

    /**
     * Send a move to the server. Send ProtocolMessages.MOVE +
     * ProtocolMessages.DELIMITER + MOVE(in the protocol move format: col,row;
     * col,row; direction)) to the server.
     * 
     * @param move to send to the server
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void move(Move move) throws ServerUnavailableException;

    /**
     * Forfeit the current game.
     * 
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void forfeit() throws ServerUnavailableException;

    // Extras
    // public void listPlayers() throws ServerUnavailableException;
    // public void challenge(String name) throws ServerUnavailableException;
    // public void challangeAccept(String name) throws ServerUnavailableException;
    // public void PM() throws ServerUnavailableException;
    // public void leaderBoard() throws ServerUnavailableException;

    /**
     * Send a message in the lobby you are currently in.
     * 
     * @param msg msg to be sent.
     * @throws ServerUnavailableException if the message fails be sent to the
     *                                    server.
     */
    public void lobbyMsg(String msg) throws ServerUnavailableException;
}
