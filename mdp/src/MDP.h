//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_MDP_H
#define MDP_MDP_H


#include <vector>
#include "State.h"
#include "Actions.h"

class MDP {
    std::vector<float> transitionProbabilities;
    float epsilon; // the maximum error allowed in any State
    float gamma;   // discount factor
    float R;       // immediate reward in non-terminal states
    std::vector<State> states;
public:
    MDP(const std::vector<float> &transitionProbabilities, float epsilon, float gamma, float r,
        const std::vector<State> &states);

    virtual ~MDP();

    const std::vector<float> &getTransitionProbabilities() const;

    void setTransitionProbabilities(const std::vector<float> &transitionProbabilities);

    float getEpsilon() const;

    void setEpsilon(float epsilon);

    float getGamma() const;

    void setGamma(float gamma);

    float getR() const;

    void setR(float R);

    std::vector<State> getStates() const;

    void setStates(std::vector<State> states);

    float transitionModel(State statePrime, State state, Actions action);
};

#endif //MDP_MDP_H
