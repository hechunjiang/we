package com.sven.huinews.international.utils.shot.utils.dataInfo;

import android.graphics.RectF;

import com.meicam.sdk.NvsVideoStreamInfo;
import com.sven.huinews.international.utils.shot.utils.Constants;

public class ClipInfo {
    public boolean isRecFile = false;
    public int rotation = NvsVideoStreamInfo.VIDEO_ROTATION_0;
    private String m_filePath;
    private float m_speed;
    private boolean m_mute;
    private long m_trimIn;
    private long m_trimOut;

    //校色数据
    private float m_brightnessVal;
    private float m_contrastVal;
    private float m_saturationVal;
    //
    //音量
    private int m_volume;

    //调整
    private int m_rotateAngle;//旋转角度
    private int m_scaleX;//
    private int m_scaleY;



    //图片展示模式
    private int m_imgDispalyMode = Constants.EDIT_MODE_PHOTO_AREA_DISPLAY;
    //是否开启图片运动
    private boolean isOpenPhotoMove = true;
    //图片起始ROI
    private RectF m_normalStartROI;
    //图片终止ROI
    private RectF m_normalEndROI;

    public RectF getNormalStartROI() {
        return m_normalStartROI;
    }

    public void setNormalStartROI(RectF normalStartROI) {
        this.m_normalStartROI = normalStartROI;
    }
    public RectF getNormalEndROI() {
        return m_normalEndROI;
    }

    public void setNormalEndROI(RectF normalEndROI) {
        this.m_normalEndROI = normalEndROI;
    }

    public boolean isOpenPhotoMove() {
        return isOpenPhotoMove;
    }

    public void setOpenPhotoMove(boolean openPhotoMove) {
        isOpenPhotoMove = openPhotoMove;
    }
    public int getImgDispalyMode() {
        return m_imgDispalyMode;
    }

    public void setImgDispalyMode(int imgDispalyMode) {
        m_imgDispalyMode = imgDispalyMode;
    }
    public int getScaleX() {
        return m_scaleX;
    }

    public void setScaleX(int scaleX) {
        this.m_scaleX = scaleX;
    }

    public int getScaleY() {
        return m_scaleY;
    }

    public void setScaleY(int scaleY) {
        this.m_scaleY = scaleY;
    }

    public int getRotateAngle() {
        return m_rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.m_rotateAngle = rotateAngle;
    }
    public int getVolume() {
        return m_volume;
    }

    public void setVolume(int volume) {
        this.m_volume = volume;
    }
    public float getBrightnessVal() {
        return m_brightnessVal;
    }

    public void setBrightnessVal(float brightnessVal) {
        this.m_brightnessVal = brightnessVal;
    }

    public float getContrastVal() {
        return m_contrastVal;
    }

    public void setContrastVal(float contrastVal) {
        this.m_contrastVal = contrastVal;
    }

    public float getSaturationVal() {
        return m_saturationVal;
    }

    public void setSaturationVal(float saturationVal) {
        this.m_saturationVal = saturationVal;
    }

    public ClipInfo() {
        m_filePath = null;
        m_speed = 1f;
        m_mute = false;
        m_trimIn = -1;
        m_trimOut = -1;
        m_brightnessVal = 1.0f;
        m_contrastVal = 1.0f;
        m_saturationVal = 1.0f;
        m_volume = 50;
        m_rotateAngle = 0;
        m_scaleX = 1;//
        m_scaleY = 1;
    }


    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }

    public String getFilePath() {
        return m_filePath;
    }

    public void setSpeed(float speed) {
        m_speed = speed;
    }

    public float getSpeed() {
        return m_speed;
    }

    public void setMute(boolean flag) {
        m_mute = flag;
    }

    public boolean getMute() {
        return m_mute;
    }

    public void changeTrimIn(long data) {
        m_trimIn = data;
    }

    public long getTrimIn() {
        return m_trimIn;
    }

    public void changeTrimOut(long data) {
        m_trimOut = data;
    }

    public long getTrimOut() {
        return m_trimOut;
    }

    public ClipInfo clone(){
        ClipInfo newClipInfo = new ClipInfo();
        newClipInfo.isRecFile = this.isRecFile;
        newClipInfo.rotation = this.rotation;
        newClipInfo.setFilePath(this.getFilePath());
        newClipInfo.setMute(this.getMute());
        newClipInfo.setSpeed(this.getSpeed());
        newClipInfo.changeTrimIn(this.getTrimIn());
        newClipInfo.changeTrimOut(this.getTrimOut());

        //copy data
        newClipInfo.setBrightnessVal(this.getBrightnessVal());
        newClipInfo.setSaturationVal(this.getSaturationVal());
        newClipInfo.setContrastVal(this.getContrastVal());
        newClipInfo.setVolume(this.getVolume());
        newClipInfo.setRotateAngle(this.getRotateAngle());
        newClipInfo.setScaleX(this.getScaleX());
        newClipInfo.setScaleY(this.getScaleY());

        //图片数据
        newClipInfo.setImgDispalyMode(this.getImgDispalyMode());
        newClipInfo.setOpenPhotoMove(this.isOpenPhotoMove());
        newClipInfo.setNormalStartROI(this.getNormalStartROI());
        newClipInfo.setNormalEndROI(this.getNormalEndROI());
        return newClipInfo;
    }
}
