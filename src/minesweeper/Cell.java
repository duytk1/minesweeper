package minesweeper;

public class Cell {
    private boolean mine;
    private boolean revealed;
    private boolean flagged;
    private int adjacentMines;

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        this.revealed = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlag() {
        if (!revealed) {
            flagged = !flagged;
        }
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
