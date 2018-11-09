package com.sven.huinews.international.entity.requst;

public class NvsResListRequest extends BaseRequest {
    private int type;

    private int categroy;
    private int page;
    private int page_size;

    public int getCategroy() {
        return categroy;
    }

    public void setCategroy(int categroy) {
        this.categroy = categroy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



}
