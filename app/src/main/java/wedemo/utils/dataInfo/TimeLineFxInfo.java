package wedemo.utils.dataInfo;

/**
 * Created by zd on 2017/6/29.
 */

public class TimeLineFxInfo {

    private String name;
    private int index;
    private long fxInPoint;
    private long fxOutPoint;
    private long masterfxInPoint;
    private long masterfxOutPoint;

    private long changeTrimIn;
    private long changeTrimOut;

    public long getMasterfxInPoint() {
        return masterfxInPoint;
    }

    public void setMasterfxInPoint(long masterfxInPoint) {
        this.masterfxInPoint = masterfxInPoint;
    }

    public long getMasterfxOutPoint() {
        return masterfxOutPoint;
    }

    public void setMasterfxOutPoint(long masterfxOutPoint) {
        this.masterfxOutPoint = masterfxOutPoint;
    }

    public TimeLineFxInfo() {
        this.name = name;
        this.index = index;
    }

    public TimeLineFxInfo(String name, long inPoint, long outPoint) {
        this.name = name;
        this.index = index;
        this.fxInPoint = inPoint;
        this.fxOutPoint = outPoint;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getInPoint() {
        return fxInPoint;
    }

    public void setInPoint(long inPoint) {
        this.fxInPoint = inPoint;
    }

    public long getOutPoint() {
        return fxOutPoint;
    }

    public void setOutPoint(long outPoint) {

        this.fxOutPoint = outPoint;
    }

    public long getChangeTrimIn() {
        return changeTrimIn;
    }

    public void setChangeTrimIn(long changeTrimIn) {
        this.changeTrimIn = changeTrimIn;
    }

    public long getChangeTrimOut() {
        return changeTrimOut;
    }

    public void setChangeTrimOut(long changeTrimOut) {
        this.changeTrimOut = changeTrimOut;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeLineFxInfo clone() {
        TimeLineFxInfo newTimeFx = new TimeLineFxInfo();
        newTimeFx.setName(this.getName());
        newTimeFx.setIndex(this.getIndex());
        newTimeFx.setInPoint(this.getInPoint());
        newTimeFx.setOutPoint(this.getOutPoint());
        newTimeFx.setMasterfxInPoint(this.getMasterfxInPoint());
        newTimeFx.setMasterfxOutPoint(this.getMasterfxOutPoint());
        newTimeFx.setChangeTrimIn(this.getInPoint());
        newTimeFx.setChangeTrimOut(this.getOutPoint());
        return newTimeFx;
    }
}
