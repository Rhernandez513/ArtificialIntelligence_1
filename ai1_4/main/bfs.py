#!/usr/bin/python3

import numpy
import time
import pympler
from collections import deque

from main.fifteenpuzzle import FifteenPuzzle
from main.node import Node


def create_state(input_string):
    values = input_string.split()
    # reverse the list
    values = values[::-1]

    value_matrix = numpy.zeros((4, 4), int)

    for x in range(3):
        for y in range(3):
            value_matrix[x, y] = values.pop()

    return value_matrix


def main():

    start_time = time.time()

    nodes_expanded_count = 0

    input = "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15"
    state = create_state(input)
    fifteen_puzzle_instance = FifteenPuzzle(state)
    final_node = bfs(fifteen_puzzle_instance, nodes_expanded_count)
    if final_node is None:
        print("Something went wrong!")
    end_time = time.time()

    time_elapsed = end_time - start_time
    print("Time elapsed: " + time_elapsed)
    print("final state path_cost: " + final_node.path_cost)


def bfs(fifteen_puzzle, expanded_count):
    node = Node(fifteen_puzzle.initial_state)
    if fifteen_puzzle.is_goal_state(node.state):
        return node
    unexplored_nodes = deque([node])
    explored_states = set()
    while unexplored_nodes:
        node = unexplored_nodes.popleft()
        explored_states.add(node.state)
        for idx, element in enumerate(node.expand(fifteen_puzzle)):
            expanded_count = expanded_count + 1
            if element.state not in explored_states and element not in unexplored_nodes:
                if fifteen_puzzle.is_goal_state(element.state):
                    print("Nodes expanded: " + expanded_count)
                    print("Memory used: " + pympler.asizeof.asizeof(explored_states))
                    return element
                unexplored_nodes.append(element)
    return None


if __name__ == "__main__":
    main()
