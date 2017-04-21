package com.haili.finance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.modle.InvestmentHistoryModel;

import java.util.List;

/*
 * Created by Monkey on 2017/1/19.
 */

public class InvestmentRecordAdapter extends BaseAdapter<InvestmentHistoryModel> {

    public InvestmentRecordAdapter(Context context, List<InvestmentHistoryModel> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void bindData(int position, View convertView, InvestmentHistoryModel item) {
        if (data != null) {
            TextView tv_name = get(R.id.tv_name);
            TextView tv_state = get(R.id.tv_state);
            TextView tv_inv_money = get(R.id.tv_inv_money);
            TextView tv_return_money = get(R.id.tv_return_money);
            TextView tv_date = get(R.id.tv_date);
            tv_name.setText(data.get(position).getName());
            tv_state.setText(data.get(position).getState());
            if ("10".equals(data.get(position).getState())) {//投资中
                tv_state.setBackgroundResource(R.mipmap.tip_muji);
                tv_state.setText("投资中");
            } else if ("20".equals(data.get(position).getState())) {//募集结束
                tv_state.setBackgroundResource(R.mipmap.tip_muji);
                tv_state.setText("募集结束");
            } else if ("30".equals(data.get(position).getState())) {//计息中
                tv_state.setBackgroundResource(R.mipmap.tip_jixi);
                tv_state.setText("计息中");
            } else if ("40".equals(data.get(position).getState())) {//还款中
                tv_state.setBackgroundResource(R.mipmap.tip_huaikuan);
                tv_state.setText("还款中");
            } else if ("50".equals(data.get(position).getState())) {//已还款
                tv_state.setBackgroundResource(R.mipmap.tip_muji);
                tv_state.setText("已还款");
            }
            tv_inv_money.setText(data.get(position).getInv_money());
            tv_return_money.setText(data.get(position).getProfit_return());
            tv_date.setText(data.get(position).getDate().substring(0,10));
        }

    }
}
