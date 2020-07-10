package com.rainwood.sentlogistics.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.ui.dialog.MessageDialog;
import com.rainwood.sentlogistics.utils.LocationManager;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.ArrayList;
import java.util.List;

import cn.rawinwood.bgabanner.BGABanner;
import cn.rawinwood.bgabanner.transformer.TransitionEffect;

public class HomeActivity extends BaseActivity implements BGABanner.Adapter<ImageView, String>,
        AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {

    @ViewInject(R.id.rl_home_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.dl_drawer_layout)
    private DrawerLayout mDrawerLayout;

    @ViewInject(R.id.iv_head)
    private ImageView mImageHead;
    @ViewInject(R.id.tv_city)
    private TextView mTextCity;
    @ViewInject(R.id.banner_home_top)
    private BGABanner mStackBanner;
    @ViewInject(R.id.mv_goods_map)
    private MapView mMapView;

    private AMap aMap;
    private LocationManager locationManager;
    private GeocodeSearch geocoderSearch;
    // 当前位置
    private double latX = 29.5928798073848;
    private double lngY = 106.59493934546349;
    private String location = "重庆市";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        // 加载Banner
        mStackBanner.setTransitionEffect(TransitionEffect.Accordion);
        loadBannerData(mStackBanner, 3);
        // 高德地图 -- 定位权限检查
        setCheckedXXPermission();
        setLocationMap();
    }

    /**
     * 检查权限
     */
    private void setCheckedXXPermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 高德地图重新初始化onCreate生命周期
                            mMapView.onCreate(getSavedInstanceState());
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        new MessageDialog.Builder(HomeActivity.this)
                                .setTitle("无法获取您的位置信息")
                                .setMessage("请在“设置-隐私-定位服务”中允许睿\n" + "睿派使用定位服务")
                                .setConfirm(R.string.text_2_setting)
                                .setCancel(null)
                                .setNowShowConfirm(false)
                                .setAutoDismiss(true)
                                .setListener(new MessageDialog.OnListener() {
                                    @Override
                                    public void onConfirm(BaseDialog dialog) {
                                        XXPermissions.gotoPermissionSettings(HomeActivity.this, true);
                                    }

                                    @Override
                                    public void onCancel(BaseDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {

    }

    @SingleClick
    @OnClick({R.id.iv_head, R.id.tv_token_order, R.id.iv_home_right_top, R.id.ll_choose_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                post(() -> mDrawerLayout.openDrawer(GravityCompat.START));
                break;
            case R.id.tv_token_order:
                toast("我要下单");
                break;
            case R.id.ll_choose_city:
                toast("选择城市");
                startActivity(getNewIntent(this, CityActivity.class, "选择城市", "chooseCity"));
                break;
            case R.id.iv_home_right_top:
                // 活动列表
                startActivity(getNewIntent(this, CommonListActivity.class, "活动列表", "eventList"));
                break;
        }
    }

    /**
     * 加载轮播图
     *
     * @param banner
     * @param count
     */
    private void loadBannerData(final BGABanner banner, final int count) {
        // 模拟图片
        List<String> modelImages = new ArrayList<>();
        List<String> imageTips = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            modelImages.add("加载的banner");
            imageTips.add("这是第" + (i + 1) + "张图片");
        }
        /**
         * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
         * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
         */
        banner.setAutoPlayAble(modelImages.size() > 1);
        banner.setAdapter(this);
        banner.setData(modelImages, imageTips);
        banner.setIsNeedShowIndicatorOnOnlyOnePage(false);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                LogUtil.d("sxs", "点击了某个bannerItem ---- " + position);
            }
        });
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        //LogUtil.d("sxs", "model---> " + model + " -- position ---> " + position);
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.img_bannger)
                        .error(R.drawable.img_bannger)
                        .dontAnimate().centerCrop())
                .into(itemView);
    }

    /**
     * 设置地图定位属性
     */
    private void setLocationMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        setLocationCallBack();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        geocoderSearch = new GeocodeSearch(this);
        //设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                locationManager.startLocation(HomeActivity.this);
            }

            @Override
            public void deactivate() {

            }
        });
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
        // 设置地图上的点可点击
        aMap.setOnMapClickListener(this);
        // 地址编码逆向
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 根据经纬度调用地图定位
     */
    private void setLocationCallBack() {
        locationManager = new LocationManager();
        //根据获取的经纬度，将地图移动到定位位置
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latX, lngY)));
        //添加定位图标，并弹出标注弹框
        aMap.addMarker(locationManager.getMarkerOption(location, latX, lngY));
        locationManager.setLocationCallBack((str, lat, lgt, aMapLocation) -> {
            latX = lat;
            lngY = lgt;
            setLocationCallBack();
            LogUtil.d("sxs", "首页-当前地址-- " + str);
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        getAddressByLatLng(latLng);
    }

    /**
     * @param latLng
     */
    private void getAddressByLatLng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 逆地理编码回调
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                location = result.getRegeocodeAddress().getFormatAddress(); // 逆转地里编码不是每次都可以得到对应地图上的opi
                LogUtil.d("sxs", "逆地理编码回调  得到的地址：" + location);
                toast("被点击了-----" + location);
            }
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

}
