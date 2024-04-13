package edu.neu.coe.info6205.mcts.checkers;

import java.util.HashMap;

public class Board {





    private static final int  SIDE_LENGTH = 8;
    private static final int NUM_SQUARES = SIDE_LENGTH * SIDE_LENGTH;

    // state of the board
    Piece[] state;
    // origin and destination position of the most recent move
    private int fromPos = -1;
    private int toPos = -1;
    // origin position of double jump move, used to invalidate other moves during multi-move
    private int doublejumpPos = -1;
    // player's turn
    private Player turn;
    // track number of human/AI pieces on board
    public HashMap<Player, Integer> pieceCount;
    private HashMap<Player, Integer> kingCount;
}
