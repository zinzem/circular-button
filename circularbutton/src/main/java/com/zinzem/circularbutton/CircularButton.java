package com.zinzem.circularbutton;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import static com.zinzem.circularbutton.utils.RessourceUtils.getBitmap;

public class CircularButton extends View {

    private Paint mBackgroundPaint;
    private int mGradientStartColor = Color.WHITE;
    private int mGradientEndColor = Color.WHITE;
    private Paint mIconPaint;
    private Bitmap mIcon;

    private Context mContext;

    public CircularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int iconResId = -1;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularView, 0, 0);
        try {
            mGradientStartColor = typedArray.getInt(R.styleable.CircularView_gradient_start, mGradientStartColor);
            mGradientEndColor = typedArray.getInt(R.styleable.CircularView_gradient_end, mGradientEndColor);
            iconResId = typedArray.getResourceId(R.styleable.CircularView_fg_icon, iconResId);
        } finally {
            typedArray.recycle();
        }

        if (iconResId != -1) {
            mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mIcon = getBitmap(context, iconResId);
        }

        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        if (mBackgroundPaint == null) {
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mBackgroundPaint.setShader(new LinearGradient(0,
                    0,
                    0,
                    height,
                    mGradientStartColor,
                    mGradientEndColor,
                    Shader.TileMode.MIRROR));
        }

        canvas.drawCircle(width / 2, height / 2, height / 2, mBackgroundPaint);

        if (mIcon != null) {
            canvas.drawBitmap(mIcon, (width - mIcon.getWidth()) / 2, (height - mIcon.getHeight()) / 2, mIconPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OvalOutlineProvider(w, h));
        }
    }

    public void setIcon(int resId) {
        setIcon(resId, null);
    }

    public void setIcon(int resId, ValueAnimator valueAnimator) {
        mIcon = getBitmap(mContext, resId);
        if (valueAnimator != null) {
            valueAnimator.start();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mIconPaint.setAlpha((int) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
        }
    }
}
