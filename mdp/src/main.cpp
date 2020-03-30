#include <iostream>
#include <string>
#include <fstream>
#include <regex>

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

std::string ltrim(const std::string& s) {
    return std::regex_replace(s, std::regex("^\\s+"), std::string(""));
}
std::string rtrim(const std::string& s) {
    return std::regex_replace(s, std::regex("\\s+$"), std::string(""));
}
// https://stackoverflow.com/a/2529011
std::string trim(std::string& s) {
    s.erase( std::remove(s.begin(), s.end(), '\r'), s.end() );
    return ltrim(rtrim(s));
}


// https://stackoverflow.com/a/13172514
std::vector<std::string> split(const std::string& str,
                                      const std::string& delimiter)
{
    std::vector<std::string> strings;

    std::string::size_type pos = 0;
    std::string::size_type prev = 0;
    while ((pos = str.find(delimiter, prev)) != std::string::npos)
    {
        strings.push_back(str.substr(prev, pos - prev));
        prev = pos + delimiter.size();
    }

    // To get the last substring (or only, if delimiter is not found)
    strings.push_back(str.substr(prev));

    return strings;
}

int main(int argc, char * argv[]) {
    std::cout << "Hello, World!" << std::endl;
    std::string fileInput = getLines(argv[1]);
    std::vector<std::string> lines = split(trim(fileInput), "\n");
    for(auto &str : lines) {
        std::cout << str << std::endl;
    }
    // size = lines[2]
    // walls = lines[6]
    // terminal states = lines[10]
    // reward = lines[14]
    // transition probabilities = lines[18]
    // discount rate = lines[20]
    // epsilon = lines [22]
    return 0;
}
