package com.haili.finance.manage.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.manage.InvestListModel;

import java.util.List;

/*
 * Created by Monkey on 2017/1/14.
 */

@SuppressLint("ValidFragment")
public class ProductDetail_ThreeFragment extends BaseFragment {
    private PullToRefreshView returnTop;
    private OnRefreshViewShow onRefreshViewShow;
    private LinearLayout list_layout;
    private TextView size_text;
    private List<InvestListModel> list;

    public ProductDetail_ThreeFragment(List<InvestListModel> investList) {
        list = investList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_three, container, false);
        initView(root);
        init();
        return root;
    }

    private void init() {
        initData();
        returnTop.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshViewShow != null) {
                    onRefreshViewShow.onViewShow();
                }
                returnTop.setRefreshing(false);
            }
        });


    }

    private void initData() {
        size_text.setText(list.size() + "");
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_licai_record,null, false);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_name.setText(list.get(i).investName);
            tv_money.setText(list.get(i).investBanalce + "");
            tv_date.setText(list.get(i).time.substring(0, 10));
            list_layout.addView(view);
        }
    }

    private void initView(View root) {
        returnTop = (PullToRefreshView) root.findViewById(R.id.return_top);
//        myListView= (MyListView) root.findViewById(R.id.myListView);
        list_layout = (LinearLayout) root.findViewById(R.id.list_layout);
        size_text = (TextView)root.findViewById(R.id.size_text);
    }

    public void setOnRefreshViewShow(OnRefreshViewShow onRefreshViewShow) {
        this.onRefreshViewShow = onRefreshViewShow;
    }

    public interface OnRefreshViewShow {
        void onViewShow();
    }
}
