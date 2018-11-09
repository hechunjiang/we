package wedemo.utils.dataInfo;

import java.io.Serializable;

public class TimeModeInfo implements Serializable {

    private int timeMode = 0;
    private float resverTime = 0;
    private float slowTime = 0;

    public TimeModeInfo() {
        timeMode = 0;
        resverTime = 0;
        slowTime = 0;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    public float getResverTime() {
        return resverTime;
    }

    public void setResverTime(float resverTime) {
        this.resverTime = resverTime;
    }

    public float getSlowTime() {
        return slowTime;
    }

    public void setSlowTime(float slowTime) {
        this.slowTime = slowTime;
    }

    public TimeModeInfo clone() {
        TimeModeInfo newTimeFx = new TimeModeInfo();
        newTimeFx.setTimeMode(this.getTimeMode());
        newTimeFx.setResverTime(this.getResverTime());
        newTimeFx.setSlowTime(this.getSlowTime());
        return newTimeFx;
    }
}
