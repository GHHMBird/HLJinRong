package com.haili.finance.manage.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.property.InvestmentRecordListModel;
import com.haili.finance.business.property.InvestmentRecordRequest;
import com.haili.finance.business.property.InvestmentRecordResponse;
import com.haili.finance.helper.PropertyBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.property.adapter.InvestmentRedrdAdapter;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/19.
 * 投资记录fragment
 */

@SuppressLint("ValidFragment")
public class InvestmentRecordFragment extends BaseFragment {
    private String manageMoneyId;
    private int pageIndex = 1;
    private boolean hasMoreItems = true, isLoad = false;
    private RecyclerView recyclerView;
    private AnimationDrawable animationDrawable;
    private InvestmentRedrdAdapter investmentRedrdAdapter;
    private Subscription subscrip;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private RelativeLayout loadingFailLayout;
    private ImageView loadingFailImage;
    private PullToRefreshView pullToRefreshView;

    public InvestmentRecordFragment(String manageMoneyId) {
        this.manageMoneyId = manageMoneyId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_investment_record, container, false);
        initView(root);
        init();
        return root;
    }

    private void initView(View root) {
        frameLayout = (SlidingFrameLayout)root.findViewById(R.id.loading_layout) ;
        imageView = (ImageView) root.findViewById(R.id.loading_view);
        if (TextUtils.isEmpty(manageMoneyId)) {//查询全部商品,显示可见
            root.findViewById(R.id.fragmet_investment_record_name).setVisibility(View.VISIBLE);
        } else {//查询某商品，隐藏名称，不可见
            root.findViewById(R.id.fragmet_investment_record_name).setVisibility(View.INVISIBLE);
        }
        loadingFailLayout = (RelativeLayout)root.findViewById(R.id.loading_fail_layout);
        loadingFailImage = (ImageView) root.findViewById(R.id.loading_fail_image);
        loadingFailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingFailLayout.setVisibility(View.GONE);
                animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                getAllData();
            }
        });
        pullToRefreshView = (PullToRefreshView) root.findViewById(R.id.fragment_investment_record_pull);
        recyclerView = (RecyclerView) root.findViewById(R.id.fragment_investment_record_recyclerview);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
            }
        });
        final MyLayoutManager myLayoutManager = new MyLayoutManager(getActivity());
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
                    investmentRedrdAdapter.httpOK = true;
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
    }

    private void init() {
        InvestmentRecordResponse response = DataCache.instance.getCacheData("haili","InvestmentRecordResponse_investment");
        if (response != null){
            if (response.investmentRecord.size()>0){
                analyseData(response, 2);
            }
        }
        getAllData();
    }

    private void getAllData() {
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getData();
    }

    public void getData() {
        InvestmentRecordRequest request = new InvestmentRecordRequest();
        request.pageSize = "10";
        request.type = "1";
        request.manageMoneyId = manageMoneyId;
        request.pageNum = pageIndex + "";
        subscrip=   PropertyBusinessHelper.getTradingRecord(request)
                .subscribe(new Action1<InvestmentRecordResponse>() {
                    @Override
                    public void call(InvestmentRecordResponse investmentRecordResponse) {
                        if (pageIndex == 1){
                            DataCache.instance.saveCacheData("haili","InvestmentRecordResponse_investment",investmentRecordResponse);
                        }

                        if (animationDrawable != null){
                            animationDrawable.stop();
                        }
                        frameLayout.setVisibility(View.GONE);

                        pullToRefreshView.setRefreshing(false);

                        if (investmentRecordResponse.investmentRecord!= null) {
                            if (pageIndex == 1) {
                                analyseData(investmentRecordResponse, 2);
                            } else {
                                isLoad = false;
                                if (investmentRecordResponse.investmentRecord.size() > 0) {
                                    analyseData(investmentRecordResponse, 1);
                                } else {
                                    hasMoreItems = false;
                                    investmentRedrdAdapter.httpOK = false;
                                    investmentRedrdAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (pageIndex != 1 && isLoad) {
                            pageIndex--;
                            isLoad = false;
                            investmentRedrdAdapter.httpOK = false;
                            investmentRedrdAdapter.notifyDataSetChanged();
                        }
                        pullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "InvestmentRecordResponse_investment") != null) {
                            frameLayout.setVisibility(View.GONE);
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                if (getActivity() != null) {
                                    ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                                }
                            }
                        } else {
                            loadingFailLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /*
     * @param response 集合数据
     * @param type     添加数据的方式 0代表创建adapter并添加 1代表添加数据（add） 2代表刷新集合
     */
    public void analyseData(InvestmentRecordResponse response, int type) {
        ArrayList<InvestmentRecordListModel> msgList = response.investmentRecord;
        if (msgList.size() > 0) {
            switch (type) {
                case 0:
                    investmentRedrdAdapter = new InvestmentRedrdAdapter(getActivity(), msgList,manageMoneyId);
                    recyclerView.setAdapter(investmentRedrdAdapter);
                    break;
                case 1:
                    investmentRedrdAdapter.addData(msgList);
                    break;
                case 2:
                    if (investmentRedrdAdapter == null) {
                        investmentRedrdAdapter = new InvestmentRedrdAdapter(getActivity(), msgList,manageMoneyId);
                        recyclerView.setAdapter(investmentRedrdAdapter);
                    } else {
                        investmentRedrdAdapter.setData(msgList);
                    }
                    break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}

