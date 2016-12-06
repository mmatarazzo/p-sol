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

import java.io.IOException;
import java.io.InputStream;

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

    // Planet, ship and object bitmaps
    public static Bitmap earth, mars, saturn;
    public static Bitmap playerLevel, playerUpOne, playerUpTwo, playerDownOne, playerDownTwo;
    public static Bitmap wingmanLevel, wingmanUpOne, wingmanUpTwo, wingmanDownOne, wingmanDownTwo;
    public static Bitmap enemyLevel, enemyUpOne, enemyUpTwo, enemyDownOne, enemyDownTwo;
    public static Bitmap asteroid, laserItem;

    // Animations
    public static Animation playerLevelAnim, playerUpOneAnim, playerUpTwoAnim, playerDownOneAnim, playerDownTwoAnim;
    public static Animation wingmanLevelAnim, wingmanUpOneAnim, wingmanUpTwoAnim, wingmanDownOneAnim, wingmanDownTwoAnim;
    public static Animation enemyLevelAnim, enemyUpOneAnim, enemyUpTwoAnim, enemyDownOneAnim, enemyDownTwoAnim;

    public static void loadMenuAssets() {
        // splash screen bitmaps
        welcome = loadBitmap("welcome.png", false);
        begin = loadBitmap("begin_button.png", true);
        beginDown = loadBitmap("begin_button_down.png", true);
        options = loadBitmap("options_button.png", true);
        optionsDown = loadBitmap("options_button_down.png", true);

        // level select bitmaps
        earth = loadBitmap("earth-new.png", true);
        mars = loadBitmap("mars.png", true);
        saturn = loadBitmap("saturn.png", true);
    }

    public static void loadPlayAssets() {
        // pause button bitmaps
        pause = loadBitmap("pause_button.png", true);
        pauseDown = loadBitmap("pause_button_down.png", true);

        // object bitmaps
        asteroid = loadBitmap("asteroid-psol.png", false);
        // more bitmaps for future asteroid frames
        laserItem = loadBitmap("laser-upgrade.png", true);

        //earth = loadBitmap("earth-new.png", true);    // a different backdrop would go here
        //mars = loadBitmap("mars.png", true);          // a different backdrop would go here
        //saturn = loadBitmap("saturn.png", true);      // a different backdrop would go here

        // player ship bitmaps
        playerLevel = loadBitmap("knight/knight-level.png", true);
        playerUpOne = loadBitmap("knight/knight-upone.png", true);
        playerUpTwo = loadBitmap("knight/knight-uptwo.png", true);
        playerDownOne = loadBitmap("knight/knight-downone.png", true);
        playerDownTwo = loadBitmap("knight/knight-downtwo.png", true);

        // wingman ship bitmaps
        wingmanLevel = loadBitmap("arwing/arwing-straight.png", true);
        wingmanUpOne = loadBitmap("arwing/arwing-leftOne.png", true);
        wingmanUpTwo = loadBitmap("arwing/arwing-leftTwo.png", true);
        wingmanDownOne = loadBitmap("arwing/arwing-rightOne.png", true);
        wingmanDownTwo = loadBitmap("arwing/arwing-rightTwo.png", true);

        // enemy ship bitmaps
        enemyLevel = loadBitmap("defender/defender-level-enemy.png", true);
        enemyUpOne = loadBitmap("defender/defender-upOne-enemy.png", true);
        enemyUpTwo = loadBitmap("defender/defender-upTwo-enemy.png", true);
        enemyDownOne = loadBitmap("defender/defender-downOne-enemy.png", true);
        enemyDownTwo = loadBitmap("defender/defender-downTwo-enemy.png", true);

        // more bitmaps for other ships, lasers, explosions etc..

        // player ship frames
        Frame playerLvl = new Frame(playerLevel, .9f);
        Frame playerUp1 = new Frame(playerUpOne, .9f);
        Frame playerUp2 = new Frame(playerUpTwo, .9f);
        Frame playerDn1 = new Frame(playerDownOne, .9f);
        Frame playerDn2 = new Frame(playerDownTwo, .9f);

        // wingman ship frames
        Frame wingmanLvl = new Frame(wingmanLevel, .9f);
        Frame wingmanUp1 = new Frame(wingmanUpOne, .9f);
        Frame wingmanUp2 = new Frame(wingmanUpTwo, .9f);
        Frame wingmanDn1 = new Frame(wingmanDownOne, .9f);
        Frame wingmanDn2 = new Frame(wingmanDownTwo, .9f);

        // enemy ship frames
        Frame enemyLvl = new Frame(enemyLevel, .9f);
        Frame enemyUp1 = new Frame(enemyUpOne, .9f);
        Frame enemyUp2 = new Frame(enemyUpTwo, .9f);
        Frame enemyDn1 = new Frame(enemyDownOne, .9f);
        Frame enemyDn2 = new Frame(enemyDownTwo, .9f);

        // more frames for other ships, lasers, explosions etc..

        // player ship animations
        playerLevelAnim = new Animation(true, playerLvl);
        playerUpOneAnim = new Animation(true, playerUp1);
        playerUpTwoAnim = new Animation(true, playerUp2);
        playerDownOneAnim = new Animation(true, playerDn1);
        playerDownTwoAnim = new Animation(true, playerDn2);

        // wingman ship animations
        wingmanLevelAnim = new Animation(true, wingmanLvl);
        wingmanUpOneAnim = new Animation(true, wingmanUp1);
        wingmanUpTwoAnim = new Animation(true, wingmanUp2);
        wingmanDownOneAnim = new Animation(true, wingmanDn1);
        wingmanDownTwoAnim = new Animation(true, wingmanDn2);

        // enemy ship animations
        enemyLevelAnim = new Animation(true, enemyLvl);
        enemyUpOneAnim = new Animation(true, enemyUp1);
        enemyUpTwoAnim = new Animation(true, enemyUp2);
        enemyDownOneAnim = new Animation(true, enemyDn1);
        enemyDownTwoAnim = new Animation(true, enemyDn2);

        // more animations for other ships, lasers, explosions etc..
    }

    public static void unloadMenuAssets() {
        // unload menu-only bitmaps
        //unloadBitmap(welcome);
        unloadBitmap(begin);
        unloadBitmap(beginDown);
        unloadBitmap(options);
        unloadBitmap(optionsDown);
    }

    public static void unloadPlayAssets() {
        // unload play state assets
        unloadBitmap(welcome);  // also game over screen
        unloadBitmap(pause);
        unloadBitmap(pauseDown);

        unloadBitmap(earth);
        unloadBitmap(mars);
        unloadBitmap(saturn);

        unloadBitmap(asteroid);
        unloadBitmap(laserItem);

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
    }

    public static void onResume() {
        // load sounds
        hitID = loadSound("hit.wav");
        fireID = loadSound("laser-03.wav");
        destroyID = loadSound("explode.wav");
        launchID = loadSound("launch-02.wav");
        engineID = loadSound("engines-hum-02.wav");

        // load multi-state bitmaps
        //welcome = loadBitmap("welcome.png", false);
        //earth = loadBitmap("earth-new.png", true);
        //mars = loadBitmap("mars.png", true);
    }

    public static void onPause() {
        // release sounds
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        // release multi-state bitmaps
        //unloadBitmap(welcome);
        //unloadBitmap(earth);
        //unloadBitmap(mars);
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
            options.inPreferredConfig = /*Config.ARGB_8888*/ Config.ARGB_4444;
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