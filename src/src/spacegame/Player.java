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
import java.awt.geom.*;

/**
 * Represents the player-controlled spaceship in the Space Game.
 * Drawn entirely with Java 2D shapes — no image file required.
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

    /** Current health points of the player (0-100). */
    private int health;

    /** Maximum health points the player can have. */
    private final int maxHealth = 100;

    /** Whether the player's shield is currently active. */
    private boolean shieldActive;

    /** Flags for directional movement. */
    private boolean movingUp, movingDown, movingLeft, movingRight;

    /** Animation tick for engine flame flicker. */
    private int flameTick;

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

        x = Math.max(0, Math.min(screenWidth - width, x));
        y = Math.max(0, Math.min(screenHeight - height, y));

        flameTick++;
    }

    /**
     * Draws the spaceship using Java 2D shapes to look like a real spacecraft.
     * Includes hull, cockpit, wings, engine glow, and optional shield ring.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(x, y);

        // --- Engine flame (flickers) ---
        boolean flameOn = (flameTick % 6) < 4;
        if (flameOn) {
            // Outer flame
            GradientPaint outerFlame = new GradientPaint(
                    32, 58, new Color(255, 100, 0, 200),
                    32, 76, new Color(255, 50, 0, 0)
            );
            g.setPaint(outerFlame);
            int[] fx = {24, 32, 40};
            int[] fy = {58, 76, 58};
            g.fillPolygon(fx, fy, 3);

            // Inner flame
            GradientPaint innerFlame = new GradientPaint(
                    32, 58, new Color(255, 255, 150, 230),
                    32, 68, new Color(255, 180, 0, 0)
            );
            g.setPaint(innerFlame);
            int[] ifx = {28, 32, 36};
            int[] ify = {58, 68, 58};
            g.fillPolygon(ifx, ify, 3);
        }

        // --- Left wing ---
        GradientPaint wingGrad = new GradientPaint(
                0, 40, new Color(30, 60, 120),
                32, 40, new Color(60, 120, 200)
        );
        g.setPaint(wingGrad);
        int[] lwx = {0, 20, 26, 10};
        int[] lwy = {55, 30, 55, 60};
        g.fillPolygon(lwx, lwy, 4);

        // Wing edge highlight
        g.setColor(new Color(100, 180, 255, 150));
        g.setStroke(new BasicStroke(1.2f));
        g.drawPolygon(lwx, lwy, 4);

        // --- Right wing ---
        g.setPaint(new GradientPaint(
                32, 40, new Color(60, 120, 200),
                64, 40, new Color(30, 60, 120)
        ));
        int[] rwx = {64, 44, 38, 54};
        int[] rwy = {55, 30, 55, 60};
        g.fillPolygon(rwx, rwy, 4);
        g.setColor(new Color(100, 180, 255, 150));
        g.drawPolygon(rwx, rwy, 4);

        // --- Main hull body ---
        GradientPaint hullGrad = new GradientPaint(
                20, 10, new Color(80, 140, 220),
                44, 58, new Color(20, 50, 120)
        );
        g.setPaint(hullGrad);
        int[] hx = {32, 44, 46, 44, 32, 20, 18, 20};
        int[] hy = {4,  18, 35, 55, 60, 55, 35, 18};
        g.fillPolygon(hx, hy, 8);

        // Hull outline
        g.setColor(new Color(120, 200, 255, 180));
        g.setStroke(new BasicStroke(1.5f));
        g.drawPolygon(hx, hy, 8);

        // --- Nose cone ---
        GradientPaint noseGrad = new GradientPaint(
                28, 4, new Color(150, 210, 255),
                36, 4, new Color(60, 130, 200)
        );
        g.setPaint(noseGrad);
        int[] nx = {32, 38, 26};
        int[] ny = {2,  18, 18};
        g.fillPolygon(nx, ny, 3);
        g.setColor(new Color(180, 230, 255, 200));
        g.setStroke(new BasicStroke(1f));
        g.drawPolygon(nx, ny, 3);

        // --- Cockpit window ---
        GradientPaint cockpitGrad = new GradientPaint(
                27, 20, new Color(180, 240, 255, 220),
                37, 34, new Color(0, 100, 180, 200)
        );
        g.setPaint(cockpitGrad);
        g.fillOval(25, 19, 14, 18);
        // Cockpit shine
        g.setColor(new Color(255, 255, 255, 120));
        g.fillOval(27, 21, 5, 6);
        g.setColor(new Color(100, 200, 255));
        g.setStroke(new BasicStroke(1f));
        g.drawOval(25, 19, 14, 18);

        // --- Engine nozzle ---
        g.setPaint(new GradientPaint(
                24, 54, new Color(40, 40, 80),
                40, 60, new Color(80, 80, 140)
        ));
        g.fillRoundRect(24, 54, 16, 8, 4, 4);
        g.setColor(new Color(100, 100, 180));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(24, 54, 16, 8, 4, 4);

        // Engine glow ring
        g.setColor(new Color(255, 150, 0, 120));
        g.fillOval(27, 58, 10, 5);

        // --- Wing accent lines ---
        g.setColor(new Color(150, 220, 255, 100));
        g.setStroke(new BasicStroke(1f));
        g.drawLine(10, 50, 22, 35);  // left wing line
        g.drawLine(54, 50, 42, 35);  // right wing line

        // --- Shield effect ---
        if (shieldActive) {
            g.setColor(new Color(0, 180, 255, 60));
            g.fillOval(-12, -12, 88, 88);
            g.setColor(new Color(0, 220, 255, 180));
            g.setStroke(new BasicStroke(2.5f));
            g.drawOval(-12, -12, 88, 88);
            // Shield shimmer dots
            g.setColor(new Color(150, 240, 255, 200));
            g.fillOval(-10, 20, 5, 5);
            g.fillOval(69, 20, 5, 5);
            g.fillOval(28, -10, 5, 5);
        }

        g.dispose();

        // Draw health bar above ship (in original coordinates)
        drawHealthBar(g2);
    }

    /**
     * Draws a color-coded health bar above the player's ship.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawHealthBar(Graphics2D g2) {
        int barWidth = width;
        int barHeight = 6;
        int barX = x;
        int barY = y - 14;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        float ratio = (float) health / maxHealth;
        Color healthColor = ratio > 0.5f ? new Color(0, 200, 80)
                : ratio > 0.25f ? Color.YELLOW
                : Color.RED;
        g2.setColor(healthColor);
        g2.fillRect(barX, barY, (int)(barWidth * ratio), barHeight);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    /**
     * Returns the bounding rectangle of the player for collision detection.
     *
     * @return A Rectangle representing the player's collision bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x + 10, y + 10, width - 20, height - 16);
    }

    /**
     * Reduces player health by the given damage amount, minimum zero.
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
     * Returns whether the player is still alive.
     *
     * @return True if health is greater than zero.
     */
    public boolean isAlive() { return health > 0; }

    /** @return Current X position. */
    public int getX() { return x; }

    /** @return Current Y position. */
    public int getY() { return y; }

    /** @return Player width in pixels. */
    public int getWidth() { return width; }

    /** @return Player height in pixels. */
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
     * @param active True to activate, false to deactivate.
     */
    public void setShieldActive(boolean active) { this.shieldActive = active; }

    /** @param movingUp True if moving up. */
    public void setMovingUp(boolean movingUp) { this.movingUp = movingUp; }

    /** @param movingDown True if moving down. */
    public void setMovingDown(boolean movingDown) { this.movingDown = movingDown; }

    /** @param movingLeft True if moving left. */
    public void setMovingLeft(boolean movingLeft) { this.movingLeft = movingLeft; }

    /** @param movingRight True if moving right. */
    public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
}