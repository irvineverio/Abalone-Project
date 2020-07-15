package client;

import abalone.Colour;
import abalone.ComputerPlayer;
import abalone.HumanPlayer;
import abalone.Move;
import abalone.Player;
import exceptions.MoveFormatException;
import exceptions.ServerUnavailableException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import protocol.ProtocolMessages;
import server.ServerGame;
import strategy.BetterStrategy;
import strategy.MiniMaxStrategy;
import strategy.Strategy;

/**
 * AbaloneServerHandler for the Abalone Server application. This class can
 * handle the communication with one server.
 * 
 * @author Wim Kamerman & Berke Guducu
 */
public class AbaloneServerHandler implements Runnable {

    private static final String INVALID_RESPONSE = "Invalid response to:";
    private static final String MAL = ProtocolMessages.DELIMITER + ProtocolMessages.MALFORMED_COMMAND;
    /** The socket and In- and OutputStreams. */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected Abalone Client. */
    private AbaloneClient clt;

    /** Name of this ClientHandler. */
    private String name;

    /** Game related. */
    private ServerGame game;
    private boolean gameActive;
    ArrayList<Player> players;
    private boolean cpPlays;
    private Player gamePlayer;
    private Player cp;
    Strategy strategy;

    /**
     * Constructs a new AbaloneServerHandler. Opens the In- and OutputStreams.
     * 
     * @param sock The client socket
     * @param clt  The connected client.
     * @param name The name of this ClientHandler
     */
    public AbaloneServerHandler(Socket sock, AbaloneClient clt, String name) {
        players = new ArrayList<>();
        // if not indicated user makes the moves.
        cpPlays = false;
        // default strategy to use for computer player
        strategy = new BetterStrategy();
        // socket of the server the client is connected to
        try {
            this.sock = sock;
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.clt = clt;
            this.name = name;
        } catch (IOException e) {
            clt.writeOut(e.getMessage());
            shutdown();
        }
    }

