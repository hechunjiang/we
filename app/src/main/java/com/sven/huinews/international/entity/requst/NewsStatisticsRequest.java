package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

public class NewsStatisticsRequest extends BaseRequest {
    @SerializedName("news_id")
    private String newsId;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

}
