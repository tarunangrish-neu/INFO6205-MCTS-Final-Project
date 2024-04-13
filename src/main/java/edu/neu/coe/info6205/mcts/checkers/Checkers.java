package edu.neu.coe.info6205.mcts.checkers;

import edu.neu.coe.info6205.mcts.core.Game;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.*;

public class Checkers implements Game<Checkers> {
    @Override
    public State<Checkers> start() {
        return null;
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
         return false;
     }

     @Override
     public int player() {
         return 0;
     }

     @Override
     public Optional<Integer> winner() {
         return Optional.empty();
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

     private final Board board;
 }

}
