package com.haili.finance.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.Index.GetHomeBannerRequest;
import com.haili.finance.business.Index.GetHomeBannerResponse;
import com.haili.finance.business.Index.GetHomeIconRequest;
import com.haili.finance.business.Index.GetHomeIconResponse;
import com.haili.finance.business.Index.GetHomeProductRequest;
import com.haili.finance.business.Index.GetHomeProductResponse;
import com.haili.finance.business.Index.HomeBannerInfo;
import com.haili.finance.business.Index.HomeIcon;
import com.haili.finance.business.Index.HomeNoticeInfo;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.NewsNoticeModel;
import com.haili.finance.business.user.NewsNoticeRequest;
import com.haili.finance.business.user.NewsNoticeResponse;
import com.haili.finance.helper.IndexBusinessHelper;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.activity.ProductDetailActivity;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.FindActivity;
import com.haili.finance.user.activity.InviteFriendActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.utils.DefaultTransformer;
import com.haili.finance.utils.GlideImageLoader;
import com.haili.finance.widget.BaseScollTextView;
import com.haili.finance.widget.ScollTextView;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/7.
 * 首页Fragment
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Banner banner;
    private GridView gridView;
    private TextView tvRent, tvStartMoney, tvQiXi, tvName, tvDay, tvProductStyle;
    private ScollTextView scollTextView;
    private LinearLayout newsView, llProduct;
    private ImageView ivProductFail;
    private String productId;
    private Subscription subscrip, subscrip2, subscrip3, subscrip4;
    private boolean bannerOK, noticeOK, gridViewOK, produceOK;
    private ArrayList<HomeBannerInfo> images = new ArrayList<>();//icon的
    private ArrayList<HomeNoticeInfo> noticeList = new ArrayList<>();//公告的

    //D:\Program Files\AndroidStudio\jre
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        init();
        return root;
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "reload_some_interface":
                httpLoading();
                break;
            case "HomeFragment":
                httpLoading();
                break;
            case "LoginSuccess":
                initProduct();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bus.getDefault().unregister(this);
    }

    private void init() {
        addLoadingFragment(R.id.home_fragment_fl, "HomeFragment");
    }

    private void initView(View root) {
        newsView = (LinearLayout) root.findViewById(R.id.home_news);
        newsView.setOnClickListener(this);
        root.findViewById(R.id.layout_tip_tv).setOnClickListener(this);
        root.findViewById(R.id.iv_layout_tip).setOnClickListener(this);
        banner = (Banner) root.findViewById(R.id.banner);
        ivProductFail = (ImageView) root.findViewById(R.id.framelayout_home_product);
        llProduct = (LinearLayout) root.findViewById(R.id.ll_homefragment_product);
        gridView = (GridView) root.findViewById(R.id.grid_main);
        scollTextView = (ScollTextView) root.findViewById(R.id.scolltext_tip);
        scollTextView.setOnItemClickListener(new BaseScollTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                startActivity(new Intent(getActivity(), FindActivity.class).putExtra("type", "1"));
            }
        });
        Button btn_product = (Button) root.findViewById(R.id.btn_product);
        btn_product.setOnClickListener(this);
        tvRent = (TextView) root.findViewById(R.id.layout_home_product_rent);
        tvProductStyle = (TextView) root.findViewById(R.id.layout_home_product_style);
        tvStartMoney = (TextView) root.findViewById(R.id.layout_start_money);
        tvDay = (TextView) root.findViewById(R.id.layout_home_product_day);
        tvQiXi = (TextView) root.findViewById(R.id.layout_home_product_qixi);
        tvName = (TextView) root.findViewById(R.id.layout_home_product_name);
        initCacheData();
    }

    private void httpLoading() {
        initGridView();//GridView初始化
        initScollTextView();//公告初始化
        initProduct();//推荐产品初始化
        initBanner();//轮播图初始化
    }

    private void initCacheData() {
        GetHomeBannerResponse homeBannerResponse = DataCache.instance.getCacheData("haili", "GetHomeBannerResponse");
        GetHomeIconResponse homeIconResponse = DataCache.instance.getCacheData("haili", "GetHomeIconResponse");
        GetHomeProductResponse homeProductResponse = DataCache.instance.getCacheData("haili", "GetHomeProductResponse");
        if (homeBannerResponse != null) {
            images = homeBannerResponse.bannerList;
            startSetBanner();
        } else {
            for (int i = 0; i < 3; i++) {
                HomeBannerInfo homeIcon = new HomeBannerInfo();
                images.add(homeIcon);
            }
            startSetBanner();
        }
        if (homeIconResponse != null) {
            ArrayList<HomeIcon> iconList = homeIconResponse.iconList;
            setIconData(iconList);
        } else {//自己造假数据
            ArrayList<HomeIcon> list = new ArrayList<>();
            HomeIcon homeIcon1 = new HomeIcon();
            homeIcon1.name = "新手指引";
            HomeIcon homeIcon2 = new HomeIcon();
            homeIcon2.name = "平台活动";
            HomeIcon homeIcon3 = new HomeIcon();
            homeIcon3.name = "安全保障";
            HomeIcon homeIcon4 = new HomeIcon();
            homeIcon4.name = "我的邀请";
            list.add(homeIcon1);
            list.add(homeIcon2);
            list.add(homeIcon3);
            list.add(homeIcon4);
            setIconData(list);
        }
        if (homeProductResponse != null) {
            if (homeProductResponse.dataModel != null) {
                tvRent.setText(homeProductResponse.dataModel.yieldRate + "");
                String moneyText = homeProductResponse.dataModel.startInvestorBanalce + "元";
                tvStartMoney.setText(Html.fromHtml("<font color=red>" + moneyText + "</font>起投"));
                tvName.setText(homeProductResponse.dataModel.productName);
                tvQiXi.setText(Html.fromHtml("<font color=red>" + homeProductResponse.dataModel.carryInterest + "</font>起息"));
                tvDay.setText(Html.fromHtml("<font color=red>" + homeProductResponse.dataModel.limitTime + "天</font>锁定"));
                if ("0".equals(homeProductResponse.dataModel.type)) {
                    tvProductStyle.setVisibility(View.GONE);
                } else if ("1".equals(homeProductResponse.dataModel.type)) {
                    tvProductStyle.setVisibility(View.VISIBLE);
                    tvProductStyle.setText("新手");
                } else if ("2".equals(homeProductResponse.dataModel.type)) {
                    tvProductStyle.setVisibility(View.VISIBLE);
                    tvProductStyle.setText("理财");
                }
            }
        } else {
            ivProductFail.setVisibility(View.VISIBLE);
            llProduct.setVisibility(View.GONE);
            ivProductFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initProduct();
                }
            });
        }
    }

    private void initGridView() {
        //获取数据
        getData();
    }

    private void setIconData(ArrayList<HomeIcon> iconList) {
        HomeIconAdapter homeIconAdapter = new HomeIconAdapter(getActivity(), iconList);
        //配置适配器
        gridView.setAdapter(homeIconAdapter);
        gridView.setOnItemClickListener(this);
    }

    private void initBanner() {
        getBannerImageList();//网络请求获取banner信息
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GetHomeIconResponse cacheData = DataCache.instance.getCacheData("haili", "GetHomeIconResponse");
        if (cacheData == null)
            return;
        ArrayList<HomeIcon> iconList = cacheData.iconList;
        switch (position) {
            case 0://新手指引
                if (!TextUtils.isEmpty(iconList.get(0).refTarget)) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("params", iconList.get(0).refTarget);
                    intent.putExtra("title", iconList.get(0).name);
                    startActivity(intent);
                }
                break;
            case 1://平台活动
                if (!TextUtils.isEmpty(iconList.get(1).refTarget)) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("params", iconList.get(1).refTarget);
                    intent.putExtra("title", iconList.get(1).name);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), FindActivity.class));
                }
                break;
            case 2://安全保障
                if (!TextUtils.isEmpty(iconList.get(2).refTarget)) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("params", iconList.get(2).refTarget);
                    intent.putExtra("title", iconList.get(2).name);
                    startActivity(intent);
                }
                break;
            case 3://邀请有奖
                LoginResponse cacheData1 = DataCache.instance.getCacheData("haili", "LoginResponse");
                if (cacheData1 == null || cacheData1.loginModel == null || TextUtils.isEmpty(cacheData1.loginModel.accessToken)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    if (!TextUtils.isEmpty(iconList.get(3).refTarget)) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("params", iconList.get(3).refTarget);
                        intent.putExtra("title", iconList.get(3).name);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getActivity(), InviteFriendActivity.class));
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product:
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
            case R.id.home_news:
            case R.id.layout_tip_tv:
            case R.id.iv_layout_tip:
                startActivity(new Intent(getActivity(), FindActivity.class).putExtra("type", "1"));
                break;
        }
    }

    public void getBannerImageList() {//轮播图
        subscrip = IndexBusinessHelper.getHomeBanner(new GetHomeBannerRequest())
                .subscribe(new Action1<GetHomeBannerResponse>() {
                    @Override
                    public void call(GetHomeBannerResponse getHomeBannerResponse) {

                        if (getHomeBannerResponse.bannerList != null) {
                            DataCache.instance.saveCacheData("haili", "GetHomeBannerResponse", getHomeBannerResponse);
                            images = getHomeBannerResponse.bannerList;
                            startSetBanner();
                        }

                        bannerOK = true;
                        if (produceOK && gridViewOK && noticeOK) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            if (getActivity() != null) {
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                });

    }

    private void startSetBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        ArrayList<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            imgUrls.add(images.get(i).pic);
        }
        banner.setImages(imgUrls);
        //设置banner动画效果
        List<Class<? extends ViewPager.PageTransformer>> transformers = new ArrayList<>();
        transformers.add(DefaultTransformer.class);
        banner.setBannerAnimation(transformers.get(0));
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                //position标记是从1开始的
                HomeBannerInfo homeBannerInfo = images.get(position - 1);
                switch (homeBannerInfo.refType) {
                    case "1"://产品
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("productId", homeBannerInfo.productId + "");
                        startActivity(intent);
                        break;
                    case "2"://产品列表
//                        Intent intent2 = new Intent(getActivity(),null);
//
//                        startActivity(intent2);
                        break;
                    case "3"://活动页面
                        Intent intent1 = new Intent(getActivity(), WebActivity.class);
                        intent1.putExtra("params", homeBannerInfo.url);
                        intent1.putExtra("title", homeBannerInfo.name);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        //设置标题集合（当banner样式有显示title时）
        //List<String> titles=new ArrayList<>();
        //titles.add("1");
        //titles.add("2");
        //titles.add("3");
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initScollTextView() {//消息
        //判断   如果有数据newsView设置可见，scollTextView设置数据，如果没有数据，newsView设置不可见
        NewsNoticeRequest request = new NewsNoticeRequest();
        request.type = "3";
        request.pageNum = "1";
        request.pageSize = "10";
        subscrip4 = UserBusinessHelper.newsNoticeList(request)
                .subscribe(new Action1<NewsNoticeResponse>() {
                    @Override
                    public void call(NewsNoticeResponse newsNoticeResponse) {

                        if (newsNoticeResponse.dataModel != null) {
                            ArrayList<NewsNoticeModel> dataModel = newsNoticeResponse.dataModel;
                            noticeList.clear();
                            for (int i = 0; i < dataModel.size(); i++) {
                                HomeNoticeInfo homeNoticeInfo = new HomeNoticeInfo();
                                homeNoticeInfo.title = dataModel.get(i).title;
                                noticeList.add(homeNoticeInfo);
                            }
                            scollTextView.setData(noticeList);
                            newsView.setVisibility(View.VISIBLE);
                        } else {
                            newsView.setVisibility(View.GONE);
                        }

                        noticeOK = true;
                        if (produceOK && bannerOK && gridViewOK) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            if (getActivity() != null) {
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                });
    }

    public void getData() {//首页gridview
        //icon和iconName的长度是相同的，这里任选其一都可以
        subscrip2 = IndexBusinessHelper.getHomeIcon(new GetHomeIconRequest())
                .subscribe(new Action1<GetHomeIconResponse>() {
                    @Override
                    public void call(GetHomeIconResponse getHomeIconResponse) {

                        if (getHomeIconResponse.iconList != null) {
                            DataCache.instance.saveCacheData("haili", "GetHomeIconResponse", getHomeIconResponse);
                            ArrayList<HomeIcon> iconList = getHomeIconResponse.iconList;
                            setIconData(iconList);
                        }

                        gridViewOK = true;
                        if (produceOK && bannerOK && noticeOK) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            if (getActivity() != null) {
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                });
    }

    public void initProduct() {//推荐产品
        GetHomeProductRequest request = new GetHomeProductRequest();
        request.terminal = "app";
        subscrip3 = IndexBusinessHelper.getHomeProduct(request)
                .subscribe(new Action1<GetHomeProductResponse>() {
                    @Override
                    public void call(GetHomeProductResponse getHomeProductResponse) {

                        if (getHomeProductResponse.dataModel != null) {
                            llProduct.setVisibility(View.VISIBLE);
                            ivProductFail.setVisibility(View.GONE);
                            DataCache.instance.saveCacheData("haili", "GetHomeProductResponse", getHomeProductResponse);
                            tvName.setText(getHomeProductResponse.dataModel.productName);//标名称
                            DecimalFormat df = new DecimalFormat("0.00");
                            tvRent.setText(df.format((getHomeProductResponse.dataModel.yieldRate + getHomeProductResponse.dataModel.yieldRateAdd) * 100) + "%");//年化收益率
                            tvStartMoney.setText(Html.fromHtml("<font color=red>" + getHomeProductResponse.dataModel.startInvestorBanalce + "元</font>起投"));
                            tvQiXi.setText(Html.fromHtml("<font color=red>" + getHomeProductResponse.dataModel.carryInterest + "</font>起息"));
                            tvDay.setText(Html.fromHtml("<font color=red>" + getHomeProductResponse.dataModel.limitTime + "天</font>锁定"));
                            productId = getHomeProductResponse.dataModel.productId;
                            if ("0".equals(getHomeProductResponse.dataModel.type)) {
                                tvProductStyle.setVisibility(View.GONE);
                            } else if ("1".equals(getHomeProductResponse.dataModel.type)) {
                                tvProductStyle.setVisibility(View.VISIBLE);
                                tvProductStyle.setText("新客");
                            } else if ("2".equals(getHomeProductResponse.dataModel.type)) {
                                tvProductStyle.setVisibility(View.VISIBLE);
                                tvProductStyle.setText("理财");
                            }
                        }

                        produceOK = true;
                        if (bannerOK && gridViewOK && noticeOK) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            if (getActivity() != null) {
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                    }
                });
    }

    class HomeIconAdapter extends BaseAdapter {

        private ArrayList<HomeIcon> iconList;
        private Context context;

        HomeIconAdapter(Context context, ArrayList<HomeIcon> iconList) {
            this.context = context;
            this.iconList = iconList;
        }

        @Override
        public int getCount() {
            return iconList.size();
        }

        @Override
        public Object getItem(int position) {
            return iconList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item_home, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context.getApplicationContext())
                    .load(iconList.get(position).imgUrl)
                    .placeholder(R.mipmap.icon_loading)
                    .error(R.mipmap.icon_loading)
                    .crossFade()
                    .into(viewHolder.image);
            viewHolder.text.setText(iconList.get(position).name);
            return convertView;
        }

        class ViewHolder {

            private ImageView image;
            private TextView text;

            public ViewHolder(View view) {
                image = (ImageView) view.findViewById(R.id.image_icon);
                text = (TextView) view.findViewById(R.id.text_icon);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scollTextView != null) {
            scollTextView.stopScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip != null) {
            subscrip.unsubscribe();
        }
        if (subscrip2 != null) {
            subscrip2.unsubscribe();
        }
        if (subscrip3 != null) {
            subscrip3.unsubscribe();
        }
        if (subscrip4 != null) {
            subscrip4.unsubscribe();
        }
    }
}
