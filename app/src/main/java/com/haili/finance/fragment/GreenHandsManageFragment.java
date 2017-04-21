package com.haili.finance.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.adapter.GreenHandsManageAdapter;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.manage.ManageListModel;
import com.haili.finance.business.manage.ManageListRequest;
import com.haili.finance.business.manage.ManageListResponse;
import com.haili.finance.helper.ManageBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.activity.ProductDetailActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by fuliang on 2017/3/9.
 */
@SuppressLint("ValidFragment")
public class GreenHandsManageFragment extends BaseFragment {

    private PullToRefreshView pullToRefresh;
    private RecyclerView mRecyclerView;
    private GreenHandsManageAdapter manageAdapter;
    private MyLayoutManager myLayoutManager;
    private boolean isLoading = false;
    private boolean hasMoreItems = true;
    private int pageIndex = 1;
    private boolean isLoadingNext = false;
    private ManageListResponse manageListResponse;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private Subscription subscribe ;
    private AnimationDrawable animationDrawable;
    private RelativeLayout loadingFailLayout;
    private ImageView loadingFailImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal_manage, container, false);
        Bus.getDefault().register(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        pullToRefresh = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        frameLayout = (SlidingFrameLayout) view.findViewById(R.id.loading_layout);
        imageView = (ImageView) view.findViewById(R.id.loading_view);
        loadingFailLayout = (RelativeLayout) view.findViewById(R.id.loading_fail_layout);
        loadingFailImage = (ImageView) view.findViewById(R.id.loading_fail_image);
        loadingFailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingFailLayout.setVisibility(View.GONE);
                animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                getManageList();
            }
        });
        getAllData();
        myLayoutManager = new MyLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(myLayoutManager);
        manageAdapter = new GreenHandsManageAdapter(this, getActivity());
        manageAdapter.setOnCkeckedListener(new GreenHandsManageAdapter.OnEditDoneListener() {
            @Override
            public void onEditDone(ManageListModel model) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ManageListModel", model);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(manageAdapter);
        ManageListResponse cacheData = DataCache.instance.getCacheData("haili", "NewManageListResponse2");
        if (cacheData!=null) {
            manageAdapter.setData(cacheData.data.manageListDetail);
            mRecyclerView.setAdapter(manageAdapter);
        }
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getManageList();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = myLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = myLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                // dy>0 表示向下滑动
                if (!isLoading && hasMoreItems && lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    pageIndex++;
                    isLoading = true;
                    getManageList();
                }
            }
        });
        return view;
    }

    private void getAllData() {
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getManageList();
    }

    @BusReceiver
    public void stringEvent(String event){
        switch (event){
            case "reload_some_interface":
                getAllData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus.getDefault().unregister(this);
    }

    private void getManageList() {
        ManageListRequest request = new ManageListRequest();
        request.type = "2";
        request.pageNum = pageIndex;
        request.pageSize = 10;
//        request.redPacket = "";
        request.terminal = "app";
        subscribe = ManageBusinessHelper.getManageList(request).subscribe(new Action1<ManageListResponse>() {
            @Override
            public void call(ManageListResponse manageListResponse) {

                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
                frameLayout.setVisibility(View.GONE);
                GreenHandsManageFragment.this.manageListResponse = manageListResponse;
                if (manageListResponse.data.manageListDetail.size() == 0) {
                    ViewHelper.showToast(getActivity(), "获取数据失败");
                    return;
                }
                if (pageIndex == manageListResponse.data.pageCount) {
                    hasMoreItems = false;
                } else {
                    hasMoreItems = true;
                }
                pullToRefresh.setRefreshing(false);
                manageAdapter.isHasMoreItem(hasMoreItems);
                if (isLoadingNext) {
                    manageAdapter.loadingMoreItem();
                    manageAdapter.setData(manageListResponse.data.manageListDetail);
                    manageAdapter.notifyDataSetChanged();
                    isLoadingNext = false;
                    return;
                }

                if (GreenHandsManageFragment.this.manageListResponse != null) {
                    manageAdapter.clearData();
                    manageAdapter.setData(manageListResponse.data.manageListDetail);
                    manageAdapter.notifyDataSetChanged();
                } else {
                    manageAdapter.setData(manageListResponse.data.manageListDetail);
                    mRecyclerView.setAdapter(manageAdapter);
                }
                DataCache.instance.saveCacheData("haili", "NewManageListResponse2", manageListResponse);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

                if (isLoadingNext) {
                    pageIndex--;
                    hasMoreItems = true;
                    manageAdapter.loadingMoreItem();
                    manageAdapter.notifyDataSetChanged();
                }
                pullToRefresh.setRefreshing(false);
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
                if (DataCache.instance.getCacheData("haili", "NewManageListResponse2") != null) {
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

    @Override
    public void onStop() {
        super.onStop();
        if (subscribe !=null) {
            subscribe.unsubscribe();
        }
    }
}
