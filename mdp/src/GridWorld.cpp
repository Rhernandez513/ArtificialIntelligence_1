//
// Created by Robert David Hernandez on 3/29/20.
//

#include "GridWorld.h"
#include <iostream>
#include <stdexcept>

GridWorld::GridWorld(int width, int height, const std::vector<std::pair<int, int>> &pillars,
                     const std::vector<State> &terminalCoordinates) : width(width), height(height), pillars(pillars),
                                                                      terminalCoordinates(terminalCoordinates) {}

GridWorld::~GridWorld() {

}

State *GridWorld::isTerminalState(int x, int y) {
    std::vector<State> terminalStates = this->getTerminalCoordinates();
    for (auto &i : terminalStates) {
        if (i.getX() == x && i.getY() == y) {
            return &i;
        }
    }
    return nullptr;
}

int GridWorld::getTerminalStateReward(int x, int y) {
    State * state = isTerminalState(x, y);
    if (state) {
        return state->getReward();
    }
    std::cerr << "ERROR: Did not find x(" << x << ") and y(" << y << ") in terminal state collection" << std::endl;
    throw std::invalid_argument("ERROR: Did not find x and y in terminal state collection");
}

int GridWorld::getHeight() const {
    return height;
}

void GridWorld::setHeight(int height) {
    GridWorld::height = height;
}

int GridWorld::getWidth() const {
    return width;
}

void GridWorld::setWidth(int width) {
    GridWorld::width = width;
}

const std::vector<std::pair<int, int>> &GridWorld::getPillars() const {
    return pillars;
}

void GridWorld::setPillars(const std::vector<std::pair<int, int>> &pillars) {
    GridWorld::pillars = pillars;
}

const std::vector<State> &GridWorld::getTerminalCoordinates() const {
    return terminalCoordinates;
}

void GridWorld::setTerminalCoordinates(const std::vector<State> &terminalCoordinates) {
    GridWorld::terminalCoordinates = terminalCoordinates;
}
