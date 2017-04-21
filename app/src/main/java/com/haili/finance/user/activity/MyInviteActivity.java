package com.haili.finance.user.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.MyInviteFriendInfo;
import com.haili.finance.business.user.MyInviteFriendRequest;
import com.haili.finance.business.user.MyInviteFriendResponse;
import com.haili.finance.business.user.MyInviteMoneyModel;
import com.haili.finance.business.user.MyInviteMoneyRequest;
import com.haili.finance.business.user.MyInviteMoneyResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.adapter.MyInviteAdapter;
import com.haili.finance.user.adapter.MyInviteMoneyAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

public class MyInviteActivity extends BaseActivity {
private Subscription subscrip,subscrip2;
    private RecyclerView recyclerView;
    private PullToRefreshView mPullToRefreshView;
    private MyInviteAdapter myInviteAdapter;
    private MyInviteMoneyAdapter myInviteMoneyAdapter;
    private TextView textOne, textTwo, textThree;
    private int type, pageIndex = 1;
    private boolean isLoading = false, hasMoreItems = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_my_invite);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        type = getIntent().getIntExtra("type", 2);//        我的邀请/我的奖励
        if (type == 2) {
            finish();
        } else if (type == 0) {
            setBarTitle("我的邀请");
        } else if (type == 1) {
            setBarTitle("我的奖励");
        }
        initView();
        init();
    }

    private void init() {
        if (type == 0) {//我的邀请
            addLoadingFragment(R.id.content_my_invite_fl, "MyInviteFriend");
        } else if (type == 1) {//邀请奖励
            addLoadingFragment(R.id.content_my_invite_fl, "MyInviteMoney");
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("MyInviteFriend")) {//我的邀请
            getMyInviteInfo();
        } else if (event.equals("MyInviteMoney")) {//我的奖励
            getMyMoneyInfo();
        }
    }

    private void initView() {
        textOne = (TextView) findViewById(R.id.content_my_invite_one);
        textTwo = (TextView) findViewById(R.id.content_my_invite_two);
        textThree = (TextView) findViewById(R.id.content_my_invite_three);
        if (type == 0) {
            textOne.setText("好友手机号");
            textTwo.setText("累计投资额(元)");
            textThree.setText("注册时间");
        } else if (type == 1) {
            textOne.setText("获得时间");
            textTwo.setText("奖励金额(元)");
            textThree.setText("奖励类型");
        }
        recyclerView = (RecyclerView) findViewById(com.haili.finance.R.id.my_invite_rv);
        mPullToRefreshView = (PullToRefreshView) findViewById(com.haili.finance.R.id.my_invite_pull);
        final MyLayoutManager myLayoutManager = new MyLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        RecyclerView.ItemDecoration decor = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 2;
            }
        };
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
                    if (type == 0) {
                        myInviteAdapter.httpOK = true;
                        getMyInviteInfo();
                    } else if (type == 1) {
                        myInviteMoneyAdapter.httpOK = true;
                        getMyMoneyInfo();
                    }
                }
            }
        });
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                hasMoreItems = true;
                if (type == 0) {
                    getMyInviteInfo();
                } else if (type == 1) {
                    getMyMoneyInfo();
                }
            }
        });
        recyclerView.addItemDecoration(decor);
        initCacheData();
    }

    private void initCacheData() {
        if (type == 0) {
            MyInviteFriendResponse cacheData = DataCache.instance.getCacheData("haili", "MyInviteFriendResponse");
            if (cacheData == null) {
                return;
            }
            analyseMyInviteData(cacheData, 0);
        } else if (type == 1) {
            MyInviteMoneyResponse cacheData = DataCache.instance.getCacheData("haili", "MyInviteMoneyResponse");
            if (cacheData == null) {
                return;
            }
            analyseMyInviteMoneyData(cacheData, 0);
        }
    }

    private void getMyInviteInfo() {
        MyInviteFriendRequest myInviteFriendRequest = new MyInviteFriendRequest();
        myInviteFriendRequest.pageNum = "" + pageIndex;
        myInviteFriendRequest.pageSize = "10";
        subscrip=   UserBusinessHelper.myInviteFriend(myInviteFriendRequest)
                .subscribe(new Action1<MyInviteFriendResponse>() {
                    @Override
                    public void call(MyInviteFriendResponse myInviteFriendResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        mPullToRefreshView.setRefreshing(false);

                        if (myInviteFriendResponse.dataModel != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "MyInviteFriendResponse", myInviteFriendResponse);
                                analyseMyInviteData(myInviteFriendResponse, 2);
                            } else {
                                isLoading = false;
                                if (myInviteFriendResponse.dataModel.dataModel != null && myInviteFriendResponse.dataModel.dataModel.size() > 0) {
                                    analyseMyInviteData(myInviteFriendResponse, 1);
                                } else {
                                    hasMoreItems = false;
                                    myInviteAdapter.httpOK = false;
                                    myInviteAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (isLoading && pageIndex != 1) {
                            pageIndex--;
                            isLoading = false;
                            myInviteAdapter.httpOK = false;
                            myInviteAdapter.notifyDataSetChanged();
                        }

                        mPullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "MyInviteFriendResponse") != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }

                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(MyInviteActivity.this, requestErrorThrowable.getMessage());
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

    private void analyseMyInviteData(MyInviteFriendResponse myInviteFriendResponse, int httptype) {
        ArrayList<MyInviteFriendInfo> dataModel = myInviteFriendResponse.dataModel.dataModel;
        if (dataModel.size() > 0) {
            switch (httptype) {
                case 0:
                    myInviteAdapter = new MyInviteAdapter(this, myInviteFriendResponse.dataModel.dataModel);
                    recyclerView.setAdapter(myInviteAdapter);
                    break;
                case 1:
                    myInviteAdapter.addData(dataModel);
                    break;
                case 2:
                    if (myInviteAdapter == null) {
                        myInviteAdapter = new MyInviteAdapter(MyInviteActivity.this, dataModel);
                        recyclerView.setAdapter(myInviteAdapter);
                    } else {
                        myInviteAdapter.setData(dataModel);
                    }
                    break;
            }
        }
    }

    private void getMyMoneyInfo() {
        MyInviteMoneyRequest myInviteMoneyRequest = new MyInviteMoneyRequest();
        myInviteMoneyRequest.pageNum = "" + pageIndex;
        myInviteMoneyRequest.pageSize = "10";
        subscrip2=    UserBusinessHelper.myInviteMoney(myInviteMoneyRequest)
                .subscribe(new Action1<MyInviteMoneyResponse>() {
                    @Override
                    public void call(MyInviteMoneyResponse myInviteMoneyResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        mPullToRefreshView.setRefreshing(false);
                        isLoading = false;

                        if (myInviteMoneyResponse.dataModel != null) {
                            if (pageIndex == 1) {
                                DataCache.instance.saveCacheData("haili", "MyInviteMoneyResponse", myInviteMoneyResponse);
                                analyseMyInviteMoneyData(myInviteMoneyResponse, 2);
                            } else {
                                analyseMyInviteMoneyData(myInviteMoneyResponse, 1);
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (isLoading && pageIndex != 1) {
                            pageIndex--;
                            isLoading = false;
                            myInviteMoneyAdapter.httpOK = false;
                            myInviteMoneyAdapter.notifyDataSetChanged();
                        }

                        mPullToRefreshView.setRefreshing(false);

                        if (DataCache.instance.getCacheData("haili", "MyInviteMoneyResponse") != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(MyInviteActivity.this, requestErrorThrowable.getMessage());
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

    private void analyseMyInviteMoneyData(MyInviteMoneyResponse myInviteMoneyResponse, int httpType) {
        ArrayList<MyInviteMoneyModel> dataModel = myInviteMoneyResponse.dataModel.dataModel;
        if (dataModel.size() > 0) {
            switch (httpType) {
                case 0:
                    myInviteMoneyAdapter = new MyInviteMoneyAdapter(this, myInviteMoneyResponse.dataModel.dataModel);
                    recyclerView.setAdapter(myInviteMoneyAdapter);
                    break;
                case 1:
                    myInviteMoneyAdapter.addData(dataModel);
                    break;
                case 2:
                    if (myInviteMoneyAdapter == null) {
                        myInviteMoneyAdapter = new MyInviteMoneyAdapter(MyInviteActivity.this, dataModel);
                        recyclerView.setAdapter(myInviteMoneyAdapter);
                    } else {
                        myInviteMoneyAdapter.setData(dataModel);
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
        }  if (subscrip2!=null) {
            subscrip2.unsubscribe();
        }
    }
}
