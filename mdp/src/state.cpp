//
// Created by Robert David Hernandez on 3/29/20.
//

#include "state.h"

state::state(int x, int y, int reward) : x(x), y(y), reward(reward) {}

state::~state() {

}

int state::getX() const {
    return x;
}

void state::setX(int x) {
    state::x = x;
}

int state::getY() const {
    return y;
}

void state::setY(int y) {
    state::y = y;
}

int state::getReward() const {
    return reward;
}

void state::setReward(int reward) {
    state::reward = reward;
}
