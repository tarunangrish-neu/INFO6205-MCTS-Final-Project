package edu.neu.coe.info6205.mcts.checkers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PieceTest {

    private Piece nonKingPlayer1Piece;
    private Piece nonKingPlayer2Piece;
    private Piece kingPlayer1Piece;

    @Before
    public void setUp() {
        nonKingPlayer1Piece = new Piece(Player.PLAYER1, false);
        nonKingPlayer2Piece = new Piece(Player.PLAYER2, false);
        kingPlayer1Piece = new Piece(Player.PLAYER1, true);
    }

    @Test
    public void testGetPlayer() {
        assertEquals(Player.PLAYER1, nonKingPlayer1Piece.getPlayer());
        assertEquals(Player.PLAYER2, nonKingPlayer2Piece.getPlayer());
        assertEquals(Player.PLAYER1, kingPlayer1Piece.getPlayer());
    }

    @Test
    public void testIsKing() {
        assertFalse(nonKingPlayer1Piece.isKing());
        assertFalse(nonKingPlayer2Piece.isKing());
        assertTrue(kingPlayer1Piece.isKing());
    }

    @Test
    public void testGetYMovements() {
        assertArrayEquals(new int[]{1}, nonKingPlayer1Piece.getYMovements());
        assertArrayEquals(new int[]{-1}, nonKingPlayer2Piece.getYMovements());
        assertArrayEquals(new int[]{-1, 1}, kingPlayer1Piece.getYMovements());
    }

    @Test
    public void testGetXMovements() {
        assertArrayEquals(new int[]{-1, 1}, nonKingPlayer1Piece.getXMovements());
        assertArrayEquals(new int[]{-1, 1}, nonKingPlayer2Piece.getXMovements());
        assertArrayEquals(new int[]{-1, 1}, kingPlayer1Piece.getXMovements());
    }

    @Test
    public void testInitialization() {
        assertEquals(Player.PLAYER1, nonKingPlayer1Piece.getPlayer());
        assertFalse(nonKingPlayer1Piece.isKing());

        assertEquals(Player.PLAYER2, nonKingPlayer2Piece.getPlayer());
        assertFalse(nonKingPlayer2Piece.isKing());

        assertEquals(Player.PLAYER1, kingPlayer1Piece.getPlayer());
        assertTrue(kingPlayer1Piece.isKing());
    }
}
