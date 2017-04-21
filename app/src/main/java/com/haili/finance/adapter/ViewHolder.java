package com.haili.finance.adapter;

import android.util.SparseArray;
import android.view.View;

/*
 * viewHolder工具类
 * 
 * @author zhl,fuyou
 *
 */
public class ViewHolder {

	private final SparseArray<View> views;
	private View convertView;

	private ViewHolder(View convertView) {
		this.views = new SparseArray<View>();
		this.convertView = convertView;
		convertView.setTag(this);
	}

	public static ViewHolder get(View convertView) {
		if (convertView.getTag() == null) {
			return new ViewHolder(convertView);
		}
		ViewHolder existedHolder = (ViewHolder) convertView.getTag();
		return existedHolder;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}
}
