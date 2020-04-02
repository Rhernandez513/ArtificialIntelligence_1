//
// Created by Robert David Hernandez on 3/29/20.
//

#include "MDP.h"
#include "Actions.h"


double MDP::transitionModel(State statePrime, State state, Actions action) { return -1.00; }


MDP::MDP(const std::vector<double> &transitionProbabilities, double epsilon, double gamma, double R,
         const std::vector<State> &states) : transitionProbabilities(transitionProbabilities), epsilon(epsilon),
                                             gamma(gamma), R(R), states(states) {}

MDP::~MDP() {}

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

std::vector<State> MDP::getStates() const {
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
