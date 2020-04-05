//
// Created by Robert David Hernandez on 3/29/20.
//

#include "MDP.h"


MDP::MDP(const std::vector<double> &transitionProbabilities, double epsilon, double gamma, double R,
         const std::vector<State> &states) : transitionProbabilities(transitionProbabilities), epsilon(epsilon),
                                             gamma(gamma), R(R), states(states) {}

MDP::~MDP() {}

// this needs to return the weighted reward state to statePrime
double MDP::transitionModel(GridWorld &grid, State statePrime, State state, Actions action) {

    // illegal to move into our out of a pillar
    std::vector<std::pair<int, int>> pillars = grid.getPillars();
    for(auto &pillar : pillars) {
        if (state.getX() == pillar.first && state.getY() == pillar.second) {
            return 0.0;
        } else if(statePrime.getX() == pillar.first && statePrime.getY() == pillar.second) {
            return 0.0;
        }
    }
    // Terminal States have utility values provided by input
    for(auto &s : grid.getTerminalCoordinates()) {
        if(statePrime.getX()  == s.getX() && statePrime.getY() == s.getY()) {
            return s.getReward();
        }
    }

    int width = grid.getWidth();
    int height = grid.getHeight();

    double probability = 0.0;
    switch(action) {
        case Actions::Up:
            // determine is there is a wall in front of us
            if(statePrime.getY() > state.getY()) {
               // attempting to move NORTH
                if(statePrime.getY() - 1 == state.getY()) {
                    // we are attempting to move forward to an unobstructed adjacent state
                    probability += transitionProbabilities[0];
                    break;
                } else if (statePrime.getY() - 1  != state.getY()) { /* we disallow jumping spaces */ break; }
            } else if (statePrime.getY() < state.getY()) {
                /* illegal move */
                break;
            } else { /* Y values are equal */
                // if attempted action is up, but statePrime is the same state
                // then only if there is a wall in front of us
                // shall we have a chance to bounce and remain in the same spot
                if(state.getY() == height) {
                    /* yes wall NORTH */
                    probability += transitionProbabilities[0];
                } else if (state.getY() == 1) {
                    // wall SOUTH
                    probability += transitionProbabilities[3];
                }
                if(state.getX() == width) {
                    // wall to EAST
                    probability += transitionProbabilities[2];
                } else if (state.getX() == 1) {
                    // wall to WEST
                    probability += transitionProbabilities[1];
                }
                // determine if there is a pillar blocking movement
                for(auto &a : pillars) {
                    // first = x, second = y
                    // pillar NORTH
                    if(a.second - 1 == state.getY()) {
                        probability += transitionProbabilities[0];
                    }
                    // pillar to WEST
                    if(a.first + 1 == state.getX()) {
                        probability += transitionProbabilities[1];
                    }
                    // pillar to EAST
                    if(a.first - 1 == state.getX()) {
                        probability += transitionProbabilities[2];
                    }
                    // pillar SOUTH
                    if(a.first + 1 == state.getY()) {
                        probability += transitionProbabilities[3];
                    }
                }
            }
            break;
        case Actions::Left:
            // determine is there is a wall to the left of us
            if(statePrime.getX() < state.getX()) {
                // attempting to move WEST
                if(statePrime.getX() + 1 == state.getX()) {
                    // we are attempting to move left to an unobstructed adjacent state
                    probability += transitionProbabilities[0];
                    break;
                } else if (statePrime.getX() + 1  != state.getX()) { /* we disallow jumping spaces */ break; }
            } else if (statePrime.getX() > state.getX()) {
                /* illegal move */
                break;
            } else { /* X values are equal */
                // if attempted action is left, but statePrime is the same state
                // then only if there is a wall to the left of us
                // shall we have a chance to bounce and remain in the same spot
                if(state.getY() == height) {
                    /* yes wall NORTH */
                    probability += transitionProbabilities[2];
                } else if (state.getY() == 1) {
                    // wall  to SOUTH
                    probability += transitionProbabilities[1];
                }
                if(state.getX() == width) {
                    // wall to EAST
                    probability += transitionProbabilities[3];
                } else if (state.getX() == 1) {
                    // wall to WEST
                    probability += transitionProbabilities[0];
                }
                // determine if there is a pillar blocking movement
                for(auto &a : pillars) {
                    // first = x, second = y
                    // pillar NORTH
                    if(a.second - 1 == state.getY()) {
                        probability += transitionProbabilities[2];
                    }
                    // pillar WEST
                    if(a.first + 1 == state.getX()) {
                        probability += transitionProbabilities[0];
                    }
                    // pillar EAST
                    if(a.first - 1 == state.getX()) {
                        probability += transitionProbabilities[3];
                    }
                    // pillar SOUTH
                    if(a.first + 1 == state.getY()) {
                        probability += transitionProbabilities[1];
                    }
                }
            }
            break;
        case Actions::Right:
            // determine is there is a wall to the right of us
            if(statePrime.getX() > state.getX()) {
                // attempting to move WEST
                if(statePrime.getX() - 1 == state.getX()) {
                    // we are attempting to move left to an unobstructed adjacent state
                    probability += transitionProbabilities[0];
                    break;
                } else if (statePrime.getX() - 1  != state.getX()) { /* we disallow jumping spaces */ break; }
            } else if (statePrime.getX() < state.getX()) {
                /* illegal move */
                break;
            } else { /* X values are equal */
                // if attempted action is right, but statePrime is the same state
                // then only if there is a wall to the right of us
                // shall we have a chance to bounce and remain in the same spot
                if(state.getY() == height) {
                    /* yes wall NORTH */
                    probability += transitionProbabilities[1];
                } else if (state.getY() == 1) {
                    // wall  to SOUTH
                    probability += transitionProbabilities[2];
                }
                if(state.getX() == width) {
                    // wall to EAST
                    probability += transitionProbabilities[0];
                } else if (state.getX() == 1) {
                    // wall to WEST
                    probability += transitionProbabilities[3];
                }
                // determine if there is a pillar blocking movement
                for(auto &a : pillars) {
                    // first = x, second = y
                    // pillar NORTH
                    if(a.second - 1 == state.getY()) {
                        probability += transitionProbabilities[1];
                    }
                    // pillar WEST
                    if(a.first + 1 == state.getX()) {
                        probability += transitionProbabilities[3];
                    }
                    // pillar EAST
                    if(a.first - 1 == state.getX()) {
                        probability += transitionProbabilities[0];
                    }
                    // pillar SOUTH
                    if(a.first + 1 == state.getY()) {
                        probability += transitionProbabilities[2];
                    }
                }
            }
            break;
        case Actions::Down:
            // determine is there is a wall below us
            if(statePrime.getY() < state.getY()) {
                // attempting to move SOUTH
                if(statePrime.getY() + 1 == state.getY()) {
                    // we are attempting to move left to an unobstructed adjacent state
                    probability += transitionProbabilities[0];
                    break;
                } else if (statePrime.getX() + 1  != state.getX()) { /* we disallow jumping spaces */ break; }
            } else if (statePrime.getY() > state.getY()) {
                /* illegal move */
                break;
            } else { /* X values are equal */
                // if attempted action is right, but statePrime is the same state
                // then only if there is a wall to the right of us
                // shall we have a chance to bounce and remain in the same spot
                if(state.getY() == height) {
                    /* yes wall NORTH */
                    probability += transitionProbabilities[3];
                } else if (state.getY() == 1) {
                    // wall  to SOUTH
                    probability += transitionProbabilities[0];
                }
                if(state.getX() == width) {
                    // wall to EAST
                    probability += transitionProbabilities[2];
                } else if (state.getX() == 1) {
                    // wall to WEST
                    probability += transitionProbabilities[1];
                }
                // determine if there is a pillar blocking movement
                for(auto &a : pillars) {
                    // first = x, second = y
                    // pillar NORTH
                    if(a.second - 1 == state.getY()) {
                        probability += transitionProbabilities[3];
                    }
                    // pillar WEST
                    if(a.first + 1 == state.getX()) {
                        probability += transitionProbabilities[2];
                    }
                    // pillar EAST
                    if(a.first - 1 == state.getX()) {
                        probability += transitionProbabilities[1];
                    }
                    // pillar SOUTH
                    if(a.first + 1 == state.getY()) {
                        probability += transitionProbabilities[0];
                    }
                }
            }
            break;
        default:
            break;
    }

    probability = (probability > 1.0) ? 1.0 : probability;
    return statePrime.getReward() * probability;
}


