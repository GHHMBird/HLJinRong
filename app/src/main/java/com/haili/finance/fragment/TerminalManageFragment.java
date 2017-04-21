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
import com.haili.finance.adapter.ManageAdapter;
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

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by fuliang on 2017/3/1.
 */

@SuppressLint("ValidFragment")
public class TerminalManageFragment extends BaseFragment {

    private PullToRefreshView pullToRefresh;
    private RecyclerView mRecyclerView;
    private ManageAdapter manageAdapter;
    private MyLayoutManager myLayoutManager;
    private boolean hasMoreItems = true;
    private boolean isLoadingNext = false;
    private int pageIndex = 1;
    private String projectType;
    private Subscription subscribe ;
    private ManageListResponse manageListResponse;
    private RelativeLayout loadingFailLayout;
    private ImageView loadingFailImage;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal_manage, container, false);
        Bus.getDefault().register(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        pullToRefresh = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        frameLayout = (SlidingFrameLayout) view.findViewById(R.id.loading_layout);
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
        imageView = (ImageView) view.findViewById(R.id.loading_view);
        getAllData();
        myLayoutManager = new MyLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(myLayoutManager);
        manageAdapter = new ManageAdapter(this, getActivity());
        manageAdapter.setOnCheckedListener(new ManageAdapter.OnEditDoneListener() {
            @Override
            public void onEditDone(ManageListModel model) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ManageListModel", model);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        manageListResponse = DataCache.instance.getCacheData("haili", "IntervalsManageListResponse1");
        if (manageListResponse != null) {
            manageAdapter.setData(manageListResponse.data.manageListDetail);
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
                if (!isLoadingNext && hasMoreItems && lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    pageIndex++;
                    isLoadingNext = true;
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
//
//    @BusReceiver
//    public void StringEvent(String event){
//        switch (event){
//            case "reload_some_interface":
//                getAllData();
//                break;
//            default:
//                break;
//        }
//    }

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
        request.type = "1";
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

                if (TerminalManageFragment.this.manageListResponse != null) {
                    manageAdapter.clearData();
                    manageAdapter.setData(manageListResponse.data.manageListDetail);
                    manageAdapter.notifyDataSetChanged();
                } else {
                    manageAdapter.setData(manageListResponse.data.manageListDetail);
                    mRecyclerView.setAdapter(manageAdapter);
                    DataCache.instance.saveCacheData("haili", "IntervalsManageListResponse1", manageListResponse);
                }


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
                if (DataCache.instance.getCacheData("haili", "IntervalsManageListResponse1") != null) {
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
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }
}
