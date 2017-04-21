/*
 * Created by fuyou on 2015/5/7.
 */
package com.haili.finance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	protected List<T> data;
	private LayoutInflater inflater;
	private int layoutId;
	private ViewHolder holder;

	public BaseAdapter(Context context, List<T> data, int layoutId) {
		super();
		this.data = data;
		this.layoutId = layoutId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflateView(convertView, parent);
		holder = ViewHolder.get(convertView);
		bindData(position, convertView, getItem(position));
		return convertView;
	}

	public abstract void bindData(int position, View convertView, T item);

	@SuppressWarnings("unchecked")
	protected <D> D get(int id) {
		return (D) holder.getView(id);
	}

	private View inflateView(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layoutId, parent, false);
		}
		return convertView;
	}

}
