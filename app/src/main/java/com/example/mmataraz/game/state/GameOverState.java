package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 2/17/2015.
 */
public class GameOverState extends State {

    private String playerScore;
    private String gameOverMessage = "GAME OVER";

    public GameOverState(int playerScore) {
        this.playerScore = playerScore + "";
        if (playerScore > GameMainActivity.getHighScore()) {
            GameMainActivity.setHighScore(playerScore);
            gameOverMessage = "HIGH SCORE";
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void onLoad() {
        //Assets.loadMenuAssets();
    }

    @Override
    public void onExit() {
        Assets.unloadPlayAssets();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Painter g) {
        //g.setColor(Color.rgb(255, 145, 0));
        //g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.drawImage(Assets.welcome, 0, 0);

        //g.setColor(Color.DKGRAY);
        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 50);
        g.drawString(gameOverMessage, 257, 175);
        g.drawString(playerScore, 385, 250);
        g.drawString("Touch the screen.", 220, 350);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            setCurrentState(new LoadState(this, new MenuState() /*new TitleState()*/));
        }

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }


    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}