std::set<Actions> MDP::A(State &s, GridWorld &grid) {
    std::set<Actions> actions;
    int width = grid.getWidth();
    int height = grid.getHeight();
    int x = s.getX();
    int y = s.getY();
    if(y < height) {
        actions.insert(Actions::Up);
    }
    if(x < width) {
        actions.insert(Actions::Right);
    }
    if(x > 1) {
        actions.insert(Actions::Left);
    }
    if(y > 1) {
        actions.insert(Actions::Down);
    }
    return actions;
}

double MDP::getEpsilon() const {
    return epsilon;
}

void MDP::setEpsilon(double epsilon) {
    MDP::epsilon = epsilon;
}

double MDP::getGamma() const {
    return gamma;
}

void MDP::setGamma(double gamma) {
    MDP::gamma = gamma;
}

double MDP::getR() const {
    return R;
}

void MDP::setR(double R) {
    MDP::R = R;
}

std::vector<State> MDP::getStates() {
    return states;
}

void MDP::setStates(std::vector<State> states) {
    MDP::states = states;
}

const std::vector<double> &MDP::getTransitionProbabilities() const {
    return transitionProbabilities;
}

void MDP::setTransitionProbabilities(const std::vector<double> &transitionProbabilities) {
    MDP::transitionProbabilities = transitionProbabilities;
}
