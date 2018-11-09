package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class TagResponse extends BaseResponse implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * tag : loli
         * tag_name : loli
         */

        private String tag;
        private String tag_name;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "tag='" + tag + '\'' +
                    ", tag_name='" + tag_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TagResponse{" +
                "data=" + data +
                '}';
    }
}
