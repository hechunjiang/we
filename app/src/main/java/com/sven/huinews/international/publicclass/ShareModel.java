package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TaskRequestAdVideo;
import com.sven.huinews.international.entity.requst.VideoReportRequest;
import com.sven.huinews.international.entity.response.TaskResponse;
import com.sven.huinews.international.utils.ToastUtils;

/**
 * 作者：burgess by Burgess on 2018/9/23 02:45
 * 作用：watchEarn
 */
public class ShareModel {
    MyRetrofit mMyRetrofit;
    private Context mContext;
    public ShareModel(Context context) {
        this.mContext = context;
        mMyRetrofit = new MyRetrofit(context);
    }

    public void videoReport(VideoReportRequest request, final DataCallBack dataCacheBack) {
        mMyRetrofit.videoRepost(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }


    public void shareVisit(ShareVisitRequest request, final DataCallBack dataCacheBack) {
        mMyRetrofit.shareVisit(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }

    public void getShareInfo(final DataCallBack dataCacheBack) {
        mMyRetrofit.getShareInfo(new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }


    public void getAdVideoByTask(final TaskRequestAdVideo request, final DataCallBack cacheBack , final int coins , final String type) {
        mMyRetrofit.getAdVideoByTask(request, new DataCallBack() {
            @Override
            public void onComplete() {
                cacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TaskResponse taskResponse = new Gson().fromJson(json, TaskResponse.class);
                if (request.getId().equals(TaskRequest.TASK_ID_READ_SHARE_AD + "")){
                    String message = mContext.getString(R.string.sharing_reward);
                    String addMessage="";
                    addMessage = "+"+10+"" + mContext.getString(R.string.me_coins);
                    ToastUtils.showZDYToast(mContext, addMessage,message);
                }
                cacheBack.onSucceed(taskResponse.getData().getCount()+"");
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                cacheBack.onFail(baseResponse);
            }
        });
    }

}
