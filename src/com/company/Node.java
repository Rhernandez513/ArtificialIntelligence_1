package com.company;

import java.util.*;

class Node {
  int heuristic;
  int[][] state;
  int pathCost;
  Node parent;
  Actions action;

  Node(int[][] state) {
    this.state = state;
    this.pathCost = 0;
    this.heuristic = Integer.MAX_VALUE;
  }

  Node(int[][] state, int pathCost, Node parent, Actions action, int heuristic) {
    this.state = state;
    this.pathCost = pathCost;
    this.parent = parent;
    this.action = action;
    this.heuristic = heuristic;
  }

  Node childNode(Problem problem, Actions action) {
    int[][] nextState = problem.result(this.state, action);
    return new Node(
        nextState,
        problem.pathCost(this.pathCost),
        this,
        action,
        Util.calculateMisplacedSquares(nextState, problem.goal));
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
  static Comparator<Node> nodeComparator = (n1, n2) -> (int) (n1.heuristic - n2.heuristic);

  @Override
  public boolean equals(Object obj) {
    return Arrays.deepEquals(((Node) obj).state, this.state);
  }
}
