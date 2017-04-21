package com.haili.finance.user.fragment;

import android.graphics.Rect;
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
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.NewsNoticeModel;
import com.haili.finance.business.user.NewsNoticeRequest;
import com.haili.finance.business.user.NewsNoticeResponse;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.adapter.ActiveAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/3/1.
 */

public class ActiveFragment extends BaseFragment {

    private PullToRefreshView pullToRefreshView;
    private RecyclerView recyclerView;
    private Subscription subscrip;
    private MyLayoutManager myLayoutManager;
    private boolean isLoading = false;
    private boolean hasMoreItems = true;
    private int pageIndex = 1;
    private ActiveAdapter activeAdapter;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private RelativeLayout failLayout;
    private AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View root = inflater.inflate(R.layout.find_fragment, container, false);
        pullToRefreshView = (PullToRefreshView) root.findViewById(R.id.find_fragment_pull);
        recyclerView = (RecyclerView) root.findViewById(R.id.find_fragment_rv);
        myLayoutManager = new MyLayoutManager(getActivity());
        frameLayout = (SlidingFrameLayout) root.findViewById(R.id.loading_layout);
        failLayout = (RelativeLayout) root.findViewById(R.id.loading_fail_layout);
        root.findViewById(R.id.loading_fail_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failLayout.setVisibility(View.GONE);
                animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                getData();
            }
        });
        imageView = (ImageView) root.findViewById(R.id.loading_view);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildPosition(view) == 0) {
                    outRect.top = 20;
                }
            }
        });
        recyclerView.setLayoutManager(new MyLayoutManager(getActivity()));
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
                    activeAdapter.httpOK = true;
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
        initCacheData();
        getAllData();
        return root;
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("ActiveFragment")) {
            getData();
        }
    }

    private void initCacheData() {
        NewsNoticeResponse cacheData = DataCache.instance.getCacheData("haili", "NewsNoticeResponse1");
        if (cacheData == null) {
            return;
        }
        analyseData(cacheData, 0);
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

    private void getData() {
        NewsNoticeRequest request = new NewsNoticeRequest();
        request.type = "1";
        request.pageNum = "" + pageIndex;
        request.pageSize = "10";
        subscrip=    UserBusinessHelper.newsNoticeList(request)
                .subscribe(new Action1<NewsNoticeResponse>() {
                    @Override
                    public void call(NewsNoticeResponse newsNoticeResponse) {
                        if (animationDrawable != null) {
                            animationDrawable.stop();
                        }
                        frameLayout.setVisibility(View.GONE);

                        pullToRefreshView.setRefreshing(false);

                        if (newsNoticeResponse.dataModel != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "NewsNoticeResponse1", newsNoticeResponse);
                                analyseData(newsNoticeResponse, 2);
                            } else {
                                isLoading = false;
                                if (newsNoticeResponse.dataModel.size() > 0) {
                                    analyseData(newsNoticeResponse, 1);
                                } else {
                                    hasMoreItems = false;
                                    activeAdapter.httpOK = false;
                                    activeAdapter.notifyDataSetChanged();
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
                            activeAdapter.httpOK = false;
                            activeAdapter.notifyDataSetChanged();
                        }

                        pullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "NewsNoticeResponse1") != null) {
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

    private void getAllData() {
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getData();
    }

    /**
     * @param newsNoticeResponse 集合数据
     * @param type               添加数据的方式 0代表创建adapter并添加 1代表添加数据（add） 2代表刷新集合
     */
    public void analyseData(NewsNoticeResponse newsNoticeResponse, int type) {
        ArrayList<NewsNoticeModel> dataModel = newsNoticeResponse.dataModel;
        if (dataModel.size() > 0) {
            switch (type) {
                case 0:
                    if (recyclerView != null) {
                        activeAdapter = new ActiveAdapter(getActivity(), dataModel);
                        recyclerView.setAdapter(activeAdapter);
                    }
                    break;
                case 1:
                    if (activeAdapter != null) {
                        activeAdapter.addData(dataModel);
                    }
                    break;
                case 2:
                    if (recyclerView != null) {
                        if (activeAdapter == null) {
                            activeAdapter = new ActiveAdapter(getActivity(), dataModel);
                            recyclerView.setAdapter(activeAdapter);
                        } else {
                            activeAdapter.setData(dataModel);
                        }
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
