package edu.neu.coe.info6205.mcts.tictactoe;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.neu.coe.info6205.mcts.core.Node;

public class MCTSTest {

    @Test
    public void testInitializationAndGameStart() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode initialNode = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(initialNode);
        assertNotNull(mcts);
        assertEquals("TicTacToe\n. . .\n. . .\n. . .\n", initialNode.state().toString());
    }

    @Test
    public void testSelectMethod() {
        // Initialize the game and root node properly
        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        rootNode.explore();  // Ensure the root node has at least one child to select from, if necessary

        MCTS mcts = new MCTS(rootNode);  // Ensure MCTS is initialized with a non-null root
        Node<TicTacToe> selectedNode = mcts.select(rootNode);

        assertNotNull(selectedNode);  // Check that select does not return null
    }

    @Test
    public void testBestChildSelection() {
        TicTacToeNode rootNode = new TicTacToeNode(new TicTacToe().start());
        rootNode.explore();  // To ensure there are children to select from
        Node<TicTacToe> bestChild = new MCTS(rootNode).bestChild(rootNode);
        assertNotNull(bestChild);
        assertTrue(rootNode.children().contains(bestChild));
    }

    @Test
    public void testSimulationAndBackPropagation() {
        TicTacToeNode node = new TicTacToeNode(new TicTacToe().start());
        MCTS mcts = new MCTS(node);
        int initialPlayouts = node.playouts();
        int result = mcts.simulate(node);
        mcts.backPropagate(node, result);
        assertEquals(initialPlayouts + 1, node.playouts());
    }

    @Test
    public void testConstructor() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(root);
        assertEquals(root, MCTS.root);
    }

    @Test
    public void testRun() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(root);
        mcts.run(100); // Running with a smaller number of iterations for testability
        // Assert something based on the result of the run method
    }

    @Test
    public void testBestChild() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(root);

        // Test scenario with no children
        assertNull(mcts.bestChild(root));

    }

}