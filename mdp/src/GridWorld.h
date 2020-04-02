//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_GRIDWORLD_H
#define MDP_GRIDWORLD_H

#include "State.h"

#include <tuple>
#include <vector>

class GridWorld {
    int width;
    int height;
    std::vector<std::pair<int, int>> pillars;
    std::vector<State> terminalCoordinates;
public:
    State* isTerminalState(int x, int y);
    int getTerminalStateReward(int x, int y);

    GridWorld(int width, int height, const std::vector<std::pair<int, int>> &pillars,
              const std::vector<State> &terminalCoordinates);

    virtual ~GridWorld();

    int getHeight() const;

    void setHeight(int height);

    int getWidth() const;

    void setWidth(int width);

    const std::vector<std::pair<int, int>> &getPillars() const;

    void setPillars(const std::vector<std::pair<int, int>> &pillars);

    const std::vector<State> &getTerminalCoordinates() const;

    void setTerminalCoordinates(const std::vector<State> &terminalCoordinates);
};


#endif //MDP_GRIDWORLD_H
