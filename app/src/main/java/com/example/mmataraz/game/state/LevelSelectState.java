package com.example.mmataraz.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.framework.util.UIButton;
import com.example.mmataraz.projectsol.Assets;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Michael on 6/16/2016.
 */
public class LevelSelectState extends State {

    private UIButton earthButton, marsButton, saturnButton;
    private boolean earthSelected, marsSelected, saturnSelected;

    @Override
    public void init() {
        //earthButton = new UIButton(140, 40, 140 + Assets.earth.getWidth()/4,
                //40 + Assets.earth.getHeight()/4, Assets.earth, Assets.earth);
        earthButton = new UIButton(270, 255, 320, 305, Assets.levelBracket, Assets.levelBracketDown);
        //marsButton = new UIButton(140, 260, 140 + Assets.mars.getWidth()/4,
                //260 + Assets.mars.getHeight()/4, Assets.mars, Assets.mars);
        marsButton = new UIButton(480, 252, 530, 302, Assets.levelBracket, Assets.levelBracketDown);
        //saturnButton = new UIButton(460, 40, 460 + Assets.saturn.getWidth()/4,
                //40 + Assets.saturn.getHeight()/4, Assets.saturn, Assets.saturn);
        saturnButton = new UIButton(328, 12, 378, 62, Assets.levelBracket, Assets.levelBracketDown);

        earthSelected = false;
        marsSelected = false;
        saturnSelected = false;
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
        //g.drawImage(Assets.welcome, 0, 0);
        g.drawImage(Assets.mapScreen, 0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 24);
        g.drawString("SELECT FACTION:", 20, 40);
        //g.drawString("EARTH:", 40, 40);
        //g.drawString("MARS:", 40, 260);
        //g.drawString("SATURN:", 360, 40);

        earthButton.render(g);
        marsButton.render(g);
        saturnButton.render(g);

        if (earthSelected)
            g.drawImage(Assets.levelSelected, 270, 255, 50, 50);
        if (marsSelected)
            g.drawImage(Assets.levelSelected, 480, 252, 50, 50);
        if (saturnSelected)
            g.drawImage(Assets.levelSelected, 328, 12, 50, 50);
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
                if (earthSelected) {
                    setCurrentState(new LoadState(this, /*new PlayState(PlayStateLevel.EARTH)*/
                            new BriefingState(PlayStateLevel.EARTH)));
                }

                earthSelected = true;
                marsSelected = false;
                saturnSelected = false;

                // If score button is active and the release was within the score button:
            } else if (marsButton.isPressed(scaledX, scaledY)) {
                marsButton.cancel();
                Log.d("LevelSelectState", "Mars level selected!");
                //currentLevel = PlayStateLevel.MARS;
                if (marsSelected) {
                    setCurrentState(new LoadState(this, /*new PlayState(PlayStateLevel.MARS)*/
                            new BriefingState(PlayStateLevel.MARS)));
                }

                earthSelected = false;
                marsSelected = true;
                saturnSelected = false;
            } else if (saturnButton.isPressed(scaledX, scaledY)) {
                saturnButton.cancel();
                Log.d("LevelSelectState", "Saturn level selected!");

                if (saturnSelected) {
                    setCurrentState(new LoadState(this, new BriefingState(PlayStateLevel.SATURN)));
                }

                earthSelected = false;
                marsSelected = false;
                saturnSelected = true;
            } else {
                // Cancel all actions.
                earthButton.cancel();
                marsButton.cancel();
                saturnButton.cancel();

                earthSelected = false;
                marsSelected = false;
                saturnSelected = false;
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
