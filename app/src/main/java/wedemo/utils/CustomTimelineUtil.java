package wedemo.utils;

import android.content.Context;
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
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.shot.bean.FilterItem;
import wedemo.utils.dataInfo.CaptionInfo;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.RecordAudioInfo;
import wedemo.utils.dataInfo.StickerInfo;
import wedemo.utils.dataInfo.TimeDataCache;
import wedemo.utils.dataInfo.TimeLineFxInfo;
import wedemo.utils.dataInfo.TimeModeInfo;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.TimelineDataSingle;
import wedemo.utils.dataInfo.TransitionInfo;
import wedemo.utils.dataInfo.VideoClipFxInfo;

/**
 * Created by admin on 2018/5/29.
 */

public class CustomTimelineUtil {
    private static String TAG = "TimelineUtil";
    public static long TIME_BASE = 1000000;

    public static int TIME_MASTER = 10001;
    public static int TIME_BRANCH = 10002;


    public static int TIME_CREATE = 10003;
    public static int TIME_BUILD = 10004;

    public static int SIMPLE_RATE = 44100;

    /************************** 草稿箱 *******************************/
    public static List<CustomTimeLine> createDraftsSingleTimelineList(TimeDataCache timeDataCache) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }
        List<CustomTimeLine> list = new ArrayList<>();
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        List<TimelineDataSingle> data = timeDataCache.getData();

        for (int i = 0; i < videoClipArray.size(); i++) {
            //创建时间线
            TimelineDataSingle timelineDataSingle = data.get(i);
            CustomTimeLine customTimeLine = createDraftTimeline(i, timelineDataSingle);
            list.add(customTimeLine);
        }
        return list;
    }


    public static CustomTimeLine createDraftTimeline(int pos, TimelineDataSingle timelineDataSingle) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);

        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        CustomTimeLine customTimeLine = new CustomTimeLine();
        customTimeLine.setTimeline(timeline);
        customTimeLine.setTimeData(timelineDataSingle);

        if (!buildDraftVideoTrack(customTimeLine, pos)) {
            return customTimeLine;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        setTimelineData(customTimeLine);
        buildTimelineSingleFilter(customTimeLine.getTimeline(), timelineDataSingle.getVideoClipFxData());

        return customTimeLine;
    }


    public static boolean buildDraftVideoTrack(CustomTimeLine customTimeLine, int pos) {

        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addDraftVideoClip(videoTrack, pos, customTimeLine);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }


    private static void addDraftVideoClip(NvsVideoTrack videoTrack, int pos, CustomTimeLine customTimeLine) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        ClipInfo clipInfo = videoClipArray.get(pos);

        ArrayList<ClipInfo> infos = new ArrayList<>();
        infos.add(clipInfo);
        customTimeLine.getTimeData().setClipInfoData(infos);

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

        if (customTimeLine.getTimeData().getTimeMode() == reverseMode) {
            if (!videoClip.getPlayInReverse()) {
                videoClip.setPlayInReverse(true);
            }
        } else if (customTimeLine.getTimeData().getTimeMode() == relapseMode) {
            relapseVideo(customTimeLine);
        } else if (customTimeLine.getTimeData().getTimeMode() == slowMode) {
            slowVideo(customTimeLine);
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


    /************************************单时间线********************************/

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
            //创建时间线
            CustomTimeLine customTimeLine = createTimeline(i);

            list.add(customTimeLine);
        }
        return list;
    }

    /**
     * 创建每条单线的音乐线
     */
    public static void createSingleMusic() {

        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        MusicInfo musicData = TimelineData.instance().getMasterMusic();

        if (musicData != null) {
            for (int i = 0; i < singleTimelineList.size(); i++) {

                CustomTimeLine customTimeLine = singleTimelineList.get(i);
                MusicInfo musicSingle = musicData.clone();
                musicSingle.setTrimIn(TimelineManager.getInstance().getPreviousAllTime(i) + musicData.getTrimIn());
                //musicSingle.setTrimOut(TimelineManager.getInstance().getPreviousAllTime(i) + musicData.getTrimIn());

                customTimeLine.getTimeData().setMusicData(musicSingle);
                buildTimelineMusic(customTimeLine.getTimeline(), musicSingle);
            }
        }
    }

    /**
     * 裁剪时移除所有音乐线
     */
    public static void removeSingleMusic() {

        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();

        for (int i = 0; i < singleTimelineList.size(); i++) {

            CustomTimeLine customTimeLine = singleTimelineList.get(i);
            MusicInfo musicSingle = new MusicInfo();

            customTimeLine.getTimeData().setMusicData(musicSingle);
            removeTimelineMusic(customTimeLine.getTimeline());
        }
    }

    /**
     * 创建对应下标时间线
     *
     * @return
     */
    public static CustomTimeLine createTimeline(int pos) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);

        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        CustomTimeLine customTimeLine = new CustomTimeLine();
        customTimeLine.setTimeline(timeline);
        customTimeLine.getTimeData().setMakeRatio(TimelineData.instance().getMakeRatio());

        if (!buildVideoTrack(customTimeLine, pos)) {
            return customTimeLine;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        return customTimeLine;
    }

    public static boolean buildVideoTrack(CustomTimeLine customTimeLine, int pos) {

        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack, pos, customTimeLine);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    private static void addVideoClip(NvsVideoTrack videoTrack, int pos, CustomTimeLine customTimeLine) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = customTimeLine.getTimeData().getClipInfoData();
        if (videoClipArray == null || videoClipArray.size() == 0) {
            videoClipArray = TimelineData.instance().getClipInfoData();
        }
        ClipInfo clipInfo = videoClipArray.get(pos);

        ArrayList<ClipInfo> infos = new ArrayList<>();
        infos.add(clipInfo);
        customTimeLine.getTimeData().setClipInfoData(infos);

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
            Log.e("weiwei", "speed======" + speed);
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


    /****************************************总线********************************************/
    /**
     * 创建总时间线
     *
     * @return
     */
    public static CustomTimeLine createTimeline(Context ctx) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }

        CustomTimeLine customTimeLine = new CustomTimeLine();
        customTimeLine.setTimeline(timeline);
        customTimeLine.getTimeData().setMakeRatio(TimelineData.instance().getMakeRatio());

        if (!buildVideoTrack(customTimeLine)) {
            return customTimeLine;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        //setTimelineData(timeline);
        MusicInfo musicData = TimelineData.instance().getMasterMusic();

        if (musicData != null) {
            buildTimelineMusic(timeline, musicData);
        }

        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        if (videoClipArray != null && videoClipArray.size() > 1) {
            TransitionInfo transitionInfo = new TransitionInfo();
            for (int i = 0; i < videoClipArray.size() - 1; i++) {
                FilterItem filterItem = new FilterItem();

                filterItem.setFilterId(null);
                filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                filterItem.setFilterName(ctx.getString(R.string.no_info));
                filterItem.setImageId(R.mipmap.no_data);
                //filterItem.setImageId();
                transitionInfo.setFilterItem(i + "", filterItem);
            }

            customTimeLine.getTimeData().setTransitionData(transitionInfo);
        }

        return customTimeLine;
    }

    public static boolean buildVideoTrack(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack, customTimeLine, TIME_CREATE);

        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    public static boolean buildCopyTranMasterVideoTrack(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack, customTimeLine, TIME_BUILD);

        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    /**
     * 添加总时间线视频片段
     *
     * @param videoTrack
     */
    private static void addVideoClip(NvsVideoTrack videoTrack, CustomTimeLine customTimeLine, int type) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();

        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
