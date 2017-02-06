package com.example.mmataraz.projectsol;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.animation.Frame;
import com.example.mmataraz.game.model.Enemy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Mike on 1/30/2015.
 */
public class Assets {

    // Sound effects
    private static SoundPool soundPool;
    public static int launchID, engineID;
    public static int hitID, fireID, destroyID;
    private static float fxVolume = 0.5f;

    // Music
    private static MediaPlayer mediaPlayer;
    private static int mediaPosition = 0;

    // Splash screen and pause button bitmaps
    public static Bitmap welcome, begin, beginDown, options, optionsDown, pause, pauseDown;
    public static Bitmap start, credits, mainGame, training, score;
    public static Bitmap mapScreen, levelBracket, levelBracketDown, levelSelected;
    public static Bitmap earthBrief, marsBrief, saturnBrief;

    // Planet, ship and object bitmaps
    public static Bitmap earth, mars, saturn;
    //public static Bitmap[] player, wingman, enemyFighter, capitalShip;
    public static Bitmap playerLevel, playerUpOne, playerUpTwo, playerDownOne, playerDownTwo;
    public static Bitmap wingmanLevel, wingmanUpOne, wingmanUpTwo, wingmanDownOne, wingmanDownTwo;
    public static Bitmap enemyLevel, enemyUpOne, enemyUpTwo, enemyDownOne, enemyDownTwo;
    public static Bitmap capitalLevel, capitalUpOne, capitalUpTwo, capitalDownOne, capitalDownTwo;
    public static Bitmap asteroid, laserItem, laserBeam, enemyLaser;

    // Animations
    //public static Animation[] playerAnim, wingmanAnim, enemyFighterAnim, capitalShipAnim;
    public static Animation playerLevelAnim, playerUpOneAnim, playerUpTwoAnim, playerDownOneAnim, playerDownTwoAnim;
    public static Animation wingmanLevelAnim, wingmanUpOneAnim, wingmanUpTwoAnim, wingmanDownOneAnim, wingmanDownTwoAnim;
    public static Animation enemyLevelAnim, enemyUpOneAnim, enemyUpTwoAnim, enemyDownOneAnim, enemyDownTwoAnim;
    public static Animation capitalLevelAnim, capitalUpOneAnim, capitalUpTwoAnim, capitalDownOneAnim, capitalDownTwoAnim;

    // Dialogue
    public static ArrayList<String> earthDialogue, marsDialogue, saturnDialogue, trainingDialogue;

    public static void loadMenuAssets() {
        // Splash screen bitmaps
        welcome = loadBitmap("backgrounds/welcome.png", false);
        begin = loadBitmap("buttons/begin_button.png", true);
        beginDown = loadBitmap("buttons/begin_button_down.png", true);
        options = loadBitmap("buttons/options_button.png", true);
        optionsDown = loadBitmap("buttons/options_button_down.png", true);

        start = loadBitmap("buttons/start.png", true);
        credits = loadBitmap("buttons/credits.png", true);
        mainGame = loadBitmap("buttons/main-game.png", true);
        training = loadBitmap("buttons/training.png", true);
        score = loadBitmap("buttons/score.png", true);
        options = loadBitmap("buttons/options.png", true);

        // Map and briefing bitmaps
        mapScreen = loadBitmap("backgrounds/juno-conflict-map-01.png", true);
        levelBracket = loadBitmap("buttons/level-bracket.png", true);
        levelBracketDown = loadBitmap("buttons/level-bracket-pressed.png", true);
        levelSelected = loadBitmap("buttons/level-selected.png", true);

        // Level background bitmaps
        earthBrief = loadBitmap("backgrounds/earth-briefing-01.png", true);
        marsBrief = loadBitmap("backgrounds/mars-briefing-01.png", true);
        saturnBrief = loadBitmap("backgrounds/saturn-briefing-01.png", true);
    }

