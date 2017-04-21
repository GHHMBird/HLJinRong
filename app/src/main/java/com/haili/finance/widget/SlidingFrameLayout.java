package com.haili.finance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 放在fragment外面可以防止点击事件穿透
 * Created by fuliang on 2017/3/6.
 */

public class SlidingFrameLayout extends FrameLayout {

    private static final String TAG = SlidingFrameLayout.class.getName();

    private Context context;

    public SlidingFrameLayout(Context context) {
        super(context);
        this.context = context;
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    public float getXFraction() {
        int width = getWidth();
        return (width == 0) ? 0 : getX() / (float) width;
    }

    public void setXFraction(float xFraction) {
        int width = getWidth();
        if (width == 0) {
            return;
        }
        setX(xFraction * width);
    }


    public float getYFraction() {
        int height = getHeight();
        return (height == 0) ? 0 : getY() / (float) height;
    }

    public void setYFraction(float yFraction) {
        int height = getHeight();
        if (height == 0) {
            return;
        }
        setY(yFraction * height);
    }
}
