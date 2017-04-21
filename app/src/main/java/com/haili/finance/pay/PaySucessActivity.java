package com.haili.finance.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haili.finance.MainActivity;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;

/*
 * Created by Monkey on 2017/1/17.
 * 支付成功界面
 */

public class PaySucessActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_look_licai,btn_look_assets ;
    private TextView name,investment_amount,earnings;
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
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        investment_amount.setText(intent.getStringExtra("money"));
        earnings.setText(intent.getStringExtra("earnings"));
        initListener();
    }

    private void initListener() {
        btn_look_licai.setOnClickListener(this);
        btn_look_assets.setOnClickListener(this);
    }
    private void initView() {
        btn_look_licai= (Button) findViewById(R.id.btn_look_licai);
        btn_look_assets= (Button) findViewById(R.id.btn_look_assets);
        name = (TextView)findViewById(R.id.name);
        investment_amount = (TextView)findViewById(R.id.investment_amount);
        earnings = (TextView)findViewById(R.id.earnings);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(new Intent(this,MainActivity.class));
        switch(v.getId()){
            case R.id.btn_look_licai:
                intent.putExtra("position",1);
                startActivity(intent);
                break;
            case R.id.btn_look_assets:
                intent.putExtra("position",2);
                startActivity(intent);
                break;
            default:
            break;
        }
    }
}
