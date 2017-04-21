package com.haili.finance.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.property.MyEarningsListModel;

import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class MyEarningsListAdapter extends RecyclerView.Adapter<MyEarningsListAdapter.ViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<MyEarningsListModel> listBanalce;

    public MyEarningsListAdapter(Context context,ArrayList<MyEarningsListModel> list){
        this.context = context;
        listBanalce = list;
    }

    public void setData(ArrayList<MyEarningsListModel> list){
        listBanalce = list;
    }


    @Override
    public MyEarningsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyEarningsListAdapter.ViewHolder holder, int position) {
        holder.time.setText(listBanalce.get(position).time);
        holder.money.setText(listBanalce.get(position).balance + "");
    }

    @Override
    public int getItemCount() {
        return listBanalce.size();
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView time,money;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView)itemView.findViewById(R.id.time);
            money = (TextView)itemView.findViewById(R.id.money);

        }
    }
}
