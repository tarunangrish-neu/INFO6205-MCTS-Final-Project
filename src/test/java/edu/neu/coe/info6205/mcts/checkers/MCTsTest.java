package edu.neu.coe.info6205.mcts.checkers;

import edu.neu.coe.info6205.mcts.checkers.Board;
import edu.neu.coe.info6205.mcts.checkers.MCTs;
import edu.neu.coe.info6205.mcts.checkers.Player;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MCTsTest {

    private MCTs mcts;
    private Board initialBoard;

    @Before
    public void setUp() {
        // Initialize MCTs with a mock player and depth for testing
        Player player = Player.PLAYER1;
        int depth = 3;
        mcts = new MCTs(player, depth);

        // Create an initial board state for testing
        initialBoard = new Board().initialize();
    }

    @Test
    public void testMove_PlayerTurn_SuccessfulMove() {
        // Test moving with the player's turn
        initialBoard.setTurn(Player.PLAYER1);

        Board result = mcts.move(initialBoard, Player.PLAYER1);

        assertNotNull(result);
        // Assert additional conditions based on expected behavior
        assertNotEquals(initialBoard, result); // Ensure the board has changed
    }

    @Test
    public void testMove_OpponentTurn_ExceptionThrown() {
        // Set the turn to opponent's turn
        initialBoard.setTurn(Player.PLAYER2);

        // Test that an exception is thrown when trying to move during the opponent's turn
        assertThrows(RuntimeException.class, () -> mcts.move(initialBoard, Player.PLAYER1));
    }

    @Test
    public void testMCTSMove_SingleSuccessor_ReturnsSuccessor() {
        // Create a mock board with a single successor
        ArrayList<Board> successors = new ArrayList<>();
        successors.add(initialBoard.deepCopy());

        Board result = mcts.MCTSMove(successors);

        assertNotNull(result);
        assertEquals(initialBoard, result); // Ensure the returned board is the only successor
    }

    @Test
    public void testRandomMove_NonEmptySuccessors_ReturnsRandom() {
        // Create a list of successors with multiple boards
        ArrayList<Board> successors = new ArrayList<>();
        successors.add(initialBoard.deepCopy());
        successors.add(initialBoard.deepCopy());
        successors.add(initialBoard.deepCopy());

        Board result = mcts.randomMove(successors);

        assertNotNull(result);
        assertTrue(successors.contains(result)); // Ensure the returned board is one of the successors
    }
}
