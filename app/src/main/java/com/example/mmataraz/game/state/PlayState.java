package com.example.mmataraz.game.state;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.game.model.Asteroid;
import com.example.mmataraz.game.model.Enemy;
import com.example.mmataraz.game.model.Item;
import com.example.mmataraz.game.model.Planet;
import com.example.mmataraz.game.model.Star;
import com.example.mmataraz.game.model.Player;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

import java.util.ArrayList;

/**
 * Created by Mike on 2/16/2015.
 */
public class PlayState extends State {

    // Game objects
    private ArrayList<Asteroid> asteroids;
    private Item dualLaser; //tbd
    private Player player, wingman;
    private ArrayList<Enemy> currentEnemyGroup;
    private ArrayList<ArrayList<Enemy>> enemyForces;
    private Rect playerWingmanRect;

    // Dialogue
    private ArrayList<String> dialogue;
    private String currentChatter;
    private int chatterTrack, timerTrack;
    private static final int DIALOGUE_DURATION = 150;

    // Background objects
    private ArrayList<Star> spaceDust;
    private ArrayList<Star> stars;
    private Planet planet;

    // Asteroid properties
    private static final int ASTEROID_WIDTH = 50;
    private static final int ASTEROID_HEIGHT = 50;
    private int asteroidSpeed = -200;

    // Item properties
    private static final int ITEM_WIDTH = 240;
    private static final int ITEM_HEIGHT = 240;

    // Player properties
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 32;
    private static int rectWidth, rectHeight;
    private int playerScore = 0;

    // Enemy properties
    private int activeEnemyGroup = 0;
    private int deactiveCount = 0;
    private static final int ENEMY_WIDTH = 32;
    private static final int ENEMY_HEIGHT = 32;
    //private static final int CAPITAL_WIDTH_HEIGHT = 96;
    private static final int CAPITAL_WIDTH = 96;
    private static final int CAPITAL_HEIGHT = 96;

    // Touch location
    private float recentTouchY;
    private float recentTouchX;

    // Boolean to keep track of game pauses.
    private boolean gamePaused = false;
    // String displayed when paused;
    private String pausedString = "Game paused. Tap to resume.";

    // Other current items
    private Animation playerCurrentAnim, wingmanCurrentAnim;
    //private Animation enemyCurrentAnim;
    private Bitmap planetImage;
    private String musicString;
    private PlayStateLevel currentLevel;
    private int timer = 0;

    // Sound stream IDs
    //private static int launchStreamID;
    private static int engineStreamID;

    // Change to UI buttons
    private UIButton pauseButton, quitButton;
    private Rect pauseRect, quitRect;

    // PlayState Constructor
    public PlayState(PlayStateLevel level) {
        currentLevel = level;
    }

