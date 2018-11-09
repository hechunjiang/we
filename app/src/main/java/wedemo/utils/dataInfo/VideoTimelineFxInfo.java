package wedemo.utils.dataInfo;

public class VideoTimelineFxInfo {
    private String name;
    private int index;
    private long fxInPoint;
    private long fxOutPoint;

    public VideoTimelineFxInfo(String name, long inPoint, long outPoint) {
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
}
