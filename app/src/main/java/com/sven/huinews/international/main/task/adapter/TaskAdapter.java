package com.sven.huinews.international.main.task.adapter;

import android.graphics.Point;
import android.graphics.Rect;
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
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.main.task.TaskTest;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.LogUtil;


import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TaskAdapter extends BaseSectionQuickAdapter<TaskTest, BaseViewHolder> {
    private String total_num = "";
    private String finish_num = "";

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
                .setText(R.id.tv_gold, "+" + item.t.getTitle_gold())
        ;

        ProgressBar progress_task = helper.getView(R.id.progress_task);
        TextView tv_go = helper.getView(R.id.tv_go);
        TextView tv_go_ad = helper.getView(R.id.tv_go_ad);

        tv_go.setVisibility(View.VISIBLE);
        tv_go_ad.setVisibility(View.GONE);
        if (item.t.getProgress() == 1) {
            progress_task.setVisibility(View.VISIBLE);
            progress_task.setMax(item.t.getTotal_num());
            progress_task.setProgress(item.t.getFinish_num());
            if (item.t.getIs_award() == 0) {
                tv_go.setBackgroundResource(R.mipmap.task_go);
                tv_go.setTextColor(ContextCompat.getColor(mContext, R.color.cfd35));
                if (item.t.getKey_code().equals(Common.AD_EXCITING_VIDEO)) {
                    tv_go.setEnabled(true);
                    tv_go.setText(item.t.getButton());
                } else {
                    tv_go.setEnabled(false);
                    tv_go.setText(item.t.getFinish_num() + "/" + item.t.getTotal_num());
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
            } else if (item.t.getIs_award() > 2) {
                tv_go_ad.setVisibility(View.VISIBLE);
                tv_go.setVisibility(View.GONE);
//                tv_go.setText(formatTimeStrWithUs(item.t.getIs_award() / 1000));
                setTimeDown(tv_go_ad, item.t.getIs_award(), item);
                tv_go_ad.setBackgroundResource(R.mipmap.task_go);
                tv_go_ad.setTextColor(ContextCompat.getColor(mContext, R.color.cfd35));
                tv_go_ad.setEnabled(false);
                total_num = item.t.getTotal_num() + "";
                finish_num = item.t.getFinish_num() + "";
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

    Subscription subscribe = null;

    private void setTimeDown(final TextView tv_time, int time, final TaskTest item) {
        if (time < 0) time = 0;
        time = time / 1000;
        final int downTime = time;
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        int times = downTime - aLong.intValue();
                        item.t.setIs_award(times * 1000);
                        return formatTimeStrWithUs(times);
                    }
                })
                .take(time + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("")) {
                            tv_time.setText(item.t.getButton());
                            tv_time.setBackgroundResource(R.mipmap.task_go);
                            tv_time.setTextColor(ContextCompat.getColor(mContext, R.color.cfd35));
                            tv_time.setEnabled(true);
                            tv_time.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onTaskClickListener != null) {
                                        onTaskClickListener.onClick(item);
                                    }
                                }
                            });
                        } else {
                            Point p = new Point();
                            AppConfig.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(p);
                            Rect rect = new Rect(0, 0, p.x, p.y);
                            if (tv_time.getLocalVisibleRect(rect)) {
                                tv_time.setText(s);
                            }
                        }
                    }
                });
    }


    private String formatTimeStrWithUs(int secTotal) {
        String result = null;
        if (secTotal > 0) {
            int hour = secTotal / 3600;
            int min = (secTotal % 3600) / 60;
            int sec = (secTotal % 3600) % 60;
            result = to2Str(hour) + ":" + to2Str(min) + ":" + to2Str(sec);
        } else {
            result = "";
        }

        return result;
    }

    private String to2Str(int i) {
        if (i > 9) {
            return i + "";
        } else {
            return "0" + i;
        }
    }
}
