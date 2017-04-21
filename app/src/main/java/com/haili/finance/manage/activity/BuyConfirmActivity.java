package com.haili.finance.manage.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haili.finance.pay.PaySucessActivity;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.manage.GetRedPacketListModel;
import com.haili.finance.business.manage.GetRedPacketRequest;
import com.haili.finance.business.manage.GetRedPacketResponse;
import com.haili.finance.business.manage.InvestRequest;
import com.haili.finance.business.manage.InvestResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.ManageBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.fragment.CanUseRedPacketFragment;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.AddBankCardActivity;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.activity.PayPasswordActivity;
import com.haili.finance.utils.SoftKeyBoardListener;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * 投资确认界面
 */
public class BuyConfirmActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_redbackpage;
    private TextView tv_redbackpage,id_name,investment_amount,earnings,balance_text,practical_money,agreement_btn;
    private Button btn_investment;
    private boolean hasRedPacket = false;
    private Subscription subscribe ;
    private GetRedPacketResponse response;
    private CanUseRedPacketFragment fragment;
    private PopupWindow popupWindow;
    private Subscription subscribe2 ;
    private CheckBox check_box_btn;
    private LoginResponse loginResponse;
    private GetRedPacketListModel model;
    private EditText inputComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirm);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("投资确认");
        initView();
        init();
        addLoadingFragment(R.id.loading_layout,"BuyConfirmActivity");
    }

    @BusReceiver
    public void stringEvent(String event){
        if (event.equals("BuyConfirmActivity")){
            getCanUseRedPacket();
            return;
        }
        if (event.equals("DO_INVEST")){
            doInvest();
        }
    }

    private void init() {
        initKeyBoardView();
        initData();
        initListener();
    }

    private void initData() {
        id_name.setText(getIntent().getStringExtra("name"));
        investment_amount.setText(getIntent().getStringExtra("money") + "元");
        earnings.setText(getIntent().getStringExtra("earning") + "元");
        loginResponse = DataCache.instance.getCacheData("haili","MyInfoResponse");
        practical_money.setText(getIntent().getStringExtra("money") + "元");
        if (loginResponse != null){
            balance_text.setText(loginResponse.loginModel.balance + "元");
        }
    }

    private void initListener() {
        ll_redbackpage.setOnClickListener(this);
        btn_investment.setOnClickListener(this);
        agreement_btn.setOnClickListener(this);
    }

    private void initView() {
        balance_text = (TextView)findViewById(R.id.balance_text);
        earnings = (TextView)findViewById(R.id.earnings);
        investment_amount = (TextView)findViewById(R.id.investment_amount);
        id_name = (TextView)findViewById(R.id.id_name);
        ll_redbackpage = (LinearLayout) findViewById(R.id.ll_redbackpage);
        tv_redbackpage = (TextView) findViewById(R.id.tv_redbackpage);
        btn_investment = (Button) findViewById(R.id.btn_investment);
        practical_money = (TextView)findViewById(R.id.practical_money);
        agreement_btn = (TextView)findViewById(R.id.agreement_btn);
        check_box_btn = (CheckBox)findViewById(R.id.check_box_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_redbackpage:
                addRedPacketFragment();
                break;
            case R.id.btn_investment:
                if (check_box_btn.isChecked()){
                    if (checkState()){
                        showPopWindow();
                    }
                }else {
                    ViewHelper.showToast(BuyConfirmActivity.this,"请同意《海狸金融用户投资协议》");
                }
                break;
            case R.id.agreement_btn:
                Intent intent = new Intent(BuyConfirmActivity.this, WebActivity.class);
                intent.putExtra("title","《海狸金融用户投资协议》");
                intent.putExtra("params","http://www.qq.com");
                startActivity(intent);
            default:
                break;
        }
    }

    private void initRedPacketData(GetRedPacketResponse getRedPacketResponse){
        double min = 0;
        int size = getRedPacketResponse.data.voList.size();
        if (size>0){
            for (int i = 0; i < size; i++ ){
                if (getRedPacketResponse.data.voList.get(i).investBanalce < Double.valueOf(getIntent().getStringExtra("money"))){
                    tv_redbackpage.setText("您有可用的红包");
                    hasRedPacket = true;
                    break;
                }else {
                    if (getRedPacketResponse.data.voList.get(i).investBanalce <min){
                        min = getRedPacketResponse.data.voList.get(i).investBanalce - Double.valueOf(getIntent().getStringExtra("money"));
                    }
                }
            }
            if (!hasRedPacket){
                tv_redbackpage.setText("还差" + min + "元有可用红包");

            }
            tv_redbackpage.setTextColor(getResources().getColor(R.color.text_main2));
        }else {
            ll_redbackpage.setVisibility(View.GONE);
        }

    }

    private void getCanUseRedPacket(){
        GetRedPacketRequest request = new GetRedPacketRequest();
        request.productId = getIntent().getStringExtra("id");
        subscribe2 = ManageBusinessHelper.getCanUseRedPacket(request).subscribe(new Action1<GetRedPacketResponse>() {
            @Override
            public void call(GetRedPacketResponse getRedPacketResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                response = getRedPacketResponse;
                initRedPacketData(getRedPacketResponse);
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
                    ViewHelper.showToast(BuyConfirmActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void addRedPacketFragment(){
        if (fragment == null) {
            fragment = new CanUseRedPacketFragment();
        }
        fragment.setOnClickDoneListener(new CanUseRedPacketFragment.OnClickDoneListener() {
            @Override
            public void onEditDone(GetRedPacketListModel model) {
                hideRedPacketFragment();
                BuyConfirmActivity.this.model = model;
                if (model.isClicked){
                    tv_redbackpage.setText("-" + model.banalce + "元");
                    tv_redbackpage.setTextColor(getResources().getColor(R.color.text_red));
                    practical_money.setText(Double.valueOf(getIntent().getStringExtra("money"))- model.banalce+ "元");
                }else {
                    practical_money.setText(getIntent().getStringExtra("money") + "元");
                    initRedPacketData(response);
                }

            }
        });
        fragment.setData(response.data.voList);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                .add(R.id.red_packet_layout, fragment, "fragment,fragment.TAG")
                .commit();
    }

    private void hideRedPacketFragment(){
        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(0, R.animator.slide_out_right)
                    .remove(fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null){
            hideRedPacketFragment();
            fragment = null;
            return;
        }else {
            finish();
        }
        super.onBackPressed();
    }

    private void showPopWindow(){
            View view = LayoutInflater.from(BuyConfirmActivity.this).inflate(
                    R.layout.pay_popuwindow, null);
            inputComment = (EditText) view
                    .findViewById(R.id.edit_comment);
            final TextView earnings = (TextView) view.findViewById(R.id.earnings);
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
                        addLoadingFragment(R.id.loading_layout,"DO_INVEST");
                    }else {
                        ViewHelper.showToast(BuyConfirmActivity.this,"请输入支付密码");
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

    private void initKeyBoardView() {
        //注册软键盘的监听
        SoftKeyBoardListener.setListener(BuyConfirmActivity.this,
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

    private void doInvest(){
        double pay = 0;
        String redId = "";
        if (model != null){
            pay = Double.valueOf(getIntent().getStringExtra("money")) - model.banalce;
            redId = model.id;
        }else {
            pay = Double.valueOf(getIntent().getStringExtra("money"));
        }
        InvestRequest request = new InvestRequest();
        request.productId = getIntent().getStringExtra("id");
        request.investBalance = getIntent().getStringExtra("money");
        request.investInterest = earnings.getText().toString();
        request.redPackId = redId;
        request.payAmount = pay +  "";
        request.paymentPassword = inputComment.getText().toString();
        subscribe =  ManageBusinessHelper.doInvest(request).subscribe(new Action1<InvestResponse>() {
            @Override
            public void call(InvestResponse investResponse) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null){
                    removeLoadingFragment();
                }
                Intent intent = new Intent(BuyConfirmActivity.this,PaySucessActivity.class);
                intent.putExtra("name",id_name.getText().toString());
                intent.putExtra("money",investment_amount.getText().toString());
                intent.putExtra("earnings",earnings.getText().toString());
                startActivity(intent);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment fragment = findLoadingFragment();
                if (fragment != null){
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(BuyConfirmActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private boolean checkState() {
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (loginResponse != null) {
            if (loginResponse.loginModel.certificationState.equals("1")) {
                startActivity(new Intent(BuyConfirmActivity.this, IDInformationActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("2") || loginResponse.loginModel.certificationState.equals("5") ) {
                startActivity(new Intent(BuyConfirmActivity.this, AddBankCardActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("3")) {
                Intent intent = new Intent(BuyConfirmActivity.this, PayPasswordActivity.class);
                intent.putExtra("title","设置支付密码");
                startActivity(intent);
                return false;
            }
        } else {
            startActivity(new Intent(BuyConfirmActivity.this, LoginActivity.class));
            return false;
        }
        return true;
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
