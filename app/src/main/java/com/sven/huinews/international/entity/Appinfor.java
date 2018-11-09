package com.sven.huinews.international.entity;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class Appinfor implements Serializable {
    /**
     * code : 200
     * data : {"URL":[],"NEEDCOUNT":"999","SHARE":[],"GOlD":[],"time":1540287867,"gold_time":30,"CMODEL":1,"IOS":{"DEBUG":1,"VERSION":"1.0.0","CONNECT_VERSION":"2.0.2"}}
     */

    @SerializedName("code")
    private String codeX;
    private DataBean data;

    public String getCodeX() {
        return codeX;
    }

    public void setCodeX(String codeX) {
        this.codeX = codeX;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * URL : []
         * NEEDCOUNT : 999
         * SHARE : []
         * GOlD : []
         * time : 1540287867
         * gold_time : 30
         * CMODEL : 1
         * IOS : {"DEBUG":1,"VERSION":"1.0.0","CONNECT_VERSION":"2.0.2"}
         */

        private String NEEDCOUNT;
        private int time;
        private int gold_time;
        private int CMODEL;
        private IOSBean IOS;
        private List<?> URL;
        private List<?> SHARE;
        private List<?> GOlD;

        public String getNEEDCOUNT() {
            return NEEDCOUNT;
        }

        public void setNEEDCOUNT(String NEEDCOUNT) {
            this.NEEDCOUNT = NEEDCOUNT;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getGold_time() {
            return gold_time;
        }

        public void setGold_time(int gold_time) {
            this.gold_time = gold_time;
        }

        public int getCMODEL() {
            return CMODEL;
        }

        public void setCMODEL(int CMODEL) {
            this.CMODEL = CMODEL;
        }

        public IOSBean getIOS() {
            return IOS;
        }

        public void setIOS(IOSBean IOS) {
            this.IOS = IOS;
        }

        public List<?> getURL() {
            return URL;
        }

        public void setURL(List<?> URL) {
            this.URL = URL;
        }

        public List<?> getSHARE() {
            return SHARE;
        }

        public void setSHARE(List<?> SHARE) {
            this.SHARE = SHARE;
        }

        public List<?> getGOlD() {
            return GOlD;
        }

        public void setGOlD(List<?> GOlD) {
            this.GOlD = GOlD;
        }

        public static class IOSBean {
            /**
             * DEBUG : 1
             * VERSION : 1.0.0
             * CONNECT_VERSION : 2.0.2
             */

            private int DEBUG;
            private String VERSION;
            private String CONNECT_VERSION;

            public int getDEBUG() {
                return DEBUG;
            }

            public void setDEBUG(int DEBUG) {
                this.DEBUG = DEBUG;
            }

            public String getVERSION() {
                return VERSION;
            }

            public void setVERSION(String VERSION) {
                this.VERSION = VERSION;
            }

            public String getCONNECT_VERSION() {
                return CONNECT_VERSION;
            }

            public void setCONNECT_VERSION(String CONNECT_VERSION) {
                this.CONNECT_VERSION = CONNECT_VERSION;
            }
        }
    }
}
