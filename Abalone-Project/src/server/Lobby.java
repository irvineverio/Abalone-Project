package server;

import abalone.Colour;
import abalone.HumanPlayer;
import abalone.Move;
import abalone.NetworkPlayer;
import abalone.Player;
import abalone.Team;

import java.util.ArrayList;
import java.util.List;

import protocol.ProtocolMessages;

/**
 * Lobby class to contain users before a game starts.
 * 
 * @invariant players.size() < size
 * @author Berke Guducu
 *
 */
public class Lobby {
    private ServerGame game;
    private List<User> users;
    private List<Player> players;
    private String name;
    private int size;
    private boolean deleted;
    private boolean gameActive;

    /**
     * Initialize a lobby.
     * 
     * @requires size > 0 && size < 5
     * @param name name of the lobby.
     * @param size size of the lobby.
     */
    public Lobby(String name, int size) {
        this.name = name;
        this.size = size;
        users = new ArrayList<>();
        deleted = false;
        gameActive = false;
    }

    /**
     * Add a user to the lobby.
     * 
     * @param user user to be added.
     * @return
     */
    public boolean addUser(User user) {
        if (users.size() < size) {
            users.add(user);
            echoChange(user);
            return true;
        }
        return false;
    }

    /**
     * Remove a user from the lobby.
     * 
     * @param user user to be removed.
     * @return
     */
    public boolean removeUser(User user) {
        if (gameActive) { // if game is active while leaving forfeit user
            forfeitUser(user);
        }
        if (users.contains(user)) {
            users.remove(user);
            if (users.size() == 0) {
                deleted = true;
            }
            user.setReady(false);
            echoChange(user);
            return true;
        }
        return false;
    }

    /**
     * Set a user as ready or unReady in the lobby.
     * 
     * @param user  user to set ready.
     * @param ready true to set ready.
     */
    public void setReadyUser(User user, boolean ready) {
        user.setReady(ready);
        if (allReady()) { // start game if everyone is ready
            startGame();
            // set everyone to not ready
            for (User u : users) {
                u.setReady(false);
            }
        }
    }

    public boolean getDeleted() {
        return deleted;
    }

    /**
     * Start the game in this lobby, size should be the same as users.size.
     * 
     * @requires players.size() >= 2 && player.size <= 4
     * @return
     */
    public boolean startGame() {
        if (users.size() != size) {
            return false;
        }
        players = new ArrayList<>();
        players.add(new NetworkPlayer(users.get(0).getName(), Colour.BLACK));
        if (users.size() == 2) { // 2 player game
            players.add(new HumanPlayer(users.get(1).getName(), Colour.WHITE));
            game = new ServerGame(players.get(0), players.get(1));
        } else if (users.size() == 3) { // 3 player game
            players.add(new NetworkPlayer(users.get(1).getName(), Colour.WHITE));
            players.add(new NetworkPlayer(users.get(2).getName(), Colour.GREEN));
            game = new ServerGame(players.get(0), players.get(1), players.get(2));
        } else if (users.size() == 4) { // 4 player game
            players.add(new NetworkPlayer(users.get(1).getName(), Colour.GREEN));
            players.add(new NetworkPlayer(users.get(2).getName(), Colour.WHITE));
            players.add(new NetworkPlayer(users.get(3).getName(), Colour.RED));
            game = new ServerGame(players.get(0), players.get(1), players.get(2), players.get(3));
        }
        gameActive = true;
        echo(ProtocolMessages.GAME_START + listUsers());
        return true;
    }

    /**
     * Check if all players are ready.
     * 
     * @return
     */
    public boolean allReady() {
        for (User u : users) {
            if (!u.getReady()) { // if there is a user that is not ready
                return false;
            }
        }
        return users.size() == size;
    }

    /**
     * Forfeit a user.
     * 
     * @param user that is forfeiting.
     * @return
     */
    public boolean forfeitUser(User user) {
        if (!gameActive) {
            return false;
        }
        game.getBoard().getTeam(getUserPlayer(user).getColour());
        gameActive = false;
        endGame(user);
        return true;
    }

    /**
     * Given user makes a move.
     * 
     * @param move the move to be made.
     * @param user user making the move.
     * @return
     */
    public boolean makeMove(Move move, User user) {
        if (!gameActive) {
            return false;
        }
        boolean validMove = game.makeMove(move, getUserPlayer(user));
        if (!validMove) {
            return false;
        }
        if (game.isOver()) {
            endGame();
        }
        return true;
    }

    /**
     * Get the player corresponding to the user.
     * 
     * @requires user to have a player.
     * @param user to get the player.
     * @return
     */
    private Player getUserPlayer(User user) {
        return players.get(users.indexOf(user));
    }

    /**
     * End the game and echo the winners, no winners if draw.
     */
    public void endGame() {
        Player[] winnerPlayers = game.getWinners(); 
        String result = "";
        if (winnerPlayers == null) { // no winners
            result = ProtocolMessages.DELIMITER; // return no winner to indicate tie according to protocol
        } else {
            for (Player p : winnerPlayers) {
                result += ProtocolMessages.DELIMITER;
                result += p.getName();
            }
        }
        gameActive = false;
        echo(ProtocolMessages.GAME_FINISH + result);
    }

    /**
     * End the game with given loser user.
     * 
     * @param loserUser the user who has lost.
     */
    public void endGame(User loserUser) { //
        Team loserTeam = game.getBoard().getTeam(getUserPlayer(loserUser).getColour()); 
        String result = "";
        ArrayList<Team> teams = game.getBoard().getOtherTeams(loserTeam);
        ArrayList<Player> winnerPlayers = new ArrayList<>();
        for (Team t : teams) { // for all teams
            for (Colour c : t.getColours()) {
                winnerPlayers.add(game.getPlayer(c));
            }
            for (Player p : winnerPlayers) {
                result += ProtocolMessages.DELIMITER;
                result += p.getName();
            }
        }
        gameActive = false;
        echo(ProtocolMessages.GAME_FINISH + result);
    }

    /**
     * Send a message to all the users.
     * 
     * @param message the message to be sent.
     */
    public void echo(String message) {
        for (User u : users) {
            u.getHandler().outWrite(message);
        }
    }

    /**
     * Send a message to everyone except the user provided.
     * 
     * @param user the user to send the message except.
     */
    public void echoExcept(String message, User user) {
        for (User u : users) {
            if (!u.equals(user)) {
                u.getHandler().outWrite(message);
            }
        }
    }

    /**
     * get the number of players who are ready.
     * 
     * @return
     */
    public int getReadyNumber() {
        int count = 0;
        for (User u : users) {
            if (u.getReady()) { // if there is a user that is not ready
                count++;
            }
        }
        return count;
    }

    /**
     * Send move to everyone except the user.
     * 
     * @param move the move to be sent.
     * @param user the user that is making the move.
     */
    public void echoMove(Move move, User user) {
        echoExcept(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + user.getName() + ProtocolMessages.DELIMITER
                + move.protocolFormat(), user);
    }

    /**
     * Echo a change for everyone except the user Includes delimiter at the end.
     */
    public void echoChange(User user) {
        echoExcept(ProtocolMessages.LOBBY_CHANGE + listUsers(), user);
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }

    public ServerGame getGame() {
        return game;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<User> getUsers() {
        return users;
    }

    public int getNumberOfPlayer() {
        return players.size();
    }

    /**
     * list the users in the lobby in a string in protocol format.
     * 
     * @return the user list.
     */
    public String listUsers() {
        String result = "";
        for (User u : users) {
            result += ProtocolMessages.DELIMITER;
            result += u.getName();
        }
        return result;
    }
}
