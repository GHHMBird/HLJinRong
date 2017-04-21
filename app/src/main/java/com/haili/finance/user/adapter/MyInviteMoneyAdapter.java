package com.haili.finance.user.adapter;

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
import com.haili.finance.business.user.MyInviteMoneyModel;

import java.util.ArrayList;

public class MyInviteMoneyAdapter extends RecyclerView.Adapter<MyInviteMoneyAdapter.ViewHolder> {

    private ArrayList<MyInviteMoneyModel> arrayList;
    private Context context;
    public boolean httpOK;

    @Override
    public int getItemViewType(int position) {
        if (httpOK && position == arrayList.size()) {
            return 1;
        }
        return 0;
    }

    public MyInviteMoneyAdapter(Context context, ArrayList<MyInviteMoneyModel> al) {
        this.context = context;
        this.arrayList = al;
    }

    @Override
    public MyInviteMoneyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.content_my_invite_adapter, parent, false);
            return new MyInviteMoneyAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new MyInviteMoneyAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MyInviteMoneyAdapter.ViewHolder holder, int position) {
        if (httpOK && position == arrayList.size()) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.imgLoading.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.imgLoading.setLayoutParams(lp);
            holder.imgLoading.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.imgLoading.getDrawable();
            animationDrawable.start();
            return;
        }
        holder.my_invite_money.setText(arrayList.get(position).amount + "");
        holder.my_invite_phone.setText(arrayList.get(position).time);
        holder.my_invite_time.setText(arrayList.get(position).type);
    }

    public void setData(ArrayList<MyInviteMoneyModel> al) {
        this.arrayList = al;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<MyInviteMoneyModel> al) {
        this.arrayList.addAll(al);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (httpOK) {
            return arrayList.size() + 1;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView my_invite_phone, my_invite_money, my_invite_time;
        ImageView imgLoading;

        public ViewHolder(View v) {
            super(v);
            my_invite_phone = (TextView) v.findViewById(R.id.my_invite_phone);
            my_invite_money = (TextView) v.findViewById(R.id.my_invite_money);
            my_invite_time = (TextView) v.findViewById(R.id.my_invite_time);
            imgLoading = (ImageView) v.findViewById(R.id.image);
        }
    }
}
