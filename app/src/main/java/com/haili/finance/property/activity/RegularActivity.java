package com.haili.finance.property.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.adapter.InvestmentRecordAdapter;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.RegularRequest;
import com.haili.finance.business.property.RegularResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.modle.InvestmentHistoryModel;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.ListViewUtil;
import com.haili.finance.widget.MyListView;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/19.
 * 定期页面
 */

public class RegularActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back;
    private MyListView listview;
    private List<InvestmentHistoryModel> data;
    private ScrollView scrollView;
    private Subscription subscribe;
    private ImageView ivCalendar;
    private View placeHolderView;
    private TextView allDeal, yesterdayEarningsView, balanceText, allEarnings, endDateView;
    private RegularResponse regularResponse;
    private ImageView ivNoProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_regular);
        initView();
        init();
        addLoadingFragment(R.id.loading_layout, "RegularActivity");
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("RegularActivity")) {
            getRegularActivity();
        }
    }

    private void initView() {
        allDeal = (TextView) findViewById(R.id.all_deal);
        ivNoProject = (ImageView) findViewById(R.id.calender_layout);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        listview = (MyListView) findViewById(R.id.listview);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ivCalendar = (ImageView) findViewById(R.id.activity_regular_calendar);
        placeHolderView = findViewById(R.id.place_holder_view);
        yesterdayEarningsView = (TextView) findViewById(R.id.yesterday_earnings);
        balanceText = (TextView) findViewById(R.id.balance_text);
        allEarnings = (TextView) findViewById(R.id.all_earnings);
        endDateView = (TextView) findViewById(R.id.end_date_view);
        placeViewControl();
        initCacheData();
    }

    private void initCacheData() {
        RegularResponse cacheData = DataCache.instance.getCacheData("haili", "RegularResponse");
        if (cacheData != null) {
            if (cacheData.data != null){
                regularResponse = cacheData;
                setViewData();
                initListView();
            }
        }
    }

    private void setViewData() {
        yesterdayEarningsView.setText(regularResponse.data.yesterdayBanalce + "");
        balanceText.setText(regularResponse.data.totalBanalce + "");
        allEarnings.setText(regularResponse.data.totalgetBalance + "");
        endDateView.setText(regularResponse.data.endDate);
    }

    private void init() {
        initListener();
    }

    private void placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            this.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeHolderView.setVisibility(View.VISIBLE);
        } else {
            placeHolderView.setVisibility(View.GONE);
        }
    }

    private void initListView() {
        initData();
        if (data.size() > 0) {
            InvestmentRecordAdapter investmentRecordAdapter = new InvestmentRecordAdapter(this, data, R.layout.item_inv_record);
            listview.setAdapter(investmentRecordAdapter);
            listview.setVisibility(View.VISIBLE);
            ivNoProject.setVisibility(View.GONE);
        } else {
            ivNoProject.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
        new ListViewUtil().setListViewHeightBasedOnChildren(listview);
//        scrollView.smoothScrollTo(0, Integer.MAX_VALUE);
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < regularResponse.data.manageMoneyList.size(); i++) {
            if (regularResponse.data.manageMoneyList.get(i)!= null){
                InvestmentHistoryModel model = new InvestmentHistoryModel();
                model.setName(regularResponse.data.manageMoneyList.get(i).productName);
                model.setState(regularResponse.data.manageMoneyList.get(i).productState + "");
                model.setDate(regularResponse.data.manageMoneyList.get(i).repaymentTime);
                model.setId(regularResponse.data.manageMoneyList.get(i).productId);
                model.setInv_money(regularResponse.data.manageMoneyList.get(i).investBalance + "");
                model.setProfit_return(regularResponse.data.manageMoneyList.get(i).yieldRateBalance + "");
                data.add(model);
            }
        }
    }

    private void initListener() {
        allDeal.setOnClickListener(this);
        ivCalendar.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RegularActivity.this, ProjectSurveyActivity.class);
                intent.putExtra("id", regularResponse.data.manageMoneyList.get(position).productId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_regular_calendar:
                startActivity(new Intent(RegularActivity.this, CalendarActivity.class));
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.all_deal:
                Intent intent = new Intent(this, AllTradeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getRegularActivity() {
        RegularRequest regularRequest = new RegularRequest();
        regularRequest.pageSize = Integer.MAX_VALUE + "";
        regularRequest.pageNum = "1";
       subscribe= PropertyBusinessHelper.getRegular(regularRequest).subscribe(new Action1<RegularResponse>() {
            @Override
            public void call(RegularResponse response) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (response.data != null) {
                    DataCache.instance.saveCacheData("haili", "RegularResponse", response);
                    regularResponse = response;
                    setViewData();
                    initListView();
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
                    ViewHelper.showToast(RegularActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }
}
