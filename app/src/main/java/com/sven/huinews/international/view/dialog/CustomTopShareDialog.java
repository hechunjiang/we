package com.sven.huinews.international.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.Common;

public class CustomTopShareDialog extends Dialog implements View.OnClickListener {

    ImageButton iv_close;
    private Context mContext;
    private TextView toast_share_facebok_zoom_tv, toast_share_twitter_tv;

    public CustomTopShareDialog(@NonNull Context context) {
        super(context, R.style.activity_dialog_anim);
        mContext = context;
        setContentView(R.layout.toast_share);

        toast_share_facebok_zoom_tv = findViewById(R.id.toast_share_facebok_zoom_tv);
        toast_share_twitter_tv = findViewById(R.id.toast_share_twitter_tv);
        iv_close = findViewById(R.id.iv_close);

        toast_share_facebok_zoom_tv.setOnClickListener(this);
        toast_share_twitter_tv.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.gravity = Gravity.TOP;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(mParams);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toast_share_facebok_zoom_tv:
                setShare(Common.SHARE_TYPE_FACEBOOK);
                break;
            case R.id.toast_share_twitter_tv:
                setShare(Common.SHARE_TYPE_TWITTER);
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private void setShare(int type) {
        dismiss();
        if (mOnShareListener != null) {
            mOnShareListener.onShare(type);
        }
    }

    public interface OnShareListener {
        void onShare(int type);
    }

    private OnShareListener mOnShareListener;

    public void setOnShareListener(OnShareListener onShareListener) {
        mOnShareListener = onShareListener;
    }
}
