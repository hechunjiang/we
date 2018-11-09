package com.sven.huinews.international.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public class FansAndFollow implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"nickname":"佩琪妙想999","user_avatar":"http://tg.199ho.com//static/img/default_head.png","user_id":24307,"du_type":1},{"nickname":"thatwasamber","user_avatar":"http://ak-cdn.kwai.net/upic/2018/08/21/23/BMjAxODA4MjEyMzAwNTBfMTA1ODkxMzQ3Nl83NzA2MDkxMjQwXzBfMw==_low_B50d114a8641c7c276ce94e9dac198055.webp?tag=1-1534885078-h-0-7rppim6yld-84a2d018f897f4c9","user_id":300,"du_type":2}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * nickname : 佩琪妙想999
         * user_avatar : http://tg.199ho.com//static/img/default_head.png
         * user_id : 24307
         * du_type : 1
         */

        private String nickname;
        private String user_avatar;
        private int user_id;
        private int du_type;
        private int sex;
        private String signature;
        private String birthday;

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getDu_type() {
            return du_type;
        }

        public void setDu_type(int du_type) {
            this.du_type = du_type;
        }
    }
}
