package com.sven.huinews.international.entity.response;

import java.io.Serializable;

/**
 * Created by W.mago on 2018/5/21 0021.
 */
public class ApprenticePageDataResponse {
    private ApprenticePageData data;

    public ApprenticePageData getData() {
        return data;
    }

    public void setData(ApprenticePageData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApprenticePageDataResponse{" +
                "data=" + data +
                '}';
    }

    public class ApprenticePageData implements Serializable{
        private String invitation_code;
        private int apprentice_total;
        private int disciple_num;
        private int gold_tribute_total;
        private Boolean is_shifu;

        public String getInvitation_code() {
            return invitation_code;
        }

        public void setInvitation_code(String invitation_code) {
            this.invitation_code = invitation_code;
        }

        public int getApprentice_total() {
            return apprentice_total;
        }

        public void setApprentice_total(int apprentice_total) {
            this.apprentice_total = apprentice_total;
        }

        public int getDisciple_num() {
            return disciple_num;
        }

        public void setDisciple_num(int disciple_num) {
            this.disciple_num = disciple_num;
        }

        public int getGold_tribute_total() {
            return gold_tribute_total;
        }

        public void setGold_tribute_total(int gold_tribute_total) {
            this.gold_tribute_total = gold_tribute_total;
        }

        public Boolean getIs_shifu() {
            return is_shifu;
        }

        public void setIs_shifu(Boolean is_shifu) {
            this.is_shifu = is_shifu;
        }

        @Override
        public String toString() {
            return "ApprenticePageData{" +
                    "invitation_code='" + invitation_code + '\'' +
                    ", apprentice_total=" + apprentice_total +
                    ", disciple_num=" + disciple_num +
                    ", gold_tribute_total=" + gold_tribute_total +
                    ", is_shifu=" + is_shifu +
                    '}';
        }
    }
}
