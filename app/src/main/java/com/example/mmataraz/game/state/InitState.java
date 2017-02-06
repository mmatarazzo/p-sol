package com.example.mmataraz.game.state;

import android.util.Log;
import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Michael on 6/15/2016.
 */
public class InitState extends State {

    @Override
    public void init() {
        Log.v("debug", "init() called");
    }

    @Override
    public void update(float delta) {
        setCurrentState(new LoadState(this, new MenuState() /*new TitleState()*/));
    }

    @Override
    public void render(Painter g) {

    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
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
