//
// Created by Robert David Hernandez on 3/29/20.
//

#ifndef MDP_MDP_H
#define MDP_MDP_H


class mdp {
    float epsilon; // the maximum error allowed in any state
    float gamma;   // discount factor
    float R;       // immediate reward in non-terminal states
    int state;
public:

    mdp(float epsilon, float gamma, float r, int state);

    virtual ~mdp();

    float getEpsilon() const;

    void setEpsilon(float epsilon);

    float getGamma() const;

    void setGamma(float gamma);

    float getR() const;

    void setR(float r);

    int getState() const;

    void setState(int state);

    float transitionModel(int state, int action);
};

#endif //MDP_MDP_H
