package server;

import abalone.Move;
import exceptions.ExitProgram;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import protocol.ProtocolMessages;
import protocol.ServerProtocol;

/**
 * Server TUI for Networked Abalone Application. Intended Functionality:
 * interactively set up & monitor a new server
 * 
 * @author Wim Kamerman & Berke Guducu
 */
public class AbaloneServer implements Runnable, ServerProtocol {

    private static final String DELI = ProtocolMessages.DELIMITER;

    private static final String UNAUTH = DELI + ProtocolMessages.UNAUTHORIZED;

    private static final String SUC = DELI + ProtocolMessages.SUCCESS;

    /** The ServerSocket of this HotelServer. */
    private ServerSocket ssock;

    /** List of HotelClientHandlers, one for each connected client. */
    private List<AbaloneClientHandler> clients;

    /** Next client number, increasing for every new connection. */
    private int nextClientNo;

    /** The view of this HotelServer. */
    private AbaloneServerTUI view;

    private List<Lobby> lobbies;

    /** The users in this server. */
    private List<User> users;

    /**
     * Constructs a new AbaloneServer. Initializes the clients list, the view, the
     * next_client_no and users.
     */
    public AbaloneServer() {
        clients = new ArrayList<>();
        view = new AbaloneServerTUI();
        lobbies = new ArrayList<Lobby>();
        users = new ArrayList<User>();
        nextClientNo = 1;
    }

