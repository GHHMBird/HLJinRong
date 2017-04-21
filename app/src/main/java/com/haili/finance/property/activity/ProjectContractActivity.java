package com.haili.finance.property.activity;
/*
 * Created by hhm on 2017/3/29.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;

public class ProjectContractActivity extends BaseActivity {

    private String url;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contract);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("项目合同");
        initView();
        url = getIntent().getStringExtra("url");
            init();
    }

    private void init() {
        Glide.with(this)
                .load(url)
                .error(R.mipmap.loading_fail)
                .placeholder(R.mipmap.loading_fail)
                .into(imageView);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.iv_project_contract);
    }
}
