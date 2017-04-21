package com.haili.finance.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.haili.finance.R;
import com.haili.finance.utils.ClickListenerInterface;

/**
 * Created by Monkey on 2017/1/12.
 */

public class MyRedbackpage_Dialog extends Dialog {
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private ImageView iv_close;

    public MyRedbackpage_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyRedbackpage_Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public MyRedbackpage_Dialog(Context context, int theme) {
        super(context, theme);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_pop_red_backpage, null);
        setContentView(view);
        initView(view);
        init();
    }

    private void initView(View view) {
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
    }

    private void init() {
        initListener();

    }

    private void initListener() {
        iv_close.setOnClickListener(new clickListener());
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.iv_close:
                    clickListenerInterface.doClose();
                    break;
            }
        }
    }
}
