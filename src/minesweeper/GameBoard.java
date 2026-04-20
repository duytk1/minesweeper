package minesweeper;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final int mineCount;
    private final Cell[][] cells;
    private boolean gameOver;
    private boolean win;
    private boolean initialized;

    public GameBoard(int rows, int cols, int mineCount) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.cells = new Cell[rows][cols];
        resetCells();
    }

    public void reset() {
        gameOver = false;
        win = false;
        initialized = false;
        resetCells();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean revealCell(int row, int col) {
        if (gameOver) {
            return false;
        }

        Cell cell = cells[row][col];
        if (cell.isFlagged() || cell.isRevealed()) {
            return false;
        }

        if (!initialized) {
            placeMines(row, col);
            calculateAdjacentMines();
            initialized = true;
        }

        if (cell.isMine()) {
            cell.reveal();
            gameOver = true;
            revealAllMines();
            return true;
        }

        floodReveal(row, col);
        evaluateWin();
        return false;
    }

    public void toggleFlag(int row, int col) {
        if (gameOver || cells[row][col].isRevealed()) {
            return;
        }
        cells[row][col].toggleFlag();
        evaluateWin();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWin() {
        return win;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int remainingMinesEstimate() {
        int flags = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isFlagged()) {
                    flags++;
                }
            }
        }
        return mineCount - flags;
    }

    private void resetCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    private void placeMines(int safeRow, int safeCol) {
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (cells[row][col].isMine()) {
                continue;
            }
            // Keep the first click and neighbors safe.
            if (Math.abs(row - safeRow) <= 1 && Math.abs(col - safeCol) <= 1) {
                continue;
            }
            cells[row][col].setMine(true);
            placed++;
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMine()) {
                    continue;
                }
                int mines = 0;
                for (int r = Math.max(0, row - 1); r <= Math.min(rows - 1, row + 1); r++) {
                    for (int c = Math.max(0, col - 1); c <= Math.min(cols - 1, col + 1); c++) {
                        if (cells[r][c].isMine()) {
                            mines++;
                        }
                    }
                }
                cells[row][col].setAdjacentMines(mines);
            }
        }
    }

    private void floodReveal(int startRow, int startCol) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startRow, startCol});

        while (!queue.isEmpty()) {
            int[] current = queue.remove();
            int row = current[0];
            int col = current[1];
            Cell cell = cells[row][col];

            if (cell.isRevealed() || cell.isFlagged()) {
                continue;
            }

            cell.reveal();
            if (cell.getAdjacentMines() != 0) {
                continue;
            }

            for (int r = Math.max(0, row - 1); r <= Math.min(rows - 1, row + 1); r++) {
                for (int c = Math.max(0, col - 1); c <= Math.min(cols - 1, col + 1); c++) {
                    if (!(r == row && c == col) && !cells[r][c].isMine()) {
                        queue.add(new int[]{r, c});
                    }
                }
            }
        }
    }

    private void evaluateWin() {
        int hiddenNonMines = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                if (!cell.isMine() && !cell.isRevealed()) {
                    hiddenNonMines++;
                }
            }
        }
        if (hiddenNonMines == 0 && !gameOver) {
            win = true;
            gameOver = true;
            revealAllMines();
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMine()) {
                    cells[row][col].reveal();
                }
            }
        }
    }
}
