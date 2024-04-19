package edu.neu.coe.info6205.mcts.checkers;
import edu.neu.coe.info6205.mcts.core.State;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private Board board2;
    private MCTs mcTs;
    @Before
    public void setUp() {
        board = new Board().initialize();
        mcTs = new MCTs(Player.PLAYER1, 6);
        State<Checkers> theGame = new Checkers().start();
        Checkers.CheckersState checkersState = (Checkers.CheckersState) theGame;
//        Board board2 = mcTs.move(((Checkers.CheckersState) theGame).board, Player.PLAYER1);

    }

    @Test
    public void testBoardInitialization() {
        assertNotNull(board);
        assertEquals(12, (int) board.pieceCount.get(Player.PLAYER1));
        assertEquals(12, (int) board.pieceCount.get(Player.PLAYER2));
        assertEquals(0, (int) board.kingCount.get(Player.PLAYER1));
        assertEquals(0, (int) board.kingCount.get(Player.PLAYER2));
        assertEquals(Player.PLAYER1, board.getTurn());
        assertEquals(7, board.getSuccessors(Player.PLAYER1).size());
        assertEquals(false, board.isGameOver());
        assertEquals(0,board.computeHeuristic(Player.PLAYER1));
        assertEquals(1,board.computeHeuristic(Player.PLAYER2));
    }

    @Before
    public void setUp2() {

        State<Checkers> theGame = new Checkers().start();
        Board board = mcTs.move(((Checkers.CheckersState) theGame).getBoard(), Player.PLAYER1);
        Checkers.CheckersState checkersState = (Checkers.CheckersState) theGame;
        board2 = new Board().initialize();
        MCTs mcTs = new MCTs(Player.PLAYER1, 6);
         board2 = mcTs.move(((Checkers.CheckersState) theGame).getBoard(), Player.PLAYER1);
        theGame = theGame.next(new Checkers.CheckersMove(theGame.player(), board));
        checkersState = (Checkers.CheckersState) theGame;
        System.out.println("System1 Moved!");
    }

    @Test
    public void testMCTS() {

    }


}
