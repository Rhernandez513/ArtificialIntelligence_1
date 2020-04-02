//
// Created by Robert David Hernandez on 3/29/20.
//

#include "MDP.h"
#include "Actions.h"


float MDP::transitionModel(State statePrime, State state, Actions action) { return -1.00; }


MDP::MDP(const std::vector<float> &transitionProbabilities, float epsilon, float gamma, float r,
         const std::vector<State> &states) : transitionProbabilities(transitionProbabilities), epsilon(epsilon),
                                             gamma(gamma), R(r), states(states) {}

MDP::~MDP() {}

float MDP::getEpsilon() const {
    return epsilon;
}

void MDP::setEpsilon(float epsilon) {
    MDP::epsilon = epsilon;
}

float MDP::getGamma() const {
    return gamma;
}

void MDP::setGamma(float gamma) {
    MDP::gamma = gamma;
}

float MDP::getR() const {
    return R;
}

void MDP::setR(float R) {
    MDP::R = R;
}

std::vector<State> MDP::getStates() const {
    return states;
}

void MDP::setStates(std::vector<State> states) {
    MDP::states = states;
}

const std::vector<float> &MDP::getTransitionProbabilities() const {
    return transitionProbabilities;
}

void MDP::setTransitionProbabilities(const std::vector<float> &transitionProbabilities) {
    MDP::transitionProbabilities = transitionProbabilities;
}
