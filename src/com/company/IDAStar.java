package com.company;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class IDAStar {
  public static void main(String[] args) {
    System.out.println("Use Both Heuristics or only ManhattanDistance? y/n");
    Scanner s = new Scanner(System.in);
    Character i = s.nextLine().charAt(0);

    // one iter
//    final String input = (args.length > 0) ? args[0] : "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";
    // two iter
    final String input = (args.length > 0) ? args[0] : "1 2 3 4 5 10 6 0 9 7 11 8 13 14 15 12";
    // ONLY USE MANHATTAN WITH THESE BELOW THIS LINE, COMMENT OUT LINES 30-32 TO DISABLE Misplaced Tiles
    // five iter
//    final String input = (args.length > 0) ? args[0] : "5 2 4 8 10 3 11 14 6 0 9 12 13 1 15 7";
    // nine iter
//    final String input = (args.length > 0) ? args[0] : "5 2 4 8 10 0 3 14 13 6 11 12 1 15 9 7";

    final int[][] initialState = Util.getStateFromSting(input);
    final int[][] goalState = Util.goalState;

    Problem problem = new Problem(initialState, goalState);
    if(i.equals('y')) {
      run(problem, false);
    }
    run(problem, true);
  }

  private static void run(Problem problem, boolean isManhattan) {
    problem.expandedCount = 0;
    // time
    final long startTime = System.nanoTime();
    List<Actions> solutionSequence = null;
    try {
      solutionSequence = IDAStarSearch(problem, isManhattan, Util.calculateManhattanDistance(problem.initialState));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

    if (solutionSequence == null) {
      System.out.println("Failure in search detected");
      System.exit(1);
    }

    Util.printResult(startTime, problem, solutionSequence, isManhattan);
  }

  // The key addition of Depth Limiting is that on each iteration, the depth limit is increased
  // by the smallest delta f(n) the limit
  //
  // f(n) = g(n) + h(n)
  // g(n) = step cost from node to goal
  // h(n) = heuristic of node (manhattan || misplaced)
  static List<Actions> IDAStarSearch(Problem problem, boolean isManhattan, int initialDepth) throws Exception {
    // I've ran a test case that ran the GC out of memory with only a depth limit of 100
    // but if you want you can try  Integer.MAX_VALUE
    int infinity = Integer.MAX_VALUE;
    System.out.println(String.format("Using %d for Infinity", infinity));
    System.out.print(String.format("Running AStar with depth of: "));
    for (int depth = initialDepth; depth < infinity; /**/ ) {
      System.out.print(String.format("%d..", depth));
      Object result = depthLimitedAStarSearch(problem, isManhattan, depth);
      if (result == null) { return null; }
      if(result instanceof Integer) {
        depth += (Integer) result;
        continue;
      }
      if(result instanceof List) {
        System.out.println();
        return (List) result;
      }
      throw new Exception("\nSomething unexpected happened");
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
        // Check for goal state at time of generation
        if (problem.goalTest(node.state)) { return node.solution(); }
        // Check if Depth-Limit has been exceeded
        if(node.heuristicPathCost() > limit) { return node.heuristicPathCost(); }
        if (!explored.contains(child.state) && !frontier.contains(child)) {
          frontier.add(child);
        }
      }
    }
    return null;
  }
}
