cmake_minimum_required (VERSION 3.1.0)
project (HelloWorld)
set(CMAKE_CXX_STANDARD 14)
SET(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -g -Wall")
include_directories("${CMAKE_CURRENT_SOURCE_DIR}")
add_executable(HelloWorld 
	src/main.cpp
	src/Rectangle.cpp
	src/Rectangle.h
)
