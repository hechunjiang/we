package com.sven.huinews.international.entity.event;

/**
 * Created by Burgess on 2018/10/11 0011.
 */
public class UpdateEvent {

    public UpdateEvent(int pageType){
        this.pageType = pageType;
    }

    private int pageType;

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }
}