    /**
     * Continuously listens to server input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                if (msg.isBlank() || msg.isEmpty()) {
                    msg = in.readLine();
                    continue;
                }
                clt.writeOut("[" + name + "] Incoming: " + msg);
                handleCommand(msg);
                msg = in.readLine();
            }
        } catch (IOException e) {
            clt.writeOut(e.getMessage());
            shutdown();
        }
    }

    /**
     * Handles commands received from the server by calling the according methods at
     * the Client.<br>
     * If the received input is not valid, send an "Unknown Command" message to the
     * client.
     * 
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    private void handleCommand(String msg) throws IOException {
        String delimiter = ProtocolMessages.DELIMITER;
        String[] args = msg.split(delimiter);
        if (args.length < 2) { // expect at least 2 arguments
            clientWrite(INVALID_RESPONSE + msg);
        }
        String command = args[0];
        Integer ackCode = getAckCode(args); // if null not a confirmation
        switch (command) {
            case ProtocolMessages.CONNECT: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("Succesful. Connection established with server");
                }
                break;
            case ProtocolMessages.CREATE_LOBBY: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("Succesful. Lobby created.");
                } else {
                    clientWrite("Lobby creation failed, lobby name may be taken or invalid");
                }
                break;
            case ProtocolMessages.FORFEIT: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("Succesful. You have forfeited.");
                } else {
                    clientWrite("Failed to forfeit.");
                }
                break;
            case ProtocolMessages.GAME_START: // status
                if (args.length < 3) { // at least 3 arguments
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                ArrayList<String> names = getNames(args, 1); // get the names of players
                startGame(names);
                gameActive = true;
                gamePlayer = game.getPlayerByName(clt.getUserName());
                // initialize computer player for hints and making moves.
                // strategy = new NaiveStrategy();
                clientWrite("First player to make a move is: " + names.get(0)
                        + "\n player at the top starts and goes around clockwise");
                strategy = new MiniMaxStrategy();
                cp = new ComputerPlayer(gamePlayer.getColour(), strategy);
                if (cpPlays && game.getPlayerIndex(gamePlayer) == game.getTurn()) { // this client's turn
                    this.clientWrite("computer player is making a move");
                    Move cpMove = cp.determineMove(game.getBoard());
                    // send computer's move
                    this.outWrite(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + cpMove.protocolFormat());
                    // move will be made on the local board when reply is sent
                }
                break;
            case ProtocolMessages.PLAYER_DEFEAT: // status
                outWrite("Player " + getNames(args, 1).get(0) + "has lost.");
                break;
            case ProtocolMessages.GAME_FINISH: // status
                // get all the winner names provided by the server
                gameActive = false;
                ArrayList<String> names1 = getNames(args, 1);
                String nameList = "";
                for (String name : names1) {
                    nameList += " " + name;
                }
                if (nameList.equals("")) {
                    clientWrite("no winners, tie!");
                }
                // send the winners' user names to client
                clientWrite("User/s: " + names1 + "has won");
                break;
            case ProtocolMessages.JOIN_LOBBY: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("You have joined the lobby.");
                } else {
                    clientWrite("Failed to join the lobby, may be full or may not exist.");
                }
                break;
            case ProtocolMessages.LEAVE_LOBBY: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("You have left the lobby.");
                } else {
                    clientWrite("Failed to leave the lobby, perhaps you are already in a lobby.");
                }
                break;
            case ProtocolMessages.LOBBY_CHANGE: // status
                clientWrite("Lobby has now the users:" + getNames(args, 1).toString());
                break;
            case ProtocolMessages.LIST_LOBBY: // status
                clientWrite(
                        "Available lobbies: [lobbyName,capacity,numberOfPlayers] \n" + getNames(args, 2).toString());
                break;
            case ProtocolMessages.MOVE: // STATUS
                if (args.length < 5) {
                    if (msg.contains(ProtocolMessages.FORBIDDEN + "")) {
                        clientWrite("forbbiden move, try another move, also it might not be your turn");
                        break;
                    } else if (msg.contains(ProtocolMessages.UNAUTHORIZED + "")) {
                        clientWrite("you are unauthrized, perhaps you are not in a game");
                        break;
                    }
                    clientWrite("Unknown command from server");
                    break;
                }
                String name = args[1]; // user making the move
                Move move = null; // move made by the user
                try { // parse the move
                    move = Move.parseProtocol(
                            args[2] + ProtocolMessages.DELIMITER + args[3] + ProtocolMessages.DELIMITER + args[4]);
                } catch (MoveFormatException e) {
                    log(e.getMessage());
                    outWrite(ProtocolMessages.MOVE + MAL);
                    break;
                }
                makeMove(move, name);
                this.clientWrite(this.game.getBoard().toString());
                // make move with computer if computer plays
                if (cpPlays && game.getPlayerIndex(gamePlayer) == game.getTurn()) { // if this client's turn
                    cp = new ComputerPlayer(gamePlayer.getColour(), strategy);
                    this.clientWrite("computer player is making a move");
                    Move cpMove = cp.determineMove(game.getBoard());
                    // send computer's move
                    this.outWrite(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + cpMove.protocolFormat());
                    // move will be made on the local board when reply is sent
                }
                break;
            case ProtocolMessages.READY_LOBBY: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode < 5) {
                    clientWrite("You have made yourself ready in the lobby. \n " + ackCode
                            + "players are ready in the lobby");
                } else if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("You have made yourself ready in the lobby.");
                } else {
                    clientWrite("Failed to make you ready, perhaps you are not in a lobby.");
                }
                break;
            case ProtocolMessages.UNREADY_LOBBY: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode < 5) {
                    clientWrite("You have made yourself unready in the lobby. \n " + ackCode
                            + "players are ready in the lobby");
                } else if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("You have made yourself unready in the lobby.");
                } else {
                    clientWrite("Failed to make you unready, perhaps you are not in a lobby.");
                }
                break;
            case ProtocolMessages.LOBBY_MSG: // ACK
                if (ackCode == null) {
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                if (ackCode.equals(ProtocolMessages.SUCCESS)) {
                    clientWrite("You have sent the message");
                } else {
                    clientWrite("Failed to send the message, perhaps you are not in a lobby?");
                }
                break;
            case ProtocolMessages.MSG_RECV: // status
                if (args.length < 3) { // at least 3 argument
                    clientWrite(INVALID_RESPONSE + msg);
                    break;
                }
                clientWrite("Message from: " + args[2] + " : " + args[1]);
                break;
            default:
                clientWrite(command + ProtocolMessages.DELIMITER + ProtocolMessages.NOT_FOUND);
                break;
        }

    }

    /**
     * Get names in a list according to the protocol.
     * 
     * @param args the args from the server includes the main command.
     * @return
     */
    private ArrayList<String> getNames(String[] args, int startAt) {
        ArrayList<String> names = new ArrayList<>();
        if (startAt >= args.length) {
            return names;
        }
        String[] argsNames = Arrays.copyOfRange(args, startAt, args.length);
        for (String s : argsNames) {
            names.add(s);
        }
        return names;
    }

