package com.haili.finance;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.haili.finance.base.BaseActivity;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.stroage.DataCache;

/*
 * Created by Monkey on 2017/3/1.
 */

public class WebActivity extends BaseActivity {

    private static String cookies = "";
    private WebView wv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        String title = getIntent().getStringExtra("title");
        setBarTitle(title);
        initView();
        getUrl();
    }


    private void initView() {
        wv = (WebView) findViewById(R.id.webview);
    }

    private void getUrl() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("params");
        WebSettings webSettings = wv.getSettings();
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //不使用cache 全部从网络上获取
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //无模式选择，通过setAppCacheEnabled(boolean flag)设置是否打开。默认关闭，即，H5的缓存无法使用。
        webSettings.setAppCacheEnabled(true);
        //设置支持HTML5本地存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        DeviceInfoModel model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
        if (model != null) {
            if (!model.accessToken.equals("")) {
                cookies = "accessToken=" + model.accessToken;
                synCookies(WebActivity.this, url);
            }
        }
        wv.loadUrl(url);
        //设置Web视图
        wv.setWebViewClient(new webViewClient());
        wv.setWebChromeClient(new webChromeClient());
    }
    private class webViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return true;
        }
    }

    //Web视图
    private class webChromeClient extends WebChromeClient {

    }

    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie

        CookieSyncManager.getInstance().sync();

        //hhm add
//        if (Build.VERSION.SDK_INT < 21) {
//            CookieSyncManager.getInstance().sync();
//        } else {
//            CookieManager.getInstance().flush();
//        }

        String newCook = cookieManager.getCookie(url);
    }

}
