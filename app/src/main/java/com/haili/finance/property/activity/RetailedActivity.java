package com.haili.finance.property.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.adapter.RetailedAdapter;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.SaveDetailListModel;
import com.haili.finance.business.property.SaveDetailRequest;
import com.haili.finance.business.property.SaveDetailResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.utils.DensityUtil;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/18.
 * 明细列表页面
 */

public class RetailedActivity extends BaseActivity implements View.OnClickListener {
//    private RelativeLayout rl_back;
    private ListView listview;
    private RetailedAdapter retailedAdapter;
//    private List<RetailedModel> data;
    private PopupWindow popupWindow;
    TextView rl_screen;
    RelativeLayout titleView;
    private Subscription subscribe ;
    SaveDetailResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTheme();
        setContentView(R.layout.activity_retailed);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("明细");
        initView();
        init();
        addLoadingFragment(R.id.loading_layout,"RetailedActivity");
    }

    @BusReceiver
    public void StringEvnent(String event){
        if (event.equals("RetailedActivity")){
            getData();
        }
    }

    private void getData(){
        SaveDetailRequest request = new SaveDetailRequest();

        subscribe =  PropertyBusinessHelper.getSaveDetails(request).subscribe(new Action1<SaveDetailResponse>() {
            @Override
            public void call(SaveDetailResponse saveDetailResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }
                response = saveDetailResponse;
                initList();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null){
                    removeLoadingFragment();
                }

                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(RetailedActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private void init() {
        initListener();
    }

    private void initListener() {
//        rl_back.setOnClickListener(this);
        rl_screen.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(RetailedActivity.this, RetailedDetailsActivity.class);
                intent.putExtra("date", response.data.listModels.get(position).time);
                intent.putExtra("money", response.data.listModels.get(position).banalce);
                intent.putExtra("type", response.data.listModels.get(position).type);
                intent.putExtra("content", response.data.listModels.get(position).remark);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        retailedAdapter = new RetailedAdapter(this, response.data.listModels, R.layout.item_retailed);
        listview.setAdapter(retailedAdapter);

    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
//        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_screen = (TextView) findViewById(R.id.rl_screen);
        titleView = (RelativeLayout)findViewById(R.id.title_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_screen:
                showPopuwindow(rl_screen);
                break;
            default:
                break;
        }
    }

    private int popTab = 0;

    private void showPopuwindow(View v) {
        View root = this.getLayoutInflater().inflate(R.layout.layout_pop_retailed, null);
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
        final TextView tv_profit = (TextView) root.findViewById(R.id.tv_profit);
        switch(popTab){
            case 0:
                tv_all.setTextColor(Color.WHITE);
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_into.setTextColor(getResources().getColor(R.color.text_main));
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_out.setTextColor(getResources().getColor(R.color.text_main));
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_profit.setTextColor(getResources().getColor(R.color.text_main));
                tv_profit.setBackgroundResource(R.drawable.btn_bg_tab_nor);
            break;
            case 1:
                tv_into.setTextColor(Color.WHITE);
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_all.setTextColor(getResources().getColor(R.color.text_main));
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_out.setTextColor(getResources().getColor(R.color.text_main));
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_profit.setTextColor(getResources().getColor(R.color.text_main));
                tv_profit.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            case 2:
                tv_out.setTextColor(Color.WHITE);
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_all.setTextColor(getResources().getColor(R.color.text_main));
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_into.setTextColor(getResources().getColor(R.color.text_main));
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_profit.setTextColor(getResources().getColor(R.color.text_main));
                tv_profit.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            case 3:
                tv_profit.setTextColor(Color.WHITE);
                tv_profit.setBackgroundResource(R.drawable.btn_bg_tab_ed);
                tv_all.setTextColor(getResources().getColor(R.color.text_main));
                tv_all.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_into.setTextColor(getResources().getColor(R.color.text_main));
                tv_into.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                tv_out.setTextColor(getResources().getColor(R.color.text_main));
                tv_out.setBackgroundResource(R.drawable.btn_bg_tab_nor);
                break;
            default:

            break;
        }
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTab != 0) {
                    popTab = 0;
                }
                listview.setAdapter(new RetailedAdapter(RetailedActivity.this, response.data.listModels, R.layout.item_retailed));
                popupWindow.dismiss();
            }
        });
        tv_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTab != 1) {
                    popTab = 1;
                }
                popupWindow.dismiss();
                if (response == null){
                    return;
                }
                if (response.data.listModels.size() == 0){
                    return;
                }
                ArrayList<SaveDetailListModel> newdata = new ArrayList();
                for (int i = 0; i < response.data.listModels.size(); i++) {
                    if (response.data.listModels.get(i).type.equals("转入")) {
                        newdata.add(response.data.listModels.get(i));
                    }
                }
                listview.setAdapter(new RetailedAdapter(RetailedActivity.this, newdata, R.layout.item_retailed));
                popupWindow.dismiss();
            }
        });
        tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTab != 2) {
                    popTab = 2;
                }
                if (response == null){
                    return;
                }
                if (response.data.listModels.size() == 0){
                    return;
                }
                ArrayList<SaveDetailListModel> newdata = new ArrayList();
                for (int i = 0; i < response.data.listModels.size(); i++) {
                    if (response.data.listModels.get(i).type.equals("转出")) {
                        newdata.add(response.data.listModels.get(i));
                    }
                }

                listview.setAdapter(new RetailedAdapter(RetailedActivity.this, newdata, R.layout.item_retailed));
                popupWindow.dismiss();
            }
        });
        tv_profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTab != 3) {
                    popTab = 3;
                }
                if (response == null){
                    return;
                }
                if (response.data.listModels.size() == 0){
                    return;
                }
                ArrayList<SaveDetailListModel> newdata = new ArrayList();
                for (int i = 0; i < response.data.listModels.size(); i++) {
                    if (response.data.listModels.get(i).type.equals("收益")) {
                        newdata.add(response.data.listModels.get(i));
                    }
                }
                listview.setAdapter(new RetailedAdapter(RetailedActivity.this, newdata, R.layout.item_retailed));
                popupWindow.dismiss();

            }
        });
        popupWindow.showAsDropDown(titleView, 0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }
}
