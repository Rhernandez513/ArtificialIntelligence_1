package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Main {

  public static void main(String[] args) {

    String input = "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";
    String goalString = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ";
    int[][] initialState = getStateFromSting(input);
    int[][] goalState = getStateFromSting(goalString);

    // time
    long startTime = System.nanoTime();

    // execute BFS
    List<Actions> solutionSequence = breadthFirstSearch(new Problem(initialState, goalState));

    // convert to human-readable
    StringBuilder solutionBuilder = new StringBuilder();
    for(Actions action : solutionSequence) { solutionBuilder.append(action); }
    final String solution = solutionBuilder.toString();

    // show time
    long endTime = System.nanoTime();
    System.out.println(String.format("Time elapsed: %dns", endTime - startTime));

    // calculate memory used
    int megabyte = 1024 * 1024;
    Runtime runtime = Runtime.getRuntime();
    long memory = getMemoryUsed();

    // Print result
    System.out.print("Moves: " + solution);
    System.out.print( "Memory used by JVM at Runtime: " + getMemoryUsed() + "MB");
    System.out.println("\n Java uses a whole LOT of memory...");
  }

  static int[][] getStateFromSting(String input) {
    int[][] state = new int[4][4];
    Queue<String> vals = new ArrayDeque<>(Arrays.asList(input.split(" ")));
    for(int i = 0; i < 4; ++i) {
      for(int j = 0; j < 4; ++j) {
        state[i][j] = Integer.parseInt(vals.poll());
      }
    }
    return state;
  }

  static long getMemoryUsed() {
    int megabyte = 1024 * 1024;
    Runtime runtime = Runtime.getRuntime();
    return (runtime.totalMemory() - runtime.freeMemory() / megabyte);
  }

  // "done"
  static List<Actions> breadthFirstSearch(Problem problem) {
    Node node = new Node(problem.initialState, 0);
    if (problem.goalTest(node.state)) { return node.solution(); }

    Queue<Node> frontier = new ArrayDeque<>();
    frontier.add(node);
    Set<int[][]> explored = new HashSet<>();

    while (!frontier.isEmpty()) {
      node = frontier.poll();
      explored.add(node.state);
      for (Actions action : problem.actions(node.state)) {
        Node child = node.childNode(problem, action);
        if (!(explored.contains(child.state) || frontier.contains(child))) {
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

class Node {
  int manhattanDistance;
  int[][] state;
  int pathCost;
  Node parent;
  Actions action;

  Node(int[][] state, int pathCost) {
    this.state = state;
    this.pathCost = pathCost;
  }

  Node(int[][] state, Node parent, Actions action, int pathCost) {
    this.state = state;
    this.parent = parent;
    this.action = action;
    this.pathCost = pathCost;
  }

//  String expand() {
//    return null;
//  }

  // "done"
  Node childNode(Problem problem, Actions action) {
    int[][] nextState = problem.result(this.state, action);
    Node nextNode = new Node(nextState, this, action, problem.pathCost(1, this.state, action, nextState));
    return nextNode;
  }

  List<Actions>solution() {
    List<Actions> sequence = new ArrayList<>();
    for(Node node : this.path()) {
      sequence.add(node.action);
    }
    return sequence;
  }

  List<Node> path() {
    Node node = this;
    List<Node> path_back = new ArrayList<>();
    while (node != null) {
      path_back.add(node);
      node = node.parent;
    }
    Collections.reverse(path_back);
    return path_back;
  }

  @Override
  public boolean equals(Object obj) {
    return (((Node) obj).state == this.state) ? true : false;
  }
}

enum Actions {
    U, //  UP,
    D, //  DOWN,
    L, //  LEFT,
    R  //  RIGHT
}

class Problem {
  int[][] initialState;
  int[][] goal;

  Problem(int[][] initialState, int[][] goal) {
    this.initialState = initialState;
    this.goal = goal;
  }

  boolean goalTest(int[][] state) {
    return this.goal.equals(state);
  }


  int pathCost(int cost, int[][] state, Actions action, int[][] nextState) {
//          """Return the cost of a solution path that arrives at state2 from
//        state1 via action, assuming cost c to get up to state1. If the problem
//        is such that the path doesn't matter, this function will only look at
//        state2. If the path does matter, it will consider c and maybe state1
//        and action. The default method costs 1 for every step in the path."""
    return ++cost;
  }
  // returns state
  int[][] result(int[][] state, Actions action) {
    return null;
  }
  // returns available actions given state
  List<Actions> actions(int[][] state) {
    return null;
  }
}
