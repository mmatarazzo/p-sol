package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.game.model.Asteroid;
import com.example.mmataraz.game.model.Star;
import com.example.mmataraz.game.model.Player;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.util.ArrayList;

/**
 * Created by Mike on 2/16/2015.
 */
public class PlayState extends State {

    private Player player;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Star> stars; // new
    private Animation currentAnim;

    private int playerScore = 0;
    //private String playerScoreString;

    // ship - new
    /*private static final int PLAYER_WIDTH = 66;
    private static final int PLAYER_HEIGHT = 92;*/
    /*private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 32;*/
    private static final int PLAYER_SIZE = 32;

    // asteroid - new
    /*private static final int ASTEROID_HEIGHT = 50;
    private static final int ASTEROID_WIDTH = 50;*/
    private static final int ASTEROID_SIZE = 50;
    private static final int NUM_ASTEROIDS = 8;
    //private static final int ASTEROID_SPACING = GameMainActivity.GAME_WIDTH / NUM_ASTEROIDS;
    private int asteroidSpeed = -200;

    private static final int NUM_STARS = 128;

    // to get position deltas - new
    private float recentTouchY;
    private float recentTouchX;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    @Override
    public void init() {
        /*player = new Player(160, GameMainActivity.GAME_HEIGHT - 45 -
                PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);*/
        player = new Player(256, 128, PLAYER_SIZE, PLAYER_SIZE);

        asteroids = new ArrayList<Asteroid>();
        stars = new ArrayList<Star>(); // new
        currentAnim = Assets.levelAnim;

        for (int i = 0; i < NUM_ASTEROIDS; i++) {
            Asteroid a = new Asteroid(i * 128, GameMainActivity.GAME_HEIGHT,
                    ASTEROID_SIZE, ASTEROID_SIZE);
            asteroids.add(a);
        }

        // stars - new
        for (int i = 0; i < NUM_STARS; i++) {
            Star c = new Star();
            stars.add(c);
        }

        //Assets.playMusic("Polfix.mid", true);
        Assets.playMusic("Bosstheme.MID", true);
    }

    // Overrides onPause() from State.
    // Called when Activity is pausing.
    @Override
    public void onPause() {
        Assets.pauseMusic();
        gamePaused = true;
    }

    @Override
    public void update(float delta) {
        // If game is paused, do not update anything.
        if (gamePaused) {
            return;
        }

        if (!player.isAlive()) {
            Assets.stopMusic();
            setCurrentState(new GameOverState(playerScore / 100));
        }

        playerScore += 1;

        if (playerScore % 500 == 0 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
        }

        // for stars
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            c.update(delta);
        }

        //Assets.runAnim.update(delta);
        //Assets.shipAnim.update(delta);
        currentAnim.update(delta);
        player.update(delta, currentAnim, asteroids);
        updateAsteroids(delta);
    }

    private void updateAsteroids(float delta) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
        //for (Asteroid b : asteroids) {
            a.update(delta, asteroidSpeed);

            if (a.isVisible()) {
                if (player.isDucked() && Rect.intersects(a.getRect(),
                        player.getDuckRect())) {
                    a.onCollide(player);
                } else if (!player.isDucked() && Rect.intersects(a.getRect(),
                        player.getRect())) {
                    a.onCollide(player);
                }
            }
        }
    }

    @Override
    public void render(Painter g) {
        //g.setColor(Color.rgb(208, 244, 247));
        g.setColor(Color.rgb(8, 8, 32));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        renderStars(g); // new
        renderPlayer(g);
        renderAsteroids(g);
        //renderSun(g);
        //renderClouds(g);

        //g.drawImage(Assets.grass, 0, 405);
        renderScore(g);

        // If game is Pause, draw additional UI elements:
        if (gamePaused) {
            // ARGB is used to set an ARGB color.
            g.setColor(Color.argb(153, 0, 0, 0));
            g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
            g.setColor(Color.LTGRAY);
            g.drawString(pausedString, 235, 240);
        }
    }

    private void renderScore(Painter g) {
        g.setFont(Typeface.SANS_SERIF, 25);
        g.setColor(Color.LTGRAY);

        //playerScoreString = String.valueOf(playerScore / 100);
        String scoreString = Integer.toString(playerScore / 100);
        g.drawString(/*"" + playerScore / 100*/ scoreString, 20, 30);
    }

    private void renderPlayer(Painter g) {
        currentAnim.render(g, (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());
        /*if (player.isGrounded()) {
            if (player.isDucked()) {
                g.drawImage(Assets.duck, (int) player.getX(), (int) player.getY());
            } else {
                Assets.runAnim.render(g, (int) player.getX(), (int) player.getY(),
                        player.getWidth(), player.getHeight());
            }
        } else {
            g.drawImage(Assets.jump, (int) player.getX(), (int) player.getY(),
                    player.getWidth(), player.getHeight());
        }*/

        player.renderWeapon(g);
    }

    private void renderAsteroids(Painter g) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
        //for (Asteroid b : asteroids) {
            if (a.isVisible()) {
                g.drawImage(Assets.asteroid, (int) a.getX(), (int) a.getY(),
                        ASTEROID_SIZE, ASTEROID_SIZE);
            }
        }
    }

    /*private void renderSun(Painter g) {
        g.setColor(Color.rgb(255, 165, 0));
        g.fillOval(715, -85, 170, 170);
        g.setColor(Color.YELLOW);
        g.fillOval(725, -75, 150, 150);
    }

    private void renderClouds(Painter g) {
        g.drawImage(Assets.cloud1, (int) cloud1.getX(), (int) cloud1.getY(), 100, 60);
        g.drawImage(Assets.cloud2, (int) cloud2.getX(), (int) cloud2.getY(), 100, 60);
    }*/

    // new feature, let's try STARS!!
    private void renderStars(Painter g) {
        //g.setColor(Color.rgb(255, 255, 255));
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            g.setColor(c.getColor());
            g.fillOval((int) c.getX(), (int) c.getY(), (int) c.getSize(), (int) c.getSize());
        }
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
            if (Math.abs(player.getVelY()) <= 8)
                currentAnim = Assets.levelAnim;
            else if (player.getVelY() > 8 && player.getVelY() <= 32)
                currentAnim = Assets.downOneAnim;
            else if (player.getVelY() > 32)
                currentAnim = Assets.downTwoAnim;
            else if (player.getVelY() < -8 && player.getVelY() >= -32)
                currentAnim = Assets.upOneAnim;
            else if (player.getVelY() < -32)
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

            /*if (scaledY - recentTouchY < -50) {
                player.jump();
            } else if (scaledY - recentTouchY > 50) {
                player.duck();
            }*/
        }

        return true;
    }

}
