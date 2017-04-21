package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.CheckMessageCodeRequest;
import com.haili.finance.business.user.CheckMessageCodeResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.RetrievePasswordRequest;
import com.haili.finance.business.user.RetrievePasswordResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by fuliang on 2017/3/14.
 */

public class FindLoginPassword extends BaseActivity implements View.OnClickListener, TextWatcher {

    private LinearLayout stepOne, stepTwo;
    private EditText etPhone, etMsg, etPasswordOne, etPasswordTwo;
    private Button btnFinish, btnNext;
    private TextView tvMsg;
    private Subscription subscrip, subscrip2, subscrip3;
    private String phoneNum, msg;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_login_psw);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("找回登录密码");
        MobclickAgent.onEvent(FindLoginPassword.this, "Login_forgetpwd_action");
        initView();
        init();
    }

    private void init() {
        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        tvMsg.setOnClickListener(this);
        etPhone.addTextChangedListener(this);
        etMsg.addTextChangedListener(this);
        etPasswordOne.addTextChangedListener(this);
        etPasswordTwo.addTextChangedListener(this);
    }

    private void initView() {
        stepOne = (LinearLayout) findViewById(R.id.find_login_password_step_one);
        stepTwo = (LinearLayout) findViewById(R.id.find_login_password_step_two);
        etPhone = (EditText) findViewById(R.id.find_login_password_phone);
        etMsg = (EditText) findViewById(R.id.find_login_password_msg);
        etPasswordOne = (EditText) findViewById(R.id.find_login_password_one);
        etPasswordTwo = (EditText) findViewById(R.id.find_login_password_two);
        btnFinish = (Button) findViewById(R.id.find_login_finish);
        btnNext = (Button) findViewById(R.id.find_login_password_next);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
    }

    private boolean checkValue(int type) {
        switch (type) {
            case 1:
                if (!StringUtils.isPhone(etPhone.getText().toString())) {
                    ViewHelper.showToast(this, "请输入正确的手机号");
                    return false;
                }
                return true;
            case 2:
                if (!StringUtils.isPhone(etPhone.getText().toString())) {
                    ViewHelper.showToast(this, "请输入正确的手机号");
                    return false;
                }
                if (etMsg.getText().toString().length() < 6) {
                    ViewHelper.showToast(this, "请输入正确的验证码");
                    return false;
                }
                return true;
            case 3:
                if (etPasswordOne.getText().toString().length() < 6 || isZZ(etPasswordOne.getText().toString())) {
                    ViewHelper.showToast(this, "请输入正确的密码，包含大小写和数字，长度为6-16位");
                    return false;
                }
                if (!etPasswordTwo.getText().toString().equals(etPasswordOne.getText().toString())) {
                    ViewHelper.showToast(this, "两次密码输入不一致");
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    private boolean isZZ(String psd) {//判断是否是包含大写或小写字母和数字的正则表达式
        String regex = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        return !psd.matches(regex);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etPasswordTwo.getText().toString().length() > 0 && etPasswordOne.getText().toString().length() > 0) {
            btnFinish.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btnFinish.setEnabled(true);
        } else {
            btnFinish.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btnFinish.setEnabled(false);
        }
        if (etPhone.getText().toString().length() > 0 && etMsg.getText().toString().length() > 0) {
            btnNext.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btnNext.setEnabled(true);
        } else {
            btnNext.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btnNext.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_login_password_next:
                if (checkValue(2)) {
                    addLoadingFragment(R.id.find_login_fl, "FindLoginPasswordNEXT");
                }
                break;
            case R.id.find_login_finish:
                if (checkValue(3)) {
                    addLoadingFragment(R.id.find_login_fl, "FindLoginPasswordFINISH");
                }
                break;
            case R.id.tv_msg:
                if (checkValue(1)) {
                    addLoadingFragment(R.id.find_login_fl, "FindLoginPasswordSENDMSG");
                }
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "FindLoginPasswordNEXT":
                goNext();
                break;
            case "FindLoginPasswordFINISH":
                changePayPassword();
                break;
            case "FindLoginPasswordSENDMSG":
                sendMsg();
                break;
        }
    }

    private void changePayPassword() {
        RetrievePasswordRequest request = new RetrievePasswordRequest();
        request.newPwd = etPasswordOne.getText().toString();
        request.phone = phoneNum;
        request.phoneCode = msg;
        subscrip = UserBusinessHelper.retrivePassword(request)
                .subscribe(new Action1<RetrievePasswordResponse>() {
                    @Override
                    public void call(RetrievePasswordResponse retrievePasswordResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (retrievePasswordResponse.dataModel != null) {
                            if ("0".equals(retrievePasswordResponse.dataModel.updateState)) {
                                ViewHelper.showToast(FindLoginPassword.this, "已重置登录密码");
                                Intent intent = new Intent();
                                intent.putExtra("result", true);
                                setResult(12306, intent);
                                finish();
                            } else {
                                ViewHelper.showToast(FindLoginPassword.this, retrievePasswordResponse.errorMsg);
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(FindLoginPassword.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void goNext() {
        CheckMessageCodeRequest request = new CheckMessageCodeRequest();
        request.phone = etPhone.getText().toString();
        request.verifyCode = etMsg.getText().toString();
        request.type = "3";
        subscrip2 = UserBusinessHelper.checkMessageCode(request)
                .subscribe(new Action1<CheckMessageCodeResponse>() {
                    @Override
                    public void call(CheckMessageCodeResponse checkMessageCodeResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (checkMessageCodeResponse.errorCode.equals("000000")) {
                            phoneNum = etPhone.getText().toString();
                            msg = etMsg.getText().toString();
                            if (timeCount != null) {
                                timeCount.cancel();
                            }
                            stepOne.setVisibility(View.GONE);
                            stepTwo.setVisibility(View.VISIBLE);
                        } else {
                            ViewHelper.showToast(FindLoginPassword.this, checkMessageCodeResponse.errorMsg);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(FindLoginPassword.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void sendMsg() {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = etPhone.getText().toString();
        request.type = 3;
        subscrip3 = UserBusinessHelper.getMsgCode(request)
                .subscribe(new Action1<RegisterMsgCodeResponse>() {
                    @Override
                    public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        timeCount = new TimeCount(60000, 1000, tvMsg, FindLoginPassword.this, 1);
                        timeCount.start();
                        //获取短信验证码

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(FindLoginPassword.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
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
        if (subscrip3 != null) {
            subscrip3.unsubscribe();
        }
    }
}