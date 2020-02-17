#!/usr/bin/python3
# some code taken from https://github.com/aimacode/aima-python

import numpy


class FifteenPuzzle:

    def __init__(self, initial_state):
        self.initial_state = initial_state
        self.goal = self.create_goal_state()

# BFS CALLS THIS
    def is_goal_state(self, state):
        # return state == self.goal
        return numpy.array_equal(state, self.goal)

    # class actions(Enum):
    #     UP = 1
    #     DOWN = 2
    #     LEFT = 3
    #     RIGHT = 4

# private called
    def actions(self, state):
        result = list()
        xy_pair = numpy.argwhere(state == 0)
        x = xy_pair[0, 0]
        y = xy_pair[0, 0]

        if x == 3:
            # Disallow right movement
            None
        elif y == 3:
            # Disallow down movement
            None
        elif x == 0:
            # Disallow left movement
            None
        elif y == 0:
            # Disallow up movement
            None

        return result

# private called
    def result(self, state, action):
        """Return the state that results from executing the given
        action in the given state. The action must be one of
        self.actions(state)."""
        None

# private called
    def path_cost(self, c, state1, action, state2):
        """Return the cost of a solution path that arrives at state2 from
        state1 via action, assuming cost c to get up to state1. If the problem
        is such that the path doesn't matter, this function will only look at
        state2. If the path does matter, it will consider c and maybe state1
        and action. The default method costs 1 for every step in the path."""
        return c + 1


    def create_goal_state(self):
        init_state = numpy.zeros((4, 4), int)
        init_state[0, 0] = 1
        init_state[0, 1] = 2
        init_state[0, 2] = 3
        init_state[0, 3] = 4
        init_state[1, 0] = 5
        init_state[1, 1] = 6
        init_state[1, 2] = 7
        init_state[1, 3] = 8
        init_state[2, 0] = 9
        init_state[2, 1] = 10
        init_state[2, 2] = 11
        init_state[2, 3] = 12
        init_state[3, 0] = 13
        init_state[3, 1] = 14
        init_state[3, 2] = 15
        init_state[3, 3] = 0

        return init_state
