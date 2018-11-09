package com.sven.huinews.international.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.sven.huinews.international.R;


/**
 * auther: sunfuyi
 * data: 2018/5/30
 * effect:
 */
public class AppUpdateDialog extends Dialog {
    private TextView tvVersion;
    private TextView btn_cancel;
    private TextView btn_enter;
    private TextView tv_update_dis;
    private VersionInfo mVersionInfo;


    public AppUpdateDialog(@NonNull Context context, VersionInfo mVersionInfo) {
        super(context, R.style.dialog);
        this.mVersionInfo = mVersionInfo;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        initView();
    }

    private void initView() {
        tvVersion = findViewById(R.id.tv_version);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_enter = findViewById(R.id.btn_update);
        tv_update_dis = findViewById(R.id.tv_update_dis);
        setCanceledOnTouchOutside(true);


        tvVersion.setText("v" + mVersionInfo.getVersion_name());
      //  tv_update_dis.setText(mVersionInfo.getVersion_msg());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVersionInfo.getMust_update().equals("2")) {
                    if (onUpdateApkLisenter != null) {
                        onUpdateApkLisenter.kill();
                    }
                } else {
                    dismiss();
                }
            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUpdateApkLisenter != null) {
                    onUpdateApkLisenter.update();
                }
                dismiss();
            }
        });
    }


    @Override
    public void show() {
        super.show();
        if (mVersionInfo.getMust_update().equals("2")) {
            setCanceledOnTouchOutside(false);
            setCancelable(false);
        } else {
            setCanceledOnTouchOutside(true);
            setCancelable(true);
        }
    }

    public interface OnUpdateApkLisenter {
        void update();

        void kill();
    }

    private OnUpdateApkLisenter onUpdateApkLisenter;

    public void OnUpdateApkLisenter(OnUpdateApkLisenter onUpdateApkLisenter) {
        this.onUpdateApkLisenter = onUpdateApkLisenter;
    }
}
