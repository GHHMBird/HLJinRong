package com.haili.finance.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.base.BaseApplication;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.RegisterRequest;
import com.haili.finance.business.user.UserGetVerificationCodeRequest;
import com.haili.finance.business.user.UserGetVerificationCodeResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.BitmapHelper;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.functions.Action1;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText edit_phone, edit_msg, codeEditText, edit_password, edit_password2, editInvateCode;
    private TextView tv_getmsg, loginEntrance;
    private Button btn_register;
    private Subscription subscribe,subscribe2,subscribe3;
    private TextView imageView,agreementText;
    private CheckBox checkBox;
    private TimeCount time;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("注册");
        initView();
        init();
    }

    private void init() {
        initListener();
        String string = "已有账号？去登录";
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        style.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.bg_mainColor)), string.length() - 2, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        loginEntrance.setText(style);
    }

    private void initListener() {
        edit_password2.addTextChangedListener(this);
        edit_password.addTextChangedListener(this);
        edit_msg.addTextChangedListener(this);
        loginEntrance.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        agreementText.setOnClickListener(this);
        tv_getmsg.setOnClickListener(this);
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) { // 已经改变了的。
                String contents = str.toString();
                int length = contents.length();
                if (length == 4) {
                    if (contents.substring(3).equals(new String(" "))) { // -
                        contents = contents.substring(0, 3);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    } else { // +
                        contents = contents.substring(0, 3) + " " + contents.substring(3);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(new String(" "))) { // -
                        contents = contents.substring(0, 8);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    }
                }
                if (isRegisterCanClick()) {
                    btn_register.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
                    btn_register.setClickable(true);
                } else {
                    btn_register.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
                    btn_register.setClickable(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {// TODO Auto-generated method stub
            }
        });
    }

    private void initView() {
        editInvateCode = (EditText) findViewById(R.id.edit_yaoqing_code);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_msg = (EditText) findViewById(R.id.edit_msg);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password2 = (EditText) findViewById(R.id.edit_password2);
        tv_getmsg = (TextView) findViewById(R.id.tv_getmsg);
        btn_register = (Button) findViewById(R.id.btn_register);
        checkBox = (CheckBox) findViewById(R.id.regiest_cb);
        agreementText = (TextView)findViewById(R.id.agreement_text);
        checkBox.setChecked(true);
        loginEntrance = (TextView) findViewById(R.id.login_entrance);
        btn_register.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_entrance:
                onBackPressed();
                break;
            case R.id.agreement_text:
                Intent intent = new Intent(RegisterActivity.this, WebActivity.class);
                intent.putExtra("title","海狸金融用户注册协议");
                intent.putExtra("params","http://m.hailijr.com/site/pdf/view/regist.htm");
                startActivity(intent);
                break;
            case R.id.tv_getmsg:
                //获取验证码
                if (checkValue(1)) {
                    showDialog();
                }
                break;
            case R.id.btn_register:
                if (checkValue(2)) {
                    addLoadingFragment(R.id.laoding_layout, "DO_REGISTER");
                }
                break;
            default:
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("GET_MSG_CODE")) {
            getMsgCode(codeEditText.getText().toString());
            return;
        }
        if (event.equals("DO_REGISTER")) {
            doRegister();
            return;
        }
    }

    private void showDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.customView(addViewContext(), false);
        builder.title("请输入验证码");
        builder.autoDismiss(false);
        builder.positiveText(R.string.ok);
        builder.negativeText(R.string.cancel);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                hintkeybord(edit_phone);
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                hintkeybord(edit_phone);
                addLoadingFragment(R.id.laoding_layout, "GET_MSG_CODE");
            }
        });
        builder.show();
    }

    private View addViewContext() {
        View view = getLayoutInflater().inflate(R.layout.verification_code_layout, null);
        codeEditText = (EditText) view.findViewById(R.id.edit_text);
        imageView = (TextView) view.findViewById(R.id.image);
        getImageCode();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        codeEditText.setHint("请输入图形验证码");
        return view;
    }

    private void getImageCode() {
        UserGetVerificationCodeRequest request = new UserGetVerificationCodeRequest();
        request.phone = edit_phone.getText().toString().replaceAll(" ", "");
        request.type = 2;
        subscribe = UserBusinessHelper.getImageCode(request).subscribe(new Action1<UserGetVerificationCodeResponse>() {
            @Override
            public void call(UserGetVerificationCodeResponse userGetVerificationCodeResponse) {
                if (!userGetVerificationCodeResponse.dataModel.imgData.equals("")) {
                    String imageCode = userGetVerificationCodeResponse.dataModel.imgData;
                    Bitmap bitmap = BitmapHelper.getImageFromStr(imageCode.substring(imageCode.indexOf(","), imageCode.length()));
                    Matrix matrix = new Matrix();
                    matrix.postScale(2.5f, 5f); //长和宽放大缩小的比例
                    Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    imageView.setText("");
                    imageView.setBackground(new BitmapDrawable(resizeBmp));
                    imageView.setClickable(true);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                imageView.setClickable(true);
                imageView.setBackground(null);
                imageView.setText("点击重新获取");
            }
        });
    }

    private void getMsgCode(String code) {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = edit_phone.getText().toString().replaceAll(" ", "");
        request.imgCode = code;
        request.type = 2;
        subscribe2 =  UserBusinessHelper.getMsgCode(request).subscribe(new Action1<RegisterMsgCodeResponse>() {
            @Override
            public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                time = new TimeCount(60000, 1000, tv_getmsg, RegisterActivity.this, 1);
                time.start();
                if (registerMsgCodeResponse.result) {
                    ViewHelper.showToast(RegisterActivity.this, "验证码已发送");
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(RegisterActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private boolean checkValue(int type) {
        if (type == 1) {
            if (edit_phone.getText().toString().length() < 12 || StringUtils.isPhone(edit_phone.getText().toString())) {
                ViewHelper.showToast(this, "请输入正确的手机号码");
                return false;
            }
        } else {
            if (edit_phone.getText().toString().length() < 12 || StringUtils.isPhone(edit_phone.getText().toString())) {
                ViewHelper.showToast(this, "请输入正确的手机号码");
                return false;
            }
            if (edit_msg.getText().toString().length() < 6) {
                ViewHelper.showToast(this, "请输入正确的验证码");
                return false;
            }
            if (edit_password.getText().toString().length() < 6 || StringUtils.isZZ(edit_password.getText().toString())) {
                ViewHelper.showToast(this, "请输入正确的密码，包含大小写和数字，长度为6-16位");
                return false;
            }
//            if (edit_password.getText().toString().length() < 6 || !StringUtils.checkSpeical(edit_password.getText().toString())) {
//                ViewHelper.showToast(this, "请输入密码，长度为6-16位，必须包含数字和字母（大写或小写）");
//                return false;
//            }
//            if (StringUtils.isZZ(edit_password.getText().toString())){
//                ViewHelper.showToast(this, "请输入包含字母及数字的密码");
//                return false;
//            }
            if (!edit_password2.getText().toString().equals(edit_password.getText().toString())) {
                ViewHelper.showToast(this, "两次密码输入不一样");
                return false;
            }
            if (!checkBox.isChecked()) {
                ViewHelper.showToast(BaseApplication.getApplication(), "请勾选我已阅读并同意《海狸金融用户注册协议》");
                return false;
            }
        }
        return true;
    }


    private void doRegister() {
        final RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.verifyCode = edit_msg.getText().toString();
        registerRequest.phone = edit_phone.getText().toString().replace(" ", "");
        registerRequest.pwd = edit_password.getText().toString();
        registerRequest.invateCode = editInvateCode.getText().toString();
        if (!TextUtils.isEmpty(registerRequest.invateCode)) {
            MobclickAgent.onEvent(RegisterActivity.this, "Reg_invicode_action");
        }
        subscribe3 =  UserBusinessHelper.register(registerRequest).subscribe(new Action1<LoginResponse>() {
            @Override
            public void call(LoginResponse registerResponse) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                registerResponse.loginModel.certificationState = "1";
                DataCache.instance.saveCacheData("haili", "RegisterResponse", registerResponse);
                DataCache.instance.saveCacheData("haili", "MyInfoResponse", registerResponse);
                Bus.getDefault().post("FINISH_LOGIN");//弹出红包
                Bus.getDefault().post("GetUserInfo");//让我的界面刷新，避免用户直接关闭红包,切换个人后台界面
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(RegisterActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private boolean isRegisterCanClick() {
        return edit_phone.getText().toString().length() > 0 && edit_msg.getText().toString().length() > 0
                && edit_password.getText().toString().length() > 0 && edit_password2.getText().toString().length() > 0;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (isRegisterCanClick()) {
            btn_register.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btn_register.setClickable(true);
        } else {
            btn_register.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btn_register.setClickable(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
        if (subscribe2!=null) {
            subscribe2.unsubscribe();
        }
        if (subscribe3!=null) {
            subscribe3.unsubscribe();
        }
    }
}
