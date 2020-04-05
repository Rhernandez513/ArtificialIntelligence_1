#include <iostream>
#include <string>
#include <fstream>
#include <regex>
#include <map>
#include <cmath>
#include <sstream>
#include "GridWorld.h"
#include "MDP.h"

std::string getLines(std::string fileName) {
    std::ifstream file(fileName);
    std::string str;
    std::string file_contents;
    while (std::getline(file, str)) {
        file_contents += str;
        file_contents.push_back('\n');
    }
    return file_contents;
}

std::string ltrim(const std::string &s) {
    return std::regex_replace(s, std::regex("^\\s+"), std::string(""));
}

std::string rtrim(const std::string &s) {
    return std::regex_replace(s, std::regex("\\s+$"), std::string(""));
}

// https://stackoverflow.com/a/2529011
std::string trim(std::string &s) {
    s.erase(std::remove(s.begin(), s.end(), '\r'), s.end());
    return ltrim(rtrim(s));
}

// https://stackoverflow.com/a/13172514
std::vector<std::string> split(const std::string &str,
                               const std::string &delimiter) {
    std::vector<std::string> strings;

    std::string::size_type pos = 0;
    std::string::size_type prev = 0;
    while ((pos = str.find(delimiter, prev)) != std::string::npos) {
        strings.push_back(str.substr(prev, pos - prev));
        prev = pos + delimiter.size();
    }

    // To get the last substring (or only, if delimiter is not found)
    strings.push_back(str.substr(prev));

    return strings;
}

// Example
//     3
//     2
// y ^ 1  2  3
//    x >
std::string prettyPrintGrid(std::map<State, double> const &utilityVector, GridWorld &gridWorld, double epsilon) {

    std::stringstream s;
    s << epsilon;
    int sig = 1;
    for(int i = 1; i < s.str().size(); ++i) {
        sig *= 10;
    }
    s.str(std::string());
    s.clear();


    int width = gridWorld.getWidth();
    int y = gridWorld.getHeight();
    int x = 1;

    int stateCount = utilityVector.size();
    while(1) {
        if(stateCount == 0) { break; }
        for(auto &v : utilityVector) {
            if (v.first.getX() == x && v.first.getY() == y) {
                --stateCount;
                double value = (floor(v.second * sig) / sig);
                if(value == 0) {
                    s << "   -----  ";
                } else {
                    s << " " << value << " ";
                }
                if(v.first.getX() == width) {
                    s << std::endl;
                }
                break;
            }
        }
        if(x == width) {
            x = 1;
            --y;
        } else {
            ++x;
        }
    }

    return s.str();
}

State getStatePrime(std::vector<State> allStates, State state, Actions action) {
    int x = state.getX();
    int y = state.getY();
    State protoStatePrime = State(0.0, 0.0, 0.0);
    switch (action) {
        case Actions::Up:
            protoStatePrime = State(x, ++y, 0.0);
            break;
        case Actions::Left:
            protoStatePrime = State(--x, y, 0.0);
            break;
        case Actions::Right:
            protoStatePrime = State(++x, y, 0.0);
            break;
        case Actions::Down:
            protoStatePrime = State(x, --y, 0.0);
            break;
    }
    for(auto &s : allStates) {
        if(s == protoStatePrime) {
            return s;
        }
    }
}

bool isTerminalOrWall(State &state, GridWorld &grid) {
    for(auto &pair: grid.getTerminalCoordinates()) {
        if(state.getX() == pair.getX() && state.getY() == pair.getY()) {
            return true;
        }
    }
    for(auto &pair : grid.getPillars()) {
        if(state.getX() == pair.first && state.getY() == pair.second) {
            return true;
        }
    }
    return false;
}

double bellmanEquation(std::set<Actions> &actions, MDP &mdp, State const &state, GridWorld &grid, std::map<State, double> &U) {
    std::vector<double> protoUtilities;
    for(auto &action : actions) {
        State statePrime = getStatePrime(mdp.getStates(), state, action);
        double weightedTransitionReward = mdp.transitionModel(grid, statePrime, state, action);
        double utility = U[statePrime];
        protoUtilities.push_back(weightedTransitionReward * utility);
    }
    double maxUtility = *std::max_element(protoUtilities.begin(), protoUtilities.end());

    return state.getReward() + (mdp.getGamma() * maxUtility);
}

