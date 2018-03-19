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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
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
    private String mText;
    private TextPaint mTextPaint;
    private float mTextX = -1;
    private float mTextY = -1;

    private Context mContext;

    public CircularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int iconResId = -1;
        int textColor = Color.WHITE;
        float textSize = 16 * getResources().getDisplayMetrics().density;
        String textStyle;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularButton, 0, 0);
        try {
            mGradientStartColor = typedArray.getInt(R.styleable.CircularButton_cb_gradient_start, mGradientStartColor);
            mGradientEndColor = typedArray.getInt(R.styleable.CircularButton_cb_gradient_end, mGradientEndColor);
            iconResId = typedArray.getResourceId(R.styleable.CircularButton_cb_icon, iconResId);
            textColor = typedArray.getInt(R.styleable.CircularButton_cb_text_color, textColor);
            textSize = typedArray.getDimension(R.styleable.CircularButton_cb_text_size, textSize);
            textStyle = typedArray.getString(R.styleable.CircularButton_cb_text_style);
            TypedValue textValue = new TypedValue();
            if (typedArray.getValue(R.styleable.CircularButton_cb_text, textValue)) {
                mText = textValue.string.toString();
            }
        } finally {
            typedArray.recycle();
        }

        // init paints
        mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        if (!TextUtils.isEmpty(textStyle)) {
            if (textStyle.contentEquals("bold")) {
                mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            } else if (textStyle.contentEquals("italic")) {
                mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        }

        if (iconResId != -1) {
            mIcon = getBitmap(context, iconResId);
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

        if (mText != null && mTextX == -1 && mTextY == -1) {
            Rect r = new Rect();
            canvas.getClipBounds(r);
            int cHeight = r.height();
            int cWidth = r.width();
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            mTextPaint.getTextBounds(mText, 0, mText.length(), r);
            mTextX = cWidth / 2f - r.width() / 2f - r.left;
            mTextY = cHeight / 2f + r.height() / 2f - r.bottom;
        }

        canvas.drawCircle(width / 2, height / 2, height / 2, mBackgroundPaint);

        if (mIcon != null) {
            canvas.drawBitmap(mIcon, mIconSourceRect, mIconDestinationRect, mIconPaint);
        }

        if (mText != null) {
            canvas.drawText(mText, mTextX, mTextY, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OvalOutlineProvider(w, h));
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
    public void animateIcon(ValueAnimator valueAnimator) {
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
    public void setText(String text) {
        setText(text, null);
    }
    public void setText(String text, ValueAnimator valueAnimator) {
        mText = text;
        if (valueAnimator != null) {
            valueAnimator.start();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mTextPaint.setAlpha((int) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
        } else {
            invalidate();
        }
    }
    public void animateText(ValueAnimator valueAnimator) {
        setText(mText, valueAnimator);
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
