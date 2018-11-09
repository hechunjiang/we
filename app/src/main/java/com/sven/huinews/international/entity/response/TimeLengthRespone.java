package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;

public class TimeLengthRespone extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("f_code")
        private String f_code;
        @SerializedName("last")
        private int last;
        @SerializedName("reset")
        private int reset;
        @SerializedName("gold_flag")
        private int gold_flag;

        public int getGold_flag() {
            return gold_flag;
        }

        public void setGold_flag(int gold_flag) {
            this.gold_flag = gold_flag;
        }

        public String getF_code() {
            return f_code;
        }

        public void setF_code(String f_code) {
            this.f_code = f_code;
        }

        public int getLast() {
            return last;
        }

        public void setLast(int last) {
            this.last = last;
        }

        public int getReset() {
            return reset;
        }

        public void setReset(int reset) {
            this.reset = reset;
        }
    }
}
