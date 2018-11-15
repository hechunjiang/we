package com.sven.huinews.international.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.requst.DisLikeVideoRequest;
import com.sven.huinews.international.entity.response.ShareResponse;
import com.sven.huinews.international.main.web.JsWebView;
import com.sven.huinews.international.main.web.presenter.WebPresenter;

public class DislikeDialog extends Dialog implements JsWebView {
    private Context mContext;
    private ImageView img_dislike;
    private WebPresenter mWebPresenter;
    private DisLikeVideoRequest mDisLikeVideoRequest;
    private LinearLayout ll_back;

    public DislikeDialog(@NonNull Context context) {
        super(context,R.style.my_dialog);
        setContentView(R.layout.dialog_dislike_layout);
        this.mContext = context;
        mWebPresenter = new WebPresenter(this, mContext);
        init();
    }

    public DisLikeVideoRequest getmDisLikeVideoRequest() {
        return mDisLikeVideoRequest;
    }

    public void setmDisLikeVideoRequest(DisLikeVideoRequest mDisLikeVideoRequest) {
        this.mDisLikeVideoRequest = mDisLikeVideoRequest;
    }

    public void init() {
        img_dislike = findViewById(R.id.img_dislike);
        ll_back = findViewById(R.id.ll_back);
        img_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebPresenter.disLikeVideo(mDisLikeVideoRequest);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity=Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBindWxSucceed(int count) {

    }

    @Override
    public void onShareInfo(ShareResponse shareResponse) {

    }

    @Override
    public void onDisLikeVideo() {
        dismiss();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {

    }
}
