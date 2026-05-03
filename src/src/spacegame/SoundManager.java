/** Project: Solo Lab 7 Assignment
 * Purpose Details: Manages loading and playback of WAV audio files for
 *                  game sound effects and looping background music.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Handles loading and playing WAV audio sound effects and background music.
 * Background music loops continuously. Sound effects play on separate threads.
 */
public class SoundManager {

    /** The clip used for looping background music. */
    private static Clip backgroundMusicClip;

    /**
     * Attempts to get an AudioInputStream from resources or file system.
     *
     * @param resourceName The WAV file name.
     * @return AudioInputStream if found, null otherwise.
     */
    private static AudioInputStream getStream(String resourceName) {
        try {
            AudioInputStream rawStream = null;

            // Try 1: classpath
            URL url = SoundManager.class.getResource("/" + resourceName);
            if (url != null) {
                rawStream = AudioSystem.getAudioInputStream(url);
            }

            // Try 2: src/resources/
            if (rawStream == null) {
                File f1 = new File("src/resources/" + resourceName);
                if (f1.exists()) rawStream = AudioSystem.getAudioInputStream(f1);
            }

            // Try 3: src/src/resources/
            if (rawStream == null) {
                File f2 = new File("src/src/resources/" + resourceName);
                if (f2.exists()) rawStream = AudioSystem.getAudioInputStream(f2);
            }

            if (rawStream == null) {
                System.out.println("Sound file not found: " + resourceName);
                return null;
            }

            // Convert to standard PCM format Java can always play
            AudioFormat baseFormat = rawStream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            return AudioSystem.getAudioInputStream(decodedFormat, rawStream);

        } catch (Exception e) {
            System.out.println("Error loading: " + resourceName + " - " + e.getMessage());
        }
        return null;
    }
    /**
     * Starts playing the given WAV file as looping background music.
     * If music is already playing, stops it first.
     *
     * @param resourceName The WAV file name in the resources folder.
     */
    public static void startBackgroundMusic(String resourceName) {
        stopBackgroundMusic();
        new Thread(() -> {
            try {
                AudioInputStream audioIn = getStream(resourceName);
                if (audioIn == null) return;

                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioIn);

                if (backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volume = (FloatControl)
                            backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                    float min = volume.getMinimum();
                    float max = volume.getMaximum();
                    volume.setValue(min + (max - min) * 0.55f);
                }

                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusicClip.start();

            } catch (Exception e) {
                System.out.println("Could not start background music.");
            }
        }).start();
    }

    /**
     * Stops and closes the currently playing background music clip, if any.
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

    /**
     * Plays a WAV audio file once as a sound effect on a separate thread.
     *
     * @param resourceName The WAV file name in the resources folder.
     */
    public static void play(String resourceName) {
        new Thread(() -> {
            try {
                AudioInputStream audioIn = getStream(resourceName);
                if (audioIn == null) return;

                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.close();

            } catch (Exception e) {
                System.out.println("Could not play: " + resourceName);
            }
        }).start();
    }

    /**
     * Plays the fire sound effect once.
     */
    public static void playFire() {
        play("fire.wav");
    }

    /**
     * Plays the collision sound effect once.
     */
    public static void playCollision() {
        play("collision.wav");
    }

    /**
     * Plays the shield activation sound effect once.
     */
    public static void playShield() {
        play("deflector-shield.wav");
    }
}