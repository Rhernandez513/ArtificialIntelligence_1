//
// Created by Robert David Hernandez on 3/29/20.
//

#include "mdp.h"


float mdp::transitionModel(int state, int action) { return -1.00; }

mdp::mdp(float epsilon, float gamma, float r, int state) : epsilon(epsilon), gamma(gamma), R(r), state(state) {}

mdp::~mdp() {}

float mdp::getEpsilon() const {
    return epsilon;
}

void mdp::setEpsilon(float epsilon) {
    mdp::epsilon = epsilon;
}

float mdp::getGamma() const {
    return gamma;
}

void mdp::setGamma(float gamma) {
    mdp::gamma = gamma;
}

float mdp::getR() const {
    return R;
}

void mdp::setR(float r) {
    R = r;
}

int mdp::getState() const {
    return state;
}

void mdp::setState(int state) {
    mdp::state = state;
}

