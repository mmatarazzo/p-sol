package com.example.mmataraz.game.state;

import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;

import java.util.logging.Level;

/**
 * Created by Mike on 2/2/2015.
 */
public class MenuState extends State {

    // Declare a UIButton object for each button.
    private UIButton playButton, optionsButton;

    @Override
    public void init() {
        playButton = new UIButton(764-Assets.begin.getWidth(), 84, 764, 84+Assets.begin.getHeight(), Assets.begin,
                Assets.beginDown);
        optionsButton = new UIButton(764-Assets.options.getWidth(), 128, 764, 128+Assets.options.getHeight(), Assets.options,
                Assets.optionsDown);
    }

    @Override
    public void onLoad() {
        Assets.loadMenuAssets();
    }

    @Override
    public void onExit() {
        //Assets.unloadMenuAssets();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);
        playButton.render(g);
        optionsButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            playButton.onTouchDown(scaledX, scaledY);
            optionsButton.onTouchDown(scaledX, scaledY);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (playButton.isPressed(scaledX, scaledY)) {
                // Button has been released.
                playButton.cancel();
                // Perform an action here!
                Log.d("MenuState", "Play Button Pressed!");
                setCurrentState(new LoadState(this, new LevelSelectState()));

                // If score button is active and the release was within the score button:
            } else if (optionsButton.isPressed(scaledX, scaledY)) {
                optionsButton.cancel();
                Log.d("MenuState", "Score Button Pressed!");
                setCurrentState(new ScoreState());
            } else {
                // Cancel all actions.
                playButton.cancel();
                optionsButton.cancel();
            }
        }

        return true;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}
