package wedemo.activity.data;

import java.io.Serializable;

public class WcsBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : {"image":{"token":"8a19a2321d51852f75c3a75787933e2b7a6bf544:ZTk1ZTY5NzE3MmQyNDE0NjU4YTlkNWRkMWQ1M2MwNTg3ZWM0ODFiMw==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTIsInJldHVybkJvZHkiOm51bGx9","deadline":1539842376752,"bucket":"watch"},"video":{"token":"8a19a2321d51852f75c3a75787933e2b7a6bf544:MzRkNWI0ZjZiMmFiMjdiN2I0MWEyODk0NmEwOWQ3NjBlYzEzYTNjNQ==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTQsInJldHVybkJvZHkiOm51bGx9","deadline":1539842376754,"bucket":"watch"}}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * image : {"token":"8a19a2321d51852f75c3a75787933e2b7a6bf544:ZTk1ZTY5NzE3MmQyNDE0NjU4YTlkNWRkMWQ1M2MwNTg3ZWM0ODFiMw==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTIsInJldHVybkJvZHkiOm51bGx9","deadline":1539842376752,"bucket":"watch"}
         * video : {"token":"8a19a2321d51852f75c3a75787933e2b7a6bf544:MzRkNWI0ZjZiMmFiMjdiN2I0MWEyODk0NmEwOWQ3NjBlYzEzYTNjNQ==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTQsInJldHVybkJvZHkiOm51bGx9","deadline":1539842376754,"bucket":"watch"}
         */

        private ImageBean image;
        private VideoBean video;
        private String url;
        private String wcsid;
        private String cover_url;

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getWcsid() {
            return wcsid;
        }

        public void setWcsid(String wcsid) {
            this.wcsid = wcsid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public static class ImageBean implements Serializable{
            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            /**
             * token : 8a19a2321d51852f75c3a75787933e2b7a6bf544:ZTk1ZTY5NzE3MmQyNDE0NjU4YTlkNWRkMWQ1M2MwNTg3ZWM0ODFiMw==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTIsInJldHVybkJvZHkiOm51bGx9
             * deadline : 1539842376752
             * bucket : watch
             */

            private String token;
            private long deadline;
            private String bucket;
            private String fileName;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getDeadline() {
                return deadline;
            }

            public void setDeadline(long deadline) {
                this.deadline = deadline;
            }

            public String getBucket() {
                return bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }
        }

        public static class VideoBean implements Serializable{
            /**
             * token : 8a19a2321d51852f75c3a75787933e2b7a6bf544:MzRkNWI0ZjZiMmFiMjdiN2I0MWEyODk0NmEwOWQ3NjBlYzEzYTNjNQ==:eyJzY29wZSI6IndhdGNoIiwiZGVhZGxpbmUiOjE1Mzk4NDIzNzY3NTQsInJldHVybkJvZHkiOm51bGx9
             * deadline : 1539842376754
             * bucket : watch
             */

            private String token;
            private long deadline;
            private String bucket;
            private String fileName;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getDeadline() {
                return deadline;
            }

            public void setDeadline(long deadline) {
                this.deadline = deadline;
            }

            public String getBucket() {
                return bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }
        }
    }
}
