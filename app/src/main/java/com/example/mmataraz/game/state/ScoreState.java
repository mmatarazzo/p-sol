package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 2/18/2015.
 */
public class ScoreState extends State {

    private String highScore;
    //private String scoreMessage = "THE ALL-TIME HIGH SCORE";

    public ScoreState() {
        this.highScore = GameMainActivity.getHighScore() + "";
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Painter g) {
        g.setColor(Color.rgb(53, 156, 253));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 50);
        g.drawString("THE ALL-TIME HIGH SCORE", 120, 175);
        g.setFont(Typeface.DEFAULT_BOLD, 70);
        g.drawString(highScore, 370, 260);
        g.setFont(Typeface.DEFAULT_BOLD, 50);
        g.drawString("Touch to go back.", 220, 350);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            setCurrentState(new MenuState());
        }

        return true;
    }

}
