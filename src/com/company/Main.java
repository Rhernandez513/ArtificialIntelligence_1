package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    if (solutionSequence == null) { System.out.println("Failure detected"); System.exit(1);}
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
    return Arrays.deepEquals(this.goal, state);
  }


  int pathCost(int cost, int[][] state, Actions action, int[][] nextState) {
    return ++cost;
  }

  // returns state
  // should produce a new state object, not modify the param
  int[][] result(int[][] state, Actions action) {

    // TODO convert State to an object that internally tracks where the blank is
    Map.Entry<Integer, Integer> xy_tuple = identifyBlank(state);

    int x = xy_tuple.getKey();
    int y = xy_tuple.getValue();

    switch (action){
      case U:
// swap zero with one above it
        state[x][y] = state[x+1][y];
        state[x+1][y] = 0;
        break;
      case D:
// swap zero with one below it
        state[x][y] = state[x-1][y];
        state[x-1][y] = 0;
        break;
      case R:
// swap zero with one to the right
        state[x][y] = state[x][y+1];
        state[x][y+1] = 0;
        break;
      case L:
// swap zero with one to the left
        state[x][y] = state[x][y-1];
        state[x][y-1] = 0;
        break;
      default:
    }
    return state;
  }

  // returns available actions given state
  List<Actions> actions(int[][] state) {
    Map.Entry<Integer, Integer> blankTuple = null;
    try {
      blankTuple = identifyBlank(state);
    } catch (Exception e) {
      System.out.println("YOUR INPUT is missing a 0");
      System.exit(1);
    }

    List<Actions> actions = new ArrayList<>(Arrays.asList(Actions.U, Actions.D, Actions.L, Actions.R));

    int x = blankTuple.getKey();
    int y = blankTuple.getValue();

    switch (x) {
      case 0:
        // Disallow left
        actions.remove(Actions.L);
        break;
      case 3:
        // Disallow right
        actions.remove(Actions.R);
        break;
      default:
    }
    switch (y) {
      case 0:
        // Disallow up
        actions.remove(Actions.U);
        break;
      case 3:
        // Disallow down
        actions.remove(Actions.D);
        break;
      default:
    }

    return actions;
  }

  private Map.Entry<Integer, Integer> identifyBlank(int[][] state) {
    // I would use a Pair<Integer, Integer> to represent a tuple
    // but I can't guarantee if the grader is running OpenJDK or OracleJDK
    Map<Integer, Integer> blank = new HashMap<>();

    for(int x = 0; x < 4; ++x) {
      for(int y = 0; y < 4; ++y) {
        if(state[x][y] == 0) {
          blank.put(x, y);
          return blank.entrySet().iterator().next();
        }
      }
    }
    return null;
  }
}
