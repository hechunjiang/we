package wedemo.utils;

import android.graphics.RectF;
import android.util.Log;

import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;

import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.TimelineData;

/**
 * Created by admin on 2018/5/29.
 */

public class SingleClipTimelineUtil {
    private static String TAG = "SingleClipTimelineUtil";

    public static NvsTimeline createTimeline(ClipInfo clipInfo, boolean isTrim) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }
        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
        videoEditRes.imagePAR = new NvsRational(1, 1);
        NvsRational videoFps = new NvsRational(25, 1);

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;
        audioEditRes.channelCount = 2;

        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        if (timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }

        if (!buildVideoTrack(timeline, clipInfo, isTrim)) {
            Log.e(TAG, "failed to create video track");
            return timeline;
        }

        return timeline;
    }

    public static boolean buildVideoTrack(NvsTimeline timeline, ClipInfo clipInfo, boolean isTrim) {
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

    public static void removeTimeline(NvsTimeline timeline) {
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if (context != null && timeline != null) {
            context.removeTimeline(timeline);
        }
    }
}
