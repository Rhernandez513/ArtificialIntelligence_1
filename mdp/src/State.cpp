//
// Created by Robert David Hernandez on 3/29/20.
//

#include "State.h"

State::State(int x, int y, double reward) : x(x), y(y), reward(reward) {}

State::~State() {

}

int State::getX() const {
    return x;
}

void State::setX(int x) {
    State::x = x;
}

int State::getY() const {
    return y;
}

void State::setY(int y) {
    State::y = y;
}

double State::getReward() const {
    return reward;
}

void State::setReward(double reward) {
    State::reward = reward;
}

bool State::operator==(const State &rhs) const {
    return x == rhs.x &&
           y == rhs.y;
}

bool State::operator!=(const State &rhs) const {
    return !(rhs == *this);
}

bool State::operator<(const State &rhs) const {
    if (x < rhs.x)
        return true;
    if (rhs.x < x)
        return false;
    return y < rhs.y;
}

bool State::operator>(const State &rhs) const {
    return rhs < *this;
}

bool State::operator<=(const State &rhs) const {
    return !(rhs < *this);
}

bool State::operator>=(const State &rhs) const {
    return !(*this < rhs);
}
