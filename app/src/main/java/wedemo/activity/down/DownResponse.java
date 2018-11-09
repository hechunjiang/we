package wedemo.activity.down;

import java.util.List;

import wedemo.base.BaseResponse;

public class DownResponse extends BaseResponse {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : FAE50247-F14C-40CE-AD43-29CA3E604838
         * name : Morning Sunlight LUT
         * url : https://meishesdk.meishe-app.com/material/videofx/FAE50247-F14C-40CE-AD43-29CA3E604838.2.videofx
         * cover : https://meishesdk.meishe-app.com/material/videofx/cover/2922C5A1-FA14-7E6B-1B48-9D7F5A55688F.png
         * desc :
         * version : 2
         */

        private String id;
        private String f_id;
        private String name;
        private String url;
        private String cover;
        private String desc;
        private int version;
        private boolean isDown = false; //是否已经下载
        private String themeId;  //主题id
        private boolean isInstall = false;  //是否已经安装
        private boolean onLoad = false;  //是否显示加载框

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public boolean isDown() {
            return isDown;
        }

        public void setDown(boolean down) {
            isDown = down;
        }

        public String getThemeId() {
            return themeId;
        }

        public void setThemeId(String themeId) {
            this.themeId = themeId;
        }

        public boolean isInstall() {
            return isInstall;
        }

        public void setInstall(boolean install) {
            isInstall = install;
        }

        public boolean isOnLoad() {
            return onLoad;
        }

        public void setOnLoad(boolean onLoad) {
            this.onLoad = onLoad;
        }
    }
}
