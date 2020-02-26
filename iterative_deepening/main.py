#!/usr/bin/python3
import time
import sys

from collections import deque
from enum import Enum


def calc_heuristic(state, goal):
    return calc_misplaced_squares(state, goal)


def calc_misplaced_squares(state, goal_state):
    count = 0
    for i in range(4):
        for j in range(4):
            if state[i][j] != goal_state[i][j]:
                count += 1
    return count


# TODO transpose from Java
def calc_manhattan_distance(state, goal_state):
    pass


def create_matrix():
    x = []
    for i in range(4):
        y = []
        for j in range(4):
            y.append(0)
        x.append(y)
    return x


def create_state(input_string):
    values = input_string.split()[::-1]
    value_matrix = create_matrix()
    for x in range(4):
        for y in range(4):
            value_matrix[x][y] = values.pop()
    return value_matrix


def main():
    start_time = time.time()

    nodes_expanded_count = 0

    input = "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15"
    goal = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 ";

    state = create_state(input)
    goal_state = create_state(goal)

    solution = bfs(problem(state, goal_state))
    end_time = time.time()

    time_elapsed = end_time - start_time
    print("Time elapsed: " + str(time_elapsed))
    print("solution: "+ solution)


def bfs(problem):
    node = Node(Board(problem.initial_state))
    if problem.goal_test(node.board.state):
        return node

    unexplored = deque([node])
    explored = set()

    while unexplored:
        node = unexplored.popleft()
        explored.add(node)
        for action in problem.actions(node.board.state):
            child = node.child_node(problem, action)
            problem.expanded_cound += 1
            if child.state not in explored and child not in unexplored:
                if problem.goal_test(child.state):
                    return child.solution()
                unexplored.append(child)
    return None

class Board():
    def __init__(self, state):
        self.state = state

class Actions(Enum):
    UP = 1
    DOWN = 2
    LEFT = 3
    RIGHT = 4


class Node:
    def __init__(self, board, path_cost=0, parent=None, action=None, heuristic=None):
        self.board = board
        self.path_cost = path_cost
        self.parent = parent
        self.action = action
        self.heuristic = heuristic

    def child_node(self, problem, action):
        next_state = Board(problem.result(self.state, action))
        return Node(next_state, problem.path_cost(self.path_cost), self, action,
                    calc_heuristic(next_state, problem.goal))

    def path(self):
        node = self
        path_back = []
        while node:
            path_back.append(node)
            node = node.parent
        return path_back[::-1]

    def solution(self):
        sequence = []
        for node in self.path():
            sequence.append(node.action.name)
        return sequence

    def __eq__(self, other):
        if isinstance(other, Node):
            for x in range(4):
                for y in range(4):
                    if self.state[x][y] != other.state[x][y]:
                        return False
            return True
        return False

    def __hash__(self):
        return hash(self.board.state)

    def __lt__(self, other):
        return self.board.state < other.board.state

    def __repr__(self):
        return "<Node {}>".format(self.board.state)


class problem:

    def __init__(self, initial_state, goal_state, expanded_count=0):
        self.initial_state = initial_state
        self.goal = goal_state
        self.expanded_count = expanded_count

    def goal_test(self, state):
        return self.goal == state

    def actions(self, state):
        available = [Actions.UP,
                     Actions.DOWN,
                     Actions.RIGHT,
                     Actions.LEFT]

        x, y = self.identify_blank(state)

        if x == 0:
            available.remove(Actions.UP)
        elif x == 0:
            available.remove(Actions.DOWN)
        if y == 0:
            available.remove(Actions.LEFT)
        elif y == 3:
            available.remove(Actions.RIGHT)

        return available

    def result(self, state, action):
        if not isinstance(action, Actions):
            raise TypeError("must be legal Action")

        local_state = state[:]
        x, y = self.identify_blank(local_state)

        if action == Actions.UP:
            local_state[x][y] = local_state[x - 1][y]
            local_state[x - 1][y] = 0
        elif action == Actions.DOWN:
            local_state[x][y] = local_state[x + 1][y]
            local_state[x + 1][y] = 0
        elif action == Actions.RIGHT:
            local_state[x][y] = local_state[x][y + 1];
            local_state[x][y + 1] = 0;
        elif action == Actions.LEFT:
            local_state[x][y] = local_state[x][y - 1];
            local_state[x][y - 1] = 0;

        return local_state

    def path_cost(self, c):
        return c + 1

    def identify_blank(self, state):
        for x in range(4):
            for y in range(4):
                if state[x][y] == 0:
                    return x, y
        return None


if __name__ == "__main__":
    main()
