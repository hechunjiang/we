package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

import java.util.List;

public class MessageResponse extends BaseResponse {


    /**
     * data : {"maxId":6,"list":[{"id":6,"title":"test","type":"","content":"english test","create_time":"2018-09-26 22:46:40","status":"normal","update_time":1538027200,"order":5},{"id":7,"title":"Robert Donnelly","type":"","content":"  Two Men Try to Rape Woman on BeachT\r\nhen a Stray Dog Acts Incredibly.Two Men \r\nTry to Rape Woman on BeachThen a Stray \r\nDog Acts Incredibly.Two Men Try to Rape \r\nWoman on BeachThen a Stray Dog Acts In\r\ncredibly.Two Men Try to Rape Woman on \r\nBeachThen a Stray Dog Acts Incredibly.","create_time":"2018-09-26 22:53:01","status":"normal","update_time":1538027581,"order":1}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * maxId : 6
         * list : [{"id":6,"title":"test","type":"","content":"english test","create_time":"2018-09-26 22:46:40","status":"normal","update_time":1538027200,"order":5},{"id":7,"title":"Robert Donnelly","type":"","content":"  Two Men Try to Rape Woman on BeachT\r\nhen a Stray Dog Acts Incredibly.Two Men \r\nTry to Rape Woman on BeachThen a Stray \r\nDog Acts Incredibly.Two Men Try to Rape \r\nWoman on BeachThen a Stray Dog Acts In\r\ncredibly.Two Men Try to Rape Woman on \r\nBeachThen a Stray Dog Acts Incredibly.","create_time":"2018-09-26 22:53:01","status":"normal","update_time":1538027581,"order":1}]
         */

        private int maxId;
        private List<ListBean> list;

        public int getMaxId() {
            return maxId;
        }

        public void setMaxId(int maxId) {
            this.maxId = maxId;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 6
             * title : test
             * type :
             * content : english test
             * create_time : 2018-09-26 22:46:40
             * status : normal
             * update_time : 1538027200
             * order : 5
             */

            private int id;
            private String title;
            private String type;
            private String content;
            private String create_time;
            private String status;
            private int update_time;
            private int order;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }
        }
    }
}
