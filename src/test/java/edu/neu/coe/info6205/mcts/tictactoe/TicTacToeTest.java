package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.State;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.Assert.*;

public class TicTacToeTest {

    /**
     *
     */
    @Test
    public void runGame() {
        long seed = 0L;
        TicTacToe target = new TicTacToe(seed);
        int mctsWon = 0;
        int randomWon = 0;
        int draw = 0;
        for(int i = 0; i < 100; i++){
            State<TicTacToe> state = target.runGame();
            Optional<Integer> winner = state.winner();
            if(winner.isPresent()){
                if(winner.get() == Integer.valueOf(TicTacToe.X)) mctsWon +=1;
                else if (winner.get() == Integer.valueOf(TicTacToe.O)) randomWon += 1;
            }

            else draw +=1;
        }
        System.out.println("MCTS won " + mctsWon);
        System.out.println("Random won " + randomWon);
        System.out.println("Draw " + draw);


//        if (winner.isPresent()) assertEquals(Integer.valueOf(TicTacToe.O), winner.get());
//        else fail("no winner");
    }
    @Test
    public void testPlayerAlternation() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> initialState = game.start();
        int firstPlayer = initialState.player();

        State<TicTacToe> nextState = initialState.next(initialState.chooseMove(firstPlayer));
        assertNotEquals(firstPlayer, nextState.player());
    }

    @Test
    public void testTerminalState() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.runGame();
        assertTrue(state.isTerminal());
    }

    @Test
    public void testStartingPosition() {

        Position startingPos = TicTacToe.startingPosition();

        assertNotNull(startingPos);

        assertEquals(". . .\n. . .\n. . .", startingPos.render());
    }

    @Test
    public void testPosition() {
        Position expectedPosition = Position.parsePosition("X . .\n. O .\n. . X", TicTacToe.blank);
        TicTacToe.TicTacToeState ticTacToeState = new TicTacToe().new TicTacToeState(expectedPosition);

        Position actualPosition = ticTacToeState.position();

        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testRunGame() {
        TicTacToe ticTacToe = new TicTacToe();
        State<TicTacToe> state = ticTacToe.runGame();
        assertTrue(state.isTerminal());
    }

    @Test
    public void testOpener() {
        TicTacToe ticTacToe = new TicTacToe();
        assertEquals(TicTacToe.X, ticTacToe.opener());
    }

    @Test
    public void testStart() {
        TicTacToe ticTacToe = new TicTacToe();
        State<TicTacToe> state = ticTacToe.start();
        assertNotNull(state);
        assertFalse(state.isTerminal());
    }

    @Test
    public void testPlayer() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertEquals(TicTacToe.X, state.player());
    }

    @Test
    public void testWinner() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertFalse(state.winner().isPresent());
    }

    @Test
    public void testRandom() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertNotNull(state.random());
    }

    @Test
    public void testMoves() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertFalse(state.moves(TicTacToe.X).isEmpty());
    }

    @Test
    public void testNext() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertFalse(state.next(new TicTacToe.TicTacToeMove(TicTacToe.X, 0, 0)).isTerminal());
    }

    @Test
    public void testIsTerminal() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        assertFalse(state.isTerminal());
    }

}