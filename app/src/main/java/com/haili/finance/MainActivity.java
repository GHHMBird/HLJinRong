package com.haili.finance;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.MyInfoRequest;
import com.haili.finance.business.user.UpDateRequest;
import com.haili.finance.business.user.UpDateResponse;
import com.haili.finance.fragment.AssetsFragment;
import com.haili.finance.fragment.HomeFragment;
import com.haili.finance.fragment.ManageFragment;
import com.haili.finance.fragment.MyFragment;
import com.haili.finance.fragment.UpDateFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.stroage.PreferencesKeeper;
import com.haili.finance.user.activity.GestureActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.fragment.RegisterRedPackageFragment;
import com.haili.finance.utils.DeviceUtils;
import com.haili.finance.utils.StringUtils;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.networkbench.agent.impl.NBSAppAgent;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_home, rl_licai, rl_asstes, rl_my;
    private ImageView iv_home, iv_licai, iv_asstes, iv_my;
    private TextView tv_home, tv_licai, tv_asstes, tv_my;
    private long exitTime = 0;
    private Subscription subscrip, subscrip2;
    HomeFragment mHomeFragment;
    ManageFragment manageFragment;
    AssetsFragment mAssetsFragment;
    private String versionName = null;
    private int versionCode = 0;
    MyFragment mMyFragment;
    private boolean isDialogShow = false;
    private static int MY_PERMISSIONS_REQUEST = 10;
    LocationManager locationManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_main);
