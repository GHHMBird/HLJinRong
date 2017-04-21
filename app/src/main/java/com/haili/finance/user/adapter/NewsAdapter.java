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
 *
 * Created by Monkey on 2017/2/28.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public boolean httpOk;//false  true
    private Context context;
    private ArrayList<NewsNoticeModel> list;
    private View inflate;

    public NewsAdapter(Context context, ArrayList<NewsNoticeModel> al) {
        this.context = context;
        this.list = al;
    }

    @Override
    public int getItemViewType(int position) {
        if (httpOk && position == list.size()) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (httpOk && position == list.size()) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.image.setLayoutParams(lp);
            holder.image.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.image.getDrawable();
            animationDrawable.start();
            return;
        }
        NewsNoticeModel newsNoticeModel = list.get(position);
        holder.icon.setImageResource("1".equals(newsNoticeModel.readSate) ? R.mipmap.news_unread : R.mipmap.news_read);
        holder.time.setText(newsNoticeModel.content);
        holder.time.setTextColor("1".equals(newsNoticeModel.readSate) ? Color.BLACK : Color.GRAY);
        holder.title.setText(newsNoticeModel.title);
        holder.title.setTextColor("1".equals(newsNoticeModel.readSate) ? Color.BLACK : Color.GRAY);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra("id", list.get(position).id);
                intent.putExtra("mode", 1);
                context.startActivity(intent);
            }
        });
    }

    public void setData(ArrayList<NewsNoticeModel> al) {
        this.list = al;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewsNoticeModel> al) {
        this.list.addAll(al);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (httpOk) {
            return list.size() + 1;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, time;
        ImageView icon, image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            icon = (ImageView) itemView.findViewById(R.id.message_adapter_item_icon);
            title = (TextView) itemView.findViewById(R.id.message_adapter_item_message);
            time = (TextView) itemView.findViewById(R.id.message_adapter_item_time);
        }
    }
}
