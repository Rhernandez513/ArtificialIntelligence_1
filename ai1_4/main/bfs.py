#!/usr/bin/python3

import numpy
import time
import sys
from collections import deque

from main.fifteenpuzzle import FifteenPuzzle
from main.node import Node


def create_state(input_string):
    values = input_string.split()
    # reverse the list
    values = values[::-1]

    value_matrix = numpy.zeros((4, 4), int)

    for x in range(4):
        for y in range(4):
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
    print("Time elapsed: " + str(time_elapsed))
    print("final state path_cost: " + final_node.path_cost)


def bfs(fifteen_puzzle, expanded_count):
    node = Node(fifteen_puzzle.initial_state)
    if fifteen_puzzle.is_goal_state(node.state):
        return node

    unexplored_nodes = deque([node])
    unexplored_node_hashes = [node.state.flatten().tostring()]

    explored_states = set()

    while unexplored_nodes:

        node = unexplored_nodes.popleft()
        state_hash = node.state.flatten().tostring()

        if node.depth > 0:
            state_hash = hash(state_hash.decode('utf-8') + node.action.name)

        unexplored_node_hashes.remove(state_hash)
        explored_states.add(state_hash)

        for element in node.expand(fifteen_puzzle):
            expanded_count = expanded_count + 1
            current_hash = element.state.flatten().tostring()
            # current_hash = element.state.flatten().tostring() + element.action.name
            if current_hash not in explored_states:
                if current_hash not in unexplored_node_hashes:
                    # if element not in unexplored_nodes:
                    if fifteen_puzzle.is_goal_state(element.state):
                        print("Nodes expanded: " + expanded_count)
                        print("Memory used (in bytes): " + sys.getsizeof(explored_states))
                        return element
                unexplored_nodes.append(element)
                if element.depth > 0:
                    unexplored_node_hashes.append(hash(current_hash.decode('utf-8') + element.action.name))
                else:
                    unexplored_node_hashes.append(current_hash)
    return None


if __name__ == "__main__":
    main()
