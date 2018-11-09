package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by sfy. on 2018/10/15 0015.
 */

public class GetGoldTimeResponse extends BaseResponse {

    /**
     * data : {"gold_tribute":21,"update_time":"2018-10-09 23:25:24","is":1,"time_difference":10945}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetGoldTimeResponse{" +
                "data=" + data +
                '}';
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "gold_tribute=" + gold_tribute +
                    ", update_time='" + update_time + '\'' +
                    ", is=" + is +
                    ", time_difference=" + time_difference +
                    '}';
        }

        /**
         * gold_tribute : 21
         * update_time : 2018-10-09 23:25:24
         * is : 1
         * time_difference : 10945
         */

        private int gold_tribute;
        private String update_time;
        private int is;
        private int time_difference;
        private int count;
        private int gold_time;

        public int getGold_time() {
            return gold_time;
        }

        public void setGold_time(int gold_time) {
            this.gold_time = gold_time;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getGold_tribute() {
            return gold_tribute;
        }

        public void setGold_tribute(int gold_tribute) {
            this.gold_tribute = gold_tribute;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getIs() {
            return is;
        }

        public void setIs(int is) {
            this.is = is;
        }

        public int getTime_difference() {
            return time_difference;
        }

        public void setTime_difference(int time_difference) {
            this.time_difference = time_difference;
        }
    }
}
