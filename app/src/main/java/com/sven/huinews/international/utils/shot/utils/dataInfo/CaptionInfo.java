package com.sven.huinews.international.utils.shot.utils.dataInfo;

import android.graphics.PointF;

/**
 * Created by liuluwei on 2017/12/9.
 */

public class CaptionInfo {
    private String m_text;//字幕文本内容
    private float m_scaleFactorX;//X缩放因子
    private float m_scaleFactorY;//Y缩放因子
    private PointF m_anchor;//锚点
    private PointF m_translation;//字幕偏移量
    private float m_rotation;//旋转角度

    //字幕样式属性
    private int m_alignVal;//字幕对齐值
    private long m_inPoint;//字幕入点
    private long m_duration;//字幕时长
    private String m_captionStyleUuid;//字幕样式Uuid
    private String m_captionColor;//字幕颜色
    private int m_captionColorAlpha;//字幕颜色不透明度
    private boolean m_hasOutline;//是否有描边
    private String m_outlineColor;//描边颜色
    private int m_outlineColorAlpha;//描边颜色不透明度
    private float m_outlineWidth;//描边宽度
    private String m_captionFont;//字幕字体
    private boolean m_isBold;//是否加粗
    private boolean m_isItalic;//是否是斜体
    private boolean m_isShadow;//是否有阴影
    private float m_captionSize;//字体大小
    private int m_captionZVal;//字幕Z值

    //字幕种类。注解：值为0是默认字幕即未使用字幕样式的字幕，值为1表示是用户自定义种类即使用字幕样式的字幕，
    // 值为2是主题字幕
    private int m_captionCategory;

    public int getCaptionCategory() {
        return m_captionCategory;
    }

    public void setCaptionCategory(int captionCategory) {
        this.m_captionCategory = captionCategory;
    }

    public int getAlignVal() {
        return m_alignVal;
    }

    public void setAlignVal(int alignVal) {
        this.m_alignVal = alignVal;
    }
    public int getCaptionZVal() {
        return m_captionZVal;
    }

    public void setCaptionZVal(int m_captionZVal) {
        this.m_captionZVal = m_captionZVal;
    }

    public long getInPoint() {
        return m_inPoint;
    }

    public void setInPoint(long inPoint) {
        this.m_inPoint = inPoint;
    }
    public long getDuration() {
        return m_duration;
    }

    public void setDuration(long duration) {
        this.m_duration = duration;
    }
    public String getCaptionStyleUuid() {
        return m_captionStyleUuid;
    }

    public void setCaptionStyleUuid(String captionStyleUuid) {
        this.m_captionStyleUuid = captionStyleUuid;
    }

    public String getCaptionColor() {
        return m_captionColor;
    }

    public void setCaptionColor(String captionColor) {
        this.m_captionColor = captionColor;
    }

    public int getCaptionColorAlpha() {
        return m_captionColorAlpha;
    }

    public void setCaptionColorAlpha(int captionColorAlpha) {
        this.m_captionColorAlpha = captionColorAlpha;
    }

    public boolean isHasOutline() {
        return m_hasOutline;
    }

    public void setHasOutline(boolean hasOutline) {
        this.m_hasOutline = hasOutline;
    }

    public String getOutlineColor() {
        return m_outlineColor;
    }

    public void setOutlineColor(String outlineColor) {
        this.m_outlineColor = outlineColor;
    }

    public int getOutlineColorAlpha() {
        return m_outlineColorAlpha;
    }

    public void setOutlineColorAlpha(int outlineColorAlpha) {
        this.m_outlineColorAlpha = outlineColorAlpha;
    }

    public float getOutlineWidth() {
        return m_outlineWidth;
    }

    public void setOutlineWidth(float outlineWidth) {
        this.m_outlineWidth = outlineWidth;
    }

    public String getCaptionFont() {
        return m_captionFont;
    }

    public void setCaptionFont(String captionFont) {
        this.m_captionFont = captionFont;
    }

