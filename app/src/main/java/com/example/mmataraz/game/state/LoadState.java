package com.example.mmataraz.game.state;

import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Mike on 2/2/2015.
 */
public class LoadState extends State {

    private State currentState;
    private State target;

    // This constructor is used when using a loading screen
    public LoadState(State currentState, State target) {
        this.target = target;
        this.currentState = currentState;
        // This will print in LogCat which screen the LoadState will transition from and to.
        Log.d("debug", "Transitioning from " + currentState.toString() + " to " + target.toString());

    }

    @Override
    public void init() {
        //load();
    }

    @Override
    public void update(float delta) {
        currentState.onExit();
        System.gc();
        target.onLoad();
        setCurrentState(target);
        //setCurrentState(new MenuState());
    }

    @Override
    public void render(Painter g) {

    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}
