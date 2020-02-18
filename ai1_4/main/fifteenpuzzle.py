#!/usr/bin/python3
# some code taken from https://github.com/aimacode/aima-python
import numpy
from enum import Enum


class FifteenPuzzle:

    def __init__(self, initial_state):
        self.initial_state = initial_state
        self.goal = self.create_goal_state()

# BFS CALLS THIS
    def is_goal_state(self, state):
        # return state == self.goal
        return numpy.array_equal(state, self.goal)

    class possible_actions(Enum):
        UP = 1
        DOWN = 2
        LEFT = 3
        RIGHT = 4

# private called
    def actions(self, state):
        available_actions = list()
        available_actions.append(self.possible_actions.UP)
        available_actions.append(self.possible_actions.DOWN)
        available_actions.append(self.possible_actions.LEFT)
        available_actions.append(self.possible_actions.RIGHT)

        xy_pair = numpy.argwhere(state == 0)
        x = xy_pair[0, 0]
        y = xy_pair[0, 1]

        if x == 3:
            # Disallow down movement
            available_actions.remove(self.possible_actions.DOWN)
        elif x == 0:
            # Disallow up movement
            available_actions.remove(self.possible_actions.UP)
        if y == 3:
            # Disallow right movement
            available_actions.remove(self.possible_actions.RIGHT)
        elif y == 0:
            # Disallow left movement
            available_actions.remove(self.possible_actions.LEFT)

        return available_actions

# private called
    def result(self, state, action):
        """Return the state that results from executing the given
        action in the given state. The action must be one of
        self.possible_actions(state)."""
        if not isinstance(action, self.possible_actions):
            raise TypeError("must be possible_action Enum")

        blank = numpy.argwhere(state == 0)

        if action == self.possible_actions.UP:
            # swap zero with one above it
            x = blank[0, 0]
            y = blank[0, 1]
            state[x, y] = state[x - 1, y]
            state[x - 1, y] = 0
        elif action == self.possible_actions.DOWN:
            # swap zero with one below it
            x = blank[0, 0]
            y = blank[0, 1]
            state[x, y] = state[x + 1, y]
            state[x + 1, y] = 0
        elif action == self.possible_actions.RIGHT:
            # swap zero with one to the right
            x = blank[0, 0]
            y = blank[0, 1]
            state[x, y] = state[x, y + 1]
            state[x, y + 1] = 0
        elif action == self.possible_actions.LEFT:
            # swap zero with one to the left
            x = blank[0, 0]
            y = blank[0, 1]
            state[x, y] = state[x, y - 1]
            state[x, y - 1] = 0

        return state

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
