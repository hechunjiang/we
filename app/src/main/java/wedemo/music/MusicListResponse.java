package wedemo.music;

import java.io.Serializable;
import java.util.List;

import wedemo.base.BaseResponse;

public class MusicListResponse extends BaseResponse {

    /**
     * code : 200
     * data : [{"id":4,"title":"Shape of You","type_id":3,"music_url":"http://image.hesheng138.com//uploads/audio/20180917/1660ff25fc6e7804849a1ed8d9febb61.mp3","music_cover":"/uploads/images/20180918/4902fe24e35c1d031e007e9133c3fbcf.png","music_singer":"Ed sheeran","music_duration":231,"create_time":1537243224,"use_count":0,"status":"normal","dis_time":0,"order":9999},{"id":5,"title":"That Girl","type_id":3,"music_url":"http://image.hesheng138.com//uploads/audio/20180918/5dd386c8bb7aa1422120701de116cdbf.mp3","music_cover":"http://image.hesheng138.com//uploads/images/20180918/2312225c40328641f05873d5e31df3b5.png","music_singer":"Olly Murs","music_duration":155,"create_time":1537256342,"use_count":0,"status":"normal","dis_time":0,"order":9998},{"id":6,"title":"Samsara","type_id":3,"music_url":"http://image.hesheng138.com/","music_cover":"http://image.hesheng138.com//uploads/images/20180918/be1c6e17b1b9b94fd90296921c8784b5.png","music_singer":"Martin Tungevaag","music_duration":242,"create_time":1537256433,"use_count":0,"status":"normal","dis_time":0,"order":9997}]
     */

    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 4
         * title : Shape of You
         * type_id : 3
         * music_url : http://image.hesheng138.com//uploads/audio/20180917/1660ff25fc6e7804849a1ed8d9febb61.mp3
         * music_cover : /uploads/images/20180918/4902fe24e35c1d031e007e9133c3fbcf.png
         * music_singer : Ed sheeran
         * music_duration : 231
         * create_time : 1537243224
         * use_count : 0
         * status : normal
         * dis_time : 0
         * order : 9999
         */

        private int id;
        private String title;
        private int type_id;
        private String music_url;
        private String music_cover;
        private String music_singer;
        private int music_duration;
        private int create_time;
        private int use_count;
        private String status;
        private int dis_time;
        private int order;
        private String music_path;
        private boolean isPlay;
        private long m_trimIn;
        private long m_trimOut;

        public long getM_trimIn() {
            return m_trimIn;
        }

        public void setM_trimIn(long m_trimIn) {
            this.m_trimIn = m_trimIn;
        }

        public long getM_trimOut() {
            return m_trimOut;
        }

        public void setM_trimOut(long m_trimOut) {
            this.m_trimOut = m_trimOut;
        }

        public String getMusic_path() {
            return music_path;
        }

        public void setMusic_path(String music_path) {
            this.music_path = music_path;
        }

        public boolean isPlay() {
            return isPlay;
        }

        public void setPlay(boolean play) {
            isPlay = play;
        }

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

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getMusic_url() {
            return music_url;
        }

        public void setMusic_url(String music_url) {
            this.music_url = music_url;
        }

        public String getMusic_cover() {
            return music_cover;
        }

        public void setMusic_cover(String music_cover) {
            this.music_cover = music_cover;
        }

        public String getMusic_singer() {
            return music_singer;
        }

        public void setMusic_singer(String music_singer) {
            this.music_singer = music_singer;
        }

        public int getMusic_duration() {
            return music_duration;
        }

        public void setMusic_duration(int music_duration) {
            this.music_duration = music_duration;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUse_count() {
            return use_count;
        }

        public void setUse_count(int use_count) {
            this.use_count = use_count;
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

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}
