package com.example.mmataraz.game.state;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.game.model.Asteroid;
import com.example.mmataraz.game.model.Enemy;
import com.example.mmataraz.game.model.Item;
import com.example.mmataraz.game.model.Planet;
import com.example.mmataraz.game.model.Star;
import com.example.mmataraz.game.model.Player;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mike on 2/16/2015.
 */
public class PlayState extends State {

    // Game objects
    private ArrayList<Asteroid> asteroids;
    private Item dualLaser; //tbd
    private Player player;
    private ArrayList<Enemy> enemies;

    // Background objects
    private ArrayList<Star> spaceDust;
    private ArrayList<Star> stars;
    private Planet planet;

    // Asteroid properties
    private static final int ASTEROID_WIDTH = 50;
    private static final int ASTEROID_HEIGHT = 50;
    private int asteroidSpeed = -200;

    // Item properties
    private static final int ITEM_WIDTH = 240;
    private static final int ITEM_HEIGHT = 240;

    // Player properties
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 32;
    private int playerScore = 0;

    // Enemy properties
    private int activeEnemy = 0;

    // Touch location
    private float recentTouchY;
    private float recentTouchX;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    // Other current items
    private Animation currentAnim;
    private Animation enemyAnim;
    private Bitmap planetImage;
    private String musicString;
    private PlayStateLevel currentLevel;
    private int timer = 0;

    // Change to UI buttons
    private UIButton pauseButton, quitButton;
    private Rect pauseRect, quitRect;

    // PlayState Constructor
    public PlayState(PlayStateLevel level) {
        currentLevel = level;
    }

    @Override
    public void init() {
        // Determine level background image and music
        switch (currentLevel) {
            case EARTH:
                planetImage = Assets.earth;
                musicString = "earth-projsol-23.wav";
                break;

            case MARS:
                planetImage = Assets.mars;
                musicString = "mars-projsol-07.wav";
                break;

            case SATURN:
                planetImage = Assets.saturn;
                musicString = "saturn-projsol-02.wav";
                break;

            default:
                planetImage = Assets.earth;
                musicString = "earth-projsol-23.wav";
                break;
        }

        // Init Asteroids
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < /*8*/4; i++) {
            Asteroid a = new Asteroid((float) i*/*125*/250, (float) GameMainActivity.GAME_HEIGHT,
                    ASTEROID_WIDTH, ASTEROID_HEIGHT);
            asteroids.add(a);
        }

        // Init dual laser item
        dualLaser = new Item((float) GameMainActivity.GAME_WIDTH, (float) GameMainActivity.GAME_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT);

        // Init player
        player = new Player(256, 128, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Init enemies
        enemies = new ArrayList<Enemy>();
        for (int i=0; i<1; i++) {
            Enemy e = new Enemy(GameMainActivity.GAME_WIDTH+128, -128, PLAYER_WIDTH, PLAYER_HEIGHT);
            enemies.add(e);
        }

        // Init spacedust
        spaceDust = new ArrayList<Star>();
        for (int i = 0; i < 32; i++) {
            Star s = new Star(2);
            spaceDust.add(s);
        }

        // Init stars
        stars = new ArrayList<Star>();
        for (int i = 0; i < 64; i++) {
            Star c = new Star(1);
            stars.add(c);
        }

        // Init planet and animation
        planet = new Planet(32, GameMainActivity.GAME_HEIGHT/2 - planetImage.getHeight()/2);
        currentAnim = Assets.levelAnim;
        enemyAnim = Assets.enemyAnim;

        pauseButton = new UIButton(672, 32, 720, 80, Assets.pause, Assets.pauseDown);
        quitButton = pauseButton;
        pauseRect = new Rect(672, 32, 720, 80);
        quitRect = pauseRect;

        // Load and play music
        Assets.resetMusic();
        Assets.loadMusic(musicString, true);
        Assets.resumeMusic();
    }

    @Override
    public void onLoad() {
        Assets.loadPlayAssets();
    }

    @Override
    public void onExit() {

    }

    @Override
    public void update(float delta) {
        // Check this first b/c quit button just sets isAlive to false
        if (!player.isAlive()) {
            Assets.stopMusic();
            setCurrentState(new LoadState(this, new GameOverState(playerScore)));
        }

        // If game is paused, do not update anything.
        if (gamePaused) {
            return;
        }

        // Update stars
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            c.update(delta);
        }

        // Update planet
        if (planet.getVisible()) {
            planet.update(delta);
        }

