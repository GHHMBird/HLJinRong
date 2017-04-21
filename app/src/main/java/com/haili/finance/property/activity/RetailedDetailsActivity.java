package com.haili.finance.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.base.BaseApplication;

/*
 * Created by Monkey on 2017/1/18.
 * 明细列表详情页
 */

public class RetailedDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_date, tv_money, tv_type, tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_retailed_detail);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("详情");
        initView();
        init();
    }

    private void initView() {
//        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_type = (TextView) findViewById(R.id.tv_type);
    }

    private void init() {
        initListener();
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        if (i.getStringExtra("date") != null) {
            tv_date.setText(i.getStringExtra("date"));
            tv_content.setText(i.getStringExtra("content"));
            tv_money.setText(i.getStringExtra("money"));
            tv_type.setText(i.getStringExtra("type"));
        }

    }

    private void initListener() {
//        rl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_back:
//                finish();
//                break;
            default:
                break;
        }
    }
}
