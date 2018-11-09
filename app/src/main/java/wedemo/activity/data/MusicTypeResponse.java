package wedemo.activity.data;

import java.io.Serializable;
import java.util.List;

import wedemo.base.BaseResponse;

public class MusicTypeResponse extends BaseResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * name : popular
         * create_time : 156478947
         * status : normal
         * dis_time : 0
         * update_time : 1537251669
         */

        private int id;
        private String name;
        private int create_time;
        private String status;
        private int dis_time;
        private int update_time;
        private boolean isSelected;
        private String type_cover;

        public String getType_cover() {
            return type_cover;
        }

        public void setType_cover(String type_cover) {
            this.type_cover = type_cover;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

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

        public int getDis_time() {
            return dis_time;
        }

        public void setDis_time(int dis_time) {
            this.dis_time = dis_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }
    }
}
