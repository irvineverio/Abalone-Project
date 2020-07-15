package client;

import abalone.Move;

import exceptions.ExitProgram;
import exceptions.MoveFormatException;
import exceptions.ServerUnavailableException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import protocol.ProtocolMessages;
import strategy.BetterStrategy;
import strategy.MiniMaxStrategy;
import utils.TextIO;

public class AbaloneClientTUI implements AbaloneClientView {

    private static final String HINT = "HINT";
    private static final String HUMAN_PLAYS = "HP";
    private static final String COMPUTER_PLAYS = "CP";
    private static final String SMART_PLAYER = "SP";

    private AbaloneClient client;

    public AbaloneClientTUI(AbaloneClient client) {
        this.client = client;
    }

    @Override
    public void start() throws ServerUnavailableException {
        while (true) {
            String input = getString("Enter a command");
            // client.sendMessage(input);
            try {
                handleUserInput(input);
            } catch (ExitProgram e) {
                e.printStackTrace();
                client.closeConnection();
                break;
            }
        }

    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        if (input.isBlank() || input.isEmpty()) {
            showMessage("Please enter valid input");
            return;
        }
        String str = input.replaceAll(" ", ProtocolMessages.DELIMITER + "");
        String delimiter = ProtocolMessages.DELIMITER;
        List<String> args = Arrays.asList(str.split(delimiter));
        // System.out.println("Client TUI handling command");
        if (args.isEmpty()) {
            showMessage("Please enter valid input");
            return;
        }
        String command = args.get(0);
        if (command.toUpperCase().equals("HELP") || command.toUpperCase().equals("H")) {
            printHelpMenu();
            return;
        }
        switch (command.toUpperCase()) {
            case COMPUTER_PLAYS:
                showMessage("Computer player will make the next moves");
                client.getHandler().computerPlays(true);
                client.getHandler().setStrategy(new BetterStrategy());
                break;
            case SMART_PLAYER:
                if (args.size() < 2) {
                    showMessage("You need to choose the thinking time(0-3) of the CP. \n Example: SP 2");
                    break;
                }
                int depth;
                try {
                    depth = Integer.parseInt(args.get(1));
                    if (depth > 2) {
                        showMessage("Please be aware! At depth 3 the moves can take up to a minute.");
                    }
                    if (depth < 0 || depth > 3) {
                        showMessage("Depth should be between 0 and 3, 1 and 2 are recommended.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    showMessage("Depth should be int");
                    break;
                }
                showMessage("SmartPlayer will make the next moves for you");
                client.getHandler().computerPlays(true);
                client.getHandler().setStrategy(new MiniMaxStrategy(depth));
                break;
            case HUMAN_PLAYS:
                showMessage("You will make the moves");
                client.getHandler().computerPlays(false);
                break;
            case HINT:
                if (!(client.getHandler().getGameActive())) { // if game not active
                    showMessage("game not active");
                    break;
                }
                // get a move from CP
                Move moveHint = client.getHandler().getCP().determineMove(client.getHandler().getGame().getBoard());
                showMessage("Move hint:\n" + ProtocolMessages.MOVE + " " + moveHint.toString());
                break;
            case ProtocolMessages.EXIT:
                throw new ExitProgram("user indicated to exit");
            case ProtocolMessages.CREATE_LOBBY:
                if (args.size() < 3) {
                    showMessage("Create lobby example: CREATE_LOBBY LOB 2");
                    break;
                }
                String lobbyName = args.get(1);
                int lobbySize = 0;
                try {
                    lobbySize = Integer.parseInt(args.get(2));
                } catch (NumberFormatException e) {
                    showMessage("lobby size should be int");
                    break;
                }
                client.createLobby(lobbyName, lobbySize);
                break;
            case ProtocolMessages.FORFEIT:
                client.forfeit();
                break;
            case ProtocolMessages.JOIN_LOBBY:
                if (args.size() < 2) {
                    showMessage("Join lobby example: JOIN_LOBBY LOB");
                    break;
                }
                String lobName = args.get(1);
                client.joinLobby(lobName);
                break;
            case ProtocolMessages.LEAVE_LOBBY:
                client.leaveLobby();
                break;
            case ProtocolMessages.LIST_LOBBY:
                client.listLobby();
                break;
            case ProtocolMessages.MOVE:
                Move move1 = null;
                if (args.size() < 2) {
                    showMessage("Example move: move a0,b1,2");
                    break;
                }
                String moveString = String.join("", args.subList(1, args.size()));
                try {
                    move1 = Move.parseWithOne(moveString, ProtocolMessages.SEPARATOR, 0);
                } catch (MoveFormatException e) {
                    showMessage(e.getMessage() + "move is in invalid format. Example move: move a0,b1,2");
                    break;
                }
                client.move(move1);
                break;
            case ProtocolMessages.READY_LOBBY:
                client.readyLobby();
                break;
            case ProtocolMessages.UNREADY_LOBBY:
                client.unreadyLobby();
                break;
            case ProtocolMessages.LOBBY_MSG:
                if (args.size() < 2) {
                    showMessage("Example msg: LOBBY_MSG hello");
                    break;
                }
                showMessage("Sending message");
                client.lobbyMsg((String.join(" ", args.subList(1, args.size()))));
                break;
            default:
                showMessage("Unknown Command. Type help to get help!");
                break;
        }

    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printHelpMenu() {
        String result = "";
        result += "--------------------------";
        result += "\n Server response codes: \n";
        result += String.format("%-5s: %5s \n", ProtocolMessages.SUCCESS, "Success");
        result += String.format("%-5s: %5s \n", ProtocolMessages.FORBIDDEN, "Forbidden move");
        result += String.format("%-5s: %5s \n", ProtocolMessages.NOT_FOUND, "Not found");
        result += String.format("%-5s: %5s \n", ProtocolMessages.UNAUTHORIZED, "Unauthrized");

        result += "--------------------------";
        result += "\n Client commands to send to the server \n";
        result += helpFormat(ProtocolMessages.CREATE_LOBBY, // command
                "<lobbyName> <size(2-4)>", // parameters
                "Create a new lobby with given name and size", // explanation
                ProtocolMessages.CREATE_LOBBY + " LOB 2"); // example
        result += helpFormat(ProtocolMessages.JOIN_LOBBY, // command
                "<lobbyName>", // parameter
                "Join a lobby with given name ", // explanation
                ProtocolMessages.JOIN_LOBBY + " LOB"); // example
        result += helpFormat(ProtocolMessages.LIST_LOBBY, // command
                "", // parameters
                "List lobbies in the server ", // explanation
                ProtocolMessages.LIST_LOBBY);
        result += helpFormat(ProtocolMessages.READY_LOBBY, // command
                "", // parameters
                "Set yourself ready in the lobby ", // explanation
                ProtocolMessages.READY_LOBBY);
        result += helpFormat(ProtocolMessages.UNREADY_LOBBY, // command
                "", // parameters
                "Set yourself unready in the lobby ", // explanation
                ProtocolMessages.UNREADY_LOBBY);
        result += helpFormat(ProtocolMessages.LEAVE_LOBBY, // command
                "", // parameters
                "Leave the lobby you are in ", // explanation
                ProtocolMessages.LEAVE_LOBBY);
        result += helpFormat(ProtocolMessages.LOBBY_MSG, // command
                " <message>", // parameters
                "Send a message in the lobby you are in ", // explanation
                ProtocolMessages.LOBBY_MSG + " hello");
        result += helpFormat(ProtocolMessages.MOVE, // command
                "<row><col>,<row><col>,<direction>", // parameters
                "Make a move on the board with 2 coordinates and direction \n"
                        + " direction starts with 0 at top left and goes " + // explanation
                        "\n around clockwise(ex. 5 is left) " + "\n note: this client uses 0 to indicate the"
                        + " first column while the protocol uses 1 so the message is converted when sent",
                ProtocolMessages.MOVE + " A0,B1,1"); // example

        result += "--------------------------";
        result += "\n Local commands \n";
        result += helpFormat(COMPUTER_PLAYS, // command
                "", // parameters
                "Let the computer make the moves(select before game starts): ", // explanation
                COMPUTER_PLAYS);
        result += helpFormat(SMART_PLAYER, // command
                " <DEPTH(1-2)>", // parameters
                "Let the smart computer make the moves with depth" + "(bigger means longer thinking time): ",
                SMART_PLAYER + " 2"); // example
        result += helpFormat(HUMAN_PLAYS, // command
                "", // parameters
                "You make the moves(default): ", // explanation
                HUMAN_PLAYS);
        result += helpFormat(HINT, // command
                "", // parameters
                "Get a hint for the this move: ", // explanation
                HINT);
        showMessage(result);
    }

    /**
     * Get a string in the help format to make it easier to read.
     * 
     * @param command     command that this is about.
     * @param parameters  the parameters to use with this command.
     * @param explanation the explanation of how this command does.
     * @param example     an example to show how this command works.
     * @return
     */
    private String helpFormat(String command, String parameters, String explanation, String example) {
        return String.format("%-15s: %15s \n %s \n Example: %20s \n\n", command, // command
                parameters, // parameters
                explanation, // explanation
                example); // example)
    }

    @Override
    public InetAddress getIp() {
        InetAddress ip = null;
        while (ip == null) {
            try {
                ip = InetAddress.getByName(getString("Provide ip"));
            } catch (UnknownHostException e) {
                this.showMessage("Invalid ip try again");
            }
        }
        return ip;
    }

    @Override
    public String getString(String question) {
        showMessage(question);
        return TextIO.getln();
    }

    @Override
    public int getInt(String question) {
        showMessage(question);
        int inti = -1;
        while (inti == -1) {
            String answer = TextIO.getln();
            try {
                return Integer.parseInt(answer);
            } catch (NumberFormatException nfe) {
                this.showMessage("Invalid int, try again");
            }
        }
        return 0;
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        return TextIO.getlnBoolean();
    }
}
