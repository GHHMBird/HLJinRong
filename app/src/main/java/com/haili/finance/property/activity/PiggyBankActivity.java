package com.haili.finance.property.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.property.ListBanalceModel;
import com.haili.finance.business.property.PiggBankRequest;
import com.haili.finance.business.property.PiggBankResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.activity.RechargeActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.AddBankCardActivity;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.activity.PayPasswordActivity;
import com.jn.chart.charts.LineChart;
import com.jn.chart.data.Entry;
import com.jn.chart.manager.LineChartManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;


/*
 *    存钱罐
 * Created by Monkey on 2017/1/18.
 */

public class PiggyBankActivity extends BaseActivity implements View.OnClickListener {
    private TextView rl_detailed, yesterdayEarningsView, tv_title_asstes1, addupEarnings, endDateView, sevenYield, millionProfitView;
    private LineChart linechart;
    private LinearLayout ll_recharge, ll_withdrawals;
    private View placeHolderView;
    private RelativeLayout rl_back;
    private Subscription subscribe;
    private FrameLayout frameLayout;
    private LoadingFragment loadingFragment;
    private PiggBankResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_piggybank);
        initView();
        init();
        addLoadingFragment(R.id.loading_layout, "PiggyBankActivity");
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("PiggyBankActivity")) {
            getIncomeDetails();
        }

    }

    private void initView() {
        yesterdayEarningsView = (TextView) findViewById(R.id.yesterday_earnings);
        LinearLayout llgoDing = (LinearLayout) findViewById(R.id.activity_piggybank_ll);
        llgoDing.setOnClickListener(this);
        tv_title_asstes1 = (TextView) findViewById(R.id.tv_title_asstes1);
        endDateView = (TextView) findViewById(R.id.end_date_view);
        addupEarnings = (TextView) findViewById(R.id.addup_earnings);
        millionProfitView = (TextView) findViewById(R.id.million_profit_view);
        sevenYield = (TextView) findViewById(R.id.seven_yield);
        frameLayout = (FrameLayout) findViewById(R.id.loading_layout);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        linechart = (LineChart) findViewById(R.id.linechart);
        rl_detailed = (TextView) findViewById(R.id.rl_detailed);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        ll_withdrawals = (LinearLayout) findViewById(R.id.ll_withdrawals);
        placeHolderView = findViewById(R.id.place_holder_view);
        placeViewControl();
        initCacheData();
    }

    private void initCacheData() {
        PiggBankResponse cacheData = DataCache.instance.getCacheData("haili", "PiggBankResponse");
        if (cacheData != null) {
            initLineChart(cacheData);
            response = cacheData;
            setViewData();
        }
    }

    private void init() {
        initListener();
    }

    private void initLineChart(PiggBankResponse cacheData) {
        ArrayList<ListBanalceModel> listBanalce = cacheData.data.listBanalce;
        if (listBanalce != null && listBanalce.size() > 0) {
            linechart.setDescription("");
            //设置X轴数据
            ArrayList<String> xList = new ArrayList<>();
            //设置Y轴数据
            ArrayList<Entry> yList = new ArrayList<>();
            for (int i = 1; i - 1 < listBanalce.size(); i++) {
                xList.add(listBanalce.get(i - 1).time);
                yList.add(new Entry((float) listBanalce.get(i - 1).balance, i - 1));
            }

            LineChartManager.setLineName("7日年化收益率");

            //创建曲线的图表
            LineChartManager.initCycleLineChart(this, linechart, xList, yList);
        }
    }

    private void setViewData() {
        yesterdayEarningsView.setText(response.data.yesterdayBanalce + "");
        tv_title_asstes1.setText(response.data.totalBanalce + "");
        endDateView.setText(response.data.endDate);
        addupEarnings.setText(response.data.totalgetBalance + "");
        sevenYield.setText(response.data.sevenYearsYield + "");
        millionProfitView.setText(response.data.millionProfit + "");

    }

    private void placeViewControl() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            this.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeHolderView.setVisibility(View.VISIBLE);
            params.topMargin = dp2px(this, 85f);
            frameLayout.setLayoutParams(params);
        } else {
            placeHolderView.setVisibility(View.GONE);

        }
    }

