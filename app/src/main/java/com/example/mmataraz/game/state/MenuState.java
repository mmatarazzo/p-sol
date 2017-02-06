package com.example.mmataraz.game.state;

import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Mike on 2/2/2015.
 */
public class MenuState extends State {

    // Declare a UIButton object for each button.
    private UIButton mainGameButton, trainingButton, scoreButton, optionsButton, creditsButton;

    @Override
    public void init() {
        mainGameButton = new UIButton(764-Assets.mainGame.getWidth(), 96, 764, 96+Assets.mainGame.getHeight(),
                Assets.mainGame, Assets.mainGame);
        trainingButton = new UIButton(764-Assets.training.getWidth(), 128, 764, 128+Assets.training.getHeight(),
                Assets.training, Assets.training);
        scoreButton = new UIButton(764-Assets.score.getWidth(), 160, 764, 160+Assets.score.getHeight(),
                Assets.score, Assets.score);
        optionsButton = new UIButton(764-Assets.options.getWidth(), 192, 764, 192+Assets.options.getHeight(),
                Assets.options, Assets.options);
        creditsButton = new UIButton(764-Assets.credits.getWidth(), 224, 764, 224+Assets.credits.getHeight(),
                Assets.credits, Assets.credits);
    }

    @Override
    public void onLoad() {
        Assets.loadMenuAssets();
    }

    @Override
    public void onExit() {
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);
        mainGameButton.render(g);
        trainingButton.render(g);
        scoreButton.render(g);
        optionsButton.render(g);
        creditsButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if buttons were pressed
            mainGameButton.onTouchDown(scaledX, scaledY);
            trainingButton.onTouchDown(scaledX, scaledY);
            scoreButton.onTouchDown(scaledX, scaledY);
            optionsButton.onTouchDown(scaledX, scaledY);
            creditsButton.onTouchDown(scaledX, scaledY);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (mainGameButton.isPressed(scaledX, scaledY)) {
                // Button has been released.
                mainGameButton.cancel();
                // Perform an action here!
                Log.d("MenuState", "Play Button Pressed!");
                //setCurrentState(new LoadState(this, new LevelSelectState()));
                setCurrentState(new LevelSelectState());

                // Check training button
            } else if (trainingButton.isPressed(scaledX, scaledY)) {
                trainingButton.cancel();
                setCurrentState(new BriefingState(PlayStateLevel.TRAINING));
                //setCurrentState(new LoadState(this, new PlayState()));

                // Check score button
            } else if (scoreButton.isPressed(scaledX, scaledY)) {
                scoreButton.cancel();
                Log.d("MenuState", "Score Button Pressed!");
                setCurrentState(new ScoreState());

                // If options button is active and the release was within the options button:
            } else if (optionsButton.isPressed(scaledX, scaledY)) {
                optionsButton.cancel();
                setCurrentState(new ScoreState());  // use score again for now

            } else if (creditsButton.isPressed(scaledX, scaledY)) {
                creditsButton.cancel();

            } else {
                // Cancel all actions.
                mainGameButton.cancel();
                trainingButton.cancel();
                scoreButton.cancel();
                optionsButton.cancel();
                creditsButton.cancel();

                // Return to the title screen
                //setCurrentState(new TitleState());
            }
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