    /**
     * Get the ack code from the server. Can be: Success, Malformed command,
     * Unauthorized, Forbidden, or Not Found. Return null if not one of the codes.
     * 
     * @param args the args from the server.
     * @return
     */
    private Integer getAckCode(String[] args) {
        int[] validCodes = { ProtocolMessages.FORBIDDEN, ProtocolMessages.MALFORMED_COMMAND, ProtocolMessages.NOT_FOUND,
                             ProtocolMessages.SUCCESS, ProtocolMessages.UNAUTHORIZED };
        Integer code = makeNumber(args, 1);
        if (code == null) { // not a number
            return null;
        }
        for (int c : validCodes) { // check is known command
            if (c == (int) code) {
                return code;
            }
        }
        return code; // return -1 if unknown command
    }

    /**
     * Log for debugging purposes.
     * 
     * @param s string to be logged.
     */
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
     * Check args to see if they are valid.
     * 
     * @param args the args to check
     * @return true if args are valid.
     */
    private boolean argsCheck(String[] args, int i) {
        return args.length < i + 1 || args[i].isEmpty() || args[i].isBlank();
    }

    /*
     * wire to client
     */
    public void clientWrite(String s) {
        clt.writeOut(s);
    }

    /**
     * Write back to server.
     * 
     * @param s the message to write to the server.
     */
    public void outWrite(String s) {
        try {
            clt.sendMessage(s);
        } catch (ServerUnavailableException e) {
            clt.writeOut("Server handler: " + e.getMessage());
        }

    }

    // ----------------Game Commands------------
    private void startGame(List<String> names) {
        List<String> users = names;
        players = new ArrayList<>();
        players.add(new HumanPlayer(users.get(0), Colour.BLACK));
        if (users.size() == 2) { // 2 player game
            players.add(new HumanPlayer(users.get(1), Colour.WHITE));
            game = new ServerGame(players.get(0), players.get(1));
        } else if (users.size() == 3) { // 3 player game
            players.add(new HumanPlayer(users.get(1), Colour.WHITE));
            players.add(new HumanPlayer(users.get(2), Colour.GREEN));
            game = new ServerGame(players.get(0), players.get(1), players.get(2));
        } else if (users.size() == 4) { // 4 player game
            players.add(new HumanPlayer(users.get(1), Colour.GREEN));
            players.add(new HumanPlayer(users.get(2), Colour.WHITE));
            players.add(new HumanPlayer(users.get(3), Colour.RED));
            game = new ServerGame(players.get(0), players.get(1), players.get(2), players.get(3));
        }
        gameActive = true;
        return;
    }

    private void makeMove(Move move, String userName) {
        game.makeMove(move, getPlayer(userName));
    }

    /**
     * Get player with the given name.
     * 
     * @param userName the user name of the player.
     * @return
     */
    private Player getPlayer(String userName) {
        for (Player p : players) {
            if (p.getName().equals(userName)) {
                return p;
            }
        }
        assert false;
        return null;
    }

    /**
     * Get the game of this server handler.
     * 
     * @return the game.
     */
    public ServerGame getGame() {
        return this.game;
    }

    /**
     * Get gameActive.
     * 
     * @return true if game is active.
     */
    public boolean getGameActive() {
        return gameActive;
    }

    /**
     * Set to true to let the computer player make the moves.
     * 
     * @param cpPlays true if CP will play.
     */
    public void computerPlays(Boolean cpPlays) {
        this.cpPlays = cpPlays;
    }

    /**
     * Get the CP.
     * 
     * @return the CP.
     */
    public Player getCP() {
        return this.cp;
    }

    /**
     * Set the strategy.
     * 
     * @param st the CP to set.
     */
    public void setStrategy(Strategy st) {
        this.strategy = st;
    }

    // ---------------------

    /**
     * Shut down the connection to this client by closing the socket and the In- and
     * OutputStreams.
     */
    private void shutdown() {
        clt.writeOut("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clt.closeConnection();
    }

}
