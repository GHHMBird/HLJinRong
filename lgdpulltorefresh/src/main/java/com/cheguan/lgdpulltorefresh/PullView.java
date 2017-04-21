package com.cheguan.lgdpulltorefresh;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sunday on 15/11/4.
 */
public class PullView extends LinearLayout {

    private Context context;
    private ImageView mImageView;
    private TextView mTextView;
    private RotateAnimation rotateAnimation;

    public PullView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        mImageView = new ImageView(context);
        mImageView.setImageResource(R.drawable.haili_loading_1);
        addView(mImageView);


//        mTextView = new TextView(context);
//        mTextView.setText("正在加载");
//        mTextView.setTextSize(12);
//        mTextView.setPadding(20,0,0,0);
//        addView(mTextView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        rotateAnimation = new RotateAnimation(0,359,mImageView.getWidth() / 2,mImageView.getHeight() / 2);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(800);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        mImageView.startAnimation(rotateAnimation);

    }
}