//        if (videoClipArray == null || videoClipArray.size() == 0) {
//            videoClipArray = TimelineData.instance().getClipInfoData();
//            customTimeLine.getTimeData().setClipInfoData(videoClipArray);
//        }

        ArrayList<TimeModeInfo> timeModeInfos = customTimeLine.getTimeData().getTimeModeInfos();
        ArrayList<VideoClipFxInfo> videoClipFxInfos = customTimeLine.getTimeData().getVideoClipFxInfos();
        LogUtil.showLog("videoClipFxInfos = " + videoClipFxInfos.size());
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

            if (type == TIME_BUILD) {
                TimeModeInfo timeModeInfo = timeModeInfos.get(i);
                customTimeLine.getTimeData().setTimeMode(timeModeInfo.getTimeMode());
                customTimeLine.getTimeData().setResverTime(timeModeInfo.getResverTime());
                customTimeLine.getTimeData().setSlowTime(timeModeInfo.getSlowTime());

                if (customTimeLine.getTimeData().getTimeMode() == reverseMode) {
                    if (!videoClip.getPlayInReverse()) {
                        videoClip.setPlayInReverse(true);
                    }
                } else if (customTimeLine.getTimeData().getTimeMode() == relapseMode) {
                    relapseVideo(customTimeLine);
                } else if (customTimeLine.getTimeData().getTimeMode() == slowMode) {
                    slowVideo(customTimeLine);
                }

                //滤镜
                removeAllVideoFx(videoClip);

                VideoClipFxInfo videoClipFxData = videoClipFxInfos.get(i);
                String name = videoClipFxData.getFxId();

                if (!TextUtils.isEmpty(name)) {
                    int mode = videoClipFxData.getFxMode();
                    float fxIntensity = videoClipFxData.getFxIntensity();
                    if (mode == FilterItem.FILTERMODE_BUILTIN) {//内建特效
                        NvsVideoFx fx = videoClip.appendBuiltinFx(name);
                        fx.setFilterIntensity(fxIntensity);
                    } else {////添加包裹特效
                        NvsVideoFx fx = videoClip.appendPackagedFx(name);
                        fx.setFilterIntensity(fxIntensity);
                    }

                    //美白
                    NvsVideoFx beauty = videoClip.appendBuiltinFx("Beauty");
                    if (beauty != null) {
                        beauty.setFloatVal("Strength", videoClipFxData.getmStrengthValue());
                        beauty.setFloatVal("Whitening", videoClipFxData.getmWhiteningValue());
                        beauty.setFloatVal("Reddening", videoClipFxData.getmReddeningValue());
                    }
                }
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

    /*****************************重置单线时间线数据*******************************/
    public static boolean reSingleBuildVideoTrack() {
        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();

        for (int i = 0; i < singleTimelineList.size(); i++) {
            CustomTimeLine customTimeLine = singleTimelineList.get(i);
            NvsTimeline timeline = customTimeLine.getTimeline();

            int videoTrackCount = timeline.videoTrackCount();
            NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
            if (videoTrack == null) {
                Log.e(TAG, "failed to append video track");
                return false;
            }
            videoTrack.removeAllClips();

            addSingleVideoClip(videoTrack, i, customTimeLine);

            setTimelineData(customTimeLine, i);

            float videoVolume = TimelineData.instance().getOriginVideoVolume();
            videoTrack.setVolumeGain(videoVolume, videoVolume);

            NvsAudioTrack audioTrackByIndex = timeline.getAudioTrackByIndex(0);
            if (audioTrackByIndex != null) {
                float musicVolume = TimelineData.instance().getMusicVolume();
                audioTrackByIndex.setVolumeGain(musicVolume, musicVolume);
            }
        }

        return true;
    }

    public static boolean buildSound(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();

        int videoTrackCount = timeline.videoTrackCount();
        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }

        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        NvsAudioTrack audioTrackByIndex = timeline.getAudioTrackByIndex(0);
        if (audioTrackByIndex != null) {
            float musicVolume = TimelineData.instance().getMusicVolume();
            audioTrackByIndex.setVolumeGain(musicVolume, musicVolume);
        }

        return true;
    }

    private static final int noneTimeMode = 0;
    private static final int reverseMode = 1;
    private static final int relapseMode = 2;
    private static final int slowMode = 3;

    private static void addSingleVideoClip(NvsVideoTrack videoTrack, int pos, CustomTimeLine customTimeLine) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        ClipInfo clipInfo = videoClipArray.get(pos);

        ArrayList<ClipInfo> infos = new ArrayList<>();
        infos.add(clipInfo);
        customTimeLine.getTimeData().setClipInfoData(infos);

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

        if (customTimeLine.getTimeData().getTimeMode() == reverseMode) {
            if (!videoClip.getPlayInReverse()) {
                videoClip.setPlayInReverse(true);
            }
        } else if (customTimeLine.getTimeData().getTimeMode() == relapseMode) {
            relapseVideo(customTimeLine);
        } else if (customTimeLine.getTimeData().getTimeMode() == slowMode) {
            slowVideo(customTimeLine);
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

    public static void setTimelineData(CustomTimeLine customTimeLine) {

        if (customTimeLine == null)
            return;

        String themeId = customTimeLine.getTimeData().getThemeData();
        applyTheme(customTimeLine.getTimeline(), themeId);

        VideoClipFxInfo videoClipFxData = customTimeLine.getTimeData().getVideoClipFxData();
        //buildTimelineFilter(customTimeLine.getTimeline(), videoClipFxData);

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        setTransition(customTimeLine.getTimeline(), transitionInfo);

        setSticker(customTimeLine, TIME_BRANCH);

        setCaption(customTimeLine, TIME_BRANCH);

        ArrayList<RecordAudioInfo> recordArray = customTimeLine.getTimeData().getRecordAudioData();
        buildTimelineRecordAudio(customTimeLine.getTimeline(), recordArray);

        ArrayList<TimeLineFxInfo> timelineFx = customTimeLine.getTimeData().getTimelineFx();
        buildTimelineFxInfo(customTimeLine.getTimeline(), timelineFx, TIME_BRANCH);

    }

    public static void setTimelineData(CustomTimeLine customTimeLine, int pos) {

        if (customTimeLine == null)
            return;

//        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
//        ClipInfo clipInfo = videoClipArray.get(pos);
//        long trimIn = clipInfo.getTrimIn();
//        long trimOut = clipInfo.getTrimOut();


        String themeId = customTimeLine.getTimeData().getThemeData();
        applyTheme(customTimeLine.getTimeline(), themeId);

//        if (musicInfoClone != null) {
//            customTimeLine.getTimeData().setMusicData(musicInfoClone);
//            buildTimelineMusic(customTimeLine.getTimeline(), musicInfoClone);
//        }

        VideoClipFxInfo videoClipFxData = customTimeLine.getTimeData().getVideoClipFxData();
        buildTimelineSingleFilter(customTimeLine.getTimeline(), videoClipFxData);

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        setTransition(customTimeLine.getTimeline(), transitionInfo);

        setSticker(customTimeLine, TIME_BRANCH);

        setCaption(customTimeLine, TIME_BRANCH);

        ArrayList<RecordAudioInfo> recordArray = customTimeLine.getTimeData().getRecordAudioData();
        buildTimelineRecordAudio(customTimeLine.getTimeline(), recordArray);

        ArrayList<TimeLineFxInfo> timelineFx = customTimeLine.getTimeData().getTimelineFx();
        buildTimelineFxInfo(customTimeLine.getTimeline(), timelineFx, TIME_BRANCH);

//        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
//        MusicInfo musicInfoClone = TimelineData.instance().cloneMusicData();
//        String themeId = TimelineData.instance().getThemeData();
//        applyTheme(timeline, themeId);
//
//        if (musicInfoClone != null) {
//            TimelineData.instance().setMusicData(musicInfoClone);
//            buildTimelineMusic(timeline, musicInfoClone);
//        }
//
//        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
//        buildTimelineFilter(timeline, videoClipFxData);
//        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
//        setTransition(timeline, transitionInfo);
//
//        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();
//        buildTimelineRecordAudio(timeline, recordArray);
    }

    /**
     * 反复
     */
    private static void relapseVideo(CustomTimeLine customTimeLine) {
        int index = 0;
        int newIndex = 0;
        float outTime = customTimeLine.getTimeData().getResverTime();
        NvsTimeline m_TimeLine = customTimeLine.getTimeline();
        int newTime = (int) outTime + 1000000;
        NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);

        index = m_videoTrack.getClipByTimelinePosition((int) outTime).getIndex();

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) outTime).getIndex(), (long) outTime)) {
            Log.d(TAG, "spilt clip is false!");
            return;
        }


        if (newTime > m_TimeLine.getDuration())
            newTime = (int) m_TimeLine.getDuration() - 100;

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) newTime).getIndex(), (long) newTime)) {
            Log.d(TAG, "%spilt clip is false!");
            return;
        }
        newIndex = m_videoTrack.getClipByTimelinePosition((int) newTime).getIndex();

        NvsVideoClip newClip = m_videoTrack.getClipByIndex(newIndex - 1);

        for (int i = 0; i < 2; i++) {
            NvsVideoClip insertClip = m_videoTrack.insertClip(newClip.getFilePath(), newClip.getTrimIn(), newClip.getTrimOut(), newIndex);
            insertClip.setVolumeGain(0f, 0f);
        }

        for (int i = 0; i < m_videoTrack.getClipCount(); i++) {
            m_videoTrack.setBuiltinTransition(i, null);
            NvsVideoClip videoClip = m_videoTrack.getClipByIndex(i);
            videoClip.setPanAndScan(0, 1);
        }

    }

    /**
     * 慢动作
     */
    private static void slowVideo(CustomTimeLine customTimeLine) {
        float slowOutTime = customTimeLine.getTimeData().getSlowTime();
        NvsTimeline m_TimeLine = customTimeLine.getTimeline();

        int newSlowOutTime = (int) slowOutTime + 1000000;
        NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);
        int index = m_videoTrack.getClipByTimelinePosition((int) slowOutTime).getIndex();

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) slowOutTime).getIndex(), (long) slowOutTime)) {
            Log.d(TAG, "spilt clip is false!");
            return;
        }

        //NvsVideoClip clip = m_videoTrack.getClipByIndex(index+1);
        if (newSlowOutTime > m_TimeLine.getDuration())
            newSlowOutTime = (int) m_TimeLine.getDuration() - 100;

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition(newSlowOutTime).getIndex(), (long) newSlowOutTime)) {
            Log.d(TAG, "%spilt clip is false!");
            return;
        }

        int newIndex = m_videoTrack.getClipByTimelinePosition(newSlowOutTime).getIndex();

        NvsVideoClip clip = m_videoTrack.getClipByIndex(newIndex - 1);
        long trimIn = clip.getTrimIn();
        long trimOut = clip.getTrimOut();

        for (int i = 0; i < m_videoTrack.getClipCount(); i++) {
            NvsVideoClip videoClip = m_videoTrack.getClipByIndex(i);
            videoClip.setPanAndScan(0, 1);
        }
