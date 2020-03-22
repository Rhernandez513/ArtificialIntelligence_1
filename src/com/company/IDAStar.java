package com.company;

import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class IDAStar {
  public static void main(String[] args) {

    final String input = (args.length > 0) ? args[0] : "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";

    final int[][] initialState = Util.getStateFromSting(input);
    final int[][] goalState = Util.goalState;

    Problem problem = new Problem(initialState, goalState);
    run(problem, false);
    run(problem, true);
  }

  private static void run(Problem problem, boolean isManhattan) {
    problem.expandedCount = 0;
    // time
    final long startTime = System.nanoTime();
    List<Actions> solutionSequence = null;
    try {
      solutionSequence = IDAStarSearch(problem, isManhattan);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

    if (solutionSequence == null) {
      System.out.println("Failure in search detected");
      System.exit(1);
    }

    Util.printResult(startTime, problem, solutionSequence);
  }

  // The key addition of Depth Limiting is that on each iteration, the depth limit is increased
  // by the smallest delta f(n) the limit
  //
  // f(n) = g(n) + h(n)
  // g(n) = step cost from node to goal
  // h(n) = heuristic of node (manhattan || misplaced)
  static List<Actions> IDAStarSearch(Problem problem, boolean isManhattan) throws Exception {
    // I've ran a test case that ran the GC out of memory with only a depth limit of 100
    // but if you want you can try  Integer.MAX_VALUE
    int infinity = 100;
    for (int depth = 1; depth < infinity; /**/ ) {
      Object result = depthLimitedAStarSearch(problem, isManhattan, depth);
      if (result == null) { return null; }
      if(result instanceof Integer) {
        depth += (Integer) result;
        continue;
      }
      if(result instanceof List) { return (List) result; }
      throw new Exception("Something unexpected happened");
    }
    return null;
  }

  // Based on AStar in the same package, with the addition of checking if the depth limit has been exceeded on node generation
  // returns a solution, or failure
  static Object depthLimitedAStarSearch(Problem problem, boolean isManhattanEnabled, int limit) {

    Node node = new Node(problem.initialState, isManhattanEnabled);

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
        Node child = node.childNode(problem, action, isManhattanEnabled);
        ++problem.expandedCount; // for metric tracking, not part of algo
        // Check if Depth-Limit has been exceeded
        if(node.heuristicPathCost() > limit) { return node.heuristicPathCost(); }
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
