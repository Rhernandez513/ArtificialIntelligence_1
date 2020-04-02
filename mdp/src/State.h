//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_STATE_H
#define MDP_STATE_H


class State {
    int x;
    int y;
    double reward;
public:
    State(int x, int y, double reward);

    virtual ~State();

    int getX() const;

    void setX(int x);

    int getY() const;

    void setY(int y);

    double getReward() const;

    void setReward(double reward);
};


#endif //MDP_STATE_H
