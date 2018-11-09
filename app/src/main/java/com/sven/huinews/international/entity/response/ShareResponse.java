package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by W.mago on 2018/9/12 0012.
 */
public class ShareResponse implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"wechat_friend":{"title":"看视频，赚零花，微信提现秒到账，快来和我一起赚零花吧！","content":"【淘视界】新用户专享1元现金，秒提现，填写我的邀请码X21786，首邀2个好友立获23元现金奖励哦~","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]},"wechat_circle":{"title":"观看视频领红包，微信提现秒到账，新人福利乐翻天，大家一起赚多多!","content":"【淘视界】不仅能看喜欢的视频，还能一元提现哦,抓住机会啦。这是我的邀请码X21786, 猛戳这里https://tg.199ho.com/index/openredpacket?id=70726","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/shareFrient4.jpg","https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]},"face_invite":{"title":"观看视频领红包，新人福利乐翻天，微信提现秒到账,大家一起赚多多!","content":"【淘视界】即能看喜欢的视频，还能一元提现哦~！抓住机会啦,这是我的邀请码X21786,猛戳这里https://tg.199ho.com/index/openredpacket?id=70726","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"},"qq_friend":{"title":"观看视频领红包，新人福利乐翻天！","content":"【淘视界】观看视频领红包，新人福利乐翻天，微信提现秒到账，大家一起赚多多，填写我的邀请码X21786","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"},"short_message_invite":{"title":"观看视频领红包，新人福利乐翻天！","content":"【淘视界】看视频还能赚钱？ 赶快扫-码加入，即能看喜欢的视频，还能一元提现哦！抓住机会啦,这是我的邀请码X21786","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"},"default":{"title":"Earn $ by watching rewarded videos","content":"Earn $ by watching rewarded videos ! Earn 5$-50$ a day !\nMin 5 $ withdraw in Paypal !","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]}}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * wechat_friend : {"title":"看视频，赚零花，微信提现秒到账，快来和我一起赚零花吧！","content":"【淘视界】新用户专享1元现金，秒提现，填写我的邀请码X21786，首邀2个好友立获23元现金奖励哦~","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]}
         * wechat_circle : {"title":"观看视频领红包，微信提现秒到账，新人福利乐翻天，大家一起赚多多!","content":"【淘视界】不仅能看喜欢的视频，还能一元提现哦,抓住机会啦。这是我的邀请码X21786, 猛戳这里https://tg.199ho.com/index/openredpacket?id=70726","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/shareFrient4.jpg","https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]}
         * face_invite : {"title":"观看视频领红包，新人福利乐翻天，微信提现秒到账,大家一起赚多多!","content":"【淘视界】即能看喜欢的视频，还能一元提现哦~！抓住机会啦,这是我的邀请码X21786,猛戳这里https://tg.199ho.com/index/openredpacket?id=70726","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"}
         * qq_friend : {"title":"观看视频领红包，新人福利乐翻天！","content":"【淘视界】观看视频领红包，新人福利乐翻天，微信提现秒到账，大家一起赚多多，填写我的邀请码X21786","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"}
         * short_message_invite : {"title":"观看视频领红包，新人福利乐翻天！","content":"【淘视界】看视频还能赚钱？ 赶快扫-码加入，即能看喜欢的视频，还能一元提现哦！抓住机会啦,这是我的邀请码X21786","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726"}
         * default : {"title":"Earn $ by watching rewarded videos","content":"Earn $ by watching rewarded videos ! Earn 5$-50$ a day !\nMin 5 $ withdraw in Paypal !","imgUrl":"https://tg.199ho.com//img/new_taologo.png","url":"https://tg.199ho.com/index/openredpacket?id=70726","imgArray":["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]}
         */

        private WechatFriendBean wechat_friend;
        private WechatCircleBean wechat_circle;
        private FaceInviteBean face_invite;
        private QqFriendBean qq_friend;
        private ShortMessageInviteBean short_message_invite;
        @SerializedName("default")
        private DefaultBean defaultX;

        public WechatFriendBean getWechat_friend() {
            return wechat_friend;
        }

        public void setWechat_friend(WechatFriendBean wechat_friend) {
            this.wechat_friend = wechat_friend;
        }

        public WechatCircleBean getWechat_circle() {
            return wechat_circle;
        }

        public void setWechat_circle(WechatCircleBean wechat_circle) {
            this.wechat_circle = wechat_circle;
        }

        public FaceInviteBean getFace_invite() {
            return face_invite;
        }

        public void setFace_invite(FaceInviteBean face_invite) {
            this.face_invite = face_invite;
        }

        public QqFriendBean getQq_friend() {
            return qq_friend;
        }

        public void setQq_friend(QqFriendBean qq_friend) {
            this.qq_friend = qq_friend;
        }

        public ShortMessageInviteBean getShort_message_invite() {
            return short_message_invite;
        }

        public void setShort_message_invite(ShortMessageInviteBean short_message_invite) {
            this.short_message_invite = short_message_invite;
        }

        public DefaultBean getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(DefaultBean defaultX) {
            this.defaultX = defaultX;
        }

        public static class WechatFriendBean {
            /**
             * title : 看视频，赚零花，微信提现秒到账，快来和我一起赚零花吧！
             * content : 【淘视界】新用户专享1元现金，秒提现，填写我的邀请码X21786，首邀2个好友立获23元现金奖励哦~
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             * imgArray : ["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;
            private List<String> imgArray;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<String> getImgArray() {
                return imgArray;
            }

            public void setImgArray(List<String> imgArray) {
                this.imgArray = imgArray;
            }
        }

        public static class WechatCircleBean {
            /**
             * title : 观看视频领红包，微信提现秒到账，新人福利乐翻天，大家一起赚多多!
             * content : 【淘视界】不仅能看喜欢的视频，还能一元提现哦,抓住机会啦。这是我的邀请码X21786, 猛戳这里https://tg.199ho.com/index/openredpacket?id=70726
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             * imgArray : ["https://tg.199ho.com//qr/shareFrient4.jpg","https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;
            private List<String> imgArray;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<String> getImgArray() {
                return imgArray;
            }

            public void setImgArray(List<String> imgArray) {
                this.imgArray = imgArray;
            }
        }

        public static class FaceInviteBean {
            /**
             * title : 观看视频领红包，新人福利乐翻天，微信提现秒到账,大家一起赚多多!
             * content : 【淘视界】即能看喜欢的视频，还能一元提现哦~！抓住机会啦,这是我的邀请码X21786,猛戳这里https://tg.199ho.com/index/openredpacket?id=70726
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class QqFriendBean {
            /**
             * title : 观看视频领红包，新人福利乐翻天！
             * content : 【淘视界】观看视频领红包，新人福利乐翻天，微信提现秒到账，大家一起赚多多，填写我的邀请码X21786
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ShortMessageInviteBean {
            /**
             * title : 观看视频领红包，新人福利乐翻天！
             * content : 【淘视界】看视频还能赚钱？ 赶快扫-码加入，即能看喜欢的视频，还能一元提现哦！抓住机会啦,这是我的邀请码X21786
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class DefaultBean {
            /**
             * title : Earn $ by watching rewarded videos
             * content : Earn $ by watching rewarded videos ! Earn 5$-50$ a day !
             Min 5 $ withdraw in Paypal !
             * imgUrl : https://tg.199ho.com//img/new_taologo.png
             * url : https://tg.199ho.com/index/openredpacket?id=70726
             * imgArray : ["https://tg.199ho.com//qr/watermark/20180911/5b4cd26127b05ce36d5bb229fb5f4f64.jpg"]
             */

            private String title;
            private String content;
            private String imgUrl;
            private String url;
            private ArrayList<String> imgArray;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public ArrayList<String> getImgArray() {
                return imgArray;
            }

            public void setImgArray(ArrayList<String> imgArray) {
                this.imgArray = imgArray;
            }
        }
    }
}
