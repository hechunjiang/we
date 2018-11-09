package wedemo.utils.dataInfo;

import com.meicam.sdk.NvsVideoResolution;

import java.util.ArrayList;
import java.util.HashMap;

import wedemo.shot.bean.FilterItem;

public class TimelineDataSingle {
    private static final String TAG = TimelineDataSingle.class.getName();
    NvsVideoResolution m_videoResolution;
    private ArrayList<StickerInfo> m_stickerArray;
    private ArrayList<CaptionInfo> m_captionArray;
    private ArrayList<RecordAudioInfo> m_recordAudioArray;
    private ArrayList<TimeLineFxInfo> timelineFx;
    private ArrayList<VideoClipFxInfo> videoClipFxInfos;

    private ArrayList<ClipInfo> m_clipInfoArray;
    private VideoClipFxInfo m_videoClipFxInfo;
    private TransitionInfo m_transitionInfo;
    private String m_themeId;

    private float m_musicVolume = 1f;
    private float m_originVideoVolume = 1f;
    private float m_recordVolume = 1f;
    private int m_makeRatio = 1;
    private MusicInfo m_musicData = null;
    private int index;  //时间线数据下标
    private ArrayList<TimeModeInfo> timeModeInfos;
    private int timeMode = 0;//时间特效
    private float resverTime = 0;
    private float slowTime = 0;

    public ArrayList<TimeModeInfo> getTimeModeInfos() {
        return timeModeInfos;
    }

