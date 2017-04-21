package com.haili.finance.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.AddBankCardRequest;
import com.haili.finance.business.user.AddBankCardResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Monkey on 2017/1/20.
 * 添加银行卡
 */
public class AddBankCardActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.add_bank_cardnum)
    EditText etCardNum;
    @Bind(R.id.tv_bank)
    TextView tv_bank;
    @Bind(R.id.ll_select_bank)
    LinearLayout ll_select_bank;
    @Bind(R.id.add_bank_phone)
    EditText etPhone;
    @Bind(R.id.edit_msg)
    EditText etMsg;
    @Bind(R.id.tv_getmsg)
    TextView tv_getmsg;
    @Bind(R.id.btn_finish)
    Button btn_finish;

    private TimeCount time;
    private Subscription subscrip, subscrip2;
    private String cbdNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bankcard);
        ButterKnife.bind(this);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("添加银行卡");
        initView();
        init();
    }

    private void initView() {
        LoginResponse cacheData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (cacheData != null) {
            if (!TextUtils.isEmpty(cacheData.loginModel.ruleName)) {
                ((TextView) findViewById(R.id.add_bank_name)).setText(StringUtils.nameAddStar(cacheData.loginModel.ruleName));
            }
        }
    }

    private void init() {
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        if (!TextUtils.isEmpty(i.getStringExtra("bank"))) {
            tv_bank.setText(i.getStringExtra("bank"));
        }
    }

    private void initListener() {
        etCardNum.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etMsg.addTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345 && resultCode == 67890) {
            cbdNo = data.getStringExtra("CBDNo");
            tv_bank.setText(data.getStringExtra("bank"));
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "AddBankCardActivity":
                addBankCard();
                break;
            case "AddBankCardActivityGETMSG":
                getMessageCode();
                break;
        }
    }

    private void addBankCard() {
        AddBankCardRequest request = new AddBankCardRequest();
        request.bank_account_no = etCardNum.getText().toString();
        request.CBDNo = cbdNo;
        request.MsgCode = etMsg.getText().toString();
        request.phone_no = etPhone.getText().toString();
        subscrip = UserBusinessHelper.addBankCard(request)
                .subscribe(new Action1<AddBankCardResponse>() {
                    @Override
                    public void call(AddBankCardResponse addBankCardResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (addBankCardResponse.errorCode.equals("000000")) {
                            LoginResponse cacheData = DataCache.instance.getCacheData("haili", "LoginResponse");
                            cacheData.loginModel.bankNum += 1;
                            if ("2".equals(cacheData.loginModel.certificationState)) {
                                cacheData.loginModel.certificationState = "3";
                            } else if ("5".equals(cacheData.loginModel.certificationState)) {
                                cacheData.loginModel.certificationState = "4";
                            }
                            cacheData.loginModel.bankNum += 1;
                            DataCache.instance.saveCacheData("haili", "LoginResponse", cacheData);
                            LoginResponse myinfo = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                            myinfo.loginModel.bankNum += 1;
                            if ("2".equals(myinfo.loginModel.certificationState)) {
                                myinfo.loginModel.certificationState = "3";
                            } else if ("5".equals(myinfo.loginModel.certificationState)) {
                                myinfo.loginModel.certificationState = "4";
                            }
                            myinfo.loginModel.bankNum += 1;
                            DataCache.instance.saveCacheData("haili", "MyInfoResponse", myinfo);
                            Bus.getDefault().post("REFRESH_MY_FRAGMENT");
                            MobclickAgent.onEvent(AddBankCardActivity.this, "BandCard_finishband_action");
                            if ("3".equals(myinfo.loginModel.certificationState)) {
                                Intent intent = new Intent(AddBankCardActivity.this, PayPasswordActivity.class);
                                intent.putExtra("params", "10086");
                                intent.putExtra("title", "设置支付密码");
                                startActivity(intent);
                            }
                            setResult(666, new Intent().putExtra("certificationState", myinfo.loginModel.certificationState));
                            finish();
                        } else {
                            ViewHelper.showToast(AddBankCardActivity.this, addBankCardResponse.errorMsg);
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
                            ViewHelper.showToast(AddBankCardActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private boolean checkValue() {
        if (etCardNum.getText().toString().length() < 19) {
            ViewHelper.showToast(AddBankCardActivity.this, "请输入正确的银行卡号");
            return false;
        }
        if ("".equals(cbdNo)) {//开户行信息
            ViewHelper.showToast(AddBankCardActivity.this, "请选择银行卡开户行");
            return false;
        }
        if (!StringUtils.isPhone(etPhone.getText().toString())) {
            ViewHelper.showToast(AddBankCardActivity.this, "请输入正确的电话号码");
            return false;
        }
        if (etMsg.getText().toString().length() < 6) {
            ViewHelper.showToast(AddBankCardActivity.this, "请输入正确的验证码");
            return false;
        }
        return true;
    }

    public void getMessageCode() {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.imgCode = "";
        request.phone = etPhone.getText().toString();
        request.type = 6;
        subscrip2 = UserBusinessHelper.getMsgCode(request)
                .subscribe(new Action1<RegisterMsgCodeResponse>() {
                    @Override
                    public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {
                        //todo 验证码获取

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        time = new TimeCount(60000, 1000, tv_getmsg, AddBankCardActivity.this, 1);
                        time.start();
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
                            ViewHelper.showToast(AddBankCardActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etPhone.getText().toString().length() > 0 && etCardNum.getText().toString().length() > 0 && etMsg.getText().toString().length() > 0) {
            btn_finish.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btn_finish.setEnabled(true);
        } else {
            btn_finish.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btn_finish.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
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
    }

    @OnClick({R.id.ll_select_bank, R.id.tv_getmsg, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_bank:
                Intent intent = new Intent(this, SelectOpeanBankActivity.class);
                intent.putExtra("bankNO", cbdNo);
                startActivityForResult(intent, 12345);
                break;
            case R.id.tv_getmsg:
                if (StringUtils.isPhone(etPhone.getText().toString())) {
                    addLoadingFragment(R.id.activity_add_bankcard_fl, "AddBankCardActivityGETMSG");
                } else {
                    ViewHelper.showToast(this, "请输入正确的手机号");
                }
                break;
            case R.id.btn_finish:
                if (checkValue()) {//验证
                    addLoadingFragment(R.id.activity_add_bankcard_fl, "AddBankCardActivity");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCardNum.getWindowToken(), 0);
                }
                break;
        }
    }
}
