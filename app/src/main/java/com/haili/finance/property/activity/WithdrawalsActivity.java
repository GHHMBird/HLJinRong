package com.haili.finance.property.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.GetBankCardDataRequest;
import com.haili.finance.business.property.GetBankCardDataResponse;
import com.haili.finance.business.property.WithdrawalsRequest;
import com.haili.finance.business.property.WithdrawalsResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.pay.WithdrawalsSuccessActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.utils.SoftKeyBoardListener;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/20.
 * 提现界面
 */

public class WithdrawalsActivity   extends BaseActivity implements View.OnClickListener,TextWatcher{

    private GetBankCardDataResponse response;
    private Button btn_recharge;
    private ImageView cardImage;
    private Subscription subscribe2;
    private TextView cardType,cardNumber,balanceText,phoneNumber,tvGetmsg,text_1,text_2,text_3;
    private EditText rechargeMoney;
    private Subscription subscribe ;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("提现");
        initView();
        initKeyBoardView();
        addLoadingFragment(R.id.loading_layout,"WithdrawalsActivity");
    }

    @BusReceiver
    public void StringEvent(String event){
        switch (event){
            case "WithdrawalsActivity":
                getData();
                break;
            case "doWithdrawals":
                doWithdrawals();
                break;
            default:
                break;
        }
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
        rechargeMoney.addTextChangedListener(this);
        btn_recharge.setOnClickListener(this);
        btn_recharge.setClickable(false);

    }

    private void initData(){
        if (response.data.dcFlag.equals("D")){
            cardType.setText("借记卡");
        }else if (response.data.dcFlag.equals("C")){
            cardType.setText("贷记卡");
        }else if (response.data.dcFlag.equals("E")){
            cardType.setText("准贷记卡");
        }
        String singleLimit = response.data.singleLimit/10000 + "";
        text_1.setText(singleLimit);
        text_2.setText(response.data.dayLimit/10000 + "");
        text_3.setText(response.data.monthLimit/10000 + "");
        cardNumber.setText(response.data.bankCard);
        balanceText.setText(response.data.balance + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                if (Double.valueOf(rechargeMoney.getText().toString()) > response.data.balance){
                    ViewHelper.showToast(WithdrawalsActivity.this,"超过账户余额！");
                }else {
                    showPopWindow();
                }
                break;
            default:
                break;
        }
    }

    private void getData(){
        GetBankCardDataRequest request = new GetBankCardDataRequest();
        request.type = 1;
        subscribe2 = PropertyBusinessHelper.getBankCardData(request).subscribe(new Action1<GetBankCardDataResponse>() {
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
                    ViewHelper.showToast(WithdrawalsActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (rechargeMoney.getText().toString().length() >0){
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
        View view = LayoutInflater.from(WithdrawalsActivity.this).inflate(
                R.layout.pay_popuwindow, null);
        final EditText inputComment = (EditText) view
                .findViewById(R.id.edit_comment);
        final TextView earnings = (TextView) view.findViewById(R.id.earnings);
        final TextView titleView = (TextView)view.findViewById(R.id.title_view);
        titleView.setText("提现金额：");
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
                    addLoadingFragment(R.id.loading_layout,"doWithdrawals");
                }else {
                    ViewHelper.showToast(WithdrawalsActivity.this,"请输入支付密码");
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
        SoftKeyBoardListener.setListener(WithdrawalsActivity.this,
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

    private void doWithdrawals(){
        WithdrawalsRequest request = new WithdrawalsRequest();
        request.balance = Double.valueOf(rechargeMoney.getText().toString());
        subscribe =  PropertyBusinessHelper.doWithdrawals(request).subscribe(new Action1<WithdrawalsResponse>() {
            @Override
            public void call(WithdrawalsResponse withdrawalsResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                Intent intent = new Intent(WithdrawalsActivity.this, WithdrawalsSuccessActivity.class);
                startActivity(intent);
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
                    ViewHelper.showToast(WithdrawalsActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
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
    }
}
