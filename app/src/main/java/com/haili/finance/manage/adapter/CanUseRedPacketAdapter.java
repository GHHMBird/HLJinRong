package com.haili.finance.manage.adapter;

import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.haili.finance.R;
import com.haili.finance.business.manage.GetRedPacketListModel;
import java.util.ArrayList;

/*
 * Created by lfu on 2017/3/20.
 */

public class CanUseRedPacketAdapter extends RecyclerView.Adapter<CanUseRedPacketAdapter.ViewHolder> {

    private OnClickDoneListener onClickDoneListener;
    private Fragment fragment;
    private ArrayList<GetRedPacketListModel> list;

    public CanUseRedPacketAdapter(ArrayList<GetRedPacketListModel> modelList, Fragment fragment){
        list = modelList;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_redbackpage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.packerName.setText(list.get(position).productType);//红包类型
        holder.packerRequire.setText("满" + list.get(position).investBanalce + "元可用");//满多少可用
        holder.packerPrice.setText(list.get(position).banalce + "");//红包金额

        holder.packerValidity.setText(list.get(position).endTime);//到期时间
        holder.userPacketBtn.setVisibility(View.GONE);
        if (list.get(position).isClicked){
            holder.image_view.setBackground(fragment.getResources().getDrawable(R.mipmap.clicked_state));
        }else {
            holder.image_view.setBackground(fragment.getResources().getDrawable(R.mipmap.no_click_state));
        }
        holder.flight_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickDoneListener != null){
                    if (list.get(position).isClicked){
                        list.get(position).isClicked = false;
                    }else {
                        for (int i = 0 ; i <list.size(); i ++){
                            list.get(i).isClicked = false;
                        }
                        list.get(position).isClicked  = true;
                    }

                    onClickDoneListener.onEditDone(list.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView packetType, packerPrice, packerName, packerRequire, packerValidity, userPacketBtn;
        LinearLayout backLayout;
        ImageView imageLoading,image_view;
        CardView flight_item_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            backLayout = (LinearLayout) itemView.findViewById(R.id.back_layout);
            packetType = (TextView) itemView.findViewById(R.id.tv_type);
            packerPrice = (TextView) itemView.findViewById(R.id.tv_money);
            packerName = (TextView) itemView.findViewById(R.id.tv_name);
            packerRequire = (TextView) itemView.findViewById(R.id.tv_content);
            packerValidity = (TextView) itemView.findViewById(R.id.tv_date);
            userPacketBtn = (TextView) itemView.findViewById(R.id.user_packet_btn);
            imageLoading = (ImageView) itemView.findViewById(R.id.image);
            image_view = (ImageView)itemView.findViewById(R.id.image_view);
            flight_item_layout =(CardView)itemView.findViewById(R.id.flight_item_layout);
        }
    }

    public void setOnClickDoneListener(OnClickDoneListener clickDoneListener) {
        this.onClickDoneListener = clickDoneListener;
    }

    public interface OnClickDoneListener {
        void onEditDone(GetRedPacketListModel model);
    }

}
