/** Project: Solo Lab 7 Assignment
 * Purpose Details: Main game panel containing the game loop, rendering, input handling,
 *                  scoring, timer, levels, and all game object management.
 * Course: IST 242
 * Author: David Adeleye
 * Date Developed: 2025-04-29
 * Last Date Changed: 2025-04-29
 * Rev: 1.0
 */
package spacegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * The main game panel that contains the game loop and renders all game elements.
 * Manages stars, player, obstacles, bullets, power-ups, score, timer, and game state.
 */
public class GamePanel extends JPanel implements KeyListener {

    /** Width of the game screen in pixels. */
    private final int screenWidth = 800;

    /** Height of the game screen in pixels. */
    private final int screenHeight = 600;

    /** Target frames per second for the game loop. */
    private final int FPS = 60;

    /** The player-controlled spaceship. */
    private Player player;

    /** List of active obstacles on screen. */
    private ArrayList<Obstacle> obstacles;

    /** List of active health power-ups on screen. */
    private ArrayList<PowerUp> powerUps;

    /** List of background stars. */
    private ArrayList<Star> stars;

    /** List of active bullets fired by the player. */
    private ArrayList<Bullet> bullets;

    /** Current player score. */
    private int score;

    /** Current game level (1 or 2). */
    private int level;

    /** Countdown timer in seconds. */
    private int timeRemaining;

    /** Tracks elapsed time in game loop ticks for the timer. */
    private int timerTicks;

    /** How many ticks equal one second (based on FPS). */
    private final int ticksPerSecond = FPS;

    /** Whether the game is currently running (not game over). */
    private boolean gameRunning;

    /** Whether the game is paused. */
    private boolean gamePaused;

    /** Counter used to throttle obstacle spawning. */
    private int obstacleSpawnCounter;

    /** Counter used to throttle power-up spawning. */
    private int powerUpSpawnCounter;

    /** Tracks time between shots to prevent spamming. */
    private int shootCooldown;

    /** Number of frames required between each shot. */
    private final int shootCooldownMax = 15;

    /** Random number generator for spawning. */
    private final Random random = new Random();

    /** The Swing Timer that drives the game loop. */
    private Timer gameTimer;

    /**
     * Constructs the GamePanel, sets preferred size, background color,
     * key listener, and mouse listener.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Mouse click to fire
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameRunning && !gamePaused) {
                    fireBullet();
                }
            }
        });

        initGame();
    }

    /**
     * Initializes or resets all game state variables, objects, and collections.
     */
    private void initGame() {
        player = new Player(screenWidth / 2 - 32, screenHeight - 120);
        obstacles = new ArrayList<>();
        powerUps = new ArrayList<>();
        stars = new ArrayList<>();
        bullets = new ArrayList<>();
        score = 0;
        level = 1;
        timeRemaining = 60;
        timerTicks = 0;
        gameRunning = true;
        gamePaused = false;
        obstacleSpawnCounter = 0;
        powerUpSpawnCounter = 0;
        shootCooldown = 0;

        for (int i = 0; i < 120; i++) {
            stars.add(new Star(screenWidth, screenHeight));
        }

        SoundManager.startBackgroundMusic("shoot_00.wav");
    }

    /**
     * Starts the main game loop using a Swing Timer at the target FPS.
     */
    public void startGameLoop() {
        gameTimer = new Timer(1000 / FPS, e -> {
            if (gameRunning && !gamePaused) {
                update();
            }
            repaint();
        });
        gameTimer.start();
    }

    /**
     * Fires a bullet from the center top of the player ship.
     * Enforces a cooldown between shots and plays the fire sound.
     */
    private void fireBullet() {
        if (shootCooldown <= 0) {
            int bulletX = player.getX() + player.getWidth() / 2 - 2;
            int bulletY = player.getY();
            bullets.add(new Bullet(bulletX, bulletY));
            SoundManager.playFire();
            shootCooldown = shootCooldownMax;
        }
    }

    /**
     * Updates all game objects and game state each frame.
     * Handles spawning, movement, collisions, timer, and level progression.
     */
    private void update() {
        // Update player
        player.update(screenWidth, screenHeight);

        // Update shoot cooldown
        if (shootCooldown > 0) shootCooldown--;

        // Update stars
        for (Star star : stars) {
            star.update();
        }

        // Countdown timer
        timerTicks++;
        if (timerTicks >= ticksPerSecond) {
            timerTicks = 0;
            timeRemaining--;
            if (timeRemaining <= 0) {
                timeRemaining = 0;
                gameRunning = false;
            }
        }

        // Level progression
        if (score >= 20 && level == 1) {
            level = 2;
        }

        // Spawn rates based on level
        int spawnRate = (level == 1) ? 60 : 35;
        int baseSpeed = (level == 1) ? 3 : 5;

        // Spawn obstacles
        obstacleSpawnCounter++;
        if (obstacleSpawnCounter >= spawnRate) {
            obstacleSpawnCounter = 0;
            int ox = random.nextInt(screenWidth - 64);
            int speed = baseSpeed + random.nextInt(2);
            obstacles.add(new Obstacle(ox, -64, speed));
        }

        // Spawn power-ups
        powerUpSpawnCounter++;
        if (powerUpSpawnCounter >= 300) {
            powerUpSpawnCounter = 0;
            int px = random.nextInt(screenWidth - 24);
            powerUps.add(new PowerUp(px, -24));
        }

        // Update bullets and check collisions with obstacles
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.update();

            if (bullet.isOffScreen()) {
                bulletIter.remove();
                continue;
            }

            // Check bullet vs obstacle collision
            boolean bulletHit = false;
            Iterator<Obstacle> obsHitIter = obstacles.iterator();
            while (obsHitIter.hasNext()) {
                Obstacle obs = obsHitIter.next();
                if (bullet.getBounds().intersects(obs.getBounds())) {
                    bulletIter.remove();
                    obsHitIter.remove();
                    score += 2;
                    SoundManager.playCollision();
                    bulletHit = true;
                    break;
                }
            }
            if (bulletHit) continue;
        }

