package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.entity.User;

/**
 * Created by Sven on 2018/1/29.
 */

public class LoginResponse extends BaseResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        @SerializedName("ticket")
        private String loginTicket;
        @SerializedName("user_info")
        private User user;

        @SerializedName("login_flag")
        private boolean isUserLogin;//是否是用户登录

        public String getLoginTicket() {
            return loginTicket;
        }

        public void setLoginTicket(String loginTicket) {
            this.loginTicket = loginTicket;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public boolean isUserLogin() {
            return isUserLogin;
        }

        public void setUserLogin(boolean userLogin) {
            isUserLogin = userLogin;
        }
    }
}
