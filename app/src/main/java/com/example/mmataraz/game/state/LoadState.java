package com.example.mmataraz.game.state;

import android.view.MotionEvent;

import com.example.mmataraz.framework.util.Painter;
import com.example.mmataraz.projectsol.Assets;

/**
 * Created by Mike on 2/2/2015.
 */
public class LoadState extends State {

    @Override
    public void init() {
        Assets.load();
    }

    @Override
    public void update(float delta) {
        setCurrentState(new MenuState());
    }

    @Override
    public void render(Painter g) {

    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }

}
