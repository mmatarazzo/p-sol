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

    private static MediaPlayer mediaPlayer;
    private static int mediaPosition = 0;

    private static float fxVolume = 0.75f;

    // splash
    public static Bitmap welcome, begin, beginDown, options, optionsDown;

    // gameplay objects and background
    public static Bitmap earth, mars, asteroid;
    /*public static Bitmap earthOne, earthTwo, earthThree, earthFour, earthFive, earthSix,
            earthSeven, earthEight, earthNine, earthTen, earthEleven, earthTwelve;*/
    public static Bitmap /*ship1, ship2, ship3,*/ level, upOne, upTwo, downOne, downTwo, laserItem;

    // animations
    //public static Animation earthAnim;
    public static Animation /*shipAnim,*/ levelAnim, upOneAnim, upTwoAnim, downOneAnim, downTwoAnim;

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

        // ship and weapons
        /*ship1 = loadBitmap("shiptest_anim1.png", true);
        ship2 = loadBitmap("shiptest_anim2.png", true);
        ship3 = loadBitmap("shiptest_anim3.png", true);*/

        level = loadBitmap("arwing-straight.png", true);
        upOne = loadBitmap("arwing-leftOne.png", true);
        upTwo = loadBitmap("arwing-leftTwo.png", true);
        downOne = loadBitmap("arwing-rightOne.png", true);
        downTwo = loadBitmap("arwing-rightTwo.png", true);
        laserItem = loadBitmap("laser_big.png", true);

        // animations
        //loadEarthAnim();

        /*Frame sf1 = new Frame(ship1, .2f);
        Frame sf2 = new Frame(ship2, .2f);
        Frame sf3 = new Frame(ship3, .2f);
        shipAnim = new Animation(sf1, sf2, sf3);*/

        // could add the other "sprites" here as a new Anim obj
        Frame lvl = new Frame(level, .3f);
        Frame up1 = new Frame(upOne, .3f);
        Frame up2 = new Frame(upTwo, .3f);
        Frame down1 = new Frame(downOne, .3f);
        Frame down2 = new Frame(downTwo, .3f);

        levelAnim = new Animation(lvl);
        upOneAnim = new Animation(up1);
        upTwoAnim = new Animation(up2);
        downOneAnim = new Animation(down1);
        downTwoAnim = new Animation(down2);
    }

    public static void unloadMenuAssets() {
        unloadBitmap(welcome);
        unloadBitmap(begin);
        unloadBitmap(beginDown);
        unloadBitmap(options);
        unloadBitmap(optionsDown);
    }

    public static void unloadPlayAssets() {
        unloadBitmap(earth);
        unloadBitmap(asteroid);
        unloadBitmap(level);
        unloadBitmap(upOne);
        unloadBitmap(upTwo);
        unloadBitmap(downOne);
        unloadBitmap(downTwo);
        unloadBitmap(laserItem);
    }

    /*private static void loadEarthAnim() {
        earthOne = loadBitmap("earth/earth-one.png", true);
        earthTwo = loadBitmap("earth/earth-two.png", true);
        earthThree = loadBitmap("earth/earth-three.png", true);
        earthFour = loadBitmap("earth/earth-four.png", true);
        earthFive = loadBitmap("earth/earth-five.png", true);
        earthSix = loadBitmap("earth/earth-six.png", true);
        earthSeven = loadBitmap("earth/earth-seven.png", true);
        earthEight = loadBitmap("earth/earth-eight.png", true);
        earthNine = loadBitmap("earth/earth-nine.png", true);
        earthTen = loadBitmap("earth/earth-ten.png", true);
        earthEleven = loadBitmap("earth/earth-eleven.png", true);
        earthTwelve = loadBitmap("earth/earth-twelve.png", true);

        Frame e1 = new Frame(earthOne, 16.0f);
        Frame e2 = new Frame(earthTwo, 16.0f);
        Frame e3 = new Frame(earthThree, 16.0f);
        Frame e4 = new Frame(earthFour, 16.0f);
        Frame e5 = new Frame(earthFive, 16.0f);
        Frame e6 = new Frame(earthSix, 16.0f);
        Frame e7 = new Frame(earthSeven, 16.0f);
        Frame e8 = new Frame(earthEight, 16.0f);
        Frame e9 = new Frame(earthNine, 16.0f);
        Frame e10 = new Frame(earthTen, 16.0f);
        Frame e11 = new Frame(earthEleven, 16.0f);
        Frame e12 = new Frame(earthTwelve, 16.0f);

        earthAnim = new Animation(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12);
    }*/

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

        // maybe pause music goes here?
        //stopMusic();
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
    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {

            // just call stop music, save the position, and release resources
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