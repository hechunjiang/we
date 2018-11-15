package wedemo.presenter;//package com.sven.huinews.international.main.shot.presenter;


import com.chinanetcenter.wcs.android.ClientConfig;
import com.chinanetcenter.wcs.android.api.FileUploader;
import com.chinanetcenter.wcs.android.api.ParamsConf;
import com.chinanetcenter.wcs.android.entity.OperationMessage;
import com.chinanetcenter.wcs.android.internal.UploadFileRequest;
import com.chinanetcenter.wcs.android.listener.FileUploaderListener;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;
import com.sven.huinews.international.entity.response.AliyunInfoResponse;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

import wedemo.MessageEvent;
import wedemo.activity.data.PublishInfo;
import wedemo.activity.data.WcsBean;
import wedemo.activity.request.WcsRequest;
import wedemo.contract.PublishContract;
import wedemo.utils.Constants;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.TimelineData;

public class PublishPresenter extends PublishContract.Presenter {
    @Override
    public void onWcsToken(WcsRequest request,final PublishInfo videoInfo) {
        MusicInfo masterMusic = TimelineData.instance().getMasterMusic();
        request.setMusic_id(masterMusic!=null?masterMusic.getId()+"":"");
        mModel.getWcsToken(request,videoInfo,new DataResponseCallback<WcsBean>() {
            @Override
            public void onSucceed(WcsBean dataBean) {
                //先上传图片
                //mView.showErrorTip(100003,"");
                uploadWCS(dataBean, videoInfo);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(),response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onUploadVideoId(VideoUploadRequest request) {
        mModel.uploadVideoId(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                ToastUtils.show(mContext, mContext.getString(R.string.updateok), 1);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }


    //网宿上传
    public void uploadWCS(final WcsBean wcsBean, final PublishInfo videoInfo) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setConnectionTimeout(15 * 1000);//连接超时
        clientConfig.setSocketTimeout(15 * 1000);//socket超时
        clientConfig.setMaxErrorRetry(0);//重试次数

        ParamsConf conf = new ParamsConf();
        conf.mimeType = "image/png";
        conf.keyName = wcsBean.getData().getImage().getFileName();

        //上传参数
        FileUploader.setClientConfig(clientConfig);
        FileUploader.setUploadUrl(wcsBean.getData().getUrl()); //url
        FileUploader.setParams(conf);

        if (wcsBean == null || wcsBean.getData() == null || wcsBean.getData().getImage() == null) {
            mView.setService(wcsBean, videoInfo);
            return;
        }

        if(Constants.UPLOAD_TEST) {
            EventBus.getDefault().post(new MessageEvent("test", "wcsid=" + wcsBean.getData().getWcsid() + "\nurl = " + wcsBean.getData().getUrl()));
            EventBus.getDefault().post(new MessageEvent("test", "\n\n图片上传\n\n token=" + wcsBean.getData().getImage().getToken() + "\n\nimagename=" + wcsBean.getData().getImage().getFileName()));
        }
        mView.showErrorTip(100001,"");
        //上传
        File file = new File(videoInfo.getImagePath());
        FileUploader.upload(mContext, wcsBean.getData().getImage().getToken(), file, null, new FileUploaderListener() {
            @Override
            public void onSuccess(int status, JSONObject responseJson) {

                String coverImg = wcsBean.getData().getUrl() +File.separator +wcsBean.getData().getImage().getFileName();

                if(Constants.UPLOAD_TEST) {
                    EventBus.getDefault().post(new MessageEvent("test", "\n\n上传成功:url = " + coverImg));
                }

                LogUtil.showLog("图片hash ==== " + coverImg);
                videoInfo.setCoverImg(coverImg);
                mView.setService(wcsBean, videoInfo);
            }

            @Override
            public void onFailure(OperationMessage operationMessage) {
                mView.setService(wcsBean, videoInfo);
                LogUtil.showLog("图片错误-------->" + operationMessage.getMessage());

                if(Constants.UPLOAD_TEST) {
                    EventBus.getDefault().post(new MessageEvent("test", "\n\n图片上传失败:result=" + operationMessage.getMessage() + "status:" + operationMessage.getStatus()));
                }
            }

            @Override
            public void onProgress(UploadFileRequest request, final long currentSize, final long totalSize) {
                LogUtil.showLog("图片uploaded---------->" + currentSize + ",total--------->" + totalSize);
                mView.showErrorTip(100002,currentSize * 100 / totalSize+"%");
            }
        });

    }


}
