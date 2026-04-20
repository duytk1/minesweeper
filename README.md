# Java Minesweeper

Simple Minesweeper built with Java and a Swing interface.

## Features

- 10x10 board with 12 mines
- Left click to reveal cells
- Right click to place or remove flags
- Safe first click (first clicked cell and neighbors are mine-free)
- New Game button to restart instantly

## Run

Compile:

```bash
javac -d out src/minesweeper/*.java
```

Start:

```bash
java -cp out minesweeper.Main
```