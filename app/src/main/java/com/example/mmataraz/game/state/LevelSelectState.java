package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Michael on 6/16/2016.
 */
public class LevelSelectState extends State {

    private UIButton earthButton, marsButton;

    @Override
    public void init() {
        earthButton = new UIButton(250, 50, 250 + Assets.earth.getWidth()/4, 50 + Assets.earth.getHeight()/4, Assets.earth, Assets.earth);
        marsButton = new UIButton(250, 250, 250 + Assets.mars.getWidth()/4, 250 + Assets.mars.getHeight()/4, Assets.mars, Assets.mars);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);  // duh

        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, /*50*/ 24);

        g.drawString("EARTH:", 50, 50);
        g.drawString("MARS:", 50, 250);

        //g.drawImage(Assets.earth, 250, 50, 100, 100);
        //g.drawImage(Assets.mars, 250, 250, 100, 100);
        earthButton.render(g);
        marsButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            earthButton.onTouchDown(scaledX, scaledY);
            marsButton.onTouchDown(scaledX, scaledY);
        }

        /*if (e.getAction() == MotionEvent.ACTION_UP) {
            if (scaledX > 150 && scaledX < 350 && scaledY > 50 && scaledY < 150) {
                Assets.setLevelID(0);
                setCurrentState(new LoadPlayState());
            } else if (scaledX > 150 && scaledX < 350 && scaledY > 250 && scaledY < 350) {
                Assets.setLevelID(1);
                setCurrentState(new LoadPlayState());
            }
        }*/

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (earthButton.isPressed(scaledX, scaledY)) {
                // Button has been released.
                earthButton.cancel();
                // Perform an action here!
                Log.d("LevelSelectState", "Earth level selected!");
                //Assets.setLevelID(0);
                currentLevel = PlayStateLevel.EARTH;
                setCurrentState(new LoadPlayState());

                // If score button is active and the release was within the score button:
            } else if (marsButton.isPressed(scaledX, scaledY)) {
                marsButton.cancel();
                Log.d("LevelSelectState", "Mars level selected!");
                //Assets.setLevelID(1);
                currentLevel = PlayStateLevel.MARS;
                setCurrentState(new LoadPlayState());
            } else {
                // Cancel all actions.
                earthButton.cancel();
                marsButton.cancel();
            }
        }

        return true;
    }

    @Override
    public void onPause() {}

    @Override
    public void onResume() {}

}