// Figure 17.4 in AIMA 3rd ed
// returns a Utility function for each State in MDP
std::map<State, double> valueIteration(MDP &mdp, GridWorld &grid, bool printProgress) {
    double delta;
    double epsilon = mdp.getEpsilon();
    double gamma = mdp.getGamma();

    std::map<State, double> U;
    std::map<State, double> UPrime;
    std::vector<State> states = mdp.getStates();

    for(auto &s : states) {
        if(isTerminalOrWall(s, grid)) {
            U[s] = s.getReward();
            UPrime[s] = s.getReward();
        } else {
            U[s] = mdp.getEpsilon();
            UPrime[s] = mdp.getEpsilon();
        }
    }
    int iter = 0;
    do {
        delta = 0.0;
        for(auto &state : states) {
            // if terminal state, we don't update it's utility
            if(isTerminalOrWall(state, grid)) { continue; }
            std::set<Actions> actions = mdp.A(state, grid);
            UPrime[state] = bellmanEquation(actions, mdp, state, grid, U);
            double abs = std::fabs(UPrime[state] - U[state]);
            if(abs > delta) {
                delta = abs;
            }
        }
        if(printProgress) {
            std::cout << "Value Iteration #" << ++iter << std::endl;
            std::cout << prettyPrintGrid(U, grid, mdp.getEpsilon());
        }
        U = UPrime; // will perform copy operation
    } while(delta < (epsilon * (1 - gamma)) / gamma);
    return U;
}

MDP constructMarkovDecisionProcess(std::string rawT, std::string rawEpsilon, std::string rawGamma, std::string rawR,
                                   GridWorld grid) {
    std::vector<std::string> _Tprobabilities = split(trim(split(rawT, ":")[1]), " ");
    std::vector<double> Tprobabilities;
    for (auto &s : _Tprobabilities) {
        Tprobabilities.push_back(std::stod(s));
    }


    double epsilon = std::stod(split(rawEpsilon, ":")[1]);
    double gamma = std::stod(split(rawGamma, ":")[1]);
    double R = std::stod(split(rawR, ":")[1]);
    std::vector<State> terminalStates = grid.getTerminalCoordinates();
    std::vector<std::pair<int, int>> walls = grid.getPillars();

    std::vector<State> states;
    int terminalStateCount = terminalStates.size();
    int pillarCount = walls.size();
    int width = grid.getWidth();
    int height = grid.getHeight();
    std::vector<std::pair<int, int>> populated;
    for (int i = 0; i < width; ++i) {
        if (i < terminalStateCount) {
            int x = terminalStates[i].getX();
            int y = terminalStates[i].getY();
            states.push_back(State(x, y, terminalStates[i].getReward()));
            populated.push_back(std::pair<int, int>(x, y));
        }
        if (i < pillarCount) {
            int x = walls[i].first;
            int y = walls[i].second;
            states.push_back(State(x, y, 0.0));
            populated.push_back(std::pair<int, int>(x, y));
        }
        for (int j = 0; j < height; ++j) {
            if (std::find(populated.begin(), populated.end(), std::pair<int, int>(i + 1, j + 1)) != populated.end()) {
                /* vector contains element */
                continue;
            } else {
                /* vector does not contain element */
                states.push_back(State(i + 1, j + 1, R));
            }
        }
    }
    return MDP(Tprobabilities, epsilon, gamma, R, states);
}

