package com.haili.finance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.property.SaveDetailListModel;
import com.haili.finance.modle.RetailedModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Monkey on 2017/1/19.
 */

public class RetailedAdapter extends BaseAdapter<SaveDetailListModel> {
    public RetailedAdapter(Context context, List data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void bindData(int position, View convertView, SaveDetailListModel item) {
        if (data != null) {
            TextView tv_date = get(R.id.tv_date);
            TextView tv_money = get(R.id.tv_money);
            TextView tv_type = get(R.id.tv_type);
            TextView tv_content = get(R.id.tv_content);
            tv_date.setText(data.get(position).time);
            tv_money.setText(data.get(position).banalce + "");
            tv_type.setText(data.get(position).type);
            tv_content.setText(data.get(position).remark);
        }
    }
}
