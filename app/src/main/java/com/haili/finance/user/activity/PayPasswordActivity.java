package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.PayPasswordRequest;
import com.haili.finance.business.user.PayPasswordResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/3/16.
 */

public class PayPasswordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private Subscription subscrip, subscrip2, subscrip3;
    private LinearLayout stepOne, stepTwo;
    private EditText etPhone, etMsg, etPasswordOne, etPasswordTwo;
    private TextView tvMsg;
    private TimeCount timeCount;
    private Button btnNext, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            setBarTitle("设置支付密码");
        } else {
            setBarTitle(title);
        }
        initView();
        init();
    }

    private void init() {
        initListener();
        String params = getIntent().getStringExtra("params");
        if ("10086".equals(params)) {
            stepOne.setVisibility(View.GONE);
            stepTwo.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        tvMsg.setOnClickListener(this);
        etPhone.addTextChangedListener(this);
        etMsg.addTextChangedListener(this);
        etPasswordOne.addTextChangedListener(this);
        etPasswordTwo.addTextChangedListener(this);
    }

    private void initView() {
        stepOne = (LinearLayout) findViewById(R.id.pay_password_one);
        etPhone = (EditText) findViewById(R.id.pay_password_phone);
        etMsg = (EditText) findViewById(R.id.pay_password_msg);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        btnNext = (Button) findViewById(R.id.pay_password_next);

        stepTwo = (LinearLayout) findViewById(R.id.pay_password_two);
        etPasswordOne = (EditText) findViewById(R.id.pay_password_psd_one);
        etPasswordTwo = (EditText) findViewById(R.id.pay_password_psd_two);
        btnFinish = (Button) findViewById(R.id.pay_password_finish);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_password_next://下一页
                if (checkValue(2)) {
                    addLoadingFragment(R.id.pay_password_fl, "PayPasswordActivityNEXT");
                }
                break;
            case R.id.pay_password_finish://设置支付密码
                if (checkValue(3)) {
                    addLoadingFragment(R.id.pay_password_fl, "PayPasswordActivityFINISH");
                }
                break;
            case R.id.tv_msg://发验证码
                if (checkValue(1)) {
                    addLoadingFragment(R.id.pay_password_fl, "PayPasswordActivitySENDMSG");
                }
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "PayPasswordActivityNEXT":
                goNext();
                break;
            case "PayPasswordActivityFINISH":
                changePayPassword();
                break;
            case "PayPasswordActivitySENDMSG":
                sendMsg();
                break;
        }
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
                if (etPasswordOne.getText().toString().length() < 6) {
                    ViewHelper.showToast(this, "请输入正确的密码，长度为6位数字");
                    return false;
                }
                if (etPasswordTwo.getText().toString().length() < 6) {
                    ViewHelper.showToast(this, "请输入正确的密码，长度为6位数字");
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

    private void sendMsg() {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = etPhone.getText().toString();
        request.type = 8;
        subscrip3 = UserBusinessHelper.getMsgCode(request)
                .subscribe(new Action1<RegisterMsgCodeResponse>() {
                    @Override
                    public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {

                        if (tvMsg != null) {
                            timeCount = new TimeCount(60000, 1000, tvMsg, PayPasswordActivity.this, 1);
                            timeCount.start();
                        }

                        //获取短信验证码
                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(PayPasswordActivity.this, requestErrorThrowable.getMessage());
                        }
                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                });
    }

    private void goNext() {
        CheckMessageCodeRequest request = new CheckMessageCodeRequest();
        request.phone = etPhone.getText().toString();
        request.verifyCode = etMsg.getText().toString();
        request.type = "8";
        subscrip2 = UserBusinessHelper.checkMessageCode(request)
                .subscribe(new Action1<CheckMessageCodeResponse>() {
                    @Override
                    public void call(CheckMessageCodeResponse checkMessageCodeResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }
                        if (getIntent().getIntExtra("type", 6) == 1) {
                            Intent myIntent = new Intent(PayPasswordActivity.this, GestureActivity.class);
                            myIntent.putExtra("type", 1);
                            startActivity(myIntent);
                            finish();
                        }
                        if (checkMessageCodeResponse.errorCode.equals("000000")) {
                            if (timeCount != null) {
                                timeCount.cancel();
                            }
                            stepOne.setVisibility(View.GONE);
                            stepTwo.setVisibility(View.VISIBLE);
                        } else {
                            ViewHelper.showToast(PayPasswordActivity.this, checkMessageCodeResponse.errorMsg);
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
                            ViewHelper.showToast(PayPasswordActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void changePayPassword() {
        PayPasswordRequest request = new PayPasswordRequest();
        request.pwd = etPasswordOne.getText().toString();
        subscrip = UserBusinessHelper.payPassword(request)
                .subscribe(new Action1<PayPasswordResponse>() {
                    @Override
                    public void call(PayPasswordResponse payPasswordResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (payPasswordResponse.dataModel != null) {
                            if ("0".equals(payPasswordResponse.dataModel.state)) {
                                //success
                                LoginResponse cacheData = DataCache.instance.getCacheData("haili", "LoginResponse");
                                cacheData.loginModel.certificationState = "4";
                                DataCache.instance.saveCacheData("haili", "LoginResponse", cacheData);
                                LoginResponse myinfo = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                                myinfo.loginModel.certificationState = "4";
                                DataCache.instance.saveCacheData("haili", "MyInfoResponse", myinfo);
                                startActivity(new Intent(PayPasswordActivity.this, AuthenticationFinishActivity.class));
                                finish();
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
                            ViewHelper.showToast(PayPasswordActivity.this, requestErrorThrowable.getMessage());
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
