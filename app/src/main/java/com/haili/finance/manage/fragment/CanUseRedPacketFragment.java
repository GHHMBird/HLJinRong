package com.haili.finance.manage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.manage.GetRedPacketListModel;
import com.haili.finance.manage.adapter.CanUseRedPacketAdapter;
import com.haili.finance.widget.MyLayoutManager;

import java.util.ArrayList;

/*
 * Created by lfu on 2017/3/20.
 */

public class CanUseRedPacketFragment extends BaseFragment{

    public static final String TAG = "CanUseRedPacketFragment";

    private RecyclerView packet_list;
    private CanUseRedPacketAdapter adapter;
    private ArrayList<GetRedPacketListModel> list;
    private MyLayoutManager myLayoutManager;
    private OnClickDoneListener onClickDoneListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_red_packet_list, container, false);
        packet_list = (RecyclerView) view.findViewById(R.id.packet_list);
        myLayoutManager = new MyLayoutManager(getActivity());
        packet_list.setLayoutManager(myLayoutManager);
        adapter = new CanUseRedPacketAdapter(list,this);
        adapter.setOnClickDoneListener(new CanUseRedPacketAdapter.OnClickDoneListener() {
            @Override
            public void onEditDone(GetRedPacketListModel model) {
                if (onClickDoneListener != null){
                    onClickDoneListener.onEditDone(model);
                }
            }
        });
        packet_list.setAdapter(adapter);
        return view;
    }

    public void setData(ArrayList<GetRedPacketListModel> list){
        this.list = list;
    }

    public void setOnClickDoneListener(OnClickDoneListener clickDoneListener) {
        this.onClickDoneListener = clickDoneListener;
    }

    public interface OnClickDoneListener {
        void onEditDone(GetRedPacketListModel model);
    }
}
