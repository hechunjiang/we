package com.sven.huinews.international.entity.response;

import java.util.List;

public class FeedBackClassifyResponse {


    /**
     * code : 200
     * msg : 操作成功
     * data : [{"id":5,"name":"界面不好看","create_time":1537255920,"status":"normal","order":3},{"id":6,"name":"ACG","create_time":1537255960,"status":"normal","order":1},{"id":7,"name":"有bug","create_time":1540033311,"status":"normal","order":1}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5
         * name : 界面不好看
         * create_time : 1537255920
         * status : normal
         * order : 3
         */

        private int id;
        private String name;
        private int create_time;
        private String status;
        private int order;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}
