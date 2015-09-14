package com.example.mmataraz.framework.animation;

import android.graphics.Bitmap;

/**
 * Created by Mike on 2/4/2015.
 */
public class Frame {

    private Bitmap image;
    private double duration;

    public Frame(Bitmap image, double duration) {
        this.image = image;
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public Bitmap getImage() {
        return image;
    }

}
