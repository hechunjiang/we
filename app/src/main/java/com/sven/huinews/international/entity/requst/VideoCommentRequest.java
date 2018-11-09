package com.sven.huinews.international.entity.requst;

public class VideoCommentRequest extends BaseRequest {
    private String id;
    private String page;
    private String order;
    private String du_type;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDu_type() {
        return du_type;
    }
    public void setDu_type(String du_type) {
        this.du_type = du_type;
    }
}
