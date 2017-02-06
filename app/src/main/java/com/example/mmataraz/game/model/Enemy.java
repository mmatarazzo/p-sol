package com.example.mmataraz.game.model;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.RandomNumberGenerator;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.util.ArrayList;

/**
 * Created by Michael on 2/4/2016.
 */
public class Enemy {

    private enum MovementType {VERTICAL, HORIZONTAL, BOTH}
    public enum EnemyType {FIGHTER, CAPITAL, OBJECT}
    //private enum EmergeType {INTERCEPT, OVERTAKE, PASS}

    private float x, y, nextX, nextY;
    private int width, height;
    private int velX, velY, nextVelX, nextVelY;  // new
    private int shield, maxShield, energy;
    private Rect rect;

    private ArrayList<Weapon> lasers;
    private boolean firing, dual, isAlive, onScreen, active, evade;
    private int evadeCounterX, evadeCounterY, firingCounter;

    // ship speed and mass
    private int maxVelY /*= 256*/;
    private int maxVelX /*= 384*/;
    private int mass /*= 100*/;
    private int weaponEnergy = 20;

    private int randInt, randInt2, animType;
    private MovementType movementType;
    private EnemyType enemyType;

    // weapon
    private static final int LASER_WIDTH = /*8*/ 16;
    private static final int LASER_HEIGHT = 1;

    private static final int LASER_SPACING = /*3*/ 2;
    private int laserTotalWidth = LASER_WIDTH * LASER_SPACING;
    private int laserSegmentRemainder = LASER_SPACING - (GameMainActivity.GAME_WIDTH % laserTotalWidth) / LASER_WIDTH;

    public Enemy(float x, float y, int width, int height, EnemyType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        maxVelY = 256;
        maxVelX = 384;
        mass = 100;
        maxShield = 50;
        shield = maxShield;
        energy = 800;

        enemyType = type;

        switch (type) {
            case FIGHTER:
                //enemyType = type;
                break;
            case CAPITAL:
                //enemyType = type;
                maxVelY = 80;
                maxVelX = 120;
                mass = 400;
                maxShield = 200;
                shield = maxShield;
                break;
            case OBJECT:
                //enemyType = type;
                break;
            default:
                break;
        }

        rect = new Rect();
        isAlive = true;
        onScreen = false;
        active = false; //try
        evade = false;  //try
        evadeCounterX = 0;
        evadeCounterY = 0;
        firingCounter = 0;

        lasers = new ArrayList<Weapon>();
        //for (int i = 0; i <= GameMainActivity.GAME_WIDTH; i += (LASER_WIDTH * 3)) {
        for (int i = GameMainActivity.GAME_WIDTH; i >= 0; i -= /*(LASER_WIDTH * 3)*/ laserTotalWidth) {
            Weapon w = new Weapon(i, y, -LASER_WIDTH, LASER_HEIGHT, laserSegmentRemainder);
            lasers.add(w);
        }

        firing = false;
        dual = false;

        randInt = 0;
        randInt2 = 0;
        animType = 0;
        movementType = MovementType.VERTICAL;
    }

    public void setAnim(int anim) {
        animType = anim;
    }

