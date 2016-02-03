package com.example.mmataraz.game.model;

import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 9/13/2015.
 */
public class Planet {

    private float x, y;
    //private int width, height;
    private boolean visible;

    private static final int PLANET_SPEED = -4;

    public Planet(float x, float y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    public void update(float delta) {
        x += PLANET_SPEED * delta;

        if (x < -GameMainActivity.GAME_WIDTH)
            visible = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getVisible() {
        return visible;
    }

}
