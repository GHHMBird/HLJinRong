package com.haili.finance.user.adapter;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.user.GetRedPackageModel;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/2/23.
 */

public class RedPacketOutDateAdapter extends RecyclerView.Adapter<RedPacketOutDateAdapter.ViewHolder> {

    private Fragment fragment;
    private OnClickDoneListener onClickDoneListener;
    private ArrayList<GetRedPackageModel> list;
    private Context context;
    public boolean httpOK;

    public RedPacketOutDateAdapter(Fragment fragment, Context context, ArrayList<GetRedPackageModel> list) {
        this.fragment = fragment;
        this.context = context;
        this.list = list;
    }

    public void setData(ArrayList<GetRedPackageModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<GetRedPackageModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() && httpOK) {
            return 1;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_redbackpage, parent, false);
            return new ViewHolder(v);
        } else {
            View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.refresh_foot_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == list.size() && httpOK) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.imageLoading.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.imageLoading.setLayoutParams(lp);
            holder.imageLoading.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.imageLoading.getDrawable();
            animationDrawable.start();
            return;
        }
        holder.userPacketBtn.setVisibility(View.GONE);
        holder.backLayout.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.bg_redbackpage_2));


        holder.packerName.setText(list.get(position).productType);//红包类型
        holder.packerRequire.setText("满" + list.get(position).investBanalce + "元可用");//满多少可用
        holder.packerPrice.setText(list.get(position).banalce + "");//红包金额
        holder.packerValidity.setText(list.get(position).endTime);//到期时间

        holder.userPacketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RedPacketModel  orderModel  =  (RedPacketModel)v.getTag();
                if (onClickDoneListener != null) {
                    onClickDoneListener.onEditDone();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (httpOK) {
            return list.size() + 1;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView packetType, packerPrice, packerName, packerRequire, packerValidity, userPacketBtn;
        LinearLayout backLayout;
        ImageView imageLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            imageLoading = (ImageView) itemView.findViewById(R.id.image);
            backLayout = (LinearLayout) itemView.findViewById(R.id.back_layout);
            packetType = (TextView) itemView.findViewById(R.id.tv_type);
            packerPrice = (TextView) itemView.findViewById(R.id.tv_money);
            packerName = (TextView) itemView.findViewById(R.id.tv_name);
            packerRequire = (TextView) itemView.findViewById(R.id.tv_content);
            packerValidity = (TextView) itemView.findViewById(R.id.tv_date);
            userPacketBtn = (TextView) itemView.findViewById(R.id.user_packet_btn);
        }
    }

    public void setOnClickDoneListener(OnClickDoneListener clickDoneListener) {
        this.onClickDoneListener = clickDoneListener;
    }

    public interface OnClickDoneListener {
        //        void onEditDone(RedPacketModel model);
        void onEditDone();
    }
}