    public int getAnim() {
        return animType;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public EnemyType getType() {
        return enemyType;

        /*switch (enemyType) {
            case FIGHTER:
                return 0;
            case CAPITAL:
                return 1;
            case OBJECT:
                return 2;
            default:
                return -1;
        }*/
    }

    public void update(float delta, ArrayList<Asteroid> asteroids, Player player, Player wingman) {

        if (!active)
            return;

        if (!onScreen && isAlive) {
            emerge();
            if (x > 50 && x < 750 && y > 50 && y < 350)
                onScreen = true;
        }
        else if (onScreen && isAlive) {
            // do cruise stuff
            //Log.d("velX", Integer.toString(velX));
            //Log.d("velY", Integer.toString(velY));

            if (!evade) {
                if (velY > 8) {
                    maneuver(-2, 0);
                    // fire
                } else if (velY < -8) {
                    maneuver(2, 0);
                    // fire
                } else {
                    int next = velY > 0 ? -1 : 1;
                    maneuver(next, 0);
                    // no fire
                }

                if (velX > 8) {
                    maneuver(0, -2);
                    // fire
                } else if (velX < -8) {
                    maneuver(0, 2);
                    // fire
                } else {
                    int next = velX > 0 ? -1 : 1;
                    maneuver(0, next);
                    // no fire
                }

                if (firing) {
                    if (firingCounter > 30) {
                        firing = false;
                        firingCounter = 0;
                    }
                    else
                        firingCounter++;
                }

                randInt = RandomNumberGenerator.getRandInt(60);
                //if (randInt % 30 == 0) {
                if (randInt % 31 == 30) {
                    firing = true;

                    if (randInt == 30) {
                        evade = true;
                        evadeCounterY = y < GameMainActivity.GAME_HEIGHT / 2 ? 1 : -1;
                        evadeCounterX = x < GameMainActivity.GAME_WIDTH / 2 ? 1 : -1;

                        randInt2 = RandomNumberGenerator.getRandInt(3);
                        switch (randInt2) {
                            case 0:
                                movementType = MovementType.VERTICAL;
                                break;
                            case 1:
                                movementType = MovementType.HORIZONTAL;
                                break;
                            case 2:
                                movementType = MovementType.BOTH;
                                break;
                            default:
                                break;
                        }
                    }
                }

            } else {
                // random movements and fire
                if (Math.abs(evadeCounterX) > 12 || Math.abs(evadeCounterY) > 12) {
                    evade = false;
                    evadeCounterX = 0;
                    evadeCounterY = 0;

                    if (firingCounter == 0)
                        firing = false;
                } else {
                    switch (movementType) {
                        case VERTICAL:
                            maneuver(evadeCounterY, 0);
                            break;
                        case HORIZONTAL:
                            maneuver(0, evadeCounterX);
                            break;
                        case BOTH:
                            maneuver(evadeCounterY, evadeCounterX);
                            break;
                        default:
                            break;
                    }
                    //maneuver(evadeCounter, 0);

                    //evadeCounter++;
                    evadeCounterX += evadeCounterX/Math.abs(evadeCounterX);
                    evadeCounterY += evadeCounterY/Math.abs(evadeCounterY);
                }
            }
        } else if (onScreen && !isAlive) {
            //depart (blow up)
        }

        nextX = x + velX * delta;
        nextY = y + velY * delta;

        if (!checkInsideLeft(nextX) && onScreen) {  // and alive
            x = 0;
            velX = 0;
        } else if (!checkInsideRight(nextX) && onScreen) {
            x = GameMainActivity.GAME_WIDTH - width;
            velX = 0;
        } else
            x = nextX;

        if (!checkInsideTop(nextY) && onScreen) {
            y = 0;
            velY = 0;
        } else if (!checkInsideBottom(nextY) && onScreen) {
            y = GameMainActivity.GAME_HEIGHT - height;
            velY = 0;
        } else
            y = nextY;

        updateRects();
        updateEnergy();
        updateWeapon(delta, asteroids, player, wingman);
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

    private void updateWeapon(float delta, ArrayList<Asteroid> asteroids, Player player, Player wingman) {
        //int scoreUpdate = 0;

        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);
            w.update(delta, -1);

            // update rects must go after the next section because yPos is changing

            // LASER START - needs some adjustment?
            if (energy >= weaponEnergy && firing && !w.getRender()) {
                //if ((int) w.getX() >= x + width - 1 && (int) w.getX() <= x + width + 11) {
                if ((int) w.getX() <= x + 4 && (int) w.getX() > x - 8) {
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
                    Assets.playSound(Assets.fireID, 0);
                }
            }

            if (!dual)
                w.updateRect();
            else
                w.updateRectDual();

            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (Rect.intersects(w.getRect(),a.getRect())) {
                    w.onCollide(a);
                    //if (w.onCollide(a))
                        //scoreUpdate++;
                }
            }

            if (Rect.intersects(w.getRect(), player.getRect())) {
                if (w.onCollideShip())
                    player.onLaserHit();
            }

            if (Rect.intersects(w.getRect(), wingman.getRect())) {
                if (w.onCollideShip())
                    wingman.onLaserHit();
            }
        }

        //return scoreUpdate;
    }

    public void renderWeapon(Painter g) {
        for (int i = 0; i < lasers.size(); i++) {
            Weapon w = lasers.get(i);

            // some adjustment needed
            if (w.getRender()) {
                /*g.setColor(Color.RED);
                if (!dual) {
                    g.fillOval((int) w.getX(), (int) w.getY(), w.getWidth(), w.getHeight());
                } else {
                    g.fillOval((int) w.getX(), (int) w.getY() + 4, w.getWidth(), w.getHeight());
                    g.fillOval((int) w.getX(), (int) w.getY() - 4, w.getWidth(), w.getHeight());
                }*/

                if (!dual)
                    g.drawImage(Assets.enemyLaser, (int) w.getX(), (int) w.getY()-height/4);
                else {
                    g.drawImage(Assets.enemyLaser, (int) w.getX(), (int) w.getY()-height/2);
                    g.drawImage(Assets.enemyLaser, (int) w.getX(), (int) w.getY());
                }
            }
        }
    }

    // basically the same, but for larger object
    public void pushBack(int dX) {
        x -= dX;
        Assets.playSound(Assets.hitID, 0);

        shield -= 5;
        if (shield < 1) {
            isAlive = false;
        }

        updateRects();
    }

    public int onLaserHit() {
        int score = 0;

        x += 1;
        //Assets.playSound(Assets.hitID, 0);

        if (isAlive)
        {
            shield -= 2;
            if (shield < 1) {
                isAlive = false;
                onScreen = false;   // for now

                if (enemyType == EnemyType.FIGHTER)
                    score = 5;
                else if (enemyType == EnemyType.CAPITAL)
                    score = 10;
            }
        }

        updateRects();

        return score;
    }

    // new
    public void maneuver(int dY, int dX) {
        // simple acceleration physics
        nextVelX = velX + dX * (enemyType == EnemyType.CAPITAL ? 2 : 3) /*2*/ /*3*/; // try 3 for more sensitive movement?
        nextVelY = velY + dY * (enemyType == EnemyType.CAPITAL ? 2 : 3) /*2*/ /*3*/;

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

    // enemy specific functions
    public void emerge() {
        //maneuver(2, /*-*/2);
        maneuver(y < 225 ? 2 : -2, x < 400 ? 2 : -2);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
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

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getMass() {
        return mass;
    }

    public void setVelX(float velX) {
        this.velX = (int) velX;
        this.velX = (Math.abs(this.velX) > maxVelX) ? (this.velX < 0 ? -maxVelX : maxVelX) : this.velX;
    }

    public void setVelY(float velY) {
        this.velY = (int) velY;
        this.velY = (Math.abs(this.velY) > maxVelY) ? (this.velY < 0 ? -maxVelY : maxVelY) : this.velY;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

}
