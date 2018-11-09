package wedemo.utils;

import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;

import java.util.ArrayList;
import java.util.List;

import wedemo.music.MusicListResponse;
import wedemo.shot.bean.FilterItem;
import wedemo.utils.dataInfo.CaptionInfo;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.RecordAudioInfo;
import wedemo.utils.dataInfo.StickerInfo;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.TimelineDataSingle;
import wedemo.utils.dataInfo.TransitionInfo;
import wedemo.utils.dataInfo.VideoClipFxInfo;

/**
 * Created by admin on 2018/5/29.
 */

public class TimelineUtil {
    private static String TAG = "TimelineUtil";
    public static long TIME_BASE = 1000000;

    public static int TIME_MASTER = 10001;
    public static int TIME_BRANCH = 10002;

    /**
     * 检查分支
     *
     * @param list
     * @return
     */
    public static boolean checkSingleTimeLine(List<CustomTimeLine> list) {
        if (list == null || list.size() == 0) {
            return false;
        }

        for (CustomTimeLine timelineda : list) {
            if (timelineda == null) {
                return false;
            }
            NvsTimeline timeline = timelineda.getTimeline();
            NvsVideoTrack mVideoTrack = timeline.getVideoTrackByIndex(0);
            if (mVideoTrack == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 追加timeline
     *
     * @return
     */
    public static List<NvsTimeline> addSingleTimelineList(int count) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<NvsTimeline> list = new ArrayList<>();
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = videoClipArray.size(); i < count + videoClipArray.size(); i++) {
            NvsTimeline timeline = createTimeline(i);
            list.add(timeline);
        }

        return list;
    }

    /**
     * 为每个视频创建timeline
     *
     * @return
     */
    public static List<CustomTimeLine> createSingleTimelineList() {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<CustomTimeLine> list = new ArrayList<>();

        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0; i < videoClipArray.size(); i++) {
            NvsTimeline timeline = createTimeline(i);
            TimelineDataSingle timelineDataSingle = new TimelineDataSingle();

            CustomTimeLine customTimeLine = new CustomTimeLine();
            customTimeLine.setTimeData(timelineDataSingle);
            customTimeLine.setTimeline(timeline);

            list.add(customTimeLine);
        }

        return list;
    }

    /**
     * 创建时间数据
     *
     * @return
     */
    public static List<TimelineDataSingle> createTimeData() {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<TimelineDataSingle> list = new ArrayList<>();

        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0; i < videoClipArray.size(); i++) {
            TimelineDataSingle timeline = new TimelineDataSingle();
            list.add(timeline);
        }

