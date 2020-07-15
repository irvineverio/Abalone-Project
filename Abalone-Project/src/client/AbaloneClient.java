package client;

import abalone.Move;
import exceptions.ExitProgram;
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import protocol.ClientProtocol;
import protocol.ProtocolMessages;

/**
 * Client for Networked Abalone Application.
 * 
 * @author Wim Kamerman & Berke Guducu
 */
public class AbaloneClient implements ClientProtocol {

    private static final String DELI = ProtocolMessages.DELIMITER;
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;

    /** The view of this Abalone Client. */
    private AbaloneClientTUI view;

    /** The server handler of this Client. */
    private AbaloneServerHandler handler;

    // /** The abalone game of the Client. */
    // private Game game;

    /** The user name registered in the server for this player. */
    private String userName;

    /**
     * Constructs a new AbaloneClient. Initializes the view.
     */
    public AbaloneClient() {
        view = new AbaloneClientTUI(this);
    }

    /**
     * Starts a new AbaloneClient by creating a connection, followed by the HELLO
     * handshake as defined in the protocol. After a successful connection and
     * handshake, the view is started. The view asks for used input and handles all
     * further calls to methods of this class.<br>
     * When errors occur, or when the user terminates a server connection, the user
     * is asked whether a new connection should be made.
     */
    public void start() {

        // create connection
        try {
            createConnection();
        } catch (ExitProgram e1) {
            e1.printStackTrace();
        }

        // say hello!
        try {
            handleHello();
        } catch (ServerUnavailableException e) { 
            view.showMessage("Connection to server failed, server unavaible.");
            this.closeConnection();
            return;
        } catch (ProtocolException e) {
            this.closeConnection();
            e.printStackTrace();
            return;
        }

        // start server handler to listen to server
        handler = new AbaloneServerHandler(serverSock, this, "server");
        new Thread(handler).start();

        try {
            view.start();
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
        }

        // To be implemented
    }

    /**
     * Creates a connection to the server. Requests the IP and port to connect to at
     * the view (TUI). <br>
     * The method continues to ask for an IP and port and attempts to connect until
     * a connection is established or until the user indicates to exit the program.
     * 
     * @throws ExitProgram if a connection is not established and the user indicates
     *                     to want to exit the program.
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            InetAddress host;
            int port = 8888;
            boolean tryAgain = true;

            // Request ip and port
            while (tryAgain) {
                view.showMessage("Which ip do you want to connect to?");
                host = view.getIp();
                port = view.getInt("Which port do you want to connect to?");

                // try to open a Socket to the server
                try {
                    InetAddress addr = (host);
                    System.out.println("Attempting to connect to " + addr + ":" + port + "...");
                    serverSock = new Socket(addr, port);
                    in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
                    return;
                } catch (IOException e) {
                    System.out.println("ERROR: could not create a socket on " + host + " and port " + port + ".");

                    tryAgain = view.getBoolean("Try to connect again?");
                }
                if (!tryAgain) {
                    throw new ExitProgram("User indicated to exit.");
                }
            }
        }
    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null. Always make sure
     * to close current connections via shutdown() before calling this method!
     */
    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }

    /**
     * Sends a message to the connected server, followed by a new line. The stream
     * is then flushed.
     * 
     * @param msg the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public synchronized void sendMessage(String msg) throws ServerUnavailableException {
        if (out != null) {
            try {
                view.showMessage("sending to server: " + msg);
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                view.showMessage(e.getMessage());
                closeConnection();
                throw new ServerUnavailableException("Could not write " + "to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not write " + "to server.");
        }
    }

    /**
     * Reads and returns one line from the server.
     * 
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readLineFromServer() throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read " + "from server.");
                }
                System.out.println("answer:" + answer);
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read " + "from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read " + "from server.");
        }
    }

    /**
     * Closes the connection by closing the In- and OutputStreams, as well as the
     * serverSocket.
     */
    public void closeConnection() {
        view.showMessage("Closing the connection...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a user name from the user and send hello messages continuously until the
     * user manages to connect(server sends success).
     * 
     * @throws ServerUnavailableException if server connection fails
     * @throws ProtocolException          if server sends an unknown protocol
     *                                    message as response.
     */
    public void handleHello() throws ServerUnavailableException, ProtocolException {
        userName = view.getString("Provide a user name");
        sendMessage((ProtocolMessages.CONNECT) + DELI + userName + DELI);
        String response = readLineFromServer();
        if (response.isBlank() || response.isEmpty()) {
            response = readLineFromServer();
        }
        if (response.contains(ProtocolMessages.CONNECT + DELI + ProtocolMessages.SUCCESS)) {
            writeOut("Welcome to the Abalone Client system");
        } else if (response.contains(ProtocolMessages.CONNECT)) {
            view.showMessage("Hello failed(the user name may be invalid or taken).");
            boolean again = view.getBoolean(" Try again?");
            if (again) {
                handleHello();
                return;
            }
        } else {
            throw new ProtocolException("Invalid hello response from server");
            // To be implemented
        }
    }

    /**
     * Write a message to the view.
     * 
     * @param str message to be written to the view
     */
    public void writeOut(String str) {
        String strP = "> " + str + "\n";
        view.showMessage(strP);

    }

    /**
     * Return the user name of this client in the server.
     * 
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Get the handler of this server.
     */
    public AbaloneServerHandler getHandler() {
        return handler;
    }

    /**
     * This method starts a new AbaloneClient.
     * 
     * @param args not used.
     */
    public static void main(String[] args) {
        (new AbaloneClient()).start();
    }

    // -------------Client Protocol Methods--------------------
    @Override
    public void createLobby(String name, int size) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.CREATE_LOBBY + DELI + name + DELI + size + DELI);
    }

    @Override
    public void listLobby() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.LIST_LOBBY + DELI);
    }

    @Override
    public void joinLobby(String name) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.JOIN_LOBBY + DELI + name + DELI);
    }

    @Override
    public void leaveLobby() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.LEAVE_LOBBY + DELI);
    }

    @Override
    public void readyLobby() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.READY_LOBBY + DELI);
    }

    @Override
    public void unreadyLobby() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.UNREADY_LOBBY + DELI);
    }

    @Override
    public void move(Move move) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.MOVE + DELI + move.protocolFormat() + DELI);
    }

    @Override
    public void forfeit() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.FORFEIT + DELI);
    }

    // ------extras----
    /**
     * Send message in the lobby.
     * 
     * @param message to be sent in the lobby.
     */
    public void lobbyMsg(String message) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.LOBBY_MSG + DELI + message + DELI);
    }

}