//        m_videoTrack.insertClip(clip.getFilePath(),trimIn,trimOut,newIndex);
//        for (int i = 0; i < m_videoTrack.getClipCount();i++) {
//            m_videoTrack.setBuiltinTransition(i,null);
//        }
        //addMusic();
    }

    public static void buildMasterTranistion(Context ctx) {
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        CustomTimeLine customTimeLine = TimelineManager.getInstance().getMasterTimeline();

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        if (transitionInfo == null) {
            transitionInfo = new TransitionInfo();
        }
        transitionInfo.getItemMap().clear();

        if (videoClipArray != null && videoClipArray.size() > 1) {
            for (int i = 0; i < videoClipArray.size() - 1; i++) {
                FilterItem filterItem = new FilterItem();

                filterItem.setFilterId(null);
                filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                filterItem.setFilterName(ctx.getString(R.string.no_info));
                filterItem.setImageId(R.mipmap.no_data);
                //filterItem.setImageId();
                transitionInfo.setFilterItem(i + "", filterItem);
            }

            customTimeLine.getTimeData().setTransitionData(transitionInfo);
        }
    }

    public static boolean removeTimeFx() {

        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        if (singleTimelineList != null) {
            for (CustomTimeLine timeLine : singleTimelineList) {
                timeLine.getTimeData().setSlowTime(0);
                timeLine.getTimeData().setResverTime(0);
                timeLine.getTimeData().setTimeMode(0);
            }
        }

        return true;
    }

    /*****************************总线重置**********************/
    public static boolean reMasterBuildVideoTrack() {
        CustomTimeLine masterTimeline = TimelineManager.getInstance().getMasterTimeline();
        NvsTimeline timeline = masterTimeline.getTimeline();

        int videoTrackCount = timeline.videoTrackCount();
        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        videoTrack.removeAllClips();

        //封装时间特效和滤镜封装
        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        ArrayList<TimeModeInfo> timeModeInfos = new ArrayList<>();
        ArrayList<VideoClipFxInfo> videoClipFxInfos = new ArrayList<>();

        for (int i = 0; i < singleTimelineList.size(); i++) {
            CustomTimeLine customTimeLine = singleTimelineList.get(i);
            long previousAllTime = TimelineManager.getInstance().getPreviousAllTime(i);

            TimeModeInfo timeModeInfo = new TimeModeInfo();
            timeModeInfo.setTimeMode(customTimeLine.getTimeData().getTimeMode());
            timeModeInfo.setResverTime(customTimeLine.getTimeData().getResverTime() + previousAllTime);
            timeModeInfo.setSlowTime(customTimeLine.getTimeData().getSlowTime() + previousAllTime);
            timeModeInfos.add(timeModeInfo);

            //滤镜
            VideoClipFxInfo videoClipFxData = customTimeLine.getTimeData().getVideoClipFxData();
            if (videoClipFxData == null) {
                videoClipFxData = new VideoClipFxInfo();
            }
            videoClipFxInfos.add(videoClipFxData);
        }

        masterTimeline.getTimeData().setTimeModeInfos(timeModeInfos);
        masterTimeline.getTimeData().setVideoClipFxInfos(videoClipFxInfos);

        addVideoClip(videoTrack, masterTimeline, TIME_BUILD);

        setTimelineData();

        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        NvsAudioTrack audioTrackByIndex = timeline.getAudioTrackByIndex(0);
        if (audioTrackByIndex != null) {
            float musicVolume = TimelineData.instance().getMusicVolume();
            audioTrackByIndex.setVolumeGain(musicVolume, musicVolume);
        }

        return true;
    }

    private static void addVideoClip(NvsVideoTrack videoTrack) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0; i < videoClipArray.size(); i++) {
            ClipInfo clipInfo = videoClipArray.get(i);
            String filePath = clipInfo.getFilePath();
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


    /*************************************时间线复制，进行操作**************************************/
    /**
     * 复制一条时间线
     *
     * @param customTimeLine
     * @return
     */
    public static CustomTimeLine createcCopyTimeline(CustomTimeLine customTimeLine) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }


        //数据赋值
        CustomTimeLine timeLines = new CustomTimeLine();
        timeLines.setTimeline(timeline);
        timeLines.setTimeData(customTimeLine);

        if (!buildCopyVideoTrack(timeLines)) {
            return timeLines;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道


        setTimelineCopyData(timeLines);

        return timeLines;
    }

    public static CustomTimeLine createcCopyMasterTimeline() {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }


        //数据赋值
        CustomTimeLine timeLines = new CustomTimeLine();
        timeLines.setTimeline(timeline);
        timeLines.setTimeData(TimelineManager.getInstance().getMasterTimeline());

        if (!buildCopyTranMasterVideoTrack(timeLines)) {
            return timeLines;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道


        setTimelineMasterCopyData(timeLines);

        return timeLines;
    }

    public static boolean buildCopyMasterVideoTrack(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addCopyVideoClip(videoTrack, customTimeLine);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }


    public static boolean buildCopyVideoTrack(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addCopyVideoClip(videoTrack, customTimeLine);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }


    /**
     * 添加总时间线视频片段
     *
     * @param videoTrack
     */
    private static void addCopyVideoClip(NvsVideoTrack videoTrack, CustomTimeLine customTimeLine) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();

        //通过素材文件路径添加素材，可以是图片或者视频

        ArrayList<ClipInfo> videoClipArray = customTimeLine.getTimeData().getClipInfoData();
        if (videoClipArray == null || videoClipArray.size() == 0) {
            videoClipArray = TimelineData.instance().getClipInfoData();
            customTimeLine.getTimeData().setClipInfoData(videoClipArray);
        }

        ClipInfo clipInfo = videoClipArray.get(0);
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


    /*********************************时间线剪切****************************/

    public static List<CustomTimeLine> createCutTimelineList(ArrayList<ClipInfo> mClipArrayList, boolean isTrim) {

        List<CustomTimeLine> list = new ArrayList<>();

        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();
        for (int i = 0; i < mClipArrayList.size(); i++) {
            CustomTimeLine timeline = createTimeline(singleTimelineList.get(i), mClipArrayList.get(i), true);
            list.add(timeline);
        }

        return list;
    }

    public static CustomTimeLine createTimeline(CustomTimeLine inTimeLine, ClipInfo clipInfo, boolean isTrim) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }
        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);
        NvsRational videoFps = new NvsRational(25, 1);

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;
        audioEditRes.channelCount = 2;

        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        CustomTimeLine customTimeLine = new CustomTimeLine();
        customTimeLine.setTimeline(timeline);
        customTimeLine.setTimeData(inTimeLine);

        if (!buildVideoTrack(customTimeLine, clipInfo, isTrim)) {
            Log.e(TAG, "failed to create video track");
            return customTimeLine;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        setTimelineCopyData(customTimeLine);
        return customTimeLine;
    }

    public static boolean buildVideoTrack(CustomTimeLine customTimeLine, ClipInfo clipInfo, boolean isTrim) {
        NvsTimeline timeline = customTimeLine.getTimeline();

        if (timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }

        String filePath = clipInfo.getFilePath();
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null) {
            Log.e(TAG, "failed to append video clip");
            return false;
        }

        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        if (blurFlag) {
            videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
        }
        NvsVideoFx videoFxColor = videoClip.appendBuiltinFx("Color Property");
        if (videoFxColor != null) {
            videoFxColor.setFloatVal("Brightness", clipInfo.getBrightnessVal());
            videoFxColor.setFloatVal("Contrast", clipInfo.getContrastVal());
            videoFxColor.setFloatVal("Saturation", clipInfo.getSaturationVal());
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
            float volumeGain = clipInfo.getVolume();
            videoClip.setVolumeGain(volumeGain, volumeGain);
            float speed = clipInfo.getSpeed();
            videoClip.changeSpeed(speed);

            int rotateAngle = clipInfo.getRotateAngle();
            videoClip.setExtraVideoRotation(rotateAngle);
            NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx("Transform 2D");
            if (videoFxTransform != null) {
                videoFxTransform.setFloatVal("Scale X", clipInfo.getScaleX());
                videoFxTransform.setFloatVal("Scale Y", clipInfo.getScaleY());
            }

            if (isTrim)//如果当前是裁剪页面，不作入出点改变
                return true;
            long trimIn = clipInfo.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if (trimIn >= 0) {
                videoClip.changeTrimInPoint(trimIn, true);
            }

            if (trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }
        }

        return true;
    }


    /*************************滤镜等剪切***************************/

    public static CustomTimeLine cut(CustomTimeLine customTimeLine, long inCut, long outCut) {
        TimelineDataSingle timeData = customTimeLine.getTimeData();

        // 文字剪切
        ArrayList<CaptionInfo> captionData = timeData.getCaptionData();
        for (CaptionInfo captionInfo : captionData) {
            long outCap = captionInfo.getChangeTrimOut(); //记录出点

            if (inCut >= outCap) {   //如果剪切入点大于了出点，直接跳过这个
                captionInfo.setInPoint(0);
                captionInfo.setDuration(0);
                continue;
            }

            long offset = captionInfo.getChangeTrimIn() - inCut;
            if (offset > 0) { //大于0，左移，入点为偏移值
                captionInfo.setInPoint(offset);
            } else {  //小于0，右，入点设为0
                captionInfo.setInPoint(0);
            }

            long outOffset = outCap - outCut;      //出点偏移量
            if (offset > 0) {
                long outResult = outCap - outOffset;   //出点 位置
                captionInfo.setDuration(outResult - captionInfo.getInPoint());
            } else {
                long outResult = outCap; //出点位置不变
                captionInfo.setDuration(outResult - captionInfo.getInPoint());
            }

            Log.e("222", "视频切入点 = inCut = " + inCut + ",outCut = " + outCut);
            Log.e("222", "原本出入点 = inpoint = " + captionInfo.getInPoint() + ",duration = " + captionInfo.getDuration());
            Log.e("222", "结果出入点 = inpoint = " + captionInfo.getInPoint() + ",duration = " + captionInfo.getDuration());
        }

        //贴纸
        ArrayList<StickerInfo> stickerData = timeData.getStickerData();
        for (StickerInfo stickerInfo : stickerData) {
            long outCap = stickerInfo.getChangeTrimOut(); //记录出点

            long offset = stickerInfo.getChangeTrimIn() - inCut;
            if (offset > 0) { //大于0，左移，入点为偏移值
                stickerInfo.setInPoint(offset);
            } else {  //小于0，右，入点设为0
                stickerInfo.setInPoint(0);
            }

            long outOffset = outCap - outCut;      //出点偏移量
            if (offset > 0) {
                long outResult = outCap - outOffset;   //出点 位置
                stickerInfo.setDuration(outResult - stickerInfo.getInPoint());
            } else {
                long outResult = outCap; //出点位置不变
                stickerInfo.setDuration(outResult - stickerInfo.getInPoint());
            }
        }

        //音乐
        MusicInfo musicData = timeData.getMusicData();
        if (musicData != null) {
            long outCap = musicData.getChangem_trimOut(); //记录出点

            long offset = musicData.getChangem_trimIn() - inCut;
            if (offset > 0) { //大于0，左移，入点为偏移值
                musicData.setTrimIn(offset);
            } else {  //小于0，右，入点设为0
                musicData.setTrimIn(0);
            }

            long outOffset = outCap - outCut;      //出点偏移量
            if (offset > 0) {
                long outResult = outCap - outOffset;   //出点 位置
                musicData.setTrimOut(outResult);
            } else {
                long outResult = outCap; //出点位置不变
                musicData.setTrimOut(outResult);
            }
        }

        //特效
        ArrayList<TimeLineFxInfo> timelineFx = timeData.getTimelineFx();
        for (TimeLineFxInfo timeLineFxInfo : timelineFx) {
            long outCap = timeLineFxInfo.getChangeTrimOut(); //记录出点

            long offset = timeLineFxInfo.getChangeTrimIn() - inCut;
            if (offset > 0) { //大于0，左移，入点为偏移值
                timeLineFxInfo.setInPoint(offset);
            } else {  //小于0，右，入点设为0
                timeLineFxInfo.setInPoint(0);
            }

            long outOffset = outCap - outCut;      //出点偏移量
            if (offset > 0) {
                long outResult = outCap - outOffset;   //出点 位置
                timeLineFxInfo.setOutPoint(outResult);
            } else {
                long outResult = outCap; //出点位置不变
                timeLineFxInfo.setOutPoint(outResult);
            }
        }

        return customTimeLine;
    }


    /*************************追加时间线************************/
    public static List<CustomTimeLine> addSingleTimelineList(ArrayList<ClipInfo> videoClipArray) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<CustomTimeLine> list = new ArrayList<>();

        for (int i = 0; i < videoClipArray.size(); i++) {
            CustomTimeLine customTimeLine = createTimeline(i, videoClipArray);

            list.add(customTimeLine);
        }

        return list;
    }

    public static CustomTimeLine createTimeline(int pos, ArrayList<ClipInfo> videoClipArray) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);  /*像素比，设为1:1*/
        NvsRational videoFps = new NvsRational(25, 1);/*帧速率，代表1秒播出多少帧画面，一般设25帧，也可设为30 */

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = SIMPLE_RATE;   /*音频采样率，可以是44100，或者48000*/
        audioEditRes.channelCount = 2;  /*音频通道数,一般是2*/

        /*创建时间线*/
        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);

        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        CustomTimeLine customTimeLine = new CustomTimeLine();
        customTimeLine.setTimeline(timeline);

        if (!buildVideoTrack(customTimeLine, pos, videoClipArray)) {
            return customTimeLine;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道
        return customTimeLine;
    }

    public static boolean buildVideoTrack(CustomTimeLine customTimeLine, int pos, ArrayList<ClipInfo> videoClipArray) {

        NvsTimeline timeline = customTimeLine.getTimeline();
        if (timeline == null) {
            return false;
        }

        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if (videoTrack == null) {
            Log.e(TAG, "failed to append video track");
            return false;
        }
        addVideoClip(videoTrack, pos, customTimeLine, videoClipArray);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }

    private static void addVideoClip(NvsVideoTrack videoTrack, int pos, CustomTimeLine customTimeLine, ArrayList<ClipInfo> videoClipArray) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        //通过素材文件路径添加素材，可以是图片或者视频
        ClipInfo clipInfo = videoClipArray.get(pos);

        ArrayList<ClipInfo> clipInfos = new ArrayList<>();
        clipInfos.add(clipInfo);
        customTimeLine.getTimeData().setClipInfoData(clipInfos);

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
     * 根据位置选择
     *
     * @return
     */
