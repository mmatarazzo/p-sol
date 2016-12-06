package com.example.mmataraz.game.model;

import android.graphics.Rect;

import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Michael on 9/5/2015.
 */
public class Weapon {

    private float x, y, startX;
    private int width, height, velY, remainder;
    private Rect rect;
    private boolean render;

    //private static final int LASER_SPEED = /*768*/ 640;
    //private static final int LASER_SPEED = /*532*/ 896;   // 6-tuplets at 213 bpm
    private static final int LASER_SPEED = /*1024*/ 720;    // try 960 next with wider render range and 16x2 segments
    private static final int LASER_DISTANCE = 384;

    public Weapon(float x, float y, int width, int height, int remainder) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.width = width;
        this.height = height;
        this.velY = 0;
        this.remainder = remainder;

        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        render = false;
    }

    public void update(float delta, int factor) {
        y += velY * delta;
        x += LASER_SPEED * delta * factor;

        if (!render)
            startX = x;
        else if (Math.abs(x - startX) > LASER_DISTANCE) // abs to account for both directions
            render = false;

        if (y < -8 || y > GameMainActivity.GAME_HEIGHT + 8)
            velY = 0;

        // to shorten laser travel distance, pick a constant
        // and check to see if the difference between the current
        // x and the start x exceeds the constant
        if (factor == 1 && x >= GameMainActivity.GAME_WIDTH ||
                factor == -1 && x <= 0) {
            if (render)
                render = false;

            //x -= (GameMainActivity.GAME_WIDTH + width*2);
            x = (factor == 1) ? x - (GameMainActivity.GAME_WIDTH + width*/*2*/remainder) :
                    x + (GameMainActivity.GAME_WIDTH + width*/*2*/remainder);
            // maybe reset y to center? - probably not necessary
        }
    }

    public void updateRect() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void updateRectDual() {
        rect.set((int) x, (int) y - 4, (int) x + width, (int) y + height + 4);
    }

    public boolean onCollide(Asteroid a) {
        if (render && a.isVisible()) {
            a.setVisible(false);
            render = false;
            Assets.playSound(Assets.destroyID, 0);

            return true;
        }

        return false;
    }

    public boolean onCollideShip() {
        if (render) {
            render = false;
            //Assets.playSound(Assets.hitID);

            return true;
        }

        return false;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // possible adjustment here?
    public void setVelY(int position) {
        switch (position) {
            case 0:
                velY = 0;
                break;
            case -1:
                velY = -LASER_SPEED / 4;    // multiply by 3/16?
                break;
            case -2:
                velY = -LASER_SPEED / 2;    // multiply by 3/8?
                break;
            case 1:
                velY = LASER_SPEED / 4;
                break;
            case 2:
                velY = LASER_SPEED / 2;
                break;
            default:
                break;
        }
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public boolean getRender() {
        return this.render;
    }

    public Rect getRect() {
        return rect;
    }

}