package wedemo.utils.dataInfo;

public class VideoClipFxInfo {
    public static int FXMODE_BUILTIN = 0;
    public static int FXMODE_PACKAGE = 1;
    private int m_fxMode;
    private String m_fxId;
    private float m_fxIntensity;
    private int selectType;
    private int selectNum;
    private int exposure = 0;
    private double mStrengthValue = 0;
    private double mWhiteningValue = 0;
    private double mReddeningValue = 0;

    public VideoClipFxInfo() {
        m_fxId = null;
        m_fxMode = FXMODE_BUILTIN;
        m_fxIntensity = 1f;
        selectType = 0;
        selectNum = 0;
    }

    public VideoClipFxInfo clone() {
        VideoClipFxInfo videoClipFxInfo = new VideoClipFxInfo();
        videoClipFxInfo.setFxId(this.getFxId());
        videoClipFxInfo.setFxMode(this.getFxMode());
        videoClipFxInfo.setFxIntensity(this.getFxIntensity());
        videoClipFxInfo.setSelectType(this.getSelectType());
        videoClipFxInfo.setSelectNum(this.getSelectNum());
        videoClipFxInfo.setExposure(this.getExposure());
        videoClipFxInfo.setmReddeningValue(this.getmReddeningValue());
        videoClipFxInfo.setmStrengthValue(this.getmStrengthValue());
        videoClipFxInfo.setmWhiteningValue(this.getmWhiteningValue());
        return videoClipFxInfo;
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
    }

    public double getmStrengthValue() {
        return mStrengthValue;
    }

    public void setmStrengthValue(double mStrengthValue) {
        this.mStrengthValue = mStrengthValue;
    }

    public double getmWhiteningValue() {
        return mWhiteningValue;
    }

    public void setmWhiteningValue(double mWhiteningValue) {
        this.mWhiteningValue = mWhiteningValue;
    }

    public double getmReddeningValue() {
        return mReddeningValue;
    }

    public void setmReddeningValue(double mReddeningValue) {
        this.mReddeningValue = mReddeningValue;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public void setFxMode(int mode) {
//        if(mode != FXMODE_BUILTIN && mode != FXMODE_PACKAGE) {
//            Log.e("", "invalid mode data");
//            return;
//        }
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
