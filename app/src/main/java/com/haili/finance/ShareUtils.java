package com.haili.finance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.haili.finance.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/*
 * Created by Monkey on 2017/3/8.
 */

public class ShareUtils {
    public static UMShareListener umShareListener = new UMShareListener();

    public static void share(Context context,String title,UMImage thumb,String shareUrl,String body) {
        BaseActivity activity = (BaseActivity) context;

        ShareBoardConfig config = new ShareBoardConfig();//设置分享面板
        config.setIndicatorVisibility(false);//指示器（小圆圈）是否可见
        config.setTitleText("分享到");//分享面板 标题文字
        config.setCancelButtonText("取消");//取消按钮文字
        config.setCancelButtonBackground(10);//取消按钮背景颜色
        config.setMenuItemBackgroundColor(10);//item背景颜色
//                config.setShareboardBackgroundColor(10);//分享面板背景颜色
//                config.setTitleTextColor(10);//分享面板标题背景颜色
//        UMImage umImage = new UMImage(activity, "http://qlogo4.store.qq.com/qzone/245760047/245760047/100?1476078555");
//        umImage.setThumb(umImage);
        UMWeb web = initShare(title, thumb, shareUrl,body);

        new ShareAction(activity)
                .setDisplayList(
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE)
                .withMedia(web)
                .setCallback(umShareListener)
                .open(config);
    }

    @NonNull
    private static UMWeb initShare(String title, UMImage thumb, String shareUrl,String body) {

        UMWeb web = new UMWeb(shareUrl/*"https://user.qzone.qq.com/245760047/"*/);//设置分享url
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(body);//描述
        return web;
    }
}