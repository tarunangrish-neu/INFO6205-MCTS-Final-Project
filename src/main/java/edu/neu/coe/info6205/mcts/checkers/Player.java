package edu.neu.coe.info6205.mcts.checkers;

public enum Player {
    PLAYER1,
    PLAYER2;

    public Player getOpposite() {
        Player result = null;
        if (this == PLAYER1) {
            result = PLAYER2;
        } else if (this == PLAYER2) {
            result = PLAYER1;
        }
        if (result == null) {
            throw new RuntimeException("Null player has no opposite.");
        }
        return result;
    }
}