//        getDeviceInfo();
        initView();
        init();
        if (DataCache.instance.getCacheData("haili", "LoginResponse") != null) {
            getUserInfo();
        }
        checkLocationAuthorization();
        checkVersion();
        NBSAppAgent.setLicenseKey("2c556b63aa334f1a895bccfcdca9eba1").withLocationServiceEnabled(true).start(this.getApplicationContext());
    }

    private void checkLocationAuthorization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }


    private void init() {
        initEvent();
        if (getIntent().getIntExtra("position", 0) == 1) {
            setSelect(1);
        } else if (getIntent().getIntExtra("position", 0) == 2) {
            setSelect(2);
        } else {
            setSelect(0);
        }

        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "LoginResponse");
        if (loginResponse != null) {
            if (!PreferencesKeeper.getGuestState(MainActivity.this, loginResponse.loginModel.userId)) {
                if (StringUtils.isEmpty(PreferencesKeeper.getGestureKey(MainActivity.this, loginResponse.loginModel.userId))) {
                    ViewHelper.buildNoTitleTextDialog4(MainActivity.this, "去设置手势密码？", new ViewHelper.OnPositiveBtnClickedListener() {
                        @Override
                        public void onPositiveBtnClicked(MaterialDialog dialog) {
                            Intent intent = new Intent(MainActivity.this, GestureActivity.class);
                            intent.putExtra("type", 2);
                            startActivity(intent);
                        }
                    }).show();
                }
                PreferencesKeeper.saveGuestShowState(MainActivity.this, loginResponse.loginModel.userId);
            }
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "RedPacketFragment":
                setSelect(1);
                break;
            case "GetUserInfo":
                getUserInfo();
                break;
            case "FINISH_LOGIN":
                setSelect(3);
                showNewUserRedPackage();
                mHomeFragment.initProduct();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5556 && resultCode == 5555) {
            String result = data.getStringExtra("result");
            if ("goToManage".equals(result)) {
                setSelect(1);
            }
        }
    }

    private void setSelect(int i) {
        clearImageView();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                iv_home.setImageResource(R.mipmap.tab_homepager_selected);
                tv_home.setTextColor(getResources().getColor(R.color.tab_selected_color));
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_ui, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                iv_licai.setImageResource(R.mipmap.tab_licai_selected);
                tv_licai.setTextColor(getResources().getColor(R.color.tab_selected_color));
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fragment_ui, manageFragment);
                } else {
                    transaction.show(manageFragment);
                }
                break;
            case 2:
                iv_asstes.setImageResource(R.mipmap.tab_assets_selected);
                tv_asstes.setTextColor(getResources().getColor(R.color.tab_selected_color));
                if (mAssetsFragment == null) {
                    mAssetsFragment = new AssetsFragment();
                    transaction.add(R.id.fragment_ui, mAssetsFragment);
                } else {
                    transaction.show(mAssetsFragment);
                }
                break;
            case 3:
                iv_my.setImageResource(R.mipmap.tab_my_select);
                tv_my.setTextColor(getResources().getColor(R.color.tab_selected_color));
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    transaction.add(R.id.fragment_ui, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                }
                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void initEvent() {
        rl_home.setOnClickListener(this);
        rl_licai.setOnClickListener(this);
        rl_asstes.setOnClickListener(this);
        rl_my.setOnClickListener(this);
    }

    private void initView() {
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_licai = (RelativeLayout) findViewById(R.id.rl_licai);
        rl_asstes = (RelativeLayout) findViewById(R.id.rl_asstes);
        rl_my = (RelativeLayout) findViewById(R.id.rl_my);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_licai = (ImageView) findViewById(R.id.iv_licai);
        iv_asstes = (ImageView) findViewById(R.id.iv_asstes);
        iv_my = (ImageView) findViewById(R.id.iv_my);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_licai = (TextView) findViewById(R.id.tv_licai);
        tv_asstes = (TextView) findViewById(R.id.tv_asstes);
        tv_my = (TextView) findViewById(R.id.tv_my);
    }

    private void clearImageView() {
        iv_home.setImageResource(R.mipmap.tab_homepager_normal);
        tv_home.setTextColor(getResources().getColor(R.color.tab_normal_color));
        iv_licai.setImageResource(R.mipmap.tab_licai_normal);
        tv_licai.setTextColor(getResources().getColor(R.color.tab_normal_color));
        iv_asstes.setImageResource(R.mipmap.tab_assets_normal);
        tv_asstes.setTextColor(getResources().getColor(R.color.tab_normal_color));
        iv_my.setImageResource(R.mipmap.tab_my_normal);
        tv_my.setTextColor(getResources().getColor(R.color.tab_normal_color));
    }

    private void hideFragment(FragmentTransaction transaction2) {

        if (mHomeFragment != null) {
            transaction2.hide(mHomeFragment);
        }

        if (manageFragment != null) {
            transaction2.hide(manageFragment);
        }

        if (mAssetsFragment != null) {
            transaction2.hide(mAssetsFragment);
        }

        if (mMyFragment != null) {
            transaction2.hide(mMyFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isDialogShow) {
            exit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            if (isDialogShow) {
                isDialogShow = false;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) < 2000) {
            isDialogShow = true;
            ViewHelper.buildNoTitleTextDialog3(MainActivity.this, getString(R.string.exit_app), new ViewHelper.OnPositiveBtnClickedListener() {
                @Override
                public void onPositiveBtnClicked(MaterialDialog dialog) {
                    dialog.dismiss();
                    quit();
                }
            }, new ViewHelper.OnNegativeBtnClickedListener() {
                @Override
                public void onNegativeBtnClicked(MaterialDialog dialog) {
                    dialog.dismiss();
                    exitTime = 0;
                    isDialogShow = false;
                }
            }).show();
        } else {
            exitTime = System.currentTimeMillis();
        }
    }

    /*
     * 退出应用
     */
    protected void quit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        moveTaskToBack(true);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home:
                if (mHomeFragment == null) {
                    setSelect(0);
                } else {
                    if (!mHomeFragment.isVisible()) {
                        setSelect(0);
                    }
                }
                break;
            case R.id.rl_licai:
                if (manageFragment == null) {
                    setSelect(1);
                } else {
                    if (!manageFragment.isVisible()) {
                        setSelect(1);
                    }
                }
                break;
            case R.id.rl_asstes:
                if (DataCache.instance.getCacheData("haili", "LoginResponse") == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return;
                }
                if (mAssetsFragment == null) {
                    setSelect(2);
                } else {
                    if (!mAssetsFragment.isVisible()) {
                        setSelect(2);
                    }
                }

                break;
            case R.id.rl_my:
                if (mMyFragment == null) {
                    setSelect(3);
                } else {
                    if (!mMyFragment.isVisible()) {
                        setSelect(3);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManage != null) {
            //移除监听器
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManage.removeUpdates(locationListener);
        }
    }


    private void getUserInfo() {
        final MyInfoRequest request = new MyInfoRequest();
        subscrip2 = UserBusinessHelper.myInfo(request)
                .subscribe(new Action1<LoginResponse>() {
                    @Override
                    public void call(LoginResponse loginResponse) {
                        DataCache.instance.saveCacheData("haili", "MyInfoResponse", loginResponse);
                        LoginResponse response = DataCache.instance.getCacheData("haili", "LoginResponse");
                        if (response != null) {
                            loginResponse.loginModel.accessToken = response.loginModel.accessToken;
                            loginResponse.loginModel.userId = response.loginModel.userId;
                            DataCache.instance.saveCacheData("haili", "LoginResponse", loginResponse);
                        }
                        Bus.getDefault().post("GetUserInfoDone");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(MainActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    public void getLocation() {
        String locationProvider = "";
        //获取地理位置管理器
        locationManage = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManage.getProviders(true);
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            //如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManage.getLastKnownLocation(locationProvider);
        if (location != null) {
            DeviceInfoModel model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
            if (model != null) {
                model.gps = location.getLongitude() + "," + location.getLatitude();
                DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
            }
        }
        locationManage.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    /*
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            DeviceInfoModel model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
            if (model != null) {
                model.gps = location.getLongitude() + "," + location.getLatitude();
                DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManage.removeUpdates(locationListener);
            }
        }
    };

    private void checkVersion() {
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        subscrip = UserBusinessHelper.upDateVersion(new UpDateRequest())
                .subscribe(new Action1<UpDateResponse>() {
                    @Override
                    public void call(UpDateResponse upDateResponse) {

                        if (versionCode < Double.parseDouble(upDateResponse.dataModel.versionCode)) {//判断当前版本是不是最新版本
                            if ("0".equals(upDateResponse.dataModel.forceUpdate)) {//强制更新
                                String hintText = "当前版本V" + versionName + "最新版本V" + upDateResponse.dataModel.versionName + ",此次更新为重大版本更新！请更新版本后再使用！";
                                UpDateFragment upDateFragment = UpDateFragment.newInstance("updateStart", hintText, upDateResponse.dataModel.updateUrl, 10);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.framelayout_main, upDateFragment, UpDateFragment.TAG)
                                        .commitAllowingStateLoss();
                            } else {
                                String hintText = "当前版本V" + versionName + " 最新版本V" + upDateResponse.dataModel.versionName + ",确认下载更新？";
                                UpDateFragment upDateFragment = UpDateFragment.newInstance("updateStart", hintText, upDateResponse.dataModel.updateUrl, 0);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.framelayout_main, upDateFragment, UpDateFragment.TAG)
                                        .commitAllowingStateLoss();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(MainActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    public void showNewUserRedPackage() {
        RegisterRedPackageFragment registerRedPackage = RegisterRedPackageFragment.newInstance("RegisterRedPackage");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_main, registerRedPackage, RegisterRedPackageFragment.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip != null) {
            subscrip.unsubscribe();
        }
        if (subscrip2 != null) {
            subscrip2.unsubscribe();
        }
    }
}
