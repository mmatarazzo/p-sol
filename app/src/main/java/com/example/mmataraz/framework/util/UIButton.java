package com.example.mmataraz.framework.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Mike on 2/16/2015.
 */
public class UIButton {

    private Rect buttonRect;
    private boolean buttonDown = false;
    private boolean buttonPressed = false;
    private Bitmap buttonImage, buttonDownImage;

    public UIButton(int left, int top, int right, int bottom, Bitmap buttonImage,
                    Bitmap buttonPressedImage) {
        buttonRect = new Rect(left, top, right, bottom);
        this.buttonImage = buttonImage;
        this.buttonDownImage = buttonPressedImage;
    }

    // maybe another constructor without the button pressed - Vichy97

    // maybe add button pressed check - Vichy97
    public void render(Painter g) {
        Bitmap currentButtonImage = /*buttonDown*/ buttonPressed ? buttonDownImage : buttonImage;
        g.drawImage(currentButtonImage, buttonRect.left, buttonRect.top,
                buttonRect.width(), buttonRect.height());
    }

    public void onTouchDown(int touchX, int touchY) {
        if (buttonRect.contains(touchX, touchY)) {
            buttonDown = true;
            //buttonPressed = true;
        } /*else {
            buttonDown = false;
            //buttonPressed = false;
        }*/
    }

    public void cancel() {
        buttonDown = false;
    }

    public boolean isPressed(int touchX, int touchY) {
        //buttonPressed = buttonDown && buttonRect.contains(touchX, touchY);
        //return buttonPressed;
        return buttonDown && buttonRect.contains(touchX, touchY);
    }

    public boolean wasHit() {
        return buttonDown;
    }

    /*private boolean getButtonPressed() {
        return buttonPressed;
    }*/

}
