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

    if (solutionSequence == null) {
      System.out.println("Failure in search detected");
      System.exit(1);
    }

    Util.printResult(startTime, problem, solutionSequence);
  }

  // based on Figure 3.17 in AIMA Russel/Norvig 3rd ed
  // combined with Figure 3.7 in AIMA Russel/Norvig 3rd ed  i.e. Graph version instead of tree
  // version
  // returns a solution or failure/cutoff
  static Node recursiveDepthLimitedSearch(
      Node node, Problem problem, int limit, Stack<Node> frontier, HashSet<int[][]> explored) {
    boolean cutoffOccured = false;
    if (frontier == null) {
      frontier = new Stack<>();
      frontier.push(node);
    }
    if (explored == null) {
      explored = new HashSet<>();
    }
    while (!frontier.empty()) {
      node = frontier.pop();
      if (problem.goalTest(node.state)) {
        return node;
      } else if (limit <= 0) {
        return null; // cutoff
      } else {
        cutoffOccured = false;
      }
      explored.add(node.state);
      for (Actions action : problem.actions(node.state)) {
        ++problem.expandedCount;
        Node child = node.childNode(problem, action);
        if (!explored.contains(child.state) && !frontier.contains(child)) {
          frontier.push(child);
        }
        Node result = recursiveDepthLimitedSearch(child, problem, --limit, frontier, explored);
        if (result == null) { // result = cutoff
          cutoffOccured = true;
        } else if (result.state != null) {
          return result;
        }
      }
    }
    return (cutoffOccured) ? null : new Node(null);
  }

  // based on Figure 3.17 in AIMA Russel/Norvig 3rd ed
  // returns a solution or failure/cutoff
  static Node depthLimitedSearch(Problem problem, int limit) {
    //        return recursiveDepthLimitedSearch(new Node(problem.initialState), problem, limit);
    return recursiveDepthLimitedSearch(new Node(problem.initialState), problem, limit, null, null);
  }

  // based on Figure 3.18 in AIMA Russel/Norvig 3rd ed
  // returns a solution or failure
  // cutoff is represented by a null
  // Failure is represented by a Node with a null state
  static List<Actions> iterativeDeepeningDepthFirstSearch(Problem problem) {
    Node result;
    int infinity = 100;
    for (int depth = 1; depth < infinity; ++depth) {
      result = depthLimitedSearch(problem, depth);
      if (result == null) {
        continue;
      }
      if (result.state == null) {
        return null;
      }
      if (problem.goalTest(result.state)) {
        return result.solution();
      }
    }
    return null;
  }
}
