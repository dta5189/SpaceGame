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
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents the player-controlled spaceship in the Space Game.
 * Handles position, image rendering, health, and shield state.
 */
public class Player {

    /** X position of the player on screen. */
    private int x;

    /** Y position of the player on screen. */
    private int y;

    /** Width of the player sprite in pixels. */
    private final int width = 64;

    /** Height of the player sprite in pixels. */
    private final int height = 64;

    /** Movement speed of the player in pixels per frame. */
    private final int speed = 5;

    /** Current health points of the player (0–100). */
    private int health;

    /** Maximum health points the player can have. */
    private final int maxHealth = 100;

    /** Whether the player's shield is currently active. */
    private boolean shieldActive;

    /** Image used to render the spaceship. */
    private BufferedImage shipImage;

    /** Flags for directional movement. */
    private boolean movingUp, movingDown, movingLeft, movingRight;

    /**
     * Constructs a Player at the given starting position with full health.
     *
     * @param startX Starting X coordinate.
     * @param startY Starting Y coordinate.
     */
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.health = maxHealth;
        this.shieldActive = false;
        loadImage();
    }

    /**
     * Loads the spaceship image from the resources folder.
     * Falls back to a drawn placeholder if the file is not found.
     */
    private void loadImage() {
        try {
            shipImage = ImageIO.read(getClass().getResourceAsStream("/spaceship.png"));
        } catch (IOException | IllegalArgumentException e) {
            shipImage = null; // will draw fallback shape
        }
    }

    /**
     * Updates the player's position based on active movement flags
     * and clamps position within the screen bounds.
     *
     * @param screenWidth  The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public void update(int screenWidth, int screenHeight) {
        if (movingUp)    y -= speed;
        if (movingDown)  y += speed;
        if (movingLeft)  x -= speed;
        if (movingRight) x += speed;

        // Clamp to screen bounds
        x = Math.max(0, Math.min(screenWidth - width, x));
        y = Math.max(0, Math.min(screenHeight - height, y));
    }

    /**
     * Draws the player ship and optionally a shield ring and health bar.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        // Draw shield visual
        if (shieldActive) {
            g2.setColor(new Color(0, 150, 255, 120));
            g2.fillOval(x - 10, y - 10, width + 20, height + 20);
            g2.setColor(new Color(0, 200, 255));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - 10, y - 10, width + 20, height + 20);
            g2.setStroke(new BasicStroke(1));
        }

        // Draw ship image or fallback
        if (shipImage != null) {
            g2.drawImage(shipImage, x, y, width, height, null);
        } else {
            g2.setColor(Color.GREEN);
            int[] xPts = {x + width / 2, x, x + width};
            int[] yPts = {y, y + height, y + height};
            g2.fillPolygon(xPts, yPts, 3);
        }

        // Draw health bar above ship
        drawHealthBar(g2);
    }

    /**
     * Draws a health bar above the player's ship.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawHealthBar(Graphics2D g2) {
        int barWidth = width;
        int barHeight = 6;
        int barX = x;
        int barY = y - 12;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        float ratio = (float) health / maxHealth;
        Color healthColor = ratio > 0.5f ? Color.GREEN : ratio > 0.25f ? Color.YELLOW : Color.RED;
        g2.setColor(healthColor);
        g2.fillRect(barX, barY, (int)(barWidth * ratio), barHeight);

        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    /**
     * Returns the bounding rectangle of the player for collision detection.
     *
     * @return A Rectangle representing the player's bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x + 8, y + 8, width - 16, height - 16);
    }

    /**
     * Reduces player health by the given damage amount.
     * Health will not drop below zero.
     *
     * @param damage The amount of damage to apply.
     */
    public void takeDamage(int damage) {
        if (!shieldActive) {
            health = Math.max(0, health - damage);
        }
    }

    /**
     * Increases player health by the given amount, up to maxHealth.
     *
     * @param amount The amount of health to restore.
     */
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /**
     * Returns whether the player is still alive (health > 0).
     *
     * @return True if health is greater than zero.
     */
    public boolean isAlive() {
        return health > 0;
    }

    // --- Getters and Setters ---

    /** @return Current X position. */
    public int getX() { return x; }

    /** @return Current Y position. */
    public int getY() { return y; }

    /** @return Player width. */
    public int getWidth() { return width; }

    /** @return Player height. */
    public int getHeight() { return height; }

    /** @return Current health value. */
    public int getHealth() { return health; }

    /** @return Maximum health value. */
    public int getMaxHealth() { return maxHealth; }

    /** @return True if shield is currently active. */
    public boolean isShieldActive() { return shieldActive; }

    /**
     * Sets whether the shield is active.
     *
     * @param active True to activate shield, false to deactivate.
     */
    public void setShieldActive(boolean active) { this.shieldActive = active; }

    /** @param movingUp True if the player is moving up. */
    public void setMovingUp(boolean movingUp) { this.movingUp = movingUp; }

    /** @param movingDown True if the player is moving down. */
    public void setMovingDown(boolean movingDown) { this.movingDown = movingDown; }

    /** @param movingLeft True if the player is moving left. */
    public void setMovingLeft(boolean movingLeft) { this.movingLeft = movingLeft; }

    /** @param movingRight True if the player is moving right. */
    public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
}