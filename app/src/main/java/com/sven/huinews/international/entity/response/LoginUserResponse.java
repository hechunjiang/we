package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by sfy. on 2018/9/27 0027.
 */

public class LoginUserResponse extends BaseResponse {

    /**
     * code : 302
     * data : {"ticket":"sHp2r312otmCpnGns3mCqJhovNiBh4-Wu719sLWlf8uxoIKviZzIko-5hpyzhYafhY232n6KkM-u4Iagv8uHzK2jra6KZM3XjZSKo7-Ik2ubfrCUi4RzoQ","login_flag":true,"user_info":{"id":47207,"c_user_id":28792,"nickname":"test","status":1,"grade_id":0,"sex":2,"headimg":"http://image.hesheng138.com//uploads/headImage/35172de81a3c5e15994bfbeabd4e673f.png","user_father_id":0,"user_grandfather_id":0,"invitation_code":"F95636","birthday":"2018-1-10","lat":0,"lng":0,"gold_flag":376,"total_gold_flag":2776,"frozen_gold_flag":0,"balance":"0.04","total_balance":"0.04","frozen_balance":"0.00","oredstatus":0,"redcash":0,"create_time":1536030741,"is_cross_read_level":0,"paypal_mail":"2654313873@qq.com"}}
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
         * ticket : sHp2r312otmCpnGns3mCqJhovNiBh4-Wu719sLWlf8uxoIKviZzIko-5hpyzhYafhY232n6KkM-u4Iagv8uHzK2jra6KZM3XjZSKo7-Ik2ubfrCUi4RzoQ
         * login_flag : true
         * user_info : {"id":47207,"c_user_id":28792,"nickname":"test","status":1,"grade_id":0,"sex":2,"headimg":"http://image.hesheng138.com//uploads/headImage/35172de81a3c5e15994bfbeabd4e673f.png","user_father_id":0,"user_grandfather_id":0,"invitation_code":"F95636","birthday":"2018-1-10","lat":0,"lng":0,"gold_flag":376,"total_gold_flag":2776,"frozen_gold_flag":0,"balance":"0.04","total_balance":"0.04","frozen_balance":"0.00","oredstatus":0,"redcash":0,"create_time":1536030741,"is_cross_read_level":0,"paypal_mail":"2654313873@qq.com"}
         */

        private String ticket;
        private boolean login_flag;
        private UserInfoBean user_info;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public boolean isLogin_flag() {
            return login_flag;
        }

        public void setLogin_flag(boolean login_flag) {
            this.login_flag = login_flag;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public static class UserInfoBean {
            /**
             * id : 47207
             * c_user_id : 28792
             * nickname : test
             * status : 1
             * grade_id : 0
             * sex : 2
             * headimg : http://image.hesheng138.com//uploads/headImage/35172de81a3c5e15994bfbeabd4e673f.png
             * user_father_id : 0
             * user_grandfather_id : 0
             * invitation_code : F95636
             * birthday : 2018-1-10
             * lat : 0
             * lng : 0
             * gold_flag : 376
             * total_gold_flag : 2776
             * frozen_gold_flag : 0
             * balance : 0.04
             * total_balance : 0.04
             * frozen_balance : 0.00
             * oredstatus : 0
             * redcash : 0
             * create_time : 1536030741
             * is_cross_read_level : 0
             * paypal_mail : 2654313873@qq.com
             */

            private int id;
            private int c_user_id;
            private String nickname;
            private int status;
            private int grade_id;
            private int sex;
            private String headimg;
            private int user_father_id;
            private int user_grandfather_id;
            private String invitation_code;
            private String birthday;
            private int lat;
            private int lng;
            private int gold_flag;
            private int total_gold_flag;
            private int frozen_gold_flag;
            private String balance;
            private String total_balance;
            private String frozen_balance;
            private int oredstatus;
            private int redcash;
            private int create_time;
            private int is_cross_read_level;
            private String paypal_mail;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getC_user_id() {
                return c_user_id;
            }

            public void setC_user_id(int c_user_id) {
                this.c_user_id = c_user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getGrade_id() {
                return grade_id;
            }

            public void setGrade_id(int grade_id) {
                this.grade_id = grade_id;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getHeadimg() {
                return headimg;
            }

            public void setHeadimg(String headimg) {
                this.headimg = headimg;
            }

            public int getUser_father_id() {
                return user_father_id;
            }

            public void setUser_father_id(int user_father_id) {
                this.user_father_id = user_father_id;
            }

            public int getUser_grandfather_id() {
                return user_grandfather_id;
            }

            public void setUser_grandfather_id(int user_grandfather_id) {
                this.user_grandfather_id = user_grandfather_id;
            }

            public String getInvitation_code() {
                return invitation_code;
            }

            public void setInvitation_code(String invitation_code) {
                this.invitation_code = invitation_code;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public int getLat() {
                return lat;
            }

            public void setLat(int lat) {
                this.lat = lat;
            }

            public int getLng() {
                return lng;
            }

            public void setLng(int lng) {
                this.lng = lng;
            }

            public int getGold_flag() {
                return gold_flag;
            }

            public void setGold_flag(int gold_flag) {
                this.gold_flag = gold_flag;
            }

            public int getTotal_gold_flag() {
                return total_gold_flag;
            }

            public void setTotal_gold_flag(int total_gold_flag) {
                this.total_gold_flag = total_gold_flag;
            }

            public int getFrozen_gold_flag() {
                return frozen_gold_flag;
            }

            public void setFrozen_gold_flag(int frozen_gold_flag) {
                this.frozen_gold_flag = frozen_gold_flag;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getTotal_balance() {
                return total_balance;
            }

            public void setTotal_balance(String total_balance) {
                this.total_balance = total_balance;
            }

            public String getFrozen_balance() {
                return frozen_balance;
            }

            public void setFrozen_balance(String frozen_balance) {
                this.frozen_balance = frozen_balance;
            }

            public int getOredstatus() {
                return oredstatus;
            }

            public void setOredstatus(int oredstatus) {
                this.oredstatus = oredstatus;
            }

            public int getRedcash() {
                return redcash;
            }

            public void setRedcash(int redcash) {
                this.redcash = redcash;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getIs_cross_read_level() {
                return is_cross_read_level;
            }

            public void setIs_cross_read_level(int is_cross_read_level) {
                this.is_cross_read_level = is_cross_read_level;
            }

            public String getPaypal_mail() {
                return paypal_mail;
            }

            public void setPaypal_mail(String paypal_mail) {
                this.paypal_mail = paypal_mail;
            }
        }
    }
}
