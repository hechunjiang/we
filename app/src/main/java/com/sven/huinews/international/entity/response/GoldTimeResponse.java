package com.sven.huinews.international.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.sven.huinews.international.base.BaseResponse;

import java.io.Serializable;

/**
 * Created by sfy. on 2018/10/10 0010.
 */

public class GoldTimeResponse extends BaseResponse implements Parcelable{

    /**
     * data : {"gold_tribute":41,"update_time":"2018-10-10 01:08:39","is":1,"time_difference":14379}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        @Override
        public String toString() {
            return "DataBean{" +
                    "gold_tribute=" + gold_tribute +
                    ", update_time='" + update_time + '\'' +
                    ", is=" + is +
                    ", time_difference=" + time_difference +
                    '}';
        }

        /**
         * gold_tribute : 41
         * update_time : 2018-10-10 01:08:39
         * is : 1
         * time_difference : 14379
         */

        private int gold_tribute;
        private String update_time;
        private int is;
        private int time_difference;

        public int getGold_tribute() {
            return gold_tribute;
        }

        public void setGold_tribute(int gold_tribute) {
            this.gold_tribute = gold_tribute;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getIs() {
            return is;
        }

        public void setIs(int is) {
            this.is = is;
        }

        public int getTime_difference() {
            return time_difference;
        }

        public void setTime_difference(int time_difference) {
            this.time_difference = time_difference;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.gold_tribute);
            dest.writeString(this.update_time);
            dest.writeInt(this.is);
            dest.writeInt(this.time_difference);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.gold_tribute = in.readInt();
            this.update_time = in.readString();
            this.is = in.readInt();
            this.time_difference = in.readInt();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    public GoldTimeResponse() {
    }

    protected GoldTimeResponse(Parcel in) {
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Creator<GoldTimeResponse> CREATOR = new Creator<GoldTimeResponse>() {
        @Override
        public GoldTimeResponse createFromParcel(Parcel source) {
            return new GoldTimeResponse(source);
        }

        @Override
        public GoldTimeResponse[] newArray(int size) {
            return new GoldTimeResponse[size];
        }
    };
}
