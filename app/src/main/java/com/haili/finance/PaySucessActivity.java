package com.haili.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.haili.finance.base.BaseActivity;

/*
 * Created by Monkey on 2017/1/17.
 * 支付成功界面
 */

public class PaySucessActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_look_licai,btn_look_assets ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_pay_sucess);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("支付成功");
        initView();
        init();
    }
    private void init() {
            initListener();
    }

    private void initListener() {
        btn_look_licai.setOnClickListener(this);
        btn_look_assets.setOnClickListener(this);
    }
    private void initView() {
        btn_look_licai= (Button) findViewById(R.id.btn_look_licai);
        btn_look_assets= (Button) findViewById(R.id.btn_look_assets);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_look_licai:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btn_look_assets:
                startActivity(new Intent(this,MainActivity.class));
                break;
            default:
            break;
        }
    }
}
