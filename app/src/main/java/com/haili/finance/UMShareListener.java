package com.haili.finance;

import android.util.Log;

import com.haili.finance.base.BaseApplication;
import com.haili.finance.helper.ViewHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

/*
 * Created by Monkey on 2017/3/8.
 */

public class UMShareListener implements com.umeng.socialize.UMShareListener{//分享的内部类  用于监听分享的回调

    @Override
    public void onStart(SHARE_MEDIA platform) {
        //分享开始的回调
    }

    @Override
    public void onResult(SHARE_MEDIA platform) {
        if (SHARE_MEDIA.WEIXIN.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"微信分享成功");
        }else if (SHARE_MEDIA.WEIXIN_CIRCLE.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"朋友圈分享成功");
        } else if (SHARE_MEDIA.QQ.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"QQ分享成功");
        } else if (SHARE_MEDIA.QZONE.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"QQ空间分享成功");
        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
        if (t != null) {
            Log.d("throw", "throw:" + t.getMessage());
            if (t.getMessage().contains("错误信息：没有安装应用")) {
                if (SHARE_MEDIA.WEIXIN.equals(platform) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(platform)) {
                    ViewHelper.showToast(BaseApplication.getApplication()," 没有安装微信");
                } else if (SHARE_MEDIA.QQ.equals(platform) || SHARE_MEDIA.QZONE.equals(platform)) {
                    ViewHelper.showToast(BaseApplication.getApplication()," 没有安装QQ");
                } else {
                    ViewHelper.showToast(BaseApplication.getApplication(),"请先安装应用程序！");
                }
            } else {
                ViewHelper.showToast(BaseApplication.getApplication(),platform + " 分享失败");
            }
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
        if (SHARE_MEDIA.WEIXIN.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"微信分享取消了");
        } else if (SHARE_MEDIA.SINA.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"新浪分享取消了");
        } else if (SHARE_MEDIA.WEIXIN_CIRCLE.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"朋友圈分享取消了");
        } else if (SHARE_MEDIA.QQ.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"QQ分享取消了");
        } else if (SHARE_MEDIA.QZONE.equals(platform)) {
            ViewHelper.showToast(BaseApplication.getApplication(),"QQ空间分享取消了");
        }
    }
}