        return list;
    }


    public static List<CustomTimeLine> createReBuildSingleTimelineList(int count, int pos) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        rbuildData(count, pos);

        List<CustomTimeLine> list = new ArrayList<>();

        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0; i < videoClipArray.size(); i++) {
            NvsTimeline timeline = createTimeline(i);
            TimelineDataSingle dataSingle = new TimelineDataSingle();

            CustomTimeLine customTimeLine = new CustomTimeLine();
            customTimeLine.setTimeline(timeline);
            customTimeLine.setTimeData(dataSingle);

            list.add(customTimeLine);
        }

        return list;
    }

    public static List<CustomTimeLine> createReBuildSingleTimelineList1(List<ClipInfo> videoClipArray, int count, int pos) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<CustomTimeLine> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            NvsTimeline timeline = createTimelineNoData(videoClipArray, i);
            TimelineDataSingle dataSingle = new TimelineDataSingle();

            CustomTimeLine customTimeLine = new CustomTimeLine();
            customTimeLine.setTimeData(dataSingle);
            customTimeLine.setTimeline(timeline);

            list.add(customTimeLine);
        }
        return list;
    }

    /**
     * 数据下标重置
     * 添加数量
     */
    public static void rbuildData(int count, int pos) {

        MusicInfo musicInfoClone = TimelineData.instance().cloneMusicData();
        if (musicInfoClone != null) {
            TimelineData.instance().setMusicData(musicInfoClone);
        }
        String themeId = TimelineData.instance().getThemeData();
        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
        ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();

        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        for (int i = 0; i < singleTimelineList.size(); i++) {
            if (pos + count > i) {
                continue;
            }
            for (StickerInfo sticker : stickerArray) {
                if (sticker.getIndex() == (i - count)) {
                    sticker.setMasterInPoint(TimelineManager.getInstance().getPreviousAllTime(i));
                    sticker.setIndex(count + sticker.getIndex());
                }
            }

            for (CaptionInfo captionInfo : captionArray) {
                if (captionInfo.getIndex() == (i - count)) {
                    captionInfo.setMasterInPoint(TimelineManager.getInstance().getPreviousAllTime(i));
                    captionInfo.setIndex(count + captionInfo.getIndex());
                }
            }
        }

    }


    /**
     * -------------------单线视频---------------
     */
    public static NvsTimeline createTimeline(int pos) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }

        if (!buildVideoTrack(timeline, pos)) {
            return timeline;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        setTimelineData(timeline, TIME_BRANCH, pos);

        return timeline;
    }


    /**
     * 创建无数据时间线
     *
     * @param videoClipArray
     * @param pos
     * @return
     */
    public static NvsTimeline createTimelineNoData(List<ClipInfo> videoClipArray, int pos) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }

        if (!buildVideoTrack(videoClipArray, timeline, pos)) {
            return timeline;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        return timeline;
    }

    /**
     * 重置所有分支时间线
     *
     * @return
     */
    public static void reBuildAllBranchVideoTrack() {
        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        for (int i = 0; i < singleTimelineList.size(); i++) {
            reBuildVideoTrack(singleTimelineList.get(i).getTimeline(), i);
        }
    }

    // 重新添加分支视频线
    public static boolean reBuildVideoTrack(NvsTimeline timeline, int pos) {
        if (timeline == null) {
            return false;
        }
        int videoTrackCount = timeline.videoTrackCount();
        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        videoTrack.removeAllClips();
        addVideoClip(videoTrack, pos);
        setTimelineData(timeline, TIME_BRANCH, pos);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }


    /**
     * 根据位置选择
     *
     * @param timeline
     * @param pos
     * @return
     */
    public static boolean buildVideoTrack(NvsTimeline timeline, int pos) {
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack, pos);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    /**
     * 只创建时间线，不设置数据
     *
     * @param videoClipArray
     * @param timeline
     * @param pos
     * @return
     */
    public static boolean buildVideoTrack(List<ClipInfo> videoClipArray, NvsTimeline timeline, int pos) {
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoClipArray, videoTrack, pos);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    private static void addVideoClip(List<ClipInfo> videoClipArray, NvsVideoTrack videoTrack, int pos) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();

        //通过素材文件路径添加素材，可以是图片或者视频
        ClipInfo clipInfo = videoClipArray.get(pos);
        String filePath = clipInfo.getFilePath();
         /*
         添加片段到轨道
        */
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null)
            return;

        if (blurFlag) {
            videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
        }
        int videoType = videoClip.getVideoType();
        if (videoType == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE) {//当前片段是图片
            long trimIn = videoClip.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if (trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }
            int imgDisplayMode = clipInfo.getImgDispalyMode();
            if (imgDisplayMode == Constants.EDIT_MODE_PHOTO_AREA_DISPLAY) {//区域显示
                videoClip.setImageMotionMode(NvsVideoClip.IMAGE_CLIP_MOTIONMMODE_ROI);
                RectF normalStartRectF = clipInfo.getNormalStartROI();
                RectF normalEndRectF = clipInfo.getNormalEndROI();
                if (normalStartRectF != null && normalEndRectF != null) {
                    videoClip.setImageMotionROI(normalStartRectF, normalEndRectF);
                }
            } else {//全图显示
                videoClip.setImageMotionMode(NvsVideoClip.CLIP_MOTIONMODE_LETTERBOX_ZOOMIN);
            }

            boolean isOpenMove = clipInfo.isOpenPhotoMove();
            videoClip.setImageMotionAnimationEnabled(isOpenMove);
        } else {//当前片段是视频
            float speed = clipInfo.getSpeed();
            videoClip.changeSpeed(speed);

            long trimIn = clipInfo.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if (trimIn > 0) {
                videoClip.changeTrimInPoint(trimIn, true);
            }

            if (trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }

            float volumeGain = clipInfo.getVolume();
            videoClip.setVolumeGain(volumeGain, volumeGain);
            videoClip.setExtraVideoRotation(clipInfo.getRotateAngle());
            NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx("Transform 2D");
            if (videoFxTransform != null) {
                videoFxTransform.setFloatVal("Scale X", clipInfo.getScaleX());
                videoFxTransform.setFloatVal("Scale Y", clipInfo.getScaleY());
            }
        }

        NvsVideoFx videoFxColor = videoClip.appendBuiltinFx("Color Property");
        if (videoFxColor != null) {
            videoFxColor.setFloatVal("Brightness", clipInfo.getBrightnessVal());
            videoFxColor.setFloatVal("Contrast", clipInfo.getContrastVal());
            videoFxColor.setFloatVal("Saturation", clipInfo.getSaturationVal());
        }
    }


    private static void addVideoClip(NvsVideoTrack videoTrack, int pos) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();

        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        ClipInfo clipInfo = videoClipArray.get(pos);
        String filePath = clipInfo.getFilePath();
         /*
         添加片段到轨道
        */
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null)
            return;

        if (blurFlag) {
            videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
        }
        int videoType = videoClip.getVideoType();
        if (videoType == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE) {//当前片段是图片
            long trimIn = videoClip.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if (trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }
            int imgDisplayMode = clipInfo.getImgDispalyMode();
            if (imgDisplayMode == Constants.EDIT_MODE_PHOTO_AREA_DISPLAY) {//区域显示
                videoClip.setImageMotionMode(NvsVideoClip.IMAGE_CLIP_MOTIONMMODE_ROI);
                RectF normalStartRectF = clipInfo.getNormalStartROI();
                RectF normalEndRectF = clipInfo.getNormalEndROI();
                if (normalStartRectF != null && normalEndRectF != null) {
                    videoClip.setImageMotionROI(normalStartRectF, normalEndRectF);
                }
            } else {//全图显示
                videoClip.setImageMotionMode(NvsVideoClip.CLIP_MOTIONMODE_LETTERBOX_ZOOMIN);
            }

            boolean isOpenMove = clipInfo.isOpenPhotoMove();
            videoClip.setImageMotionAnimationEnabled(isOpenMove);
        } else {//当前片段是视频
            float speed = clipInfo.getSpeed();
            videoClip.changeSpeed(speed);

            long trimIn = clipInfo.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if (trimIn > 0) {
                videoClip.changeTrimInPoint(trimIn, true);
            }

            if (trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }

            float volumeGain = clipInfo.getVolume();
            videoClip.setVolumeGain(volumeGain, volumeGain);
            videoClip.setExtraVideoRotation(clipInfo.getRotateAngle());
            NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx("Transform 2D");
            if (videoFxTransform != null) {
                videoFxTransform.setFloatVal("Scale X", clipInfo.getScaleX());
                videoFxTransform.setFloatVal("Scale Y", clipInfo.getScaleY());
            }
        }

        NvsVideoFx videoFxColor = videoClip.appendBuiltinFx("Color Property");
        if (videoFxColor != null) {
            videoFxColor.setFloatVal("Brightness", clipInfo.getBrightnessVal());
            videoFxColor.setFloatVal("Contrast", clipInfo.getContrastVal());
            videoFxColor.setFloatVal("Saturation", clipInfo.getSaturationVal());
        }
    }


    /**
     * -----------------合并视频线------------------
     */
    public static NvsTimeline createTimeline() {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }

        if (!buildVideoTrack(timeline)) {
            return timeline;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        setTimelineData(timeline, TIME_MASTER);

        return timeline;
    }


    //TODO:此处需要根据不同的timeline获取不同的数据
    public static void setTimelineData(NvsTimeline timeline, int type, int index) {
        if (timeline == null)
            return;
        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
        MusicInfo musicInfoClone = TimelineData.instance().cloneMusicData();
        String themeId = TimelineData.instance().getThemeData();
        applyTheme(timeline, themeId);

        if (musicInfoClone != null) {
            TimelineData.instance().setMusicData(musicInfoClone);
            buildTimelineMusic(timeline, musicInfoClone);
        }

        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
        buildTimelineFilter(timeline, videoClipFxData);

        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
        setTransition(timeline, transitionInfo);

        ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
        setSticker(timeline, stickerArray, type, index);

        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        setCaption(timeline, captionArray, type, index);

        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();
        buildTimelineRecordAudio(timeline, recordArray);
    }

    public static void setTimelineCopyData(CustomTimeLine customTimeLine, int type, int index) {
        if (customTimeLine == null)
            return;
        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
        MusicInfo musicInfoClone = customTimeLine.getTimeData().cloneMusicData();
        String themeId = customTimeLine.getTimeData().getThemeData();
        applyTheme(customTimeLine.getTimeline(), themeId);

        if (musicInfoClone != null) {
            customTimeLine.getTimeData().setMusicData(musicInfoClone);
            buildTimelineMusic(customTimeLine.getTimeline(), musicInfoClone);
        }

        VideoClipFxInfo videoClipFxData = customTimeLine.getTimeData().getVideoClipFxData();
        buildTimelineFilter(customTimeLine.getTimeline(), videoClipFxData);

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        setTransition(customTimeLine.getTimeline(), transitionInfo);

        ArrayList<StickerInfo> stickerArray = customTimeLine.getTimeData().getStickerData();
        setSticker(customTimeLine.getTimeline(), stickerArray, type, index);

        ArrayList<CaptionInfo> captionArray = customTimeLine.getTimeData().getCaptionData();
        setCaption(customTimeLine.getTimeline(), captionArray, type, index);

        ArrayList<RecordAudioInfo> recordArray = customTimeLine.getTimeData().getRecordAudioData();
        buildTimelineRecordAudio(customTimeLine.getTimeline(), recordArray);
    }

    public static void setTimelineData(NvsTimeline timeline, int type) {
        if (timeline == null)
            return;
        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
        MusicInfo musicInfoClone = TimelineData.instance().cloneMusicData();
        String themeId = TimelineData.instance().getThemeData();
        applyTheme(timeline, themeId);

        if (musicInfoClone != null) {
            TimelineData.instance().setMusicData(musicInfoClone);
            buildTimelineMusic(timeline, musicInfoClone);
        }

        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
        buildTimelineFilter(timeline, videoClipFxData);
        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
        setTransition(timeline, transitionInfo);
        ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
        setSticker(timeline, stickerArray, type);

        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        setCaption(timeline, captionArray, type);

        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();
        buildTimelineRecordAudio(timeline, recordArray);
    }

    public static boolean removeTimeline(NvsTimeline timeline) {
        if (timeline == null)
            return false;

        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null)
            return false;

        return context.removeTimeline(timeline);
    }

    //重置视频线
    public static boolean buildVideoTrack(NvsTimeline timeline) {
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    // 重新添加视频线
    public static boolean reBuildVideoTrack(NvsTimeline timeline) {
        if (timeline == null) {
            return false;
        }
        int videoTrackCount = timeline.videoTrackCount();
        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        videoTrack.removeAllClips();
        addVideoClip(videoTrack);
        setTimelineData(timeline, TIME_MASTER);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    private static void addVideoClip(NvsVideoTrack videoTrack) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();

        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0; i < videoClipArray.size(); i++) {
            ClipInfo clipInfo = videoClipArray.get(i);
            String filePath = clipInfo.getFilePath();
             /*
             添加片段到轨道
            */
            NvsVideoClip videoClip = videoTrack.appendClip(filePath);
            if (videoClip == null)
                continue;

            if (blurFlag) {
                videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
            }
            int videoType = videoClip.getVideoType();
            if (videoType == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE) {//当前片段是图片
                long trimIn = videoClip.getTrimIn();
                long trimOut = clipInfo.getTrimOut();
                if (trimOut > 0 && trimOut > trimIn) {
                    videoClip.changeTrimOutPoint(trimOut, true);
                }
                int imgDisplayMode = clipInfo.getImgDispalyMode();
                if (imgDisplayMode == Constants.EDIT_MODE_PHOTO_AREA_DISPLAY) {//区域显示
                    videoClip.setImageMotionMode(NvsVideoClip.IMAGE_CLIP_MOTIONMMODE_ROI);
                    RectF normalStartRectF = clipInfo.getNormalStartROI();
                    RectF normalEndRectF = clipInfo.getNormalEndROI();
                    if (normalStartRectF != null && normalEndRectF != null) {
                        videoClip.setImageMotionROI(normalStartRectF, normalEndRectF);
                    }
                } else {//全图显示
                    videoClip.setImageMotionMode(NvsVideoClip.CLIP_MOTIONMODE_LETTERBOX_ZOOMIN);
                }

                boolean isOpenMove = clipInfo.isOpenPhotoMove();
                videoClip.setImageMotionAnimationEnabled(isOpenMove);
            } else {//当前片段是视频
                float speed = clipInfo.getSpeed();
                videoClip.changeSpeed(speed);

                long trimIn = clipInfo.getTrimIn();
                long trimOut = clipInfo.getTrimOut();
                if (trimIn > 0) {
                    videoClip.changeTrimInPoint(trimIn, true);
                }

                if (trimOut > 0 && trimOut > trimIn) {
                    videoClip.changeTrimOutPoint(trimOut, true);
                }

                float volumeGain = clipInfo.getVolume();
                videoClip.setVolumeGain(volumeGain, volumeGain);
                videoClip.setExtraVideoRotation(clipInfo.getRotateAngle());
                NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx("Transform 2D");
                if (videoFxTransform != null) {
                    videoFxTransform.setFloatVal("Scale X", clipInfo.getScaleX());
                    videoFxTransform.setFloatVal("Scale Y", clipInfo.getScaleY());
                }
            }

            NvsVideoFx videoFxColor = videoClip.appendBuiltinFx("Color Property");
            if (videoFxColor != null) {
                videoFxColor.setFloatVal("Brightness", clipInfo.getBrightnessVal());
                videoFxColor.setFloatVal("Contrast", clipInfo.getContrastVal());
                videoFxColor.setFloatVal("Saturation", clipInfo.getSaturationVal());
            }
        }
    }

    public static boolean buildTimelineFilter(NvsTimeline timeline, VideoClipFxInfo videoClipFxData) {
        if (timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            return false;
        }

        if (videoClipFxData == null)
            return false;

        ArrayList<ClipInfo> clipInfos = TimelineData.instance().getClipInfoData();

        int videoClipCount = videoTrack.getClipCount();
        for (int i = 0; i < videoClipCount; i++) {
            NvsVideoClip clip = videoTrack.getClipByIndex(i);
            if (clip == null)
                continue;

            removeAllVideoFx(clip);
            String clipFilPath = clip.getFilePath();
            boolean isSrcVideoAsset = false;
            for (ClipInfo clipInfo : clipInfos) {
                String videoFilePath = clipInfo.getFilePath();
                if (clipFilPath.equals(videoFilePath)) {
                    isSrcVideoAsset = true;
                    break;
                }
            }

            if (!isSrcVideoAsset)
                continue;

            String name = videoClipFxData.getFxId();
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            int mode = videoClipFxData.getFxMode();
            float fxIntensity = videoClipFxData.getFxIntensity();
            if (mode == FilterItem.FILTERMODE_BUILTIN) {//内建特效
                NvsVideoFx fx = clip.appendBuiltinFx(name);
                fx.setFilterIntensity(fxIntensity);
            } else {////添加包裹特效
                NvsVideoFx fx = clip.appendPackagedFx(name);
                fx.setFilterIntensity(fxIntensity);
            }
        }

        return true;
    }

    public static boolean applyTheme(NvsTimeline timeline, String themeId) {
        if (timeline == null)
            return false;

        timeline.removeCurrentTheme();
        if (themeId == null || themeId.isEmpty())
            return false;

        if (!timeline.applyTheme(themeId)) {
            Log.e(TAG, "failed to apply theme");
            return false;
        }
        timeline.setThemeMusicVolumeGain(1.0f, 1.0f);

        // 应用主题之后，要把已经应用的背景音乐去掉
        TimelineData.instance().setMusicData(null);
        TimelineUtil.buildTimelineMusic(timeline, null);
        return true;
    }

    private static boolean removeAllVideoFx(NvsVideoClip videoClip) {
        if (videoClip == null)
            return false;

        int fxCount = videoClip.getFxCount();
        for (int i = 0; i < fxCount; i++) {
            NvsVideoFx fx = videoClip.getFxByIndex(i);
            if (fx == null)
                continue;

            String name = fx.getBuiltinVideoFxName();
            Log.e("===>", "fx name: " + name);
            if (name.equals("Color Property") || name.equals("Transform 2D")) {
                continue;
            }
            videoClip.removeFx(i);
            i--;
        }
        return true;
    }

    public static boolean setTransition(NvsTimeline timeline, TransitionInfo transitionInfo) {
        if (timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            return false;
        }

        if (transitionInfo == null)
            return false;

        int videoClipCount = videoTrack.getClipCount();
        if (videoClipCount <= 1)
            return false;

        for (int i = 0; i < videoClipCount - 1; i++) {
            if (transitionInfo.getTransitionMode() == TransitionInfo.TRANSITIONMODE_BUILTIN) {
                videoTrack.setBuiltinTransition(i, transitionInfo.getTransitionId());
            } else {
                videoTrack.setPackagedTransition(i, transitionInfo.getTransitionId());
            }
        }

        return true;
    }


    public static boolean buildTimelineMusic1(NvsTimeline timeline, MusicListResponse.DataBean musicInfo) {
        if (timeline == null) {
            return false;
        }
        NvsAudioTrack audioTrack = timeline.getAudioTrackByIndex(0);
        if (musicInfo == null) {
            if (audioTrack != null) {
                audioTrack.removeAllClips();
            }

            // 去掉音乐之后，要把已经应用的主题中的音乐还原
            String pre_theme_id = TimelineData.instance().getThemeData();
            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
                timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
            }
            return false;
        }
        if (audioTrack != null) {
            audioTrack.removeAllClips();
            audioTrack.addClip(musicInfo.getMusic_path(), 0, musicInfo.getM_trimIn(), musicInfo.getM_trimOut());
            float audioVolume = TimelineData.instance().getMusicVolume();
            audioTrack.setVolumeGain(audioVolume, audioVolume);
        }

        // 应用音乐之后，要把已经应用的主题中的音乐去掉
        String pre_theme_id = TimelineData.instance().getThemeData();
        if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
            timeline.setThemeMusicVolumeGain(0, 0);
        }
        return true;
    }

    public static boolean buildTimelineMusic(NvsTimeline timeline, MusicInfo musicInfo) {
        if (timeline == null) {
            return false;
        }
        NvsAudioTrack audioTrack = timeline.getAudioTrackByIndex(0);
        if (musicInfo == null) {
            if (audioTrack != null) {
                audioTrack.removeAllClips();
            }

            // 去掉音乐之后，要把已经应用的主题中的音乐还原
            String pre_theme_id = TimelineData.instance().getThemeData();
            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
                timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
            }
            return false;
        }
        if (audioTrack != null) {
            audioTrack.removeAllClips();
            audioTrack.addClip(musicInfo.getFilePath(), 0, musicInfo.getTrimIn(), musicInfo.getTrimOut());
            float audioVolume = TimelineData.instance().getMusicVolume();
            audioTrack.setVolumeGain(audioVolume, audioVolume);
        }

        // 应用音乐之后，要把已经应用的主题中的音乐去掉
        String pre_theme_id = TimelineData.instance().getThemeData();
        if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
            timeline.setThemeMusicVolumeGain(0, 0);
        }
        return true;
    }

    public static void buildTimelineRecordAudio(NvsTimeline timeline, ArrayList<RecordAudioInfo> recordAudioInfos) {
        if (timeline == null) {
            return;
        }
        NvsAudioTrack audioTrack = timeline.getAudioTrackByIndex(1);
        if (audioTrack != null) {
            audioTrack.removeAllClips();
            if (recordAudioInfos != null) {
                for (int i = 0; i < recordAudioInfos.size(); ++i) {
                    RecordAudioInfo recordAudioInfo = recordAudioInfos.get(i);
                    if (recordAudioInfo == null) {
                        continue;
                    }
                    NvsAudioClip audioClip = audioTrack.addClip(recordAudioInfo.getPath(), recordAudioInfo.getInPoint(), recordAudioInfo.getTrimIn(),
                            recordAudioInfo.getOutPoint() - recordAudioInfo.getInPoint() + recordAudioInfo.getTrimIn());
                    if (audioClip != null) {
                        audioClip.setVolumeGain(recordAudioInfo.getVolume(), recordAudioInfo.getVolume());
                        if (recordAudioInfo.getFxID() != null && !recordAudioInfo.getFxID().equals(Constants.NO_FX)) {
                            audioClip.appendFx(recordAudioInfo.getFxID());
                        }
                    }
                }
            }
            float audioVolume = TimelineData.instance().getRecordVolume();
            audioTrack.setVolumeGain(audioVolume, audioVolume);
        }
    }

    /**
     * 设置所有文字
     *
     * @param timeline
     * @param index
     * @return
     */
    public static boolean setAllSticker(NvsTimeline timeline, NvsTimeline masterTimeline, ArrayList<StickerInfo> stickerArray, int type, int index) {
        boolean result = true;
        result = setSticker(timeline, stickerArray, TIME_BRANCH, index);
        result = setSticker(masterTimeline, stickerArray, TIME_MASTER);
        return result;
    }

    /**
     * @param timeline
     * @param stickerArray
     * @param type
     * @param index        获取对应下标数据
     * @return
     */
    public static boolean setSticker(NvsTimeline timeline, ArrayList<StickerInfo> stickerArray, int type, int index) {
        if (timeline == null)
            return false;

        NvsTimelineAnimatedSticker deleteSticker = timeline.getFirstAnimatedSticker();
        while (deleteSticker != null) {
            deleteSticker = timeline.removeAnimatedSticker(deleteSticker);
        }

        for (StickerInfo sticker : stickerArray) {
            NvsTimelineAnimatedSticker newSticker = null;
            if (type == TIME_MASTER) {
                newSticker = timeline.addAnimatedSticker(sticker.getInMasterPoint(), sticker.getDuration(), sticker.getId());

            } else if (type == TIME_BRANCH) {
                if (index != sticker.getIndex()) {
                    continue;
                }
                newSticker = timeline.addAnimatedSticker(sticker.getInPoint(), sticker.getDuration(), sticker.getId());
            }
            if (newSticker == null)
                continue;
            newSticker.setZValue(sticker.getAnimateStickerZVal());
            newSticker.setHorizontalFlip(sticker.isHorizFlip());
            PointF translation = sticker.getTranslation();
            float scaleFactor = sticker.getScaleFactor();
            float rotation = sticker.getRotation();
            newSticker.setScale(scaleFactor);
            newSticker.setRotationZ(rotation);
            newSticker.setTranslation(translation);
            float volumeGain = sticker.getVolumeGain();
            newSticker.setVolumeGain(volumeGain, volumeGain);
        }
        return true;
    }

    public static boolean setSticker(NvsTimeline timeline, ArrayList<StickerInfo> stickerArray, int type) {
        if (timeline == null)
            return false;

        NvsTimelineAnimatedSticker deleteSticker = timeline.getFirstAnimatedSticker();
        while (deleteSticker != null) {
            deleteSticker = timeline.removeAnimatedSticker(deleteSticker);
        }

        for (StickerInfo sticker : stickerArray) {
            NvsTimelineAnimatedSticker newSticker = null;
            if (type == TIME_MASTER) {
                newSticker = timeline.addAnimatedSticker(sticker.getInMasterPoint(), sticker.getDuration(), sticker.getId());

            } else if (type == TIME_BRANCH) {
                newSticker = timeline.addAnimatedSticker(sticker.getInPoint(), sticker.getDuration(), sticker.getId());
            }
            if (newSticker == null)
                continue;
            newSticker.setZValue(sticker.getAnimateStickerZVal());
            newSticker.setHorizontalFlip(sticker.isHorizFlip());
            PointF translation = sticker.getTranslation();
            float scaleFactor = sticker.getScaleFactor();
            float rotation = sticker.getRotation();
            newSticker.setScale(scaleFactor);
            newSticker.setRotationZ(rotation);
            newSticker.setTranslation(translation);
            float volumeGain = sticker.getVolumeGain();
            newSticker.setVolumeGain(volumeGain, volumeGain);
        }
        return true;
    }

    public static boolean setAllCaption(NvsTimeline timeline, NvsTimeline masterTimeline, ArrayList<CaptionInfo> captionArray, int index) {
        boolean result = true;
        result = setCaption(timeline, captionArray, TIME_BRANCH, index);
        result = setCaption(masterTimeline, captionArray, TIME_MASTER);
        return result;
    }


    public static boolean setCaption(NvsTimeline timeline, ArrayList<CaptionInfo> captionArray, int type, int index) {
        if (timeline == null)
            return false;

        NvsTimelineCaption deleteCaption = timeline.getFirstCaption();
        while (deleteCaption != null) {
            deleteCaption = timeline.removeCaption(deleteCaption);
        }

        for (CaptionInfo caption : captionArray) {
            if (type == TIME_MASTER) {
                NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getMasterInPoint(),
                        caption.getDuration(), null);
                updateCaptionAttribute(newCaption, caption);
            } else if (type == TIME_BRANCH) {
                if (index != caption.getIndex()) {
                    continue;
                }

                NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getInPoint(),
                        caption.getDuration(), null);
                updateCaptionAttribute(newCaption, caption);
            }
        }

        return true;
    }

    /**
     * @param timeline
     * @param captionArray
     * @return
     */
    public static boolean setCaption(NvsTimeline timeline, ArrayList<CaptionInfo> captionArray, int type) {
        if (timeline == null)
            return false;

        NvsTimelineCaption deleteCaption = timeline.getFirstCaption();
        while (deleteCaption != null) {
            deleteCaption = timeline.removeCaption(deleteCaption);
        }

        for (CaptionInfo caption : captionArray) {
            if (type == TIME_MASTER) {
                NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getMasterInPoint(),
                        caption.getDuration(), null);
                updateCaptionAttribute(newCaption, caption);
            } else if (type == TIME_BRANCH) {
                NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getInPoint(),
                        caption.getDuration(), null);
                updateCaptionAttribute(newCaption, caption);
            }
        }

        return true;
    }


    private static void updateCaptionAttribute(NvsTimelineCaption newCaption, CaptionInfo caption) {
        if (newCaption == null || caption == null)
            return;

        //字幕StyleUuid需要首先设置，后面设置的字幕属性才会生效，
        // 因为字幕样式里面可能自带偏移，缩放，旋转等属性，最后设置会覆盖前面的设置的。
        newCaption.applyCaptionStyle(caption.getCaptionStyleUuid());
        int alignVal = caption.getAlignVal();
        if (alignVal >= 0)
            newCaption.setTextAlignment(alignVal);
        NvsColor textColor = ColorUtil.colorStringtoNvsColor(caption.getCaptionColor());
        if (textColor != null) {
            textColor.a = caption.getCaptionColorAlpha() / 100.0f;
            newCaption.setTextColor(textColor);
        }

        // 放缩字幕
        float scaleFactorX = caption.getScaleFactorX();
        float scaleFactorY = caption.getScaleFactorY();
        newCaption.setScaleX(scaleFactorX);
        newCaption.setScaleY(scaleFactorY);

        float rotation = caption.getRotation();
        // 旋转字幕
        newCaption.setRotationZ(rotation);
        newCaption.setZValue(caption.getCaptionZVal());
        boolean hasOutline = caption.isHasOutline();
        if (hasOutline) {
            newCaption.setDrawOutline(hasOutline);
            NvsColor outlineColor = ColorUtil.colorStringtoNvsColor(caption.getOutlineColor());
            if (outlineColor != null) {
                outlineColor.a = caption.getOutlineColorAlpha() / 100.0f;
                newCaption.setOutlineColor(outlineColor);
                newCaption.setOutlineWidth(caption.getOutlineWidth());
            }
        }
        String fontPath = caption.getCaptionFont();
        if (!fontPath.isEmpty())
            newCaption.setFontByFilePath(fontPath);
        boolean isBold = caption.isBold();

        newCaption.setBold(isBold);
        boolean isItalic = caption.isItalic();
        newCaption.setItalic(isItalic);
        boolean isShadow = caption.isShadow();
        newCaption.setDrawShadow(isShadow);
        if (isShadow) {
            PointF offset = new PointF(7, -7);
            NvsColor shadowColor = new NvsColor(0, 0, 0, 0.5f);
            newCaption.setShadowOffset(offset);  //字幕阴影偏移量
            newCaption.setShadowColor(shadowColor); // 字幕阴影颜色
        }
        float fontSize = caption.getCaptionSize();
        if (fontSize >= 0)
            newCaption.setFontSize(fontSize);
        PointF translation = caption.getTranslation();
        if (translation != null)
            newCaption.setCaptionTranslation(translation);
    }
}