    @Override
    public void init() {
        // Determine level background image and music
        dialogue = new ArrayList<String>();
        switch (currentLevel) {
            case EARTH:
                planetImage = Assets.earth;
                musicString = "audio/earth-projsol-27.wav";

                dialogue.add("Gaia 2 standing by.. here we go!");
                dialogue.add("It looks like we're up against one of their smaller interdiction groups.");
                dialogue.add("I'm not sure why they're trying to stop us, but too bad for them.");
                dialogue.add("I have a feeling that we're gonna have other visitors too.");
                dialogue.add("Heads up!  I've got two fighters inbound.  It's the Mars 21st.");
                dialogue.add("Burn em!");
                dialogue.add("Two more, boss.  Man, I thought these guys would be going to Jupiter.");
                dialogue.add("That's it, no more fighters on my scope for now.");
                dialogue.add("But now I'm picking up two corvettes.  Mars 4th battle group.");
                dialogue.add("Watch that blast!");
                dialogue.add("Those cap ships are weak..");
                dialogue.add("Damn, more of the 4th.  Go get em!");

                break;

            case MARS:
                planetImage = Assets.mars;
                musicString = "audio/mars-projsol-08.wav";

                dialogue.add("Hey Skip, Ares 3 on your wing today.");
                dialogue.add("Dude, I am dying to burn some pompous ass Saturnians.");
                dialogue.add("Just hiding in the belt like little bitches.");
                dialogue.add("Hope we don't have to deal with anyone else.");
                dialogue.add("Hey I've got 2 wandies coming in.  Yeah, it's their scout group.");
                dialogue.add("That's one drink on me!");
                dialogue.add("I've got more scouts, Skip.  Wonder if they brought any BattleStars?");
                dialogue.add("Well that's it for the wandies.");
                dialogue.add("Wait!  I knew it.. 2 BattleStars.");
                dialogue.add("Stay clear!");
                dialogue.add("Not so much better than ours.");
                dialogue.add("Another bunch of pussies, Skip.");

                break;

            case SATURN:
                planetImage = Assets.saturn;
                musicString = "audio/saturn-projsol-04.wav";

                dialogue.add("Cronus 2, reporting in.  This is our finest hour, Commander!");
                dialogue.add("So these Jovian fools think they can stop us?");
                dialogue.add("Well, I suppose a little target practice is in order before we take on the Martians.");
                dialogue.add("Should anyone else decide to join the fray, let's make an example of them.");
                dialogue.add("Ah.  2 interceptors from above, Commander.");
                dialogue.add("A fitting end.");
                dialogue.add("More fools.  They could have just targeted our research station over Tethys.");
                dialogue.add("Cheers, commander.  No more interceptors.");
                dialogue.add("Oh, a few capital ships.  A bit of a challenge now.");
                dialogue.add("Don't get caught in that shockwave!");
                dialogue.add("Bigger, but not much stronger.");
                dialogue.add("Well they don't give up, I'll give 'em that much.");

                break;

            default:    // need to add training level
                planetImage = Assets.earth;
                musicString = "audio/earth-projsol-27.wav";

                dialogue.add("Terra 2 standing by.. here we go!");
                dialogue.add("It looks like we're up against one of their smaller interdiction groups.");
                dialogue.add("Not sure why they're trying to stop us, but too bad for them.");
                dialogue.add("And I have a feeling we're going to have other visitors too.");
                dialogue.add("Heads up!  I've got two fighters inbound.  It's the Mars 21st.");
                dialogue.add("Burn em!");
                dialogue.add("Two more, boss.  Man, I thought these guys would be going to Jupiter.");
                dialogue.add("That's it, no more fighters on my scope for now.");
                dialogue.add("But I'm picking up two corvettes.  Mars 4th battle group.");
                dialogue.add("Watch that blast!");
                dialogue.add("Weak cap ships.  Those cores suck.");
                dialogue.add("Damn, more of the 4th.  Go get it!");

                break;
        }

        // Load and play music
        Assets.resetMusic();
        Assets.loadMusic(musicString, true);
        Assets.resumeMusic();

        // Play launch & engine sound
        //launchStreamID = Assets.playSound(Assets.launchID, 0);
        engineStreamID = Assets.playSound(Assets.engineID, -1);

        // Init asteroids
        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < 4; i++) {
            Asteroid a = new Asteroid((float) i*250, (float) GameMainActivity.GAME_HEIGHT,
                    ASTEROID_WIDTH, ASTEROID_HEIGHT);
            asteroids.add(a);
        }

        // Init dual laser item
        dualLaser = new Item((float) GameMainActivity.GAME_WIDTH, (float) GameMainActivity.GAME_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT);

