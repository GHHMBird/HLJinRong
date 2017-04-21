package com.haili.finance.user.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.UrlSettingActivity;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginRequest;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.UserGetVerificationCodeRequest;
import com.haili.finance.business.user.UserGetVerificationCodeResponse;
import com.haili.finance.business.user.VerifyCodeLoginRequest;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.user.viewModel.LoginViewModel;
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
 * Created by Monkey on 2017/1/11.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.ll_login)
    LinearLayout editTextLayout;
    @Bind(R.id.place_holder_view)
    View placeHolderView;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.edit_phone)
    EditText editPhone;
    @Bind(R.id.edit_password)
    EditText editPassword;
    @Bind(R.id.tv_tab_password)
    TextView textPassword;
    @Bind(R.id.tv_tab_msg)
    TextView textMsg;
    @Bind(R.id.ll_edit_msg)
    LinearLayout verificationCodeLayout;
    @Bind(R.id.ll_edit_password)
    LinearLayout loginPassword;
    @Bind(R.id.tv_getmsg)
    TextView getCodeText;
    @Bind(R.id.rl_register)
    TextView registerView;
    @Bind(R.id.rl_back)
    RelativeLayout backLayout;
    @Bind(R.id.tv_forget_password)
    TextView forgetPasswordText;
    @Bind(R.id.edit_msgcode)
    EditText editMsgCode;

    private Boolean isCheck_tab_login = true; //true:密码登录   false：验证码登录
    private Subscription subscrip, subscrip2, subscrip3, subscrip4;
    private EditText codeEditText;
    private TextView imageView;
    private TimeCount timeCount;
    private LoginViewModel viewModel;
    private long startTime = 0;
    private long endTime = 0;
    private int count = 0;
    private long total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initListener();
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
        if (subscrip4 != null) {
            subscrip4.unsubscribe();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
        ButterKnife.unbind(this);
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "GETMSGCODDE":
                getMsgCode();
                break;
            case "FINISH_LOGIN":
                LoginActivity.this.finish();
                break;
            case "DO_PAS_LOGIN":
                pasLogin();
                break;
            case "DO_MSG_LOGIN":
                msgLogin();
                break;
        }
    }

    @OnClick(R.id.iv_logo)
    public void showUrl() {
        showUrlView();
    }

    @OnClick(R.id.btn_login)
    public void doLogin() {
        if (checkValue(0)) {
            if (isCheck_tab_login) {
                addLoadingFragment(R.id.loading_fragment, "DO_PAS_LOGIN");
            } else {
                addLoadingFragment(R.id.loading_fragment, "DO_MSG_LOGIN");
            }
        }
    }

    @OnClick(R.id.tv_tab_password)
    public void changeLoginTypeToPassword() {
        if (!isCheck_tab_login) {
            forgetPasswordText.setText("忘记密码？");
            forgetPasswordText.setClickable(true);
            editTextLayout.setBackground(getResources().getDrawable(R.mipmap.login_edit_left));
            textPassword.setTextColor(getResources().getColor(R.color.bg_mainColor));
            textMsg.setTextColor(getResources().getColor(R.color.text_grey));
            loginPassword.setVisibility(View.VISIBLE);
            if (timeCount != null) {
                timeCount.cancel();
            }
            verificationCodeLayout.setVisibility(View.GONE);
            isCheck_tab_login = true;
            changeBtnStatus();
        }
    }

    @OnClick(R.id.tv_tab_msg)
    public void changeLoginTypeToCode() {
        if (isCheck_tab_login) {
            forgetPasswordText.setText("提示：未注册用户也可通过验证码直接登录!");
            forgetPasswordText.setClickable(false);
            editTextLayout.setBackground(getResources().getDrawable(R.mipmap.login_edit_right));
            textPassword.setTextColor(getResources().getColor(R.color.text_grey));
            textMsg.setTextColor(getResources().getColor(R.color.bg_mainColor));
            loginPassword.setVisibility(View.GONE);
            verificationCodeLayout.setVisibility(View.VISIBLE);
            isCheck_tab_login = false;
            changeBtnStatus();
        }
    }

    @OnClick(R.id.tv_getmsg)
    public void getCode() {
        if (checkValue(1))
            showDialog();
    }

    @OnClick(R.id.rl_register)
    public void doRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.rl_back)
    public void doFinish() {
        finish();
    }

    @OnClick(R.id.tv_forget_password)
    public void doForgetPassword() {
        startActivity(new Intent(LoginActivity.this, FindLoginPassword.class));
    }


    private void initListener() {
        btnLogin.setClickable(false);
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                if (viewModel.changeString(str.toString(), arg1, arg2)) {
                    editPhone.setText(viewModel.phoneText);
                    editPhone.setSelection(viewModel.index);
                }
                changeBtnStatus();
            }

            @Override
            public void beforeTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {// TODO Auto-generated method stub
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeBtnStatus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editMsgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeBtnStatus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {
        viewModel = new LoginViewModel(this);
        if (getIntent().getIntExtra("type", 1) == 9) {
            backLayout.setVisibility(View.GONE);
            registerView.setVisibility(View.GONE);
        }
        if (viewModel.placeViewControl()) {
            placeHolderView.setVisibility(View.VISIBLE);
        } else {
            placeHolderView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            int type = getIntent().getIntExtra("type", 1);
            if (type != 9) {
                return super.onKeyDown(keyCode, event);
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showUrlView() {
        count++;
        if (count == 1) {
            startTime = System.currentTimeMillis();
        } else if (count == 5) {
            endTime = System.currentTimeMillis();
            total = endTime - startTime;
            if (total < 5000) {
                Intent in = new Intent(this, UrlSettingActivity.class);
                startActivity(in);
            }
            count = 0;
            startTime = 0;
            endTime = 0;
            total = 0;
        }
    }

    private void showDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.customView(addViewContext(), false);
        builder.title("验证通过后发送短信验证码");
        builder.autoDismiss(false);
        builder.positiveText(R.string.ok);
        builder.negativeText(R.string.cancel);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                hintkeybord(editPhone);
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                addLoadingFragment(R.id.loading_fragment, "GETMSGCODDE");
                dialog.dismiss();
                hintkeybord(editPhone);
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
        codeEditText.setHint("请输入图片中的验证码");
        return view;
    }

    private void getImageCode() {
        UserGetVerificationCodeRequest request = new UserGetVerificationCodeRequest();
        request.phone = editPhone.getText().toString().replaceAll(" ", "");
        request.type = 1;
        subscrip = UserBusinessHelper.getImageCode(request).subscribe(new Action1<UserGetVerificationCodeResponse>() {
            @Override
            public void call(UserGetVerificationCodeResponse userGetVerificationCodeResponse) {
                if (!userGetVerificationCodeResponse.dataModel.imgData.equals("")) {
                    imageView.setText("");
                    imageView.setBackground(new BitmapDrawable(getResources(), viewModel.changeBitmap(userGetVerificationCodeResponse.dataModel.imgData)));
                    imageView.setClickable(true);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                imageView.setClickable(true);
                imageView.setBackground(null);
                imageView.setText("点击重新获取");
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(LoginActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void getMsgCode() {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.imgCode = codeEditText.getText().toString();
        request.phone = editPhone.getText().toString().replace(" ", "");
        request.type = 1;
        subscrip2 = UserBusinessHelper.getMsgCode(request).subscribe(new Action1<RegisterMsgCodeResponse>() {
            @Override
            public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                timeCount = new TimeCount(60000, 1000, getCodeText, LoginActivity.this, 2);
                timeCount.start();
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
                    ViewHelper.showToast(LoginActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void pasLogin() {
        LoginRequest request = new LoginRequest();
        request.phone = editPhone.getText().toString().replaceAll(" ", "");
        request.pwd = editPassword.getText().toString();
        subscrip4 = UserBusinessHelper.pasLogin(request).subscribe(new Action1<LoginResponse>() {
            @Override
            public void call(LoginResponse loginResponse) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null) {
                    removeLoadingFragment();
                }
                Bus.getDefault().post("LoginSuccess");
                Bus.getDefault().post("GetUserInfo");
                MobclickAgent.onEvent(LoginActivity.this, "Login_pwd_action");
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null) {
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(LoginActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void msgLogin() {
        VerifyCodeLoginRequest request = new VerifyCodeLoginRequest();
        request.phone = editPhone.getText().toString().replaceAll(" ", "");
        request.phoneCode = editMsgCode.getText().toString();
        subscrip3 = UserBusinessHelper.verifyCodeLogin(request).subscribe(new Action1<LoginResponse>() {
            @Override
            public void call(LoginResponse loginResponse) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null) {
                    removeLoadingFragment();
                }
                Bus.getDefault().post("LoginSuccess");
                Bus.getDefault().post("GetUserInfo");
                MobclickAgent.onEvent(LoginActivity.this, "Login_vercode_action");
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null) {
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(LoginActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });

    }

    private boolean checkValue(int type) {
        if (editPhone.getText().toString().length() < 13) {
            ViewHelper.showToast(this, "请输入正确的手机号码");
            return false;
        }
        if (type != 1) {
            if (isCheck_tab_login) {
                if (editPassword.getText().toString().length() < 6) {
                    ViewHelper.showToast(this, "请输入6到16位密码");
                    return false;
                }
            } else {
                if (editMsgCode.getText().toString().length() < 6) {
                    ViewHelper.showToast(this, "请输入6位短信验证码");
                    return false;
                }
            }
        }
        return true;
    }

    private void changeBtnStatus() {
        if (isCheck_tab_login) {
            if (editPassword.getText().toString().length() > 0 && editPhone.getText().toString().length() > 0) {
                btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_home_pro));
                btnLogin.setClickable(true);
            } else {
                btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_home_pro2));
                btnLogin.setClickable(false);
            }
        } else {
            if (editMsgCode.getText().toString().length() > 0 && editPhone.getText().toString().length() > 0) {
                btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_home_pro));
                btnLogin.setClickable(true);
            } else {
                btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_home_pro2));
                btnLogin.setClickable(false);
            }
        }
    }

}
