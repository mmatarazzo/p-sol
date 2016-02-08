package com.example.mmataraz.game.model;

import android.graphics.Color;

import com.example.mmataraz.framework.util.RandomNumberGenerator;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 2/15/2015.
 */
public class Star {

    private float x, y;
    private int size, speed, color;

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 3;

    private static final int MAX_STAR_SPEED = /*3*/ 4;
    private static final int MIN_DUST_SPEED = 64;
    private static final int MAX_DUST_SPEED = 128;

    private static final int[] STAR_COLORS = { Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE };
    private static final int[] DUST_COLORS = { Color.LTGRAY, Color.GRAY, Color.DKGRAY, Color.BLACK };

    public Star(int choice) {
        //x = RandomNumberGenerator.getRandIntBetween(4, 796);
        x = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_WIDTH - 4);
        //y = RandomNumberGenerator.getRandIntBetween(4, 446);
        y = RandomNumberGenerator.getRandIntBetween(4, GameMainActivity.GAME_HEIGHT - 4);
        size = RandomNumberGenerator.getRandIntBetween(MIN_SIZE, MAX_SIZE);

        switch (choice) {
            case 1: // star
                speed = RandomNumberGenerator.getRandInt(MAX_STAR_SPEED);
                color = STAR_COLORS[RandomNumberGenerator.getRandInt(STAR_COLORS.length)];
                break;
            case 2: // spacedust
                speed = RandomNumberGenerator.getRandIntBetween(MIN_DUST_SPEED, MAX_DUST_SPEED);
                color = DUST_COLORS[RandomNumberGenerator.getRandInt(DUST_COLORS.length)];
                break;
            default:
                break;
        }
    }

    public void update(float delta) {
        x -= speed * delta;

        if (x <= -4) {
            // Reset to the right
            x += (GameMainActivity.GAME_WIDTH + 4);
            //y = RandomNumberGenerator.getRandIntBetween(4, 446);
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
