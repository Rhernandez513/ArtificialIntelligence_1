#include <iostream>
#include <string>
#include <fstream>
#include <regex>
#include "gridWorld.h"

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

int policyIteration();

int modifiedBellmanEquation();

int bellmanEquation();

int evaluatePolicy();

int constructMarkovDecisionProcess();

int constructState();

int processInput(std::string input);

void constructGridWorld(std::string rawSize, std::string rawWallLocations, std::string rawTerminalStates) {

    // Determine width (x) and height (y) of grid
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

}

int main(int argc, char *argv[]) {
    std::cout << "Hello, World!" << std::endl;
    std::string fileInput = getLines(argv[1]);
    std::vector<std::string> lines = split(trim(fileInput), "\n");
    for (auto &str : lines) {
        std::cout << str << std::endl;
    }

//    gridWorld grid = constructGridWorld(lines[2], lines[6], lines[10]);
    constructGridWorld(lines[2], lines[6], lines[10]);

    // size = lines[2]
    // walls = lines[6]
    // terminal states = lines[10]
    // reward = lines[14]
    // transition probabilities = lines[18]
    // discount rate = lines[20]
    // epsilon = lines [22]
    return 0;
}
