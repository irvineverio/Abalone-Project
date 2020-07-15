package server;

import abalone.Move;
import exceptions.MoveFormatException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import protocol.ProtocolMessages;

/**
 * HotelClientHandler for the Hotel Server application. This class can handle
 * the communication with one client.
 * 
 * @author Wim Kamerman
 */
public class AbaloneClientHandler implements Runnable {

    private static final String MAL = ProtocolMessages.DELIMITER + ProtocolMessages.MALFORMED_COMMAND;
    /** The socket and In- and OutputStreams. */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected Abalone Server. */
    private AbaloneServer srv;

    /** Name of this ClientHandler. */
    private String name;

    /** User of this client handler. */
    private User user;

    /** Connection established. */
    private boolean connected;

    /**
     * Constructs a new HotelClientHandler. Opens the In- and OutputStreams.
     * 
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public AbaloneClientHandler(Socket sock, AbaloneServer srv, String name) {
        try {
            this.sock = sock;
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.srv = srv;
            this.name = name;
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Handles commands received from the client by calling the according methods at
     * the HotelServer. For example, when the message "i Name" is received, the
     * method doIn() of HotelServer should be called and the output must be sent to
     * the client. <br>
     * If the received input is not valid, send an "Unknown Command" message to the
     * server.
     * 
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    private void handleCommand(String msg) throws IOException {
        // ignore empty lines?
        // if(msg.isBlank() || msg.isEmpty()) {
        // return;
        // }
        String delimiter = ProtocolMessages.DELIMITER;
        String[] args = msg.split(delimiter);
        System.out.println("> [" + name + "] handling command");
        String command = args[0];
        if (!connected && !args[0].equals(ProtocolMessages.CONNECT)) { // connection not yet established
            outWrite(args[0] + ProtocolMessages.DELIMITER + ProtocolMessages.UNAUTHORIZED);
        }
        switch (command) {
            case ProtocolMessages.CONNECT:
                if (argsCheck(args, 1)) {
                    outWrite(ProtocolMessages.CONNECT + MAL);
                    break;
                }
                user = new User(args[1], this);
                String response = srv.getHello(user);
                if (response.contains(ProtocolMessages.SUCCESS + "")) {
                    connected = true;
                }
                outWrite(response);
                break;
            case ProtocolMessages.CREATE_LOBBY:
                if (argsCheck(args, 1)) {
                    outWrite(ProtocolMessages.CREATE_LOBBY + MAL);
                    break;
                }
                Integer lobbySize = makeNumber(args, 2);
                if (lobbySize == null) {
                    outWrite(ProtocolMessages.CREATE_LOBBY + MAL);
                    break;
                }
                String response1 = srv.createLobby(args[1], lobbySize);
                outWrite(response1);
                break;
            case ProtocolMessages.FORFEIT:
                outWrite(srv.forfeit(user));
                break;
            case ProtocolMessages.JOIN_LOBBY:
                if (argsCheck(args, 1)) {
                    outWrite(ProtocolMessages.JOIN_LOBBY + MAL);
                    break;
                }
                outWrite(srv.joinLobby(args[1], user));
                break;
            case ProtocolMessages.LEAVE_LOBBY:
                outWrite(srv.leaveLobby(user));
                break;
            case ProtocolMessages.LIST_LOBBY:
                outWrite(srv.listLobby());
                break;
            case ProtocolMessages.MOVE:
                if (args.length < 4) {
                    outWrite(ProtocolMessages.MOVE + MAL);
                    break;
                }
                Move move = null;
                try {
                    move = Move.parseProtocol(
                            args[1] + ProtocolMessages.DELIMITER + args[2] + ProtocolMessages.DELIMITER + args[3]);
                } catch (MoveFormatException e) {
                    log(e.getMessage());
                    outWrite(ProtocolMessages.MOVE + MAL);
                    break;
                }
                outWrite(srv.move(move, user));
                break;
            case ProtocolMessages.READY_LOBBY:
                outWrite(srv.readyLobby(user));
                break;
            case ProtocolMessages.UNREADY_LOBBY:
                outWrite(srv.unreadyLobby(user));
                break;
            case ProtocolMessages.LOBBY_MSG:
                if (args.length < 2) {
                    outWrite(ProtocolMessages.LOBBY_MSG + MAL);
                    break;
                }
                outWrite(srv.lobbyMsg(user, args[1]));
                break;
            default:
                outWrite(command + ProtocolMessages.DELIMITER + ProtocolMessages.NOT_FOUND);
                break;
        }

    }

    private void log(String s) {
        System.out.println("> [" + name + "]" + "Logging: " + s);
    }

    private Integer makeNumber(String[] args, int i) {
        if (argsCheck(args, i)) {
            return null;
        }
        try {
            return Integer.parseInt(args[i]);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    /**
     * Check the args.
     * 
     * @param args to check.
     * @return
     */
    private boolean argsCheck(String[] args, int i) {
        return args.length < i + 1 || args[i].isEmpty() || args[i].isBlank();
    }

    /**
     * Send message to client.
     * 
     * @param s message to send.
     */
    public void outWrite(String s) {
        System.out.println("> [" + name + "]" + "Sending: " + s + ProtocolMessages.DELIMITER);
        try {
            out.append(s + ProtocolMessages.DELIMITER);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shut down the connection to this client by closing the socket and the In- and
     * OutputStreams.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // also remove from the lobby that they are in and forfeit their game
        srv.leaveLobby(user); // make user leave its lobby
        srv.removeClient(this);
    }
}
