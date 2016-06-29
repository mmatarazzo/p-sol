package com.example.mmataraz.game.state;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.game.model.Asteroid;
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

    private ArrayList<Asteroid> asteroids;
    private Item dualLaser; //tbd
    private Planet planet;
    private Player player;
    private ArrayList<Star> spaceDust;
    private ArrayList<Star> stars;
    private Animation currentAnim;

    // asteroids
    private static final int ASTEROID_WIDTH = 50;
    private static final int ASTEROID_HEIGHT = 50;
    private int asteroidSpeed = -200;

    // item - tbd
    private static final int ITEM_WIDTH = 240;
    private static final int ITEM_HEIGHT = 240;

    // player
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 32;
    private int playerScore = 0;

    // input
    private float recentTouchY;
    private float recentTouchX;

    // other
    private int timer = 0;  // tbd
    private Bitmap planetImage;
    private String musicString;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    private Rect pauseRect = new Rect(32, 32, 128, 128);
    private Rect quitRect = new Rect(32, GameMainActivity.GAME_HEIGHT-128, 128, GameMainActivity.GAME_HEIGHT-32);

    private PlayStateLevel currentLevel;

    // constructor
    public PlayState(PlayStateLevel level) {
        currentLevel = level;
    }

    @Override
    public void init() {

        // set up level image and music
        switch (currentLevel) {
            case EARTH:
                planetImage = Assets.earth;
                musicString = "earth-projsol-21.wav";
                break;

            case MARS:
                planetImage = Assets.mars;
                musicString = "mars-projsol-05.wav";
                break;

            default:
                planetImage = Assets.earth;
                musicString = "earth-projsol-21.wav";
                break;
        }

        // asteroids
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < 8; i++) {
            Asteroid a = new Asteroid((float) i*125, (float) GameMainActivity.GAME_HEIGHT,
                    ASTEROID_WIDTH, ASTEROID_HEIGHT);
            asteroids.add(a);
        }

        // dual laser item
        dualLaser = new Item((float) GameMainActivity.GAME_WIDTH, (float) GameMainActivity.GAME_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT);

        // planet and image
        //planetImage = getPlanetImage();
        planet = new Planet(32, GameMainActivity.GAME_HEIGHT/2 - planetImage.getHeight()/2);

        // player and animation
        currentAnim = Assets.levelAnim;
        player = new Player(256, 128, PLAYER_WIDTH, PLAYER_HEIGHT);

        // spacedust
        spaceDust = new ArrayList<Star>();
        for (int i = 0; i < 32; i++) {
            Star s = new Star(2);
            spaceDust.add(s);
        }

        // stars
        stars = new ArrayList<Star>();
        for (int i = 0; i < 64; i++) {
            Star c = new Star(1);
            stars.add(c);
        }

        // reset music
        Assets.resetMusic();
        //musicString = selectMusic();
        Assets.playMusic(musicString, true);
    }

    @Override
    public void onLoad() {
        Assets.loadPlayAssets();
    }

    @Override
    public void onExit() {
        Assets.unloadPlayAssets();
    }

    private Bitmap getPlanetImage() {
        switch (currentLevel) {
            case EARTH:
                return Assets.earth;

            case MARS:
                return Assets.mars;

            default:
                return Assets.earth;
        }
    }

    private String selectMusic() {
        switch (currentLevel) {
            case EARTH:
                return "earth-projsol-21.wav";

            case MARS:
                return "mars-projsol-05.wav";

            default:
                return "";
        }
    }

    @Override
    public void update(float delta) {
        if (!player.isAlive()) {
            Assets.stopMusic();
            setCurrentState(new GameOverState(playerScore));
        }

        // If game is paused, do not update anything.
        if (gamePaused) {
            return;
        }

        // update spacedust
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            s.update(delta);
        }

        // update planet next
        if (planet.getVisible()) {
            planet.update(delta);
        }

        // update stars last
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            c.update(delta);
        }

        currentAnim.update(delta);
        playerScore += player.update(delta, asteroids);

        //asteroidSpeed = Asteroid.getVelX();
        if (playerScore > 30 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
            Asteroid.setVelX(asteroidSpeed);
        }

        updateDualLaser(delta);
        updateAsteroids(delta);

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
                // box collision detection
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
        renderAsteroids(g);
        renderItems(g);
        renderScore(g);
        renderShield(g);
        //renderEnergy(g);

        // If game is Paused, draw additional UI elements:
        if (gamePaused) {
            // ARGB is used to set an ARGB color.
            g.setColor(Color.argb(160, 0, 0, 0));
            g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
            g.setColor(Color.LTGRAY);
            g.drawString(pausedString, 235, 240);
        }
    }

    // render stars
    private void renderStars(Painter g) {
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            g.setColor(c.getColor());
            g.fillOval((int) c.getX(), (int) c.getY(), (int) c.getSize(), (int) c.getSize());
        }
    }

    // render earth
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

    private void renderAsteroids(Painter g) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            if (a.isVisible()) {
                g.drawImage(Assets.asteroid, (int) a.getX(), (int) a.getY(),
                        ASTEROID_WIDTH, ASTEROID_HEIGHT);
            }
        }
    }

    private void renderItems(Painter g) {
        if (dualLaser.isVisible())
            g.drawImage(Assets.laserItem, (int) dualLaser.getX(), (int) dualLaser.getY(),
                    Assets.laserItem.getWidth(), Assets.laserItem.getHeight());
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

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        //Log.d("Var", String.valueOf(recentTouchY));
        //Log.d("Var", String.valueOf(scaledY));

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // return if pause?
            if (gamePaused)
                return true;

            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                //gamePaused = true;
                return true;
            }

            recentTouchY = scaledY;
            recentTouchX = scaledX; // new

            player.setFiringStatus(true);

        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // return if pause?
            if (gamePaused)
                return true;

            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                //gamePaused = true;
                player.setFiringStatus(false);
                return true;
            } else {
                player.setFiringStatus(true);
            }

            // to add some simple acceleration physics, use delta values
            // to change velocity, not just position..
            int deltaY = scaledY - (int) recentTouchY;
            int deltaX = scaledX - (int) recentTouchX;

            player.maneuver(deltaY, deltaX);

            // here is where you would determine which direction
            // the movement was in, and then load the appropriate animation

            // change values according to angle
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
            recentTouchX = scaledX; // new
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            // Resume game if paused.
            if (gamePaused) {

                if (quitRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                    player.setAlive(false);
                    return true;
                }

                gamePaused = false;

                if (Assets.resumeMusic())
                    return true;
                else {
                    musicString = selectMusic();
                    Assets.playMusic(musicString, true);
                    return true;
                }
                //selectMusic();
            }

            if (pauseRect.intersects(scaledX, scaledY, scaledX, scaledY)) {
                gamePaused = true;
                Assets.pauseMusic();
                //onPause();
                return true;
            }

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

    }

}