    public void setTimeModeInfos(ArrayList<TimeModeInfo> timeModeInfos) {
        this.timeModeInfos = timeModeInfos;
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

    public ArrayList<TimeModeInfo> cloneTimeModeInfo() {
        ArrayList<TimeModeInfo> newList = new ArrayList<>();
        for (TimeModeInfo timeModeInfo : timeModeInfos) {
            TimeModeInfo newTimeFx = timeModeInfo.clone();
            newList.add(newTimeFx);
        }
        return newList;
    }

    public ArrayList<VideoClipFxInfo> cloneVideoClipFxInfos() {
        ArrayList<VideoClipFxInfo> newList = new ArrayList<>();
        for (VideoClipFxInfo videoClipFxInfo : videoClipFxInfos) {
            VideoClipFxInfo newTimeFx = videoClipFxInfo.clone();
            newList.add(newTimeFx);
        }
        return newList;
    }

    public TimelineDataSingle clone() {

        TimelineDataSingle dataSingle = new TimelineDataSingle();

        dataSingle.setIndex(this.getIndex());
        dataSingle.setMusicData(this.cloneMusicData());
        dataSingle.setMakeRatio(this.getMakeRatio());
        dataSingle.setRecordVolume(this.getRecordVolume());
        dataSingle.setOriginVideoVolume(this.getOriginVideoVolume());
        dataSingle.setMusicVolume(this.getMusicVolume());
        dataSingle.setThemeData(this.getThemeData());
        dataSingle.setTransitionData(this.cloneTransitionData());
        dataSingle.setVideoClipFxData(this.cloneVideoClipFxData());
        dataSingle.setClipInfoData(this.cloneClipInfoData());
        dataSingle.setRecordAudioData(this.getRecordAudioData());
        dataSingle.setCaptionData(this.cloneCaptionData());
        dataSingle.setStickerData(this.cloneStickerData());
        dataSingle.setVideoResolution(this.cloneVideoResolution());
        dataSingle.setTimelineFx(this.cloneTimelineFxInfo());
        dataSingle.setTimeMode(this.timeMode);
        dataSingle.setResverTime(this.resverTime);
        dataSingle.setSlowTime(this.slowTime);
        dataSingle.setTimeModeInfos(this.cloneTimeModeInfo());
        dataSingle.setVideoClipFxInfos(this.cloneVideoClipFxInfos());

        return dataSingle;

    }

    public ArrayList<VideoClipFxInfo> getVideoClipFxInfos() {
        return videoClipFxInfos;
    }

    public void setVideoClipFxInfos(ArrayList<VideoClipFxInfo> videoClipFxInfos) {
        this.videoClipFxInfos = videoClipFxInfos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getMusicVolume() {
        return m_musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.m_musicVolume = musicVolume;
    }

    public float getOriginVideoVolume() {
        return m_originVideoVolume;
    }

    public void setOriginVideoVolume(float originVideoVolume) {
        this.m_originVideoVolume = originVideoVolume;
    }

    public float getRecordVolume() {
        return m_recordVolume;
    }

    public void setRecordVolume(float recordVolume) {
        this.m_recordVolume = recordVolume;
    }

    public int getMakeRatio() {
        return m_makeRatio;
    }

    public void setMakeRatio(int makeRatio) {
        this.m_makeRatio = makeRatio;
    }

    public void setVideoResolution(NvsVideoResolution resolution) {
        m_videoResolution = resolution;
    }

    public NvsVideoResolution getVideoResolution() {
        return m_videoResolution;
    }

    public NvsVideoResolution cloneVideoResolution() {
        if (m_videoResolution == null)
            return null;

        NvsVideoResolution resolution = new NvsVideoResolution();
        resolution.imageWidth = m_videoResolution.imageWidth;
        resolution.imageHeight = m_videoResolution.imageHeight;
        return resolution;
    }

    /**
     * 设置视频片段
     *
     * @param clipInfoArray
     */
    public void setClipInfoData(ArrayList<ClipInfo> clipInfoArray) {
        this.m_clipInfoArray = clipInfoArray;
    }

    public ArrayList<ClipInfo> getClipInfoData() {
        return m_clipInfoArray;
    }

    public ArrayList<ClipInfo> cloneClipInfoData() {
        ArrayList<ClipInfo> newArrayList = new ArrayList<>();
        for (ClipInfo clipInfo : m_clipInfoArray) {
            ClipInfo newClipInfo = clipInfo.clone();
            newArrayList.add(newClipInfo);
        }
        return newArrayList;
    }

    public void resetClipTrimInfo() {
        for (int i = 0; i < m_clipInfoArray.size(); i++) {
            ClipInfo clipInfo = m_clipInfoArray.get(i);
            clipInfo.changeTrimIn(-1);
            clipInfo.changeTrimOut(-1);
        }
    }

    public void clear() {
        if (m_clipInfoArray != null) {
            m_clipInfoArray.clear();
        }
        if (m_captionArray != null) {
            m_captionArray.clear();
        }
        if (m_stickerArray != null) {
            m_stickerArray.clear();
        }
        if (m_recordAudioArray != null) {
            m_recordAudioArray.clear();
        }
        if (timeModeInfos != null) {
            timeModeInfos.clear();
        }
        if (timelineFx != null) {
            timelineFx.clear();
        }
        if (videoClipFxInfos != null) {
            videoClipFxInfos.clear();
        }

        timeMode = 0;
        resverTime = 0;
        slowTime = 0;
        m_musicData = new MusicInfo();

        m_recordVolume = 1.0f;
        m_videoResolution = null;
        m_videoClipFxInfo = new VideoClipFxInfo();
        m_transitionInfo = new TransitionInfo();
        m_themeId = "";
    }

    public void addClip(ClipInfo clipInfo) {
        m_clipInfoArray.add(clipInfo);
    }

    public void removeClip(int index) {
        if (index < m_clipInfoArray.size()) {
            m_clipInfoArray.remove(index);
        }
    }

    public int getClipCount() {
        return m_clipInfoArray.size();
    }

    public ArrayList<TimeLineFxInfo> getTimelineFx() {
        return timelineFx;
    }

    public void setTimelineFx(ArrayList<TimeLineFxInfo> timelineFx) {
        this.timelineFx = timelineFx;
    }


    public ArrayList<TimeLineFxInfo> cloneTimelineFxInfo() {
        ArrayList<TimeLineFxInfo> newList = new ArrayList<>();
        for (TimeLineFxInfo timeLineFxInfo : timelineFx) {
            TimeLineFxInfo newTimeFx = timeLineFxInfo.clone();
            newList.add(newTimeFx);
        }
        return newList;
    }


    public void setMusicData(MusicInfo musicInfo) {
        m_musicData = musicInfo;
    }

    public MusicInfo getMusicData() {
        return m_musicData;
    }

    public MusicInfo cloneMusicData() {
        if (m_musicData == null)
            return null;

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setFileUrl(m_musicData.getFileUrl());
        musicInfo.setFilePath(m_musicData.getFilePath());
        musicInfo.setDuration(m_musicData.getDuration());
        musicInfo.setArtist(m_musicData.getArtist());
        musicInfo.setImage(m_musicData.getImage());
        musicInfo.setImagePath(m_musicData.getImagePath());
        musicInfo.setTitle(m_musicData.getTitle());
        musicInfo.setTrimIn(m_musicData.getTrimIn());
        musicInfo.setTrimOut(m_musicData.getTrimOut());
        musicInfo.setMimeType(m_musicData.getMimeType());
        musicInfo.setIsAsset(m_musicData.isAsset());
        musicInfo.setPrepare(m_musicData.isPrepare());
        musicInfo.setPlay(m_musicData.isPlay());
        musicInfo.setIsHttpMusic(m_musicData.isHttpMusic());
        musicInfo.setAssetPath(m_musicData.getAssetPath());
        musicInfo.setChangem_trimIn(m_musicData.getChangem_trimIn());
        musicInfo.setChangem_trimOut(m_musicData.getChangem_trimOut());

        return musicInfo;
    }

    public void setVideoClipFxData(VideoClipFxInfo videoClipFxInfo) {
        m_videoClipFxInfo = videoClipFxInfo;
    }

    public VideoClipFxInfo getVideoClipFxData() {
        return m_videoClipFxInfo;
    }

    public VideoClipFxInfo cloneVideoClipFxData() {
        if (m_videoClipFxInfo == null)
            return null;

        VideoClipFxInfo videoClipFxInfo = new VideoClipFxInfo();
        videoClipFxInfo.setFxId(m_videoClipFxInfo.getFxId());
        videoClipFxInfo.setFxMode(m_videoClipFxInfo.getFxMode());
        return videoClipFxInfo;
    }

    public void setTransitionData(TransitionInfo transitionInfo) {
        m_transitionInfo = transitionInfo;
    }

    public TransitionInfo getTransitionData() {
        return m_transitionInfo;
    }

    public TransitionInfo cloneTransitionData() {
        if (m_transitionInfo == null)
            return null;

        TransitionInfo transitionInfo = new TransitionInfo();
        transitionInfo.setTransitionId(m_transitionInfo.getTransitionId());
        transitionInfo.setTransitionMode(m_transitionInfo.getTransitionMode());
        //HashMap<String,FilterItem> itemMap = new HashMap<>();

        HashMap<String, FilterItem> map = m_transitionInfo.getItemMap();
        for (HashMap.Entry<String, FilterItem> entry : map.entrySet()) {
            //itemMap.put(entry.getKey(),entry.getValue());
            transitionInfo.setFilterItem(entry.getKey(), entry.getValue());
        }

        return transitionInfo;
    }

    public String getThemeData() {
        return m_themeId;
    }

    public void setThemeData(String themeData) {
        this.m_themeId = themeData;
    }

    public void addCaption(CaptionInfo caption) {
        m_captionArray.add(caption);
    }

    public void setCaptionData(ArrayList<CaptionInfo> captionArray) {
        m_captionArray = captionArray;
    }

    public ArrayList<CaptionInfo> getCaptionData() {
        return m_captionArray;
    }

    public ArrayList<CaptionInfo> cloneCaptionData() {
        ArrayList<CaptionInfo> newList = new ArrayList<>();
        for (CaptionInfo captionInfo : m_captionArray) {
            CaptionInfo newCaptionInfo = captionInfo.clone();
            newList.add(newCaptionInfo);
        }
        return newList;
    }

    public void setStickerData(ArrayList<StickerInfo> stickerArray) {
        m_stickerArray = stickerArray;
    }

    public ArrayList<StickerInfo> getStickerData() {
        return m_stickerArray;
    }

    public ArrayList<StickerInfo> cloneStickerData() {
        ArrayList<StickerInfo> newList = new ArrayList<>();
        for (StickerInfo stickerInfo : m_stickerArray) {
            StickerInfo newStickerInfo = stickerInfo.clone();
            newList.add(newStickerInfo);
        }
        return newList;
    }

    public ArrayList<RecordAudioInfo> getRecordAudioData() {
        return m_recordAudioArray;
    }

    public void setRecordAudioData(ArrayList<RecordAudioInfo> recordAudioArray) {
        this.m_recordAudioArray = recordAudioArray;
    }

    public TimelineDataSingle() {
        m_captionArray = new ArrayList<>();
        m_stickerArray = new ArrayList<>();
        m_clipInfoArray = new ArrayList<>();
        timeModeInfos = new ArrayList<>();
        m_videoResolution = new NvsVideoResolution();
        timelineFx = new ArrayList<>();
        videoClipFxInfos = new ArrayList<>();
    }


}
