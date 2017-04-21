package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.stroage.PreferencesKeeper;
import com.haili.finance.user.fragment.ChangePhoneNumberFragment;
import com.haili.finance.user.fragment.UserChangeLoginPasswordFragment;
import com.haili.finance.utils.StringUtils;
import com.mcxiaoke.bus.Bus;

/**
 * Created by lfu on 2017/2/22.
 */

public class UserAccountSecurityActivity extends BaseActivity implements View.OnClickListener {

    private SwitchCompat gesturesSwitch;
    private LinearLayout unLoginLayout, changePhoneNumberLayout, changeLoginPasswordLayout, changeAccountPasswordLayout, changeGesturesPasswordLayout;
    private TextView phoneNumber;
    private ChangePhoneNumberFragment changePhoneNumberFragment;
    private UserChangeLoginPasswordFragment changeLoginPasswordFragment;
//    private ChangeAccountPwdFragment changeAccountPwdFragment;

    private boolean isFragmentShow = false;

    public UserAccountSecurityActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle(getString(R.string.account_security));
        initView();
        initData();
        addClickListener();
        addSwitchControl();
    }

    private void initView() {
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        LoginResponse cacheData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        String phone = cacheData.loginModel.phone.substring(0, 3) +  /*phone.substring(3, 7) */"****" + cacheData.loginModel.phone.substring(7, 11);
        phoneNumber.setText(phone);
        gesturesSwitch = (SwitchCompat) findViewById(R.id.gestures_switch);
        changePhoneNumberLayout = (LinearLayout) findViewById(R.id.change_phone_number);
        changeLoginPasswordLayout = (LinearLayout) findViewById(R.id.change_login_password_layout);
        changeAccountPasswordLayout = (LinearLayout) findViewById(R.id.change_account_password_layout);
        changeGesturesPasswordLayout = (LinearLayout) findViewById(R.id.change_gestures_password_layout);
        unLoginLayout = (LinearLayout) findViewById(R.id.change_unlogin);
    }

    private void initData() {
        LoginResponse response = DataCache.instance.getCacheData("haili", "LoginResponse");
        if (StringUtils.isEmpty(PreferencesKeeper.getGestureKey(UserAccountSecurityActivity.this, response.loginModel.userId))) {
            changeGesturesPasswordLayout.setVisibility(View.GONE);
            gesturesSwitch.setChecked(false);
        } else {
            if (PreferencesKeeper.getGuestState(UserAccountSecurityActivity.this, response.loginModel.userId)) {
                gesturesSwitch.setChecked(true);
                changeGesturesPasswordLayout.setVisibility(View.VISIBLE);
            } else {
                changeGesturesPasswordLayout.setVisibility(View.GONE);
                gesturesSwitch.setChecked(false);
            }
        }
    }

    private void addClickListener() {
        changePhoneNumberLayout.setOnClickListener(this);
        changeLoginPasswordLayout.setOnClickListener(this);
        changeAccountPasswordLayout.setOnClickListener(this);
        changeGesturesPasswordLayout.setOnClickListener(this);
        unLoginLayout.setOnClickListener(this);
    }

    private void addSwitchControl() {
        gesturesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LoginResponse response = DataCache.instance.getCacheData("haili", "LoginResponse");
                if (isChecked) {
                    String key = PreferencesKeeper.getGestureKey(UserAccountSecurityActivity.this, response.loginModel.userId);
                    if (key.equals("")) {
                        Intent intent = new Intent(UserAccountSecurityActivity.this, GestureActivity.class);
                        intent.putExtra("type", 2);
                        startActivityForResult(intent, 1234);
                    } else {
                        changeGesturesPasswordLayout.setVisibility(View.VISIBLE);
                        PreferencesKeeper.saveGuestState(UserAccountSecurityActivity.this, response.loginModel.userId, true);
                    }
                } else {
                    changeGesturesPasswordLayout.setVisibility(View.GONE);
                    PreferencesKeeper.saveGuestState(UserAccountSecurityActivity.this, response.loginModel.userId, false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_phone_number:
            LoginResponse cachData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                if ("0".equals(cachData.loginModel.certificationState)) {
                    finish();
                } else if ("1".equals(cachData.loginModel.certificationState)) {
                    
                }
                showChangePhoneFragment();
                break;
            case R.id.change_login_password_layout:
                showChangeLoginPwdFragment();
                break;
            case R.id.change_account_password_layout:
            LoginResponse cacheData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                if ("0".equals(cacheData.loginModel.certificationState)) {
                    finish();
                } else if ("1".equals(cacheData.loginModel.certificationState)) {
                    startActivity(new Intent(UserAccountSecurityActivity.this, IDInformationActivity.class));//用户实名认证
                } else if ("2".equals(cacheData.loginModel.certificationState)) {
                    Intent intent = new Intent(UserAccountSecurityActivity.this, AddBankCardActivity.class);
                    startActivity(intent);
                } else if ("3".equals(cacheData.loginModel.certificationState)) {
                    Intent intent = new Intent(UserAccountSecurityActivity.this, PayPasswordActivity.class);
                    intent.putExtra("title", "设置支付密码");
                    startActivity(intent);
                } else if ("4".equals(cacheData.loginModel.certificationState) || "5".equals(cacheData.loginModel.certificationState)) {
                    Intent intent = new Intent(UserAccountSecurityActivity.this, PayPasswordActivity.class);
                    intent.putExtra("title", "修改支付密码");
                    startActivity(intent);
                }
                break;
            case R.id.change_gestures_password_layout:
                //修改手势密码跳进去，验证手机验证码，然后跳出PayPasswordActivity，进入手势密码界面
                Intent myIntent = new Intent(UserAccountSecurityActivity.this, PayPasswordActivity.class);
                myIntent.putExtra("title", "身份验证");
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
                break;
            case R.id.change_unlogin:
                ViewHelper.buildNoTitleTextDialog(UserAccountSecurityActivity.this, "确定要退出登录吗？", new ViewHelper.OnPositiveBtnClickedListener() {
                    @Override
                    public void onPositiveBtnClicked(MaterialDialog dialog) {
                        unLogin();
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    private void unLogin() {
        DataCache.instance.clearCacheData("haili", "LoginResponse");
        DataCache.instance.clearCacheData("haili", "MyInfoResponse");
        DataCache.instance.clearCacheData("haili", "RegularResponse");
        DataCache.instance.clearCacheData("haili", "MyEarningsResponse1");
        DataCache.instance.clearCacheData("haili", "MyEarningsResponse2");
        DataCache.instance.clearCacheData("haili", "MyEarningsResponse3");
        DeviceInfoModel model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
        if (model != null) {
            model.accessToken = "";
        }
        DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
        Bus.getDefault().post("unLogin");
        finish();
    }

    //    private void showChangeAccountPwdFragment() {
//        if (changeAccountPwdFragment == null) {
//            changeAccountPwdFragment = new ChangeAccountPwdFragment();
//        }
//        getFragmentManager().beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
//                .add(R.id.change_login_fragment, changeAccountPwdFragment, "12234")
//                .commit();
//        setBarTitle("修改支付密码");
//        isFragmentShow = true;
//    }
//
    private void showChangePhoneFragment() {
        if (changePhoneNumberFragment == null) {
            changePhoneNumberFragment = new ChangePhoneNumberFragment();
        }
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                .add(R.id.change_phone_fragment, changePhoneNumberFragment, "12234")
                .commit();
        setBarTitle("修改手机号码");
        isFragmentShow = true;
    }

    private synchronized void showChangeLoginPwdFragment() {
        if (!isFragmentShow /*&& getFragmentManager().findFragmentById(R.id.change_login_fragment) == null*/) {
            if (changeLoginPasswordFragment == null) {
                changeLoginPasswordFragment = new UserChangeLoginPasswordFragment();
            }
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                    .add(R.id.change_login_fragment, changeLoginPasswordFragment, "12234")
                    .commitAllowingStateLoss();
            setBarTitle("修改登录密码");
            isFragmentShow = true;
        }
    }

    public void removeFragment() {
        if (changePhoneNumberFragment != null && isFragmentShow) {
            changePhoneNumberFragment.removeSelf();
            changePhoneNumberFragment = null;
        }
        if (changeLoginPasswordFragment != null && isFragmentShow) {
            changeLoginPasswordFragment.removeSelf();
            changeLoginPasswordFragment = null;
        }
        isFragmentShow = false;
        setBarTitle("账户安全");
//        if (changeAccountPwdFragment != null) {
//            getFragmentManager().beginTransaction()
//                    .setCustomAnimations(0, R.animator.slide_out_right)
//                    .remove(changeAccountPwdFragment).commitAllowingStateLoss();
//        }
    }

    @Override
    public void onBackPressed() {
        if (isFragmentShow) {
            removeFragment();
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                changeGesturesPasswordLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                gesturesSwitch.setChecked(false);
                changeGesturesPasswordLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (changeLoginPasswordFragment != null) {
            changeLoginPasswordFragment = null;
        }
    }
}
