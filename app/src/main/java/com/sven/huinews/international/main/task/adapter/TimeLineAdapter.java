package com.sven.huinews.international.main.task.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.TimeLineBean;
import com.sven.huinews.international.entity.response.TaskListResponse;

import java.util.List;

public class TimeLineAdapter extends BaseQuickAdapter<TaskListResponse.DataBeanX.SignBean.Day7Bean,BaseViewHolder> {

    private int day = -1;

    public TimeLineAdapter(@Nullable List<TaskListResponse.DataBeanX.SignBean.Day7Bean> data) {
        super(R.layout.view_time_line, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, TaskListResponse.DataBeanX.SignBean.Day7Bean item) {
        int adapterPosition = helper.getAdapterPosition();
        if(adapterPosition == 0){
            helper.setVisible(R.id.view_left,false);
        }

        if(adapterPosition == 6){
            helper.setVisible(R.id.view_right,false);
        }

        helper.setText(R.id.tv_day,"Day"+item.getDay())
                .setText(R.id.tv_num,item.getGold_tribute()+"");


        if(day == adapterPosition){
            helper.setBackgroundColor(R.id.view_left,ContextCompat.getColor(mContext,R.color.task_bar))
                    .setBackgroundColor(R.id.view_right,ContextCompat.getColor(mContext,R.color.task_un_bar))
                    .setBackgroundRes(R.id.tv_middle,R.drawable.time_line_un_circle)
                    .setBackgroundRes(R.id.tv_num,R.mipmap.task_day);
        }else if(day > adapterPosition){
            helper.setBackgroundColor(R.id.view_left,ContextCompat.getColor(mContext,R.color.task_bar))
                    .setBackgroundColor(R.id.view_right,ContextCompat.getColor(mContext,R.color.task_bar))
                    .setBackgroundRes(R.id.tv_middle,R.drawable.time_line_un_circle)
                    .setBackgroundRes(R.id.tv_num,R.mipmap.task_day);
        }else{
            helper.setBackgroundColor(R.id.view_left,ContextCompat.getColor(mContext,R.color.task_un_bar))
                    .setBackgroundColor(R.id.view_right,ContextCompat.getColor(mContext,R.color.task_un_bar))
                    .setBackgroundRes(R.id.tv_middle,R.drawable.time_line_circle)
                    .setBackgroundRes(R.id.tv_num,R.mipmap.task_un_day);
        }
    }

    /**
     * 设置星期几选中， 0 - 6
     * @param day
     */
    public void setDay(int day){
        if(day < 0){
            day = 0;
        }
        if(day > 6){
            day = 6;
        }
        this.day = day;
        notifyDataSetChanged();
    }
}
