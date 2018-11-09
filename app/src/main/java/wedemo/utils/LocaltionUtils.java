package wedemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.sven.huinews.international.utils.LogUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LocaltionUtils {

    private final LocationManager locationManager;
    private float longitude = 0;
    private float latitude = 0;
    private String country = null;
    private LocationListener locationListener;
    private Activity activity;

    private OnLocationListener onLocationListener;

    @SuppressLint("MissingPermission")
    public LocaltionUtils(Activity activity, OnLocationListener onLocationListener) {
        this.activity = activity;
        this.onLocationListener = onLocationListener;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        initListiner();

        // 判断GPS是否正常启动
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            // 返回开启GPS导航设置界面
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            activity.startActivityForResult(intent, 0);
//            return;
//        }
        String provider = locationManager.getBestProvider(getCriteria(), true); // 获取GPS信息
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        if (location != null) {
            if (this.onLocationListener != null) {
                //经度
                longitude = (float) location.getLongitude();
                //纬度
                latitude = (float) location.getLatitude();
                onLocationListener.onLocationSuccess(longitude, latitude);
            }
            initCountry(longitude, latitude);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //开启网络定位
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void initListiner() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //经度
                longitude = (float) location.getLongitude();
                //纬度
                latitude = (float) location.getLatitude();
                if (onLocationListener != null) {
                    onLocationListener.onLocationSuccess(longitude, latitude);
                }

                initCountry(longitude, latitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    case 0:
                        LogUtil.showLog("当前GPS状态为服务区外状态");
                        break;
                    case 1:
                        LogUtil.showLog("当前GPS状态为暂停服务状态");
                        break;
                    case 2:
                        LogUtil.showLog("当前GPS状态为可见状态");
                }

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void initCountry(final float longitude, final float latitude) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(getAddress(latitude, longitude));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        locationManager.removeUpdates(locationListener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onLocationListener != null) {
                            onLocationListener.onFail();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        country = s;
                        if (onLocationListener != null) {
                            onLocationListener.onGetInfoSuccess(s);
                        }
                    }
                });
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    //放入经纬度就可以了
    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                Address address = addresses.get(0);

                String countryCode = address.getCountryCode();
                if (!TextUtils.isEmpty(countryCode)) {
                    return countryCode;
                }

                String country = address.getCountryName();
                if (isChinese(country)) {
                    country = URLEncoder.encode(country, "utf-8");
                }
                return country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 中文判断
     *
     * @param str
     * @return
     */
    public boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;
        return flg;
    }

    public interface OnLocationListener {
        void onLocationSuccess(float longitude, float latitude);

        void onGetInfoSuccess(String country);

        void onFail();
    }
}
