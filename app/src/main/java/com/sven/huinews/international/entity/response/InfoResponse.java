package com.sven.huinews.international.entity.response;

import java.util.List;

public class InfoResponse  {


    /**
     * code : 200
     * msg :
     * data : [{"nickname":"burgess","headimg":"http://image.hesheng138.com//uploads/headImage/b125b6b62047cfb92352a670a45934e2.png","total_balance":"2000.37","mail":"7569168581@qq.com","user_ip":"171.221.137.43","country":"../images/country/China.png"},{"nickname":"the.success.ch","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"110.11","mail":"the.success.ch@gmail.com","user_ip":"223.24.158.62","country":"../images/country/Thailand.png"},{"nickname":"Cassandra Marie T. Torres","headimg":"http://abs.twimg.com/sticky/default_profile_images/default_profile.png","total_balance":"56.69","mail":"casstorres3@gmail.com","user_ip":"112.198.67.234","country":"../images/country/Philippines.png"},{"nickname":"kongwirawut","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"32.57","mail":"kongwirawut@gmail.com","user_ip":"223.24.92.167","country":"../images/country/Thailand.png"},{"nickname":"okyaskm","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"25.73","mail":"okyaskm@gmail.com","user_ip":"125.161.107.52","country":"../images/country/Indonesia.png"},{"nickname":"Bonivie","headimg":"http://abs.twimg.com/sticky/default_profile_images/default_profile.png","total_balance":"23.86","mail":"bonivielacar1130@gmail.com","user_ip":"172.105.220.89","country":"../images/country/Japan.png"},{"nickname":"christhianysanchez985","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"23.09","mail":"christhianysanchez985@gmail.com","user_ip":"110.54.231.209","country":"../images/country/Philippines.png"},{"nickname":"manmat83","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"22.51","mail":"manmat83@gmail.com","user_ip":"121.54.54.190","country":"../images/country/Philippines.png"},{"nickname":"1174398728","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"21.14","mail":"1174398728@qq.com","user_ip":"118.113.0.89","country":"../images/country/China.png"},{"nickname":"jian.cruz12","headimg":"http://www.masjmzs.com/static/img/default_head.png","total_balance":"21.05","mail":"jian.cruz12@gmail.com","user_ip":"223.25.8.55","country":"../images/country/Philippines.png"}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nickname : burgess
         * headimg : http://image.hesheng138.com//uploads/headImage/b125b6b62047cfb92352a670a45934e2.png
         * total_balance : 2000.37
         * mail : 7569168581@qq.com
         * user_ip : 171.221.137.43
         * country : ../images/country/China.png
         */

        private String nickname;
        private String headimg;
        private String total_balance;
        private String mail;
        private String user_ip;
        private String country;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getTotal_balance() {
            return total_balance;
        }

        public void setTotal_balance(String total_balance) {
            this.total_balance = total_balance;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getUser_ip() {
            return user_ip;
        }

        public void setUser_ip(String user_ip) {
            this.user_ip = user_ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
