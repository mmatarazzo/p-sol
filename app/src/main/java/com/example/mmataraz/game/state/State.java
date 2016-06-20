package com.example.mmataraz.game.state;

import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 1/27/2015.
 */
public abstract class State {

    protected enum PlayStateLevel {EARTH, MARS, SATURN, JUPITER, NUM_LEVELS}
    protected static PlayStateLevel currentLevel = PlayStateLevel.NUM_LEVELS;

    public void setCurrentState(State newState) {
        GameMainActivity.sGame.setCurrentState(newState);
    }

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(Painter g);

    public abstract boolean onTouch(MotionEvent e, int scaledX, int scaledY);

    public void onPause() {}

    public void onResume() {}

    public void load() {}

    public void unload() {}

}
