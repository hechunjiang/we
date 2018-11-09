package wedemo.utils.dataInfo;

import java.util.ArrayList;
import java.util.List;


public class TimeDataCache  {
    private List<TimelineDataSingle> data;
    private ArrayList<ClipInfo> m_clipInfoArray;
    private MusicInfo mMusicData;
    private int m_makeRatio = 1;
    private long duration = 0;
    private int id = -1;
    private String date;
    private TransitionInfo transitionInfo;

    public TransitionInfo getTransitionInfo() {
        return transitionInfo;
    }

    public void setTransitionInfo(TransitionInfo transitionInfo) {
        this.transitionInfo = transitionInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TimelineDataSingle> getData() {
        return data;
    }

    public void setData(List<TimelineDataSingle> data) {
        this.data = data;
    }

    public ArrayList<ClipInfo> getM_clipInfoArray() {
        return m_clipInfoArray;
    }

    public void setM_clipInfoArray(ArrayList<ClipInfo> m_clipInfoArray) {
        this.m_clipInfoArray = m_clipInfoArray;
    }

    public MusicInfo getmMusicData() {
        return mMusicData;
    }

    public void setmMusicData(MusicInfo mMusicData) {
        this.mMusicData = mMusicData;
    }

    public int getM_makeRatio() {
        return m_makeRatio;
    }

    public void setM_makeRatio(int m_makeRatio) {
        this.m_makeRatio = m_makeRatio;
    }
}
