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
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.Common;

public class CustomShareDialog extends Dialog implements View.OnClickListener {

    Button mDialogShareCancelBtn;
    private Context mContext;
    private TextView mDialogShareWechatZoomTv, mDialogShareWechatTv,
            mDialogShareWhatsTv, mDialogShareQqTv, dialog_zhanwei_tv,
            mDialogReportTv, dialog_delete_tv;
    public final static int PERSONAL = 1;//个人
    public final static int OTHER = 2;//其他


    public CustomShareDialog(@NonNull Context context) {
        super(context, R.style.my_dialog);
        mContext = context;
        setContentView(R.layout.dialog_share);

        mDialogShareWechatZoomTv = findViewById(R.id.dialog_share_facebok_zoom_tv);
        mDialogShareWechatTv = findViewById(R.id.dialog_share_twitter_tv);
        mDialogShareWhatsTv = findViewById(R.id.dialog_share_whats_app);
        mDialogShareQqTv = findViewById(R.id.dialog_share_linkedin_tv);
        mDialogReportTv = findViewById(R.id.dialog_report_tv);
        mDialogShareCancelBtn = findViewById(R.id.dialog_share_cancel_btn);
        dialog_delete_tv = findViewById(R.id.dialog_delete_tv);
        dialog_zhanwei_tv = findViewById(R.id.dialog_zhanwei_tv);

        mDialogShareWechatZoomTv.setOnClickListener(this);
        mDialogShareWechatTv.setOnClickListener(this);
        mDialogShareWhatsTv.setOnClickListener(this);
        mDialogShareQqTv.setOnClickListener(this);
        mDialogShareCancelBtn.setOnClickListener(this);
        mDialogReportTv.setOnClickListener(this);
        dialog_delete_tv.setOnClickListener(this);

        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.gravity = Gravity.BOTTOM;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(mParams);
    }

    public void setCopeLink() {
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.dialog_copy_link);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mDialogReportTv.setCompoundDrawables(null, drawable, null, null);
        mDialogReportTv.setText(mContext.getString(R.string.copy_link));
    }

    public void setUploadType() {
        mDialogReportTv.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_share_facebok_zoom_tv:
                setShare(Common.SHARE_TYPE_FACEBOOK);
                break;
            case R.id.dialog_share_twitter_tv:
                setShare(Common.SHARE_TYPE_TWITTER);
                break;
            case R.id.dialog_share_whats_app:
                setShare(Common.SHARE_TYPE_WHATS);
                break;
            case R.id.dialog_share_linkedin_tv:
                setShare(Common.SHARE_TYPE_LINKEDIN);
                break;
            case R.id.dialog_report_tv:
                setShare(Common.SHARE_TYPE_REPORT);
                break;
            case R.id.dialog_share_cancel_btn:
                dismiss();
                break;
            case R.id.dialog_delete_tv:
                setShare(Common.SHARE_TYPE_DELETE);
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

    public void show(int type) {
        switch (type) {
            case PERSONAL:
                dialog_zhanwei_tv.setVisibility(View.GONE);
                dialog_delete_tv.setVisibility(View.VISIBLE);
                break;
            case OTHER:
                dialog_zhanwei_tv.setVisibility(View.VISIBLE);
                dialog_delete_tv.setVisibility(View.GONE);
                break;
        }
        show();
    }

}
