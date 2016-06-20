package com.example.mmataraz.game.state;

import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Michael on 6/15/2016.
 */
public class LoadPlayState extends State {

    @Override
    public void init() {
        //unload();
        load();
    }

    @Override
    public void update(float delta) {
        unload();
        setCurrentState(new PlayState());
    }

    @Override
    public void render(Painter g) {}

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }

    @Override
    public void onPause() {}

    @Override
    public void onResume() {}

    @Override
    public void load() {
        Assets.loadPlayAssets();
    }

    @Override
    public void unload() {
        Assets.unloadMenuAssets();
    }
}
