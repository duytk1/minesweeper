package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MinesweeperUI extends JFrame {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int MINES = 12;

    private final GameBoard board;
    private final JButton[][] buttons;
    private final JLabel statusLabel;

    public MinesweeperUI() {
        this.board = new GameBoard(ROWS, COLS, MINES);
        this.buttons = new JButton[ROWS][COLS];
        this.statusLabel = new JLabel("", SwingConstants.CENTER);

        setTitle("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(8, 8));

        JButton resetButton = new JButton("New Game");
        resetButton.addActionListener(e -> resetGame());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        statusLabel.setText("Mines left: " + MINES);
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(resetButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton button = createCellButton(row, col);
                buttons[row][col] = button;
                gridPanel.add(button);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        pack();
        setMinimumSize(new Dimension(520, 560));
        setLocationRelativeTo(null);
        refreshBoard();
    }

    private JButton createCellButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(48, 48));
        button.setFocusPainted(false);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (board.isGameOver()) {
                    return;
                }

                if (event.getButton() == MouseEvent.BUTTON1) {
                    boolean hitMine = board.revealCell(row, col);
                    refreshBoard();
                    if (hitMine) {
                        showEndDialog("Game over! You hit a mine.");
                    } else if (board.isWin()) {
                        showEndDialog("You won! Great job.");
                    }
                } else if (event.getButton() == MouseEvent.BUTTON3) {
                    board.toggleFlag(row, col);
                    refreshBoard();
                    if (board.isWin()) {
                        showEndDialog("You won! Great job.");
                    }
                }
            }
        });
        return button;
    }

    private void refreshBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = board.getCell(row, col);
                JButton button = buttons[row][col];
                updateButtonForCell(button, cell);
            }
        }
        statusLabel.setText("Mines left: " + board.remainingMinesEstimate());
    }

    private void updateButtonForCell(JButton button, Cell cell) {
        button.setEnabled(!cell.isRevealed());
        button.setOpaque(true);
        button.setBorderPainted(true);

        if (cell.isRevealed()) {
            if (cell.isMine()) {
                button.setText("*");
                button.setBackground(new Color(244, 96, 96));
                button.setForeground(Color.BLACK);
            } else {
                int count = cell.getAdjacentMines();
                button.setText(count == 0 ? "" : Integer.toString(count));
                button.setBackground(new Color(228, 228, 228));
                button.setForeground(colorForNumber(count));
            }
            return;
        }

        button.setBackground(new Color(204, 204, 204));
        if (cell.isFlagged()) {
            button.setText("F");
            button.setForeground(new Color(216, 52, 52));
        } else {
            button.setText("");
            button.setForeground(Color.BLACK);
        }
    }

    private Color colorForNumber(int number) {
        switch (number) {
            case 1:
                return new Color(35, 91, 200);
            case 2:
                return new Color(38, 130, 46);
            case 3:
                return new Color(208, 46, 46);
            case 4:
                return new Color(95, 54, 181);
            case 5:
                return new Color(123, 43, 43);
            case 6:
                return new Color(31, 140, 140);
            case 7:
                return new Color(30, 30, 30);
            case 8:
                return new Color(95, 95, 95);
            default:
                return Color.BLACK;
        }
    }

    private void showEndDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGame() {
        board.reset();
        refreshBoard();
    }
}
