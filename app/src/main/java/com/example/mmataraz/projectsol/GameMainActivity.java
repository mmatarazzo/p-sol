package com.example.mmataraz.projectsol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Mike on 1/25/2015.
 */
public class GameMainActivity extends Activity {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 450;
    public static GameView sGame;
    public static AssetManager assets;

    private static SharedPreferences prefs;
    private static final String highScoreKey = "highScoreKey";
    private static int highScore;

    // resolution testing
    //private int gameWidth = getResources().getDisplayMetrics().widthPixels;
    //private int gameHeight = getResources().getDisplayMetrics().heightPixels;
    //public static final int GAME_WIDTH = 1920;
    //public static final int GAME_HEIGHT = 1080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getPreferences(Activity.MODE_PRIVATE);
        highScore = retrieveHighScore();
        assets = getAssets();
        sGame = new GameView(this, GAME_WIDTH, GAME_HEIGHT);
        setContentView(sGame);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        Log.d("GameMainActivity", "onResume()");
        super.onResume();
        Assets.onResume();
        sGame.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("GameMainActivity", "onPause()");
        //super.onPause();
        //Assets.onPause();
        sGame.onPause();    // try reverse order
        Assets.onPause();
        super.onPause();
    }

    public static void setHighScore(int highScore) {
        GameMainActivity.highScore = highScore;
        Editor editor = prefs.edit();
        editor.putInt(highScoreKey, highScore);
        editor.commit();
    }

    private int retrieveHighScore() {
        return prefs.getInt(highScoreKey, 0);
    }

    public static int getHighScore() {
        return highScore;
    }
}
