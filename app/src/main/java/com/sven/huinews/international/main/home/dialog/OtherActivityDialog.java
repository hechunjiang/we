package com.sven.huinews.international.main.home.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.sven.huinews.international.R;

/**
 * Created by Sven on 2018/3/1.
 */

public class OtherActivityDialog extends Dialog implements View.OnClickListener {
    private ImageView mDialogOtherActivityIv, mDialogOtherActivityCloseIv;

    Handler handler = new Handler();

    public OtherActivityDialog(@NonNull Context context) {
//        super(context);
        super(context, R.style.my_new_dialog);
        setContentView(R.layout.dialog_other_activity);
        mDialogOtherActivityIv = findViewById(R.id.dialog_other_activity_iv);
        mDialogOtherActivityCloseIv = findViewById(R.id.dialog_other_activity_close_iv);
        mDialogOtherActivityIv.setOnClickListener(this);
        mDialogOtherActivityCloseIv.setOnClickListener(this);
    }

    public void showDialog(Drawable drawable) {
        mDialogOtherActivityIv.setImageDrawable(drawable);
        try {
            show();
        } catch (Exception e) {

        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCancelable(true);
                setCanceledOnTouchOutside(true);
            }
        }, 3000);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_other_activity_iv:
                dismiss();
                if (mOnOpenBagListener != null) {
                    mOnOpenBagListener.onOpen(openType);
                }
                break;
            case R.id.dialog_other_activity_close_iv:
                dismiss();
                break;
        }
    }


    public interface OnOpenBagListener {
        void onOpen(int openType);
    }

    private OnOpenBagListener mOnOpenBagListener;

    public void setOnOpenBagListener(OnOpenBagListener onOpenBagListener) {
        mOnOpenBagListener = onOpenBagListener;
    }

    private int openType = -1;
    public void showDialog(Drawable drawable,int type) {
        this.openType = type;
        mDialogOtherActivityIv.setImageDrawable(drawable);
        try {
            show();
        } catch (Exception e) {

        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCancelable(true);
                setCanceledOnTouchOutside(true);
            }
        }, 3000);


    }
}
