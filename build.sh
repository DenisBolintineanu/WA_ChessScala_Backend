#!/bin/bash

# Navigate to the subproject directory and run SBT
cd lib/ChessScala
sbt compile assembly

# Navigate back to the main project directory and run SBT
cd ../..
sbt compile
