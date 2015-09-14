package com.example.mmataraz.game.model;

import android.graphics.Rect;

import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Michael on 9/5/2015.
 */
public class Weapon {

    private float x, y;
    private int width, height, velY;
    private Rect rect;
    private boolean render;

    private static final int LASER_SPEED = 512;
    //public static final int LASER_COLOR = Color.GREEN;

    public Weapon(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velY = 0;

        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        render = false;
    }

    public void update(float delta) {
        if (y < -1 || y > GameMainActivity.GAME_HEIGHT + 1)
            velY = 0;

        y += velY * delta;
        x += LASER_SPEED * delta;

        if (x >= GameMainActivity.GAME_WIDTH /*+ 4*/) {
            if (render)
                render = false;

            //x -= GameMainActivity.GAME_WIDTH + 8;
            x -= (GameMainActivity.GAME_WIDTH + width * 2);
        }
    }

    public void updateRect() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void onCollide(Asteroid a) {
        if (render && a.isVisible()) {
            Assets.playSound(Assets.destroyID);
            render = false;
            a.setVisible(false);
        }
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

    public void setVelY(int position) {
        switch (position) {
            case 0:
                velY = 0;
                break;
            case -1:
                velY = -LASER_SPEED / 4;
                break;
            case -2:
                velY = -LASER_SPEED / 2;
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
