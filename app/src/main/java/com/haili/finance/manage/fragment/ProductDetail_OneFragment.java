package com.haili.finance.manage.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.manage.ProductDescribeModel;

/*
 * Created by Monkey on 2017/1/14.
 */

@SuppressLint("ValidFragment")
public class ProductDetail_OneFragment extends BaseFragment{
    private TextView tv_product_content;
    private PullToRefreshView returnTop;
    private OnRefreshViewShow onRefreshViewShow;
    private ProductDescribeModel model;
    private LinearLayout financing_info_layout,check_info_layout,prove_list_layout;

    public ProductDetail_OneFragment(ProductDescribeModel productDescribe){
        model = productDescribe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_one, container, false);
        initView(root);
        init();
        return root;
    }

    private void init() {
        for (int i = 0 ; i < model.productDescribe.size(); i ++){
            TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_view_layout,null,false);
            textView.setText(model.productDescribe.get(i).name + "：" +model.productDescribe.get(i).desc);
            financing_info_layout.addView(textView);
        }
        for (int i = 0 ; i < model.auditList.size(); i ++){
            View linearLayout = LayoutInflater.from(getActivity()).inflate(R.layout.checked_view_layout,null,false);
            TextView textView = (TextView)linearLayout.findViewById(R.id.title);
            TextView textView1 = (TextView)linearLayout.findViewById(R.id.content);
            textView.setText(model.auditList.get(i).name);
            textView1.setText(model.auditList.get(i).desc);
            check_info_layout.addView(linearLayout);
        }
        for (int i = 0 ; i < model.imgList.size(); i ++){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_view,null,false);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(model.imgList.get(i).desc);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            Glide.with(getActivity())
                    .load(model.imgList.get(i).imgUrl)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
            prove_list_layout.addView(view);
        }

    }

    private void initView(View root){
        prove_list_layout = (LinearLayout)root.findViewById(R.id.prove_list_layout);
        check_info_layout = (LinearLayout)root.findViewById(R.id.check_info_layout);
        financing_info_layout = (LinearLayout)root.findViewById(R.id.financing_info_layout);
        tv_product_content= (TextView) root.findViewById(R.id.tv_product_content);
        returnTop = (PullToRefreshView)root.findViewById(R.id.return_top);
        returnTop.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshViewShow!= null){
                    onRefreshViewShow.onViewShow();
                }
                returnTop.setRefreshing(false);
            }
        });
    }

    public void setOnRefreshViewShow(OnRefreshViewShow onRefreshViewShow){
        this.onRefreshViewShow = onRefreshViewShow;
    }

    public interface OnRefreshViewShow{
        void onViewShow();
    }
}
