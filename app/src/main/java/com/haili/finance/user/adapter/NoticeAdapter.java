package com.haili.finance.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.user.NewsNoticeModel;
import com.haili.finance.user.activity.MessageDetailActivity;

import java.util.ArrayList;

/**
 * Created by Monkey on 2017/2/28.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NewsNoticeModel> arrayList;
    private View inflate;
    public boolean httpOK;

    public NoticeAdapter(Context context, ArrayList<NewsNoticeModel> al) {
        this.context = context;
        this.arrayList = al;
    }

    @Override
    public int getItemViewType(int position) {
        if (httpOK && position == arrayList.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            inflate = LayoutInflater.from(context).inflate(R.layout.message_adapter_item, parent, false);
            return new ViewHolder(inflate);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new ViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        holder.icon.setImageResource("1".equals(arrayList.get(position).readSate) ? R.mipmap.tell_unread : R.mipmap.tell_read);
        holder.time.setText(arrayList.get(position).content);
        holder.time.setTextColor("1".equals(arrayList.get(position).readSate) ? Color.BLACK : Color.GRAY);
        holder.title.setText(arrayList.get(position).title);
        holder.title.setTextColor("1".equals(arrayList.get(position).readSate) ? Color.BLACK : Color.GRAY);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra("id", arrayList.get(position).id);
                intent.putExtra("mode", 1);
                context.startActivity(intent);
                if ("1".equals(arrayList.get(position).readSate)) {//把未读状态改变
                    arrayList.get(position).readSate ="2";
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setData(ArrayList<NewsNoticeModel> al) {
        this.arrayList = al;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewsNoticeModel> al) {
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

        TextView title, time;
        ImageView icon, imgLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.message_adapter_item_icon);
            imgLoading = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.message_adapter_item_message);
            time = (TextView) itemView.findViewById(R.id.message_adapter_item_time);
        }
    }
}
