package com.haili.finance.user.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.widget.LockView;

/**
 *
 * Created by fuliang on 2017/3/1.
 */

public class GestureLoginActivity extends BaseActivity implements View.OnClickListener{

    private View placeHolderView;
    private LockView lockView;
    private TextView hintTextView,otherBtn,forgetBtn;
    private ImageView userPortrait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_layout);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("");
        showBackBtn(false);
        initView();
    }

    private void initView(){
        placeHolderView = findViewById(R.id.place_holder_view);
        lockView = (LockView)findViewById(R.id.lockView);
        hintTextView = (TextView)findViewById(R.id.hint_text);
        userPortrait = (ImageView)findViewById(R.id.user_portrait);
        forgetBtn = (TextView)findViewById(R.id.forget_btn);
        otherBtn = (TextView)findViewById(R.id.other_btn);
        userPortrait.setVisibility(View.VISIBLE);
        forgetBtn.setVisibility(View.VISIBLE);
        otherBtn.setVisibility(View.VISIBLE);
    }

    private void placeViewControl(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            GestureLoginActivity.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            GestureLoginActivity.this.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeHolderView.setVisibility(View.VISIBLE);
        }else{
            placeHolderView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_btn:
                break;
            case R.id.other_btn:
                break;
        }
    }
}
