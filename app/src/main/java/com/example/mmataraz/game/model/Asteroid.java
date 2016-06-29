package com.example.mmataraz.game.model;

import android.graphics.Rect;

import com.example.mmataraz.framework.util.Collision;
import com.example.mmataraz.framework.util.RandomNumberGenerator;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 2/15/2015.
 */
public class Asteroid {

    private float x, y;
    private int width, height;
    private boolean visible;
    private Rect rect;

    private static final int UPPER_Y = 64;
    private static final int LOWER_Y = GameMainActivity.GAME_HEIGHT - 64;

    private static int velX;
    private int mass = 300;

    public Asteroid(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        visible = false;

        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void update(float delta, float velX) {
        x += velX * delta;
        updateRect();

        if (x <= -50) {
            reset();
        }
    }

    public void updateRect() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void reset() {
        visible = true;
        x += 1000;
        y = RandomNumberGenerator.getRandIntBetween(UPPER_Y, LOWER_Y);

        updateRect();
    }

    public void onCollide(Player p) {
        visible = false;
        //p.pushBack(32);
        Collision.playerAsteroidCollision(p, this);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public Rect getRect() {
        return rect;
    }

    public int getMass() {
        return mass;
    }

    public static int getVelX() {
        return velX;
    }

    public static void setVelX(int velX) {
        Asteroid.velX = velX;
    }

}