    public static void loadPlayAssets() {
        // Backdrops
        //earth = loadBitmap("backgrounds/earth-level-01.png", true);
        earth = loadBitmap("backgrounds/earth_hd.png", true);
        mars = loadBitmap("backgrounds/mars-level-01.png", true);
        saturn = loadBitmap("backgrounds/saturn-level-01.png", true);

        // Pause button bitmaps
        pause = loadBitmap("buttons/pause_button.png", true);
        pauseDown = loadBitmap("buttons/pause_button_down.png", true);

        // Object bitmaps
        asteroid = loadBitmap("objects/asteroid-psol.png", false);
        // More bitmaps for future asteroid frames
        laserItem = loadBitmap("objects/laser-upgrade.png", true);
        laserBeam = loadBitmap("objects/laserBeam-01.png", true);
        enemyLaser = loadBitmap("objects/laserBeam-02.png", true);

        // Player ship bitmaps
        playerLevel = loadBitmap("knight/knight-level.png", true);
        playerUpOne = loadBitmap("knight/knight-upone.png", true);
        playerUpTwo = loadBitmap("knight/knight-uptwo.png", true);
        playerDownOne = loadBitmap("knight/knight-downone.png", true);
        playerDownTwo = loadBitmap("knight/knight-downtwo.png", true);

        /*player[0] = loadBitmap("knight/knight-uptwo.png", true);
        player[1] = loadBitmap("knight/knight-upone.png", true);
        player[2] = loadBitmap("knight/knight-level.png", true);
        player[3] = loadBitmap("knight/knight-downone.png", true);
        player[4] = loadBitmap("knight/knight-downtwo.png", true);*/

        // Wingman ship bitmaps
        wingmanLevel = loadBitmap("arwing/arwing-straight.png", true);
        wingmanUpOne = loadBitmap("arwing/arwing-leftOne.png", true);
        wingmanUpTwo = loadBitmap("arwing/arwing-leftTwo.png", true);
        wingmanDownOne = loadBitmap("arwing/arwing-rightOne.png", true);
        wingmanDownTwo = loadBitmap("arwing/arwing-rightTwo.png", true);

        /*wingman[0] = loadBitmap("arwing/arwing-leftTwo.png", true);
        wingman[1] = loadBitmap("arwing/arwing-leftOne.png", true);
        wingman[2] = loadBitmap("arwing/arwing-straight.png", true);
        wingman[3] = loadBitmap("arwing/arwing-rightOne.png", true);
        wingman[4] = loadBitmap("arwing/arwing-rightTwo.png", true);*/

        // Enemy fighter bitmaps
        enemyLevel = loadBitmap("defender/defender-level-enemy.png", true);
        enemyUpOne = loadBitmap("defender/defender-upOne-enemy.png", true);
        enemyUpTwo = loadBitmap("defender/defender-upTwo-enemy.png", true);
        enemyDownOne = loadBitmap("defender/defender-downOne-enemy.png", true);
        enemyDownTwo = loadBitmap("defender/defender-downTwo-enemy.png", true);

        /*enemyFighter[0] = loadBitmap("defender/defender-upTwo-enemy.png", true);
        enemyFighter[1] = loadBitmap("defender/defender-upOne-enemy.png", true);
        enemyFighter[2] = loadBitmap("defender/defender-level-enemy.png", true);
        enemyFighter[3] = loadBitmap("defender/defender-downOne-enemy.png", true);
        enemyFighter[4] = loadBitmap("defender/defender-downTwo-enemy.png", true);*/

        // Capital ship bitmaps
        capitalLevel = loadBitmap("battleStar/battleStar-lvl.png", true);
        capitalUpOne = loadBitmap("battleStar/battleStar-up1.png", true);
        capitalUpTwo = loadBitmap("battleStar/battleStar-up2.png", true);
        capitalDownOne = loadBitmap("battleStar/battleStar-dn1.png", true);
        capitalDownTwo = loadBitmap("battleStar/battleStar-dn2.png", true);

        /*capitalShip[0] = loadBitmap("battleStar/battleStar-up2.png", true);
        capitalShip[1] = loadBitmap("battleStar/battleStar-up1.png", true);
        capitalShip[2] = loadBitmap("battleStar/battleStar-lvl.png", true);
        capitalShip[3] = loadBitmap("battleStar/battleStar-dn1.png", true);
        capitalShip[4] = loadBitmap("battleStar/battleStar-dn2.png", true);*/

        // More bitmaps for other ships, lasers, explosions etc..

        // Player ship frames
        Frame playerLvl = new Frame(playerLevel, .9f);
        Frame playerUp1 = new Frame(playerUpOne, .9f);
        Frame playerUp2 = new Frame(playerUpTwo, .9f);
        Frame playerDn1 = new Frame(playerDownOne, .9f);
        Frame playerDn2 = new Frame(playerDownTwo, .9f);

        // Wingman ship frames
        Frame wingmanLvl = new Frame(wingmanLevel, .9f);
        Frame wingmanUp1 = new Frame(wingmanUpOne, .9f);
        Frame wingmanUp2 = new Frame(wingmanUpTwo, .9f);
        Frame wingmanDn1 = new Frame(wingmanDownOne, .9f);
        Frame wingmanDn2 = new Frame(wingmanDownTwo, .9f);

        // Enemy fighter frames
        Frame enemyLvl = new Frame(enemyLevel, .9f);
        Frame enemyUp1 = new Frame(enemyUpOne, .9f);
        Frame enemyUp2 = new Frame(enemyUpTwo, .9f);
        Frame enemyDn1 = new Frame(enemyDownOne, .9f);
        Frame enemyDn2 = new Frame(enemyDownTwo, .9f);

        // Capital ship frames
        Frame capitalLvl = new Frame(capitalLevel, .9f);
        Frame capitalUp1 = new Frame(capitalUpOne, .9f);
        Frame capitalUp2 = new Frame(capitalUpTwo, .9f);
        Frame capitalDn1 = new Frame(capitalDownOne, .9f);
        Frame capitalDn2 = new Frame(capitalDownTwo, .9f);

        // More frames for other ships, lasers, explosions etc..

        // Player ship animations
        playerLevelAnim = new Animation(true, playerLvl);
        playerUpOneAnim = new Animation(true, playerUp1);
        playerUpTwoAnim = new Animation(true, playerUp2);
        playerDownOneAnim = new Animation(true, playerDn1);
        playerDownTwoAnim = new Animation(true, playerDn2);

        // Wingman ship animations
        wingmanLevelAnim = new Animation(true, wingmanLvl);
        wingmanUpOneAnim = new Animation(true, wingmanUp1);
        wingmanUpTwoAnim = new Animation(true, wingmanUp2);
        wingmanDownOneAnim = new Animation(true, wingmanDn1);
        wingmanDownTwoAnim = new Animation(true, wingmanDn2);

        // Enemy ship animations
        enemyLevelAnim = new Animation(true, enemyLvl);
        enemyUpOneAnim = new Animation(true, enemyUp1);
        enemyUpTwoAnim = new Animation(true, enemyUp2);
        enemyDownOneAnim = new Animation(true, enemyDn1);
        enemyDownTwoAnim = new Animation(true, enemyDn2);

        // Capital ship animations
        capitalLevelAnim = new Animation(true, capitalLvl);
        capitalUpOneAnim = new Animation(true, capitalUp1);
        capitalUpTwoAnim = new Animation(true, capitalUp2);
        capitalDownOneAnim = new Animation(true, capitalDn1);
        capitalDownTwoAnim = new Animation(true, capitalDn2);

        // More animations for other ships, lasers, explosions etc..
    }

