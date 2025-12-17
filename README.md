# About

This is my final project for the MAS course at UGE university.

# Prerequisites

- A Java 25 installation
- maven
- ollama installed
- granite3.1-moe:1b needs to be accessible in ollama

# Building

By running `maven clean package install`, a jar is produced that contains all dependencies in `target/`.

# Running

Make sure that ollama is started.
Then, just execute the jar with `java -jar final-1.0-SNAPSHOT-jar-with-dependencies.jar`
Apart from logs, which are saved, simulation output is stored in several CSV files.
- `balances.csv` contains the account balances of each agent at the end of each round
- `events.csv` contains the event that occured each round in plaintext
- `price.csv` contains the price at the end of each round
- `valuations.csv` contains the valuations (stock value + account balance) of the agents at the end of each round

# Configuring

All configuration options are located in `Configuration.java`.
Just change the values there.
Most meaningful are of course the chances of positive events, as well as the different consequence probabilities.