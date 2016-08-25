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

    private UIButton earthButton, marsButton, saturnButton;

    @Override
    public void init() {
        earthButton = new UIButton(140, 40, 140 + Assets.earth.getWidth()/4,
                40 + Assets.earth.getHeight()/4, Assets.earth, Assets.earth);
        marsButton = new UIButton(140, 260, 140 + Assets.mars.getWidth()/4,
                260 + Assets.mars.getHeight()/4, Assets.mars, Assets.mars);
        saturnButton = new UIButton(460, 40, 460 + Assets.saturn.getWidth()/4,
                40 + Assets.saturn.getHeight()/4, Assets.saturn, Assets.saturn);
    }

    @Override
    public void onLoad() {
        //Assets.loadMenuAssets();
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

        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 24);
        g.drawString("EARTH:", 40, 40);
        g.drawString("MARS:", 40, 260);
        g.drawString("SATURN:", 360, 40);

        earthButton.render(g);
        marsButton.render(g);
        saturnButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            earthButton.onTouchDown(scaledX, scaledY);
            marsButton.onTouchDown(scaledX, scaledY);
            saturnButton.onTouchDown(scaledX, scaledY);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            // If the play button is active and the release was within the play button:
            if (earthButton.isPressed(scaledX, scaledY)) {
                // Button has been released.
                earthButton.cancel();
                // Perform an action here!
                Log.d("LevelSelectState", "Earth level selected!");
                //currentLevel = PlayStateLevel.EARTH;
                setCurrentState(new LoadState(this, /*new PlayState(PlayStateLevel.EARTH)*/
                        new BriefingState(PlayStateLevel.EARTH)));

                // If score button is active and the release was within the score button:
            } else if (marsButton.isPressed(scaledX, scaledY)) {
                marsButton.cancel();
                Log.d("LevelSelectState", "Mars level selected!");
                //currentLevel = PlayStateLevel.MARS;
                setCurrentState(new LoadState(this, /*new PlayState(PlayStateLevel.MARS)*/
                        new BriefingState(PlayStateLevel.MARS)));
            } else if (saturnButton.isPressed(scaledX, scaledY)) {
                saturnButton.cancel();
                Log.d("LevelSelectState", "Saturn level selected!");
                setCurrentState(new LoadState(this, new BriefingState(PlayStateLevel.SATURN)));
            } else {
                // Cancel all actions.
                earthButton.cancel();
                marsButton.cancel();
                saturnButton.cancel();
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
