package com.sven.huinews.international.entity.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sfy. on 2018/9/17 0017.
 */

public class FollowVideoPlayResponse implements Serializable {

    /**
     * result : true
     * resultData : {"requestId":"ACFC73B2-9C90-4364-BCD9-154E3BFEB86C","playInfoList":[{"width":576,"height":1024,"size":4760846,"playURL":"http://video.7yse.com/2696aeca6e324cdd92bbd7d23b7a5754/34cac31151304a019c15d562503fa679-56f40392347b30117e556fd3cc602bcb-od-S00000001-200000.mp4","bitrate":"4554.199","definition":"OD","duration":"8.3630","format":"mp4","fps":"30","encrypt":0,"streamType":"video","jobId":"ad6f0c0605654454a1be9940b5885e38","preprocessStatus":"UnPreprocess"}],"videoBase":{"coverURL":"http://video.7yse.com/image/cover/BAED2BB0417E454D86441A6655FC1077-6-2.png","duration":"8.34","status":"Normal","title":"1535440184024.mp4","videoId":"2696aeca6e324cdd92bbd7d23b7a5754","mediaType":"video|audio","creationTime":"2018-08-28T07:10:13Z"}}
     * code : 000
     */

    private boolean result;
    private ResultDataBean resultData;
    private String code;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResultDataBean implements Serializable {
        /**
         * requestId : ACFC73B2-9C90-4364-BCD9-154E3BFEB86C
         * playInfoList : [{"width":576,"height":1024,"size":4760846,"playURL":"http://video.7yse.com/2696aeca6e324cdd92bbd7d23b7a5754/34cac31151304a019c15d562503fa679-56f40392347b30117e556fd3cc602bcb-od-S00000001-200000.mp4","bitrate":"4554.199","definition":"OD","duration":"8.3630","format":"mp4","fps":"30","encrypt":0,"streamType":"video","jobId":"ad6f0c0605654454a1be9940b5885e38","preprocessStatus":"UnPreprocess"}]
         * videoBase : {"coverURL":"http://video.7yse.com/image/cover/BAED2BB0417E454D86441A6655FC1077-6-2.png","duration":"8.34","status":"Normal","title":"1535440184024.mp4","videoId":"2696aeca6e324cdd92bbd7d23b7a5754","mediaType":"video|audio","creationTime":"2018-08-28T07:10:13Z"}
         */

        private String requestId;
        private VideoBaseBean videoBase;
        private List<PlayInfoListBean> playInfoList;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public VideoBaseBean getVideoBase() {
            return videoBase;
        }

        public void setVideoBase(VideoBaseBean videoBase) {
            this.videoBase = videoBase;
        }

        public List<PlayInfoListBean> getPlayInfoList() {
            return playInfoList;
        }

        public void setPlayInfoList(List<PlayInfoListBean> playInfoList) {
            this.playInfoList = playInfoList;
        }

        public static class VideoBaseBean {
            /**
             * coverURL : http://video.7yse.com/image/cover/BAED2BB0417E454D86441A6655FC1077-6-2.png
             * duration : 8.34
             * status : Normal
             * title : 1535440184024.mp4
             * videoId : 2696aeca6e324cdd92bbd7d23b7a5754
             * mediaType : video|audio
             * creationTime : 2018-08-28T07:10:13Z
             */

            private String coverURL;
            private String duration;
            private String status;
            private String title;
            private String videoId;
            private String mediaType;
            private String creationTime;

            public String getCoverURL() {
                return coverURL;
            }

            public void setCoverURL(String coverURL) {
                this.coverURL = coverURL;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

            public String getMediaType() {
                return mediaType;
            }

            public void setMediaType(String mediaType) {
                this.mediaType = mediaType;
            }

            public String getCreationTime() {
                return creationTime;
            }

            public void setCreationTime(String creationTime) {
                this.creationTime = creationTime;
            }
        }

        public static class PlayInfoListBean {
            /**
             * width : 576
             * height : 1024
             * size : 4760846
             * playURL : http://video.7yse.com/2696aeca6e324cdd92bbd7d23b7a5754/34cac31151304a019c15d562503fa679-56f40392347b30117e556fd3cc602bcb-od-S00000001-200000.mp4
             * bitrate : 4554.199
             * definition : OD
             * duration : 8.3630
             * format : mp4
             * fps : 30
             * encrypt : 0
             * streamType : video
             * jobId : ad6f0c0605654454a1be9940b5885e38
             * preprocessStatus : UnPreprocess
             */

            private int width;
            private int height;
            private int size;
            private String playURL;
            private String bitrate;
            private String definition;
            private String duration;
            private String format;
            private String fps;
            private int encrypt;
            private String streamType;
            private String jobId;
            private String preprocessStatus;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getPlayURL() {
                return playURL;
            }

            public void setPlayURL(String playURL) {
                this.playURL = playURL;
            }

            public String getBitrate() {
                return bitrate;
            }

            public void setBitrate(String bitrate) {
                this.bitrate = bitrate;
            }

            public String getDefinition() {
                return definition;
            }

            public void setDefinition(String definition) {
                this.definition = definition;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getFps() {
                return fps;
            }

            public void setFps(String fps) {
                this.fps = fps;
            }

            public int getEncrypt() {
                return encrypt;
            }

            public void setEncrypt(int encrypt) {
                this.encrypt = encrypt;
            }

            public String getStreamType() {
                return streamType;
            }

            public void setStreamType(String streamType) {
                this.streamType = streamType;
            }

            public String getJobId() {
                return jobId;
            }

            public void setJobId(String jobId) {
                this.jobId = jobId;
            }

            public String getPreprocessStatus() {
                return preprocessStatus;
            }

            public void setPreprocessStatus(String preprocessStatus) {
                this.preprocessStatus = preprocessStatus;
            }
        }
    }
}
