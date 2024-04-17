package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.Node;

import java.util.Comparator;

public class UCT {
    public static double calculateUCTValueForNode(Node<TicTacToe> parent, Node<TicTacToe>child){
        double c = Math.sqrt(2);
        int totalVisits = parent.playouts();
        int winScore = child.wins();
        int numberOfVisits = child.playouts();
        if(numberOfVisits == 0) return Double.MAX_VALUE;
        return (winScore / (double) numberOfVisits) + c * Math.sqrt(Math.log(totalVisits) / numberOfVisits);
    }

    public static Node<TicTacToe> findNodeWithHighestUCT(Node<TicTacToe> node){
        return node.children().stream()
                .max(Comparator.comparing(c -> calculateUCTValueForNode(node, c)))
                .orElseThrow(() -> new IllegalStateException("No Children Nodes Found"));
    }
}
