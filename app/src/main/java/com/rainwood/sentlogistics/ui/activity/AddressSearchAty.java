package com.rainwood.sentlogistics.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.AddressSearchBody;
import com.rainwood.sentlogistics.network.sqlite.SQLiteHelper;
import com.rainwood.sentlogistics.ui.adapter.SearchAddressListAdapter;
import com.rainwood.sentlogistics.ui.widget.LoadingView;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LocationManager;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.rainwood.tools.wheel.widget.SettingBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:40
 * @Desc: 地址搜索 page
 */
public final class AddressSearchAty extends BaseActivity implements Inputtips.InputtipsListener {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_content)
    private TextView mTextSearchContent;
    @ViewInject(R.id.ll_address)
    private LinearLayout currentAddressView;
    @ViewInject(R.id.tv_choose_city)
    private TextView currentCity;
    @ViewInject(R.id.lv_search_loading)
    private LoadingView mLoadingView;
    @ViewInject(R.id.iv_down_arrow)
    private ImageView mImageDownArrow;

    // content
    @ViewInject(R.id.mv_goods_map)
    private MapView mMapView;
    @ViewInject(R.id.fl_choose_address)
    private SettingBar chooseAddressView;
    @ViewInject(R.id.tv_address_tips)
    private TextView addressTips;
    @ViewInject(R.id.rv_address_list)
    private RecyclerView addressListView;

    // 当前位置 start
    private double latX = 29.5928798073848;
    private double lngY = 106.59493934546349;
    private String DEFAULT_CITY = "重庆";
    private AMap aMap;
    private LocationManager locationManager;
    // 当前位置 end
    private String currentAddress = "暂无定位";
    private List<Tip> mCurrentTipList;
    private List<AddressSearchBody> mSearchBodyList;
    private SearchAddressListAdapter mAddressListAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_address;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setMargin(this, pageTop);
        // 初始化高德地图
        initMap();
        // 地址列表
        mCurrentTipList = new ArrayList<>();
        mSearchBodyList = new ArrayList<>();
        // 设置布局管理器
        addressListView.setLayoutManager(new GridLayoutManager(this, 1));
        addressListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 适配器
        mAddressListAdapter = new SearchAddressListAdapter();
        addressListView.setAdapter(mAddressListAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initData() {
        // 只显示20条数据
        String historyAddressSql = "select name,address,latX,lngY,time from historyAddress order by time desc limit 20;";
        List<Map<String, String>> resultList = SQLiteHelper.with(this).query(historyAddressSql);
        //LogUtil.d("sxs", "-- result--" + resultList);
        for (Map<String, String> result : resultList) {
            AddressSearchBody body = new AddressSearchBody();
            body.setName(result.get("name"));
            body.setAddress(result.get("address"));
            body.setLatX(Double.parseDouble(Objects.requireNonNull(result.get("latX"))));
            body.setLngY(Double.parseDouble(Objects.requireNonNull(result.get("lngY"))));
            mSearchBodyList.add(body);
        }
        mAddressListAdapter.setSearchBodyList(mSearchBodyList);
    }

    @Override
    protected void initEvent() {
        // 搜索框焦点监听
        mTextSearchContent.setOnFocusChangeListener((v, hasFocus) ->
                currentAddressView.setVisibility(hasFocus ? View.GONE : View.VISIBLE));
        // 搜索框内容监听
        mTextSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressTips.setVisibility(TextUtils.isEmpty(s) ? View.VISIBLE : View.GONE);
                mLoadingView.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                currentCity.setText(TextUtils.isEmpty(s) ? currentAddress : currentAddress);
                // TODO: 搜索地址 -- 如果输入的数据为空，则清空list数据
                if (!TextUtils.isEmpty(s)) {
                    InputtipsQuery inputtipsQuery = new InputtipsQuery(s.toString().trim(), DEFAULT_CITY);
                    Inputtips inputTips = new Inputtips(AddressSearchAty.this, inputtipsQuery);
                    inputTips.setInputtipsListener(AddressSearchAty.this);
                    inputTips.requestInputtipsAsyn();
                } else {
                    mCurrentTipList.clear();
                }
            }
        });
        // 选址 -- 有经纬度的时候进行选址
        mAddressListAdapter.setClickItemAddress(new SearchAddressListAdapter.OnClickItemAddress() {
            @Override
            public void onClickItem(AddressSearchBody searchBody, int position) {
                /*if (TextUtils.isEmpty(mTextSearchContent.getText())) {
                    // 点击了历史记录
                    mTextSearchContent.setText(searchBody.getAddress());
                } else {

                }*/
                Constants.isChooseCurrentAddress = false;
                // 选中了之后去填写收货人信息
                Intent intent = new Intent(AddressSearchAty.this, AddressMapActivity.class);
                intent.putExtra("latX", searchBody.getLatX());
                intent.putExtra("lngY", searchBody.getLngY());
                intent.putExtra("address", searchBody.getAddress() + searchBody.getName());
                startActivityForResult(intent, Constants.CHOOSE_ADDRESS_IN_MAP_CODE);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.ll_address, R.id.iv_page_back, R.id.fl_choose_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.ll_address:
                toast("选择地址");
                break;
            case R.id.fl_choose_address:
                // 在地图上选址
                Constants.isChooseCurrentAddress = true;
                openActivity(AddressMapActivity.class);
                break;
        }
    }

    /**
     * 初始化地图属性
     */
    private void initMap() {
        // 高德地图重新初始化onCreate生命周期
        mMapView.onCreate(getSavedInstanceState());
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        setLocationCallBack();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                locationManager.startLocation(AddressSearchAty.this);
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
    }

    /**
     * 根据经纬度调用地图定位
     */
    private void setLocationCallBack() {
        locationManager = new LocationManager();
        //根据获取的经纬度，将地图移动到定位位置
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latX, lngY)));
        // 定位当前位置
        locationManager.setLocationCallBack((str, lat, lgt, aMapLocation) -> {
            latX = lat;
            lngY = lgt;
            currentAddress = aMapLocation.getCity();
            setLocationCallBack();
        });
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onGetInputtips(List<Tip> list, int code) {
        // 高德地图错误码对照表 https://lbs.amap.com/api/android-sdk/guide/map-tools/error-code
        if (code == 1000) {
            mSearchBodyList.clear();
            mCurrentTipList.addAll(list);
            // 算距离、赋值
            for (Tip tip : list) {
                LatLonPoint targetPoint = tip.getPoint();
                if (targetPoint != null) {
                    AddressSearchBody addressSearchBody = new AddressSearchBody();
                    addressSearchBody.setName(tip.getName());
                    addressSearchBody.setAddress(tip.getDistrict() + tip.getAddress());
                    addressSearchBody.setLatX(targetPoint.getLatitude());
                    addressSearchBody.setLngY(targetPoint.getLongitude());
                    DPoint minePoint = new DPoint(latX, lngY);
                    DPoint dPoint = new DPoint(targetPoint.getLatitude(), targetPoint.getLongitude());
                    float distance = CoordinateConverter.calculateLineDistance(minePoint, dPoint);
                    addressSearchBody.setDistance(String.format("%.2f", distance / 1000));
                    mSearchBodyList.add(addressSearchBody);
                }
            }
            // 距离比较器
            Collections.sort(mSearchBodyList, (o1, o2) ->
                    (int) (Double.parseDouble(o1.getDistance()) - Double.parseDouble(o2.getDistance())));
            mAddressListAdapter.setSearchBodyList(mSearchBodyList);
            mLoadingView.setVisibility(View.GONE);
        } else {
            LogUtil.d("sxs", "错误码: " + code);
        }
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
