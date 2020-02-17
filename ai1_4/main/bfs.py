#!/usr/bin/python3

import numpy
from collections import deque
from main.node import Node


nodes_expanded_count = 0
memory_used = 0
time_taken = 0


def main():
    None


def bfs(fifteen_puzzle):
    node = Node(fifteen_puzzle.initial_state)
    if fifteen_puzzle.is_goal_state(node.state):
        return node
    unexplored = deque([node])
    explored = set()
    while unexplored:
        node = unexplored.popleft()
        explored.add(node.state)
        for child in node.expand(fifteen_puzzle):
            if child.state not in explored and child not in unexplored:
                if fifteen_puzzle.is_goal_state(child.state):
                    return child
                unexplored.append(child)
    return None


if __name__ == "__main__":
    main()
