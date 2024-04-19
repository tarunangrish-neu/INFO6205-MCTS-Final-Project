package edu.neu.coe.info6205.mcts.checkers;

import java.util.ArrayList;
import java.util.Random;

public class MCTs {

    private final Player player;
    private final Random rand = new Random();
    private final int depth;

    double c = Math.sqrt(2);
    public MCTs(Player player, int depth) {
        this.player = player;
        this.depth = depth;
    }

    public Board move(Board state, Player player) {
        if (state.getTurn() == player) {
            ArrayList<Board> successors = state.getSuccessors();
            return MCTSMove(successors);
        } else {
            throw new RuntimeException("Cannot generate moves for player if it's not their turn");
        }
    }

    public Board MCTSMove(ArrayList<Board> successors) {
        if (successors.size() == 1) {
            return successors.get(0);
        }
        double bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        ArrayList<Board> equalBests = new ArrayList<>();
        for (Board succ : successors) {
            int[] val = mct(succ, depth,alpha, beta);
            int winScore = val[0];
            int numberOfVisits = val[1];
            int totalVisits = successors.size()+ numberOfVisits;
            // calculate UCT
            double UCT =(winScore / (double) numberOfVisits) + c * Math.sqrt(Math.log(totalVisits) / numberOfVisits);
            if (UCT > bestScore) {
                bestScore = UCT;
                equalBests.clear();
            }
            if (UCT == bestScore) {
                equalBests.add(succ);
            }
        }
        if (equalBests.size() > 1) {
            System.out.println(player.toString() + " choosing random best move");
        }
        // choose randomly from equally scoring best moves
        return randomMove(equalBests);
    }

    private int[] mct(Board node, int depth , int alpha, int beta) {
        if (node.isGameOver() || depth ==0) {
            return new int[] {node.computeHeuristic(this.player),1};
        }
        if (node.getTurn() == player) {
            // player tries to maximize this value
            int v = Integer.MIN_VALUE;
            int visits =0;
            for (Board child : node.getSuccessors()) {
                int[] mctValues = mct(child, depth-1,alpha, beta);
                v = Math.max(v, mctValues[0]);
                visits += mctValues[1];

            }
            return new int[]{v, visits};
        }

        if (node.getTurn() == player.getOpposite()) {
            // opponent goes random
            int v = Integer.MAX_VALUE;
            int visits =0;
            for (Board child : node.getSuccessors()) {
                int[] mctValues = mct(child, depth-1,alpha, beta);
                v = Math.max(v, mctValues[0]);
                visits += mctValues[1];

            }
            return new int[]{v,visits};
        }

        throw new RuntimeException("Error in MCTs algorithm");
    }

    Board randomMove(ArrayList<Board> successors) {
        if (successors.isEmpty()) {
            throw new RuntimeException("Can't randomly choose from empty list.");
        }
        int i = rand.nextInt(successors.size());
        return successors.get(i);
    }
}
