package com.company;

import java.util.*;

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
