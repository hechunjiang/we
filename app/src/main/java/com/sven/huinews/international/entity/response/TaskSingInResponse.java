package com.sven.huinews.international.entity.response;

import java.io.Serializable;

/**
 * Created by Burgess on 2018/9/25 0025.
 */
public class TaskSingInResponse implements Serializable {

    /**
     * code : 200
     * msg : successed
     * data : {"gold_tribute":40,"update_time":"2018-09-25 00:08:25","is":1,"time_difference":14400}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * gold_tribute : 40
         * update_time : 2018-09-25 00:08:25
         * is : 1
         * time_difference : 14400
         */

        private int gold_flag;
        private int gold_tomorrow;


        public int getGold_flag() {
            return gold_flag;
        }

        public void setGold_flag(int gold_flag) {
            this.gold_flag = gold_flag;
        }

        public int getGold_tomorrow() {
            return gold_tomorrow;
        }

        public void setGold_tomorrow(int gold_tomorrow) {
            this.gold_tomorrow = gold_tomorrow;
        }
    }
}
