package edu.neu.coe.info6205.mcts.tictactoe;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {
    public static TicTacToeNode root;
    Random random = new Random();
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        root = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(root);
        mcts.run(100000);
            if (root.children().isEmpty()) {
                System.out.println("No moves available.");
            } else {
                Node<TicTacToe> bestMove = mcts.bestChild(root);
                if (bestMove != null) {
                    System.out.println("Recommended move: " + bestMove.state().toString());
                } else {
                    System.out.println("No best move could be determined.");
                }
        }

        // This is where you process the MCTS to try to win the game.
    }
    public void run(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<TicTacToe> node = select(root);
            int result = simulate(node);
            backPropagate(node, result);
        }
    }
    Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf()) {
            if (!node.children().isEmpty()) {
                node = bestChild(node);
            } else {
                node.explore();
                return node;
            }
        }
        return node;
    }

    Node<TicTacToe> bestChild(Node<TicTacToe> node) {
        if (node.children().isEmpty()) {
            return null;
        }
        return node.children().stream()
                .max(Comparator.comparingDouble(this::UCT))
                .orElseThrow(() -> new IllegalStateException("No best child found, but children list is not empty"));
    }


    public double UCT(Node<TicTacToe> node) {
        double c = Math.sqrt(2);
        return node.wins() / (double) node.playouts() +
                c * Math.sqrt(Math.log(node.getParent().playouts()) / (double) node.playouts());
    }

    int simulate(Node<TicTacToe> node) {
        State<TicTacToe> state = node.state();
        while (!state.isTerminal()) {
            List<Move<TicTacToe>> moves = new ArrayList<>(state.moves(state.player()));
            Move<TicTacToe> move = moves.get(random.nextInt(moves.size()));
            state = state.next(move);
        }
        return state.winner().orElse(-1);
    }

    /* Result Tree and it's interpretations
    -1 -> Draw
    1 -> X is the Winner
    0 -> O is the winner
     */

    void backPropagate(Node<TicTacToe> node, int result) {
        while (node != null) {
            int playout = node.playouts();
            node.setPlayouts(playout+1);
            int win = node.wins();
            if ((node.state().player() == 1 && result == 1)) { //Assuming X is the winner in this case
                node.setWins(win + 2);

            }else if((node.state().player() == 0 && result == 0)){ //Assuming O is the winner in this case
                node.setWins(win - 1);
            }
            else if (result == -1) { //If there is a scenario, where we have to make the game as a draw and force the algorithm to look for scenarios where Draw has the highest reward
                node.setWins(win + 1);
            }
            node = node.getParent();
        }
    }

    public MCTS(TicTacToeNode root) {
        MCTS.root = root;
    }

}