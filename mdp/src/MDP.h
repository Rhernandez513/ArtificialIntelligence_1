//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_MDP_H
#define MDP_MDP_H


#include <vector>
#include "State.h"
#include "Actions.h"
#include "GridWorld.h"

class MDP {
    std::vector<double> transitionProbabilities;
    double epsilon; // the maximum error allowed in any State
    double gamma;   // discount factor
    double R;       // immediate reward in non-terminal states
    std::vector<State> states;
public:
    MDP(const std::vector<double> &transitionProbabilities, double epsilon, double gamma, double R,
        const std::vector<State> &states);

    virtual ~MDP();

    const std::vector<double> &getTransitionProbabilities() const;

    void setTransitionProbabilities(const std::vector<double> &transitionProbabilities);

    double getEpsilon() const;

    void setEpsilon(double epsilon);

    double getGamma() const;

    void setGamma(double gamma);

    double getR() const;

    void setR(double R);

    std::vector<State> getStates() const;

    void setStates(std::vector<State> states);

    double transitionModel(GridWorld &grid, State statePrime, State state, Actions action);

    Actions A(State s);
};

#endif //MDP_MDP_H
