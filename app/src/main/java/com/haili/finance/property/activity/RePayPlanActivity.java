package com.haili.finance.property.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.RePayPlanRequest;
import com.haili.finance.business.property.RePayPlanResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.property.adapter.RepayPlanAdapter;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.widget.MyLayoutManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/3/20.
 */

public class RePayPlanActivity extends BaseActivity {

    private String id;
    private Subscription subscribe ;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_pay_plan);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("还款计划");
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)) {
            finish();
        }
        initView();
        init();
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("RePayPlanActivity")) {
            getData();
        }
    }

    private void init() {
        addLoadingFragment(R.id.repay_plan_fl, "RePayPlanActivity");
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.repay_plan_recycler);
        final MyLayoutManager myLayoutManager = new MyLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        RecyclerView.ItemDecoration decor = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
            }
        };
        recyclerView.addItemDecoration(decor);
        initCacheData();
    }

    private void initCacheData() {
        RePayPlanResponse cacheData = DataCache.instance.getCacheData("haili", "RePayPlanResponse" + id);
        if (cacheData == null) {
            return;
        }
        RepayPlanAdapter repayPlanAdapter = new RepayPlanAdapter(RePayPlanActivity.this, cacheData.dataModel.repaymentPlan);
        recyclerView.setAdapter(repayPlanAdapter);
    }

    public void getData() {
        RePayPlanRequest request = new RePayPlanRequest();
        request.pageSize = Integer.MAX_VALUE + "";
        request.pageNum = "1";
        request.manageMoneyId = id;
        subscribe =   PropertyBusinessHelper.repayPlan(request)
                .subscribe(new Action1<RePayPlanResponse>() {
                    @Override
                    public void call(RePayPlanResponse rePayPlanResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (rePayPlanResponse.dataModel != null && rePayPlanResponse.dataModel.repaymentPlan != null && rePayPlanResponse.dataModel.repaymentPlan.size() > 0) {
                            DataCache.instance.saveCacheData("haili", "RePayPlanResponse" + id, rePayPlanResponse);
                            RepayPlanAdapter repayPlanAdapter = new RepayPlanAdapter(RePayPlanActivity.this, rePayPlanResponse.dataModel.repaymentPlan);
                            recyclerView.setAdapter(repayPlanAdapter);
                        } else {
                            ViewHelper.showToast(RePayPlanActivity.this, "暂无还款计划");
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (DataCache.instance.getCacheData("haili", "RePayPlanResponse" + id) != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(RePayPlanActivity.this, requestErrorThrowable.getMessage());
                            }
                        } else {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment!=null) {
                                loadingFragment.showLoadingFailView();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe !=null) {
            subscribe .unsubscribe();
        }
    }
}