        // Init player and wingman
        player = new Player(256, 128, PLAYER_WIDTH, PLAYER_HEIGHT);
        wingman = new Player(192, 192, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerWingmanRect = new Rect((int)wingman.getX(), (int)player.getY(),
                (int)(player.getX()+PLAYER_WIDTH), (int)(wingman.getY()+PLAYER_HEIGHT));
        rectWidth = playerWingmanRect.right - playerWingmanRect.left;
        rectHeight = playerWingmanRect.bottom - playerWingmanRect.top;

        // Initialize enemy forces
        currentEnemyGroup = new ArrayList<Enemy>();
        enemyForces = new ArrayList<ArrayList<Enemy>>();
        ArrayList<Enemy> fighterGroup1 = new ArrayList<Enemy>();
        Enemy fighter1 = new Enemy(-100, 0, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy fighter2 = new Enemy(0, -100, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        fighterGroup1.add(fighter1);
        fighterGroup1.add(fighter2);

        ArrayList<Enemy> fighterGroup2 = new ArrayList<Enemy>();
        Enemy fighter3 = new Enemy(800, 550, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy fighter4 = new Enemy(900, 450, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        fighterGroup2.add(fighter3);
        fighterGroup2.add(fighter4);

        ArrayList<Enemy> capitalGroup1 = new ArrayList<Enemy>();
        Enemy capital1 = new Enemy(400, 550, CAPITAL_WIDTH, CAPITAL_HEIGHT, Enemy.EnemyType.CAPITAL);
        Enemy capital2 = new Enemy(400, -100, CAPITAL_WIDTH, CAPITAL_HEIGHT, Enemy.EnemyType.CAPITAL);
        capitalGroup1.add(capital1);
        capitalGroup1.add(capital2);

        ArrayList<Enemy> mixedGroup1 = new ArrayList<>();
        Enemy fighter5 = new Enemy(800, 550, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy fighter6 = new Enemy(900, 450, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy fighter7 = new Enemy(-100, 0, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy fighter8 = new Enemy(0, -100, ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
        Enemy capital3 = new Enemy(400, 550, CAPITAL_WIDTH, CAPITAL_HEIGHT, Enemy.EnemyType.CAPITAL);
        mixedGroup1.add(fighter5);
        mixedGroup1.add(fighter6);
        mixedGroup1.add(fighter7);
        mixedGroup1.add(fighter8);
        mixedGroup1.add(capital3);

        ArrayList<Enemy> fighterSquadron1 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Enemy f = new Enemy(50*(i+1), -50*(i+1), ENEMY_WIDTH, ENEMY_HEIGHT, Enemy.EnemyType.FIGHTER);
            fighterSquadron1.add(f);
        }

        ArrayList<Enemy> capitalGroup2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Enemy c = new Enemy(800 + i*50, 500 - i*50, CAPITAL_WIDTH, CAPITAL_HEIGHT, Enemy.EnemyType.CAPITAL);
            capitalGroup2.add(c);
        }

        enemyForces.add(fighterGroup1);
        enemyForces.add(fighterGroup2);
        enemyForces.add(capitalGroup1);
        enemyForces.add(mixedGroup1);
        enemyForces.add(fighterSquadron1);
        enemyForces.add(capitalGroup2);

        // Init chatter
        currentChatter = "";
        chatterTrack = 0;
        timerTrack = 0;

        // Init spacedust
        spaceDust = new ArrayList<Star>();
        for (int i = 0; i < 24; i++) {
            Star s = new Star(2);
            spaceDust.add(s);
        }

        // Init stars
        stars = new ArrayList<Star>();
        for (int i = 0; i < 64; i++) {
            Star c = new Star(1);
            stars.add(c);
        }

        // Init planet
        planet = new Planet(0,0);

        // Init current animations
        playerCurrentAnim = Assets.playerLevelAnim;
        wingmanCurrentAnim = Assets.wingmanLevelAnim;
        //enemyCurrentAnim = Assets.enemyLevelAnim;

        // Init pause/quit button
        pauseButton = new UIButton(672, 32, 720, 80, Assets.pause, Assets.pauseDown);
        quitButton = pauseButton;
        pauseRect = new Rect(672, 32, 720, 80);
        quitRect = pauseRect;
    }

    @Override
    public void onLoad() {
        Assets.loadPlayAssets();
    }

    @Override
    public void onExit() {

    }

    @Override
    public void update(float delta) {

        // Check this first b/c quit button just sets isAlive to false
        if (!player.isAlive()) {
            Assets.stopSound(engineStreamID);
            //Assets.stopSound(launchStreamID);
            Assets.stopMusic();
            setCurrentState(new LoadState(this, new GameOverState(playerScore)));
        }

        // If game is paused, do not update anything.
        if (gamePaused) {
            if (player.getFiringStatus()) {
                player.setFiringStatus(false);
            }

            return;
        }

        // Update stars
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            c.update(delta);
        }

        // Update planet
        if (planet.getVisible()) {
            planet.update(delta);
        }

        // Update spacedust
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            s.update(delta);
        }

        // need this
        if (activeEnemyGroup < enemyForces.size()) {
            currentEnemyGroup = enemyForces.get(activeEnemyGroup);
        }

        // Update ships
        checkFullRect(delta);
        playerScore += player.update(delta, asteroids, currentEnemyGroup);
        playerCurrentAnim.update(delta);

        int test = wingman.update(delta, asteroids, currentEnemyGroup);
        wingmanCurrentAnim.update(delta);

        // Update items and objects
        updateDualLaser(delta);
        //updateAsteroids(delta);

        // Increase asteroid speed when score exceeds 30
        if (playerScore > 30 && asteroidSpeed > -280) {
            asteroidSpeed -= 10;
            Asteroid.setVelX(asteroidSpeed);
        }

        // Update Enemies
        updateEnemies(delta);

        // Update timer
        if (++timer > 90000)
            timer = 0;

        if (timer >= DIALOGUE_DURATION) {
            updateChatter();
        }
    }

    private void updateEnemies(float delta) {
        if (activeEnemyGroup < enemyForces.size() && timer > DIALOGUE_DURATION*5) {
            for (int i = 0; i < currentEnemyGroup.size(); i++) {
                Enemy e = currentEnemyGroup.get(i);
                e.update(delta, asteroids, player, wingman);

                // going to change
                if (!e.getActive() && e.isAlive()) {
                    e.setActive(true);
                }
                if (!e.isAlive() && !e.isOnScreen()) {
                    e.setActive(false);
                    deactiveCount++;

                    if (e.getType() == Enemy.EnemyType.FIGHTER) {
                        chatterTrack = 5;
                    } else if (e.getType() == Enemy.EnemyType.CAPITAL) {
                        chatterTrack = 9;
                    }
                }

                // Update animation
                if (Math.abs(e.getVelY()) <= 70) {
                    e.setAnim(0);
                } else if (e.getVelY() > 70 && e.getVelY() <= 145) {
                    e.setAnim(-1);
                } else if (e.getVelY() > 145) {
                    e.setAnim(-2);
                } else if (e.getVelY() < -70 && e.getVelY() >= -145) {
                    e.setAnim(1);
                } else if (e.getVelY() < -145) {
                    e.setAnim(2);
                }
            }

            if (deactiveCount == currentEnemyGroup.size()) {
                activeEnemyGroup++;

                // must happen only when group is destroyed
                if (activeEnemyGroup == 1) {
                    chatterTrack = 6;
                } else if (activeEnemyGroup == 2) {
                    chatterTrack = 7;
                } else if (activeEnemyGroup == 3) {
                    chatterTrack = 10;
                }
            }

            deactiveCount = 0;  // needs to reset every frame
        }
    }

    private void checkFullRect(float delta) {
        float nextFullX = /*playerWingmanRect.left*/ wingman.getX() + wingman.getVelX() * delta;
        float nextFullY = /*playerWingmanRect.top*/ player.getY() + player.getVelY() * delta;

        if (nextFullX < 0) {
            wingman.setX(0);
            wingman.setVelX(0);
            player.setX(wingman.getX() + rectWidth - player.getWidth());
            player.setVelX(0);

        } else if (nextFullX > GameMainActivity.GAME_WIDTH - rectWidth) {
            wingman.setX(GameMainActivity.GAME_WIDTH - rectWidth);
            wingman.setVelX(0);
            player.setX(wingman.getX() + rectWidth - player.getWidth());
            player.setVelX(0);

        } /*else {
            playerWingmanRect.left = (int) nextFullX;
            playerWingmanRect.right = playerWingmanRect.left + rectWidth;
        }*/

        if (nextFullY < 0) {
            player.setY(0);
            player.setVelY(0);
            wingman.setY(player.getY() + rectHeight - wingman.getHeight());
            wingman.setVelY(0);

        } else if (nextFullY > GameMainActivity.GAME_HEIGHT - rectHeight) {
            player.setY(GameMainActivity.GAME_HEIGHT - rectHeight);
            player.setVelY(0);
            wingman.setY(player.getY() + rectHeight - wingman.getHeight());
            wingman.setVelY(0);

        } /*else {
            playerWingmanRect.top = (int) nextFullY;
            playerWingmanRect.bottom = playerWingmanRect.top + rectHeight;
        }*/
    }

    private void updateChatter() {
        if (timer >= DIALOGUE_DURATION + chatterTrack*DIALOGUE_DURATION) {
            if (dialogue.get(chatterTrack) != currentChatter) {
                currentChatter = dialogue.get(chatterTrack);
                timerTrack = timer;
            }
        }

        if (timer-timerTrack > DIALOGUE_DURATION &&
                (chatterTrack < 4 || chatterTrack > 6 && chatterTrack < 9 || chatterTrack > 9 && chatterTrack < 12)) {
            chatterTrack++;
        }
    }

    private void updateDualLaser(float delta) {
        dualLaser.checkItemAppear(player.getDual(), timer);
        dualLaser.update(delta);

        if (dualLaser.isVisible()) {
            if (Rect.intersects(dualLaser.getRect(), player.getRect())) {
                dualLaser.onCollide(player);
            }
        }
    }

    private void updateAsteroids(float delta) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            a.update(delta, asteroidSpeed);

            if (a.isVisible()) {
                // Box collision detection
                if (Rect.intersects(a.getRect(), player.getRect())) {
                    a.onCollide(player);
                }
            }
        }
    }

    @Override
    public void render(Painter g) {
        g.setColor(Color.rgb(4, 4, 16));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        // render order
        renderStars(g);
        renderPlanet(g);
        renderSpaceDust(g);
        renderPlayer(g);
        renderWingman(g);

        if (activeEnemyGroup < enemyForces.size()) {
            for (int i = 0; i < currentEnemyGroup.size(); i++) {
                Enemy e = currentEnemyGroup.get(i);

                if (e.getActive() && e.isAlive()) {   // eventually, don't need e.isAlive()
                    renderEnemy(g, e);
                }
                if (e.isOnScreen() && e.isAlive()) {
                    renderEnemyShield(g, e);
                }
            }
        }

        renderItems(g);
        renderAsteroids(g);
        renderScore(g);
        renderShield(g);
        //renderEnergy(g);
        renderChatter(g);

        // If game is Paused, draw additional UI elements:
        if (gamePaused) {
            // ARGB is used to set an ARGB color.
            g.setColor(Color.argb(160, 0, 0, 0));
            g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
            g.setColor(Color.LTGRAY);
            g.drawString(pausedString, 235, 240);

            quitButton.render(g);
        } else {
            // Draw pauseButton if not paused.
            pauseButton.render(g);
        }
    }

    private void renderChatter(Painter g) {
        //Assets.wingmanLevelAnim.render(g, 110, 420, wingman.getWidth(), wingman.getHeight());
        g.setColor(Color.LTGRAY);
        if (timer-timerTrack < currentChatter.length()) {
            g.drawString(currentChatter.substring(0, timer-timerTrack), 150, 430);
        } else if (timer-timerTrack < 90) {
            g.drawString(currentChatter, 150, 430);
        }
    }

    private void renderStars(Painter g) {
        for (int i = 0; i < stars.size(); i++) {
            Star c = stars.get(i);
            g.setColor(c.getColor());
            g.fillOval((int) c.getX(), (int) c.getY(), (int) c.getSize(), (int) c.getSize());
        }
    }

    private void renderPlanet(Painter g) {
        if (planet.getVisible()) {
            g.drawImage(planetImage, (int) planet.getX(), (int) planet.getY(),
                    planetImage.getWidth(), planetImage.getHeight());
        }
    }

    private void renderSpaceDust(Painter g) {
        for (int i = 0; i < spaceDust.size(); i++) {
            Star s = spaceDust.get(i);
            g.setColor(s.getColor());
            g.fillOval((int) s.getX(), (int) s.getY(), (int) s.getSize(), (int) s.getSize());
        }
    }

    private void renderPlayer(Painter g) {
        playerCurrentAnim.render(g, (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());
        player.renderWeapon(g);
    }

    private void renderWingman(Painter g) {
        wingmanCurrentAnim.render(g, (int) wingman.getX(), (int) wingman.getY(),
                wingman.getWidth(), wingman.getHeight());
        wingman.renderWeapon(g);
    }

    private void renderEnemy(Painter g, Enemy e) {
        // maybe some method that returns the correct animation from Assets
        Animation testCurrentAnim = Assets.getEnemyAnim(e.getType(), e.getAnim());

        testCurrentAnim.render(g, (int) e.getX(), (int) e.getY(), e.getWidth(), e.getHeight());
        e.renderWeapon(g);
    }

    private void renderItems(Painter g) {
        if (dualLaser.isVisible()) {
            g.drawImage(Assets.laserItem, (int) dualLaser.getX(), (int) dualLaser.getY(), 100, 100);
        }
    }

    private void renderAsteroids(Painter g) {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            if (a.isVisible()) {
                g.drawImage(Assets.asteroid, (int) a.getX(), (int) a.getY(), ASTEROID_WIDTH, ASTEROID_HEIGHT);
            }
        }
    }

    private void renderScore(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(Color.WHITE);

        String scoreString = Integer.toString(playerScore);
        g.drawString("SCORE: ", 30, 30);
        g.drawString(scoreString, 100, 30);
    }

    private void renderShield(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);

        g.setColor(Color.BLUE);
        g.drawString("SHIELD: ", 200, 30);
        g.fillRect(280, 15, player.getShield(), 15);
        g.setColor(Color.WHITE);
        g.fillRect(280, 15, 100, 1);
        g.fillRect(280, 30, 100, 1);
        g.fillRect(280, 15, 1, 15);
        g.fillRect(380, 15, 1, 15);

        g.setColor(Color.CYAN);
        g.drawString("WINGMAN: ", 170, 55);
        g.fillRect(280, 40, wingman.getShield(), 15);
        g.setColor(Color.WHITE);
        g.fillRect(280, 40, 100, 1);
        g.fillRect(280, 55, 100, 1);
        g.fillRect(280, 40, 1, 15);
        g.fillRect(380, 40, 1, 15);
    }

    private void renderEnergy(Painter g) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(Color.GREEN);

        g.drawString("ENERGY: ", 430, 30);
        if (player.getEnergy() > 0) {
            g.fillRect(510, 15, player.getEnergy() / 8, 15);
        }

        g.setColor(Color.WHITE);
        g.fillRect(510, 15, 100, 1);
        g.fillRect(510, 30, 100, 1);
        g.fillRect(510, 15, 1, 15);
        g.fillRect(610, 15, 1, 15);
    }

