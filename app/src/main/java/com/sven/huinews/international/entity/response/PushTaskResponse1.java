package com.sven.huinews.international.entity.response;

public class PushTaskResponse1 {

    /**
     * code : 200
     * msg : success
     * data : {"type":2,"active_push":{"picUrl":"https://g.tg.199ho.com//images/alert_retail_2.png","redirect":"https://g.tg.199ho.com//personal/joinUs.html"},"pageList":{"home":{"tag":"home","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"home_5_min":{"tag":"home_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"center":{"tag":"center","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video":{"tag":"video","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_5_min":{"tag":"video_5_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_30_min":{"tag":"video_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}}}
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

    public static class DataBean {
        /**
         * type : 2
         * active_push : {"picUrl":"https://g.tg.199ho.com//images/alert_retail_2.png","redirect":"https://g.tg.199ho.com//personal/joinUs.html"}
         * pageList : {"home":{"tag":"home","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"home_5_min":{"tag":"home_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"center":{"tag":"center","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video":{"tag":"video","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_5_min":{"tag":"video_5_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"},"video_30_min":{"tag":"video_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}}
         */

        private int type;
        private ActivePushBean active_push;
        private PageListBean pageList;

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

        public static class ActivePushBean {
            /**
             * picUrl : https://g.tg.199ho.com//images/alert_retail_2.png
             * redirect : https://g.tg.199ho.com//personal/joinUs.html
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
             * home : {"tag":"home","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * home_5_min : {"tag":"home_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * center : {"tag":"center","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * video : {"tag":"video","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * video_5_min : {"tag":"video_5_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             * video_30_min : {"tag":"video_30_min","isShow":true,"picUrl":"https://g.tg.199ho.com//images/notic-img.png","redirect":"https://g.tg.199ho.com//personal/howToEarn.html"}
             */

            private HomeBean home;
            private Home5MinBean home_5_min;
            private CenterBean center;
            private VideoBean video;
            private Video5MinBean video_5_min;
            private Video30MinBean video_30_min;

            public HomeBean getHome() {
                return home;
            }

            public void setHome(HomeBean home) {
                this.home = home;
            }

            public Home5MinBean getHome_5_min() {
                return home_5_min;
            }

            public void setHome_5_min(Home5MinBean home_5_min) {
                this.home_5_min = home_5_min;
            }

            public CenterBean getCenter() {
                return center;
            }

            public void setCenter(CenterBean center) {
                this.center = center;
            }

            public VideoBean getVideo() {
                return video;
            }

            public void setVideo(VideoBean video) {
                this.video = video;
            }

            public Video5MinBean getVideo_5_min() {
                return video_5_min;
            }

            public void setVideo_5_min(Video5MinBean video_5_min) {
                this.video_5_min = video_5_min;
            }

            public Video30MinBean getVideo_30_min() {
                return video_30_min;
            }

            public void setVideo_30_min(Video30MinBean video_30_min) {
                this.video_30_min = video_30_min;
            }

            public static class HomeBean {
                /**
                 * tag : home
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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

            public static class Home5MinBean {
                /**
                 * tag : home_30_min
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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
                 * tag : center
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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

            public static class VideoBean {
                /**
                 * tag : video
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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

            public static class Video5MinBean {
                /**
                 * tag : video_5_min
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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

            public static class Video30MinBean {
                /**
                 * tag : video_30_min
                 * isShow : true
                 * picUrl : https://g.tg.199ho.com//images/notic-img.png
                 * redirect : https://g.tg.199ho.com//personal/howToEarn.html
                 */

                private String tag;
                private boolean isShow;
                private String picUrl;
                private String redirect;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
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
