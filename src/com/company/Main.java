package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        String input = null;
        if(args.length > 0) {
            input = args[0];
        } else {
            input = "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15";
        }

        String goalString = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ";
        int[][] initialState = getStateFromSting(input);
        int[][] goalState = getStateFromSting(goalString);

        // This calculation works, but I couldn't get the heuristic to work in the search before I ran out of time
//        int man = calculateManhattanDistance(initialState);

        // time
        long startTime = System.nanoTime();

        // execute BFS
        Problem problem = new Problem(initialState, goalState);
        List<Actions> solutionSequence = breadthFirstSearch(problem);

        if (solutionSequence == null) { System.out.println("Failure in search detected"); System.exit(1); }

        // convert to human-readable
        StringBuilder solutionBuilder = new StringBuilder();
        solutionSequence.forEach(actions -> solutionBuilder.append(actions));
        final String solution = solutionBuilder.toString().substring(4);

        // time
        long endTime = System.nanoTime();
        long timeDelta = endTime - startTime;

        // Print result
        System.out.println("Moves: " + solution);
        System.out.println("Number of Nodes expanded: " + problem.expandedCount);
        System.out.println(String.format("Time elapsed: %d ns", timeDelta));
        System.out.println("Memory used by the entire JVM at Runtime: " + getMemoryUsed() + " MB");
        System.out.println("\nJava uses a whole LOT of memory...");
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

    static List<Actions> breadthFirstSearch(Problem problem) {
        Node node = new Node(problem.initialState);
        if (problem.goalTest(node.state)) {
            return node.solution();
        }

        Queue<Node> frontier = new PriorityQueue<>(nodeComparator);

        frontier.add(node);
        Set<int[][]> explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            node = frontier.poll();
            explored.add(node.state);
            for (Actions action : problem.actions(node.state)) {
                Node child = node.childNode(problem, action);
                ++problem.expandedCount;
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

    // Comparator anonymous class implementation
    static Comparator<Node> nodeComparator = (n1, n2) -> (int) (n1.heuristic - n2.heuristic);

    static Map<Integer, Map.Entry<Integer, Integer>> getGoalCoordinateMap(){
        Map<Integer, Map.Entry<Integer, Integer>> coordinatesOfGoals = new HashMap<>();
        Map<Integer, Integer> currentCoordinate;
        int k = 1;
        for (int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                currentCoordinate = new HashMap<>();
                currentCoordinate.put(i, j);
                if(k == 16) {
                    coordinatesOfGoals.put(0, currentCoordinate.entrySet().iterator().next());
                } else {
                    coordinatesOfGoals.put(k++, currentCoordinate.entrySet().iterator().next());
                }
            }
        }
        return coordinatesOfGoals;
    }

    // TODO replace simple misplacedSquares heuristic w/ Manhattan distance heuristic
    static int calculateManhattanDistance(int[][] state) {
        // For speed of calculation, since we know the goal state, we will use a lookup table
        // TODO put this in the Problem object so it's only calc'd once
        Map<Integer, Map.Entry<Integer, Integer>> goalXYs = getGoalCoordinateMap();

        int distance = 0;
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j) {
                int value = state[i][j];
                Map.Entry<Integer, Integer> goalXYTuple = goalXYs.get(value);
                //        a, b = current location of [i][j]
                //        c, d = location of where [i][j] SHOULD be
                distance += Math.abs(i - goalXYTuple.getKey()) + Math.abs(j - goalXYTuple.getValue());
            }
        }
        return distance;
    }

    static int calculateMisplacedSquares(int[][] state, int[][] goalState) {
        int count = 0;
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j) {
                if(state[i][j] != goalState[i][j]) {
                    ++count;
                }
            }
        }
        return count;
    }
}

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
        return new Node(nextState, problem.pathCost(this.pathCost), this, action, Main.calculateMisplacedSquares(nextState, problem.goal));
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

    @Override
    public boolean equals(Object obj) {
        return Arrays.deepEquals(((Node) obj).state, this.state);
    }
}

enum Actions { U, D, L, R }

class Problem {
    int[][] initialState;
    int[][] goal;
    int expandedCount;

    Problem(int[][] initialState, int[][] goal) {
        this.initialState = initialState;
        this.goal = goal;
    }

    boolean goalTest(int[][] state) {
        return Arrays.deepEquals(this.goal, state);
    }

    int pathCost(int cost) {
        return ++cost;
    }

    int[][] result(int[][] state, Actions action) {

        // TODO convert State to an object that internally tracks where the blank is
        Map.Entry<Integer, Integer> xy_tuple = identifyBlank(state);

        int[][] localState = new int[4][4];

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                localState[i][j] = state[i][j];
            }
        }

        int x = xy_tuple.getKey();
        int y = xy_tuple.getValue();

        switch (action) {
            case U:
                localState[x][y] = localState[x - 1][y];
                localState[x - 1][y] = 0;
                break;
            case D:
                localState[x][y] = localState[x + 1][y];
                localState[x + 1][y] = 0;
                break;
            case R:
                localState[x][y] = localState[x][y + 1];
                localState[x][y + 1] = 0;
                break;
            case L:
                localState[x][y] = localState[x][y - 1];
                localState[x][y - 1] = 0;
                break;
            default:
        }
        return localState;
    }

    // returns available actions given state
    List<Actions> actions(int[][] state) {
        Map.Entry<Integer, Integer> blankTuple = identifyBlank(state);

        List<Actions> actions = new ArrayList<>(Arrays.asList(Actions.U, Actions.D, Actions.L, Actions.R));

        int x = blankTuple.getKey();
        int y = blankTuple.getValue();

        switch (x) {
            case 0:
                actions.remove(Actions.U);
                break;
            case 3:
                actions.remove(Actions.D);
                break;
            default:
        }
        switch (y) {
            case 0:
                actions.remove(Actions.L);
                break;
            case 3:
                actions.remove(Actions.R);
                break;
            default:
        }

        return actions;
    }

    private Map.Entry<Integer, Integer> identifyBlank(int[][] state) {
        // I would use a Pair<Integer, Integer> to represent a tuple
        // but I can't guarantee if the grader is running OpenJDK or OracleJDK
        // Map.Entry provides much the same interface
        Map<Integer, Integer> blank = new HashMap<>();

        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                if (state[x][y] == 0) {
                    blank.put(x, y);
                    return blank.entrySet().iterator().next();
                }
            }
        }
        return null;
    }
}
