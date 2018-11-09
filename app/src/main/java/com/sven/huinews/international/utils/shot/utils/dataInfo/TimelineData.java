package com.sven.huinews.international.utils.shot.utils.dataInfo;

import com.meicam.sdk.NvsVideoResolution;

import java.util.ArrayList;

public class TimelineData {
    private static final String TAG = TimelineData.class.getName();
    private static TimelineData m_timelineData;
    NvsVideoResolution m_videoResolution;
    private ArrayList<StickerInfo> m_stickerArray;
    private ArrayList<CaptionInfo> m_captionArray;
    private ArrayList<RecordAudioInfo> m_recordAudioArray;

    private ArrayList<ClipInfo> m_clipInfoArray;
    private VideoClipFxInfo m_videoClipFxInfo;
    private TransitionInfo m_transitionInfo;
    private String m_themeId;

    private float m_musicVolume = 1f;
    private float m_originVideoVolume = 1f;
    private float m_recordVolume = 1f;
    private int m_makeRatio = 1;
    private MusicInfo m_musicData = null;

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
        if(m_videoResolution == null)
            return null;

        NvsVideoResolution resolution = new NvsVideoResolution();
        resolution.imageWidth = m_videoResolution.imageWidth;
        resolution.imageHeight = m_videoResolution.imageHeight;
        return resolution;
    }

    public void setClipInfoData(ArrayList<ClipInfo> clipInfoArray) {
        this.m_clipInfoArray = clipInfoArray;
    }

    public ArrayList<ClipInfo> getClipInfoData() {
        return m_clipInfoArray;
    }

    public ArrayList<ClipInfo> cloneClipInfoData() {
        ArrayList<ClipInfo> newArrayList = new ArrayList<>();
        for(ClipInfo clipInfo:m_clipInfoArray) {
            ClipInfo newClipInfo = clipInfo.clone();
            newArrayList.add(newClipInfo);
        }
        return newArrayList;
    }

    public void resetClipTrimInfo() {
        for(int i = 0;i < m_clipInfoArray.size();i++) {
            ClipInfo clipInfo = m_clipInfoArray.get(i);
            clipInfo.changeTrimIn(-1);
            clipInfo.changeTrimOut(-1);
        }
    }

    public void clear() {
        if(m_clipInfoArray != null) {
            m_clipInfoArray.clear();
        }
        if(m_captionArray != null) {
            m_captionArray.clear();
        }
        if(m_stickerArray != null) {
            m_stickerArray.clear();
        }
        if(m_recordAudioArray != null) {
            m_recordAudioArray.clear();
        }
        m_musicData = null;
        m_musicVolume = 1.0f;
        m_originVideoVolume = 1.0f;
        m_recordVolume = 1.0f;
        m_videoResolution = null;
        m_videoClipFxInfo = null;
        m_transitionInfo = new TransitionInfo();
        m_themeId = "";
    }

    public void addClip(ClipInfo clipInfo) {
        m_clipInfoArray.add(clipInfo);
    }

    public void removeClip(int index) {
        if(index < m_clipInfoArray.size()) {
            m_clipInfoArray.remove(index);
        }
    }

    public int getClipCount() {
        return m_clipInfoArray.size();
    }

    public void setMusicData(MusicInfo musicInfo) {
        m_musicData = musicInfo;
    }

    public MusicInfo getMusicData() {
        return m_musicData;
    }

    public MusicInfo cloneMusicData() {
        if(m_musicData == null)
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
        for(CaptionInfo captionInfo:m_captionArray) {
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
        for(StickerInfo stickerInfo:m_stickerArray) {
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

    private TimelineData() {
        m_captionArray = new ArrayList<>();
        m_stickerArray = new ArrayList<>();
        m_clipInfoArray = new ArrayList<>();
        m_videoResolution = new NvsVideoResolution();
    }

    public static TimelineData init() {
        if (m_timelineData == null) {
            synchronized (TimelineData.class){
                if (m_timelineData == null) {
                    m_timelineData = new TimelineData();
                }
            }
        }
        return m_timelineData;
    }

    public static TimelineData instance() {
        if (m_timelineData == null)
        {
            m_timelineData = new TimelineData();
        }
        return m_timelineData;
    }

}
