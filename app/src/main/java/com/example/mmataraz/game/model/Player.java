package com.example.mmataraz.game.model;

import android.graphics.Color;
import android.graphics.Rect;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.util.ArrayList;

/**
 * Created by Mike on 2/15/2015.
 */
public class Player {

    private float x, y, nextX, nextY;
    private int width, height;
    private int velX, velY, nextVelX, nextVelY;  // new
    private int shield, energy;
    private Rect rect;

    private ArrayList<Weapon> lasers;
    private boolean firing, dual, isAlive;

    // ship speed
    private int maxVelY= 288; // >30 degrees of 400
    private int maxVelX= 512;

    // weapon
    private static final int LASER_WIDTH = 8;
    private static final int LASER_HEIGHT = 1;
    private int weaponEnergy = 20;

    private int mass = 100;

    public Player(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        shield = 100;
        energy = 800;
        rect = new Rect();
        isAlive = true;

        lasers = new ArrayList<Weapon>();
        for (int i = 0; i <= GameMainActivity.GAME_WIDTH; i += (LASER_WIDTH * 3)) {
            Weapon w = new Weapon(i, y, LASER_WIDTH, LASER_HEIGHT);
            lasers.add(w);
        }

        firing = false;
        dual = false;
    }

    public int update(float delta, ArrayList<Asteroid> asteroids) {
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

        updateRects();
        updateEnergy();
        return updateWeapon(delta, asteroids);
    }

    public void updateRects() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    private void updateEnergy() {
        if (energy < weaponEnergy)
            energy = 800;
        if (energy < 800)
            energy++;
    }

    private int updateWeapon(float delta, ArrayList<Asteroid> asteroids) {
        int scoreUpdate = 0;

        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);
            w.update(delta);

            // update rects must go after the next section because yPos is changing

            // LASER START - needs some adjustment?
            if (energy >= weaponEnergy && firing && !w.getRender()) {
                if ((int) w.getX() >= x + width - 1 && (int) w.getX() <= x + width + 11) {
                    if (Math.abs(velY) <= 70) {
                        w.setVelY(0);
                        w.setY(y + height / 2);
                    }
                    else if (velY < -70 && velY >= -145) {
                        w.setVelY(-1);
                        w.setY(y + height/2 - 3); // for more accurate tan(10)
                    }
                    else if (velY < -145) {
                        w.setVelY(-2);
                        w.setY(y + height / 2 - 6); // for more accurate tan(20)
                    }
                    else if (velY > 70 && velY <= 145) {
                        w.setVelY(1);
                        w.setY(y + height / 2 + 3); // for more accurate tan(10)
                    }
                    else if (velY > /*32*/ 145) {
                        w.setVelY(2);
                        w.setY(y + height / 2 + 6); // for more accurate tan(20)
                    }

                    w.setRender(true);
                    energy -= weaponEnergy;
                    Assets.playSound(Assets.fireID);
                }
            }

            if (!dual)
                w.updateRect();
            else
                w.updateRectDual();

            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (Rect.intersects(w.getRect(),a.getRect())) {
                    if (w.onCollide(a))
                        scoreUpdate++;
                }
            }
        }

        return scoreUpdate;
    }

    public void renderWeapon(Painter g) {
        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);

            // some adjustment needed
            if (w.getRender()) {
                g.setColor(Color.GREEN);
                if (!dual) {
                    g.fillOval((int) w.getX(), (int) w.getY(), w.getWidth(), w.getHeight());
                } else {
                    g.fillOval((int) w.getX(), (int) w.getY() + 4, w.getWidth(), w.getHeight());
                    g.fillOval((int) w.getX(), (int) w.getY() - 4, w.getWidth(), w.getHeight());
                }
            }
        }
    }

    public void pushBack(int dX) {
        x -= dX;
        Assets.playSound(Assets.hitID);
        if (x < -width / 2) {
            isAlive = false;
        }

        shield -= 5;
        if (shield < 1) {
            isAlive = false;
        }

        updateRects();
    }

    // new
    public void maneuver(int dY, int dX) {
        // simple acceleration physics
        nextVelX = velX + dX * /*2*/ 3; // try 3 for more sensitive movement?
        nextVelY = velY + dY * /*2*/ 3;

        // should just lock at max if max is exceeded
        velX = (Math.abs(nextVelX) > maxVelX) ? (nextVelX < 0 ? -maxVelX : maxVelX) : nextVelX;
        velY = (Math.abs(nextVelY) > maxVelY) ? (nextVelY < 0 ? -maxVelY : maxVelY) : nextVelY;
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

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getShield() {
        return shield;
    }

    public int getEnergy() {
        return energy;
    }

    public Rect getRect() {
        return rect;
    }

    public void setFiringStatus(boolean firing) {
        this.firing = firing;
    }

    public void setDual(boolean dual) {
        this.dual = dual;
    }

    public boolean getDual() {
        return dual;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getMass() {
        return mass;
    }

    public void setVelX(float velX) {
        this.velX = (int) velX;
    }

    public void setVelY(float velY) {
        this.velY = (int) velY;
    }
}