//    private void initLineChart() {
//
//        linechart.setDrawGridBackground(false);
//        // 没有描述文本
//        linechart.getDescription().setText("");
//        // 支持触控手势
//        linechart.setTouchEnabled(true);
//        // 支持缩放和拖动
//        linechart.setDragEnabled(true);
//        linechart.setScaleEnabled(true);
//        // 如果禁用,扩展可以在x轴和y轴分别完成
//        linechart.setPinchZoom(true);
//        // 设置另一种背景颜色
////        linechart.setBackgroundColor(Color.WHITE);
//        linechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
////                Log.i("VAL SELECTED",
////                        "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
////                                + ", DataSet index: " + dataSetIndex);
//            }
//
//            @Override
//            public void onNothingSelected() {
//            }
//        });
//        // 创建一个自定义MarkerView(扩展MarkerView)和指定的布局
//        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(linechart); // For bounds control
//        linechart.setMarker(mv); // Set the marker to the chart
//        // 设置标记图
//
//        XAxis xl = linechart.getXAxis();
//        xl.setAvoidFirstLastClipping(true);
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的显示位置
//        xl.setAvoidFirstLastClipping(true); //设置x轴起点和终点label不超出屏幕
//        //隐藏左边坐标轴横网格线
//        linechart.getAxisLeft().setDrawGridLines(true);
//        linechart.getAxisLeft().setTextColor(getResources().getColor(R.color.line_grey));
////隐藏右边坐标轴横网格线
//        linechart.getAxisRight().setDrawGridLines(false);
////隐藏X轴竖网格线
//        linechart.getXAxis().setDrawGridLines(false);
//        linechart.getXAxis().setTextColor(getResources().getColor(R.color.line_grey));
//
//        YAxis leftAxis = linechart.getAxisLeft();
////            //Y轴起始值
//        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        leftAxis.setLabelCount(5); // 设置Y轴最多显示的数据个数
//        leftAxis.setAxisLineColor(Color.WHITE);     //设置Y轴线的颜色
//        YAxis rightAxis = linechart.getAxisRight();
//        rightAxis.setEnabled(false);
//        // add data
//        setData(response.data.listBanalce.size(), 18);
//        // 限制的最大扩展的因素
////         mChart.setScaleMinima(1f, 1f);
//        // // center the view to a specific position inside the chart
////         mChart.centerViewPort(10, 50);
////         get the legend (only possible after setting data)
//        Legend l = linechart.getLegend();
//        l.setEnabled(false);
//        // modify the legend ...
//        // l.setPosition(LegendPosition.LEFT_OF_CHART);
////        l.setForm(Legend.LegendForm.CIRCLE);
////        l.setTextColor(getResources().getColor(R.color.linechart_org));
//        linechart.valuesToHighlight();
//        //设置动画
//        linechart.animateXY(3000, 3000);
//        // dont forget to refresh the drawing
//        linechart.invalidate();
////        //折线图切换tab
//        linechart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        linechart.setDoubleTapToZoomEnabled(false);
//
//    }

