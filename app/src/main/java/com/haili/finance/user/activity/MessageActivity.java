package com.haili.finance.user.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.MessageBean;
import com.haili.finance.business.user.MessageListRequest;
import com.haili.finance.business.user.MessageListResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.adapter.MessageAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

public class MessageActivity extends BaseActivity {
private Subscription subscrip;
    private PullToRefreshView pullToRefreshView;
    private RecyclerView recyclerView;
    private boolean isLoad = false, hasMoreItems = true;
    private int pageIndex = 1;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("消息");
        initView();
        init();
    }

    private void init() {
        addLoadingFragment(R.id.activity_message_fl, "MessageActivity");
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("MessageActivity")) {
            getData();
        }
    }

    private void initView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_message_pull);
        recyclerView = (RecyclerView) findViewById(R.id.activity_message_rv);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
            }
        });
        final MyLayoutManager myLayoutManager = new MyLayoutManager(this);
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
                    messageAdapter.httpOK = true;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageAdapter != null) {
            if (messageAdapter.arrayList.size() > 0) {
                for (int i = 0; i < messageAdapter.arrayList.size(); i++) {
                    if ("1".equals(messageAdapter.arrayList.get(i).readState)) {
                        setResult(12300012);
                        return;
                    }
                }
            }
        }
    }

    private void initCacheData() {
        MessageListResponse cacheData = DataCache.instance.getCacheData("haili", "MessageListResponse");
        if (cacheData == null) {
            return;
        }
        analyseData(cacheData, 0);
    }

    public void getData() {
        MessageListRequest request = new MessageListRequest();
        request.pageNum = "" + pageIndex;
        request.pageSize = "10";
        subscrip=   UserBusinessHelper.getMessageList(request)
                .subscribe(new Action1<MessageListResponse>() {
                    @Override
                    public void call(MessageListResponse messageListResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        pullToRefreshView.setRefreshing(false);

                        if (messageListResponse.msgList != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "MessageListResponse", messageListResponse);
                                analyseData(messageListResponse, 2);
                            } else {
                                isLoad = false;
                                if (messageListResponse.msgList.size() > 0) {
                                    analyseData(messageListResponse, 1);
                                } else {//没有更多数据了
                                    hasMoreItems = false;
                                    messageAdapter.httpOK = false;
                                    messageAdapter.notifyDataSetChanged();
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
                            messageAdapter.httpOK = false;
                            messageAdapter.notifyDataSetChanged();
                        }

                        pullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "MessageListResponse") != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(MessageActivity.this, requestErrorThrowable.getMessage());
                            }
                        } else {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                loadingFragment.showLoadingFailView();
                            }
                        }

                    }
                });
    }

    /**
     * @param response 集合数据
     * @param type     添加数据的方式 0代表创建adapter并添加 1代表添加数据（add） 2代表刷新集合
     */
    public void analyseData(MessageListResponse response, int type) {
        ArrayList<MessageBean> msgList = response.msgList;
        if (msgList.size() > 0) {
            switch (type) {
                case 0:
                    messageAdapter = new MessageAdapter(this, msgList);
                    recyclerView.setAdapter(messageAdapter);
                    break;
                case 1:
                    messageAdapter.addData(msgList);
                    break;
                case 2:
                    if (messageAdapter == null) {
                        messageAdapter = new MessageAdapter(this, msgList);
                        recyclerView.setAdapter(messageAdapter);
                    } else {
                        messageAdapter.setData(msgList);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
