package strategy;

import abalone.Board;
import abalone.Colour;
import abalone.Move;
import abalone.Team;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class implementation from Strategy for a dumb strategy in Abalone board
 * game project. <br>
 * Credits: Inspired by GeeksForGeeks' minimax algirthm for tic-tac-toe
 * https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
 *
 */
public class MiniMaxStrategy extends BetterStrategy implements Strategy {

    private String name = "MiniMaxStrategy";
    private static final int MOVE_LIMIT = 96;
    private int depthLim; // default depth limit
    private Colour mainColour;
    private Colour opColour;

    /**
     * Construct a new Mini Max strategy.
     * 
     * @param depthLim the depth limit to be used (advised to be 1 or 2).
     */
    public MiniMaxStrategy(int depthLim) {
        this.depthLim = depthLim;
    }

    /**
     * Construct a new Mini Max strategy with depth 2.
     */
    public MiniMaxStrategy() {
        this(2);
    }
    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the strategy.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Use the MiniMax algorithm to find a board state's score. The maximizer tries
     * to get the best score for this player and the minimizer acts as an opponent.
     * 
     * @param board     the board state to
     * @param depth     the
     * @param isMax     true if maximizers' turn.
     * @param colour    the color of the player making the move.
     * @param movesLeft the moves until the game ends.
     * @return the point for this board
     */
    private int minimax(Board board, int depth, Boolean isMax, int movesLeft) {
        int score = determineBoardScore(board);
        if (score == 100) { // if results in winning the game
            return score;
        } else if (score == -100) { // if results in losing the game
            return score;
        }
        // return score if depth limit reached or no more moves left to make
        if (depth >= depthLim || movesLeft <= 0) {
            return score;
        }
        if (isMax) { // maximizer's turn
            int best = -1000;
            ArrayList<Move> allMoves = getAllMoves(board, mainColour);
            for (Move m : allMoves) { // for all possible moves
                Board moveBoard = board.deepCopy();
                moveBoard.moveD(m);
                best = Math.max(best, minimax(moveBoard, depth + 1, !isMax, movesLeft - 1));
            }
            return best;
        } else { // minimizer's turn
            int best = 1000;
            ArrayList<Move> allMoves = getAllMoves(board, opColour);
            for (Move m : allMoves) { // for all possible moves
                Board moveBoard = board.deepCopy();
                moveBoard.moveD(m);
                best = Math.min(best, minimax(moveBoard, depth + 1, isMax, movesLeft - 1));
            }
            return best;
        }
    }

    private int determineBoardScore(Board board) {
        Team team = board.getTeam(this.mainColour);
        if (team.getScore() == 6) { // this colour has won
            return 100;
        }
        if (board.getOtherTeams(team).get(0).getScore() == 6) { // other colour has won
            return -100;
        }
        return getRelativeScore(board);
    }
    // -- Commands ---------------------------------------------------

    /**
     * Determine a valid move in the board by choosing a the best move found so far.
     * 
     * @param board that holds fields for marbles
     * @return the calculated valid move
     */
    @Override
    public Move determineMove(Board board, Colour colour) {
        Random random = new Random(); // for getting a random move
        this.mainColour = colour; // own color
        opColour = board.getOtherTeams(board.getTeam(colour)).get(0).getColours()[0]; // opponent color
        int bestVal = getRelativeScore(board);
        ArrayList<Move> allMoves = getAllMoves(board, colour);
        // first get a random move
        Move bestMove = allMoves.get(random.nextInt(allMoves.size()));
        for (Move m : allMoves) { // perform all possible moves
            Board boardWithMove = board.deepCopy();
            boardWithMove.moveD(m);
            // find the score for the move
            int score = minimax(boardWithMove, 0, false, MOVE_LIMIT);
            // if better move, this Move becomes bestMove
            if (score >= bestVal) {
                bestMove = m;
                bestVal = score;
            }

        }
        return bestMove; // return best move
    }

    /**
     * Get the score of this color minus other team's score.
     * 
     * @param board  board to check this.
     * @param colour color to check.
     * @return
     */
    private int getRelativeScore(Board board) {
        return board.getTeam(mainColour).getScore() - board.getTeam(opColour).getScore();
    }
}