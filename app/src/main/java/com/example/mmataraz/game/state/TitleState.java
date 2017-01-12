package com.example.mmataraz.game.state;

import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Michael on 12/20/2016.
 */

public class TitleState extends State {

    private UIButton startButton, creditsButton;

    @Override
    public void init() {
        startButton = new UIButton(764-Assets.start.getWidth(), 96, 764, 96+Assets.start.getHeight(),
                Assets.start, Assets.start);
        creditsButton = new UIButton(764-Assets.credits.getWidth(), 128, 764, 128+Assets.credits.getHeight(),
                Assets.credits, Assets.credits);
    }

    public void onLoad() {
        Assets.loadMenuAssets();
    }

    public void onExit() {
    }

    public void update(float delta) {

    }

    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);
        startButton.render(g);
        creditsButton.render(g);
    }

    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if buttons were pressed
            startButton.onTouchDown(scaledX, scaledY);
            creditsButton.onTouchDown(scaledX, scaledY);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (startButton.isPressed(scaledX, scaledY)) {
                // Button has been released.
                startButton.cancel();
                // Perform an action here!
                //Log.d("MenuState", "Play Button Pressed!");
                setCurrentState(new MenuState());   // don't need to do a whole new LoadState

                // Check credits button
            } else if (creditsButton.isPressed(scaledX, scaledY)) {
                creditsButton.cancel();
                //setCurrentState(new CreditState());

            } else {
                // Cancel all actions.
                startButton.cancel();
                creditsButton.cancel();
            }
        }

        return true;
    }

    public void onPause() {

    }

    public void onResume() {

    }

}
