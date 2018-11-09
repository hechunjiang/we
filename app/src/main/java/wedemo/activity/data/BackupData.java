package wedemo.activity.data;

import java.util.ArrayList;

import wedemo.utils.dataInfo.CaptionInfo;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.StickerInfo;

/**
 * Created by admin on 2018/7/11.
 */

public class BackupData {
    private static BackupData mAssetDataBackup;
    private ArrayList<StickerInfo> mStickerArrayList;
    private ArrayList<CaptionInfo> mCaptionArrayList;
    private ArrayList<ClipInfo> mClipInfoArray;
    private int mAnimateStickerZVal;
    private int mCaptionZVal;

    private long m_curSeekTimelinePos = 0;//贴纸和字幕使用
    private long m_stickerDuration = 0;//贴纸专用

    private int m_clipIndex;//片段编辑专用

    private ArrayList<ClipInfo> mAddClipInfoList;//只在EditActivity点击添加视频使用

    public ArrayList<ClipInfo> getAddClipInfoList() {
        return mAddClipInfoList;
    }

    public void setAddClipInfoList(ArrayList<ClipInfo> addClipInfoList) {
        this.mAddClipInfoList = addClipInfoList;
    }
    public void clearAddClipInfoList() {
        mAddClipInfoList.clear();
    }
    public int getClipIndex() {
        return m_clipIndex;
    }

    public void setClipIndex(int clipIndex) {
        this.m_clipIndex = clipIndex;
    }
    public long getStickerDuration() {
        return m_stickerDuration;
    }

    public void setStickerDuration(long stickerDuration) {
        this.m_stickerDuration = stickerDuration;
    }
    public long getCurSeekTimelinePos() {
        return m_curSeekTimelinePos;
    }

    public void setCurSeekTimelinePos(long curSeekTimelinePos) {
        this.m_curSeekTimelinePos = curSeekTimelinePos;
    }

    public int getAnimateStickerZVal() {
        return mAnimateStickerZVal;
    }

    public void setAnimateStickerZVal(int animateStickerZVal) {
        this.mAnimateStickerZVal = animateStickerZVal;
    }

    public int getCaptionZVal() {
        return mCaptionZVal;
    }

    public void setCaptionZVal(int captionZVal) {
        this.mCaptionZVal = captionZVal;
    }

    public void setCaptionData(ArrayList<CaptionInfo> captionArray) {
        mCaptionArrayList = captionArray;
    }
    public ArrayList<CaptionInfo> getCaptionData() {
        return mCaptionArrayList;
    }

    public ArrayList<CaptionInfo> cloneCaptionData() {
        ArrayList<CaptionInfo> newList = new ArrayList<>();
        for(CaptionInfo captionInfo:mCaptionArrayList) {
            CaptionInfo newCaptionInfo = captionInfo.clone();
            newList.add(newCaptionInfo);
        }
        return newList;
    }

    public void clearCaptionData() {
        mCaptionArrayList.clear();
        mCaptionZVal = 0;
    }

    public void setStickerData(ArrayList<StickerInfo> stickerArray) {
        mStickerArrayList = stickerArray;
    }

    public ArrayList<StickerInfo> getStickerData() {
        return mStickerArrayList;
    }

    public ArrayList<StickerInfo> cloneStickerData() {
        ArrayList<StickerInfo> newList = new ArrayList<>();
        for(StickerInfo stickerInfo:mStickerArrayList) {
            StickerInfo newStickerInfo = stickerInfo.clone();
            newList.add(newStickerInfo);
        }
        return newList;
    }

    public void clearStickerData() {
        mStickerArrayList.clear();
        mAnimateStickerZVal = 0;
    }

    public void setClipInfoData(ArrayList<ClipInfo> clipInfoArray) {
        this.mClipInfoArray = clipInfoArray;
    }

    public ArrayList<ClipInfo> getClipInfoData() {
        return mClipInfoArray;
    }

    public ArrayList<ClipInfo> cloneClipInfoData() {
        ArrayList<ClipInfo> newArrayList = new ArrayList<>();
        for(ClipInfo clipInfo:mClipInfoArray) {
            ClipInfo newClipInfo = clipInfo.clone();
            newArrayList.add(newClipInfo);
        }
        return newArrayList;
    }

    public static BackupData init() {
        if (mAssetDataBackup == null) {
            synchronized (BackupData.class){
                if (mAssetDataBackup == null) {
                    mAssetDataBackup = new BackupData();
                }
            }
        }
        return mAssetDataBackup;
    }

    public void clear() {
        clearCaptionData();
        clearStickerData();
        clearAddClipInfoList();
        mAnimateStickerZVal = 0;
        mCaptionZVal = 0;
        m_clipIndex = 0;
        m_curSeekTimelinePos = 0;
        m_stickerDuration = 0;
    }

    public static BackupData instance() {
        if (mAssetDataBackup == null)
            mAssetDataBackup = new BackupData();
        return mAssetDataBackup;
    }
    private BackupData() {
        mStickerArrayList = new ArrayList<>();
        mCaptionArrayList = new ArrayList<>();
        mClipInfoArray = new ArrayList<>();
        mAddClipInfoList = new ArrayList<>();
        mAnimateStickerZVal = 0;
        mCaptionZVal = 0;
        m_clipIndex = 0;
        m_curSeekTimelinePos = 0;
        m_stickerDuration = 0;
    }
}
