/** Project: Solo Lab 7 Assignment
 * Purpose Details: Manages loading and playback of WAV audio files for game sound effects.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Handles loading and playing WAV audio sound effects for the Space Game.
 * Supports fire and collision sounds.
 */
public class SoundManager {

    /**
     * Plays a WAV audio file from the resources folder by filename.
     * The sound plays on a separate thread to avoid blocking the game loop.
     *
     * @param resourceName The name of the WAV file in the resources folder (e.g., "fire.wav").
     */
    public static void play(String resourceName) {
        new Thread(() -> {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        SoundManager.class.getResourceAsStream("/" + resourceName)
                );
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();

                // Wait for clip to finish then close
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.close();
            } catch (UnsupportedAudioFileException | IOException |
                     LineUnavailableException | InterruptedException e) {
                // Sound not available — silently ignore to keep game running
            }
        }).start();
    }

    /**
     * Plays the fire sound effect.
     */
    public static void playFire() {
        play("fire.wav");
    }

    /**
     * Plays the collision sound effect.
     */
    public static void playCollision() {
        play("collision.wav");
    }
}