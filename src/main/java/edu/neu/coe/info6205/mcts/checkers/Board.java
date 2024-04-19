package edu.neu.coe.info6205.mcts.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Board {


    private static final int SIDE_LENGTH = 8;
    private static final int NUM_SQUARES = SIDE_LENGTH * SIDE_LENGTH;

    Piece[] state;
    private int fromPos = -1;
    private int toPos = -1;
    // origin position of double jump move, used to invalidate other moves during multi-move
    private int doublejumpPos = -1;
    // set the players turn
    private Player turn;
    // to track number of PLAYER1/PLAYER2 pieces on board
    public HashMap<Player, Integer> pieceCount;
    public HashMap<Player, Integer> kingCount;
    private boolean FORCETAKES = true;

    public Board() {
        state = new Piece[NUM_SQUARES];
    }

    public Board initialize() {
        Board board = new Board();
        for (int i = 0; i < NUM_SQUARES; i++) {
            int x = i % SIDE_LENGTH;
            int y = i / SIDE_LENGTH;
            // setting pieces on the appropriate position
            if ((x + y) % 2 == 1) {
                //setting PLAYER 1 pieces on intial 3 rows
                if (y < 3) {
                    board.state[i] = new Piece(Player.PLAYER1, false);
                } else if (y > 4) {
                    board.state[i] = new Piece(Player.PLAYER2, false);
                }
            }
        }
        board.pieceCount = new HashMap<>();
        int player1Count = (int) Arrays.stream(board.state).filter(Objects::nonNull).filter(obj -> obj.getPlayer() == Player.PLAYER1).count();
        int player2Count = (int) Arrays.stream(board.state).filter(Objects::nonNull).filter(obj -> obj.getPlayer() == Player.PLAYER2).count();
        board.pieceCount.put(Player.PLAYER1, player1Count);
        board.pieceCount.put(Player.PLAYER2, player2Count);
        board.kingCount = new HashMap<>();
        board.kingCount.put(Player.PLAYER1, 0);
        board.kingCount.put(Player.PLAYER2, 0);
        board.turn = Player.PLAYER1;
        return board;
    }

    public Board deepCopy() {
        Board board = new Board();
        System.arraycopy(this.state, 0, board.state, 0, board.state.length);
        return board;
    }

    public int computeHeuristic(Player player) {
        switch (player) {
            case PLAYER1:
                return heuristic1(player);
            case PLAYER2:
                return heuristic2(player);
        }
        throw new RuntimeException("Invalid heuristic");
    }

    private int heuristic1(Player player) {
        if (this.pieceCount.get(player.getOpposite()) == 0) {
            return Integer.MAX_VALUE;
        }
        if (this.pieceCount.get(player) == 0) {
            return Integer.MIN_VALUE;
        }
        // difference between piece counts with kings counted twice
        return pieceScore(player) - pieceScore(player.getOpposite());
    }


    private int heuristic2(Player player) {
        if (this.pieceCount.get(player.getOpposite()) == 0) {
            return Integer.MAX_VALUE;
        }
        else if (this.pieceCount.get(player) == 0) {
            return Integer.MIN_VALUE;
        } else {
            return pieceScore(player) / pieceScore(player.getOpposite());
        }
    }

    private int pieceScore(Player player) {
        return this.pieceCount.get(player) + this.kingCount.get(player);
    }

    public ArrayList<Board> getSuccessors() {
        // compute jump successors
        ArrayList<Board> successors = getSuccessors(true);
        if (FORCETAKES) {
            if (!successors.isEmpty()) {
                // return only jump successors if available
                return successors;
            } else {
                // return non-jump successors (since no jumps available)
                return getSuccessors(false);
            }
        } else {
            // return jump and non-jump successors
            successors.addAll(getSuccessors(false));
            return successors;
        }
    }

    /**
     *
     * Gets valid successors states for a specific player
     */

    public ArrayList<Board> getSuccessors (Player player) {
        ArrayList<Board> result = new ArrayList<>();

        // get jumped positions
        for (int i = 0; i < this.state.length; i++) {
            if (state[i] != null) {
                if (state[i].getPlayer() == player) {
                    result.addAll(getSuccessors(i, true));
                }
            }
        }
        //get non-jump positions
        for (int i = 0; i < this.state.length; i++) {
            if (state[i] != null) {
                if (state[i].getPlayer() == player) {
                    result.addAll(getSuccessors(i, false));
                }
            }
        }
        return result;
    }

    public ArrayList<Board> getSuccessors(boolean jump) {
        ArrayList<Board> result = new ArrayList<>();
        for (int i = 0; i < this.state.length; i++) {
            if (state[i] != null) {
                if (state[i].getPlayer() == turn) {
                    result.addAll(getSuccessors(i, jump));
                }
            }
        }
        return result;
    }

    public ArrayList<Board> getSuccessors(int position) {
        if (FORCETAKES) {
            ArrayList<Board> jumps = getSuccessors(true);
            if (!jumps.isEmpty()) {
                return getSuccessors(position, true);
            } else {
                return getSuccessors(position, false);
            }
        } else {
            ArrayList<Board> result = new ArrayList<>();
            result.addAll(getSuccessors(position, true));
            result.addAll(getSuccessors(position, false));
            return result;
        }
    }
public ArrayList<Board> getSuccessors(int position, boolean jump) {
        if (this.getPiece(position).getPlayer() != turn) {
            throw new IllegalArgumentException("No such piece at that position");
        }
        Piece piece = this.state[position];
        if (jump) {
            return jumpSuccessors(piece, position);
        } else {
            return nonJumpSuccessors(piece, position);
        }
    }

    private ArrayList<Board> nonJumpSuccessors(Piece piece, int position) {
        ArrayList<Board> result = new ArrayList<>();
        int x = position % SIDE_LENGTH;
        int y = position / SIDE_LENGTH;
        // loop through allowed movement directions
        for (int dx : piece.getXMovements()) {
            for (int dy : piece.getYMovements()) {
                int newX = x + dx;
                int newY = y + dy;
                // new position valid?
                if (isValid(newY, newX)) {
                    // new position available?
                    if (getPiece(newY, newX) == null) {
                        int newpos = SIDE_LENGTH * newY + newX;
                        result.add(createNewState(position, newpos, piece, false, dy, dx));
                    }
                }
            }
        }
        return result;
    }
private ArrayList<Board> jumpSuccessors(Piece piece, int position) {
        ArrayList<Board> result = new ArrayList<>();
        if (doublejumpPos > 0 && position != doublejumpPos) {
            return result;
        }
        int x = position % SIDE_LENGTH;
        int y = position / SIDE_LENGTH;
        // loop through allowed movement directions
        for (int dx : piece.getXMovements()) {
            for (int dy : piece.getYMovements()) {
                int newX = x + dx;
                int newY = y + dy;
                // new position valid?
                if (isValid(newY, newX)) {
                    // new position contain opposite player?
                    if (getPiece(newY, newX) != null && getPiece(newY, newX).getPlayer() == piece.getPlayer().getOpposite()) {
                        newX = newX + dx;
                        newY = newY + dy;
                        // jump position valid?
                        if (isValid(newY, newX)) {
                            // jump position available?
                            if (getPiece(newY, newX) == null) {
                                int newpos = SIDE_LENGTH * newY + newX;
                                result.add(createNewState(position, newpos, piece, true, dy, dx));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private Board createNewState(int oldPos, int newPos, Piece piece, boolean jumped, int dy, int dx) {
        Board result = this.deepCopy();
        result.pieceCount = new HashMap<>(pieceCount);
        result.kingCount = new HashMap<>(kingCount);
        // check if its king position
        boolean kingConversion = false;
        if (isKingPosition(newPos, piece.getPlayer())) {
            piece = new Piece(piece.getPlayer(), true);
            kingConversion = true;
            result.kingCount.replace(piece.getPlayer(), result.kingCount.get(piece.getPlayer()) + 1);
        }
        result.state[oldPos] = null;
        result.state[newPos] = piece;
        // store meta data
        result.fromPos = oldPos;
        result.toPos = newPos;
        Player oppPlayer = piece.getPlayer().getOpposite();
        result.turn = oppPlayer;
        if (jumped) {
            // remove captured piece
            result.state[newPos - SIDE_LENGTH * dy - dx] = null;
            result.pieceCount.replace(oppPlayer, result.pieceCount.get(oppPlayer) - 1);
            // is another jump available? (not allowed if just converted into king)
            if (!result.jumpSuccessors(piece, newPos).isEmpty() && !kingConversion) {
                // don't swap turns
                result.turn = piece.getPlayer();
                // remember double jump position
                result.doublejumpPos = newPos;
            }
        }
        return result;
    }

    private boolean isKingPosition(int pos, Player player) {
        int y = pos / SIDE_LENGTH;
        if (y == 0 && player == Player.PLAYER2) {
            return true;
        } else return y == SIDE_LENGTH - 1 && player == Player.PLAYER1;
    }

    public int getToPos() {
        return this.toPos;
    }

    public int getFromPos() {
        return this.fromPos;
    }


    public Player getTurn() {
        return turn;
    }

    public boolean isGameOver() {
        return (pieceCount.get(Player.PLAYER1) == 0 || pieceCount.get(Player.PLAYER2) == 0);
    }

    public int getSideLength() {
        return SIDE_LENGTH;
    }

    public Piece getPiece(int i) {
        return state[i];
    }

    private Piece getPiece(int y, int x) {
        return getPiece(SIDE_LENGTH * y + x);
    }

    private boolean isValid(int y, int x) {
        return (0 <= y) && (y < SIDE_LENGTH) && (0 <= x) && (x < SIDE_LENGTH);
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = 0; i < SIDE_LENGTH; i++) {
        sb.append(i + " ");
        }
        sb.append('\n');
            for (int i = 0; i < SIDE_LENGTH; i++) {
                sb.append(i+" ");
            for (int j = 0; j < SIDE_LENGTH; j++) {
                sb.append(render(state[i*SIDE_LENGTH + j]));
               sb.append(' ');
            }
            if (i < SIDE_LENGTH - 1) sb.append('\n');
        }
        return sb.toString();

    }

    private char render(Piece x) {
        if (Objects.isNull(x)) {
            return '⎵';
        }
        if (x.isKing()) {
            return switch (x.getPlayer()) {
                case PLAYER1 -> 'O';
                case PLAYER2 -> 'X';
                default -> '.';
            };
        } else {
            return switch (x.getPlayer()) {
                case PLAYER1 -> 'o';
                case PLAYER2 -> 'x';
                default -> '.';

            };
        }
    }

    public void setTurn(Player player) {
        turn =  player;
    }
}
