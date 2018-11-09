package com.sven.huinews.international.utils.shot;

import com.meicam.sdk.NvsStreamingContext;

import java.io.Serializable;

/**
 * Created by admin on 2018-5-28.
 */

public class ParameterSettingValues implements Serializable {
    private static ParameterSettingValues parameterValues;
    private int m_captureResolutionGrade;
    private int m_compileResolutionGrade;
    private double m_compileBitrate;
    private boolean m_disableDeviceEncorder;
    private boolean m_isUseBackgroudBlur;

    public ParameterSettingValues() {
        m_captureResolutionGrade = NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH;
        m_compileResolutionGrade = NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_720;
        m_compileBitrate = 0;
        m_disableDeviceEncorder = false;
        m_isUseBackgroudBlur = false;
    }

    public static ParameterSettingValues instance() {
        if (parameterValues == null) {
            parameterValues = init();
        }
        return getParameterValues();
    }

    public static ParameterSettingValues init() {
        if (parameterValues == null) {
            synchronized (ParameterSettingValues.class) {
                if (parameterValues == null) {
                    parameterValues = new ParameterSettingValues();
                }
            }
        }
        return parameterValues;
    }

    public static void setParameterValues(ParameterSettingValues values) {
        parameterValues = values;
    }

    public static ParameterSettingValues getParameterValues() {
        return parameterValues;
    }

    public int getCaptureResolutionGrade() {
        return m_captureResolutionGrade;
    }

    public void setCaptureResolutionGrade(int captureRatio) {
        this.m_captureResolutionGrade = captureRatio;
    }

    public int getCompileResolutionGrade() {
        return m_compileResolutionGrade;
    }

    public void setCompileResolutionGrade(int outputRatio) {
        this.m_compileResolutionGrade = outputRatio;
    }

    public double getCompileBitrate() {
        return m_compileBitrate;
    }

    public void setCompileBitrate(double bitrate) {
        this.m_compileBitrate = bitrate;
    }

    public boolean disableDeviceEncorder() {
        return m_disableDeviceEncorder;
    }

    public void setDisableDeviceEncorder(boolean useDeviceEncorder) {
        m_disableDeviceEncorder = useDeviceEncorder;
    }

    public boolean isUseBackgroudBlur() {
        return m_isUseBackgroudBlur;
    }

    public void setUseBackgroudBlur(boolean useBackgroudBlur) {
        m_isUseBackgroudBlur = useBackgroudBlur;
    }
}
