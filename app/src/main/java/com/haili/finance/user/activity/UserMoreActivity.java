package com.haili.finance.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.UpDateModel;
import com.haili.finance.business.user.UpDateRequest;
import com.haili.finance.business.user.UpDateResponse;
import com.haili.finance.fragment.UpDateFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.fragment.FeedbackFragment;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lfu on 2017/2/21.
 */

public final class UserMoreActivity extends BaseActivity implements View.OnClickListener {

    private Subscription subscrip;
    private LinearLayout userSafeLayout, aboutOurLayout, userFeedbackLayout, userServiceLayout, versionLayout;
    private TextView titleText;
    private String versinName = "", updateUrl;
    private int versionCode;
    private FeedbackFragment feedbackFragment;
    private boolean isFeedbackFragmentShow = false;
    private UpDateModel upDateModel;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_version:
                showDialogVerSion();
                break;
            case R.id.user_safe:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("params", "http://192.168.100.204/sui/html/my/apsafe.html");
                intent.putExtra("title", "安全保障");
                startActivity(intent);
                break;
            case R.id.about_our:
                Intent intent2 = new Intent(this, WebActivity.class);
                intent2.putExtra("params", "http://192.168.100.204/sui/html/my/aboutUs.html");
                intent2.putExtra("title", "关于我们");
                startActivity(intent2);
                break;
            case R.id.user_feedback:
                LoginResponse cacheData = DataCache.instance.getCacheData("haili", "LoginResponse");
                if (cacheData != null && cacheData.loginModel != null && !TextUtils.isEmpty(cacheData.loginModel.accessToken)) {
                    showFeedbackFragment();
                } else {
                    startActivity(new Intent(UserMoreActivity.this, LoginActivity.class));
                }
                break;
            case R.id.my_service:
                showDialog();
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("feedBackIsFinish")) {
            removeFeedbackFragment();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_more);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("更多");
        initView();
        initListener();
    }


    @Override
    public void onBackPressed() {
        if (isFeedbackFragmentShow) {
            removeFeedbackFragment();
        } else {
            finish();
        }
    }

    private void initView() {
        versionLayout = (LinearLayout) findViewById(R.id.app_version);
        userSafeLayout = (LinearLayout) findViewById(R.id.user_safe);
        aboutOurLayout = (LinearLayout) findViewById(R.id.about_our);
        userFeedbackLayout = (LinearLayout) findViewById(R.id.user_feedback);
        userServiceLayout = (LinearLayout) findViewById(R.id.my_service);
        titleText = (TextView) findViewById(R.id.title_text);
        try {
            versinName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.version_text)).setText("V" + versinName);
    }

    private void initListener() {
        userSafeLayout.setOnClickListener(this);
        aboutOurLayout.setOnClickListener(this);
        userFeedbackLayout.setOnClickListener(this);
        userServiceLayout.setOnClickListener(this);
        versionLayout.setOnClickListener(this);
    }


    private void showDialog() {
        ViewHelper.buildDialog(this, "确定拨打客服电话？", "400001668", new ViewHelper.OnPositiveBtnClickedListener() {
            @Override
            public void onPositiveBtnClicked(MaterialDialog dialog) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:400001668"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        }).show();
    }

    private void showDialogVerSion() {
        subscrip=  UserBusinessHelper.upDateVersion(new UpDateRequest())
                .subscribe(new Action1<UpDateResponse>() {
                    @Override
                    public void call(UpDateResponse upDateResponse) {
                        if (upDateResponse.dataModel != null) {
                            upDateModel = upDateResponse.dataModel;
                            updateUrl = upDateModel.updateUrl;
                            showUpdateDialog();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(UserMoreActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void showUpdateDialog() {
        if (versionCode < Double.parseDouble(upDateModel.versionCode)) {//判断当前版本是不是最新版本

            String hintText = "当前版本 <font color=red>V" + versinName + "</font> 最新版本 <font color=red>V" + upDateModel.versionName + "</font>,确认下载更新？";
            UpDateFragment upDateFragment = UpDateFragment.newInstance("updateStart", hintText, updateUrl, 0);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_feedback, upDateFragment, UpDateFragment.TAG)
                    .commitAllowingStateLoss();

        } else {
            ViewHelper.showToast(this, "当前是最新版本");
        }
    }

    private void showFeedbackFragment() {
        if (!isFeedbackFragmentShow && getFragmentManager().findFragmentById(R.id.fragment_feedback) == null) {
            if (feedbackFragment == null) {
                feedbackFragment = new FeedbackFragment();
            }
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                    .add(R.id.fragment_feedback, feedbackFragment, "12234")
                    .commit();
            setBarTitle("意见反馈");
            isFeedbackFragmentShow = true;
        }
    }

    private void removeFeedbackFragment() {
        if (feedbackFragment != null && isFeedbackFragmentShow) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(0, R.animator.slide_out_right)
                    .remove(feedbackFragment).commitAllowingStateLoss();
            isFeedbackFragmentShow = false;
            feedbackFragment = null;
            setBarTitle("更多");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (feedbackFragment != null) {
            feedbackFragment = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}