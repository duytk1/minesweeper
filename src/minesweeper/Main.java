package minesweeper;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {
        setSystemLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            MinesweeperUI ui = new MinesweeperUI();
            ui.setVisible(true);
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // Continue with default look and feel if system style fails.
        }
    }
}
