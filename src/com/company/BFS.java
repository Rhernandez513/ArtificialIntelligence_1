package com.company;

import java.util.*;

public class BFS {

  public static void main(String[] args) {

    final String input = (args.length > 0) ? args[0] : "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";

    final int[][] initialState = Util.getStateFromSting(input);
    final int[][] goalState = Util.getStateFromSting("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ");

    // This calculation works, but I couldn't get the heuristic to work in the search before I ran
    // out of time
    //        int man = calculateManhattanDistance(initialState);

    // time
    final long startTime = System.nanoTime();

    // execute BFS
    Problem problem = new Problem(initialState, goalState);
    final List<Actions> solutionSequence = breadthFirstSearch(problem);

    if (solutionSequence == null) {
      System.out.println("Failure in search detected");
      System.exit(1);
    }

    Util.printResult(startTime, problem, solutionSequence);
  }

  static List<Actions> breadthFirstSearch(Problem problem) {
    Node node = new Node(problem.initialState);
    if (problem.goalTest(node.state)) {
      return node.solution();
    }

    Queue<Node> frontier = new PriorityQueue<>(Node.nodeComparator);

    frontier.add(node);
    Set<int[][]> explored = new HashSet<>();

    while (!frontier.isEmpty()) {
      node = frontier.poll();
      explored.add(node.state);
      for (Actions action : problem.actions(node.state)) {
        Node child = node.childNode(problem, action);
        ++problem.expandedCount;
        if (!explored.contains(child.state) || !frontier.contains(child)) {
          if (problem.goalTest(child.state)) {
            return child.solution();
          }
          frontier.add(child);
        }
      }
    }
    return null;
  }
}
