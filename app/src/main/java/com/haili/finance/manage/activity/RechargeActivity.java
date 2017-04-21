package com.haili.finance.manage.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.RechargeResultActivity;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.DoRechargeRequest;
import com.haili.finance.business.property.DoRechargeResponse;
import com.haili.finance.business.property.GetBankCardDataRequest;
import com.haili.finance.business.property.GetBankCardDataResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.SoftKeyBoardListener;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/17.
 * 充值界面
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener,TextWatcher{

    private Button btn_recharge;
    private ImageView cardImage;
    private  Subscription subscrip,subscrip2,subscrip3;
    private TextView cardType,cardNumber,balanceText,phoneNumber,tvGetmsg,text_1,text_2,text_3;
    private EditText rechargeMoney,editMsg;
    private GetBankCardDataResponse response;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_recharge);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("充值");
        initKeyBoardView();
        initView();
        init();
        addLoadingFragment(R.id.loading_layout,"RechargeActivity");
    }

    @BusReceiver
    public void StringEvent(String event){
        switch (event){
            case "RechargeActivity":
                getData();
                break;
            case "RechargeActivity_GetCode":
                getMsgCode();
                break;
            case "RechargeActivity_DoRecharge":
                doRecharge();
                break;
        }
    }

    private void init() {
        initListener();
    }

    private void initListener() {
        tvGetmsg.setOnClickListener(this);
        btn_recharge.setOnClickListener(this);
        rechargeMoney.addTextChangedListener(this);
        editMsg.addTextChangedListener(this);
        btn_recharge.setClickable(false);
    }

    private void initView() {
        text_1 = (TextView)findViewById(R.id.text_1);
        text_2 = (TextView)findViewById(R.id.text_2);
        text_3 = (TextView)findViewById(R.id.text_3);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        cardImage = (ImageView)findViewById(R.id.card_image);
        cardType = (TextView)findViewById(R.id.card_type);
        cardNumber = (TextView)findViewById(R.id.card_number);
        balanceText = (TextView)findViewById(R.id.balance);
        phoneNumber = (TextView)findViewById(R.id.phone_number);
        tvGetmsg = (TextView)findViewById(R.id.tv_getmsg);
        rechargeMoney = (EditText) findViewById(R.id.recharge_money);
        editMsg = (EditText) findViewById(R.id.edit_msg);

    }

    private void initData(){
        if (response.data.dcFlag.equals("D")){
            cardType.setText("借记卡");
        }else if (response.data.dcFlag.equals("C")){
            cardType.setText("贷记卡");
        }else if (response.data.dcFlag.equals("E")){
            cardType.setText("准贷记卡");
        }
        String singleLimit = response.data.singleLimit/10000 + "元";
        text_1.setText(singleLimit);
        text_2.setText(response.data.dayLimit/10000 + "元");
        text_3.setText(response.data.monthLimit/10000 + "元");
        cardNumber.setText(response.data.bankCard);
        balanceText.setText(response.data.balance + "元");
        LoginResponse userInfo = DataCache.instance.getCacheData("haili","LoginResponse");
        if (userInfo != null){
            phoneNumber.setText(userInfo.loginModel.phone);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                showPopWindow();
                break;
            case R.id.tv_getmsg:
                if (checkValue()){
                    addLoadingFragment(R.id.loading_layout,"RechargeActivity_GetCode");
                }
                break;
            default:
                break;
        }
    }

    private void getData(){
        GetBankCardDataRequest request = new GetBankCardDataRequest();
        request.type = 1;
        subscrip2=  PropertyBusinessHelper.getBankCardData(request).subscribe(new Action1<GetBankCardDataResponse>() {
            @Override
            public void call(GetBankCardDataResponse getBankCardDataResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                response = getBankCardDataResponse;
                initData();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(RechargeActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void getMsgCode() {
        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = phoneNumber.getText().toString().replaceAll(" ","");
        request.type = 2;
        subscrip3=  UserBusinessHelper.getMsgCode(request).subscribe(new Action1<RegisterMsgCodeResponse>() {
            @Override
            public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                if (registerMsgCodeResponse.result){
                    ViewHelper.showToast(RechargeActivity.this,"验证码已发送");
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(RechargeActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void doRecharge(){
        DoRechargeRequest request = new DoRechargeRequest();
        request.balance = Double.valueOf(rechargeMoney.getText().toString());
        request.phoneCode = editMsg.getText().toString();
     subscrip= PropertyBusinessHelper.doRecharge(request).subscribe(new Action1<DoRechargeResponse>() {
            @Override
            public void call(DoRechargeResponse doRechargeResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                Bus.getDefault().post("GetUserInfo");
                LoginResponse loginResponse = DataCache.instance.getCacheData("haili","MyInfoResponse");
                loginResponse.loginModel.balance = loginResponse.loginModel.balance + Double.valueOf(rechargeMoney.getText().toString());
                DataCache.instance.saveCacheData("haili","MyInfoResponse",loginResponse);
                Intent intent = new Intent(RechargeActivity.this,RechargeResultActivity.class);
                intent.putExtra("result",response.result);
                intent.putExtra("balance",rechargeMoney.getText().toString());
                intent.putExtra("title",doRechargeResponse.errorMsg);
                startActivity(intent);
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    Intent intent = new Intent(RechargeActivity.this,RechargeResultActivity.class);
                    intent.putExtra("result",false);
                    intent.putExtra("title",requestErrorThrowable.getMessage());
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkValue(){
        if (phoneNumber.getText().toString().length() != 11){
            ViewHelper.showToast(this,"请输入正确的手机号码");
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        if (rechargeMoney.getText().toString().length()>0 && editMsg.getText().toString().length() > 0){
            btn_recharge.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            btn_recharge.setClickable(true);
        }else {
            btn_recharge.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            btn_recharge.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }.start();
        //
    }

    private void showPopWindow(){
        View view = LayoutInflater.from(RechargeActivity.this).inflate(
                R.layout.pay_popuwindow, null);
        final EditText inputComment = (EditText) view
                .findViewById(R.id.edit_comment);
        final TextView earnings = (TextView) view.findViewById(R.id.earnings);
        final TextView titleView = (TextView)view.findViewById(R.id.title_view);
        titleView.setText("充值金额：");
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setFocusable(true);
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置弹出窗体需要软键盘
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.update();
        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        earnings.setText(getIntent().getStringExtra("money"));
        TextView tv_cancle= (TextView) view.findViewById(R.id.tv_cancle);
        TextView tv_comit= (TextView)view.findViewById(R.id.tv_comit);
        tv_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputComment.getText().toString().length() != 0){
                    if (popupWindow != null){
                        popupWindow.dismiss();
                    }
                    addLoadingFragment(R.id.loading_layout,"RechargeActivity_DoRecharge");
                }else {
                    ViewHelper.showToast(RechargeActivity.this,"请输入支付密码");
                }
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
    private void initKeyBoardView() {
        //注册软键盘的监听
        SoftKeyBoardListener.setListener(RechargeActivity.this,
                new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
                        Log.i("text","键盘显示 高度" + height);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        Log.i("text","键盘隐藏 高度" + height);

                        if(popupWindow!=null){
                            popupWindow.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }  if (subscrip2!=null) {
            subscrip2.unsubscribe();
        }  if (subscrip3!=null) {
            subscrip3.unsubscribe();
        }
    }
}
