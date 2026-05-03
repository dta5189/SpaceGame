/** Project: Solo Lab 7 Assignment
 * Purpose Details: Represents a randomly colored, moving background star
 *                  with twinkle animation in the Space Game.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import java.awt.*;
import java.util.Random;

/**
 * Represents a single animated star in the game background.
 * Stars move downward, twinkle, and wrap around the screen.
 */
public class Star {

    /** X position of the star on screen. */
    private float x;

    /** Y position of the star on screen. */
    private float y;

    /** Size (diameter) of the star in pixels. */
    private int size;

    /** Base color of the star chosen randomly. */
    private Color color;

    /** Falling speed of the star in pixels per frame. */
    private float speed;

    /** Horizontal drift speed for a floating effect. */
    private float driftX;

    /** Current alpha (opacity) value for twinkle effect. */
    private float alpha;

    /** Direction the alpha is changing (1 = brightening, -1 = dimming). */
    private float alphaDir;

    /** Speed at which the star twinkles (alpha changes). */
    private float twinkleSpeed;

    /** Width of the game screen, used for wrapping. */
    private int screenWidth;

    /** Height of the game screen, used for wrapping. */
    private int screenHeight;

    /** Shared random number generator. */
    private static final Random random = new Random();

    /**
     * Constructs a Star with random position, color, size, speed, and twinkle rate.
     *
     * @param screenWidth  The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public Star(int screenWidth, int screenHeight) {
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        randomize(true);
    }

    /**
     * Randomizes all star properties. When initializing, stars are placed
     * anywhere on screen. When respawning after going off-screen, they
     * start at the top.
     *
     * @param anywhere True to place anywhere on screen, false to spawn at top.
     */
    private void randomize(boolean anywhere) {
        this.x           = random.nextInt(screenWidth);
        this.y           = anywhere ? random.nextInt(screenHeight) : -5;
        this.size        = random.nextInt(4) + 1;
        this.speed       = 0.3f + random.nextFloat() * 1.5f;
        this.driftX      = (random.nextFloat() - 0.5f) * 0.4f;
        this.alpha       = 0.4f + random.nextFloat() * 0.6f;
        this.alphaDir    = random.nextBoolean() ? 1f : -1f;
        this.twinkleSpeed = 0.008f + random.nextFloat() * 0.02f;
        this.color       = generateRandomStarColor();
    }

    /**
     * Generates a vivid random star color from a palette of space-themed colors.
     *
     * @return A randomly selected Color.
     */
    private Color generateRandomStarColor() {
        Color[] palette = {
                new Color(255, 255, 255),   // white
                new Color(255, 230, 100),   // warm yellow
                new Color(100, 220, 255),   // ice blue
                new Color(255, 120, 80),    // orange-red
                new Color(180, 100, 255),   // purple
                new Color(80,  255, 180),   // mint green
                new Color(255, 180, 220),   // pink
                new Color(140, 200, 255),   // light blue
                new Color(255, 255, 120),   // bright yellow
                new Color(200, 255, 200),   // pale green
        };
        return palette[random.nextInt(palette.length)];
    }

    /**
     * Updates the star's position and twinkle animation each frame.
     * Wraps the star back to the top when it moves off the bottom of the screen.
     * Wraps horizontally when drifting off the sides.
     */
    public void update() {
        // Move downward and drift sideways
        y += speed;
        x += driftX;

        // Twinkle: oscillate alpha between 0.2 and 1.0
        alpha += alphaDir * twinkleSpeed;
        if (alpha >= 1.0f) { alpha = 1.0f; alphaDir = -1f; }
        if (alpha <= 0.2f) { alpha = 0.2f; alphaDir =  1f; }

        // Wrap vertically
        if (y > screenHeight + 5) {
            randomize(false);
        }

        // Wrap horizontally
        if (x < -5)              x = screenWidth;
        if (x > screenWidth + 5) x = 0;
    }

    /**
     * Draws the star as a glowing colored circle with an alpha-based twinkle.
     * Larger stars get an extra soft glow halo behind them.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        // Outer glow for bigger stars
        if (size >= 3) {
            Color glowColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int)(alpha * 60)
            );
            g2.setColor(glowColor);
            g2.fillOval((int)x - size, (int)y - size, size * 3, size * 3);
        }

        // Core star dot
        Color drawColor = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int)(alpha * 255)
        );
        g2.setColor(drawColor);
        g2.fillOval((int)x, (int)y, size, size);
    }
}