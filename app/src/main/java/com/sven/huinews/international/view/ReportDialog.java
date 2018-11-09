package com.sven.huinews.international.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class ReportDialog extends Dialog {
    private Context mContext;
    private TextView btnReport;

    public ReportDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.comment_report_dialog, null);
        this.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.setCanceledOnTouchOutside(true);

        initView(view);
    }

    private void initView(View v) {
        btnReport = v.findViewById(R.id.tv_report);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onReportLisenter != null) {
                    onReportLisenter.report();
                }
            }
        });
    }


    public interface onReportLisenter {
        void report();
    }

    private onReportLisenter onReportLisenter;

    public void setOnReportLisenter(onReportLisenter onReportLisenter) {
        this.onReportLisenter = onReportLisenter;
    }
}
