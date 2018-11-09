package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by Sven on 2018/2/5.
 */

public class TaskResponse extends BaseResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        @SerializedName("gold_flag")
        private int count;
        @SerializedName("count")
        private int mCount;

        public int getmCount() {
            return mCount;
        }

        public void setmCount(int mCount) {
            this.mCount = mCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
