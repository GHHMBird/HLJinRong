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
import com.haili.finance.business.user.MyInviteFriendInfo;

import java.util.ArrayList;

public class MyInviteAdapter extends RecyclerView.Adapter<MyInviteAdapter.ViewHolder> {

    private ArrayList<MyInviteFriendInfo> arrayList;
    private Context context;
    public boolean httpOK;

    public MyInviteAdapter(Context context, ArrayList<MyInviteFriendInfo> al) {
        this.context = context;
        this.arrayList = al;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrayList.size() && httpOK) {
            return 1;
        }
        return 0;
    }

    @Override
    public MyInviteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.content_my_invite_adapter, parent, false);
            return new MyInviteAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new MyInviteAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MyInviteAdapter.ViewHolder holder, int position) {
        if (position == arrayList.size() && httpOK) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.image.setLayoutParams(lp);
            holder.image.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.image.getDrawable();
            animationDrawable.start();
            return;
        }
        holder.my_invite_money.setText(arrayList.get(position).investAmount + "");
        holder.my_invite_phone.setText(arrayList.get(position).phone);
        holder.my_invite_time.setText(arrayList.get(position).registerTime);
    }

    public void setData(ArrayList<MyInviteFriendInfo> al) {
        this.arrayList = al;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<MyInviteFriendInfo> al) {
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
        ImageView image;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            my_invite_phone = (TextView) v.findViewById(R.id.my_invite_phone);
            my_invite_money = (TextView) v.findViewById(R.id.my_invite_money);
            my_invite_time = (TextView) v.findViewById(R.id.my_invite_time);
        }
    }
}
