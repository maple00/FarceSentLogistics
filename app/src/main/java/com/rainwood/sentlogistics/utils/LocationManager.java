package com.rainwood.sentlogistics.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.rainwood.tools.toast.ToastUtils;

/**
 * @Author: a797s
 * @Date: 2020/6/22 17:53
 * @Desc: 高德定位
 */
public class LocationManager implements AMapLocationListener {

    private ILocationCallBack callBack;
    private static final double EARTH_RADIUS = 6378137.0;

    public LocationManager() {

    }

    public void startLocation(Context context) {
        // 初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        AMapLocationClient mLocationClient = new AMapLocationClient(context);
        // 设置定位监听
        mLocationClient.setLocationListener(this);
        //设置定位模式（高精度模式），Battery_Saving 为低功耗模式，Device_Sensors为仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置单词定位
        mLocationOption.setOnceLocation(true);
        // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //是否返回地址信息
        mLocationOption.setNeedAddress(true);
        // 设置是否强制刷新WiFi
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置是否缓存
        mLocationOption.setLocationCacheEnable(false);
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient.startLocation();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }

        if (aMapLocation.getErrorCode() == 0) {
            double lat = aMapLocation.getLatitude();//获取纬度
            double lgt = aMapLocation.getLongitude();//获取经度
            String country = aMapLocation.getCountry();//国家信息
            String province = aMapLocation.getProvince();//省信息
            String city = aMapLocation.getCity();//城市信息
            String district = aMapLocation.getDistrict();//城区信息
            String street = aMapLocation.getStreet();//街道信息

            if (callBack != null) {
                callBack.callBack(country + province + city + district + street, lat, lgt, aMapLocation);
            }
        }
    }

    /**
     * 自定义图标
     *
     * @return
     */
    public MarkerOptions getMarkerOption(String str, double lat, double lgt) {
        MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.coordinate));
        markerOptions.position(new LatLng(lat, lgt));
        markerOptions.title(str);
       /* markerOptions.title(str);
        markerOptions.snippet("纬度:" + lat + "   经度:" + lgt);
        markerOptions.period(100);*/
        return markerOptions;
    }

    /**
     * 获取指定地址的经纬度
     *
     * @param context
     * @param addressName
     */
    public void getLatLng(Context context, String addressName) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                            geocodeResult.getGeocodeAddressList().size() > 0) {

                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        // 纬度
                        double latitude = geocodeAddress.getLatLonPoint().getLatitude();
                        // 经度
                        double longititude = geocodeAddress.getLatLonPoint().getLongitude();
                        // 区域编码
                        String adcode = geocodeAddress.getAdcode();
                        LogUtil.d("sxs --lgq地理编码", geocodeAddress.getAdcode() + "");
                        LogUtil.d("sxs --- lgq纬度latitude", latitude + "");
                        LogUtil.d("sxs  --- lgq经度longititude", longititude + "");
                    } else {
                        ToastUtils.show("地址名出错");
                    }
                }
            }
        });
        GeocodeQuery geocodeQuery = new GeocodeQuery(addressName.trim(), "29");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }


    public interface ILocationCallBack {
        void callBack(String str, double lat, double lgt, AMapLocation aMapLocation);
    }

    public void setLocationCallBack(ILocationCallBack callBack) {
        this.callBack = callBack;
    }
}
