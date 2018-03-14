package com.zinzem.circularbutton;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import static com.zinzem.circularbutton.utils.RessourceUtils.getBitmap;

public class CircularButton extends View {

    private Paint mBackgroundPaint;
    private int mGradientStartColor = Color.WHITE;
    private int mGradientEndColor = Color.WHITE;
    private Bitmap mIcon;
    private Paint mIconPaint;
    private Rect mIconSourceRect;
    private Rect mIconDestinationRect;

    private Context mContext;

    public CircularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int iconResId = -1;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularButton, 0, 0);
        try {
            mGradientStartColor = typedArray.getInt(R.styleable.CircularButton_cb_gradient_start, mGradientStartColor);
            mGradientEndColor = typedArray.getInt(R.styleable.CircularButton_cb_gradient_end, mGradientEndColor);
            iconResId = typedArray.getResourceId(R.styleable.CircularButton_cb_icon, iconResId);
        } finally {
            typedArray.recycle();
        }

        if (iconResId != -1) {
            mIcon = getBitmap(context, iconResId);
            mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mIconSourceRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
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

        if (mIcon != null && mIconDestinationRect == null) {
            int left = (width / 2) - (mIcon.getWidth() / 2);
            int top = (height / 2) - (mIcon.getHeight() / 2);
            mIconDestinationRect = new Rect(left, top, left + mIcon.getWidth(), top + mIcon.getHeight());
        }

        canvas.drawCircle(width / 2, height / 2, height / 2, mBackgroundPaint);

        if (mIcon != null) {
            canvas.drawBitmap(mIcon, mIconSourceRect, mIconDestinationRect, mIconPaint);
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
        mIconSourceRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
        mIconDestinationRect = null;
        if (valueAnimator != null) {
            valueAnimator.start();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mIconPaint.setAlpha((int) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
        } else {
            invalidate();
        }
    }
    public void setCb_gradient_start(int color) {
        mGradientStartColor = color;
        mBackgroundPaint = null;
        invalidate();
    }
    public void setCb_gradient_end(int color) {
        mGradientEndColor = color;
        mBackgroundPaint = null;
        invalidate();
    }
    public void setCb_icon(Drawable drawable) {
        mIcon = getBitmap(drawable);
        mIconSourceRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
        mIconDestinationRect = null;
        invalidate();
    }

    public Drawable getCb_icon() {
        return new BitmapDrawable(getResources(), mIcon);
    }
    public int getCb_gradient_start(int color) {
        return mGradientEndColor;
    }
    public int getCb_gradient_end(int color) {
        return mGradientEndColor;
    }
}
