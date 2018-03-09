package com.zinzem.circularbutton;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class OvalOutlineProvider extends ViewOutlineProvider {

    private int mWidth;
    private int mHeight;

    public OvalOutlineProvider(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setOval(0, 0, mWidth, mHeight);
    }
}