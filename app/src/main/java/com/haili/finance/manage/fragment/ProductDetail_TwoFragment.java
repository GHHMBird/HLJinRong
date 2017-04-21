package com.haili.finance.manage.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.base.BaseFragment;

/*
 * Created by Monkey on 2017/1/14.
 */

@SuppressLint("ValidFragment")
public class ProductDetail_TwoFragment extends BaseFragment {
    private WebView webView;
    private PullToRefreshView returnTop;
    private OnRefreshViewShow onRefreshViewShow;
    private String url;

    public ProductDetail_TwoFragment(String url,String id){
        this.url = url+ "?productId="+ id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_two, container, false);
        initView(root);
        init();
        return root;
    }

    private void init() {
        initData();
    }

    private void initData() {
        returnTop.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshViewShow != null){
                    onRefreshViewShow.onViewShow();
                }
                returnTop.setRefreshing(false);
            }
        });
        getUrl();
    }

    private void initView(View root) {
        webView = (WebView)root.findViewById(R.id.webView);
        returnTop = (PullToRefreshView)root.findViewById(R.id.return_top);

    }

    private void getUrl(){
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl(url);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
    }

    //Web视图
    private class webViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void setOnRefreshViewShow(OnRefreshViewShow onRefreshViewShow){
        this.onRefreshViewShow = onRefreshViewShow;
    }

    public interface OnRefreshViewShow{
        void onViewShow();
    }
}
