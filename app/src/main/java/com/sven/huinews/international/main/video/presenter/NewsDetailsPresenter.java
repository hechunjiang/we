package com.sven.huinews.international.main.video.presenter;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.main.video.contract.NewsDetailsContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TaskModel;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.Map;

public class NewsDetailsPresenter extends NewsDetailsContract.Presenter {

    private TaskModel mTaskModel = new TaskModel(AppConfig.getAppContext());

    @Override
    public void newsStatistics(String news_id) {

        NewsStatisticsRequest request = new NewsStatisticsRequest();
        request.setNewsId(news_id);
        mModel.setNewsStatistics(request, new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                LogUtil.showLog("统计成功");
            }

            @Override
            public void onFail(BaseResponse response) {
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public boolean isCanGetCoinByReadNews(Object data) {
        mTaskModel = new TaskModel(AppConfig.getAppContext());
        return mTaskModel.readNewsCanGetCoin(data);
    }

    @Override
    public void readAnyNewsGetGold(final int type,final String type_id) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(type + "");
        if (type == TaskRequest.TASK_ID_READ_AD) {//新闻
            taskRequest.setType_name("news");
        } else if (type == TaskRequest.TASK_ID_READ_NEWS) { //视频
            taskRequest.setType_name("video");
        }
        taskRequest.setType_id(type_id);//页面id
        mModel.getGoldByTask(taskRequest, new DataResponseCallback<Map<String, String>>() {
            @Override
            public void onSucceed(Map<String, String> map) {
                mView.showGoldCome(Integer.parseInt(map.get("Count")), type, map.get("message"));
            }

            @Override
            public void onFail(BaseResponse response) {
                if (response.getCode() == Api.API_CODE_GOLD_FAILD) {
                    mView.showGoldCome(0,type, "");
                    //Toast提示用户无法获取更多金币
                    ToastUtils.showShort(mContext, response.getMsg());
                } else if (response.getCode() == -1000001) {
                    mView.showNetWorkError();
                    ToastUtils.showShort(mContext, mContext.getString(R.string.network_unavailable_try_again_later));
                }
            }

            @Override
            public void onComplete() {

            }
        }, 0, "null");
    }

    @Override
    public void addReadNews(Object data) {
        String id = "";
        if (data instanceof MyNews) {
            MyNews myNews = (MyNews) data;
            id = myNews.getId();
        } else if (data instanceof NewsInfo) {
            NewsInfo newsInfo = (NewsInfo) data;
            id = newsInfo.getId() + "";
        }
        mTaskModel.addReadNews(id);

    }

    @Override
    public void saveGoldOpenCount() {
        int count = UserSpCache.getInstance(mContext).getInt(UserSpCache.OPEN_GOLD_COUNT);
        count++;
        LogUtil.showLog("msg--- count：" + count);
        UserSpCache.getInstance(mContext).putInt(UserSpCache.OPEN_GOLD_COUNT, count);
    }
}
