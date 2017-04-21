package com.haili.finance.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.haili.finance.R;
import com.haili.finance.WebActivity;
import com.haili.finance.business.user.NewsNoticeModel;

import java.util.ArrayList;

/**
 *
 * Created by Monkey on 2017/3/1.
 */

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.FindPicViewHolder> {

    private Context context;
    private ArrayList<NewsNoticeModel> newsNoticeModelArrayList;
    private View inflate;
    public boolean httpOK;

    public ActiveAdapter(Context context, ArrayList<NewsNoticeModel> arrayList) {
        this.context = context;
        this.newsNoticeModelArrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (httpOK && position == newsNoticeModelArrayList.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public FindPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            inflate = LayoutInflater.from(context).inflate(R.layout.find_pic_fm, parent, false);
            return new FindPicViewHolder(inflate);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new FindPicViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(FindPicViewHolder holder, final int position) {
        if (httpOK && position == newsNoticeModelArrayList.size()) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.imgLoading.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.imgLoading.setLayoutParams(lp);
            holder.imgLoading.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.imgLoading.getDrawable();
            animationDrawable.start();
            return;
        }
        Glide.with(context)
                .load(newsNoticeModelArrayList.get(position).imageUrl)
                .error(R.mipmap.hhm)
                .placeholder(R.mipmap.hhm)
                .into(holder.iv);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newsNoticeModelArrayList.get(position).url)) {
                    //没有url
                } else {
                    //有url
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("params",newsNoticeModelArrayList.get(position).url);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (httpOK) {
            return newsNoticeModelArrayList.size() + 1;
        }
        return newsNoticeModelArrayList.size();
    }

    public void addData(ArrayList<NewsNoticeModel> al) {
        this.newsNoticeModelArrayList.addAll(al);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<NewsNoticeModel> al) {
        this.newsNoticeModelArrayList = al;
        notifyDataSetChanged();
    }

    class FindPicViewHolder extends RecyclerView.ViewHolder {

        ImageView iv, imgLoading;

        FindPicViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.find_pic_pic);
            imgLoading = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
