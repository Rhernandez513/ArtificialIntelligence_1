cmake_minimum_required(VERSION 3.15)
project(mdp)

set(CMAKE_CXX_STANDARD 17)

add_executable(mdp src/main.cpp src/mdp.cpp src/mdp.h src/Actions.cpp src/Actions.h src/gridWorld.cpp src/gridWorld.h src/state.cpp src/state.h)