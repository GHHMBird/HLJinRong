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
import com.haili.finance.user.adapter.NoticeAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;
import com.mcxiaoke.bus.Bus;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Monkey on 2017/3/1.
 */

public class NoticeFragment extends BaseFragment {
private Subscription subscrip;
    private PullToRefreshView pullToRefreshView;
    private MyLayoutManager myLayoutManager;
    private boolean isLoading = false, hasMoreItems = true;
    private int pageIndex = 1;
    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    private RelativeLayout failLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View root = View.inflate(getActivity(), R.layout.find_fragment, null);
        pullToRefreshView = (PullToRefreshView) root.findViewById(R.id.find_fragment_pull);
        recyclerView = (RecyclerView) root.findViewById(R.id.find_fragment_rv);
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
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
                if (parent.getChildPosition(view) == 0) {
                    outRect.top = 0;
                }
            }
        });
        myLayoutManager = new MyLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
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
                    noticeAdapter.httpOK = true;
                    getData();
                }
            }
        });
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hasMoreItems = true;
                pageIndex = 1;
                getData();
            }
        });
        initCacheData();
        getAllData();
        return root;
    }

    private void getAllData() {
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getData();
    }

    private void initCacheData() {
        NewsNoticeResponse cacheData = DataCache.instance.getCacheData("haili", "NewsNoticeResponse3");
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

    public void getData() {
        NewsNoticeRequest request = new NewsNoticeRequest();
        request.type = "3";
        request.pageNum = "" + pageIndex;
        request.pageSize = "10";
        subscrip=  UserBusinessHelper.newsNoticeList(request)
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
                                DataCache.instance.saveCacheData("haili", "NewsNoticeResponse3", newsNoticeResponse);
                                analyseData(newsNoticeResponse, 2);
                            } else {
                                isLoading = false;
                                if (newsNoticeResponse.dataModel.size() > 0) {
                                    analyseData(newsNoticeResponse, 1);
                                } else {
                                    hasMoreItems = false;
                                    noticeAdapter.httpOK = false;
                                    noticeAdapter.notifyDataSetChanged();
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
                            noticeAdapter.httpOK = false;
                            noticeAdapter.notifyDataSetChanged();
                        }

                        pullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "NewsNoticeResponse3") != null) {
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

    /**
     * @param newsNoticeResponse 集合数据
     * @param type               添加数据的方式 0代表创建adapter并添加 1代表添加数据（add） 2代表刷新集合
     */
    public void analyseData(NewsNoticeResponse newsNoticeResponse, int type) {
        ArrayList<NewsNoticeModel> dataModel = newsNoticeResponse.dataModel;
        if (dataModel.size() > 0) {
            switch (type) {
                case 0:
                    noticeAdapter = new NoticeAdapter(getActivity(), dataModel);
                    recyclerView.setAdapter(noticeAdapter);
                    break;
                case 1:
                    noticeAdapter.addData(dataModel);
                    break;
                case 2:
                    if (noticeAdapter == null) {
                        noticeAdapter = new NoticeAdapter(getActivity(), dataModel);
                        recyclerView.setAdapter(noticeAdapter);
                    } else {
                        noticeAdapter.setData(dataModel);
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
