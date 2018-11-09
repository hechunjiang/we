package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.entity.NewsInfo;

import java.util.ArrayList;

public class NewsInfoResponse extends BaseResponse {
    @SerializedName("is_has_more")
    private boolean isHasMore;


    private ListData data;

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public ListData getData() {
        return data;
    }

    public void setData(ListData data) {
        this.data = data;
    }

    public static class  ListData{
        private ArrayList<NewsInfo> data;

        public ArrayList<NewsInfo> getData() {
            return data;
        }

        public void setData(ArrayList<NewsInfo> data) {
            this.data = data;
        }
    }
}
