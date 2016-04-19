package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 2/18/2015.
 */
public class ScoreState extends State {

    private String highScore;
    //private String scoreMessage = "THE ALL-TIME HIGH SCORE";

    private int musicSliderPosX = 0;
    private int fxSliderPosX = 200;
    private boolean sliderTouch = false;

    public ScoreState() {
        this.highScore = GameMainActivity.getHighScore() + "";
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        Assets.updateVolumes(fxSliderPosX);
    }

    @Override
    public void render(Painter g) {
        //g.setColor(Color.rgb(53, 156, 253));
        //g.setColor(Color.rgb(64, 64, 128));
        //g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        g.drawImage(Assets.welcome, 0, 0);  // duh
        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, /*50*/ 32);
        //g.drawString("THE ALL-TIME HIGH SCORE IS:", 120, 175);
        g.drawString("HIGH SCORE:", 50, 50);
        //g.setFont(Typeface.DEFAULT_BOLD, /*70*/ 64);
        g.drawString(highScore, 250, 50);
        //g.setFont(Typeface.DEFAULT_BOLD, 50);
        //g.drawString("Touch to go back.", 220, 350);

        renderSliders(g);
    }

    private void renderSliders(Painter g) {
        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 24);
        g.drawString("FX Volume", 50, 350);

        g.fillRect(50, 400, 200, 10);
        g.fillOval(fxSliderPosX - 25, 385, 40, 40);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (Math.abs(scaledX - fxSliderPosX) < 20 && Math.abs(scaledY - 405) < 20)
                sliderTouch = true;
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            fxSliderPosX = scaledX < 50 ? 50 : (scaledX > 250 ? 250 : scaledX);
            //Log.d("FX Volume:", Integer.toString(fxSliderPosX));
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            if (!sliderTouch)
                setCurrentState(new MenuState());
            else
                sliderTouch = false;
        }

        return true;
    }

}
