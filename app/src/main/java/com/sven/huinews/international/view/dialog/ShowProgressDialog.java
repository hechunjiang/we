package com.sven.huinews.international.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sven.huinews.international.R;

public class ShowProgressDialog extends Dialog {
    private static ShowProgressDialog mDialog = null;

    public ShowProgressDialog(@NonNull Context context) {
        super(context);
    }

    public static ShowProgressDialog initDialog(Context mContext) {
        mDialog = new ShowProgressDialog(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.upload_progress, null, false);
        Window window = mDialog.getWindow();//这部分是设置dialog宽高的，宽高是我从sharedpreferences里面获取到的。之前程序启动的时候有获取
        window.getDecorView().setPadding(30, 30, 30, 30);
        mDialog.setContentView(v);
        mDialog.getWindow().getAttributes().gravity = Gravity.LEFT | Gravity.TOP;
        return mDialog;
    }

    public static ShowProgressDialog initGrayDialog(Context mContext) {
        mDialog = new ShowProgressDialog(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.upload_progress, null, false);
        mDialog.setContentView(v);
        mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return mDialog;
    }

    /**
     * 设置提示内容
     *
     * @param strMessage
     * @return
     */
    public ShowProgressDialog setMessage(String strMessage) {
        TextView load_tv = (TextView) mDialog.findViewById(R.id.load_tv);

        if (load_tv != null) {
            load_tv.setText(strMessage);
        }

        return mDialog;
    }

    public void setInfo(String info){
        TextView tv_info = mDialog.findViewById(R.id.tv_info);

        tv_info.setVisibility(View.VISIBLE);
        if (tv_info != null) {
            tv_info.setText(info);
        }
    }
}
