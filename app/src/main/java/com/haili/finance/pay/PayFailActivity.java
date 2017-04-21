package com.haili.finance.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;

/*
 * Created by lfu on 2017/3/27.
 */

public class PayFailActivity extends BaseActivity {

    private TextView errorText;
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fail);
        setUpToolbar();
        setBarTitle("投资失败");
        getSupportActionBar().setTitle("");
        errorText = (TextView)findViewById(R.id.error_text);
        btnBack = (Button)findViewById(R.id.btn_investment);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        errorText.setText(intent.getStringExtra("error"));
    }
}