    public static void unloadMenuAssets() {
        // Unload menu-only bitmaps
        unloadBitmap(begin);
        unloadBitmap(beginDown);
        unloadBitmap(options);
        unloadBitmap(optionsDown);

        unloadBitmap(mapScreen);
        unloadBitmap(levelBracket);
        unloadBitmap(levelBracketDown);
        unloadBitmap(levelSelected);

        unloadBitmap(earthBrief);
        unloadBitmap(marsBrief);
        unloadBitmap(saturnBrief);
    }

    public static void unloadPlayAssets() {
        // Unload play state assets
        unloadBitmap(welcome);  // also game over screen

        unloadBitmap(pause);
        unloadBitmap(pauseDown);

        unloadBitmap(earth);
        unloadBitmap(mars);
        unloadBitmap(saturn);

        unloadBitmap(asteroid);
        unloadBitmap(laserItem);
        unloadBitmap(laserBeam);

        unloadBitmap(playerLevel);
        unloadBitmap(playerUpOne);
        unloadBitmap(playerUpTwo);
        unloadBitmap(playerDownOne);
        unloadBitmap(playerDownTwo);

        unloadBitmap(wingmanLevel);
        unloadBitmap(wingmanUpOne);
        unloadBitmap(wingmanUpTwo);
        unloadBitmap(wingmanDownOne);
        unloadBitmap(wingmanDownTwo);

        unloadBitmap(enemyLevel);
        unloadBitmap(enemyUpOne);
        unloadBitmap(enemyUpTwo);
        unloadBitmap(enemyDownOne);
        unloadBitmap(enemyDownTwo);

        unloadBitmap(capitalLevel);
        unloadBitmap(capitalUpOne);
        unloadBitmap(capitalUpTwo);
        unloadBitmap(capitalDownOne);
        unloadBitmap(capitalDownTwo);
    }

