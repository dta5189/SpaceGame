/** Project: Solo Lab 7 Assignment
 * Purpose Details: Main entry point for the Space Game application.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Space Game application.
 * Creates and displays the main game window.
 */
public class Main {

    /**
     * Application entry point. Sets up the JFrame and starts the game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Space Game - David Adeleye");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            // Request focus so keyboard input works immediately
            gamePanel.requestFocusInWindow();
            gamePanel.startGameLoop();
        });
    }
}