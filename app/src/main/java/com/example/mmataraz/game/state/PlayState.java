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

import java.util.ArrayList;

/**
 * Created by Mike on 2/16/2015.
 */
public class PlayState extends State {

    // Game objects
    private ArrayList<Asteroid> asteroids;
    private Item dualLaser; //tbd
    private Player player, wingman;
    private ArrayList<Enemy> currentEnemyGroup;
    private ArrayList<ArrayList<Enemy>> enemyForces;

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
    //private static final int NUM_ENEMIES = 4;
    private int activeEnemyGroup = 0;
    private int deactiveCount = 0;

    // Touch location
    private float recentTouchY;
    private float recentTouchX;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    // Other current items
    private Animation playerCurrentAnim, wingmanCurrentAnim, enemyCurrentAnim;
    private Bitmap planetImage;
    private String musicString;
    private PlayStateLevel currentLevel;
    private int timer = 0;

    // Sound stream IDs
    private static int launchStreamID;
    private static int engineStreamID;

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
                musicString = "audio/earth-projsol-27.wav";
                break;

            case MARS:
                planetImage = Assets.mars;
                musicString = "audio/mars-projsol-08.wav";
                break;

            case SATURN:
                planetImage = Assets.saturn;
                musicString = "audio/saturn-projsol-03.wav";
                break;

