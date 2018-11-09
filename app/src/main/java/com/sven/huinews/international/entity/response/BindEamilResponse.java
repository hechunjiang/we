package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by sfy. on 2018/11/1 0001.
 */

public class BindEamilResponse extends BaseResponse {

    /**
     * data : {"gold":100}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gold : 100
         */

        private int gold;

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }
    }
}
