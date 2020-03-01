package com.company;

import com.sun.tools.internal.xjc.model.CElement;

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

    // This calculation works, but I couldn't get the heuristic to work in the search before I ran
    // out of time
    //        int man = calculateManhattanDistance(initialState);

    // time
    final long startTime = System.nanoTime();

    // execute BFS
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
//    node <- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
//    frontier <- a priority queue ordered by PATH-COST, with _node_ as the only element
//    explored <- an empty set
//    loop do
//      if EMPTY?(frontier) then return failure
//      node <- POP(frontier) /* chooses lowest cost node in the frontier */
//      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
//      add node.STATE to explored
//      for each action in problem.ACTIONS(node.STATE) do
//        child <- CHILD-NODE(problem, node, action)
//        if child.STATE is not in explored or frontier then
//          frontier <- INSERT(child, frontier)
//        else if child.STATE is in frontier with higher PATH-COST then
//          replace that frontier node with child
    return null;
  }
}
