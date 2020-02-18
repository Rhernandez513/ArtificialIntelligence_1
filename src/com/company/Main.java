package com.company;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

      // TODO
      // formulate state object from string input

      // time
      long startTime = System.nanoTime();

      // execute BFS
//      public static String breadthFirstSearch(String problem);

      // show time
      long endTime = System.nanoTime();
      System.out.println(String.format("Time elapsed: %dns", endTime - startTime));

      // calculate memory used
      int megabyte = 1024 * 1024;
      Runtime runtime = Runtime.getRuntime();
      System.out.print("Memory used by JVM at Runtime: " + (runtime.totalMemory() - runtime.freeMemory()) / megabyte);
      System.out.println("MB");
      System.out.println("Java uses a whole LOT of memory...");
    }

//    function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
    public static String breadthFirstSearch(String problem) {
//        node <- a node with STATE=problem.INITIAL-STATE, PATH-COST=0
          Node node = new Node(null, 0);
//        if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
//        frontier <- a FIFO queue with node as the only element
          Queue<Node> frontier = new ArrayDeque<>();

//        explored <- an empty set
          Set<Node> explored = new HashSet<>();

//        loop do
//            if EMPTY?(frontier) then return failure
//            node <- POP(frontier) /* chooses the shallowest node in frontier */
//            add node.STATE to explored
//            for each action in problem.ACTIONS(node.STATE) do
//                child <- CHILD-NODE(problem, node, action)
//                if child.STATE is not in explored or frontier then
//                    if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
//                    frontier <- INSERT(child, frontier)
      return null;
    }
}

class Node {
    String state;
    int pathCost;
    String parent;
    String action;

    Node(String state, int pathCost) {
      this.state = state;
      this.pathCost = pathCost;
    }

    Node(String state, String parent, String action, int pathCost) {
      this.state = state;
      this.parent = parent;
      this.action = action;
      this.pathCost = pathCost;
    }

    String expand() { return null; }
    String childNode() { return null; }
    String solution() { return null; }
    String path() { return null; }

  @Override
    public boolean equals(Object obj) {
      if (((Node) obj).state == this.state) { return true; }
      return false;
    }
}

enum Actions {
   UP, DOWN, LEFT, RIGHT
}

class Problem {
  String initialState;
  String goal;
  Problem(String initialState, String goal) {
    this.initialState = initialState;
    this.goal = goal;
  }

  boolean goalTest() { return false; }
  int  pathCost() { return -1; }
  // returns state
  String result(String state, String action) { return null; }
  // returns available actions given state
  String actions(String state) { return null; }
}