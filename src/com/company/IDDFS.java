package com.company;

import java.util.*;

public class IDDFS {

    public static void main(String[] args) {

        final String input = (args.length > 0) ? args[0] : "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";

        final int[][] initialState = Util.getStateFromSting(input);
        final int[][] goalState = Util.getStateFromSting("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ");

        // time
        final long startTime = System.nanoTime();

        // execute ID_DFS
        Problem problem = new Problem(initialState, goalState);
        final List<Actions> solutionSequence = iterativeDeepeningDepthFirstSearch(problem);

        if (solutionSequence == null) { System.out.println("Failure in search detected"); System.exit(1); }

        Util.printResult(startTime, problem, solutionSequence);
    }

    // based on Figure 3.17 in AIMA Russel/Norvig 3rd ed
    // returns a solution or failure/cutoff
    static Node recursiveDepthLimitedSearch(Node node, Problem problem, int limit) {
        if (problem.goalTest(node.state)) { return node; }
        else if (limit <= 0) { return null; } // return "cutoff"
        else {
            boolean cutoffOccurred = false;
            for(Actions action : problem.actions(node.state)) {
                ++problem.expandedCount;
                Node child = node.childNode(problem, action);
                Node result = recursiveDepthLimitedSearch(child, problem, --limit);
                // if result = cutoff
                if (result == null) { cutoffOccurred = true; }
                // if result is not a failure
                else if (result.state != null) { return result; }
            }
            // return "cutoff" else "failure"
            return (cutoffOccurred) ? null : new Node(null);
        }
    }

    // based on Figure 3.17 in AIMA Russel/Norvig 3rd ed
    // returns a solution or failure/cutoff
    static Node depthLimitedSearch(Problem problem, int limit) {
        return recursiveDepthLimitedSearch(new Node(problem.initialState), problem, limit);
    }

    // based on Figure 3.18 in AIMA Russel/Norvig 3rd ed
    // returns a solution or failure
    // cutoff is represented by a null
    // Failure is represented by a Node with a null state
    static List<Actions> iterativeDeepeningDepthFirstSearch(Problem problem) {
        Node result = null;
        int infinity = Integer.MAX_VALUE;
        for(int depth = 1; depth < infinity; ++depth) {
            result = depthLimitedSearch(problem, depth);
            if(result == null) { continue; }
            if(result.state == null) { return null; }
            if (problem.goalTest(result.state)) { return result.solution(); }
        }
        return null;
    }
}