        // Update obstacles and check player collision
        Iterator<Obstacle> obsIter = obstacles.iterator();
        while (obsIter.hasNext()) {
            Obstacle obs = obsIter.next();
            obs.update();

            if (obs.isOffScreen(screenHeight)) {
                obsIter.remove();
                score++;
            } else if (obs.getBounds().intersects(player.getBounds())) {
                obsIter.remove();
                if (!player.isShieldActive()) {
                    player.takeDamage(20);
                    SoundManager.playCollision();
                }
                if (!player.isAlive()) {
                    gameRunning = false;
                }
            }
        }

        // Update power-ups and check player collision
        Iterator<PowerUp> puIter = powerUps.iterator();
        while (puIter.hasNext()) {
            PowerUp pu = puIter.next();
            pu.update();

            if (pu.isOffScreen(screenHeight)) {
                puIter.remove();
            } else if (pu.getBounds().intersects(player.getBounds())) {
                player.heal(pu.getHealAmount());
                puIter.remove();
            }
        }
    }

    /**
     * Paints all game elements to the screen each frame.
     *
     * @param g The Graphics context provided by Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw stars
        for (Star star : stars) {
            star.draw(g2);
        }

        if (gameRunning || gamePaused) {
            // Draw power-ups
            for (PowerUp pu : powerUps) pu.draw(g2);

            // Draw obstacles
            for (Obstacle obs : obstacles) obs.draw(g2);

            // Draw bullets
            for (Bullet bullet : bullets) bullet.draw(g2);

            // Draw player
            player.draw(g2);

            // Draw HUD
            drawHUD(g2);

        } else {
            drawGameOver(g2);
        }

        if (gamePaused) drawPauseOverlay(g2);
    }

    /**
     * Draws the HUD showing score in blue, health, timer, level, and shield status.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawHUD(Graphics2D g2) {
        // Score in blue
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.BLUE);
        g2.drawString("Score: " + score, 10, 30);

        // Health
        g2.setColor(Color.WHITE);
        g2.drawString("Health: " + player.getHealth() + "/" + player.getMaxHealth(), 10, 55);

        // Timer
        g2.setColor(timeRemaining <= 10 ? Color.RED : Color.WHITE);
        g2.drawString("Time: " + timeRemaining + "s", 10, 80);

        // Level
        g2.setColor(Color.YELLOW);
        g2.drawString("Level: " + level, 10, 105);

        // Shield indicator
        if (player.isShieldActive()) {
            g2.setColor(new Color(0, 200, 255));
            g2.drawString("[ SHIELD ON ]", screenWidth / 2 - 55, 30);
        }

        // Level 2 warning
        if (level == 2) {
            g2.setColor(new Color(255, 80, 80));
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("LEVEL 2 - DANGER ZONE!", screenWidth - 220, 30);
        }

        // Controls
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("WASD/Arrows=Move  SPACE=Shield  Click=Fire  P=Pause  R=Restart",
                10, screenHeight - 10);
    }

    /**
     * Draws the game over screen with final score and restart instructions.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.RED);
        g2.drawString("GAME OVER", screenWidth / 2 - 150, screenHeight / 2 - 40);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Final Score: " + score, screenWidth / 2 - 80, screenHeight / 2 + 10);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Press R to Restart", screenWidth / 2 - 80, screenHeight / 2 + 50);
    }

    /**
     * Draws a semi-transparent pause overlay with resume instructions.
     *
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawPauseOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(Color.YELLOW);
        g2.drawString("PAUSED", screenWidth / 2 - 80, screenHeight / 2);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        g2.drawString("Press P to Resume", screenWidth / 2 - 80, screenHeight / 2 + 40);
    }

    /**
     * Handles key press events for movement, shield, pause, and restart.
     *
     * @param e The KeyEvent triggered by a key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP:    player.setMovingUp(true);    break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  player.setMovingDown(true);  break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  player.setMovingLeft(true);  break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: player.setMovingRight(true); break;
            case KeyEvent.VK_SPACE:
                if (!player.isShieldActive()) {
                    SoundManager.playShield();
                }
                player.setShieldActive(true);
                break;
            case KeyEvent.VK_P:
                if (gameRunning) gamePaused = !gamePaused;
                break;
            case KeyEvent.VK_R:
                if (!gameRunning) initGame();
                break;
        }
    }

    /**
     * Handles key release events to stop movement and deactivate shield.
     *
     * @param e The KeyEvent triggered by a key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP:    player.setMovingUp(false);    break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  player.setMovingDown(false);  break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  player.setMovingLeft(false);  break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: player.setMovingRight(false); break;
            case KeyEvent.VK_SPACE:                      player.setShieldActive(false); break;
        }
    }

    /**
     * Not used but required by the KeyListener interface.
     *
     * @param e The KeyEvent (unused).
     */
    @Override
    public void keyTyped(KeyEvent e) {}
}