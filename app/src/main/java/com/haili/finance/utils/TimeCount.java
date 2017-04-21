package com.haili.finance.utils;

import android.content.Context;
import android.widget.TextView;
import com.haili.finance.R;

/**
 * Created by admin on 2016/9/19.
 */
public class TimeCount extends CountDownTimer {
    private TextView button;
    private Context context;
    private int style = 0;

    public TimeCount(long millisInFuture, long countDownInterval, TextView button, Context context, int style) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        this.context = context;
        this.style = style;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setText(millisUntilFinished / 1000 + "秒后重试");
        if (style == 1) {
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.btn_bg_getmsg_ed);
        } else {
            button.setTextColor(context.getResources().getColor(R.color.text_grey));
            button.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void onFinish() {
        button.setText(context.getString(R.string.get_msgcode));
        button.setClickable(true);
        if (style == 1) {
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.btn_bg_getmsg);
        } else {
            button.setTextColor(context.getResources().getColor(R.color.text_red));
            button.setBackgroundResource(R.color.white);
        }
    }
}