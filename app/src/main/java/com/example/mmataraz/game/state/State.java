package com.example.mmataraz.game.state;

import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 1/27/2015.
 */
public abstract class State {

    protected enum PlayStateLevel {EARTH, MARS, SATURN, TRAINING}

    public void setCurrentState(State newState) {
        GameMainActivity.sGame.setCurrentState(newState);
    }

    public abstract void init();

    public void onLoad() {
    }

    public void onExit() {
    }

    public abstract void update(float delta);

    public abstract void render(Painter g);

    public abstract boolean onTouch(MotionEvent e, int scaledX, int scaledY);

    public abstract boolean onDoubleTap(MotionEvent e);

    public abstract void onPause();

    public abstract void onResume();

}
