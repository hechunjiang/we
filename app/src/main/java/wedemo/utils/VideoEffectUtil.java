package wedemo.utils;

import android.content.Context;

import java.util.ArrayList;

public class VideoEffectUtil {
    private static final String TAG = VideoEffectUtil.class.getName();
    private static VideoEffectUtil util;
    private Context mContext;
    private ArrayList<Boolean> mBeautyBooleanData;
    private ArrayList<Double> mStrengthDoubleData;
    private ArrayList<Double> mWhiteningDoubleData;
    private ArrayList<Long> mMusicIimeFloatData;
    private ArrayList<Long> mMusicOutIimeFloatData;
    private ArrayList<String> mFilterNameStringData;
    private ArrayList<Float> mSpeedFloatData;

    public ArrayList<Boolean> getBeautyBooleanData() {
        return mBeautyBooleanData;
    }

    public void setBeautyBooleanData(ArrayList<Boolean> boolData) {
        this.mBeautyBooleanData = boolData;
    }

    public ArrayList<Double> getStrengthDoubleData() {
        return mStrengthDoubleData;
    }

    public void setStrengthDoubleData(ArrayList<Double> doubleData) {
        this.mStrengthDoubleData = doubleData;
    }

    public ArrayList<Double> getwhiteningDoubleData() {
        return mWhiteningDoubleData;
    }

    public void setwhiteningDoubleData(ArrayList<Double> doubleData) {
        this.mWhiteningDoubleData = doubleData;
    }

    public ArrayList<Long> getmusicTimeFloatData() {
        return mMusicIimeFloatData;
    }

    public void setmusicTimeFloatData(ArrayList<Long> floatData) {
        this.mMusicIimeFloatData = floatData;
    }

    public ArrayList<Long> getmusicOutTimeFloatData() {
        return mMusicOutIimeFloatData;
    }

    public void setmusicOutTimeFloatData(ArrayList<Long> floatData) {
        this.mMusicOutIimeFloatData = floatData;
    }

    public void setspeedFloatData(ArrayList<Float> floatData) {
        this.mSpeedFloatData = floatData;
    }

    public ArrayList<Float> getspeedFloatData() {
        return this.mSpeedFloatData;
    }


    public ArrayList<String> getfilterNameStringData() {
        return mFilterNameStringData;
    }

    public void setfilterNameStringData(ArrayList<String> stringData) {
        this.mFilterNameStringData = stringData;
    }


    private VideoEffectUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    public static VideoEffectUtil init(Context context) {
        if (util == null) {
            synchronized (Util.class) {
                if (util == null) {
                    util = new VideoEffectUtil(context);
                }
            }
        }
        return util;
    }

    public static VideoEffectUtil instance() {
        return util;
    }
}
