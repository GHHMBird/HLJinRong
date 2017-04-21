package com.haili.finance.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;

/**
 * Created by lfu on 2017/3/29.
 */

public class WithdrawalsSuccessActivity extends BaseActivity {

    private Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals_success);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("提现");
        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
