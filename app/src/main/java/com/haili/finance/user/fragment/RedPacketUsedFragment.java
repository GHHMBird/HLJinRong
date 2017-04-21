package com.haili.finance.user.fragment;

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
import com.haili.finance.MainActivity;
import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.GetRedPackageModel;
import com.haili.finance.business.user.GetRedPackageRequest;
import com.haili.finance.business.user.GetRedPackageResponse;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.adapter.RedPacketUsedAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;
import com.mcxiaoke.bus.Bus;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by lfu on 2017/2/23.
 */

public class RedPacketUsedFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MyLayoutManager myLayoutManager;
    private RedPacketUsedAdapter adapter;
    private PullToRefreshView mPullToRefreshView;
    private ArrayList<GetRedPackageModel> redPackageArrayList;
    private boolean isLoading = false;
    private Subscription subscrip;
    private boolean hasMoreItems = true;
    private int pageIndex = 1;
    private RelativeLayout failLayout;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView, failLoading;
    private AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_red_packet, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.packet_list);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        failLayout = (RelativeLayout) view.findViewById(R.id.loading_fail_layout);
        failLoading = (ImageView) view.findViewById(R.id.loading_fail_image);
        frameLayout = (SlidingFrameLayout) view.findViewById(R.id.loading_layout);
        imageView = (ImageView) view.findViewById(R.id.loading_view);
        myLayoutManager = new MyLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
        failLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failLayout.setVisibility(View.GONE);
                animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                getRedPackage();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    adapter.httpOK = true;
                    getRedPackage();
                }
            }
        });
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hasMoreItems = true;
                pageIndex = 1;
                getRedPackage();
            }
        });
        initCacheData();
        getAllData();
        return view;
    }

    private void getAllData() {
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getRedPackage();
    }

    private void initCacheData() {
        GetRedPackageResponse cacheData = DataCache.instance.getCacheData("haili", "GetRedPackageResponse2");
        if (cacheData == null) {
            return;
        }
        setAdapter(cacheData, 0);
    }

    private void getRedPackage() {
        GetRedPackageRequest request = new GetRedPackageRequest();
        request.state = "2";
        request.pageNum = "" + pageIndex;
        request.pageSize = "10";
        subscrip=   UserBusinessHelper.getRedPackage(request)
                .subscribe(new Action1<GetRedPackageResponse>() {
                    @Override
                    public void call(GetRedPackageResponse getRedPackageResponse) {
                        if (animationDrawable != null) {
                            animationDrawable.stop();
                        }
                        frameLayout.setVisibility(View.GONE);

                        mPullToRefreshView.setRefreshing(false);

                        if (getRedPackageResponse.dataModel != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "GetRedPackageResponse2", getRedPackageResponse);
                                setAdapter(getRedPackageResponse, 2);
                            } else {
                                isLoading = false;
                                if (getRedPackageResponse.dataModel.size() > 0) {
                                    setAdapter(getRedPackageResponse, 1);
                                } else {
                                    hasMoreItems = false;
                                    adapter.httpOK = false;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (animationDrawable != null) {
                            animationDrawable.stop();
                        }

                        if (isLoading && pageIndex != 1) {
                            pageIndex--;
                            isLoading = false;
                            adapter.httpOK = false;
                            adapter.notifyDataSetChanged();
                        }

                        mPullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "GetRedPackageResponse2") != null) {
                            frameLayout.setVisibility(View.GONE);
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                if (getActivity() != null) {
                                    ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                                }
                            }
                        } else {
                            failLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void setAdapter(GetRedPackageResponse getRedPackageResponse, int type) {
        ArrayList<GetRedPackageModel> dataModel = getRedPackageResponse.dataModel;
        if (dataModel != null && dataModel.size() > 0) {
            switch (type) {
                case 0:
                    adapter = new RedPacketUsedAdapter(RedPacketUsedFragment.this, getActivity(), dataModel);
                    recyclerView.setAdapter(adapter);
                    break;
                case 1:
                    adapter.addData(dataModel);
                    break;
                case 2:
                    if (adapter == null) {
                        adapter = new RedPacketUsedAdapter(RedPacketUsedFragment.this, getActivity(), dataModel);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setData(dataModel);
                    }
                    break;
            }
            adapter.setOnClickDoneListener(new RedPacketUsedAdapter.OnClickDoneListener() {
                @Override
                public void onEditDone() {
                    Bus.getDefault().post("RedPacketFragment");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Bus.getDefault().unregister(this);
        super.onDestroyView();
        failLayout = null;
        frameLayout=null;
        imageView=null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
