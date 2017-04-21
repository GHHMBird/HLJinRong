package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.MessageDetailRequest;
import com.haili.finance.business.user.MessageDetailResponse;
import com.haili.finance.business.user.NewsNoticeDetailRequest;
import com.haili.finance.business.user.NewsNoticeDetailResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/3/13.
 */

public class MessageDetailActivity extends BaseActivity {
private Subscription subscrip,subscrip2;
    private TextView tvBody, tvTime, tvTitle;
    private String id;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("消息详情");
        initView();
        init();
    }

    private void init() {
        if (mode == -1) {
            finish();
        } else if (mode == 1) {//发现页面跳进来的
            if (!TextUtils.isEmpty(id)) {
                addLoadingFragment(R.id.avtivity_message_detail_fl, "MessageDetailActivity_find");
            }
        } else if (mode == 2) {//消息列表跳进来的
            if (!TextUtils.isEmpty(id)) {
                addLoadingFragment(R.id.avtivity_message_detail_fl, "MessageDetailActivity_message");
            }
        }
    }

    private void initView() {
        tvBody = (TextView) findViewById(R.id.message_detail_body);
        tvTime = (TextView) findViewById(R.id.message_detail_time);
        tvTitle = (TextView) findViewById(R.id.message_detail_title);
        initCacheData();
    }

    private void initCacheData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);
        id = intent.getStringExtra("id");
        if (mode == 1) {
            NewsNoticeDetailResponse cacheData = DataCache.instance.getCacheData("haili", "MessageDetailActivity_find" + mode + id);
            if (cacheData != null) {
                tvBody.setText(cacheData.dataModel.content);
                tvTime.setText(cacheData.dataModel.publishTime);
                tvTitle.setText(cacheData.dataModel.title);
            }
        } else if (mode == 2) {
            MessageDetailResponse cacheData = DataCache.instance.getCacheData("haili", "MessageDetailActivity_message" + mode + id);
            if (cacheData != null) {
                tvBody.setText(cacheData.dataModel.content);
                tvTime.setText(cacheData.dataModel.time);
                tvTitle.setText(cacheData.dataModel.title);
            }
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("MessageDetailActivity_message")) {
            getMessageData();
        } else if (event.equals("MessageDetailActivity_find")) {
            getFindData();
        }
    }


    public void getMessageData() {
        MessageDetailRequest request = new MessageDetailRequest();
        request.msgId = id;
        subscrip2=  UserBusinessHelper.getMessageDetail(request)
                .subscribe(new Action1<MessageDetailResponse>() {
                    @Override
                    public void call(MessageDetailResponse messageDetailResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (messageDetailResponse.dataModel != null) {
                            DataCache.instance.saveCacheData("haili", "MessageDetailActivity_message" + mode + id, messageDetailResponse);
                            tvBody.setText(messageDetailResponse.dataModel.content);
                            tvTime.setText(messageDetailResponse.dataModel.time);
                            tvTitle.setText(messageDetailResponse.dataModel.title);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (DataCache.instance.getCacheData("haili", "MessageDetailActivity_message" + mode + id) != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(MessageDetailActivity.this, requestErrorThrowable.getMessage());
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

    public void getFindData() {
        NewsNoticeDetailRequest request = new NewsNoticeDetailRequest();
        request.newsId = id;
        subscrip=  UserBusinessHelper.newsNoticeDetail(request)
                .subscribe(new Action1<NewsNoticeDetailResponse>() {
                    @Override
                    public void call(NewsNoticeDetailResponse newsNoticeDetailResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (newsNoticeDetailResponse.dataModel != null) {
                            DataCache.instance.saveCacheData("haili", "MessageDetailActivity_find" + mode + id, newsNoticeDetailResponse);
                            tvBody.setText(newsNoticeDetailResponse.dataModel.content);
                            tvTime.setText(newsNoticeDetailResponse.dataModel.publishTime);
                            tvTitle.setText(newsNoticeDetailResponse.dataModel.title);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (DataCache.instance.getCacheData("haili", "MessageDetailActivity_find" + mode + id) != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(MessageDetailActivity.this, requestErrorThrowable.getMessage());
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
