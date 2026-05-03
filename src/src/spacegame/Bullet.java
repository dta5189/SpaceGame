/** Project: Solo Lab 7 Assignment
 * Purpose Details: Represents a laser bullet fired by the player spaceship.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import java.awt.*;

/**
 * Represents a single laser bullet fired by the player.
 * Moves upward each frame and is removed when off screen or hitting an obstacle.
 */
public class Bullet {

    /** X position of the bullet. */
    private int x;

    /** Y position of the bullet. */
    private int y;

    /** Width of the laser beam in pixels. */
    private final int width = 4;

    /** Height of the laser beam in pixels. */
    private final int height = 20;

    /** Speed the bullet travels upward per frame. */
    private final int speed = 12;

    /** Color of the laser beam core. */
    private final Color coreColor = new Color(0, 255, 255);

    /** Color of the outer glow of the laser. */
    private final Color glowColor = new Color(0, 150, 255, 100);

    /**
     * Constructs a Bullet at the given position.
     *
     * @param x Starting X position (centered on player).
     * @param y Starting Y position (top of player).
     */
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the bullet position, moving it upward each frame.
     */
    public void update() {
        y -= speed;
    }

    /**
     * Draws the laser beam as a glowing line on screen.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        // Outer glow
        g2.setColor(glowColor);
        g2.fillRect(x - 2, y, width + 4, height);

        // Bright core beam
        g2.setColor(coreColor);
        g2.fillRect(x, y, width, height);

        // White hot center
        g2.setColor(Color.WHITE);
        g2.fillRect(x + 1, y, 2, height);
    }

    /**
     * Returns the bounding rectangle for collision detection.
     *
     * @return Rectangle representing the bullet bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

 
    public boolean isOffScreen() {
        return y < -height;
    }
}