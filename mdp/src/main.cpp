#include <iostream>
#include <string>
#include <fstream>
#include <regex>
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

int valueIteration();

int modifiedPolicyIteration();

int modifiedBellmanEquation();

int bellmanEquation();

int evaluatePolicy();

MDP constructMarkovDecisionProcess(std::string rawT, std::string rawEpsilon, std::string rawGamma, std::string rawR, GridWorld grid) {
    std::vector<std::string> _Tprobabilities = split(trim(split(rawT, ":")[1]), " ");
    std::vector<double> Tprobabilities;
    for(auto &s : _Tprobabilities) {
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
    for(int i = 0; i < width; ++i) {
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
        for(int j = 0; j < height; ++j) {
            if(std::find(populated.begin(), populated.end(), std::pair<int, int>(i+1, j+1)) != populated.end()) {
                /* vector contains element */
                continue;
            } else {
                /* vector does not contain element */
                states.push_back(State(i + 1, j + 1, R));
            }
        }
    }
    std::cout << "states" << std::endl;
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

    MDP mdp = constructMarkovDecisionProcess(lines[18], lines[22],lines[20],lines[14], grid);
    return 0;
}
