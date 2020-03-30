//
// Created by Robert David Hernandez on 3/29/20.
//

#include "gridWorld.h"

gridWorld::gridWorld(int length, int width, const std::vector<std::tuple<int, int>> &pillars,
                     const std::vector<state> &terminalCoordinates) : length(length), width(width), pillars(pillars),
                                                                      terminalCoordinates(terminalCoordinates) {}

gridWorld::~gridWorld() {

}

int gridWorld::getLength() const {
    return length;
}

void gridWorld::setLength(int length) {
    gridWorld::length = length;
}

int gridWorld::getWidth() const {
    return width;
}

void gridWorld::setWidth(int width) {
    gridWorld::width = width;
}

const std::vector<std::tuple<int, int>> &gridWorld::getPillars() const {
    return pillars;
}

void gridWorld::setPillars(const std::vector<std::tuple<int, int>> &pillars) {
    gridWorld::pillars = pillars;
}

const std::vector<state> &gridWorld::getTerminalCoordinates() const {
    return terminalCoordinates;
}

void gridWorld::setTerminalCoordinates(const std::vector<state> &terminalCoordinates) {
    gridWorld::terminalCoordinates = terminalCoordinates;
}
