package com.rainwood.sentlogistics.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.rainwood.citynavigation.CityBean;
import com.rainwood.citynavigation.DividerItemDecoration;
import com.rainwood.citynavigation.IndexBar.widget.IndexBar;
import com.rainwood.citynavigation.TitleItemDecoration;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.HotCity;
import com.rainwood.sentlogistics.persenter.IMapPresenter;
import com.rainwood.sentlogistics.ui.adapter.CityAdapter;
import com.rainwood.sentlogistics.ui.adapter.CityHotAdapter;
import com.rainwood.sentlogistics.utils.LocationManager;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.sentlogistics.view.IMapCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 13:52
 * @Desc: 城市列表
 */
public final class CityActivity extends BaseActivity implements IMapCallback {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;

    // content
    @ViewInject(R.id.rv_hot_city)
    private RecyclerView hotCityView;
    @ViewInject(R.id.rv_city_list)
    private RecyclerView cityListView;
    @ViewInject(R.id.indexBar)
    private IndexBar mIndexBar;
    @ViewInject(R.id.tv_side_bar_hint)
    private TextView sideBarHint;
    @ViewInject(R.id.mv_goods_map)
    private MapView mMapView;
    @ViewInject(R.id.tv_name)
    private TextView currentAddress;

    private IMapPresenter mMapPresenter;
    private CityAdapter mCityAdapter;
    private CityHotAdapter mCityHotAdapter;
    private AMap aMap;
    private LocationManager locationManager;
    // 当前位置
    private double latX = 29.5928798073848;
    private double lngY = 106.59493934546349;
    private String location = "重庆市";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_city_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setMargin(this, pageTop);
        // 设置布局管理器
        hotCityView.setLayoutManager(new GridLayoutManager(this, 3));
        hotCityView.addItemDecoration(new SpacesItemDecoration(10));
        // 创建适配器
        mCityAdapter = new CityAdapter();
        mCityHotAdapter = new CityHotAdapter();
        // 设置适配器
        cityListView.setAdapter(mCityAdapter);
        hotCityView.setAdapter(mCityHotAdapter);
        // 检查定位权限
        setCheckedXXPermission();
        setLocationMap();
    }

    @Override
    protected void initPresenter() {
        mMapPresenter = PresenterManager.getOurInstance().getMapPresenter();
        mMapPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mMapPresenter.requestCityData();
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    private void setCheckedXXPermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        // 高德地图重新初始化onCreate生命周期
                        mMapView.onCreate(getSavedInstanceState());
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        currentAddress.setText("当前没有定位权限，请手动开启");
                    }
                });
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
        //设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                locationManager.startLocation(CityActivity.this);
            }

            @Override
            public void deactivate() {

            }
        });
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
    }

    private void setLocationCallBack() {
        locationManager = new LocationManager();
        locationManager.setLocationCallBack((str, lat, lgt, aMapLocation) -> {
            latX = lat;
            lngY = lgt;
            setLocationCallBack();
            currentAddress.setText(aMapLocation.getCity());
        });
    }

    @Override
    public void getCityData(List<CityBean> cityList, List<HotCity> hotCityList) {

        LinearLayoutManager manager;
        cityListView.setLayoutManager(manager = new LinearLayoutManager(this));
        cityListView.addItemDecoration(new TitleItemDecoration(this, cityList));
        cityListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        mIndexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(manager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(cityList);//设置数据源
        mCityAdapter.setDatas(cityList);
        mCityHotAdapter.setList(hotCityList);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
