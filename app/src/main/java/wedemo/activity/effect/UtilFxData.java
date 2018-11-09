package wedemo.activity.effect;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by zd on 2017/6/13.
 */

public class UtilFxData {

    private static final String TAG = UtilFxData.class.getName();
    private static UtilFxData util;
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


    private UtilFxData(Context context) {
        mContext = context.getApplicationContext();
    }

    public static UtilFxData init(Context context) {
        if (util == null) {
            synchronized (UtilFxData.class) {
                if (util == null) {
                    util = new UtilFxData(context);
                }
            }
        }
        return util;
    }

    public static UtilFxData instance() {
        return util;
    }
}
