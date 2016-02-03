package com.example.mmataraz.game.model;

import android.graphics.Rect;

import com.example.mmataraz.framework.util.RandomNumberGenerator;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Michael on 11/17/2015.
 */
public class Item {

    private float x, y;
    private int width, height, startTime;
    private boolean visible;
    private Rect rect;

    private static final int UPPER_Y = 128;
    private static final int LOWER_Y = GameMainActivity.GAME_HEIGHT - 128;

    private static final int ITEM_SPEED = -128;
    private static final int MIN_START_TIME = 1024;
    private static final int MAX_START_TIME = 2048;

    public Item(float x, float y, int width, int height) {
        //x = GameMainActivity.GAME_WIDTH;
        this.x = x;
        //y = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_HEIGHT - 4);
        //y = RandomNumberGenerator.getRandIntBetween(UPPER_Y, LOWER_Y);
        this.y = y;

        this.width = width;
        this.height = height;
        startTime = RandomNumberGenerator.getRandIntBetween(MIN_START_TIME, MAX_START_TIME);

        visible = false;

        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void update(float delta, Player p, int timer) {
        /*if (x < -width)
            visible = false;*/
        if (!p.getDual() && !visible && timer > startTime) {
            visible = true;
            y = RandomNumberGenerator.getRandIntBetween(UPPER_Y, LOWER_Y);
        }

        if (visible) {
            x += ITEM_SPEED * delta;
            updateRect();
        }

        if (x < -width)
            visible = false;
    }

    public void updateRect() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void onCollide(Player p) {
        visible = false;
        p.setDual(true);
        x = GameMainActivity.GAME_WIDTH;
        updateRect();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public Rect getRect() {
        return rect;
    }
}
