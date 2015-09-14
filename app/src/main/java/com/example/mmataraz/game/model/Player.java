package com.example.mmataraz.game.model;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.game.state.PlayState;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by Mike on 2/15/2015.
 */
public class Player {

    private float x, y, nextX, nextY;
    private int width, height;
    private int velX, velY, nextVelX, nextVelY;  // new
    private Rect rect, duckRect, ground;

    private ArrayList<Weapon> lasers;
    private boolean firing;

    //private boolean inBounds, inBoundsX, inBoundsY;    // new
    private boolean isAlive;
    private boolean isDucked;
    private float duckDuration = .6f;

    private static final int JUMP_VELOCITY = -600;
    //private static final int ACCEL_GRAVITY = 1800;

    // new
    private static final int MAX_VELOCITY_Y = 128;
    private static final int MAX_VELOCITY_X = 256;

    // weapon
    private static final int LASER_WIDTH = 8;
    private static final int LASER_HEIGHT = 1;
    /*private static final int LASER_SPEED = 512;
    private static final int LASER_COLOR = Color.GREEN;*/

    public Player(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        ground = new Rect(0, 405, 0 + 800, 405 + 45);
        rect = new Rect();
        duckRect = new Rect();
        /*inBounds = true;    // new
        inBoundsX = true;
        inBoundsY = true;*/
        isAlive = true;
        isDucked = false;

        lasers = new ArrayList<Weapon>();

        for (int i = 0; i <= GameMainActivity.GAME_WIDTH; i += (LASER_WIDTH * 3)) {
            Weapon w = new Weapon(i, y, LASER_WIDTH, LASER_HEIGHT);
            lasers.add(w);
        }
    }

    public void update(float delta, Animation currentAnim, ArrayList<Asteroid> asteroids) {
        /*if (duckDuration > 0 && isDucked) {
            duckDuration -= delta;
        } else {
            isDucked = false;
            duckDuration = .6f;
        }*/

        /*if (!isGrounded()) {
            //velY += ACCEL_GRAVITY * delta;
        } else {
            y = 406 - height;
            velY = 0;
        }*/

        // bounce flags NOT NECESSARY

        /*if (inBoundsX) {
            if (!checkInsideLeft(x) || !checkInsideRight(x)) {
                //velX = -velX/velX;
                inBoundsX = false;
            }
        }

        if (inBoundsY) {
            if (!checkInsideTop(y) || !checkInsideBottom(y)) {
                //velY = -velY/velY;
                inBoundsY = false;
            }
        }*/

        nextX = x + velX * delta;
        nextY = y + velY * delta;

        if (!checkInsideLeft(nextX)) {
            x = 0;
            velX = 0;
        } else if (!checkInsideRight(nextX)) {
            x = GameMainActivity.GAME_WIDTH - width;
            velX = 0;
        } else
            x = nextX;

        if (!checkInsideTop(nextY)) {
            y = 0;
            velY = 0;
        } else if (!checkInsideBottom(nextY)) {
            y = GameMainActivity.GAME_HEIGHT - height;
            velY = 0;
        } else
            y = nextY;

        /*y += velY * delta;
        x += velX * delta;*/

        /*if (checkInsideLeft(x) && checkInsideRight(x))
            inBoundsX = true;
        if (checkInsideTop(y) && checkInsideBottom(y))
            inBoundsY = true;*/

        updateRects();
        updateWeapon(delta, currentAnim, asteroids);
    }

    private void updateWeapon(float delta, Animation currentAnim, ArrayList<Asteroid> asteroids) {
        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);
            w.update(delta);

            if (firing && !w.getRender()) {
                if ((int) w.getX() >= /*==*/ x + width - 1 && (int) w.getX() <= x + width + 7) {
                    if (currentAnim == Assets.levelAnim) {
                        w.setVelY(0);
                        w.setY(y + height / 2);
                    } else if (currentAnim == Assets.upOneAnim) {
                        w.setVelY(-1);
                        w.setY(y + 3 * height / 8);
                    } else if (currentAnim == Assets.upTwoAnim) {
                        w.setVelY(-2);
                        w.setY(y + height / 4);
                    } else if (currentAnim == Assets.downOneAnim) {
                        w.setVelY(1);
                        w.setY(y + 5 * height / 8);
                    } else if (currentAnim == Assets.downTwoAnim) {
                        w.setVelY(2);
                        w.setY(y + 3 * height / 4);
                    }

                    Assets.playSound(Assets.fireID);
                    //w.setY(y + height / 2);
                    w.setRender(true);
                }
            }

            w.updateRect();
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (Rect.intersects(w.getRect(),a.getRect()))
                    w.onCollide(a);
            }
        }
    }

    public void updateRects() {
        //rect.set((int) x + 10, (int) y, (int) x + (width - 20), (int) y + height);
        //duckRect.set((int) x, (int) y + 20, (int) x + width, (int) y + 20 + (height - 20));

        // new
        //rect.set((int) x, (int) y + 28, (int) x + (width - 8), (int) y + (height - 27));
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void renderWeapon(Painter g) {
        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);

            if (w.getRender()) {
                g.setColor(Color.GREEN);
                g.fillOval((int) w.getX(), (int) w.getY(), w.getWidth(), w.getHeight());
            }
        }
    }

    public void jump() {
        if (isGrounded()) {
            Assets.playSound(Assets.onJumpID);
            isDucked = false;
            duckDuration = .6f;
            y -= 10;
            velY = JUMP_VELOCITY;
            updateRects();
        }
    }

    public void duck() {
        if (isGrounded()) {
            isDucked = true;
        }
    }

    public void pushBack(int dX) {
        x -= dX;
        Assets.playSound(Assets.hitID);
        if (x < -width / 2) {
            isAlive = false;
        }
        //rect.set((int) x, (int) y, (int) x + width, (int) y + height);
        updateRects();
    }

    // new
    public void maneuver(int dY, int dX) {
        /*y += deltaY;
        x += deltaX;
        updateRects();*/

        // simple acceleration physics
        nextVelX = velX + dX * 2;
        nextVelY = velY + dY * 2;

        // max velocity check
        velX = (Math.abs(nextVelX) < MAX_VELOCITY_X) ? nextVelX : velX;
        velY = (Math.abs(nextVelY) < MAX_VELOCITY_Y) ? nextVelY : velY;

        /*velY += dY*2;
        velX += dX*2;*/

        // Show velocity here
        Log.d("VelX", String.valueOf(velX));
        Log.d("VelY", String.valueOf(velY));
    }

    public void setFiringStatus(boolean firing) {
        this.firing = firing;
    }

    public boolean isGrounded() {
        return Rect.intersects(rect, ground);
    }

    public boolean checkInsideEdges() {
        return checkInsideLeft(x) && checkInsideRight(x) && checkInsideTop(y) && checkInsideBottom(y);
    }

    public boolean checkInsideLeft(float checkX) {
        return (checkX > 0);
    }

    public boolean checkInsideRight(float checkX) {
        return (checkX + width < GameMainActivity.GAME_WIDTH);
    }

    public boolean checkInsideTop(float checkY) {
        return (checkY > 0);
    }

    public boolean checkInsideBottom(float checkY) {
        return (checkY + height < GameMainActivity.GAME_HEIGHT);
    }

    public boolean isDucked() {
        return isDucked;
    }

    public float getX() {
        return x;
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

    public int getVelY() {
        return velY;
    }

    public Rect getRect() {
        return rect;
    }

    public Rect getDuckRect() {
        return duckRect;
    }

    public Rect getGround() {
        return ground;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public float getDuckDuration() {
        return duckDuration;
    }

}
