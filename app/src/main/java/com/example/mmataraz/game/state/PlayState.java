package com.example.mmataraz.game.state;

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
    private Item dualLaser;
    private Planet earth;
    private Player player;
    private ArrayList<Star> spaceDust;
    private ArrayList<Star> stars;
    private Animation currentAnim;

    private static final int NUM_ASTEROIDS = 8;
    private static final int ASTEROID_SPACING = 125;
    private static final int ASTEROID_WIDTH = Assets.asteroid.getWidth();
    private static final int ASTEROID_HEIGHT = Assets.asteroid.getHeight();
    private int asteroidSpeed = -200;

    private static final int ITEM_WIDTH = Assets.laserItem.getWidth();
    private static final int ITEM_HEIGHT = Assets.laserItem.getHeight();

    private static final int PLANET_START_X = 16;
    private static final int PLANET_START_Y = 96;
    //private static final int PLANET_SIZE = 720;

    private static final int PLAYER_START_X = 256;
    private static final int PLAYER_START_Y = 128;
    private static final int PLAYER_WIDTH = Assets.level.getWidth();
    private static final int PLAYER_HEIGHT = Assets.level.getHeight();
    private int playerScore = 0;
    //private String playerScoreString;

    private static final int SPACEDUST_CHOICE = 2;
    private static final int NUM_SPACEDUST = 32;
    private static final int STAR_CHOICE = 1;
    private static final int NUM_STARS = 64;

    private float recentTouchY;
    private float recentTouchX;

    private int timer = 0;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    @Override
    public void init() {
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < NUM_ASTEROIDS; i++) {
            Asteroid a = new Asteroid((float) i * ASTEROID_SPACING, (float) GameMainActivity.GAME_HEIGHT,
                    ASTEROID_WIDTH, ASTEROID_HEIGHT);
            asteroids.add(a);
        }

        dualLaser = new Item((float) GameMainActivity.GAME_WIDTH, (float) GameMainActivity.GAME_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT);

        earth = new Planet(PLANET_START_X, PLANET_START_Y);

        player = new Player(PLAYER_START_X, PLAYER_START_Y, PLAYER_WIDTH, PLAYER_HEIGHT);

        spaceDust = new ArrayList<Star>();
        // spacedust
        for (int i = 0; i < NUM_SPACEDUST; i++) {
            Star s = new Star(SPACEDUST_CHOICE);
            spaceDust.add(s);
        }

        stars = new ArrayList<Star>();
        // stars - new
        for (int i = 0; i < NUM_STARS; i++) {
            Star c = new Star(STAR_CHOICE);
            stars.add(c);
        }

        currentAnim = Assets.levelAnim;

        //Assets.playMusic("Polfix.mid", true);
        Assets.playMusic("Bosstheme.MID", true);
    }

    @Override
    public void update(float delta) {
        // If game is paused, do not update anything.
        if (gamePaused) {
            return;
        }

        if (!player.isAlive()) {
            Assets.stopMusic();
            setCurrentState(new GameOverState(playerScore /*/ 100*/));
        }

        // update spacedust
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            s.update(delta);
        }

        // update earth next
        if (earth.getVisible()) {
            earth.update(delta);
            Assets.earthAnim.update(delta);
            //earth.update(delta);
        }

        // update stars last
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            c.update(delta);
        }

        currentAnim.update(delta);
        playerScore += player.update(delta, asteroids);

        if (playerScore > 30 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
        }

        dualLaser.update(delta, player, timer);
        if (dualLaser.isVisible())
            if (Rect.intersects(dualLaser.getRect(), player.getRect()))
                dualLaser.onCollide(player);

        updateAsteroids(delta);

        if (++timer > 60000)
            timer = 0;
    }

    private void updateAsteroids(float delta) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            a.update(delta, asteroidSpeed);

            if (a.isVisible()) {
                if (Rect.intersects(a.getRect(), player.getRect()))
                    a.onCollide(player);
            }
        }
    }

    @Override
    public void render(Painter g) {
        //g.setColor(Color.rgb(208, 244, 247));
        g.setColor(Color.rgb(8, 8, 32));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        // render order
        renderStars(g);
        if (earth.getVisible())
            renderPlanet(g);
        renderSpaceDust(g);
        renderPlayer(g);
        renderAsteroids(g);
        renderItems(g);
        renderScore(g);

        renderShield(g);

        // If game is Pause, draw additional UI elements:
        if (gamePaused) {
            // ARGB is used to set an ARGB color.
            g.setColor(Color.argb(153, 0, 0, 0));
            g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
            g.setColor(Color.LTGRAY);
            g.drawString(pausedString, 235, 240);
        }
    }

    // render stars
    private void renderStars(Painter g) {
        //g.setColor(Color.rgb(255, 255, 255));
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            g.setColor(c.getColor());
            g.fillOval((int) c.getX(), (int) c.getY(), (int) c.getSize(), (int) c.getSize());
        }
    }

    // render earth
    private void renderPlanet(Painter g) {
        if (earth.getVisible()) {
            /*Assets.earthAnim.render(g, (int) earth.getX(), (int) earth.getY(),
                    GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);*/
            Assets.earthAnim.render(g, (int) earth.getX(), (int) earth.getY());
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
        //for (Asteroid b : asteroids) {
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
        g.setFont(Typeface.SANS_SERIF, 20);
        g.setColor(Color.LTGRAY);

        //playerScoreString = String.valueOf(playerScore / 100);
        String scoreString = Integer.toString(playerScore /*/ 100*/);
        g.drawString(/*"" + playerScore / 100*/ scoreString, 20, 30);
    }

    private void renderShield(Painter g) {
        g.setFont(Typeface.SANS_SERIF, 20);
        g.setColor(Color.RED);

        g.drawString("SHIELD: ", 150, 30);
        g.fillRect(230, 15, player.getShield(), 15);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        //Log.d("Var", String.valueOf(recentTouchY));
        //Log.d("Var", String.valueOf(scaledY));

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.d("Var", String.valueOf(recentTouchY));

            recentTouchY = scaledY;
            recentTouchX = scaledX; // new

            player.setFiringStatus(true);
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
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
                gamePaused = false;
                Assets.resumeMusic();
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
        Assets.pauseMusic();
        gamePaused = true;
    }
}
