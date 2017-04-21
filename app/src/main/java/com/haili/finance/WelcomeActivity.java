package com.haili.finance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.Index.SplashImageRequest;
import com.haili.finance.business.Index.SplashImageResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.helper.IndexBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.stroage.PreferencesKeeper;
import com.haili.finance.user.activity.GestureActivity;
import com.haili.finance.utils.DeviceUtils;
import com.haili.finance.utils.StringUtils;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.functions.Action1;


public class WelcomeActivity extends BaseActivity {

    private String isFirstLogin = "true";
    private  Subscription subscribe;
    private RelativeLayout splashLayout;
    private ImageView imageView;
    private TextView timeView;
    private RelativeLayout timeBack;

    private CountDownTimer timer;

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<WelcomeActivity> mActivity;

        public MyHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Bus.getDefault().post("SHOW_IMAGE");
            } else {
                WelcomeActivity activity = mActivity.get();
                if (activity != null) {
                    activity.toNextPage();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_welcome);
        getDeviceInfo();
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setVisibility(View.GONE);
        timeView = (TextView) findViewById(R.id.time_view);
        timeView.setVisibility(View.GONE);
        timeBack = (RelativeLayout) findViewById(R.id.time_back);
        timeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextPage();
            }
        });
        View decorView = getWindow().getDecorView();
        getImage();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mHandler.sendEmptyMessageDelayed(0, 1500);
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("SHOW_IMAGE")) {
            addImage();
        }
    }

    private void getDeviceInfo() {
        DeviceInfoModel model = new DeviceInfoModel();
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "LoginResponse");
        if (loginResponse == null) {
            model.accessToken = "";
        } else {
            if (loginResponse.loginModel.accessToken == null) {
                model.accessToken = "";
            } else {
                model.accessToken = loginResponse.loginModel.accessToken;
            }

        }

        model.os = "Android";
        model.version = DeviceUtils.getVersionCode(this);
        model.phoneId = DeviceUtils.getDeviceId(this);
        model.channnelId = getAppMetaData(WelcomeActivity.this, "UMENG_CHANNEL");
        model.phoneVersion = DeviceUtils.getBuildVersion();
        model.devicevendor = DeviceUtils.getPhoneBrand();
        model.devicemodel = DeviceUtils.getPhoneModel();
        model.lang = DeviceUtils.getLanguage(this);
        model.sign = "";
        model.gps = "";
        DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
    }

    /*
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    private void toNextPage() {
        if (!PreferencesKeeper.getHasNewFeatureShown(getApplicationContext())) {
            goToGuide();
            return;
        }
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "LoginResponse");
        Intent getBundle = getIntent();
        Bundle bundle = getBundle.getBundleExtra("launchBundle");
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.putExtra("params", bundle);
        if (loginResponse != null) {
            if (PreferencesKeeper.getGuestState(WelcomeActivity.this, loginResponse.loginModel.userId)) {
                if (!StringUtils.isEmpty(PreferencesKeeper.getGestureKey(WelcomeActivity.this, loginResponse.loginModel.userId))) {
                    Intent gestureIntent = new Intent(WelcomeActivity.this, GestureActivity.class);
                    gestureIntent.putExtra("type", 3);
                    Intent[] intents = {intent, gestureIntent};
                    startActivities(intents);
                } else {
                    startActivity(intent);
                }
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);
        }
        timer.cancel();
        finish();

    }

    void goToGuide() {
        startActivity(new Intent(WelcomeActivity.this, GuidePageActivity.class));
        WelcomeActivity.this.finish();
    }

    private void getImage() {
       subscribe = IndexBusinessHelper.getInviteInfo(new SplashImageRequest())
                .subscribe(new Action1<SplashImageResponse>() {
                    @Override
                    public void call(SplashImageResponse splashImageResponse) {
                        if (!splashImageResponse.dataModel.imageUrl.equals("")) {
                            if (!WelcomeActivity.this.isDestroyed()) {
                                DataCache.instance.saveCacheData("ImageCache", "SplashImageResponse", splashImageResponse);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(WelcomeActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void addImage() {
        SplashImageResponse response = DataCache.instance.getCacheData("ImageCache", "SplashImageResponse");
        if (response == null) {
            toNextPage();
            return;
        }
        Glide.with(this)
                .load(response.dataModel.imageUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
        addTime();
        imageView.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(1, 3000);

    }

    private void addTime() {
        timeView.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                timeView.setText("跳过" + l / 1000);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }

}