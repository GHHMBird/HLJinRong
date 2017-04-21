package com.haili.finance.property.adapter;
/*
 * Created by hhm on 2017/3/28.
 */

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
import com.haili.finance.business.property.ManageMoney;

import java.util.ArrayList;

import static com.haili.finance.R.id.tv_name;

public class ManageMoneyAdapter extends RecyclerView.Adapter<ManageMoneyAdapter.ViewHolder> {

    private ArrayList<ManageMoney> list;
    public boolean httpOK;
    private View inflate;
    private Context context;

    public ManageMoneyAdapter(Context context, ArrayList<ManageMoney> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(ArrayList<ManageMoney> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<ManageMoney> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ManageMoneyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            inflate = LayoutInflater.from(context).inflate(R.layout.item_inv_record, parent, false);
            return new ManageMoneyAdapter.ViewHolder(inflate);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new ManageMoneyAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ManageMoneyAdapter.ViewHolder holder, int position) {
        if (position == list.size() && httpOK) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.image.setLayoutParams(lp);
            holder.image.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.image.getDrawable();
            animationDrawable.start();
            return;
        }
        holder.tvName.setText(list.get(position).productName);
        holder.tvState.setText(list.get(position).productState);
        if (list.get(position).productState.equals("募集中")) {
            holder.tvState.setBackgroundResource(R.mipmap.tip_muji);
        } else if (list.get(position).productState.equals("计息中")) {
            holder.tvState.setBackgroundResource(R.mipmap.tip_jixi);
        } else if (list.get(position).productState.equals("还款中")) {
            holder.tvState.setBackgroundResource(R.mipmap.tip_huaikuan);
        }
        holder.tvInvMoney.setText(list.get(position).investBalance + "");
        holder.tvReturnMoney.setText(list.get(position).yieldRateBalance + "");
        holder.tvDate.setText(list.get(position).repaymentTime);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() && httpOK) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (httpOK) {
            return list.size() + 1;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvState, tvInvMoney, tvReturnMoney, tvDate;
        private ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            tvName = (TextView) view.findViewById(tv_name);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            tvInvMoney = (TextView) view.findViewById(R.id.tv_inv_money);
            tvReturnMoney = (TextView) view.findViewById(R.id.tv_return_money);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
