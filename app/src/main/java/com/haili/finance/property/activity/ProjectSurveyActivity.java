package com.haili.finance.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.InvestmentDetailRequest;
import com.haili.finance.business.property.InvestmentDetailResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.activity.ProductDetailActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 *  项目概览
 * Created by fuliang on 2017/3/13.
 */

public class ProjectSurveyActivity extends BaseActivity implements View.OnClickListener {

    private String id, imgUrl;
    private Subscription subscribe ;
    private TextView tvTitle, tvState, tvMoney, tvEarn, tvYear, tvLimit, tvStyle, tvStartDay, tvEndDay, tvDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_survey);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("项目概览");
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)) {
            finish();
        }
        initView();
        init();
    }

    private void init() {
        addLoadingFragment(R.id.project_survey_fl, "ProjectSurveyActivity");
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("ProjectSurveyActivity")) {
            getData();
        }
    }

    private void initView() {
        LinearLayout llDetail = (LinearLayout) findViewById(R.id.user_info_login);
        LinearLayout llContract = (LinearLayout) findViewById(R.id.contract_view);
        LinearLayout llPlan = (LinearLayout) findViewById(R.id.plan_view);
        LinearLayout llRecord = (LinearLayout) findViewById(R.id.record_view);
        tvTitle = (TextView) findViewById(R.id.my_fragment_userid);//名字
        tvState = (TextView) findViewById(R.id.my_fragment_state);//还款中
        tvMoney = (TextView) findViewById(R.id.project_survey_money);//投资金额
        tvEarn = (TextView) findViewById(R.id.project_survey_earn);//预期收益
        tvYear = (TextView) findViewById(R.id.project_survey_year);//年化收益
        tvLimit = (TextView) findViewById(R.id.project_suevey_limit_day);//项目期限
        tvStyle = (TextView) findViewById(R.id.project_suevey_style);//还款方式
        tvStartDay = (TextView) findViewById(R.id.project_survey_start_day);//起息日期
        tvEndDay = (TextView) findViewById(R.id.project_survey_end_day);//结束日期
        tvDay = (TextView) findViewById(R.id.project_survey_day);//预计还款日期

        llPlan.setOnClickListener(this);
        llRecord.setOnClickListener(this);
        llDetail.setOnClickListener(this);
        llContract.setOnClickListener(this);

        initCacheData();
    }

    private void initCacheData() {
        InvestmentDetailResponse cacheData = DataCache.instance.getCacheData("haili", "InvestmentDetailResponse" + id);
        if (cacheData != null) {
            analyseData(cacheData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_login://项目详情
                Intent detailIntent = new Intent(ProjectSurveyActivity.this, ProductDetailActivity.class);
                detailIntent.putExtra("productId", id);
                startActivity(detailIntent);
                break;
            case R.id.contract_view://项目合同
                startActivity(new Intent(ProjectSurveyActivity.this, ProjectContractActivity.class).putExtra("url", imgUrl));
                break;
            case R.id.plan_view://还款计划
                Intent intent = new Intent(ProjectSurveyActivity.this, RePayPlanActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.record_view://交易记录
                Intent intents = new Intent(ProjectSurveyActivity.this, AllTradeActivity.class);
                intents.putExtra("manageMoneyId", id);
                startActivity(intents);
                break;
        }
    }

    public void getData() {
        InvestmentDetailRequest request = new InvestmentDetailRequest();
        request.manageMoneyId = id;
        subscribe =  PropertyBusinessHelper.investmentDetail(request)
                .subscribe(new Action1<InvestmentDetailResponse>() {
                    @Override
                    public void call(InvestmentDetailResponse investmentDetailResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (investmentDetailResponse.responseModel != null) {
                            DataCache.instance.saveCacheData("haili", "InvestmentDetailResponse" + id, investmentDetailResponse);
                            analyseData(investmentDetailResponse);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(ProjectSurveyActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void analyseData(InvestmentDetailResponse investmentDetailResponse) {
        imgUrl = investmentDetailResponse.responseModel.productPact;
        tvTitle.setText(investmentDetailResponse.responseModel.productName + "");
        tvState.setText(investmentDetailResponse.responseModel.productState + "");
        tvMoney.setText(investmentDetailResponse.responseModel.investBanalce + "");
        tvEarn.setText(investmentDetailResponse.responseModel.yieldRateBanalce + "");
        tvYear.setText(investmentDetailResponse.responseModel.yieldRate + "");
        tvLimit.setText(investmentDetailResponse.responseModel.investDeadline + "");
        tvStyle.setText(investmentDetailResponse.responseModel.repaymentStyle + "");
        tvStartDay.setText(investmentDetailResponse.responseModel.interestDate + "");
        tvEndDay.setText(investmentDetailResponse.responseModel.expiryDate + "");
        tvDay.setText(investmentDetailResponse.responseModel.repaymentTime + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }
}
