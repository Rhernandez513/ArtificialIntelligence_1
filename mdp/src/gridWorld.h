//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_GRIDWORLD_H
#define MDP_GRIDWORLD_H

#include "state.h"

#include <tuple>
#include <vector>

class gridWorld {
    int length;
    int width;
    std::vector<std::tuple<int, int>> pillars;
    std::vector<state> terminalCoordinates;
public:
    bool isTerminalState(int x, int y);
    int getTerminalStateReward(int x, int y);

    gridWorld(int length, int width, const std::vector<std::tuple<int, int>> &pillars,
              const std::vector<state> &terminalCoordinates);

    virtual ~gridWorld();

    int getLength() const;

    void setLength(int length);

    int getWidth() const;

    void setWidth(int width);

    const std::vector<std::tuple<int, int>> &getPillars() const;

    void setPillars(const std::vector<std::tuple<int, int>> &pillars);

    const std::vector<state> &getTerminalCoordinates() const;

    void setTerminalCoordinates(const std::vector<state> &terminalCoordinates);
};


#endif //MDP_GRIDWORLD_H
