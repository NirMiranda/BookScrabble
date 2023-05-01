# Book Scrabble Game

This is a project for a Book Scrabble game, where players will compete to create words and score points based on the tiles they use and the placement of those tiles on the board. The game will be built by a team of four developers: Chen, Nir, Uri, and Dorin. Each team member will be assigned a specific role to help with the development of the game.

## Table of Contents

1.Collaborators

2.Video links

3.Game role 

4.Game Overview

5.Usage

6.Vision

7.Planning

8.Gantt Chart

## Collaborators
* [Dorin Cohen](https://github.com/Dorins12)
* [Nir Miranda](https://github.com/NirMiranda)
* [Ori Farkash](https://github.com/orifarkash)
* [Chen Amrani](https://github.com/chenAmrani)
## Video links
 * Demo video of the board  - will be added
 * Project presentation video - will be added
 
## Game role 
We have established a set of rules that simplifies the original game. Here's how it works:
1. Start by randomly drawing a tile from the bag.
2. The order of players is determined by the letters drawn, from smallest to largest. If an empty tile is drawn, return it and draw another one.
3. After each turn, all the tiles go back into the bag.
4. Draw seven tiles to start with.
5. The first player (who drew the smallest letter) must create a legal word that passes through the central slot (the star) on the board. They receive double points for this word and then draw new tiles to replenish their rack.
6. Players take turns creating legal words that intersect with existing words on the board. Each word's value is calculated by summing the value of each tile, taking into account double and triple letter and word spaces on the board. Points accumulate for each word played.
7. If a player cannot form a legal word, they forfeit their turn.
8. The game ends after N rounds.
For a word to be legal, it must satisfy the following criteria:
* Be written from left to right or top to bottom (not diagonally)
* Appear in one of the pre-selected books for the game
* Touch at least one tile already on the board
* Not form any illegal words on the board

## Game Overview

The Book Scrabble game is played on a board that is 15x15 squares in size. Players will use letter tiles to create words on the board, and score points based on the value of those tiles and the placement of their words on the board. The board has some bonus slots, which are marked as follows:

- The central square (marked with a star) doubles the value of the word written on it.
- Squares that double the value of the letter on them (light blue).
- Squares that triple the value of the letter on them (blue).
- Squares that double the value of the entire word (yellow).
- Squares that triple the value of the entire word (red).

## Usage
* Game Board

The game board is a 15x15 grid of squares, with the bonus slots marked as described above. Players will place their letter tiles on the board to create words, and score points based on the value of those tiles and the placement of their words on the board.
* The borad will loke like the following diagram that we create :

<img src="https://user-images.githubusercontent.com/118439273/229484247-4854a0a0-7e4f-4f2d-9e87-60fadd52d077.png" width="520" height="520" />

* Tille 
 As part of our project, we have simplified the rules of the game. Here's what you need to know:
 The game board is small and includes English letters with assigned point values.
 * Check out the diagram below to see how many points each letter is worth in the game:
 
 <img src="https://user-images.githubusercontent.com/118439273/229486188-1f5ab09f-8f78-4e12-b2d1-8aea60616c7c.png" width="360" height="180" />
 
 * Bag
There are a total of 98 tiles in the bag.
The bag allows for randomization of the tiles.
At the beginning of the game, each letter has a specific number of tiles in the bag.

<img src="https://user-images.githubusercontent.com/118439273/229486334-5985074d-ffa1-4366-bc21-8acfd1d4445d.png" width="1080" height="120" />
 
 

## Vision

The vision for the Book Scrabble game is to create an engaging and fun experience for players of all ages. The game should be easy to learn, but challenging to master, with strategic gameplay and a variety of different ways to score points.

## Planning

The team will work on the project from now until 30.06 To meet this deadline, we have created the following plan:

- Week 1: Planning and setup
- Week 2: Develop basic game logic and board layout
- Week 3: Implement game rules and scoring
- Week 4: Develop UI and frontend features
- Week 5: Backend development and testing
- Week 6: Quality assurance, bug fixing, and final polish

## Gantt Chart

Here is a Gantt chart showing the planned timeline for the project:

![Gantt Chart](gantt_chart.png)

This chart shows the planned tasks for each week of the project, along with their estimated duration and dependencies. We will use this chart to help us stay on track and ensure that we meet our deadline of 30.06.
