package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.VideoReportRequest;
import com.sven.huinews.international.utils.ToastUtils;

/**
 * 作者：burgess by Burgess on 2018/9/23 02:42
 * 作用：watchEarn
 */
public class ReportPresenter {
    private Context mContext;
    ShareModel mShareModel;

    public ReportPresenter(Context mContext) {
        this.mContext = mContext;
        this.mShareModel = new ShareModel(mContext) ;
    }

    public void videoReport(String id) {
        VideoReportRequest request = new VideoReportRequest();
        request.setVideoId(id);
        mShareModel.videoReport(request, new DataCallBack() {
            @Override
            public void onSucceed(String s) {
                ToastUtils.showLong(mContext,mContext.getString(R.string.report));
            }

            @Override
            public void onFail(BaseResponse response) {
                ToastUtils.showLong(mContext,response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