    /**
     * Opens a new socket by calling {@link #setup()} and starts a new
     * AbaloneClientHandler for every connecting client.<br>
     * . If {@link #setup()} throws a ExitProgram exception, stop the program. In
     * case of any other errors, ask the user whether the setup should be ran again
     * to open a new socket.
     */
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the abalone application
                setup();

                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client " + String.format("%02d", nextClientNo++);
                    view.showMessage("New client [" + name + "] connected!");
                    AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }

            } catch (ExitProgram e1) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: " + e.getMessage());

                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    /**
     * Opens a new ServerSocket at localhost on a user-defined port. <br>
     * The user is asked to input a port, after which a socket is attempted to be
     * opened. If the attempt succeeds, the method ends, If the attempt fails, the
     * user decides to try again, after which an ExitProgram exception is thrown or
     * a new port is entered.
     * 
     * @throws ExitProgram if a connection can not be created on the given port and
     *                     the user decides to exit the program.
     * @ensures a serverSocket is opened.
     */
    public void setup() throws ExitProgram {
        ssock = null;
        while (ssock == null) {
            int port = view.getInt("Please enter the server port.");
            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
                ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException | IllegalArgumentException e) {
                view.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");

                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the " + "program.");
                }
                setup();
            }
        }

    }

    /**
     * Removes a clientHandler from the client list.Also removes the user from its
     * lobby.
     * 
     * @requires client != null
     */
    public void removeClient(AbaloneClientHandler client) {
        this.clients.remove(client);
    }

    // ------------------ Server Methods --------------------------

    @Override
    public String getHello(User user) { // add a user to the server
        // can't have multiple names in the server
        for (User u : users) {
            // is name exists in the server or not a valid name
            if (u.getName().equals(user.getName()) || !isValidName(u.getName())) {
                return ProtocolMessages.CONNECT + UNAUTH;
            }
        }
        users.add(user);
        return ProtocolMessages.CONNECT + SUC;
        // To be implemented
    }

    /**
     * Checks if a name is valid to be used in the server.
     * 
     * @param name to be checked.
     * @return
     */
    private boolean isValidName(String name) {
        return !(name.contains(ProtocolMessages.DELIMITER)) // should not contain delimiter
                && !name.contains(ProtocolMessages.SUCCESS + ""); // should not contain success code
    }

    @Override
    public synchronized String createLobby(String name, int size) {
        for (Lobby l : lobbies) {
            if (l.getName().equals(name)) { // lobby with name already exists
                return ProtocolMessages.CREATE_LOBBY + UNAUTH;
            }
        }
        lobbies.add(new Lobby(name, size));
        return ProtocolMessages.CREATE_LOBBY + SUC;
    }

    @Override
    public synchronized String listLobby() {
        String lobbyList = "";
        for (Lobby lobby : lobbies) {
            String lobbyName = lobby.getName();
            int lobbyCapacity = lobby.getSize();
            int numberOfUsers = lobby.getUsers().size();
            lobbyList += ProtocolMessages.DELIMITER + lobbyName + ProtocolMessages.SEPARATOR + lobbyCapacity
                    + ProtocolMessages.SEPARATOR + numberOfUsers;
        }

        return ProtocolMessages.LIST_LOBBY + SUC + lobbyList;
    }

    @Override
    public synchronized String joinLobby(String lobbyName, User user) {
        Lobby joinLobby = null;
        for (Lobby lobby : lobbies) {
            joinLobby = lobby;
        }
        if (joinLobby == null) { // lobby must exist
            return ProtocolMessages.JOIN_LOBBY + UNAUTH;
        }
        if (getUserLobby(user) != null) { // user already in a lobby
            return ProtocolMessages.JOIN_LOBBY + UNAUTH;
        }
        if (joinLobby.addUser(user)) { // add user to lobby
            return ProtocolMessages.JOIN_LOBBY + SUC;
        }
        return ProtocolMessages.JOIN_LOBBY + UNAUTH;
    }

    @Override
    public synchronized String leaveLobby(User user) {
        Lobby leaveLobby = getUserLobby(user);
        if (leaveLobby == null) { // if user not in a lobby
            return ProtocolMessages.LEAVE_LOBBY + UNAUTH;
        }
        if (leaveLobby.removeUser(user)) { // remove user from lobby
            // lobby sends change to other players when a user is removed
            return ProtocolMessages.LEAVE_LOBBY + SUC;
        }
        return ProtocolMessages.LEAVE_LOBBY + UNAUTH;

    }

    @Override
    public synchronized String readyLobby(User user) { // requires player to be in a lobby
        // get user's lobby'
        Lobby userLobby = getUserLobby(user);
        if (userLobby == null) { // user not found in a lobby
            return ProtocolMessages.READY_LOBBY + UNAUTH;
        }
        userLobby.setReadyUser(user, true);
        // get number of ready users to send as response
        return ProtocolMessages.READY_LOBBY + DELI + userLobby.getReadyNumber();
    }

    @Override
    public synchronized String unreadyLobby(User user) { // requires player to be in a lobby
        Lobby userLobby = getUserLobby(user);
        if (userLobby == null) {
            return ProtocolMessages.UNREADY_LOBBY + UNAUTH;
        }
        userLobby.setReadyUser(user, false);
        return ProtocolMessages.UNREADY_LOBBY + DELI + userLobby.getReadyNumber();
    }

    @Override
    public synchronized String move(Move move, User user) {
        Lobby userLobby = getUserLobby(user);
        if (userLobby == null) {
            return ProtocolMessages.MOVE + UNAUTH;
        }
        if (userLobby.makeMove(move, user)) {
            userLobby.echoMove(move, user);
            System.out.println(userLobby.getGame().getBoard().toString());
            return ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + user.getName() + ProtocolMessages.DELIMITER
                    + move.protocolFormat(); // don't return suc. instead send user name and move.
        }
        return ProtocolMessages.MOVE + DELI + ProtocolMessages.FORBIDDEN;
    }

    @Override
    public synchronized String forfeit(User user) {
        Lobby userLobby = getUserLobby(user);
        if (userLobby == null) {
            return ProtocolMessages.FORFEIT + UNAUTH;
        }
        if (userLobby.forfeitUser(user)) {
            return ProtocolMessages.FORFEIT + SUC;
        }
        return ProtocolMessages.FORFEIT + UNAUTH;
    }

    // -----Extras------------
    // **
    /**
     * Send a message in the lobby.
     * 
     * @param user sending the message.
     * @param msg  msg to send.
     * @return
     */
    public synchronized String lobbyMsg(User user, String msg) {
        Lobby userLobby = getUserLobby(user);
        if (userLobby == null) {
            return ProtocolMessages.LOBBY_MSG + UNAUTH;
        }
        userLobby.echoExcept(ProtocolMessages.MSG_RECV + DELI + msg + DELI + user.getName(), user);
        return ProtocolMessages.LOBBY_MSG + SUC;
    }

    // -------------------Private Methods-----------------
    /**
     * get the lobby of given user.
     * 
     * @param user to get the lobby of.
     * @return
     */
    private Lobby getUserLobby(User user) {
        Lobby userLobby = null;
        for (Lobby lobby : lobbies) { // get all lobbies
            for (User u : lobby.getUsers()) { // get players in lobbies
                if (u.equals(user)) {
                    userLobby = lobby;
                }
            }
        }
        return userLobby;
    }

    // ------------------ Main --------------------------

    /** Start a new AbaloneServer. */
    public static void main(String[] args) {
        AbaloneServer server = new AbaloneServer();
        System.out.println("Welcome to the Abalone Server! Starting...");
        new Thread(server).start();
    }

}