    public boolean isBold() {
        return m_isBold;
    }

    public void setBold(boolean isBold) {
        this.m_isBold = isBold;
    }

    public boolean isItalic() {
        return m_isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.m_isItalic = isItalic;
    }

    public boolean isShadow() {
        return m_isShadow;
    }

    public void setShadow(boolean isShadow) {
        this.m_isShadow = isShadow;
    }

    public float getCaptionSize() {
        return m_captionSize;
    }

    public void setCaptionSize(float captionSize) {
        this.m_captionSize = captionSize;
    }

    public CaptionInfo() {
        m_text = null;
        m_scaleFactorX = 1.0f;
        m_scaleFactorY = 1.0f;
        m_anchor = null;
        m_rotation = 0;
        m_translation = null;
        m_alignVal = -1;
        m_inPoint = 0;
        m_duration = 0;
        m_captionStyleUuid = "";
        m_captionColor = "";
        m_captionColorAlpha = 100;
        m_hasOutline = false;
        m_outlineColor = "";
        m_outlineColorAlpha = 100;
        m_outlineWidth = 8.0f;
        m_captionFont = "";
        m_isBold = true;
        m_isItalic = false;
        m_isShadow = false;
        m_captionSize = -1;
        m_captionZVal = 0;
        m_captionCategory = 0;
    }

    public void setText(String text) {
        m_text = text;
    }

    public String getText() {
        return m_text;
    }
    
    public void setScaleFactorX(float value) {
        m_scaleFactorX = value;
    }

    public float getScaleFactorX() {
        return m_scaleFactorX;
    }
    public void setScaleFactorY(float value) {
        m_scaleFactorY = value;
    }

    public float getScaleFactorY() {
        return m_scaleFactorY;
    }

    public void setAnchor(PointF point) {
        m_anchor = point;
    }

    public PointF getAnchor() {
        return m_anchor;
    }

    public void setRotation(float value) {
        m_rotation = value;
    }

    public float getRotation() {
        return m_rotation;
    }

    public void setTranslation(PointF point) {
        m_translation = point;
    }

    public PointF getTranslation() {
        return m_translation;
    }

    public CaptionInfo clone(){
        CaptionInfo newCaptionInfo = new CaptionInfo();
        newCaptionInfo.setText(this.getText());
        newCaptionInfo.setCaptionColor(this.getCaptionColor());
        newCaptionInfo.setAnchor(this.getAnchor());
        newCaptionInfo.setRotation(this.getRotation());
        newCaptionInfo.setScaleFactorX(this.getScaleFactorX());
        newCaptionInfo.setScaleFactorY(this.getScaleFactorY());
        newCaptionInfo.setTranslation(this.getTranslation());
        //copy data
        newCaptionInfo.setAlignVal(this.getAlignVal());
        newCaptionInfo.setInPoint(this.getInPoint());
        newCaptionInfo.setDuration(this.getDuration());
        newCaptionInfo.setCaptionZVal(this.getCaptionZVal());
        newCaptionInfo.setCaptionStyleUuid(this.getCaptionStyleUuid());
        newCaptionInfo.setCaptionColor(this.getCaptionColor());
        newCaptionInfo.setCaptionColorAlpha(this.getCaptionColorAlpha());
        newCaptionInfo.setHasOutline(this.isHasOutline());
        newCaptionInfo.setOutlineColor(this.getOutlineColor());
        newCaptionInfo.setOutlineColorAlpha(this.getOutlineColorAlpha());
        newCaptionInfo.setOutlineWidth(this.getOutlineWidth());
        newCaptionInfo.setCaptionFont(this.getCaptionFont());
        newCaptionInfo.setBold(this.isBold());
        newCaptionInfo.setItalic(this.isItalic());
        newCaptionInfo.setShadow(this.isShadow());
        newCaptionInfo.setCaptionSize(this.getCaptionSize());

        //字幕种类
        newCaptionInfo.setCaptionCategory(this.getCaptionCategory());
        return newCaptionInfo;
    }
}
