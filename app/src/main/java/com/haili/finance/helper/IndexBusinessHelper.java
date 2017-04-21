package com.haili.finance.helper;

import com.haili.finance.business.Index.GetHomeBannerRequest;
import com.haili.finance.business.Index.GetHomeBannerResponse;
import com.haili.finance.business.Index.GetHomeIconRequest;
import com.haili.finance.business.Index.GetHomeIconResponse;
import com.haili.finance.business.Index.GetHomeNoticeRequest;
import com.haili.finance.business.Index.GetHomeNoticeResponse;
import com.haili.finance.business.Index.GetHomeProductRequest;
import com.haili.finance.business.Index.GetHomeProductResponse;
import com.haili.finance.business.Index.SplashImageRequest;
import com.haili.finance.business.Index.SplashImageResponse;
import com.haili.finance.http.InterfaceAPI;

import rx.Observable;

/*
 * Created by fuliang on 2017/3/9.
 */

public class IndexBusinessHelper {

    /*
     * 获取启动页ICON
     */
    public static Observable<SplashImageResponse> getInviteInfo(SplashImageRequest request) {
        return new InterfaceAPI().getSplashImage(request);
    }

    //获取首页banner
    public static Observable<GetHomeBannerResponse> getHomeBanner(GetHomeBannerRequest request) {
        return new InterfaceAPI().getHomeBanner(request);
    }

    //获取首页notice
    public static Observable<GetHomeNoticeResponse> getHomeNotice(GetHomeNoticeRequest request) {
        return new InterfaceAPI().getHomeNotice(request);
    }

    //首页推荐产品
    public static Observable<GetHomeProductResponse> getHomeProduct(GetHomeProductRequest request) {
        return new InterfaceAPI().getHomeProduct(request);
    }

    //获取首页icon
    public static Observable<GetHomeIconResponse> getHomeIcon(GetHomeIconRequest request) {
        return new InterfaceAPI().getHomeIcon(request);
    }}
