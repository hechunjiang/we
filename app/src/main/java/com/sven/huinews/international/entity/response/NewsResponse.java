package com.sven.huinews.international.entity.response;

import com.google.gson.annotations.SerializedName;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.entity.MyNews;

import java.util.ArrayList;

/**
 * Created by Sven on 2018/2/1.
 */

public class NewsResponse extends BaseResponse {
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

    public static class ListData {
        private ArrayList<MyNews> data;

        public ArrayList<MyNews> getData() {
            return data;
        }

        public void setData(ArrayList<MyNews> data) {
            this.data = data;
        }
    }

}
