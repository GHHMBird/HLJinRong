package com.haili.finance.user.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cheguan.lgdpulltorefresh.PullToRefreshView;
import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.GetRedPackageModel;
import com.haili.finance.business.user.GetRedPackageRequest;
import com.haili.finance.business.user.GetRedPackageResponse;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.user.adapter.UsedRedPacketAdapter;
import com.haili.finance.widget.MyLayoutManager;
import com.haili.finance.widget.SlidingFrameLayout;
import com.mcxiaoke.bus.Bus;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by fuliang on 2017/3/9.
 */

@SuppressLint("ValidFragment")
public class UsedRedPacketFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MyLayoutManager myLayoutManager;
    private UsedRedPacketAdapter adapter;
    private PullToRefreshView mPullToRefreshView;
    private int type = 0;
    private Subscription subscrip;
    private ArrayList<GetRedPackageModel> redPackageArrayList;
    private SlidingFrameLayout frameLayout;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    public UsedRedPacketFragment(int getType){
        type = getType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_red_packet, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.packet_list);
        frameLayout = (SlidingFrameLayout)view.findViewById(R.id.loading_layout) ;
        imageView = (ImageView) view.findViewById(R.id.loading_view);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        myLayoutManager = new MyLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        getAllData();
        return view;
    }

    private void getAllData(){
        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        getRedPackage(2);
    }

    private void getRedPackage(int type) {
        GetRedPackageRequest request = new GetRedPackageRequest();
        request.state = type + "";
        subscrip=    UserBusinessHelper.getRedPackage(request)
                .subscribe(new Action1<GetRedPackageResponse>() {
                    @Override
                    public void call(GetRedPackageResponse getRedPackageResponse) {
                        if (getRedPackageResponse.dataModel != null) {
                            redPackageArrayList = getRedPackageResponse.dataModel;
                            setAdapter();
                        }
                        if (animationDrawable != null){
                            animationDrawable.stop();
                        }
                        frameLayout.setVisibility(View.GONE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (animationDrawable != null){
                            animationDrawable.stop();
                        }
                        frameLayout.setVisibility(View.GONE);
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(context, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void setAdapter() {
        if (redPackageArrayList != null && redPackageArrayList.size() > 0) {
            adapter = new UsedRedPacketAdapter(UsedRedPacketFragment.this, getActivity(), redPackageArrayList);
            adapter.setData(type);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Bus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}
