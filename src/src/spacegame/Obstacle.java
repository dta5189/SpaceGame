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
 * Represents a falling obstacle (asteroid/enemy) in the Space Game.
 * Uses a 4-frame horizontal sprite sheet for animation.
 */
public class Obstacle {

    /** X position of the obstacle. */
    private int x;

    /** Y position of the obstacle. */
    private int y;

    /** Width of each sprite frame in pixels. */
    private final int frameWidth = 64;

    /** Height of the sprite frame in pixels. */
    private final int frameHeight = 64;

    /** Falling speed in pixels per frame. */
    private int speed;

    /** Index of the current animation frame (0–3). */
    private int currentFrame;

    /** Counter used to throttle animation frame changes. */
    private int animCounter;

    /** Number of game updates between animation frame changes. */
    private final int animDelay = 8;

    /** Total number of frames in the sprite sheet. */
    private final int totalFrames = 4;

    /** The full sprite sheet image containing all 4 frames. */
    private static BufferedImage spriteSheet;

    /** Whether the sprite sheet has been loaded yet. */
    private static boolean imageLoaded = false;

    /**
     * Constructs an Obstacle at the given position with the given speed.
     *
     * @param x     Starting X position.
     * @param y     Starting Y position.
     * @param speed Falling speed in pixels per frame.
     */
    public Obstacle(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.currentFrame = 0;
        this.animCounter = 0;
        loadSpriteSheet();
    }

    /**
     * Loads the obstacle sprite sheet image from resources.
     * Only loads once; subsequent calls reuse the cached image.
     */
    private static void loadSpriteSheet() {
        if (!imageLoaded) {
            try {
                spriteSheet = ImageIO.read(Obstacle.class.getResourceAsStream("/obstacles.png"));
            } catch (IOException | IllegalArgumentException e) {
                spriteSheet = null;
            }
            imageLoaded = true;
        }
    }

    /**
     * Updates the obstacle's position and animation frame each game loop tick.
     */
    public void update() {


        y += speed;

        animCounter++;
        if (animCounter >= animDelay) {
            currentFrame = (currentFrame + 1) % totalFrames;
            animCounter = 0;
        }


    }

    /**
     * Draws the obstacle using the current animation frame from the sprite sheet.
     * Falls back to a drawn asteroid shape if the image is unavailable.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2) {
        if (spriteSheet != null) {
            BufferedImage frame = spriteSheet.getSubimage(
                    currentFrame * frameWidth, 0, frameWidth, frameHeight
            );
            g2.drawImage(frame, x, y, frameWidth, frameHeight, null);
        } else {
            // Fallback: draw a grey circle
            g2.setColor(new Color(160, 82, 45));
            g2.fillOval(x, y, frameWidth, frameHeight);
            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(x, y, frameWidth, frameHeight);
        }
    }

    /**
     * Returns the bounding rectangle for collision detection.
     *
     * @return Rectangle representing the obstacle's bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x + 4, y + 4, frameWidth - 8, frameHeight - 8);
    }

    /**
     * Returns whether the obstacle has moved off the bottom of the screen.
     *
     * @param screenHeight The height of the game screen.
     * @return True if the obstacle is below the visible screen area.
     */
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    /** @return Current X position. */
    public int getX() { return x; }

    /** @return Current Y position. */
    public int getY() { return y; }
}