//    public static boolean buildVideoTrack(CustomTimeLine customTimeLine) {
//        if (customTimeLine == null) {
//            return false;
//        }
//
//        NvsTimeline timeline = customTimeLine.getTimeline();
//        /*添加视频轨道，如果不做画中画，添加一条视频轨道即可*/
//        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
//        if (videoTrack == null) {
//            Log.e(TAG, "failed to append video track");
//            return false;
//        }
//        addVideoClip(videoTrack, customTimeLine.getTimeData());
//        float videoVolume = TimelineData.instance().getOriginVideoVolume();
//        videoTrack.setVolumeGain(videoVolume, videoVolume);
//        return true;
//    }


    /**
     * 视频流数据复制添加
     *
     * @param videoTrack
     * @param dataSingle
     */
    private static void addVideoClip(NvsVideoTrack videoTrack, TimelineDataSingle dataSingle) {
        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        //通过素材文件路径添加素材，可以是图片或者视频
        ArrayList<ClipInfo> videoClipArray = dataSingle.getClipInfoData();
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

    /**
     * 数据复制
     *
     * @param customTimeLine
     */
    public static void setTimelineCopyData(CustomTimeLine customTimeLine) {
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
        buildTimelineSingleFilter(customTimeLine.getTimeline(), videoClipFxData);

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        setTransition(customTimeLine.getTimeline(), transitionInfo);

        setSticker(customTimeLine, TIME_BRANCH);

        setCaption(customTimeLine, TIME_BRANCH);

        ArrayList<RecordAudioInfo> recordArray = customTimeLine.getTimeData().getRecordAudioData();
        buildTimelineRecordAudio(customTimeLine.getTimeline(), recordArray);

        ArrayList<TimeLineFxInfo> timelineFx = customTimeLine.getTimeData().getTimelineFx();

        buildTimelineFxInfo(customTimeLine.getTimeline(), timelineFx, TIME_BRANCH);
    }

    /**
     * 数据复制
     *
     * @param customTimeLine
     */
    public static void setTimelineMasterCopyData(CustomTimeLine customTimeLine) {
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
        buildTimelineSingleFilter(customTimeLine.getTimeline(), videoClipFxData);

        TransitionInfo transitionInfo = customTimeLine.getTimeData().getTransitionData();
        setTransition(customTimeLine.getTimeline(), transitionInfo);

        setSticker(customTimeLine, TIME_MASTER);

        setCaption(customTimeLine, TIME_MASTER);

        ArrayList<RecordAudioInfo> recordArray = customTimeLine.getTimeData().getRecordAudioData();
        buildTimelineRecordAudio(customTimeLine.getTimeline(), recordArray);

        ArrayList<TimeLineFxInfo> timelineFx = customTimeLine.getTimeData().getTimelineFx();

        buildTimelineFxInfo(customTimeLine.getTimeline(), timelineFx, TIME_MASTER);
    }


    /*******************************************其他操作***********************************/

    /**
     * 检查分支
     *
     * @return
     */
    public static boolean checkSingleTimeLine() {
        List<CustomTimeLine> list = TimelineManager.getInstance().getSingleTimelineList();
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
    public static List<CustomTimeLine> addSingleTimelineList(int count) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        List<CustomTimeLine> list = new ArrayList<>();
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = videoClipArray.size(); i < count + videoClipArray.size(); i++) {
            CustomTimeLine timeline = createTimeline(i);
            list.add(timeline);
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

    /****************************贴图**********************/
    public static boolean setAllSticker(CustomTimeLine customTimeLine) {

        return setSticker(customTimeLine, TIME_BRANCH);
    }

    /**
     * @return
     */
    public static boolean setSticker(CustomTimeLine customTimeLine, int type) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        ArrayList<StickerInfo> stickerArray = customTimeLine.getTimeData().getStickerData();

        if (timeline == null)
            return false;

        NvsTimelineAnimatedSticker deleteSticker = timeline.getFirstAnimatedSticker();
        while (deleteSticker != null) {
            deleteSticker = timeline.removeAnimatedSticker(deleteSticker);
        }

        for (StickerInfo sticker : stickerArray) {
            NvsTimelineAnimatedSticker newSticker = null;

            if (type == TIME_BRANCH) {
                newSticker = timeline.addAnimatedSticker(sticker.getInPoint(), sticker.getDuration(), sticker.getId());
            } else if (type == TIME_MASTER) {
                newSticker = timeline.addAnimatedSticker(sticker.getInMasterPoint(), sticker.getDuration(), sticker.getId());
            }


            if (newSticker == null)
                continue;
            newSticker.setClipAffinityEnabled(false);

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


    public static boolean setWaterSticker(CustomTimeLine customTimeLine, String id, float width, float height) {
        if (customTimeLine == null)
            return false;

        NvsTimeline timeline = customTimeLine.getTimeline();

        if (timeline == null)
            return false;

        NvsTimelineAnimatedSticker sticker = timeline.addAnimatedSticker(0, timeline.getDuration(), id);
        RectF originalBoundingRect = sticker.getOriginalBoundingRect();

        float x = originalBoundingRect.top * 0.17f;
        float y = originalBoundingRect.right * 0.17f;
        float result_width = width - y - 40;
        float result_height = height + x - 40;


        if (sticker == null)
            return false;
        sticker.setZValue(1f);
        sticker.setHorizontalFlip(false);
        sticker.setScale(0.17f);
        sticker.setRotationZ(0f); //旋转角度
        sticker.setTranslation(new PointF(result_width, result_height)); //动画贴纸的平移
        return true;
    }


    /*******************************文字****************************/
    public static boolean setAllCaption(CustomTimeLine customTimeLine) {
        return setCaption(customTimeLine, TIME_BRANCH);
    }


    public static boolean setCaption(CustomTimeLine customTimeLine, int type) {
        NvsTimeline timeline = customTimeLine.getTimeline();
        ArrayList<CaptionInfo> captionArray = customTimeLine.getTimeData().getCaptionData();

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


    /**
     * 重置时间线数据
     *
     * @return
     */
    public static boolean reBuildMasterVideoTrack(CustomTimeLine customTimeLine) {
        NvsTimeline timeline = customTimeLine.getTimeline();

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

        addVideoClip(videoTrack, customTimeLine.getTimeData());

        setTimelineCopyData(customTimeLine);

        float videoVolume = customTimeLine.getTimeData().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume, videoVolume);

        return true;
    }


    /**
     * -----------------设置总线数据------------------
     */
    public static void setTimelineData() {
        CustomTimeLine masterTimeline = TimelineManager.getInstance().getMasterTimeline();
        List<CustomTimeLine> singleTimelineList = TimelineManager.getInstance().getSingleTimelineList();

        masterTimeline.getTimeData().setMusicData(TimelineData.instance().getMasterMusic());

        ArrayList<TimeLineFxInfo> timeLineFxInfoArrayList = new ArrayList<>();
        ArrayList<StickerInfo> stickerInfoArrayList = new ArrayList<>();
        ArrayList<CaptionInfo> captionInfoArrayList = new ArrayList<>();
        // ArrayList<MusicInfo> musicInfoArrayList = new ArrayList<>();

        for (int i = 0; i < singleTimelineList.size(); i++) {

            CustomTimeLine customTimeLine = singleTimelineList.get(i);
            long previousAllTime = TimelineManager.getInstance().getPreviousAllTime(i);

            TimelineDataSingle timeData = customTimeLine.getTimeData();
//            //音乐
//            MusicInfo musicData = timeData.getMusicData();
//            if (musicData != null) {
//                musicData.setInPoint(previousAllTime);
//                musicData.setTrimOut(customTimeLine.getTimeline().getDuration() + musicData.getTrimIn());
//                musicInfoArrayList.add(musicData);
//            }

            //packet特效
            ArrayList<TimeLineFxInfo> timelineFxData = timeData.getTimelineFx();
            for (TimeLineFxInfo timeLineFxInfo : timelineFxData) {
                timeLineFxInfo.setMasterfxInPoint(previousAllTime + timeLineFxInfo.getInPoint());
                timeLineFxInfo.setMasterfxOutPoint(previousAllTime + timeLineFxInfo.getOutPoint());
            }
            timeLineFxInfoArrayList.addAll(timelineFxData);

            //贴图
            ArrayList<StickerInfo> stickerData = timeData.getStickerData();
            for (StickerInfo stickerInfo : stickerData) {
                stickerInfo.setMasterInPoint(stickerInfo.getInPoint() + previousAllTime);
            }
            stickerInfoArrayList.addAll(stickerData);


            //文字
            ArrayList<CaptionInfo> captionData = timeData.getCaptionData();
            for (CaptionInfo captionInfo : captionData) {
                captionInfo.setMasterInPoint(captionInfo.getInPoint() + previousAllTime);
            }
            captionInfoArrayList.addAll(captionData);
        }

        masterTimeline.getTimeData().setTimelineFx(timeLineFxInfoArrayList);
        masterTimeline.getTimeData().setStickerData(stickerInfoArrayList);
        masterTimeline.getTimeData().setCaptionData(captionInfoArrayList);


        //buildMasterTimelineMusic(masterTimeline.getTimeline(), musicInfoArrayList);
        buildTimelineMusic(masterTimeline.getTimeline(), TimelineData.instance().getMasterMusic());

        setSticker(masterTimeline, TIME_MASTER);
        setCaption(masterTimeline, TIME_MASTER);

        buildTimelineFxInfo(masterTimeline.getTimeline(), timeLineFxInfoArrayList, TIME_MASTER);
        setTransition(masterTimeline.getTimeline(), masterTimeline.getTimeData().getTransitionData());
    }

    public static boolean removeTimeline(CustomTimeLine timeline) {
        if (timeline == null)
            return false;

        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null)
            return false;
        timeline.getTimeData().clear();
        return context.removeTimeline(timeline.getTimeline());
    }

    public static boolean buildTimelineSingleFilter(NvsTimeline timeline, VideoClipFxInfo videoClipFxData) {
        if (timeline == null) {
            return false;
        }
        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            return false;
        }

        if (videoClipFxData == null)
            return false;

        NvsVideoClip clip = videoTrack.getClipByIndex(0);
        if (clip == null)
            return false;

        removeAllVideoFx(clip);

        String name = videoClipFxData.getFxId();
        if (TextUtils.isEmpty(name)) {
            return false;
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

        NvsVideoFx beauty = clip.appendBuiltinFx("Beauty");
        if (beauty != null) {
            beauty.setFloatVal("Strength", videoClipFxData.getmStrengthValue());
            beauty.setFloatVal("Whitening", videoClipFxData.getmWhiteningValue());
            beauty.setFloatVal("Reddening", videoClipFxData.getmReddeningValue());
        }

        return true;
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
        CustomTimelineUtil.buildTimelineMusic(timeline, null);
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


    /**
     * 特效添加
     *
     * @param timeline
     * @param timeLineFxInfos
     */
    public static void buildTimelineFxInfo(NvsTimeline timeline, ArrayList<TimeLineFxInfo> timeLineFxInfos, int type) {
        if (timeline == null) {
            return;
        }

        NvsTimelineVideoFx nextFx = timeline.removeTimelineVideoFx(timeline.getFirstTimelineVideoFx());
        while (nextFx != null) {
            nextFx = timeline.removeTimelineVideoFx(nextFx);
        }

        if (timeLineFxInfos == null || timeLineFxInfos.size() == 0) {
            return;
        }

        for (int j = 0; j < timeLineFxInfos.size(); j++) {

            if ("Video Echo".equals(timeLineFxInfos.get(j).getName())) {
                if (type == TIME_MASTER) {
                    timeline.addBuiltinTimelineVideoFx(timeLineFxInfos.get(j).getMasterfxInPoint(), timeLineFxInfos.get(j).getMasterfxOutPoint() - timeLineFxInfos.get(j).getMasterfxInPoint(), timeLineFxInfos.get(j).getName());
                } else {
                    timeline.addBuiltinTimelineVideoFx(timeLineFxInfos.get(j).getInPoint(), timeLineFxInfos.get(j).getOutPoint() - timeLineFxInfos.get(j).getInPoint(), timeLineFxInfos.get(j).getName());
                }
            } else {
                if (type == TIME_MASTER) {
                    timeline.addPackagedTimelineVideoFx(timeLineFxInfos.get(j).getMasterfxInPoint(), timeLineFxInfos.get(j).getMasterfxOutPoint() - timeLineFxInfos.get(j).getMasterfxInPoint(), timeLineFxInfos.get(j).getName());
                } else {
                    timeline.addPackagedTimelineVideoFx(timeLineFxInfos.get(j).getInPoint(), timeLineFxInfos.get(j).getOutPoint() - timeLineFxInfos.get(j).getInPoint(), timeLineFxInfos.get(j).getName());
                }
            }

        }
    }

    public static boolean setTransition(NvsTimeline timeline, TransitionInfo transitionInfo) {
        if (timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            return false;
        }

        if (transitionInfo == null) {
            return false;
        }

        int videoClipCount = videoTrack.getClipCount();
        if (videoClipCount <= 1)
            return false;

        for (int i = 0; i < videoClipCount - 1; i++) {
            FilterItem filterItem = transitionInfo.getItemMap().get(i + "");
            if (filterItem == null) {
                return false;
            }
            if (filterItem.getFilterMode() == FilterItem.FILTERMODE_BUILTIN) {
                videoTrack.setBuiltinTransition(i, filterItem.getFilterId());
            } else {
                videoTrack.setPackagedTransition(i, filterItem.getPackageId());
            }
        }

        return true;
    }

    public static boolean buildMasterTimelineMusic(NvsTimeline timeline, List<MusicInfo> musicInfoArray) {
        if (timeline == null) {
            return false;
        }
        NvsAudioTrack audioTrack = timeline.getAudioTrackByIndex(0);

        if (musicInfoArray == null || musicInfoArray.size() == 0) {
            return true;
        }

        if (audioTrack != null) {
            audioTrack.removeAllClips();
        }
        for (int i = 0; i < musicInfoArray.size(); i++) {
            MusicInfo musicInfo = musicInfoArray.get(i);
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
                //audioTrack.removeAllClips();
                audioTrack.addClip(musicInfo.getFilePath(), musicInfo.getInPoint(), musicInfo.getTrimIn(), musicInfo.getTrimOut());
                float audioVolume = TimelineData.instance().getMusicVolume();
                audioTrack.setVolumeGain(audioVolume, audioVolume);
            }

        }

        // 应用音乐之后，要把已经应用的主题中的音乐去掉
        String pre_theme_id = TimelineData.instance().getThemeData();
        if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
            timeline.setThemeMusicVolumeGain(0, 0);
        }

        return true;
    }

    public static boolean removeTimelineMusic(NvsTimeline timeline) {
        if (timeline == null) {
            return false;
        }
        NvsAudioTrack audioTrack = timeline.getAudioTrackByIndex(0);
        if (audioTrack != null) {
            audioTrack.removeAllClips();

            // 去掉音乐之后，要把已经应用的主题中的音乐还原
            String pre_theme_id = TimelineData.instance().getThemeData();
            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
                timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
            }
            return false;
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
