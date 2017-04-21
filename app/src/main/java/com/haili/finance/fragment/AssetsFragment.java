package com.haili.finance.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.property.PropertyHomeRequest;
import com.haili.finance.business.property.PropertyHomeResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.activity.RechargeActivity;
import com.haili.finance.property.activity.CalendarActivity;
import com.haili.finance.property.activity.EarningDetailActivity;
import com.haili.finance.property.activity.PiggyBankActivity;
import com.haili.finance.property.activity.RegularActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.AddBankCardActivity;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.activity.PayPasswordActivity;
import com.haili.finance.widget.SimpleLineChart;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscription;
import rx.functions.Action1;


/*
 * Created by Monkey on 2017/1/7.
 * 资产首页Fragment
 */

public class AssetsFragment extends BaseFragment implements View.OnClickListener {

    private ImageView iv_eye1,iv_eye2;
    private Boolean boolen_iv_eye1 = false;
    private LinearLayout infoLayout,moneyLayout;
    private Subscription subscrip;
    private TextView tv_title_asstes1, tv_recharge, allEarnings, endDateView,tv_title_asstes2,all_earnings2;
    private TextView earningsRanking, depositView, depositEarningsView, fixDateBanalce, fixDateReceiveBanalce;
    private RelativeLayout ll_piggy_bank, ll_regular;
    private View placeView;
    private TextView earningDetailBtn,chartTitle;
    private ImageView calendarView;
    private PropertyHomeResponse response;
    private static final int MSG_DATA_CHANGE = 0x11;
    private Handler handler;
    private SimpleLineChart mSimpleLineChart;
    private int mX = 0;
    private double maxY = 0;

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            setViewData();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = null;
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_assets, container, false);
        }
        Bus.getDefault().register(this);
        initView(root);
        placeViewControl();
        init();
        response = DataCache.instance.getCacheData("haili","PropertyHomeResponse");
        if (response != null){
            setViewData();
        }
        addLoadingFragment(R.id.loading_layout, "AssetsFragment");
        return root;
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event){
            case "AssetsFragment":
                getPropertyData();
                break;
            case "reload_some_interface":
                getPropertyData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus.getDefault().unregister(this);
    }

    private void init() {
        initListener();
    }


    private void initListener() {
        iv_eye2.setOnClickListener(this);
        iv_eye1.setOnClickListener(this);
        ll_piggy_bank.setOnClickListener(this);
        ll_regular.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        earningDetailBtn.setOnClickListener(this);
        calendarView.setOnClickListener(this);
    }

    private void initView(View root) {
        iv_eye2 = (ImageView)root.findViewById(R.id.iv_eye2);
        iv_eye1 = (ImageView) root.findViewById(R.id.iv_eye1);
        tv_title_asstes2 = (TextView)root.findViewById(R.id.tv_title_asstes2);
        tv_title_asstes1 = (TextView) root.findViewById(R.id.tv_title_asstes1);
        ll_piggy_bank = (RelativeLayout) root.findViewById(R.id.ll_piggy_bank);
        ll_regular = (RelativeLayout) root.findViewById(R.id.ll_regular);
        tv_recharge = (TextView) root.findViewById(R.id.tv_recharge);
        placeView = root.findViewById(R.id.place_holder_view);
        earningDetailBtn = (TextView) root.findViewById(R.id.earning_detail);
        calendarView = (ImageView) root.findViewById(R.id.calendar_view);

        //fu
        moneyLayout = (LinearLayout)root.findViewById(R.id.money_layout);
        chartTitle = (TextView)root.findViewById(R.id.chart_title);
        infoLayout = (LinearLayout)root.findViewById(R.id.info_layout);
        allEarnings = (TextView) root.findViewById(R.id.all_earnings);
        all_earnings2 = (TextView)root.findViewById(R.id.all_earnings2);
        endDateView = (TextView) root.findViewById(R.id.end_date_view);
        earningsRanking = (TextView) root.findViewById(R.id.earnings_ranking);
        depositView = (TextView) root.findViewById(R.id.deposit_view);
        depositEarningsView = (TextView) root.findViewById(R.id.deposit_earnings_view);
        fixDateBanalce = (TextView) root.findViewById(R.id.fixDate_banalce);
        fixDateReceiveBanalce = (TextView) root.findViewById(R.id.fixDate_receive_banalce);
        mSimpleLineChart = (SimpleLineChart)root.findViewById(R.id.simpleLineChart);
    }

    private void setViewData(){
        if (response.data.listDayBalance.size()!= 3){
            infoLayout.setVisibility(View.VISIBLE);
            moneyLayout.setVisibility(View.GONE);
            mSimpleLineChart.setVisibility(View.GONE);
            chartTitle.setVisibility(View.GONE);
        }else {
            infoLayout.setVisibility(View.GONE);
            moneyLayout.setVisibility(View.VISIBLE);
            mSimpleLineChart.setVisibility(View.VISIBLE);
            chartTitle.setVisibility(View.VISIBLE);
            DecimalFormat df = new DecimalFormat("######0.00");
            for (int x = 0 ; x < response.data.listDayBalance.size();x++ ){
                if (maxY <= response.data.listDayBalance.get(x).balance){
                    maxY = response.data.listDayBalance.get(x).balance;
                }
            }
            String[] xItem = {response.data.listDayBalance.get(0).time, response.data.listDayBalance.get(1).time, response.data.listDayBalance.get(2).time};
            String[] yItem = {df.format(maxY), df.format( maxY / 7 * 6) + "", df.format( maxY / 7 * 5) + "", df.format(maxY / 7 * 4) + "", df.format( maxY / 7 * 3) + "", df.format( maxY / 7 * 2) + "", df.format(maxY / 7 * 1) + ""};
            mSimpleLineChart.setXItem(xItem);
            mSimpleLineChart.setYItem(yItem);
            final HashMap<Integer, Integer> pointMap = new HashMap();
            final ArrayList<Float> data = new ArrayList<>();
            for (int i = 0; i < xItem.length; i++) {
                pointMap.put(i, i);
            }
            data.add((float) response.data.listDayBalance.get(0).balance / 9);
            data.add((float) response.data.listDayBalance.get(1).balance / 9);
            data.add((float) response.data.listDayBalance.get(2).balance / 9);
            mSimpleLineChart.setData(pointMap, data);
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    switch (msg.what) {
                        case MSG_DATA_CHANGE://接到消息开始画折线图
                            mSimpleLineChart.drawPointAndLine(mX);
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }
            };

            new Thread(){//每隔一秒发送一次，生成开场动画
                @Override
                public void run() {
                    for (int x = 1 ; x <= data.size() ;x++){
                        Message message = new Message();
                        message.what = MSG_DATA_CHANGE;
                        mX =x;
                        handler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        if (response.data.isHaveDeposit.equals(1)){
            depositEarningsView.setText(response.data.wxStoreReceiveBalance + "元");
            depositView.setText(response.data.wxStoreReceiveBalance + "元");
            ll_piggy_bank.setVisibility(View.VISIBLE);
        }else {
            ll_piggy_bank.setVisibility(View.GONE);
        }
        tv_title_asstes1.setText(response.data.totalBanalce+ "元");
        tv_title_asstes2.setText(response.data.totalBanalce+ "元");
        allEarnings.setText(response.data.totalIncome + "元");
        all_earnings2.setText(response.data.totalIncome + "元");
        endDateView.setText(response.data.endDate);
        earningsRanking.setText("累计收益排名" + response.data.ranking + "名，较前日" + response.data.rankNum + "名");
        fixDateBanalce.setText(response.data.fixDateBalance + "元");
        fixDateReceiveBanalce.setText(response.data.fixDateReceiveBalance + "元");

        if(response.data.ranking == 0){
            earningsRanking.setText("累计收益暂无排名");
        }else {
            ColorStateList redColors = ColorStateList.valueOf(getResources().getColor(R.color.text_red));
            SpannableStringBuilder builder = new SpannableStringBuilder(earningsRanking.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_red));
            ForegroundColorSpan redSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_red));
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(getResources().getColor(R.color.green_text));
            ForegroundColorSpan textMain = new ForegroundColorSpan(getResources().getColor(R.color.text_main));
            builder.setSpan(redSpan, earningsRanking.getText().toString().indexOf("日")+1, earningsRanking.getText().toString().length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new TextAppearanceSpan(null, 0, 50,redColors, null), 6, earningsRanking.getText().toString().indexOf("，")-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            if (response.data.rankNum > 0){
                builder.setSpan(redSpan2, earningsRanking.getText().toString().indexOf("日")+1, earningsRanking.getText().toString().length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                earningsRanking.setText(builder);
                Drawable nav_up=getResources().getDrawable(R.mipmap.go_up);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                earningsRanking.setCompoundDrawables(null, null, nav_up, null);
                return;
            }
            if(response.data.rankNum < 0){
                builder.setSpan(greenSpan, earningsRanking.getText().toString().indexOf("日")+1, earningsRanking.getText().toString().length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                earningsRanking.setText(builder);
                Drawable nav_up=getResources().getDrawable(R.mipmap.decline);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                earningsRanking.setCompoundDrawables(null, null, nav_up, null);
                return;
            }
            if (response.data.rankNum == 0){
                builder.setSpan(textMain, earningsRanking.getText().toString().indexOf("日")+1, earningsRanking.getText().toString().length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                earningsRanking.setText(builder);
            }
        }


    }

    private void placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeView.setVisibility(View.VISIBLE);
        } else {
            placeView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_eye2:
                if (boolen_iv_eye1) {
                    boolen_iv_eye1 = false;
                    iv_eye2.setImageResource(R.mipmap.ic_eye_normal);
                    tv_title_asstes1.setText(response.data.totalBanalce+ "元");
                    tv_title_asstes2.setText(response.data.totalBanalce+ "元");
                } else {
                    boolen_iv_eye1 = true;
                    iv_eye2.setImageResource(R.mipmap.ic_eye_selected);
                    tv_title_asstes1.setText("***");
                    tv_title_asstes2.setText("***");
                }
                break;
            case R.id.iv_eye1:
                if (boolen_iv_eye1) {
                    boolen_iv_eye1 = false;
                    iv_eye1.setImageResource(R.mipmap.ic_eye_normal);
                    tv_title_asstes1.setText("0.00");
                } else {
                    boolen_iv_eye1 = true;
                    iv_eye1.setImageResource(R.mipmap.ic_eye_selected);
                    tv_title_asstes1.setText("***");
                    tv_title_asstes2.setText("***");
                }
                break;
            case R.id.ll_piggy_bank://存钱罐
                getActivity().startActivityForResult(new Intent(getActivity(), PiggyBankActivity.class), 5556);
                break;
            case R.id.ll_regular://定期
                startActivity(new Intent(getActivity(), RegularActivity.class));
                break;
            case R.id.tv_recharge:
                if (checkState()) {//充值
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                    MobclickAgent.onEvent(getActivity(), "Assets_recharge_action");
                }
                break;
            case R.id.earning_detail://收益明细
                startActivity(new Intent(getActivity(), EarningDetailActivity.class));
                break;
            case R.id.calendar_view://还款日历
                startActivity(new Intent(getActivity(), CalendarActivity.class));
                MobclickAgent.onEvent(getActivity(), "Assets_repayloaded_action");
                break;
            default:

                break;
        }
    }

    private boolean checkState() {
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (loginResponse != null) {
            if (loginResponse.loginModel.certificationState.equals("1")) {
                startActivity(new Intent(getActivity(), IDInformationActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("2") || loginResponse.loginModel.certificationState.equals("5")) {
                startActivity(new Intent(getActivity(), AddBankCardActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("3")) {
                startActivity(new Intent(getActivity(), PayPasswordActivity.class));
                return false;
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return false;
        }
        return true;
    }
    private void getPropertyData() {
        subscrip=    PropertyBusinessHelper.getCanUseRedPacket(new PropertyHomeRequest())
                .subscribe(new Action1<PropertyHomeResponse>() {
            @Override
            public void call(PropertyHomeResponse propertyHomeResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                response = propertyHomeResponse;
                Message message = new Message();
                mHandler.sendMessage(message);
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
                    if (getActivity() != null) {
                        ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
