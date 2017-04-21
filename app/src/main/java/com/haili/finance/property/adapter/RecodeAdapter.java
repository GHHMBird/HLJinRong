package com.haili.finance.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.property.ManageMoney;

import java.util.ArrayList;

import static com.haili.finance.R.id.tv_name;

/*
 * Created by Monkey on 2017/3/18.
 */

public class RecodeAdapter extends BaseAdapter {
    private ArrayList<ManageMoney> list;
    private Context context;

    public RecodeAdapter(Context context, ArrayList<ManageMoney> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_inv_record, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).productName);
        viewHolder.tvState.setText(list.get(position).productState);
        if (list.get(position).productState.equals("募集中")) {
            viewHolder.tvState.setBackgroundResource(R.mipmap.tip_muji);
        } else if (list.get(position).productState.equals("计息中")) {
            viewHolder.tvState.setBackgroundResource(R.mipmap.tip_jixi);
        } else if (list.get(position).productState.equals("还款中")) {
            viewHolder.tvState.setBackgroundResource(R.mipmap.tip_huaikuan);
        }
        viewHolder.tvInvMoney.setText(list.get(position).investBalance + "");
        viewHolder.tvReturnMoney.setText(list.get(position).yieldRateBalance + "");
        viewHolder.tvDate.setText(list.get(position).repaymentTime);
        return convertView;
    }

    public void setData(ArrayList<ManageMoney> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView tvName, tvState, tvInvMoney, tvReturnMoney, tvDate;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(tv_name);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            tvInvMoney = (TextView) view.findViewById(R.id.tv_inv_money);
            tvReturnMoney = (TextView) view.findViewById(R.id.tv_return_money);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