    // Could make this cleaner by using arrays
    public static Animation getEnemyAnim(Enemy.EnemyType type, int anim) {
        if (type == Enemy.EnemyType.CAPITAL) {    // cap ship
            switch (anim) {
                case 2:
                    return capitalUpTwoAnim;
                case 1:
                    return capitalUpOneAnim;
                case 0:
                    return capitalLevelAnim;
                case -1:
                    return capitalDownOneAnim;
                case -2:
                    return capitalDownTwoAnim;
                default:
                    return capitalLevelAnim;
            }
        } else if (type == Enemy.EnemyType.FIGHTER) { // fighter
            switch (anim) {
                case 2:
                    return enemyUpTwoAnim;
                case 1:
                    return enemyUpOneAnim;
                case 0:
                    return enemyLevelAnim;
                case -1:
                    return enemyDownOneAnim;
                case -2:
                    return enemyDownTwoAnim;
                default:
                    return enemyLevelAnim;
            }
        }

        return enemyLevelAnim;
    }

    public static void onResume() {
        // Load sounds
        hitID = loadSound("audio/hit.wav");
        fireID = loadSound("audio/laser-discharge-03.wav");
        destroyID = loadSound("audio/explode.wav");
        //launchID = loadSound("launch-02.wav");
        engineID = loadSound("audio/engines-hum-02.wav");
    }

    public static void onPause() {
        // Release sounds
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private static Bitmap loadBitmap(String filename, Boolean transparency) {
        InputStream inputStream = null;
        try {
            inputStream = GameMainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Options options = new Options();
        if (transparency) {
            options.inPreferredConfig = Config.ARGB_8888 /*Config.ARGB_4444*/;
        } else {
            options.inPreferredConfig = Config.RGB_565;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, /*new Options()*/ options);
        return bitmap;
    }

    // from Vichy97
    private static void unloadBitmap(Bitmap bitmap) {
        bitmap.recycle();
        bitmap = null;
    }

    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            //soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
            buildSoundPool();
        }
        try {
            soundID = soundPool.load(GameMainActivity.assets.openFd(filename), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return soundID;
    }

    // from Vichy97
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static SoundPool buildSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(25)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }

        return soundPool;
    }

    public static int playSound(int soundID, int loop) {
        if (soundPool != null) {
            return soundPool.play(soundID, fxVolume, fxVolume, 1, loop, 1);
        }

        return 0;
    }
    public static void pauseSound(int streamID) {
        if (soundPool != null) {
            soundPool.pause(streamID);
        }
    }

    public static void resumeSound(int streamID) {
        if (soundPool != null) {
            soundPool.resume(streamID);
        }
    }

    public static void stopSound(int streamID) {
        if (soundPool != null) {
            soundPool.stop(streamID);
        }
    }

    public static void loadMusic(String filename, boolean looping) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            AssetFileDescriptor afd = GameMainActivity.assets.openFd(filename);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(looping);

            // if music was stopped previously, return to last location
            mediaPlayer.seekTo(mediaPosition);  // test

            //mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // might need eventually
    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // might need eventually
    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            // save the position before releasing resources
            mediaPosition = mediaPlayer.getCurrentPosition();   // test

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void resetMusic() {
        mediaPosition = 0;
    }

    public static int getMusicPosition() {
        return mediaPosition;
    }

    public static void updateVolumes(int vol) {
        fxVolume = (vol - 50) / 200.0f;
        //Log.d("Volume", Float.toString(fxVolume));
    }

    public static float getFxVolume() {
        return fxVolume;
    }

}