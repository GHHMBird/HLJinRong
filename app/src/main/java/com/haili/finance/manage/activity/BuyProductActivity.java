package com.haili.finance.manage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.manage.ManageDetailDataModel;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.stroage.DataCache;

import java.text.DecimalFormat;

/*
 * 产品购买界面
 */
public class BuyProductActivity extends BaseActivity implements View.OnClickListener,TextWatcher {
    private Button btn_next;
    private TextView tv_recharge,earnings,tv_name,balance_text;
    private EditText edit_comment;
    private String id,name;
    private LoginResponse loginResponse;
    private ManageDetailDataModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("购买");
        initView();
        init();
    }

    private void initView() {
        balance_text = (TextView)findViewById(R.id.balance_text);
        tv_name = (TextView)findViewById(R.id.tv_name);
        earnings = (TextView)findViewById(R.id.earnings);
        btn_next= (Button) findViewById(R.id.btn_next);
        tv_recharge= (TextView) findViewById(R.id.tv_recharge);
        edit_comment = (EditText)findViewById(R.id.edit_comment);
    }

    private void init() {
            initListener();
        model = (ManageDetailDataModel)getIntent().getSerializableExtra("model");
        id = model.productInfoVo.productId;
        name = model.productInfoVo.productName;
        tv_name.setText(name);
        loginResponse = DataCache.instance.getCacheData("haili","MyInfoResponse");
        balance_text.setText(loginResponse.loginModel.balance + "元");
    }

    private void initListener() {
        btn_next.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        edit_comment.addTextChangedListener(this);
        btn_next.setClickable(false);
    }

    private boolean checkValue(){
        if (Double.valueOf(edit_comment.getText().toString())== 0){
            ViewHelper.showToast(BuyProductActivity.this,"请输入投资金额");
            return false;
        }
        if (Double.valueOf(edit_comment.getText().toString()) < model.productInfoVo.startInvestorBanalce){
            ViewHelper.showToast(BuyProductActivity.this,"起投金额为" + model.productInfoVo.startInvestorBanalce + "元");
            return false;
        }
        if (model.productInfoVo.remainingAmount < Double.valueOf(edit_comment.getText().toString())){
            ViewHelper.showToast(BuyProductActivity.this,"最大投资金额为" + model.productInfoVo.remainingAmount + "元");
            return false;
        }
        if (Double.valueOf(edit_comment.getText().toString()) > loginResponse.loginModel.balance){
            double difference = Double.valueOf(edit_comment.getText().toString())-loginResponse.loginModel.balance;
            ViewHelper.buildNoTitleTextDialog(BuyProductActivity.this, "还差"+ difference + "元，去充值？", new ViewHelper.OnPositiveBtnClickedListener() {
                @Override
                public void onPositiveBtnClicked(MaterialDialog dialog) {
                    startActivity(new Intent(BuyProductActivity.this,RechargeActivity.class));
                }
            }).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_next:
                if (checkValue()){
                    Intent intent = new Intent(BuyProductActivity.this,BuyConfirmActivity.class);
                    intent.putExtra("type",model.productInfoVo.type);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("money",edit_comment.getText().toString());
                    intent.putExtra("earning",earnings.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(BuyProductActivity.this,RechargeActivity.class));
            default:
            break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (edit_comment.getText().toString().length() == 0){
            earnings.setText("0.00");
        }else {
            int a = Integer.parseInt(edit_comment.getText().toString());
            int b = Integer.parseInt(model.productInfoVo.limitTime);
            double yieldRate = model.productInfoVo.yieldRate;
            double c = a*yieldRate/365*b;
            DecimalFormat df = new DecimalFormat("0.00");
            earnings.setText(df.format(c) + "");
        }
        if (edit_comment.getText().toString().length() > 0){
            btn_next.setClickable(true);
            btn_next.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
        }else {
            btn_next.setClickable(false);
            btn_next.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
