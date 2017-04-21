package com.haili.finance.property.adapter;
/*
 * Created by hhm on 2017/3/20.
 */

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
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.property.InvestmentRecordListModel;
import com.haili.finance.manage.activity.ProductDetailActivity;

import java.util.ArrayList;

public class InvestmentRedrdAdapter extends RecyclerView.Adapter<InvestmentRedrdAdapter.ViewHolder> {

    private View inflate;
    private Context context;
    public boolean httpOK;
    private ArrayList<InvestmentRecordListModel> msgList;
    private String manageMoneyId;

    public InvestmentRedrdAdapter(Context context, ArrayList<InvestmentRecordListModel> msgList, String manageMoneyId) {
        this.msgList = msgList;
        this.context = context;
        this.manageMoneyId = manageMoneyId;
    }

    public void addData(ArrayList<InvestmentRecordListModel> msgList) {
        this.msgList.addAll(msgList);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<InvestmentRecordListModel> msgList) {
        this.msgList = msgList;
        notifyDataSetChanged();
    }

    @Override
    public InvestmentRedrdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            inflate = LayoutInflater.from(context).inflate(R.layout.investment_redrd_item, parent, false);
            return new InvestmentRedrdAdapter.ViewHolder(inflate);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.refresh_foot_layout, parent, false);
            return new InvestmentRedrdAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(InvestmentRedrdAdapter.ViewHolder holder, final int position) {
        if (position == msgList.size() && httpOK) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float) 60 * density);
            holder.image.setLayoutParams(lp);
            holder.image.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.image.getDrawable();
            animationDrawable.start();
            return;
        }
        holder.textMoney.setText(msgList.get(position).banalce + "");
        if (TextUtils.isEmpty(manageMoneyId)) {
            holder.textName.setText(msgList.get(position).productName+"");
        } else {
            holder.textName.setText("");
        }
        holder.textTime.setText(msgList.get(position).time + "");

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(manageMoneyId)) {

                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("params", msgList.get(position).productId);
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == msgList.size() && httpOK) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (httpOK) {
            return msgList.size() + 1;
        }
        return msgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView textTime, textName, textMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            textTime = (TextView) itemView.findViewById(R.id.invest_redrd_item_time);
            textName = (TextView) itemView.findViewById(R.id.invest_redrd_item_name);
            textMoney = (TextView) itemView.findViewById(R.id.invest_redrd_item_money);
        }
    }
}
