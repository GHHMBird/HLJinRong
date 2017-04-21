package com.haili.finance.property.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.adapter.CalendarViewAdapter;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.ManageMoney;
import com.haili.finance.business.property.RePayCalendarRequest;
import com.haili.finance.business.property.RePayCalendarResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.modle.CustomDate;
import com.haili.finance.property.adapter.ManageMoneyAdapter;
import com.haili.finance.property.adapter.RecodeAdapter;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.widget.CalendarCard;
import com.haili.finance.widget.MyLayoutManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by fuliang on 2017/3/10.
 */

public class CalendarActivity extends BaseActivity {

    private PullToRefreshView pullToRefreshView;
    private RecyclerView recyclerView;
    private Subscription subscrip;
    private ManageMoneyAdapter manageMoneyAdapter;
    private ViewPager mViewPager;
    private int mCurrentIndex = 498;
    private CalendarCard[] mShowViews;
    private TextView textOne, textTwo;
    private CalendarViewAdapter<CalendarCard> adapter;
    private RecodeAdapter recodeAdapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;
    private String requestData = "";
    CalendarCard[] views;
    private int pageIndex = 1;
    private boolean isLoad = false, hasMoreItems = true;
    private ImageView ivNoPlan;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_layout);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_calendar_pull);
        recyclerView = (RecyclerView) findViewById(R.id.activity_calendar_rv);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
            }
        });
        final MyLayoutManager myLayoutManager = new MyLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = myLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = myLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                // dy>0 表示向下滑动
                if (!isLoad && hasMoreItems && lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    pageIndex++;
                    isLoad = true;
                    manageMoneyAdapter.httpOK = true;
                    getData();
                }
            }
        });
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                hasMoreItems = true;
                getData();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        textOne = (TextView) findViewById(R.id.calendar_text_one);
        textTwo = (TextView) findViewById(R.id.calendar_two_text);
        views = new CalendarCard[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new CalendarCard(this, new CalendarCard.OnCellClickListener() {
                @Override
                public void clickDate(CustomDate date) {

                }

                @Override
                public void changeDate(CustomDate date) {
                    setBarTitle(date.year + "年" + date.month + "月");
                    requestData = date.year + "-" + date.month;
                    pageIndex = 1;
                    initCacheData();
                }
            });
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();
        initCacheData();
        addLoadingFragment(R.id.calendar_layout_fl, "CalendarActivity");
    }

    private void initCacheData() {
        RePayCalendarResponse cacheData = DataCache.instance.getCacheData("haili", "RePayCalendarResponse" + requestData);
        if (cacheData != null) {
            refreshData(cacheData);
            if (cacheData.model.list != null && cacheData.model.list.size() > 0) {
                analyseData(cacheData.model.list, 0);
            }
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("CalendarActivity")) {
            getData();
        }
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
                addLoadingFragment(R.id.calendar_layout_fl, "CalendarActivity");
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /*
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }

    private void getData() {
        RePayCalendarRequest request = new RePayCalendarRequest();
        request.date = requestData;
        request.pageSize = "10";
        request.pageNum = pageIndex + "";
        subscrip=   PropertyBusinessHelper.repayCalendar(request)
                .subscribe(new Action1<RePayCalendarResponse>() {
                    @Override
                    public void call(RePayCalendarResponse rePayCalendarResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        pullToRefreshView.setRefreshing(false);

                        if (rePayCalendarResponse.model != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "RePayCalendarResponse" + requestData, rePayCalendarResponse);
                                analyseData(rePayCalendarResponse.model.list, 2);
                            } else {
                                isLoad = false;
                                if (rePayCalendarResponse.model.list.size() > 0) {
                                    analyseData(rePayCalendarResponse.model.list, 1);
                                } else {
                                    hasMoreItems = false;
                                    manageMoneyAdapter.httpOK = false;
                                    manageMoneyAdapter.notifyDataSetChanged();
                                }
                            }
                            refreshData(rePayCalendarResponse);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (pageIndex != 1 && isLoad) {
                            pageIndex--;
                            isLoad = false;
                            manageMoneyAdapter.httpOK = false;
                            manageMoneyAdapter.notifyDataSetChanged();
                        }

                        pullToRefreshView.setRefreshing(false);

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(CalendarActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void refreshData(RePayCalendarResponse rePayCalendarResponse) {
        textOne.setText(rePayCalendarResponse.model.productNumber == 0 ? "当月无还款项" : Html.fromHtml("当月还款项有<font color=red>" + rePayCalendarResponse.model.productNumber + "</font>项"));
        if (rePayCalendarResponse.model.totalPrincipalInterest > 0.0) {
            textTwo.setText(Html.fromHtml("本息合计<font color=red>" + rePayCalendarResponse.model.totalPrincipalInterest + "</font>"));
        } else {
            textTwo.setText("");
        }
        if (rePayCalendarResponse.model.list != null && rePayCalendarResponse.model.list.size() > 0) {

            //改日历(画出还款日)
            drawRecodeDay(rePayCalendarResponse.model.list);

//            recyclerView.setVisibility(View.VISIBLE);
//            ivNoPlan.setVisibility(View.GONE);
        } else {
//            recyclerView.setVisibility(View.GONE);
//            ivNoPlan.setVisibility(View.VISIBLE);
        }
    }

    private void drawRecodeDay(ArrayList<ManageMoney> list) {//更新还款日历
        ArrayList<Integer> intDays = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ManageMoney manageMoney = list.get(i);
            String repaymentTime = manageMoney.repaymentTime;
            String[] split = repaymentTime.split("-");
            int day = Integer.parseInt(split[2]);
            intDays.add(day);
        }
        for (CalendarCard view : views) {//在viewpager的三个子页面中加入intDays集合，显示还款日期
            view.setList(intDays);
        }
    }

    /**
     * @param list 集合
     * @param type 添加数据的方式 0代表创建adapter并添加 1代表添加数据（add） 2代表刷新集合
     */
    public void analyseData(ArrayList<ManageMoney> list, int type) {
        if (list.size() > 0) {
            switch (type) {
                case 0:
                    manageMoneyAdapter = new ManageMoneyAdapter(this, list);
                    recyclerView.setAdapter(manageMoneyAdapter);
                    break;
                case 1:
                    manageMoneyAdapter.addData(list);
                    break;
                case 2:
                    if (manageMoneyAdapter == null) {
                        manageMoneyAdapter = new ManageMoneyAdapter(this, list);
                        recyclerView.setAdapter(manageMoneyAdapter);
                    } else {
                        manageMoneyAdapter.setData(list);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