//    private void setData(int count, float range) {
//
////        ArrayList<Entry> entries = new ArrayList<Entry>();
////        ArrayList<ListBanalceModel>  listBanalce = response.data.listBanalce;
//
//        //构建X轴数据
//        ArrayList<String> xVals = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            // x轴显示的数据，这里默认使用数字下标显示
//            xVals.add(response.data.listBanalce.get(i).time);
//        }
//
//        //模拟一组y轴数据(存放y轴数据的是一个Entry的ArrayList) 他是构建LineDataSet的参数之一
//
//        ArrayList<Entry> yValue = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            yValue.add(new Entry(i, i));
//        }
//
//        //构建一个LineDataSet 代表一组Y轴数据 （比如不同的彩票： 七星彩  双色球）
//        LineDataSet dataSet = new LineDataSet(yValue, "双色球");
//
//        //模拟第二组组y轴数据(存放y轴数据的是一个Entry的ArrayList) 他是构建LineDataSet的参数之一
//
//        ArrayList<Entry> yValue1 = new ArrayList<>();
//
//        yValue1.add(new Entry(7, 0));
//        yValue1.add(new Entry(17, 1));
//        yValue1.add(new Entry(3, 2));
//        yValue1.add(new Entry(5, 3));
//        yValue1.add(new Entry(4, 4));
//        yValue1.add(new Entry(3, 5));
//        yValue1.add(new Entry(7, 6));
//
//        //构建一个LineDataSet 代表一组Y轴数据 （比如不同的彩票： 七星彩  双色球）
//
//        LineDataSet dataSet1 = new LineDataSet(yValue1, "七星彩");
//        dataSet1.setColor(Color.BLACK);
//        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
//        ArrayList<LineDataSet> dataSets = new ArrayList<>();
//
//        //将数据加入dataSets
//        dataSets.add(dataSet);
//        dataSets.add(dataSet1);
//
//
//        // sort by x-value
////        Collections.sort(entries, new EntryXComparator());
////        // create a dataset and give it a type
////        LineDataSet set1 = new LineDataSet(entries, "");
//        set1.setMode(CUBIC_BEZIER);
////        set1.setDrawValues(false);
////        set1.setDrawCircles(true);
////        set1.setCubicIntensity(0.15f);
//////        set1.setDrawFilled(true);
//////        set1.setFillColor(Color.rgb(237,85,42));
//////        set1.setColor(Color.rgb(237,85,42));
////        set1.setCircleSize(2.0f);//射置曲线上圆点大小
////        set1.setCircleColor(getResources().getColor(R.color.linechart_org));//射置曲线上圆点颜色
////        set1.setDrawCircleHole(false);
////        set1.setLineWidth(1.5f);
////        set1.setCircleRadius(4f);
//
//        // 折线的颜色
////        set1.setColor(getResources().getColor(R.color.linechart_org));
//        // create a data object with the datasets
////        LineData data = new LineData(xVals,dataSets);
//
//        // set data
////        linechart.setData(data);
//    }

    private void initListener() {
        rl_back.setOnClickListener(this);
        rl_detailed.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);
        ll_withdrawals.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_detailed:
                startActivity(new Intent(this, RetailedActivity.class));
                break;
            case R.id.ll_recharge:
                if (checkState()) {
                    startActivity(new Intent(this, RechargeActivity.class));
                }
                break;
            case R.id.ll_withdrawals:
                if (checkState()) {
                    startActivity(new Intent(this, WithdrawalsActivity.class));
                }
                break;
            case R.id.activity_piggybank_ll:
                Intent intent = new Intent();
                intent.putExtra("result", "goToManage");
                setResult(5555, intent);
                finish();
                break;
            default:
                break;
        }
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void getIncomeDetails() {
        subscribe = PropertyBusinessHelper.getPiggBankData(new PiggBankRequest()).subscribe(new Action1<PiggBankResponse>() {
            @Override
            public void call(PiggBankResponse piggBankResponse) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (piggBankResponse.data != null) {
                    DataCache.instance.saveCacheData("haili", "PiggBankResponse", piggBankResponse);
                    initLineChart(piggBankResponse);
                    response = piggBankResponse;
                    setViewData();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                if (throwable instanceof RequestErrorThrowable) {
                    RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                    ViewHelper.showToast(PiggyBankActivity.this, requestErrorThrowable.getMessage());
                }
            }
        });
    }

    private boolean checkState() {
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (loginResponse != null) {
            if (loginResponse.loginModel.certificationState.equals("1")) {
                startActivity(new Intent(PiggyBankActivity.this, IDInformationActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("2") || loginResponse.loginModel.certificationState.equals("5")) {
                startActivity(new Intent(PiggyBankActivity.this, AddBankCardActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("3")) {
                startActivity(new Intent(PiggyBankActivity.this, PayPasswordActivity.class));
                return false;
            }
        } else {
            startActivity(new Intent(PiggyBankActivity.this, LoginActivity.class));
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe != null) {
            subscribe.unsubscribe();
        }
    }
}
