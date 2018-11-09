package com.sven.huinews.international.utils.shot.utils.dataInfo;

import android.util.Log;

public class VideoClipFxInfo {
    public static int FXMODE_BUILTIN = 0;
    public static int FXMODE_PACKAGE = 1;

    private int m_fxMode;
    private String m_fxId;
    private float m_fxIntensity;

    public VideoClipFxInfo() {
        m_fxId = null;
        m_fxMode = FXMODE_BUILTIN;
        m_fxIntensity = 0.5f;
    }

    public void setFxMode(int mode) {
        if(mode != FXMODE_BUILTIN && mode != FXMODE_PACKAGE) {
            Log.e("", "invalid mode data");
            return;
        }
        m_fxMode = mode;
    }

    public int getFxMode() {
        return m_fxMode;
    }

    public void setFxId(String fxId) {
        m_fxId = fxId;
    }

    public String getFxId() {
        return m_fxId;
    }

    public void setFxIntensity(float intensity) {
        m_fxIntensity = intensity;
    }

    public float getFxIntensity() {
        return m_fxIntensity;
    }
}