        // Update spacedust
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            s.update(delta);
        }

        // Update game objects
        playerScore += player.update(delta, asteroids, enemies);
        currentAnim.update(delta);
        updateDualLaser(delta);
        updateAsteroids(delta);

        // Increase asteroid speed when score exceeds 30
        if (playerScore > 30 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
            Asteroid.setVelX(asteroidSpeed);
        }

        for (int i=0; i<enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update(delta, asteroids);

            // going to change
            if (i == activeEnemy && !e.getActive())
                e.activateEnemy();
        }

        // Update timer
        if (++timer > 60000)
            timer = 0;
    }

    private void updateDualLaser(float delta) {
        dualLaser.checkItemAppear(player.getDual(), timer);
        dualLaser.update(delta);

        if (dualLaser.isVisible())
            if (Rect.intersects(dualLaser.getRect(), player.getRect()))
                dualLaser.onCollide(player);
    }

    private void updateAsteroids(float delta) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            a.update(delta, asteroidSpeed);

            if (a.isVisible()) {
                // Box collision detection
                if (Rect.intersects(a.getRect(), player.getRect()))
                    a.onCollide(player);
            }
        }
    }

    @Override
    public void render(Painter g) {
        g.setColor(Color.rgb(4, 4, 16));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        // render order
        renderStars(g);
        renderPlanet(g);
        renderSpaceDust(g);
        renderPlayer(g);

        for (int i=0; i<enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.getActive() && e.isAlive())
                renderEnemy(g);
        }

        renderItems(g);
        renderAsteroids(g);
        renderScore(g);
        renderShield(g);
        //renderEnergy(g);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isOnScreen())
                renderEnemyShield(g, e);
        }

        // If game is Paused, draw additional UI elements:
        if (gamePaused) {
            // ARGB is used to set an ARGB color.
            g.setColor(Color.argb(160, 0, 0, 0));
            g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
            g.setColor(Color.LTGRAY);
            g.drawString(pausedString, 235, 240);

            quitButton.render(g);
        } else {
            // Draw pauseButton if not paused.
            pauseButton.render(g);
        }
    }

    private void renderStars(Painter g) {
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            g.setColor(c.getColor());
            g.fillOval((int) c.getX(), (int) c.getY(), (int) c.getSize(), (int) c.getSize());
        }
    }

    private void renderPlanet(Painter g) {
        if (planet.getVisible()) {
            g.drawImage(planetImage, (int) planet.getX(), (int) planet.getY(),
                    planetImage.getWidth(), planetImage.getHeight());
        }
    }

    private void renderSpaceDust(Painter g) {
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            g.setColor(s.getColor());
            g.fillOval((int) s.getX(), (int) s.getY(), (int) s.getSize(), (int) s.getSize());
        }
    }

    private void renderPlayer(Painter g) {
        currentAnim.render(g, (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());
        player.renderWeapon(g);
    }

    private void renderEnemy(Painter g) {
        enemyAnim.render(g, (int) enemies.get(0).getX(), (int) enemies.get(0).getY(),
                enemies.get(0).getWidth(), enemies.get(0).getHeight());
        enemies.get(0).renderWeapon(g);
    }

    private void renderItems(Painter g) {
        if (dualLaser.isVisible())
            g.drawImage(Assets.laserItem, (int) dualLaser.getX(), (int) dualLaser.getY(),
                    Assets.laserItem.getWidth(), Assets.laserItem.getHeight());
    }

    private void renderAsteroids(Painter g) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            if (a.isVisible()) {
                g.drawImage(Assets.asteroid, (int) a.getX(), (int) a.getY(),
                        ASTEROID_WIDTH, ASTEROID_HEIGHT);
            }
        }
    }

    private void renderScore(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(Color.WHITE);

        String scoreString = Integer.toString(playerScore);
        g.drawString("SCORE: ", 30, 30);
        g.drawString(scoreString, 100, 30);
    }

    private void renderShield(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(/*Color.CYAN*/ Color.BLUE);

        g.drawString("SHIELD: ", 200, 30);
        g.fillRect(280, 15, player.getShield(), 15);

        g.setColor(Color.WHITE);
        g.fillRect(280, 15, 100, 1);
        g.fillRect(280, 30, 100, 1);
        g.fillRect(280, 15, 1, 15);
        g.fillRect(380, 15, 1, 15);
    }

    private void renderEnergy(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(Color.GREEN);

        g.drawString("ENERGY: ", 430, 30);
        if (player.getEnergy() > 0)
            g.fillRect(510, 15, player.getEnergy()/8, 15);

        g.setColor(Color.WHITE);
        g.fillRect(510, 15, 100, 1);
        g.fillRect(510, 30, 100, 1);
        g.fillRect(510, 15, 1, 15);
        g.fillRect(610, 15, 1, 15);
    }

    private void renderEnemyShield(Painter g, Enemy e) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(/*Color.CYAN*/ Color.RED);

        g.drawString("ENEMY: ", 600, 30);
        g.fillRect(680, 15, e.getShield(), 15);

        g.setColor(Color.WHITE);
        g.fillRect(680, 15, 100, 1);
        g.fillRect(680, 30, 100, 1);
        g.fillRect(680, 15, 1, 15);
        g.fillRect(780, 15, 1, 15);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Call onTouchDown for quit button
            if (gamePaused) {
                quitButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            // Call onTouchDown for pause button, and return if pressed
            pauseButton.onTouchDown(scaledX, scaledY);
            if (pauseButton.isPressed(scaledX, scaledY)) {
                return true;
            }

            /*// Return if the pause button is hit
            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                return true;
            }*/

            // Store touch location
            recentTouchY = scaledY;
            recentTouchX = scaledX; // new

            // Player is firing
            player.setFiringStatus(true);

        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // Return if paused
            if (gamePaused) {
                quitButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            // If not paused, only return if the pause button was pressed
            // This means that user pressed the pause button but moved off it
            //pauseButton.onTouchDown(scaledX, scaledY);
            if (pauseButton.wasHit()) {
                pauseButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            /*// Action should stop if user slides over pause button - CHANGE
            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                //gamePaused = true;
                player.setFiringStatus(false);
                return true;
            } else {
                player.setFiringStatus(true);
            }*/

            // To add some simple acceleration physics, use delta values
            // between touches to change velocity, not just position..
            int deltaY = scaledY - (int) recentTouchY;
            int deltaX = scaledX - (int) recentTouchX;
            player.maneuver(deltaY, deltaX);

            // Change sprite (animation) based on Y velocity to give the appearance of movement
            if (Math.abs(player.getVelY()) <= /*8*/ 70)
                currentAnim = Assets.levelAnim;
            else if (player.getVelY() > /*8*/ 70 && player.getVelY() <= /*32*/ 145)
                currentAnim = Assets.downOneAnim;
            else if (player.getVelY() > /*32*/ 145)
                currentAnim = Assets.downTwoAnim;
            else if (player.getVelY() < /*-8*/ -70 && player.getVelY() >= /*-32*/ -145)
                currentAnim = Assets.upOneAnim;
            else if (player.getVelY() < /*-32*/ -145)
                currentAnim = Assets.upTwoAnim;

            recentTouchY = scaledY;
            recentTouchX = scaledX;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            // Resume game if paused.
            if (gamePaused) {
                // If touch up triggers quit button, quit game
                if (quitButton.isPressed(scaledX, scaledY)) {
                    quitButton.cancel();
                /*}

                // Quit game if quit rect is hit
                if (quitRect.intersects(scaledX, scaledY, scaledX, scaledY)) {*/
                    player.setAlive(false);
                    return true;
                } else if (quitButton.wasHit()) {
                    // If quit button was pressed but user moved off it, return and don't unpause game
                    quitButton.cancel();
                    return true;
                } else {
                    // Quit button was never pressed
                    quitButton.cancel();
                }

                // Unpause game
                gamePaused = false;
                // Must cancel pauseButton before onTouch() method returns.
                pauseButton.cancel();
                Assets.resumeMusic();

                return true;
            }

            // If Touch Up triggers PauseButton, pause the game.
            if (pauseButton.isPressed(scaledX, scaledY)) {
                pauseButton.cancel();
                Assets.pauseMusic();
                gamePaused = true;
                return true;
            } else if (pauseButton.wasHit()) {
                // If pause button was pressed but user moved off it, return and don't pause game
                pauseButton.cancel();
                return true;
            } else {
                // Pause button was never hit
                pauseButton.cancel();
            }

            /*// Pause game if pause button is hit
            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                Assets.pauseMusic();
                gamePaused = true;

                return true;
            }*/

            // Player is not firing
            player.setFiringStatus(false);
        }

        return true;
    }

    // Overrides onPause() from State.
    // Called when Activity is pausing.
    @Override
    public void onPause() {
        Assets.stopMusic();
        gamePaused = true;
    }

    @Override
    public void onResume() {
        Assets.loadMusic(musicString, true);
    }

}