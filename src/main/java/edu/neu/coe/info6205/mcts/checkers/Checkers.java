package edu.neu.coe.info6205.mcts.checkers;

import edu.neu.coe.info6205.mcts.core.Game;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.*;

public class Checkers implements Game<Checkers> {

    public static void main(String[] args) {
    State<Checkers> theGame = new Checkers().start();
    CheckersState checkersState = (CheckersState) theGame;
    String output = ((CheckersState) theGame).render();
        System.out.println(output);
    }

    @Override
    public State<Checkers> start() {
        return new CheckersState();
    }

    @Override
    public int opener() {
        return 0;
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
         return List.of();
     }

     @Override
     public State<Checkers> next(Move<Checkers> move) {
         return null;
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
