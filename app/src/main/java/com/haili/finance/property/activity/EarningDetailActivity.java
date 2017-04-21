package com.haili.finance.property.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.MyEarningsRequest;
import com.haili.finance.business.property.MyEarningsResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.property.adapter.MyEarningsListAdapter;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.DensityUtil;
import com.haili.finance.widget.MyLayoutManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by lfu on 2017/2/24.
 */

public class EarningDetailActivity extends BaseActivity {

    private RecyclerView listView;
    private MyEarningsResponse response;
    private PopupWindow popupWindow;
    private RelativeLayout titleView;
    private TextView allEarnings;
    private int popTab = 0;
    private Subscription subscrip;
    private MyLayoutManager myLayoutManager;
    MyEarningsListAdapter adapter;
    String type = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_detail);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("收益明细");
        listView = (RecyclerView) findViewById(R.id.my_list);
        titleView = (RelativeLayout) findViewById(R.id.title_view);
        allEarnings = (TextView) findViewById(R.id.all_earnings);
        TextView checkBtn = (TextView) findViewById(R.id.check_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWindow();
            }
        });
        myLayoutManager = new MyLayoutManager(this);
        listView.setLayoutManager(myLayoutManager);
        initCacheData();
        addLoadingFragment(R.id.loading_fragment, "EarningDetailActivity");
    }

    private void initCacheData() {
        response = DataCache.instance.getCacheData("haili", "MyEarningsResponse" + type);
        if (response != null) {
            allEarnings.setText(response.data.totalBanalce + "");
            if (response.data.listBalance != null && response.data.listBalance.size() > 0) {
                initAdapter();
            }
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "EarningDetailActivity":
                getData();
                break;
        }
    }

    public void getData() {
        MyEarningsRequest request = new MyEarningsRequest();
        request.timeSlot = type;
        subscrip=  PropertyBusinessHelper.getMyEarnings(request).subscribe(new Action1<MyEarningsResponse>() {
            @Override
            public void call(MyEarningsResponse myEarningsResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (myEarningsResponse.data != null) {
                    DataCache.instance.saveCacheData("haili", "MyEarningsResponse" + type, myEarningsResponse);
                    response = myEarningsResponse;
                    allEarnings.setText(response.data.totalBanalce + "");
                    if (response.data.listBalance != null && response.data.listBalance.size() > 0) {
                        initAdapter();
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (DataCache.instance.getCacheData("haili", "MyEarningsResponse" + type) != null) {
                    LoadingFragment loadingFragment = findLoadingFragment();
                    if (loadingFragment != null) {
                        removeLoadingFragment();
                    }
                    if (throwable instanceof RequestErrorThrowable) {
                        RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                        ViewHelper.showToast(EarningDetailActivity.this, requestErrorThrowable.getMessage());
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

    private void initAdapter(){
        adapter = new MyEarningsListAdapter(EarningDetailActivity.this,response.data.listBalance);
        listView.setAdapter(adapter);
    }

    private void showWindow() {
        View root = this.getLayoutInflater().inflate(R.layout.layout_pop_my_earnings, null);
        popupWindow = new PopupWindow(root, WindowManager.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 80f));
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);   //设置窗口显示的动画效果
        final TextView tv_all = (TextView) root.findViewById(R.id.tv_all);
        final TextView tv_into = (TextView) root.findViewById(R.id.tv_into);
        final TextView tv_out = (TextView) root.findViewById(R.id.tv_out);
        switch (popTab) {
            case 0:
                tv_all.setTextColor(Color.WHITE);
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_into.setTextColor(getResources().getColor(R.color.text_main));
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_out.setTextColor(getResources().getColor(R.color.text_main));
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            case 1:
                tv_into.setTextColor(Color.WHITE);
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_all.setTextColor(getResources().getColor(R.color.text_main));
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_out.setTextColor(getResources().getColor(R.color.text_main));
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            case 2:
                tv_out.setTextColor(Color.WHITE);
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_all.setTextColor(getResources().getColor(R.color.text_main));
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_into.setTextColor(getResources().getColor(R.color.text_main));
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            default:
                break;
        }
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTab != 0) {
                    popTab = 0;
                    type = "1";
                    initCacheData();
                    addLoadingFragment(R.id.loading_fragment, "EarningDetailActivity");
                }
                popupWindow.dismiss();
            }
        });
        tv_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popTab != 1) {
                    popTab = 1;
                    type = "2";
                    initCacheData();
                    addLoadingFragment(R.id.loading_fragment, "EarningDetailActivity");
                }
                popupWindow.dismiss();
            }
        });
        tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popTab != 2) {
                    popTab = 2;
                    type = "3";
                    initCacheData();
                    addLoadingFragment(R.id.loading_fragment, "EarningDetailActivity");
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(titleView, 0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
