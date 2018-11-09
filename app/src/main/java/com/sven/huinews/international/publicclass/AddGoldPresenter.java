package com.sven.huinews.international.publicclass;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.GetGoldTimeResponse;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TimeUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.Map;

import wedemo.utils.TimeUtil;

/**
 * 作者：burgess by Burgess on 2018/9/24 02:09
 * 作用：watchEarn
 */
public class AddGoldPresenter {
    private Context mContext;
    private AddGoldModel mAddGoldModel;
    private AddGoldView mAddColdView;
    String pattern = "yyyy-MM-dd HH:mm:ss";

    public AddGoldPresenter(AddGoldView mAddColdView, Context mContext) {
        this.mAddColdView = mAddColdView;
        this.mContext = mContext;
        this.mAddGoldModel = new AddGoldModel(mContext);
    }

    public void readAnyNewsGetGold(final int type, int type_id) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(type + "");
        mAddGoldModel.getGoldByTask(taskRequest, new DataResponseCallback<Map<String, String>>() {
            @Override
            public void onSucceed(Map<String, String> map) {
                if (type==TaskRequest.TASK_ID_OPEN_BOX){
                    mAddColdView.showGoldCome(Integer.parseInt(map.get("Count")), type, map.get("message"));
                }else{
                    mAddColdView.showGoldSignInCome(Integer.parseInt(map.get("Count")), type, map.get("message"));
                }
            }

            @Override
            public void onFail(BaseResponse response) {
                if (response.getCode() == Api.API_CODE_GOLD_FAILD) {
                    mAddColdView.showGoldCome(0, type, "");
                    //Toast提示用户无法获取更多金币
                    ToastUtils.showLong(mContext, response.getMsg());
                } else if (response.getCode() == -1000001) {
                    ToastUtils.showLong(mContext, mContext.getString(R.string.network_unavailable_try_again_later));
                }
            }

            @Override
            public void onComplete() {

            }
        }, 0, null);
    }

    public void getGoldTime() {
        GetboxtimeRequst requst = new GetboxtimeRequst();

        requst.setKey_code("treasure");

        mAddGoldModel.getGoldTime(requst, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
//                2018-10-15 04:37:28    1539592623000 1539592178320
                GetGoldTimeResponse response = new Gson().fromJson(json, GetGoldTimeResponse.class);
                UserSpCache.getInstance(mContext).putInt(UserSpCache.GOLD_NUMBERS,response.getData().getCount());//保存获取金币次数

                UserSpCache.getInstance(mContext).putInt(UserSpCache.GOLD_TIME ,response.getData().getGold_time());
                if (response.getData().getTime_difference() != 0) {
                    mAddColdView.showGoldTime(response.getData().getTime_difference());
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }


}
