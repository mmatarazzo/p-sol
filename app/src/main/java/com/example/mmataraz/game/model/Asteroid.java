package com.example.mmataraz.game.model;

import android.graphics.Rect;

import com.example.mmataraz.framework.util.RandomNumberGenerator;

/**
 * Created by Mike on 2/15/2015.
 */
public class Asteroid {

    private float x, y;
    private int width, height;
    private Rect rect;
    private boolean visible;

    //private static final int UPPER_Y = 275;
    private static final int UPPER_Y = 32;
    //private static final int LOWER_Y = 355;
    private static final int LOWER_Y = 416;

    public Asteroid(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        visible = false;
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

        /*// 1 in 3 chance of becoming an Upper Asteroid
        if (RandomNumberGenerator.getRandInt(3) == 0) {
            y = UPPER_Y;
        } else {
            y = LOWER_Y;
        }*/

        y = RandomNumberGenerator.getRandIntBetween(UPPER_Y, LOWER_Y);
        x += 1000;

        updateRect();
    }

    public void onCollide(Player p) {
        visible = false;
        p.pushBack(30);
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

}
