package com.haili.finance.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.RealNameRequest;
import com.haili.finance.business.user.RealNameResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Monkey on 2017/1/20.
 * 填写个人身份信息
 */
public class IDInformationActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private Button btn_next;
    private EditText etIdCode, etName;
    private Subscription subscrip;
    private FrameLayout fl_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_id_information);
        MobclickAgent.onEvent(this, "MyAccount_notauth_action");
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("填写个人信息");
        initView();
        init();
    }

    private void initView() {
        btn_next = (Button) findViewById(R.id.btn_next);
        etName = (EditText) findViewById(R.id.id_information_name);
        etIdCode = (EditText) findViewById(R.id.id_infomation_code);
        fl_loading = (FrameLayout) findViewById(R.id.loading_layout);
        etName.addTextChangedListener(this);
        etIdCode.addTextChangedListener(this);
    }

    private void init() {
        initListener();
    }

    private void initListener() {
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etIdCode.getWindowToken(), 0);
                if (checkValue()) {
                    addLoadingFragment(R.id.loading_layout, "IDInformationActivity");
                }
                break;
            default:
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("IDInformationActivity")) {
            realName();
        }
    }

    private void realName() {
        RealNameRequest realNameRequest = new RealNameRequest();
        realNameRequest.IDCard = etIdCode.getText().toString();
        realNameRequest.ruleName = etName.getText().toString();
        subscrip=  UserBusinessHelper.realName(realNameRequest)
                .subscribe(new Action1<RealNameResponse>() {
                    @Override
                    public void call(RealNameResponse realNameResponse) {
                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if ("000000".equals(realNameResponse.errorCode)) {
                            LoginResponse login = DataCache.instance.getCacheData("haili", "LoginResponse");
                            login.loginModel.ruleName = etName.getText().toString();
                            login.loginModel.IDCard = etIdCode.getText().toString();
                            login.loginModel.certificationState = "2";
                            DataCache.instance.saveCacheData("haili", "LoginResponse", login);
                            LoginResponse myINfo = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                            myINfo.loginModel.ruleName = etName.getText().toString();
                            myINfo.loginModel.IDCard = etIdCode.getText().toString();
                            myINfo.loginModel.certificationState = "2";
                            DataCache.instance.saveCacheData("haili", "MyInfoResponse", myINfo);

                            Intent intent = new Intent(IDInformationActivity.this, AddBankCardActivity.class);
                            intent.putExtra("name", etName.getText().toString());
                            startActivity(intent);
                            finish();
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
                            ViewHelper.showToast(IDInformationActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private boolean checkValue() {
        if (etIdCode.getText().toString().length() < 18) {
            ViewHelper.showToast(this, "请输入正确的身份证号码");
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etName.getText().toString().length() > 0 && etIdCode.getText().toString().length() > 0) {
            btn_next.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btn_next.setEnabled(true);
        } else {
            btn_next.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btn_next.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.getDefault().post("REFRESH_MY_FRAGMENT");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