    private void renderEnemyShield(Painter g, Enemy e) {
        g.setFont(Typeface.DEFAULT_BOLD, 18);
        g.setColor(Color.RED);
        g.fillRect((int) e.getX(), (int) e.getY() + e.getHeight() + 10, e.getShield() / (e.getMaxShield() / 50), 10);

        g.setColor(Color.WHITE);
        g.fillRect((int) e.getX(), (int) e.getY() + e.getHeight() + 10, 50, 1);
        g.fillRect((int) e.getX(), (int) e.getY() + e.getHeight() + 20, 50, 1);
        g.fillRect((int) e.getX(), (int) e.getY() + e.getHeight() + 10, 1, 10);
        g.fillRect((int) e.getX() + 50, (int) e.getY() + e.getHeight() + 10, 1, 10);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Call onTouchDown for quit button
            if (gamePaused) {
                quitButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            // Call onTouchDown for pause button, and return if pressed
            pauseButton.onTouchDown(scaledX, scaledY);
            if (pauseButton.isPressed(scaledX, scaledY)) {
                return true;
            }

            // Store touch location
            recentTouchY = scaledY;
            recentTouchX = scaledX; // new

            // Player is firing
            //player.setFiringStatus(true);

        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // Return if paused
            if (gamePaused) {
                quitButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            // If not paused, only return if the pause button was pressed
            // This means that user pressed the pause button but moved off it
            if (pauseButton.wasHit()) {
                pauseButton.onTouchDown(scaledX, scaledY);
                return true;
            }

            // To add some simple acceleration physics, use delta values
            // between touches to change velocity, not just position..
            int deltaY = scaledY - (int) recentTouchY;
            int deltaX = scaledX - (int) recentTouchX;

            player.maneuver(deltaY, deltaX);
            wingman.maneuver(deltaY, deltaX);   // let's see
            //Log.d("dY", Integer.toString(deltaY));
            //Log.d("dX", Integer.toString(deltaX));

            // Change sprite (animation) based on Y velocity to give the appearance of movement
            if (Math.abs(player.getVelY()) <= /*8*/ 70)
                playerCurrentAnim = Assets.playerLevelAnim;
            else if (player.getVelY() > /*8*/ 70 && player.getVelY() <= /*32*/ 145)
                playerCurrentAnim = Assets.playerDownOneAnim;
            else if (player.getVelY() > /*32*/ 145)
                playerCurrentAnim = Assets.playerDownTwoAnim;
            else if (player.getVelY() < /*-8*/ -70 && player.getVelY() >= /*-32*/ -145)
                playerCurrentAnim = Assets.playerUpOneAnim;
            else if (player.getVelY() < /*-32*/ -145)
                playerCurrentAnim = Assets.playerUpTwoAnim;

            if (Math.abs(wingman.getVelY()) <= 70)
                wingmanCurrentAnim = Assets.wingmanLevelAnim;
            else if (wingman.getVelY() > 70 && wingman.getVelY() <= 145)
                wingmanCurrentAnim = Assets.wingmanDownOneAnim;
            else if (wingman.getVelY() > 145)
                wingmanCurrentAnim = Assets.wingmanDownTwoAnim;
            else if (wingman.getVelY() < -70 && wingman.getVelY() >= -145)
                wingmanCurrentAnim = Assets.wingmanUpOneAnim;
            else if (wingman.getVelY() < -145)
                wingmanCurrentAnim = Assets.wingmanUpTwoAnim;

            recentTouchY = scaledY;
            recentTouchX = scaledX;

        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            // Resume game if paused.
            if (gamePaused) {
                // If touch up triggers quit button, quit game
                if (quitButton.isPressed(scaledX, scaledY)) {
                    quitButton.cancel();
                /*}

                // Quit game if quit rect is hit
                if (quitRect.intersects(scaledX, scaledY, scaledX, scaledY)) {*/
                    player.setAlive(false);
                    return true;
                } else if (quitButton.wasHit()) {
                    // If quit button was pressed but user moved off it, return and don't unpause game
                    quitButton.cancel();
                    return true;
                } else {
                    // Quit button was never pressed
                    quitButton.cancel();
                }

                // Unpause game
                gamePaused = false;

                // Must cancel pauseButton before onTouch() method returns.
                pauseButton.cancel();

                // Resume audio
                Assets.resumeMusic();
                //Assets.resumeSound(launchStreamID);
                Assets.resumeSound(engineStreamID);

                return true;
            }

            // If Touch Up triggers PauseButton, pause the game.
            if (pauseButton.isPressed(scaledX, scaledY)) {
                pauseButton.cancel();

                // Pause audio
                Assets.pauseSound(engineStreamID);
                //Assets.pauseSound(launchStreamID);
                Assets.pauseMusic();

                // Pause game
                gamePaused = true;

                return true;

            } else if (pauseButton.wasHit()) {
                // If pause button was pressed but user moved off it, return and don't pause game
                pauseButton.cancel();
                return true;

            } else {
                // Pause button was never hit
                pauseButton.cancel();
            }

            // Stop firing
            if (player.getFiringStatus())
                player.setFiringStatus(false);
        }

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        player.setFiringStatus(true);
        return true;
    }

    // Overrides onPause() from State.
    // Called when Activity is pausing.
    @Override
    public void onPause() {
        Assets.stopSound(engineStreamID);
        //Assets.stopSound(launchStreamID);
        Assets.stopMusic();

        gamePaused = true;
    }

    @Override
    public void onResume() {
        Assets.loadMusic(musicString, true);
        Assets.playSound(Assets.engineID, -1);
    }

}