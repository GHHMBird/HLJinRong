package com.haili.finance.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.ViewHelper;

import com.mcxiaoke.bus.Bus;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/*
 * Created by Monkey on 2016/12/23.
 */

public class BaseActivity extends AppCompatActivity {

    private View actionView;
    private TextView titleText;
    private RelativeLayout backBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.getDefault().register(this);
    }

    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleText = (TextView)findViewById(R.id.title_text);
        backBtn = (RelativeLayout)findViewById(R.id.back_btn);
        actionView = toolbar.getRootView();
        toolbar.getBackground().setAlpha(255);
        setSupportActionBar(toolbar);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setBarTitle(String title){
        titleText.setText(title);
    }

    public void showBackBtn(boolean isShow){
        if (isShow){
            backBtn.setVisibility(View.VISIBLE);
        }else {
            backBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /*
     * 显示透明状态栏
     */
    protected void applyTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(this, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(0x00000000);
        ViewGroup view = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        if (null != view) {
            view.setClipToPadding(false);
        }
    }
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);
    }
    public void addLoadingFragment(int viewId,String event){
        LoadingFragment mLoadingFragment = LoadingFragment.newInstance(event);
        getFragmentManager()
                .beginTransaction()
                .replace(viewId, mLoadingFragment,LoadingFragment.TAG)
                .commitAllowingStateLoss();
    }

    public void removeLoadingFragment(){
        LoadingFragment mLoadingFragment = findLoadingFragment();
        if(mLoadingFragment != null){
            mLoadingFragment.removeSelf(getFragmentManager());
        }
    }


    public LoadingFragment findLoadingFragment(){
        Fragment fragment = getFragmentManager().findFragmentByTag(LoadingFragment.TAG);
        if(fragment != null){
            return (LoadingFragment)fragment;
        }
        return null;
    }

    /*
     * 显示键盘
     */
    public void showkeybord(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }
    /*
     * 隐藏键盘
     */
    public void hintkeybord(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        Bus.getDefault().unregister(this);
        ViewHelper.toastStop();
        super.onDestroy();
    }

}
