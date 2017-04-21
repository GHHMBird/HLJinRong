package com.haili.finance.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.user.GetRedPackageModel;
import com.haili.finance.user.fragment.UsedRedPacketFragment;

import java.util.ArrayList;

/**
 * Created by fuliang on 2017/3/9.
 */

public class UsedRedPacketAdapter extends RecyclerView.Adapter<UsedRedPacketAdapter.ViewHolder>{

    private UsedRedPacketFragment fragment;
    private ArrayList<GetRedPackageModel> list;

    private int redPacketType = 0;
    private Context context;

    public UsedRedPacketAdapter(UsedRedPacketFragment fragment, Context context, ArrayList<GetRedPackageModel> list) {
        this.fragment = fragment;
        this.context = context;
        this.list = list;
    }

    public void setData(int type) {
        redPacketType = type;
    }

    @Override
    public UsedRedPacketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_redbackpage, null);
        return new UsedRedPacketAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (redPacketType != 0) {
            holder.userPacketBtn.setVisibility(View.GONE);
            holder.backLayout.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.bg_redbackpage_2));
        }

        holder.packerName.setText(list.get(position).productType);//红包类型
        holder.packerRequire.setText("满"+list.get(position).investBanalce+"元可用");//满多少可用
        holder.packerPrice.setText(list.get(position).banalce+"");//红包金额
        holder.packerValidity.setText(list.get(position).endTime);//到期时间
        holder.userPacketBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView packetType, packerPrice, packerName, packerRequire, packerValidity, userPacketBtn;
        LinearLayout backLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            backLayout = (LinearLayout) itemView.findViewById(R.id.back_layout);
            packetType = (TextView) itemView.findViewById(R.id.tv_type);
            packerPrice = (TextView) itemView.findViewById(R.id.tv_money);
            packerName = (TextView) itemView.findViewById(R.id.tv_name);
            packerRequire = (TextView) itemView.findViewById(R.id.tv_content);
            packerValidity = (TextView) itemView.findViewById(R.id.tv_date);
            userPacketBtn = (TextView) itemView.findViewById(R.id.user_packet_btn);
        }
    }
}
