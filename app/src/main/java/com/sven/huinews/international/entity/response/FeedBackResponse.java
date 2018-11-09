package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

public class FeedBackResponse extends BaseResponse {


    /**
     * data : {"maxId":6,"list":[{"id":6,"title":"test","type":"","content":"english test","create_time":"2018-09-26 22:46:40","status":"normal","update_time":1538027200,"order":5},{"id":7,"title":"Robert Donnelly","type":"","content":"  Two Men Try to Rape Woman on BeachT\r\nhen a Stray Dog Acts Incredibly.Two Men \r\nTry to Rape Woman on BeachThen a Stray \r\nDog Acts Incredibly.Two Men Try to Rape \r\nWoman on BeachThen a Stray Dog Acts In\r\ncredibly.Two Men Try to Rape Woman on \r\nBeachThen a Stray Dog Acts Incredibly.","create_time":"2018-09-26 22:53:01","status":"normal","update_time":1538027581,"order":1}]}
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
         * type : 程序崩溃
         * content : 程序崩溃了
         * language : en
         * meid : 1234564
         * mobile_info : {"mobileBrand":"","mobileRatio":"","os":"ios","version":""}
         * user_id : 70795
         * ip : 127.0.0.1
         * create_time : 1540017851
         */

        private String type;
        private String content;
        private String language;
        private String meid;
        private String mobile_info;
        private int user_id;
        private String ip;
        private int create_time;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getMeid() {
            return meid;
        }

        public void setMeid(String meid) {
            this.meid = meid;
        }

        public String getMobile_info() {
            return mobile_info;
        }

        public void setMobile_info(String mobile_info) {
            this.mobile_info = mobile_info;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }
    }
}
