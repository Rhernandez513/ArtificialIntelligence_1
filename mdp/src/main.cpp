#include <iostream>
#include <string>
#include <fstream>
#include <regex>
#include <map>
#include <cmath>
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


// Figure 17.4 in AIMA 3rd ed
//function VALUE-ITERATION(mdp, epsilon) returns a utility funtion
//    inputs: mdp, an MDP with states S, actions A(s), transition model P(s'| s,a),
//            rewards R(s), discount: gamma
//
//    local variables: U, U', vectors of utilities for states in S, initially zero
//                    delta, the maximum change in the utility of any state in an iteration
//
//    repeat
//        U <- U'; delta <- 0
//        for each state s in S do
//            U'[s] <- R(s) + gamma * for max(action in A(s)) in sigma_s'{P(s'|s,a) * U[s']}
//
//            if absoulute_value(U'[s] - U[s]) > delta) then
//                delta <- absolute_value(U'[s] - U[s])
//    until delta < (epsilon * (1 - gamma)) / gamma
//    return U

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

// returns a Utility function for each State in MDP
std::map<State, double> valueIteration(MDP &mdp, GridWorld &grid) {
    std::vector<State> states = mdp.getStates();
    std::map<State, double> U;
    std::map<State, double> UPrime;
    for(auto &s : mdp.getStates()) {
        U[s] = mdp.getEpsilon();
        UPrime[s] = mdp.getEpsilon();
    }
    double delta = 0.0;
    double epsilon = mdp.getEpsilon();
    double gamma = mdp.getGamma();
    do {
//    while(delta < (epsilon * (1 - gamma)) / gamma) {
//        for(auto& [key, value] : UPrime) {
//            U[key] = value;
//        }
//        U = UPrime;
        delta = 0.0;
        for(auto &state : states) {
            std::set<Actions> actions = mdp.A(state, grid);
            std::vector<double> protoUtilities;
            for(auto &action : actions) {
                State statePrime = getStatePrime(mdp.getStates(), state, action);
                double weightedTransitionReward = mdp.transitionModel(grid, statePrime, state, action);
                double utility = U[statePrime];
                protoUtilities.push_back(weightedTransitionReward * utility);
            }
            double maxUtility = *std::max_element(protoUtilities.begin(), protoUtilities.end());
            UPrime[state] = state.getReward() + (gamma * maxUtility);
            double abs = std::fabs(UPrime[state] - U[state]);
            if(abs > delta) {
                delta = abs;
            }
        }
//        // I don't exactly know why I need to try and copy twice but that bug is to be worked out
        U = UPrime; // will perform copy operation
//    }
    } while(delta < (epsilon * (1 - gamma)) / gamma);
    return U;
}

int modifiedPolicyIteration();

int modifiedBellmanEquation();

int bellmanEquation();

int evaluatePolicy();

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

int main(int argc, char *argv[]) {
    std::cout << "Hello, World!" << std::endl;
    std::string fileInput = getLines(argv[1]);
    std::vector<std::string> lines = split(trim(fileInput), "\n");
    for (auto &str : lines) {
        std::cout << str << std::endl;
    }

    GridWorld grid = constructGridWorld(lines[2], lines[6], lines[10]);

    MDP mdp = constructMarkovDecisionProcess(lines[18], lines[22], lines[20], lines[14], grid);
    std::map<State, double> UtilityVector = valueIteration(mdp, grid);
    std::cout << "This version of the assignment is in an incomplete state" << std::endl;
    std::cout << "Input format should be the same as mdp_input.txt example provided (whitespace, colons, etc)" << std::endl;
    std::cout << "Here is the Utility vector at the conclusion of the VALUE-ITERATION subroutine: " << std::endl;
    for(auto &v : UtilityVector) {
        double e = mdp.getEpsilon();
        std::cout << "State: x(" << v.first.getX() << ") y(" << v.first.getY() << ")"  << " Utility: " << (floor(v.second * 10000) / 10000) << std::endl;
    }
    return 0;
}
