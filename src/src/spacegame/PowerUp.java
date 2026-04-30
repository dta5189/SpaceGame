/** Project: Solo Lab 7 Assignment
 * Purpose Details: Represents the player spaceship with movement, shooting,
 *                  health, and shield mechanics.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import java.awt.*;

/**
 * Represents a falling health power-up in the Space Game.
 * When collected by the player, it restores a set amount of health.
 */
public class PowerUp {

    /** X position of the power-up. */
    private int x;

    /** Y position of the power-up. */
    private int y;

    /** Size (width and height) of the power-up icon. */
    private final int size = 24;

    /** Falling speed in pixels per frame. */
    private final int speed = 2;

    /** Amount of health this power-up restores when collected. */
    private final int healAmount = 25;

    /**
     * Constructs a PowerUp at the given coordinates.
     *
     * @param x Starting X position.
     * @param y Starting Y position.
     */
    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the power-up's position, moving it downward each frame.
     */
    public void update() {
        y += speed;
    }

    /**
     * Draws the power-up as a green cross/plus symbol on screen.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 220, 80));
        // Horizontal bar
        g2.fillRect(x, y + size / 3, size, size / 3);
        // Vertical bar
        g2.fillRect(x + size / 3, y, size / 3, size);

        g2.setColor(Color.WHITE);
        g2.drawRect(x, y + size / 3, size, size / 3);
        g2.drawRect(x + size / 3, y, size / 3, size);
    }

    /**
     * Returns the bounding rectangle for collision detection.
     *
     * @return Rectangle representing the power-up's bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * Returns whether the power-up has moved below the screen.
     *
     * @param screenHeight The height of the game screen.
     * @return True if the power-up is off screen.
     */
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    /**
     * Returns the amount of health this power-up restores.
     *
     * @return Heal amount in hit points.
     */
    public int getHealAmount() {
        return healAmount;
    }
}