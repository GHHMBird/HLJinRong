package com.haili.finance.user.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haili.finance.R;
import com.haili.finance.business.user.BankInfo;

import java.util.ArrayList;

/**
 *
 * Created by Monkey on 2017/3/14.
 */

public class BankListAdapter extends BaseAdapter {

    private ArrayList<BankInfo> list;
    private Context context;
    private String bankCardNo;

    public BankListAdapter(Context context, ArrayList<BankInfo> list, String bankCardNo) {
        this.list = list;
        this.context = context;
        this.bankCardNo = bankCardNo;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bank_list_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bankName.setText(list.get(position).backName);
        Glide.with(context)
                .load(list.get(position).bankIcon)
                .error(R.mipmap.icon_loading)
                .placeholder(R.mipmap.icon_loading)
                .into(viewHolder.icon);
        viewHolder.text.setText(Html.fromHtml("单笔限<font color=red>" + (Integer.parseInt(list.get(position).singleLimit) / 10000) + "</font>万元,单日限<font color=red>" + (Integer.parseInt(list.get(position).dayLimit) / 10000) + "</font>万元,单月限<font color=red>"+(Integer.parseInt(list.get(position).monthLimit) / 10000) +"</font>万元"));
        if (TextUtils.equals(bankCardNo, list.get(position).backCBDNO)) {
            viewHolder.selector.setImageResource(R.mipmap.check_true);
        } else {
            viewHolder.selector.setImageResource(R.color.white);
        }
        return convertView;
    }

    class ViewHolder {

        private TextView bankName, text;
        private ImageView icon, selector;

        public ViewHolder(View view) {
            bankName = (TextView) view.findViewById(R.id.bank_list_adapter_bankname);
            text = (TextView) view.findViewById(R.id.bank_list_adapter_text);
            icon = (ImageView) view.findViewById(R.id.bank_list_adapter_iv);
            selector = (ImageView) view.findViewById(R.id.bank_list_adapter_select);
        }
    }
}
