package com.haili.finance.user.activity;

import android.os.Bundle;
import android.view.View;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;

import static com.networkbench.agent.impl.instrumentation.NBSOkHttp3Instrumentation.init;


/**
 * Created by Monkey on 2017/1/20.
 * 完成认证
 */

public class AuthenticationFinishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_finish);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("完成认证");
        init();
    }

    public void finished(View view) {
        finish();
    }
}
