package com.example.mmataraz.projectsol;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.mmataraz.framework.animation.Animation;
import com.example.mmataraz.framework.animation.Frame;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 1/30/2015.
 */
public class Assets {

    private static SoundPool soundPool;
    //public static Animation runAnim;
    public static int hitID, onJumpID, fireID, destroyID;
    public static Bitmap welcome, block, asteroid, cloud1, cloud2, earth, duck, grass, jump, /*run1, run2, run3,
            run4, run5,*/ optionsDown, options, beginDown, begin;

    // ship test
    public static Bitmap /*ship1, ship2, ship3,*/ level, upOne, upTwo, downOne, downTwo;
    public static Animation shipAnim, levelAnim, upOneAnim, upTwoAnim, downOneAnim, downTwoAnim;

    public static Bitmap earthOne, earthTwo, earthThree, earthFour, earthFive, earthSix,
            earthSeven, earthEight, earthNine, earthTen, earthEleven, earthTwelve;
    public static Animation earthAnim;

    // music test
    private static MediaPlayer mediaPlayer;

    public static void load() {
        welcome = loadBitmap("welcome.png", false);
        block = loadBitmap("block.png", false);
        asteroid = loadBitmap("asteroid1.png", false);
        cloud1 = loadBitmap("cloud1.png", true);
        cloud2 = loadBitmap("cloud2.png", true);
        earth = loadBitmap("earth-psol.png", true);
        duck = loadBitmap("duck.png", true);
        grass = loadBitmap("grass.png", false);
        jump = loadBitmap("jump.png", true);
        /*run1 = loadBitmap("run_anim1.png", true);
        run2 = loadBitmap("run_anim2.png", true);
        run3 = loadBitmap("run_anim3.png", true);
        run4 = loadBitmap("run_anim4.png", true);
        run5 = loadBitmap("run_anim5.png", true);*/
        optionsDown = loadBitmap("options_button_down.png", true);
        options = loadBitmap("options_button.png", true);
        beginDown = loadBitmap("begin_button_down.png", true);
        begin = loadBitmap("begin_button.png", true);

        // ship test
        /*ship1 = loadBitmap("shiptest_anim1.png", true);
        ship2 = loadBitmap("shiptest_anim2.png", true);
        ship3 = loadBitmap("shiptest_anim3.png", true);*/

        level = loadBitmap("arwing-straight.png", true);
        upOne = loadBitmap("arwing-leftOne.png", true);
        upTwo = loadBitmap("arwing-leftTwo.png", true);
        downOne = loadBitmap("arwing-rightOne.png", true);
        downTwo = loadBitmap("arwing-rightTwo.png", true);

        /*Frame f1 = new Frame(run1, .1f);
        Frame f2 = new Frame(run2, .1f);
        Frame f3 = new Frame(run3, .1f);
        Frame f4 = new Frame(run4, .1f);
        Frame f5 = new Frame(run5, .1f);
        runAnim = new Animation(f1, f2, f3, f4, f5, f3, f2);*/

        // ship test
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

        loadEarthAnim();
    }

    private static void loadEarthAnim() {
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
    }

    public static void onResume() {
        hitID = loadSound("hit.wav");
        onJumpID = loadSound("onjump.wav");
        fireID = loadSound("laser2.wav");
        destroyID = loadSound("explode.wav");
        //playMusic("Polfix.mid", true);
    }

    public static void onPause() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        /*if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }*/
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

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, new Options());
        return bitmap;
    }

    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            soundID = soundPool.load(GameMainActivity.assets.openFd(filename), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return soundID;
    }

    public static void playSound(int soundID) {
        if (soundPool != null) {
            soundPool.play(soundID, 1, 1, 1, 0, 1);
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
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
