package com.haili.finance.user.activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.stroage.PreferencesKeeper;
import com.haili.finance.utils.MD5Utils;
import com.haili.finance.widget.LockView;
import com.haili.finance.widget.RingView;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by fuliang on 2017/2/27.
 */

public class GestureActivity extends BaseActivity implements View.OnClickListener {

    private View placeHolderView;
    private RelativeLayout backBtn;
    private LinearLayout gestureMemory;
    private LockView lockView;
    private RingView view;
    private TextView hintTextView, resetBtn, otherBtn, forgetBtn;
    private ImageView userPortrait;
    private List<Integer> mCheckedPositionsFirst = new ArrayList<>();
    private List<Integer> mCheckedPositionsSecond = new ArrayList<>();
    private boolean isFirst = true;
    private int type;
    private int count = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_gesture_layout);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            setBarTitle("修改手势密码");
        } else if (type == 2) {
            setBarTitle("设置手势密码");
        } else if (type == 3) {
            setBarTitle("");
        }
        backBtn = (RelativeLayout) findViewById(R.id.back_btn);
        otherBtn = (TextView) findViewById(R.id.other_btn);
        forgetBtn = (TextView) findViewById(R.id.forget_btn);
        otherBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        userPortrait = (ImageView) findViewById(R.id.user_portrait);
        hintTextView = (TextView) findViewById(R.id.hint_text);
        placeHolderView = findViewById(R.id.place_holder_view);
        gestureMemory = (LinearLayout) findViewById(R.id.gesture_memory);
        resetBtn = (TextView) findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(this);
        lockView = (LockView) findViewById(R.id.lockView);
        lockView.setOnPointChangeListener(new LockView.OnDrawFinishedListener() {
            @Override
            public boolean onDrawFinished(List<Integer> passPositions) {
                LoginResponse response = DataCache.instance.getCacheData("haili", "LoginResponse");
                if (passPositions.size() > 3) {
                    if (type == 3) {
                        mCheckedPositionsFirst.clear();
                        mCheckedPositionsFirst.addAll(passPositions);
                        String keyString = "";
                        for (int i = 0; i < mCheckedPositionsFirst.size(); i++) {
                            keyString = keyString + mCheckedPositionsFirst.get(i);
                        }
                        if (PreferencesKeeper.getGestureKey(GestureActivity.this, response.loginModel.userId).equals(MD5Utils.MD5(keyString))) {
                            finish();
                        } else {
                            count--;
                            if (count == 0) {
                                Intent intent = new Intent(GestureActivity.this, LoginActivity.class);
                                intent.putExtra("type", 9);
                                startActivity(intent);
                                finish();
//                                PreferencesKeeper.saveGestureKey(GestureActivity.this, response.loginModel.userId, MD5Utils.MD5(""));
                                PreferencesKeeper.clearData(GestureActivity.this, response.loginModel.userId);
                                PreferencesKeeper.saveGuestState(GestureActivity.this,response.loginModel.userId, false);
                            } else {
                                hintTextView.setText("您还有" + count + "次机会");
                                Animation shake = AnimationUtils.loadAnimation(GestureActivity.this, R.anim.shake);
                                hintTextView.startAnimation(shake);
                                lockView.resetPoints();
                            }

                        }
                        return true;
                    }
                    if (isFirst) {
                        mCheckedPositionsFirst.clear();
                        mCheckedPositionsFirst.addAll(passPositions);
                        view.getCheckedPoint(passPositions);
                        isFirst = false;
                        hintTextView.setText("请再输一次手势密码");
                        resetBtn.setVisibility(View.VISIBLE);
                        view.invalidate();
                    } else {
                        mCheckedPositionsSecond.clear();
                        mCheckedPositionsSecond.addAll(passPositions);
                        if (!checkGesture()) {
                            hintTextView.setText("与上次绘制不一致，请重新绘制");
                            Animation shake = AnimationUtils.loadAnimation(GestureActivity.this, R.anim.shake);
                            hintTextView.startAnimation(shake);
                        } else {
                            String keyString = "";
                            for (int i = 0; i < mCheckedPositionsFirst.size(); i++) {
                                keyString = keyString + mCheckedPositionsFirst.get(i);
                            }
                            PreferencesKeeper.saveGestureKey(GestureActivity.this, response.loginModel.userId, MD5Utils.MD5(keyString));
                            PreferencesKeeper.saveGuestState(GestureActivity.this,response.loginModel.userId, true);
                            GestureActivity.this.setResult(0, null);
                            finish();
                        }

                    }
                } else {
                    Animation shake = AnimationUtils.loadAnimation(GestureActivity.this, R.anim.shake);
                    hintTextView.startAnimation(shake);
                }
                lockView.resetPoints();
                return true;
            }
        });
        if (type == 1) {
            backBtn.setVisibility(View.GONE);
        }
        if (type == 3) {
            backBtn.setVisibility(View.GONE);
            otherBtn.setVisibility(View.VISIBLE);
            forgetBtn.setVisibility(View.VISIBLE);
            resetBtn.setVisibility(View.GONE);
            gestureMemory.setVisibility(View.GONE);
            userPortrait.setVisibility(View.VISIBLE);
            hintTextView.setText("请输入手势密码");
        }
        placeViewControl();
        initRingView();
    }

    private boolean checkGesture() {
        int firstSize = mCheckedPositionsFirst.size();
        int secondSize = mCheckedPositionsSecond.size();
        if (firstSize != secondSize) {
            return false;
        }
        for (int i = 0; i < firstSize; i++) {
            if (mCheckedPositionsFirst.get(i) != mCheckedPositionsSecond.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GestureActivity.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            GestureActivity.this.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeHolderView.setVisibility(View.VISIBLE);
        } else {
            placeHolderView.setVisibility(View.GONE);
        }
    }

    private void initRingView() {
        mCheckedPositionsFirst.add(10);
        view = new RingView(this);
        view.getCheckedPoint(mCheckedPositionsFirst);
        view.invalidate();
        gestureMemory.addView(view);
    }

    private void resetGesture() {
        isFirst = true;
        hintTextView.setText("请绘制手势密码，至少连接4个点");
        mCheckedPositionsFirst.clear();
        view.getCheckedPoint(mCheckedPositionsFirst);
        view.invalidate();
        lockView.resetPoints();
        resetBtn.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        GestureActivity.this.setResult(1, null);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (type == 1) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_btn:
                Intent myIntent = new Intent(GestureActivity.this, PayPasswordActivity.class);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
                break;
            case R.id.other_btn:
                Intent intent = new Intent(GestureActivity.this, LoginActivity.class);
                intent.putExtra("type", 9);
                startActivity(intent);
                finish();
                LoginResponse response = DataCache.instance.getCacheData("haili", "LoginResponse");
//                PreferencesKeeper.saveGestureKey(GestureActivity.this, response.loginModel.userId, MD5Utils.MD5(""));
                PreferencesKeeper.clearData(GestureActivity.this, response.loginModel.userId);
                PreferencesKeeper.saveGuestState(this,response.loginModel.userId, false);
                break;
            case R.id.reset_btn:
                resetGesture();
                break;
        }

    }
}
