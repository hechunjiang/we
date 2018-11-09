package com.sven.huinews.international.main.task.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.task.TaskTest;
import com.sven.huinews.international.utils.LogUtil;


import java.util.List;

public class TaskAdapter extends BaseSectionQuickAdapter<TaskTest, BaseViewHolder> {
    public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    private OnTaskClickListener onTaskClickListener;


    public TaskAdapter(List<TaskTest> data) {
        super(R.layout.item_task, R.layout.item_task_head, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, TaskTest item) {
        helper.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TaskTest item) {
        helper.setText(R.id.tv_title, item.t.getTitle())
                .setText(R.id.tv_content, item.t.getContent())
                .setText(R.id.tv_gold, "+" + item.t.getTitle_gold());

        ProgressBar progress_task = helper.getView(R.id.progress_task);
        TextView tv_go = helper.getView(R.id.tv_go);

        tv_go.setVisibility(View.VISIBLE);
        if (item.t.getProgress() == 1) {
            progress_task.setVisibility(View.VISIBLE);
            progress_task.setMax(item.t.getTotal_num());
            progress_task.setProgress(item.t.getFinish_num());

            if (item.t.getIs_award() == 0) {
                tv_go.setText(item.t.getFinish_num() + "/" + item.t.getTotal_num());
                tv_go.setBackgroundResource(R.mipmap.task_go);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.cfd35));
                tv_go.setEnabled(false);

            } else if (item.t.getIs_award() == 1) {
                tv_go.setText(mContext.getString(R.string.task_go_select));
                tv_go.setBackgroundResource(R.drawable.task_go);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_go.setEnabled(true);

            } else if (item.t.getIs_award() == 2) {
                tv_go.setText(mContext.getString(R.string.task_go_un));
                tv_go.setBackgroundResource(R.drawable.task_go_un);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_go.setEnabled(false);
            } else if (item.t.getIs_award() == -1) {
                tv_go.setVisibility(View.INVISIBLE);
            }

        } else {
            progress_task.setVisibility(View.GONE);

            if (item.t.getIs_award() == 0) {

                tv_go.setBackgroundResource(R.mipmap.task_go);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.cfd35));

                if (TextUtils.isEmpty(item.t.getButton_url())) {
                    tv_go.setEnabled(false);
                    tv_go.setText(item.t.getFinish_num() + "/" + item.t.getTotal_num());
                } else {
                    tv_go.setEnabled(true);
                    tv_go.setText(item.t.getButton());
                }

            } else if (item.t.getIs_award() == 1) {
                tv_go.setText(mContext.getString(R.string.task_go_select));
                tv_go.setBackgroundResource(R.drawable.task_go);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_go.setEnabled(true);
            } else if (item.t.getIs_award() == 2) {
                tv_go.setText(mContext.getString(R.string.task_go_un));
                tv_go.setBackgroundResource(R.drawable.task_go_un);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.bg_white));
                tv_go.setEnabled(false);
            } else if (item.t.getIs_award() == -1) {
                tv_go.setVisibility(View.INVISIBLE);
            }
        }

        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTaskClickListener != null) {
                    onTaskClickListener.onClick(item);
                }
            }
        });

        ImageView iv_task = helper.getView(R.id.iv_task);
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.mipmap.task_header)
                .error(R.mipmap.task_header);


        Glide.with(mContext).load(item.t.getTask_img()).apply(options).into(iv_task);

    }

    public interface OnTaskClickListener {
        void onClick(TaskTest item);
    }
}
