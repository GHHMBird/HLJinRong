package com.haili.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.base.BaseActivity;

/*
 * Created by Monkey on 2017/1/17.
 * 充值结果
 */

public class RechargeResultActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_recharge_result;
    private TextView balance,titleView,textView;
    private ImageView result_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_recharge_result);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("");
        showBackBtn(false);
        initView();
        init();
    }

    private void init() {
            initListener();
    }

    private void initListener() {
        setBarTitle(getIntent().getStringExtra("title"));
        btn_recharge_result.setOnClickListener(this);
    }

    private void initView() {
        result_image = (ImageView)findViewById(R.id.result_image);
        titleView = (TextView)findViewById(R.id.title_view);
        textView = (TextView)findViewById(R.id.text_view);
        balance = (TextView) findViewById(R.id.balance);
        btn_recharge_result= (Button) findViewById(R.id.btn_recharge_result);
        if (!getIntent().getBooleanExtra("result",false)){
            textView.setVisibility(View.GONE);
            balance.setVisibility(View.GONE);
            titleView.setText(getIntent().getStringExtra("title"));
            btn_recharge_result.setText("返回充值界面");
            result_image.setImageDrawable(getResources().getDrawable(R.mipmap.pay_fail));
        }else {
            balance.setText(getIntent().getStringExtra("balance"));
            btn_recharge_result.setText("完成");
        }
    }

    @Override
    public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_recharge_result:
                    finish();
                    break;
                default:
                    break;
            }
    }
}
