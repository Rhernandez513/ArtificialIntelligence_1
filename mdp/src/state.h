//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_STATE_H
#define MDP_STATE_H


class state {
    int x;
    int y;
    int reward;
public:
    state(int x, int y, int reward);

    virtual ~state();

    int getX() const;

    void setX(int x);

    int getY() const;

    void setY(int y);

    int getReward() const;

    void setReward(int reward);
};


#endif //MDP_STATE_H
