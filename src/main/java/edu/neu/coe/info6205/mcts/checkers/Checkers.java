package edu.neu.coe.info6205.mcts.checkers;
import edu.neu.coe.info6205.mcts.checkers.MCTs;
import edu.neu.coe.info6205.mcts.core.Game;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.*;

public class Checkers implements Game<Checkers> {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


//    String output = ((CheckersState) theGame).render();
//        System.out.println(ANSI_RED + output + ANSI_RESET);
//        ArrayList<Board> list = checkersState.board.getSuccessors(Player.PLAYER1);
        int player1wins = 0;
        int player2wins = 0;
        int gameIterations = 8;
        int depth =7;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i< gameIterations; i++) {
            State<Checkers> theGame = new Checkers().start();
            CheckersState checkersState = (CheckersState) theGame;
            System.out.println(checkersState.board.getSuccessors(Player.PLAYER1).size());
            while (!theGame.isTerminal()) {
                System.out.println("Current state: ");
                System.out.println(((CheckersState) theGame).render());
                if (theGame.player() == 0) {
                    // human move
                    System.out.println("-------PLAYER 1's MOVE--------");
                    //----------------HUMAN INPUT CODE ------------------
//                ArrayList<Board> list = checkersState.board.getSuccessors(Player.PLAYER1);
//                System.out.println("choose one of the move from the list below by entering the number");
//                for (int i = 0; i < list.size(); i++) {
//                    System.out.println(i+1 + ": From Position (X):" + (list.get(i).getFromPos()%8) + " (Y):" +
//                            (list.get(i).getFromPos()/8) + " to Position (X):" + (list.get(i).getToPos()%8) +
//                            "(Y):"+(list.get(i).getToPos()/8));
//                }
//                int chosenMove = scanner.nextInt()-1;
//                System.out.println(chosenMove);
//                System.out.println("You choose " + chosenMove+1 + " from the list");
//                theGame = theGame.next(new CheckersMove(theGame.player(),list.get(chosenMove)));
//                checkersState = (CheckersState) theGame;

                    // ----------------------- AI VS AI ------------------
                    MCTs mcTs = new MCTs(Player.PLAYER1, depth);
                    Board board = mcTs.move(((CheckersState) theGame).board, Player.PLAYER1);
                    theGame = theGame.next(new CheckersMove(theGame.player(), board));
                    checkersState = (CheckersState) theGame;
                    System.out.println("System1 Moved!");
                } else {
                    //MCTs move
                    System.out.println("-------PLAYER 2's MOVE(MCTS)--------");
//                ArrayList<Board> list = checkersState.board.getSuccessors(Player.PLAYER2);
                    MCTs mcTs = new MCTs(Player.PLAYER2, depth);
                    Board board = mcTs.move(((CheckersState) theGame).board, Player.PLAYER2);
                    theGame = theGame.next(new CheckersMove(theGame.player(), board));
                    checkersState = (CheckersState) theGame;
                    System.out.println("System2 Moved!");
                }
            }
            System.out.println(theGame.winner().get());
            if(theGame.winner().get()==(Player.PLAYER1.ordinal())) {
                player1wins++;
            } else if(theGame.winner().get()==(Player.PLAYER2.ordinal())){
                player2wins++;
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time for Checkers: " + totalTime + "ms");
        System.out.println("Player 1 wins: " + player1wins);
        System.out.println("Player 2 wins: " + player2wins);
    }

    @Override
    public State<Checkers> start() {
        return new CheckersState();
    }

    @Override
    public int opener() {
        return 0;
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

     public Board getBoard() {
         return board;
     }

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
             boolean player1Status = board.pieceCount.get(Player.PLAYER2) == 0;
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
    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
}
