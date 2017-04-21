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
import com.haili.finance.business.user.MessageBean;
import com.haili.finance.user.activity.MessageDetailActivity;

import java.util.ArrayList;

/**
 *
 * Created by Monkey on 2017/2/28.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    public ArrayList<MessageBean> arrayList = new ArrayList<>();
    private View inflate;
    public boolean httpOK;
    private MessageItemClick messageItemClick;

    public MessageAdapter(Context context, ArrayList<MessageBean> al) {
        this.context = context;
        this.arrayList = al;
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
    public int getItemViewType(int position) {
        if (position == arrayList.size() && httpOK) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        holder.icon.setImageResource("1".equals(arrayList.get(position).readState) ? R.mipmap.message_unread2 : R.mipmap.message_read);
        holder.time.setText(arrayList.get(position).time);
        holder.time.setTextColor("1".equals(arrayList.get(position).readState) ? Color.BLACK : Color.GRAY);
        holder.title.setText(arrayList.get(position).title);
        holder.title.setTextColor("1".equals(arrayList.get(position).readState) ? Color.BLACK : Color.GRAY);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra("id", arrayList.get(position).id);
                intent.putExtra("mode", 2);
                context.startActivity(intent);
                if ("1".equals(arrayList.get(position).readState)) {
                    arrayList.get(position).readState = "2";
                    notifyDataSetChanged();
                }
                if (messageItemClick != null) {
                    messageItemClick.clickDone(position);
                }
            }
        });
    }

    public void setData(ArrayList<MessageBean> al) {
        this.arrayList = al;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<MessageBean> al) {
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
        ImageView icon;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            icon = (ImageView) itemView.findViewById(R.id.message_adapter_item_icon);
            title = (TextView) itemView.findViewById(R.id.message_adapter_item_message);
            time = (TextView) itemView.findViewById(R.id.message_adapter_item_time);
        }
    }

    public void messageOnClick(MessageItemClick messageItemClick) {
        this.messageItemClick = messageItemClick;
    }

    public interface MessageItemClick {
        void clickDone(int position);
    }
}
