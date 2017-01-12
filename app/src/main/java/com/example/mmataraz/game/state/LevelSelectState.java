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
    private boolean earthSelected, marsSelected, saturnSelected, noneSelected;

    @Override
    public void init() {
        earthButton = new UIButton(270, 255, 320, 305, Assets.levelBracket, Assets.levelBracketDown);
        marsButton = new UIButton(480, 252, 530, 302, Assets.levelBracket, Assets.levelBracketDown);
        saturnButton = new UIButton(328, 12, 378, 62, Assets.levelBracket, Assets.levelBracketDown);

        earthSelected = false;
        marsSelected = false;
        saturnSelected = false;
        noneSelected = true;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onExit() {
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.mapScreen, 0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(Typeface.DEFAULT_BOLD, 24);
        g.drawString("SELECT FACTION:", 20, 40);

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

                if (earthSelected) {
                    //setCurrentState(new LoadState(this, new BriefingState(PlayStateLevel.EARTH)));
                    setCurrentState(new BriefingState(PlayStateLevel.EARTH));
                }

                earthSelected = true;
                marsSelected = false;
                saturnSelected = false;
                noneSelected = false;

            } else if (marsButton.isPressed(scaledX, scaledY)) {
                marsButton.cancel();
                Log.d("LevelSelectState", "Mars level selected!");

                if (marsSelected) {
                    //setCurrentState(new LoadState(this, new BriefingState(PlayStateLevel.MARS)));
                    setCurrentState(new BriefingState(PlayStateLevel.MARS));
                }

                earthSelected = false;
                marsSelected = true;
                saturnSelected = false;
                noneSelected = false;

            } else if (saturnButton.isPressed(scaledX, scaledY)) {
                saturnButton.cancel();
                Log.d("LevelSelectState", "Saturn level selected!");

                if (saturnSelected) {
                    //setCurrentState(new LoadState(this, new BriefingState(PlayStateLevel.SATURN)));
                    setCurrentState(new BriefingState(PlayStateLevel.SATURN));
                }

                earthSelected = false;
                marsSelected = false;
                saturnSelected = true;
                noneSelected = false;

            } else {
                // Cancel all actions.
                earthButton.cancel();
                marsButton.cancel();
                saturnButton.cancel();

                earthSelected = false;
                marsSelected = false;
                saturnSelected = false;

                if (noneSelected) {
                    setCurrentState(new MenuState());
                }

                noneSelected = true;
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
