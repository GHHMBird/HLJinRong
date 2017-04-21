package com.haili.finance.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.property.RePayPlanItem;

import java.util.ArrayList;

/*
 * Created by hhm on 2017/3/20.
 */

public class RepayPlanAdapter extends RecyclerView.Adapter<RepayPlanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RePayPlanItem> repaymentPlan;

    public RepayPlanAdapter(Context context, ArrayList<RePayPlanItem> repaymentPlan) {
        this.context = context;
        this.repaymentPlan = repaymentPlan;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.repay_plan_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textStyle.setText(repaymentPlan.get(position).repaymentNo + "");
        holder.textState.setText(repaymentPlan.get(position).repaymentState + "");
        holder.textDate.setText(repaymentPlan.get(position).repaymentDate + "");
        holder.textMoney.setText(repaymentPlan.get(position).totalRepaymentBalance + "");
    }

    @Override
    public int getItemCount() {
        return repaymentPlan.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textStyle, textDate, textState, textMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            textStyle = (TextView) itemView.findViewById(R.id.repay_plan_item_one);
            textDate = (TextView) itemView.findViewById(R.id.repay_plan_item_date);
            textMoney = (TextView) itemView.findViewById(R.id.repay_plan_item_money);
            textState = (TextView) itemView.findViewById(R.id.repay_plan_item_three);
        }
    }
}
