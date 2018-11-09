//package com.sven.huinews.international.utils.shot.utils;
//
//import android.graphics.PointF;
//import android.graphics.RectF;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.meicam.sdk.NvsAudioClip;
//import com.meicam.sdk.NvsAudioResolution;
//import com.meicam.sdk.NvsAudioTrack;
//import com.meicam.sdk.NvsColor;
//import com.meicam.sdk.NvsRational;
//import com.meicam.sdk.NvsStreamingContext;
//import com.meicam.sdk.NvsTimeline;
//import com.meicam.sdk.NvsTimelineAnimatedSticker;
//import com.meicam.sdk.NvsTimelineCaption;
//import com.meicam.sdk.NvsVideoClip;
//import com.meicam.sdk.NvsVideoFx;
//import com.meicam.sdk.NvsVideoResolution;
//import com.meicam.sdk.NvsVideoTrack;
//
//import com.sven.huinews.international.utils.shot.ParameterSettingValues;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.CaptionInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.ClipInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.MusicInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.RecordAudioInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.StickerInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.TimelineData;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.TransitionInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.VideoClipFxInfo;
//
//import java.util.ArrayList;
//
///**
// * Created by admin on 2018/5/29.
// */
//
//public class TimelineUtil {
//    private static String TAG = "TimelineUtil";
//    public static long TIME_BASE = 1000000;
//    public static NvsTimeline createTimeline(){
//        NvsStreamingContext context = NvsStreamingContext.getInstance();
//        if(context == null) {
//            Log.e(TAG, "failed to get streamingContext");
//            return null;
//        }
//
//        NvsVideoResolution videoEditRes = TimelineData.instance().getVideoResolution();
//        videoEditRes.imagePAR = new NvsRational(1, 1);
//        NvsRational videoFps = new NvsRational(25, 1);
//
//        NvsAudioResolution audioEditRes = new NvsAudioResolution();
//        audioEditRes.sampleRate = 44100;
//        audioEditRes.channelCount = 2;
//
//
//        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
//        if(timeline == null) {
//            Log.e(TAG, "failed to create timeline");
//            return null;
//        }
//
//        if(!buildVideoTrack(timeline)) {
//            return timeline;
//        }
//
//        timeline.appendAudioTrack(); // 音乐轨道
//        timeline.appendAudioTrack(); // 录音轨道
//
//        setTimelineData(timeline);
//
//        return timeline;
//    }
//
//
//    public static void setTimelineData(NvsTimeline timeline) {
//        if(timeline == null)
//            return;
//        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
//        MusicInfo musicInfoClone = TimelineData.instance().cloneMusicData();
//        String themeId = TimelineData.instance().getThemeData();
//        applyTheme(timeline,themeId);
//
//        if(musicInfoClone != null) {
//            TimelineData.instance().setMusicData(musicInfoClone);
//            buildTimelineMusic(timeline, musicInfoClone);
//        }
//
//        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
//        buildTimelineFilter(timeline, videoClipFxData);
//        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
//        setTransition(timeline, transitionInfo);
//        ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
//        setSticker(timeline, stickerArray);
//
//        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
//        setCaption(timeline, captionArray);
//
//        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();
//        buildTimelineRecordAudio(timeline, recordArray);
//    }
//
//    public static boolean removeTimeline(NvsTimeline timeline){
//        if(timeline == null)
//            return false;
//
//        NvsStreamingContext context = NvsStreamingContext.getInstance();
//        if(context == null)
//            return false;
//
//        return context.removeTimeline(timeline);
//    }
//
//    public static boolean buildVideoTrack(NvsTimeline timeline) {
//        if(timeline == null) {
//            return false;
//        }
//
//        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
//        if(videoTrack == null){
//            Log.e(TAG, "failed to append video track");
//            return false;
//        }
//        addVideoClip(videoTrack);
//        float videoVolume = TimelineData.instance().getOriginVideoVolume();
//        videoTrack.setVolumeGain(videoVolume,videoVolume);
//
//        return true;
//    }
//
//    public static boolean reBuildVideoTrack(NvsTimeline timeline) {
//        if(timeline == null) {
//            return false;
//        }
//        int videoTrackCount = timeline.videoTrackCount();
//        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
//        if(videoTrack == null){
//            Log.e(TAG, "failed to append video track");
//            return false;
//        }
//        videoTrack.removeAllClips();
//        addVideoClip(videoTrack);
//        setTimelineData(timeline);
//        float videoVolume = TimelineData.instance().getOriginVideoVolume();
//        videoTrack.setVolumeGain(videoVolume,videoVolume);
//
//        return true;
//    }
//
//    private static void addVideoClip(NvsVideoTrack videoTrack){
//        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
//        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
//        for (int i = 0;i < videoClipArray.size();i++) {
//            ClipInfo clipInfo = videoClipArray.get(i);
//            String filePath = clipInfo.getFilePath();
//            NvsVideoClip videoClip = videoTrack.appendClip(filePath);
//            if (videoClip == null)
//                continue;
//
//            if(blurFlag) {
//                videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
//            }
//            int videoType = videoClip.getVideoType();
//            if(videoType == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE){//当前片段是图片
//                long trimIn = videoClip.getTrimIn();
//                long trimOut = clipInfo.getTrimOut();
//                if(trimOut > 0 && trimOut > trimIn ) {
//                    videoClip.changeTrimOutPoint(trimOut, true);
//                }
//                int imgDisplayMode = clipInfo.getImgDispalyMode();
//                if(imgDisplayMode == Constants.EDIT_MODE_PHOTO_AREA_DISPLAY){//区域显示
//                    videoClip.setImageMotionMode(NvsVideoClip.IMAGE_CLIP_MOTIONMMODE_ROI);
//                    RectF normalStartRectF = clipInfo.getNormalStartROI();
//                    RectF normalEndRectF = clipInfo.getNormalEndROI();
//                    if(normalStartRectF != null && normalEndRectF != null){
//                        videoClip.setImageMotionROI(normalStartRectF,normalEndRectF);
//                    }
//                }else {//全图显示
//                    videoClip.setImageMotionMode(NvsVideoClip.CLIP_MOTIONMODE_LETTERBOX_ZOOMIN);
//                }
//
//                boolean isOpenMove = clipInfo.isOpenPhotoMove();
//                videoClip.setImageMotionAnimationEnabled(isOpenMove);
//            }
//            else{//当前片段是视频
//                float speed = clipInfo.getSpeed();
//                videoClip.changeSpeed(speed);
//
//                long trimIn = clipInfo.getTrimIn();
//                long trimOut = clipInfo.getTrimOut();
//                if(trimIn > 0) {
//                    videoClip.changeTrimInPoint(trimIn, true);
//                }
//
//                if(trimOut > 0 && trimOut > trimIn) {
//                    videoClip.changeTrimOutPoint(trimOut, true);
//                }
//
//                float volumeGain = clipInfo.getVolume();
//                videoClip.setVolumeGain(volumeGain,volumeGain);
//                videoClip.setExtraVideoRotation(clipInfo.getRotateAngle());
//                NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx("Transform 2D");
//                if(videoFxTransform != null){
//                    videoFxTransform.setFloatVal("Scale X",clipInfo.getScaleX());
//                    videoFxTransform.setFloatVal("Scale Y",clipInfo.getScaleY());
//                }
//            }
//
//            NvsVideoFx videoFxColor = videoClip.appendBuiltinFx("Color Property");
//            if(videoFxColor != null){
//                videoFxColor.setFloatVal("Brightness",clipInfo.getBrightnessVal());
//                videoFxColor.setFloatVal("Contrast",clipInfo.getContrastVal());
//                videoFxColor.setFloatVal("Saturation",clipInfo.getSaturationVal());
//            }
//        }
//    }
//
//    public static boolean buildTimelineFilter(NvsTimeline timeline, VideoClipFxInfo videoClipFxData) {
//        if(timeline == null) {
//            return false;
//        }
//
//        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
//        if(videoTrack == null) {
//            return false;
//        }
//
//        if(videoClipFxData == null)
//            return false;
//
//        ArrayList<ClipInfo> clipInfos = TimelineData.instance().getClipInfoData();
//
//        int videoClipCount = videoTrack.getClipCount();
//        for(int i = 0;i<videoClipCount;i++) {
//            NvsVideoClip clip = videoTrack.getClipByIndex(i);
//            if(clip == null)
//                continue;
//
//            removeAllVideoFx(clip);
//            String clipFilPath = clip.getFilePath();
//            boolean isSrcVideoAsset = false;
//            for(ClipInfo clipInfo :clipInfos) {
//                String videoFilePath = clipInfo.getFilePath();
//                if(clipFilPath.equals(videoFilePath)){
//                    isSrcVideoAsset = true;
//                    break;
//                }
//            }
//
//            if(!isSrcVideoAsset)
//                continue;
//
//            String name = videoClipFxData.getFxId();
//            if(TextUtils.isEmpty(name)) {
//                continue;
//            }
//            int mode = videoClipFxData.getFxMode();
//            float fxIntensity = videoClipFxData.getFxIntensity();
//            if(mode == FilterItem.FILTERMODE_BUILTIN){//内建特效
//                NvsVideoFx fx = clip.appendBuiltinFx(name);
//                fx.setFilterIntensity(fxIntensity);
//            }else {////添加包裹特效
//                NvsVideoFx fx = clip.appendPackagedFx(name);
//                fx.setFilterIntensity(fxIntensity);
//            }
//        }
//
//        return true;
//    }
//
//    public static boolean applyTheme(NvsTimeline timeline, String themeId) {
//        if(timeline == null)
//            return false;
//
//        timeline.removeCurrentTheme();
//        if (themeId == null || themeId.isEmpty())
//            return false;
//
//        if(!timeline.applyTheme(themeId)) {
//            Log.e(TAG, "failed to apply theme");
//            return false;
//        }
//        timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
//
//        // 应用主题之后，要把已经应用的背景音乐去掉
//        TimelineData.instance().setMusicData(null);
//        TimelineUtil.buildTimelineMusic(timeline, null);
//        return true;
//    }
//
//    private static boolean removeAllVideoFx(NvsVideoClip videoClip) {
//        if(videoClip == null)
//            return false;
//
//        int fxCount = videoClip.getFxCount();
//        for(int i=0;i<fxCount;i++) {
//            NvsVideoFx fx = videoClip.getFxByIndex(i);
//            if(fx == null)
//                continue;
//
//            String name = fx.getBuiltinVideoFxName();
//            Log.e("===>", "fx name: " + name);
//            if(name.equals("Color Property") || name.equals("Transform 2D")) {
//                continue;
//            }
//            videoClip.removeFx(i);
//            i--;
//        }
//        return true;
//    }
//
//    public static boolean setTransition(NvsTimeline timeline, TransitionInfo transitionInfo) {
//        if(timeline == null) {
//            return false;
//        }
//
//        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
//        if(videoTrack == null) {
//            return false;
//        }
//
//        if(transitionInfo == null)
//            return false;
//
//        int videoClipCount = videoTrack.getClipCount();
//        if(videoClipCount <= 1)
//            return false;
//
//        for(int i = 0;i<videoClipCount - 1;i++) {
//            if(transitionInfo.getTransitionMode() == TransitionInfo.TRANSITIONMODE_BUILTIN) {
//                videoTrack.setBuiltinTransition(i, transitionInfo.getTransitionId());
//            } else {
//                videoTrack.setPackagedTransition(i, transitionInfo.getTransitionId());
//            }
//        }
//
//        return true;
//    }
//
//    public static boolean buildTimelineMusic(NvsTimeline timeline, MusicInfo musicInfo) {
//        if(timeline == null) {
//            return false;
//        }
//        NvsAudioTrack audioTrack =timeline.getAudioTrackByIndex(0);
//        if(musicInfo == null) {
//            if(audioTrack != null) {
//                audioTrack.removeAllClips();
//            }
//
//            // 去掉音乐之后，要把已经应用的主题中的音乐还原
//            String pre_theme_id = TimelineData.instance().getThemeData();
//            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
//                timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
//            }
//            return false;
//        }
//        if(audioTrack != null) {
//            audioTrack.removeAllClips();
//            audioTrack.addClip(musicInfo.getFilePath(), 0, musicInfo.getTrimIn(), musicInfo.getTrimOut());
//            float audioVolume = TimelineData.instance().getMusicVolume();
//            audioTrack.setVolumeGain(audioVolume,audioVolume);
//        }
//
//        // 应用音乐之后，要把已经应用的主题中的音乐去掉
//        String pre_theme_id = TimelineData.instance().getThemeData();
//        if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
//            timeline.setThemeMusicVolumeGain(0 , 0);
//        }
//        return true;
//    }
//
//    public static void buildTimelineRecordAudio(NvsTimeline timeline, ArrayList<RecordAudioInfo> recordAudioInfos) {
//        if(timeline == null) {
//            return;
//        }
//        NvsAudioTrack audioTrack =timeline.getAudioTrackByIndex(1);
//        if(audioTrack != null) {
//            audioTrack.removeAllClips();
//            if(recordAudioInfos != null) {
//                for (int i = 0; i < recordAudioInfos.size(); ++i) {
//                    RecordAudioInfo recordAudioInfo = recordAudioInfos.get(i);
//                    if (recordAudioInfo == null) {
//                        continue;
//                    }
//                    NvsAudioClip audioClip = audioTrack.addClip(recordAudioInfo.getPath(), recordAudioInfo.getInPoint(), recordAudioInfo.getTrimIn(),
//                            recordAudioInfo.getOutPoint() - recordAudioInfo.getInPoint() + recordAudioInfo.getTrimIn());
//                    if(audioClip != null) {
//                        audioClip.setVolumeGain(recordAudioInfo.getVolume(), recordAudioInfo.getVolume());
//                        if(recordAudioInfo.getFxID() != null && !recordAudioInfo.getFxID().equals(Constants.NO_FX)) {
//                            audioClip.appendFx(recordAudioInfo.getFxID());
//                        }
//                    }
//                }
//            }
//            float audioVolume = TimelineData.instance().getRecordVolume();
//            audioTrack.setVolumeGain(audioVolume,audioVolume);
//        }
//    }
//
//    public static boolean setSticker(NvsTimeline timeline, ArrayList<StickerInfo> stickerArray) {
//        if(timeline == null)
//            return false;
//
//        NvsTimelineAnimatedSticker deleteSticker = timeline.getFirstAnimatedSticker();
//        while (deleteSticker != null) {
//            deleteSticker = timeline.removeAnimatedSticker(deleteSticker);
//        }
//
//        for(StickerInfo sticker : stickerArray) {
//            NvsTimelineAnimatedSticker newSticker = timeline.addAnimatedSticker(sticker.getInPoint(), sticker.getDuration(), sticker.getId());
//            if(newSticker == null)
//                continue;
//            newSticker.setZValue(sticker.getAnimateStickerZVal());
//            newSticker.setHorizontalFlip(sticker.isHorizFlip());
//            PointF translation = sticker.getTranslation();
//            float scaleFactor = sticker.getScaleFactor();
//            float rotation = sticker.getRotation();
//            newSticker.setScale(scaleFactor);
//            newSticker.setRotationZ(rotation);
//            newSticker.setTranslation(translation);
//            float volumeGain = sticker.getVolumeGain();
//            newSticker.setVolumeGain(volumeGain,volumeGain);
//        }
//        return true;
//    }
//
//    public static boolean setCaption(NvsTimeline timeline, ArrayList<CaptionInfo> captionArray) {
//        if(timeline == null)
//            return false;
//
//        NvsTimelineCaption deleteCaption = timeline.getFirstCaption();
//        while (deleteCaption != null) {
//            deleteCaption = timeline.removeCaption(deleteCaption);
//        }
//
//        for(CaptionInfo caption : captionArray) {
//            NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getInPoint(),
//                    caption.getDuration(),null);
//            updateCaptionAttribute(newCaption,caption);
//        }
//        return true;
//    }
//
//    private static void updateCaptionAttribute(NvsTimelineCaption newCaption, CaptionInfo caption){
//        if(newCaption == null || caption == null)
//            return;
//
//        //字幕StyleUuid需要首先设置，后面设置的字幕属性才会生效，
//        // 因为字幕样式里面可能自带偏移，缩放，旋转等属性，最后设置会覆盖前面的设置的。
//        newCaption.applyCaptionStyle(caption.getCaptionStyleUuid());
//        int alignVal = caption.getAlignVal();
//        if(alignVal >= 0)
//            newCaption.setTextAlignment(alignVal);
//        NvsColor textColor = ColorUtil.colorStringtoNvsColor(caption.getCaptionColor());
//        if(textColor != null){
//            textColor.a = caption.getCaptionColorAlpha() / 100.0f;
//            newCaption.setTextColor(textColor);
//        }
//
//        // 放缩字幕
//        float scaleFactorX = caption.getScaleFactorX();
//        float scaleFactorY = caption.getScaleFactorY();
//        newCaption.setScaleX(scaleFactorX);
//        newCaption.setScaleY(scaleFactorY);
//
//        float rotation = caption.getRotation();
//        // 旋转字幕
//        newCaption.setRotationZ(rotation);
//        newCaption.setZValue(caption.getCaptionZVal());
//        boolean hasOutline = caption.isHasOutline();
//        if(hasOutline){
//            newCaption.setDrawOutline(hasOutline);
//            NvsColor outlineColor = ColorUtil.colorStringtoNvsColor(caption.getOutlineColor());
//            if(outlineColor != null){
//                outlineColor.a = caption.getOutlineColorAlpha() / 100.0f;
//                newCaption.setOutlineColor(outlineColor);
//                newCaption.setOutlineWidth(caption.getOutlineWidth());
//            }
//        }
//        String fontPath = caption.getCaptionFont();
//        if(!fontPath.isEmpty())
//            newCaption.setFontByFilePath(fontPath);
//        boolean isBold = caption.isBold();
//
//        newCaption.setBold(isBold);
//        boolean isItalic = caption.isItalic();
//        newCaption.setItalic(isItalic);
//        boolean isShadow = caption.isShadow();
//        newCaption.setDrawShadow(isShadow);
//        if(isShadow) {
//            PointF offset = new PointF(7, -7);
//            NvsColor shadowColor = new NvsColor(0, 0, 0, 0.5f);
//            newCaption.setShadowOffset(offset);  //字幕阴影偏移量
//            newCaption.setShadowColor(shadowColor); // 字幕阴影颜色
//        }
//        float fontSize = caption.getCaptionSize();
//        if(fontSize >= 0)
//            newCaption.setFontSize(fontSize);
//        PointF translation = caption.getTranslation();
//        if(translation != null)
//            newCaption.setCaptionTranslation(translation);
//    }
//}
