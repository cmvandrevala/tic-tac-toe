# Tic-Tac-Toe in Clojure

[![Build Status](https://travis-ci.org/cmvandrevala/tic-tac-toe.svg?branch=master)](https://travis-ci.org/cmvandrevala/tic-tac-toe)

## Project Description

This is an implementation of tic-tac-toe in Clojure. I wrote it to become familiar with functional programming concepts and to learn the syntax of [Leiningen](http://leiningen.org/), [SpeclJ](http://speclj.com/), and [Clojure](https://clojure.org/). A player can start a game of tic-tac-toe against another human, a first-available-move computer player, or an unbeatable computer player. Additionally, the player can choose the order in which the two opponents move. All of the games are played on the console with a colored output.

## Testing the Codebase

You can run the SpeclJ test suite by entering the following command at the project root:

```
lein spec
```

## Playing a Game

You can start a game of tic-tac-toe on the console by using the command:

```
lein run <first-player> <second-player>
```

The user can choose one of three options for ```<first-player>``` and ```<second-player>```: human, easy, or hard. The easy keyword corresponds to the first-available-spot computer player while the hard keyword corresponds to the unbeatable computer player. The human keyword corresponds, of course, to a human player. The order in which the options are entered dictates the order in which the players move.