GridWorld constructGridWorld(std::string rawSize, std::string rawWallLocations, std::string rawTerminalStates) {

    // Determine height (x) and height (y) of grid
    std::vector<std::string> _a = split(rawSize, ":");
    std::vector<std::string> a = split(_a[1], " ");

    int gridWidth = std::stoi(a[1]);
    int gridHeight = std::stoi(a[2]);

    // Determine location of walls
    std::vector<std::string> _b = split(rawWallLocations, ":");
    std::vector<std::string> b = split(_b[1], ",");
    std::vector<std::pair<int, int>> wallXYs;
    for (auto &str : b) {
        str = trim(str);
        std::vector<std::string> tuple = split(str, " ");
        wallXYs.push_back(std::pair<int, int>(std::stoi(tuple[0]), std::stoi(tuple[1])));
    }

    std::vector<std::string> _c = split(rawTerminalStates, ":");
    std::vector<std::string> c = split(_c[1], ",");
    std::vector<State> terminalStates;
    for (auto &s : c) {
        s = trim(s);
        std::vector<std::string> states = split(s, " ");
        terminalStates.push_back(State(
                std::stoi(states[0]), std::stoi(states[1]), std::stoi(states[2])));
    }
    return GridWorld(gridWidth, gridHeight, wallXYs, terminalStates);
}

// Psuedo
//function POLICY-ITERATION(mdp) returns a policy
//    inputs: mdp, an MDP with States, actions A(s), transition model P(s' | s,a)
//    local variables: U, a vector of utilities for states in S, initially zero
//                     pi, a policy vector indexed by state, initially random
//
//    repeat
//       U <- POLICY-EVALUATION(pi, U, mdp)
//       unchanged? <- true
//       for each state in s do
//           if max(a in A(s)) in sigma_s'{ P(s'|s,a) U[s']} > sigma_s'{ P(s'| s, pi[s]) * U[s'] } then do
//               pi[s] <- argmax(a in A(s)) in sigma_s'{ P(s' | s,a) * U[s']
//               unchanged? <- false
//    until unchanged?
//    return pi


// basic idea is to run Bellman Update k times
std::map<State, double> policyEvaluation(std::map<State, Actions> &pi, std::map<State, double> U, MDP &mdp, GridWorld &grid) {
    int k = 10;
    for(int i = 0; i < k; ++i) {
        for(auto &v : U) {
            State state = v.first; // more readable, copy ops are not a worry
            std::set<Actions> action { pi[state] };
            U[state] = bellmanEquation(action, mdp, state, grid, U);
        }
    }
    return U;
}

// based on AIMA 3rd ed, Figure 17.7
int modifiedPolicyIteration(MDP &mdp, GridWorld &grid) {
    // init
    std::map<State, Actions> pi;
    std::map<State, double> U;
    std::vector<State> states = mdp.getStates();
    srand((unsigned)time(NULL));
    for(auto &s : states) {
        U[s] = (isTerminalOrWall(s, grid)) ? s.getReward() : mdp.getEpsilon();
        pi[s] = Actions(rand() % 4 + 1);
    }
    bool unchanged = true;
    do {
        U = policyEvaluation(pi, U, mdp, grid);
        for(auto &state: states) {
//           if max(a in A(s)) in sigma_s'{ P(s'|s,a) U[s']} > sigma_s'{ P(s'| s, pi[s]) * U[s'] } then do
//               pi[s] <- argmax(a in A(s)) in sigma_s'{ P(s' | s,a) * U[s']
//               unchanged? <- false
        }
    } while (unchanged);
}


int main(int argc, char *argv[]) {

    std::string fileInput = getLines(argv[1]);
    std::vector<std::string> lines = split(trim(fileInput), "\n");

    GridWorld grid = constructGridWorld(lines[2], lines[6], lines[10]);

    MDP mdp = constructMarkovDecisionProcess(lines[18], lines[22], lines[20], lines[14], grid);
    std::map<State, double> utilityVector;

    std::cout << "This version of the assignment is in an incomplete state" << std::endl;
    std::cout << "Input format should be the same as mdp_input.txt example provided (whitespace, colons, etc)\n" << std::endl;
    utilityVector = valueIteration(mdp, grid, true);
    std::cout << "\nFinal Utility Vector (Value-Iteration): " << std::endl;
    std::cout << prettyPrintGrid(utilityVector, grid, mdp.getEpsilon()) << std::endl;
    return 0;
}
