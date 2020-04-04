cmake_minimum_required(VERSION 3.15)
project(mdp)

set(CMAKE_CXX_STANDARD 17)

add_executable(mdp src/main.cpp src/MDP.cpp src/MDP.h src/Actions.h src/GridWorld.cpp src/GridWorld.h src/State.cpp src/State.h)