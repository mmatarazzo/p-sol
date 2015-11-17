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

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 3;

    private static final int MIN_DUST_SPEED = -64;
    private static final int MAX_DUST_SPEED = -128;

    private static final int[] STAR_COLORS = { Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE,
            Color.LTGRAY, Color.GRAY, Color.DKGRAY };

    public Star(int choice) {
        x = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_WIDTH - 4);
        y = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_HEIGHT - 4);

        size = RandomNumberGenerator.getRandIntBetween(MIN_SIZE, MAX_SIZE);

        switch (choice) {
            case 1:
                speed = RandomNumberGenerator.getRandInt(3);
                color = STAR_COLORS[RandomNumberGenerator.getRandInt(STAR_COLORS.length - 3)];
                break;
            case 2:
                speed = RandomNumberGenerator.getRandIntBetween(MAX_DUST_SPEED, MIN_DUST_SPEED);
                color = STAR_COLORS[RandomNumberGenerator.getRandIntBetween(STAR_COLORS.length - 3, STAR_COLORS.length)];
                break;
            default:
                break;
        }
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
