package com.company;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AStar {
  public static void main(String[] args) {

    final String input = (args.length > 0) ? args[0] : "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";

    final int[][] initialState = Util.getStateFromSting(input);
    final int[][] goalState = Util.getStateFromSting("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ");

    // time
    final long startTime = System.nanoTime();

    // execute A* Graph Search
    Problem problem = new Problem(initialState, goalState);
    final List<Actions> solutionSequence = aStarSearch(problem);

    if (solutionSequence == null) {
      System.out.println("Failure in search detected");
      System.exit(1);
    }

    Util.printResult(startTime, problem, solutionSequence);
  }

  // Loosely based on Figure 3.14 in AIMA 3rd Ed plus description in section 3.5.2
  // A* search is algorithmically identical to UNIFORM-COST-SEARCH (Fig 3.14)
  // with the exception of the added heuristic
  // f(n) = g(n) + h(n)
  static List<Actions> aStarSearch(Problem problem) { // returns a solution, or failue

    Node node = new Node(problem.initialState);

    Queue<Node> frontier = new PriorityQueue<>(Node.nodeComparator);
    frontier.add(node);

    Set<int[][]> explored = new HashSet<>();

    while (!frontier.isEmpty()) {
      node = frontier.poll();
      if (problem.goalTest(node.state)) {
        return node.solution();
      }
      explored.add(node.state);

      for (Actions action : problem.actions(node.state)) {
        Node child = node.childNode(problem, action);
        ++problem.expandedCount; // for metric tracking, not part of algo
        if (!explored.contains(child.state) && !frontier.contains(child)) {
          frontier.add(child);
        } else if (frontier.contains(child)) {
       // else if child.STATE is in frontier with higher PATH-COST then
          for (Node n : frontier) {
            if (n.equals(child) && (n.pathCost > child.pathCost)) {
              frontier.remove(n);
              frontier.add(child);
              break;
            }
          }
        }
      }
    }
    return null;
  }
}
