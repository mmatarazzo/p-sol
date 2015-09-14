package com.example.mmataraz.game.model;

import android.graphics.Color;

import com.example.mmataraz.framework.util.RandomNumberGenerator;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.util.Random;

/**
 * Created by Mike on 2/15/2015.
 */
public class Star {

    private float x, y;
    private int size, speed;
    private int color;

    private static final int MIN_STAR_SIZE = 1;
    private static final int MAX_STAR_SIZE = 3;

    private static final int MIN_STAR_SPEED = -32;
    private static final int MAX_STAR_SPEED = -128;

    private static final int[] STAR_COLORS = { Color.CYAN, Color.WHITE, Color.YELLOW,
            Color.MAGENTA, Color.LTGRAY };

    public Star() {
        x = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_WIDTH - 4);
        y = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_HEIGHT - 4);

        size = RandomNumberGenerator.getRandIntBetween(MIN_STAR_SIZE, MAX_STAR_SIZE);
        speed = RandomNumberGenerator.getRandIntBetween(MAX_STAR_SPEED, MIN_STAR_SPEED);
        color = STAR_COLORS[RandomNumberGenerator.getRandInt(STAR_COLORS.length)];
    }

    public void update(float delta) {
        //x += VEL_X * delta;
        x += speed * delta;

        //if (x <= -200) {
        if (x <= -4) {
            // Reset to the right
            //x += 1000;
            x += GameMainActivity.GAME_WIDTH + 8;
            //y = RandomNumberGenerator.getRandIntBetween(20, 100);
            y = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_HEIGHT - 4);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() { return size; }

    public int getColor() {
        return color;
    }

}
