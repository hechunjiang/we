package com.sven.huinews.international.entity;

import com.sven.huinews.international.base.BaseResponse;

import java.io.Serializable;

/**
 * Created by sfy. on 2018/9/8 0008.
 */

public class UserInfoResponse extends BaseResponse implements Serializable{
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "data=" + data +
                '}';
    }

    public static class UserInfo implements Serializable {
        private UserMsg userMsg;
        private boolean login_flag; //是否临时用户

        public boolean isLogin_flag() {
            return login_flag;
        }

        public void setLogin_flag(boolean login_flag) {
            this.login_flag = login_flag;
        }

        public UserMsg getUserMsg() {
            return userMsg;
        }

        public void setUserMsg(UserMsg userMsg) {
            this.userMsg = userMsg;
        }

        @Override
        public String toString() {
            return "UserInfoResponse{" +
                    "userMsg=" + userMsg +
                    '}';
        }

        public static class UserMsg implements Serializable {


            /**
             * nickname : 798077078
             * headimg : http://www.masjmzs.com/static/img/default_head.png
             * invitation_code : J99648
             * gold_flag : 4461
             * balance : 0.00
             * total_balance : 0.00
             * share_code_status : false
             * ored_status : false
             * redcash_status : false
             * apprentice_status : false
             * friend_num : 0
             * is_bind_wx : false
             * is_has_paypal : false
             * paypal_mail :
             * signature : 个性签名
             * birthday :
             * video_num : 18
             * min_draw : 5
             * max_draw : 5
             * is_hidden_first_mission : false
             */

            private String nickname;
            private String headimg;
            private String invitation_code;
            private int gold_flag;
            private String balance;
            private String total_balance;
            private boolean share_code_status;
            private boolean ored_status;
            private boolean redcash_status;
            private boolean apprentice_status;
            private int friend_num;
            private boolean is_bind_wx;
            private boolean is_has_paypal;
            private String paypal_mail;
            private String signature;
            private String birthday;
            private int video_num;
            private int min_draw;
            private int max_draw;
            private boolean is_hidden_first_mission;

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

            public String getInvitation_code() {
                return invitation_code;
            }

            public void setInvitation_code(String invitation_code) {
                this.invitation_code = invitation_code;
            }

            public int getGold_flag() {
                return gold_flag;
            }

            public void setGold_flag(int gold_flag) {
                this.gold_flag = gold_flag;
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

            public boolean isShare_code_status() {
                return share_code_status;
            }

            public void setShare_code_status(boolean share_code_status) {
                this.share_code_status = share_code_status;
            }

            public boolean isOred_status() {
                return ored_status;
            }

            public void setOred_status(boolean ored_status) {
                this.ored_status = ored_status;
            }

            public boolean isRedcash_status() {
                return redcash_status;
            }

            public void setRedcash_status(boolean redcash_status) {
                this.redcash_status = redcash_status;
            }

            public boolean isApprentice_status() {
                return apprentice_status;
            }

            public void setApprentice_status(boolean apprentice_status) {
                this.apprentice_status = apprentice_status;
            }

            public int getFriend_num() {
                return friend_num;
            }

            public void setFriend_num(int friend_num) {
                this.friend_num = friend_num;
            }

            public boolean isIs_bind_wx() {
                return is_bind_wx;
            }

            public void setIs_bind_wx(boolean is_bind_wx) {
                this.is_bind_wx = is_bind_wx;
            }

            public boolean isIs_has_paypal() {
                return is_has_paypal;
            }

            public void setIs_has_paypal(boolean is_has_paypal) {
                this.is_has_paypal = is_has_paypal;
            }

            public String getPaypal_mail() {
                return paypal_mail;
            }

            public void setPaypal_mail(String paypal_mail) {
                this.paypal_mail = paypal_mail;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public int getVideo_num() {
                return video_num;
            }

            public void setVideo_num(int video_num) {
                this.video_num = video_num;
            }

            public int getMin_draw() {
                return min_draw;
            }

            public void setMin_draw(int min_draw) {
                this.min_draw = min_draw;
            }

            public int getMax_draw() {
                return max_draw;
            }

            public void setMax_draw(int max_draw) {
                this.max_draw = max_draw;
            }

            public boolean isIs_hidden_first_mission() {
                return is_hidden_first_mission;
            }

            public void setIs_hidden_first_mission(boolean is_hidden_first_mission) {
                this.is_hidden_first_mission = is_hidden_first_mission;
            }
        }
    }
}
