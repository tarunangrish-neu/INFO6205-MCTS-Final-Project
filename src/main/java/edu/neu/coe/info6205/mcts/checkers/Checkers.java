package edu.neu.coe.info6205.mcts.checkers;

import edu.neu.coe.info6205.mcts.core.Game;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.*;

public class Checkers implements Game<Checkers> {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
    State<Checkers> theGame = new Checkers().start();
    CheckersState checkersState = (CheckersState) theGame;
    String output = ((CheckersState) theGame).render();
        System.out.println(ANSI_RED + output + ANSI_RESET);
//        ArrayList<Board> list = checkersState.board.getSuccessors(Player.PLAYER1);
    }

    @Override
    public State<Checkers> start() {
        return new CheckersState();
    }

    @Override
    public int opener() {
        return 0;
    }

    public Board playerMove (Board board,int fromPos, int dx, int dy) {
        int toPos = fromPos + dx + board.getSideLength() * dy;
        if (toPos > board.state.length) {
//            return MoveFeedback.NOT_ON_BOARD;
            throw new RuntimeException("Move not on board" );
        }
        // check for forced jumped
        ArrayList<Board> jumpSuccessors = board.getSuccessors(true);
        boolean jumps = !jumpSuccessors.isEmpty();
        if (jumps) {
            for (Board succ : jumpSuccessors) {
                if (succ.getFromPos() == fromPos && succ.getToPos() == toPos) {
//                    updateState(succ);
                    System.out.println("Move Successful");
                    return succ;
                }
            }
            throw new RuntimeException("It's a forced jump" );
//            return MoveFeedback.FORCED_JUMP;
        }
//        return MoveFeedback.UNKNOWN_INVALID;
        throw new RuntimeException("Check your move again");
    }

    static class CheckersMove implements Move<Checkers> {

        @Override
        public int player() {
            return player;
        }

        public CheckersMove(int player, Board board) {
            this.player = player;
            this.board = board;
        }

        private final int player;
        private final Board board;
    }

 class CheckersState implements State<Checkers> {

     @Override
     public Checkers game() {
         return Checkers.this;
     }

     @Override
     public boolean isTerminal() {
         return board.isGameOver();
     }

     @Override
     public int player() {
         return switch(board.getTurn()) {
             case  PLAYER1-> PLAYER1;
             case  PLAYER2-> PLAYER2;
             default -> 0;
         };
     }

     @Override
     public Optional<Integer> winner() {
         boolean isOver = isTerminal();
         if(isOver) {
             boolean player1Status = board.pieceCount.get(Player.PLAYER1) == 0;
             if (player1Status) {
                 return Optional.of(PLAYER1);
             }
             else {
                 return Optional.of(PLAYER2);
             }
         } else return Optional.empty();
     }

     @Override
     public Random random() {
         return null;
     }

     @Override
     public Collection<Move<Checkers>> moves(int player) {
        ArrayList<Move<Checkers>> moves = new ArrayList<>();
        ArrayList<Board> nextMoves = board.getSuccessors(player);
        for (Board nextBoard : nextMoves) { moves.add(new CheckersMove(player, nextBoard)); }

         return moves;
     }

     @Override
     public State<Checkers> next(Move<Checkers> move) {
         CheckersMove checkersMove = (CheckersMove) move;
         Board board = checkersMove.board;
         return new CheckersState(board);
     }

     @Override
     public Iterator<Move<Checkers>> moveIterator(int player) {
         return State.super.moveIterator(player);
     }

     @Override
     public Move<Checkers> chooseMove(int player) {
         return State.super.chooseMove(player);
     }

     public String render () {
         return board.render();
     }

     public CheckersState(Board board) {
         this.board = board;
     }

     public CheckersState () {
         Board board = new Board();
        Board initialBoard = board.initialize();
        this.board = initialBoard;
     }

     private final Board board;
 }
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 0;
}
