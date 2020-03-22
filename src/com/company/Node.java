package com.company;

import java.util.*;

class Node {
  boolean isManhattanEnabled;
  int heuristic;
  int[][] state;
  int pathCost;
  Node parent;
  Actions action;

  Node(int[][] state) {
    this.state = state;
    this.pathCost = 0;
    this.heuristic = Util.calculateMisplacedSquares(state, Util.goalState);
    this.isManhattanEnabled = false;
  }

  Node(int[][] state, boolean isManhattanEnabled) {
    this.state = state;
    this.pathCost = 0;
    this.isManhattanEnabled = isManhattanEnabled;
    this.heuristic = (this.isManhattanEnabled) ? Util.calculateManhattanDistance(this.state) : Util.calculateMisplacedSquares(this.state, Util.goalState);
  }

  Node(int[][] state, int pathCost, Node parent, Actions action, int heuristic, boolean isManhattanEnabled) {
    this.state = state;
    this.pathCost = pathCost;
    this.parent = parent;
    this.action = action;
    this.heuristic = heuristic;
    this.isManhattanEnabled = isManhattanEnabled;
  }

  Node childNode(Problem problem, Actions action) {
    return this.childNode(problem, action, false);
  }

  Node childNode(Problem problem, Actions action, boolean isManhattanEnabled) {
    int[][] nextState = problem.result(this.state, action);
    int heuristic =
        (isManhattanEnabled)
            ? Util.calculateManhattanDistance(nextState)
            : Util.calculateMisplacedSquares(nextState, problem.goal);
    return new Node(nextState, problem.pathCost(this.pathCost), this, action, heuristic, this.isManhattanEnabled);
  }

  List<Actions> solution() {
    List<Actions> sequence = new ArrayList<>();
    for (Node node : this.path()) {
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

  // Comparator anonymous class implementation
  static Comparator<Node> nodeComparator =
      (n1, n2) -> (int) ((n1.heuristic + n1.pathCost) - (n2.heuristic + n2.pathCost));

  Integer heuristicPathCost() {
    return this.heuristic + this.pathCost;
  }

  @Override
  public boolean equals(Object obj) {
    return Arrays.deepEquals(((Node) obj).state, this.state);
  }
}
