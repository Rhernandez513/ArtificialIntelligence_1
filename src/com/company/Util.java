package com.company;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Map;

public class Util {

  // For runtime speed, since we know the goal state, we will use a lookup table
  private static Map<Integer, Map.Entry<Integer, Integer>> goalXYs = getGoalCoordinateMap();

  static void printResult(long startTime, Problem problem, List<Actions> solutionSequence) {
    printResult(startTime, problem, solutionSequence, false);
  }
  static void printResult(long startTime, Problem problem, List<Actions> solutionSequence, boolean isManhattanEnabled) {
    // convert to human-readable
    StringBuilder solutionBuilder = new StringBuilder();
    solutionSequence.forEach(actions -> solutionBuilder.append(actions));
    final String solution = solutionBuilder.toString().substring(4);

    System.out.println((isManhattanEnabled) ? "Manhattan Distance" : "Count of Misplaced Squares");
    System.out.println("Moves: " + solution);
    System.out.println("Number of Nodes expanded: " + problem.expandedCount);
    System.out.println(String.format("Time elapsed: %d ns", System.nanoTime() - startTime));
    System.out.println("Memory used by the entire JVM at Runtime: " + Util.getMemoryUsed() + " MB\n");
  }

  static int[][] getStateFromSting(String input) {
    int[][] state = new int[4][4];
    Queue<String> vals = new ArrayDeque<>(Arrays.asList(input.split(" ")));
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        state[i][j] = Integer.parseInt(vals.poll());
      }
    }
    return state;
  }

  static long getMemoryUsed() {
    int megabyte = 1024 * 1024;
    Runtime runtime = Runtime.getRuntime();
    return ((runtime.totalMemory() - runtime.freeMemory()) / megabyte);
  }

  static int calculateMisplacedSquares(int[][] state, int[][] goalState) {
    int count = 0;
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        if (state[i][j] != goalState[i][j]) {
          ++count;
        }
      }
    }
    return count;
  }

  static int calculateManhattanDistance(int[][] state) {
    int distance = 0;
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        int value = state[i][j];
        Map.Entry<Integer, Integer> goalXYTuple = goalXYs.get(value);
        //        a, b = current location of [i][j]
        //        c, d = location of where [i][j] SHOULD be
        distance += Math.abs(i - goalXYTuple.getKey()) + Math.abs(j - goalXYTuple.getValue());
      }
    }
    return distance;
  }

  static Map<Integer, Map.Entry<Integer, Integer>> getGoalCoordinateMap() {
    Map<Integer, Map.Entry<Integer, Integer>> coordinatesOfGoals = new HashMap<>();
    Map<Integer, Integer> currentCoordinate;
    int k = 1;
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        currentCoordinate = new HashMap<>();
        currentCoordinate.put(i, j);
        if (k == 16) {
          coordinatesOfGoals.put(0, currentCoordinate.entrySet().iterator().next());
        } else {
          coordinatesOfGoals.put(k++, currentCoordinate.entrySet().iterator().next());
        }
      }
    }
    return coordinatesOfGoals;
  }
}
