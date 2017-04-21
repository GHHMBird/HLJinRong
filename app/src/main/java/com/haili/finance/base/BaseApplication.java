package com.haili.finance.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.haili.finance.stroage.DataCache;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;


/*
 * Created by Monkey on 2016/12/22.
 */

public class BaseApplication extends Application {
    {
        {
            PlatformConfig.setWeixin("wx4b9eb4d637a7582d", "b48b88e7ff465c5145d3eb5f7cca2f33");
            //豆瓣RENREN平台目前只能在服务器端配置
            PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
//            Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
            PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
            PlatformConfig.setQQZone("1105682575", "GTBbMXMaRxzYn3xp");
            PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
            PlatformConfig.setAlipay("2015111700822536");
            PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
            PlatformConfig.setPinterest("1439206");
            PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
            PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
            PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J");
            PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a");

        }
    }

    public static BaseApplication instance;
    public Context applicationContext;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        init();
        initImageLoader(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Config.DEBUG = true;
        refWatcher = LeakCanary.install(this);
        //友盟统计
        //当应用在后台运行超过30秒（默认）再回到前端，
        // 将被认为是两个独立的session(启动)，例如用户回到home，
        // 或进入其他程序，经过一段时间后再返回之前的应用。可通过接口：
        MobclickAgent.setSessionContinueMillis(1000);
        NBSAppAgent.setLicenseKey("2c556b63aa334f1a895bccfcdca9eba1").withLocationServiceEnabled(true).startInApplication(this.getApplicationContext());
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    private void init() {
        applicationContext = this;
        instance = this;
        DataCache.instance.init(BaseApplication.this);
        DataCache.instance.clearCacheData("haili", "BankListResponse");
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPoolSize(3);
        config.memoryCacheExtraOptions(480, 640);// 限制缓存图片大小为640*480
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheExtraOptions(480, 640, null);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static BaseApplication getApplication() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}