            default:
                planetImage = Assets.earth;
                musicString = "audio/earth-projsol-27.wav";
                break;
        }

        // Load and play music
        Assets.resetMusic();
        Assets.loadMusic(musicString, true);
        Assets.resumeMusic();

        //launchStreamID = Assets.playSound(Assets.launchID, 0);
        engineStreamID = Assets.playSound(Assets.engineID, -1);

        // Init Asteroids
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < /*8*/4; i++) {
            Asteroid a = new Asteroid((float) i*250, (float) GameMainActivity.GAME_HEIGHT,
                    ASTEROID_WIDTH, ASTEROID_HEIGHT);
            asteroids.add(a);
        }

        // Init dual laser item
        dualLaser = new Item((float) GameMainActivity.GAME_WIDTH, (float) GameMainActivity.GAME_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT);

        // Init player
        player = new Player(256, 128, PLAYER_WIDTH, PLAYER_HEIGHT);
        wingman = new Player(192, 192, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Init enemies
        /*enemies = new ArrayList<Enemy>();
        for (int i=0; i<NUM_ENEMIES; i++) {
            // emerge from top left
            Enemy e = new Enemy(-128, -128, PLAYER_WIDTH, PLAYER_HEIGHT, 0);
            enemies.add(e);
        }*/

        currentEnemyGroup = new ArrayList<Enemy>();
        enemyForces = new ArrayList<ArrayList<Enemy>>();
        ArrayList<Enemy> fighterGroup1 = new ArrayList<Enemy>();
        Enemy fighter1 = new Enemy(-100, 0, PLAYER_WIDTH, PLAYER_HEIGHT, 0);
        Enemy fighter2 = new Enemy(0, -100, PLAYER_WIDTH, PLAYER_HEIGHT, 0);
        fighterGroup1.add(fighter1);
        fighterGroup1.add(fighter2);

        ArrayList<Enemy> fighterGroup2 = new ArrayList<Enemy>();
        Enemy fighter3 = new Enemy(800, 550, PLAYER_WIDTH, PLAYER_HEIGHT, 0);
        Enemy fighter4 = new Enemy(900, 450, PLAYER_WIDTH, PLAYER_HEIGHT, 0);
        fighterGroup2.add(fighter3);
        fighterGroup2.add(fighter4);

        ArrayList<Enemy> capitalGroup1 = new ArrayList<Enemy>();
        Enemy capital1 = new Enemy(400, 550, 96, 96, 1);
        Enemy capital2 = new Enemy(400, -100, 96, 96, 1);
        capitalGroup1.add(capital1);
        capitalGroup1.add(capital2);

        enemyForces.add(fighterGroup1);
        enemyForces.add(fighterGroup2);
        enemyForces.add(capitalGroup1);

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
        //planet = new Planet(32, GameMainActivity.GAME_HEIGHT/2 - planetImage.getHeight()/2);
        //planet = new Planet(-250, -250);
        planet = new Planet(0,0);
        playerCurrentAnim = Assets.playerLevelAnim;
        wingmanCurrentAnim = Assets.wingmanLevelAnim;
        enemyCurrentAnim = Assets.enemyLevelAnim;

        pauseButton = new UIButton(672, 32, 720, 80, Assets.pause, Assets.pauseDown);
        quitButton = pauseButton;
        pauseRect = new Rect(672, 32, 720, 80);
        quitRect = pauseRect;
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
            Assets.stopSound(engineStreamID);
            //Assets.stopSound(launchStreamID);
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

        // need this
        if (activeEnemyGroup < enemyForces.size())
            currentEnemyGroup = enemyForces.get(activeEnemyGroup);

        // Update game objects
        playerScore += player.update(delta, asteroids, /*enemies*/ currentEnemyGroup);
        playerCurrentAnim.update(delta);
        updateDualLaser(delta);
        updateAsteroids(delta);

        int test = wingman.update(delta, asteroids, /*enemies*/ currentEnemyGroup);
        wingmanCurrentAnim.update(delta);

        // Increase asteroid speed when score exceeds 30
        if (playerScore > 30 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
            Asteroid.setVelX(asteroidSpeed);
        }

        if (activeEnemyGroup < /*enemies.size()*/ enemyForces.size()) {
            //Enemy e = enemies.get(activeEnemyGroup);
            //ArrayList<Enemy> group = enemyForces.get(activeEnemyGroup);
            for (int i = 0; i < currentEnemyGroup.size(); i++) {
                Enemy e = currentEnemyGroup.get(i);
                e.update(delta, asteroids, player, wingman);

                // going to change
                if (!e.getActive() && e.isAlive())
                    e.setActive(true);
                //e.activateEnemy();

                if (!e.isAlive() && /*e.getActive()*/ !e.isOnScreen()) {
                    //e.deactivateEnemy();
                    e.setActive(false);
                    deactiveCount++;
                    //activeEnemyGroup++;
                }

                // update animation
                if (Math.abs(e.getVelY()) <= 70)
                    e.setAnim(0);
                    //enemyCurrentAnim = Assets.enemyLevelAnim;
                else if (e.getVelY() > 70 && e.getVelY() <= 145)
                    e.setAnim(-1);
                    //enemyCurrentAnim = Assets.enemyDownOneAnim;
                else if (e.getVelY() > 145)
                    e.setAnim(-2);
                    //enemyCurrentAnim = Assets.enemyDownTwoAnim;
                else if (e.getVelY() < -70 && e.getVelY() >= -145)
                    e.setAnim(1);
                    //enemyCurrentAnim = Assets.enemyUpOneAnim;
                else if (e.getVelY() < -145)
                    e.setAnim(2);
                    //enemyCurrentAnim = Assets.enemyUpTwoAnim;
            }

            if (deactiveCount == currentEnemyGroup.size())
                activeEnemyGroup++;

            deactiveCount = 0;  // needs to reset every frame
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
        renderWingman(g);

        /*for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.getActive() && e.isAlive())   // eventually, don't need e.isAlive()
                renderEnemy(g);
        }*/

        if (activeEnemyGroup < enemyForces.size()) {
            //ArrayList<Enemy> group = enemyForces.get(activeEnemyGroup);
            for (int i = 0; i < currentEnemyGroup.size(); i++) {
                Enemy e = currentEnemyGroup.get(i);
                if (e.getActive() && e.isAlive())   // eventually, don't need e.isAlive()
                    renderEnemy(g, e);
                if (e.isOnScreen() && e.isAlive())
                    renderEnemyShield(g, e);
            }
        }


        renderItems(g);
        renderAsteroids(g);
        renderScore(g);
        renderShield(g);
        //renderEnergy(g);

        /*for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isOnScreen() && e.isAlive())
                renderEnemyShield(g, e);
        }*/

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
        playerCurrentAnim.render(g, (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());
        player.renderWeapon(g);
    }

    private void renderWingman(Painter g) {
        wingmanCurrentAnim.render(g, (int) wingman.getX(), (int) wingman.getY(),
                wingman.getWidth(), wingman.getHeight());
        wingman.renderWeapon(g);
    }

    private void renderEnemy(Painter g, Enemy e) {
        // maybe some method that returns the correct animation from Assets
        Animation testCurrentAnim = Assets.getEnemyAnim(e.getType(), e.getAnim());

        /*enemyCurrentAnim.render(g, (int) enemies.get(activeEnemyGroup).getX(), (int) enemies.get(activeEnemyGroup).getY(),
                enemies.get(activeEnemyGroup).getWidth(), enemies.get(activeEnemyGroup).getHeight());
        enemies.get(activeEnemyGroup).renderWeapon(g);*/

        testCurrentAnim.render(g, (int) e.getX(), (int) e.getY(), e.getWidth(), e.getHeight());
        e.renderWeapon(g);
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

        g.setColor(Color.CYAN);
        g.drawString("WINGMAN: ", 170, 55);
        g.fillRect(280, 40, wingman.getShield(), 15);
        g.setColor(Color.WHITE);
        g.fillRect(280, 40, 100, 1);
        g.fillRect(280, 55, 100, 1);
        g.fillRect(280, 40, 1, 15);
        g.fillRect(380, 40, 1, 15);
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

        //g.drawString("ENEMY: ", 600, 30);
        //g.fillRect(680, 15, e.getShield(), 15);
        g.fillRect((int) e.getX(), (int) e.getY()+e.getHeight()+10, e.getShield()/(e.getMaxShield()/50), 10);

        g.setColor(Color.WHITE);
        /*g.fillRect(680, 15, 100, 1);
        g.fillRect(680, 30, 100, 1);
        g.fillRect(680, 15, 1, 15);
        g.fillRect(780, 15, 1, 15);*/

        g.fillRect((int) e.getX(), (int) e.getY()+e.getHeight()+10, 50, 1);
        g.fillRect((int) e.getX(), (int) e.getY()+e.getHeight()+20, 50, 1);
        g.fillRect((int) e.getX(), (int) e.getY()+e.getHeight()+10, 1, 10);
        g.fillRect((int) e.getX()+50, (int) e.getY()+e.getHeight()+10, 1, 10);
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

            // To add some simple acceleration physics, use delta values
            // between touches to change velocity, not just position..
            int deltaY = scaledY - (int) recentTouchY;
            int deltaX = scaledX - (int) recentTouchX;

            player.maneuver(deltaY, deltaX);
            wingman.maneuver(deltaY, deltaX);   // let's see
            //Log.d("dY", Integer.toString(deltaY));
            //Log.d("dX", Integer.toString(deltaX));

            // Change sprite (animation) based on Y velocity to give the appearance of movement
            if (Math.abs(player.getVelY()) <= /*8*/ 70)
                playerCurrentAnim = Assets.playerLevelAnim;
            else if (player.getVelY() > /*8*/ 70 && player.getVelY() <= /*32*/ 145)
                playerCurrentAnim = Assets.playerDownOneAnim;
            else if (player.getVelY() > /*32*/ 145)
                playerCurrentAnim = Assets.playerDownTwoAnim;
            else if (player.getVelY() < /*-8*/ -70 && player.getVelY() >= /*-32*/ -145)
                playerCurrentAnim = Assets.playerUpOneAnim;
            else if (player.getVelY() < /*-32*/ -145)
                playerCurrentAnim = Assets.playerUpTwoAnim;

            if (Math.abs(wingman.getVelY()) <= 70)
                wingmanCurrentAnim = Assets.wingmanLevelAnim;
            else if (wingman.getVelY() > 70 && wingman.getVelY() <= 145)
                wingmanCurrentAnim = Assets.wingmanDownOneAnim;
            else if (wingman.getVelY() > 145)
                wingmanCurrentAnim = Assets.wingmanDownTwoAnim;
            else if (wingman.getVelY() < -70 && wingman.getVelY() >= -145)
                wingmanCurrentAnim = Assets.wingmanUpOneAnim;
            else if (wingman.getVelY() < -145)
                wingmanCurrentAnim = Assets.wingmanUpTwoAnim;

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
                //Assets.resumeSound(launchStreamID);
                Assets.resumeSound(engineStreamID);

                return true;
            }

            // If Touch Up triggers PauseButton, pause the game.
            if (pauseButton.isPressed(scaledX, scaledY)) {
                pauseButton.cancel();
                Assets.pauseSound(engineStreamID);
                //Assets.pauseSound(launchStreamID);
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

            // Player is not firing
            player.setFiringStatus(false);
        }

        return true;
    }

    // Overrides onPause() from State.
    // Called when Activity is pausing.
    @Override
    public void onPause() {
        Assets.stopSound(engineStreamID);
        //Assets.stopSound(launchStreamID);
        Assets.stopMusic();
        gamePaused = true;
    }

    @Override
    public void onResume() {
        Assets.loadMusic(musicString, true);
        Assets.playSound(Assets.engineID, -1);
    }

}