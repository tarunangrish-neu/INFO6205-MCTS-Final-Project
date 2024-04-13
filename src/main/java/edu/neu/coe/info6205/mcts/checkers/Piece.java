package edu.neu.coe.info6205.mcts.checkers;

public class Piece {

public Piece(Player player, boolean isKing) {
    this.player = player;
    this.isKing = isKing;
}

public Player getPlayer() {
    return player;
}
public boolean isKing() {
    return isKing;
}
    /**
     * Get Y-direction movements
     */
    public int[] getYMovements() {
        int[] result = new int[]{};
        if (isKing) {
            result = new int[]{-1, 1};
        } else {
            switch (player) {
                case PLAYER1:
                    result = new int[]{1};
                    break;
                case PLAYER2:
                    result = new int[]{-1};
                    break;
            }
        }
        return result;
    }

    /**
     * Get Y-direction movements
     */
    public int[] getXMovements() {
        return new int[]{-1, 1};
    }
    private final Player player;
    private final boolean isKing;


}
