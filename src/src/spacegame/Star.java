/** Project: Solo Lab 7 Assignment
 * Purpose Details: Represents a randomly colored background star in the Space Game.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Represents a single star in the game background.
 * Stars are randomly positioned and randomly colored.
 */
public class Star {

    /** X position of the star on screen. */
    private int x;

    /** Y position of the star on screen. */
    private int y;

    /** Size (diameter) of the star in pixels. */
    private int size;

    /** Color of the star, chosen randomly. */
    private Color color;

    /** Random number generator for star properties. */
    private static final Random random = new Random();

    /**
     * Constructs a Star with random position, size, and color.
     *
     * @param screenWidth  The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public Star(int screenWidth, int screenHeight) {
        this.x = random.nextInt(screenWidth);
        this.y = random.nextInt(screenHeight);
        this.size = random.nextInt(3) + 1;
        this.color = generateRandomStarColor();
    }

    /**
     * Generates a random star color chosen from white, yellow, cyan, or light blue.
     *
     * @return A randomly selected star Color.
     */
    private Color generateRandomStarColor() {
        Color[] starColors = {
                Color.WHITE,
                Color.YELLOW,
                Color.CYAN,
                new Color(180, 180, 255),
                new Color(255, 255, 200)
        };
        return starColors[random.nextInt(starColors.length)];
    }

    /**
     * Draws the star on the provided Graphics2D context.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillOval(x, y, size, size);
    }
}