package com.example.mmataraz.framework.util;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.mmataraz.game.state.State;
import com.example.mmataraz.projectsol.GameMainActivity;

/**
 * Created by Mike on 1/27/2015.
 */
public class InputHandler extends GestureDetector.SimpleOnGestureListener implements OnTouchListener /*, GestureDetector.OnDoubleTapListener*/ {

    private State currentState;
    private GestureDetectorCompat gestureDetectorCompat;

    public InputHandler(Context context) {
        gestureDetectorCompat = new GestureDetectorCompat(context, this);
        gestureDetectorCompat.setOnDoubleTapListener(this);
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    // from OnTouchListener

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d("InputHandler", "onTouch: " + event.toString());
        int scaledX = (int) ((event.getX() / v.getWidth()) * GameMainActivity.GAME_WIDTH);
        int scaledY = (int) ((event.getY() / v.getHeight()) * GameMainActivity.GAME_HEIGHT);

        gestureDetectorCompat.onTouchEvent(event);  // must call to check for gestures
        return currentState.onTouch(event, scaledX, scaledY);
    }

    // from GestureDetector.SimpleOnGestureListener

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    /*@Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }*/

    // GestureDetector.OnDoubleTapListener

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        //Log.d("InputHandler", "onDoubleTap: " + event.toString());
        //return true;
        return currentState.onDoubleTap(event);
    }

    /*@Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        //Log.d("InputHandler", "onDoubleTapEvent: " + event.toString());
        return true;
    }*/

    /*@Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //Log.d("InputHandler", "onSingleTapConfirmed: " + event.toString());
        //return true;
        return false;   // can this be true?
    }*/

}
