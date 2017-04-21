package com.haili.finance.adapter;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.manage.ManageListModel;
import com.haili.finance.fragment.GreenHandsManageFragment;
import com.haili.finance.widget.ShadowLayout;

import java.text.NumberFormat;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/9.
 */

public class GreenHandsManageAdapter extends RecyclerView.Adapter<GreenHandsManageAdapter.ViewHolder> implements View.OnClickListener {

    private Fragment fragment;
    private Context context;
    private OnEditDoneListener onEditDoneListener;
    private boolean hasMore = false;
    private ArrayList<ManageListModel> models = new ArrayList<>();

    public GreenHandsManageAdapter (GreenHandsManageFragment fragment, Context context){
        this.fragment = fragment;
        this.context = context;
    }

    public void setData(ArrayList<ManageListModel> listModels){
        models.addAll(listModels);
        if (hasMore){
            ManageListModel manageListModel = new ManageListModel();
            manageListModel.isLoading = 1;
            models.add(manageListModel);
        }
    }

    public void clearData(){
        models.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if(models == null || models.get(position) == null){
            return 0;
        }
        return models.get(position).isLoading;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_product_licai, null);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }else {
            View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.refresh_foot_layout, null);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.image.getLayoutParams();
            float density = context.getResources().getDisplayMetrics().density;
            lp.height = Math.round((float)60 * density);
            holder.image.setLayoutParams(lp);
            holder.image.setImageResource(com.cheguan.lgdpulltorefresh.R.drawable.refresh_animlist);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.image.getDrawable();
            animationDrawable.start();
            return;
        }
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        String yieldRate = nt.format(models.get(position).yieldRate);
        holder.tv_return_rate.setText(yieldRate.substring(0,yieldRate.length()-1));
        if (models.get(position).yieldRateAdd != 0){
            holder.tv_return_rate2.setText("%+" + nt.format(models.get(position).yieldRateAdd));
        }
        holder.tv_name.setText(models.get(position).productName);
        if ("1".equals(models.get(position).isCanUseRedBag)){
            holder.iv_redPackpage.setVisibility(View.VISIBLE);
        }else {
            holder.iv_redPackpage.setVisibility(View.GONE);
        }
        holder.tv_progress.setText(nt.format(models.get(position).investmentProgress));
        holder.tv_term.setText((models.get(position).loanPeriod));
        if (models.get(position).productState.equals("20") || models.get(position).productState.equals("10")){
            holder.progressBar.setProgress((int)(models.get(position).investmentProgress * 100));
        }else {
            holder.progressBar.setProgress(100);
            holder.progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_style_second));
        }
        if (models.get(position).productState.equals("10")){
            holder.tv_state2.setVisibility(View.GONE);
            holder.tv_state.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_circle1));
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_state.setText(setButtonText(position));
        }else {
            holder.shadow.setVisibility(View.GONE);
            holder.tv_state2.setVisibility(View.VISIBLE);
            holder.tv_state2.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_circle2));
            holder.tv_state2.setTextColor(context.getResources().getColor(R.color.text_grey));
            holder.tv_state2.setText(setButtonText(position));
        }

        holder.contentLayout.setTag(models.get(position));
        holder.contentLayout.setOnClickListener(this);
    }

    private String setButtonText(int position){
        if (models.get(position).productState.equals("10")){
            return "抢购";
        }
        if(models.get(position).productState.equals("20")){
            return "满标";
        }
        if(models.get(position).productState.equals("30")){
            return "计息中";
        }
        if(models.get(position).productState.equals("40")){
            return "还款中";
        }
        if(models.get(position).productState.equals("50")){
            return "已还款";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onClick(View view) {
        if (onEditDoneListener!= null){
            ManageListModel model = (ManageListModel) view.getTag();
            onEditDoneListener.onEditDone(model);
        }
    }

    public void setOnCkeckedListener(OnEditDoneListener onEditDone) {
        this.onEditDoneListener = onEditDone;
    }

    public interface OnEditDoneListener {
        void onEditDone(ManageListModel model);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;
        TextView tv_name,tv_progress,tv_state,tv_term,tv_return_rate2,tv_return_rate,tv_state2;
        ImageView iv_redPackpage,image;
        LinearLayout contentLayout;
        ShadowLayout shadow;


        public ViewHolder(View itemView) {
            super(itemView);
            shadow = (ShadowLayout)itemView.findViewById(R.id.shadow);
            tv_return_rate = (TextView) itemView.findViewById(R.id.tv_return_rate);
            tv_return_rate2 = (TextView)itemView.findViewById(R.id.tv_return_rate2);
            tv_term = (TextView)itemView.findViewById(R.id.tv_term);
            tv_state = (TextView)itemView.findViewById(R.id.tv_state);
            tv_state2 = (TextView)itemView.findViewById(R.id.tv_state2);
            progressBar= (ProgressBar)itemView.findViewById(R.id.progress);
            tv_progress = (TextView)itemView.findViewById(R.id.tv_progress);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            iv_redPackpage = (ImageView)itemView.findViewById(R.id.iv_redPackpage);
            contentLayout = (LinearLayout)itemView.findViewById(R.id.content_layout);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }


    //是否有多页数据，如果有则加载更多的项目
    public void loadingMoreItem(){
        //先把加载更多的Loading清除掉
        this.models.remove(models.size()-1);
        this.notifyItemRemoved(this.getItemCount() - 1);
    }

    public void isHasMoreItem(boolean isHasMore){
        this.hasMore = isHasMore;
    }


}
