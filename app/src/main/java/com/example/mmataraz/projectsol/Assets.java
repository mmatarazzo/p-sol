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
import android.util.Log;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.animation.Frame;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 1/30/2015.
 */
public class Assets {

    // audio
    private static SoundPool soundPool;
    public static int hitID, fireID, destroyID;
    private static float fxVolume = 0.75f;

    // music
    private static MediaPlayer mediaPlayer;
    private static int mediaPosition = 0;

    // splash and ui buttons
    public static Bitmap welcome, begin, beginDown, options, optionsDown;

    // gameplay objects and background
    public static Bitmap earth, mars, asteroid;
    public static Bitmap level, upOne, upTwo, downOne, downTwo, laserItem;

    // animations
    public static Animation levelAnim, upOneAnim, upTwoAnim, downOneAnim, downTwoAnim;

    public static void loadMenuAssets() {
        // splash
        welcome = loadBitmap("welcome.png", false);
        begin = loadBitmap("begin_button.png", true);
        beginDown = loadBitmap("begin_button_down.png", true);
        options = loadBitmap("options_button.png", true);
        optionsDown = loadBitmap("options_button_down.png", true);

        // level select
        earth = loadBitmap("earth-new.png", true);
        mars = loadBitmap("mars.png", true);
    }

    public static void loadPlayAssets() {
        // gameplay objects and background
        //earth = loadBitmap("earth-new.png", true);    // a different backdrop would go here
        //mars = loadBitmap("mars.png", true);          // a different backdrop would go here
        asteroid = loadBitmap("asteroid1.png", false);
        // more bitmaps for asteroid frames

        // ship and lasers
        level = loadBitmap("arwing-straight.png", true);
        upOne = loadBitmap("arwing-leftOne.png", true);
        upTwo = loadBitmap("arwing-leftTwo.png", true);
        downOne = loadBitmap("arwing-rightOne.png", true);
        downTwo = loadBitmap("arwing-rightTwo.png", true);
        laserItem = loadBitmap("laser_big.png", true);
        // more bitmaps for ship and laser frames

        // animations
        Frame lvl = new Frame(level, .9f);
        Frame up1 = new Frame(upOne, .9f);
        Frame up2 = new Frame(upTwo, .9f);
        Frame down1 = new Frame(downOne, .9f);
        Frame down2 = new Frame(downTwo, .9f);
        // more frames here for ship, lasers and explosion

        levelAnim = new Animation(true, lvl);
        upOneAnim = new Animation(true, up1);
        upTwoAnim = new Animation(true, up2);
        downOneAnim = new Animation(true, down1);
        downTwoAnim = new Animation(true, down2);
        // more animations for ship, lasers and explosion
    }

    public static void unloadMenuAssets() {
        // unload menu-only bitmaps
        unloadBitmap(welcome);
        unloadBitmap(begin);
        unloadBitmap(beginDown);
        unloadBitmap(options);
        unloadBitmap(optionsDown);
    }

    public static void unloadPlayAssets() {
        // unload play state assets
        unloadBitmap(earth);
        unloadBitmap(mars);
        unloadBitmap(asteroid);
        unloadBitmap(level);
        unloadBitmap(upOne);
        unloadBitmap(upTwo);
        unloadBitmap(downOne);
        unloadBitmap(downTwo);
        unloadBitmap(laserItem);
    }

    public static void onResume() {
        // load sounds
        hitID = loadSound("hit.wav");
        fireID = loadSound("laser2.wav");
        destroyID = loadSound("explode.wav");
    }

    public static void onPause() {
        // release sounds
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
            options.inPreferredConfig = Config.ARGB_8888;
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

    public static void playSound(int soundID) {
        if (soundPool != null) {
            soundPool.play(soundID, fxVolume, fxVolume, 1, 0, 1);
        }
    }

    public static void playMusic(String filename, boolean looping) {
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

            mediaPlayer.start();
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
    public static boolean resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            return true;
        }
        else
            return false;
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

    public static void updateVolumes(int vol) {
        fxVolume = (vol - 50) / 200.0f;
        //Log.d("Volume", Float.toString(fxVolume));
    }

    public static float getFxVolume() {
        return fxVolume;
    }

}