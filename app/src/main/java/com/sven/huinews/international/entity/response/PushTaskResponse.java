package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by Sven on 2018/2/28.
 */

public class PushTaskResponse extends BaseResponse {


    /**
     * code : 200
     * data : {"type":2,"active_push":{"picUrl":"","redirect":""},"pageList":{"home_first":{"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"home_second":{"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"center":{"time":0,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_first":{"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_second":{"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}},"maxId":10}
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
         * type : 2
         * active_push : {"picUrl":"","redirect":""}
         * pageList : {"home_first":{"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"home_second":{"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"center":{"time":0,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_first":{"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_second":{"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}}
         * maxId : 10
         */

        private int type;
        private ActivePushBean active_push;
        private PageListBean pageList;
        private int maxId;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ActivePushBean getActive_push() {
            return active_push;
        }

        public void setActive_push(ActivePushBean active_push) {
            this.active_push = active_push;
        }

        public PageListBean getPageList() {
            return pageList;
        }

        public void setPageList(PageListBean pageList) {
            this.pageList = pageList;
        }

        public int getMaxId() {
            return maxId;
        }

        public void setMaxId(int maxId) {
            this.maxId = maxId;
        }

        public static class ActivePushBean {
            /**
             * picUrl :
             * redirect :
             */

            private String picUrl;
            private String redirect;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getRedirect() {
                return redirect;
            }

            public void setRedirect(String redirect) {
                this.redirect = redirect;
            }
        }

        public static class PageListBean {
            /**
             * home_first : {"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * home_second : {"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * center : {"time":0,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * video_first : {"time":300,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * video_second : {"time":1800,"isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             */

            private HomeFirstBean home_first;
            private HomeSecondBean home_second;
            private CenterBean center;
            private VideoFirstBean video_first;
            private VideoSecondBean video_second;

            public HomeFirstBean getHome_first() {
                return home_first;
            }

            public void setHome_first(HomeFirstBean home_first) {
                this.home_first = home_first;
            }

            public HomeSecondBean getHome_second() {
                return home_second;
            }

            public void setHome_second(HomeSecondBean home_second) {
                this.home_second = home_second;
            }

            public CenterBean getCenter() {
                return center;
            }

            public void setCenter(CenterBean center) {
                this.center = center;
            }

            public VideoFirstBean getVideo_first() {
                return video_first;
            }

            public void setVideo_first(VideoFirstBean video_first) {
                this.video_first = video_first;
            }

            public VideoSecondBean getVideo_second() {
                return video_second;
            }

            public void setVideo_second(VideoSecondBean video_second) {
                this.video_second = video_second;
            }

            public static class HomeFirstBean {
                /**
                 * time : 300
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private int time;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public boolean isIsShow() {
                    return isShow;
                }

                public void setIsShow(boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getRedirect() {
                    return redirect;
                }

                public void setRedirect(String redirect) {
                    this.redirect = redirect;
                }
            }

            public static class HomeSecondBean {
                /**
                 * time : 1800
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private int time;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public boolean isIsShow() {
                    return isShow;
                }

                public void setIsShow(boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getRedirect() {
                    return redirect;
                }

                public void setRedirect(String redirect) {
                    this.redirect = redirect;
                }
            }

            public static class CenterBean {
                /**
                 * time : 0
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private int time;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public boolean isIsShow() {
                    return isShow;
                }

                public void setIsShow(boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getRedirect() {
                    return redirect;
                }

                public void setRedirect(String redirect) {
                    this.redirect = redirect;
                }
            }

            public static class VideoFirstBean {
                /**
                 * time : 300
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private int time;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public boolean isIsShow() {
                    return isShow;
                }

                public void setIsShow(boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getRedirect() {
                    return redirect;
                }

                public void setRedirect(String redirect) {
                    this.redirect = redirect;
                }
            }

            public static class VideoSecondBean {
                /**
                 * time : 1800
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private int time;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public boolean isIsShow() {
                    return isShow;
                }

                public void setIsShow(boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getRedirect() {
                    return redirect;
                }

                public void setRedirect(String redirect) {
                    this.redirect = redirect;
                }
            }
        }
    }
}
