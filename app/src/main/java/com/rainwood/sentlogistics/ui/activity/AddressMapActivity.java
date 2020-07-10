package com.rainwood.sentlogistics.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.network.sqlite.SQLiteHelper;
import com.rainwood.sentlogistics.ui.widget.LoadingView;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.LocationManager;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/8 13:11
 * @Desc: 在地图上选址、填写收货人信息
 */
public final class AddressMapActivity extends BaseActivity implements
        AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_content)
    private EditText mTextSearchContent;
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
    // input info
    @ViewInject(R.id.tv_take_address)
    private TextView takeAddressView;
    @ViewInject(R.id.et_floor_door)
    private EditText floorDoorView;
    @ViewInject(R.id.et_contact_name)
    private EditText contactNameView;
    @ViewInject(R.id.et_tel_number)
    private EditText telNumberView;
    @ViewInject(R.id.tv_contact_name)
    private TextView contactName;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumber;
    // choose point
    @ViewInject(R.id.tv_district)
    private TextView mTextDistrict;
    @ViewInject(R.id.tv_point_address)
    private TextView mTextAddress;
    // page
    @ViewInject(R.id.ll_page_input_consignee_info)
    private LinearLayout inputConsigneePage;
    @ViewInject(R.id.rl_page_choose_point)
    private RelativeLayout chooseInputPage;

    // 当前位置 start
    private double latX = 29.5928798073848;
    private double lngY = 106.59493934546349;
    private String DEFAULT_CITY = "重庆";
    //定位位置显示（标记）
    private Marker mGPSMarker;
    private String DEFAULT_ADDRESS = "";
    private AMap aMap;
    private LocationManager locationManager;
    private GeocodeSearch geocoderSearch;
    private MarkerOptions markOptions;
    private LatLng latLng;
    // 当前位置 end
    private int MAX_LINES = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_map_address;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setMargin(this, pageTop);
        // 初始化高德地图
        initMap();
        // 搜索框initial
        mTextSearchContent.setFocusableInTouchMode(false);
        // 设置必填项
        setRequiredValue(contactName, getString(R.string.text_contact));
        setRequiredValue(telNumber, getString(R.string.text_tel_number));
        // 设置动画

    }

    @Override
    protected void initData() {
        double latXDou = getIntent().getDoubleExtra("latX", latX);
        double lngYDou = getIntent().getDoubleExtra("lngY", lngY);
        String address = getIntent().getStringExtra("address");
        inputConsigneePage.setVisibility(Constants.isChoosePoint ? View.GONE : View.VISIBLE);
        chooseInputPage.setVisibility(Constants.isChoosePoint ? View.VISIBLE : View.GONE);
        if (Constants.isChoosePoint) {
            // 选择发货地址
            setShowAddress(latXDou, lngYDou, address);
        } else {
            // 选择收货地址
            //设置软键盘不被遮挡
            Window win = getWindow();
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            setShowAddress(latXDou, lngYDou, address);
        }
    }

    /**
     * 设置地址显示
     *
     * @param latXDou
     * @param lngYDou
     * @param address
     */
    private void setShowAddress(double latXDou, double lngYDou, String address) {
        mTextSearchContent.setText(address);
        takeAddressView.setText(address);
        mTextAddress.setText(address);
        latX = latXDou;
        lngY = lngYDou;
        if (address != null) {
            locationManager.getLatLng(this, address);
        }
    }

    @Override
    protected void initPresenter() {

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
                mLoadingView.setVisibility(View.GONE);
                // mLoadingView.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                currentCity.setText(TextUtils.isEmpty(s) ? DEFAULT_CITY : DEFAULT_CITY);
                // 限制最大行数
                int lines = mTextSearchContent.getLineCount();
                // 限制最大输入行数
                if (lines > MAX_LINES) {
                    String str = s.toString();
                    int cursorStart = mTextSearchContent.getSelectionStart();
                    int cursorEnd = mTextSearchContent.getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                        str = str.substring(0, cursorStart - 1) + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length() - 1);
                    }
                    // setText会触发afterTextChanged的递归
                    mTextSearchContent.setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    mTextSearchContent.setSelection(mTextSearchContent.getText().length());
                }
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search_content, R.id.btn_confirm, R.id.tv_confirm_point})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search_content:
                openActivity(AddressSearchAty.class);
                break;
            case R.id.btn_confirm:
                // 收货人信息填写
                if (TextUtils.isEmpty(contactNameView.getText())) {
                    toast(contactNameView.getHint());
                    return;
                }
                if (TextUtils.isEmpty(telNumberView.getText())) {
                    toast(telNumberView.getHint());
                    return;
                }
                String floorDoorNum = floorDoorView.getText().toString().trim();
                String contactName = contactNameView.getText().toString().trim();
                String telNumber = telNumberView.getText().toString().trim();
                // TODO：先访问接口，是否开通服务

                String takeAddressStr = takeAddressView.getText().toString().trim();
                String takeAddressDistrict = takeAddressView.getHint().toString().trim();
                String takeNowTime = DateTimeUtils.getNowTime();
                // sql不存在则插入
                String takePointSql = "INSERT INTO historyAddress(name, address, latX, lngY, time)" +
                        "SELECT '" + takeAddressDistrict + "', '" + takeAddressStr + "', '" + latX + "'"
                        + ",'" + lngY + "','" + takeNowTime + "'" + "FROM DUAL"
                        + "WHERE NOT EXISTS(SELECT address FROM historyAddress address field = '" + takeAddressStr + "')";
                LogUtil.d("sxs", "--- SQL -- " + takePointSql);
                SQLiteHelper.with(this).insert(takePointSql);
                LogUtil.d("sxs", "-----" + takeAddressDistrict);
                LogUtil.d("sxs", "----" + takeAddressStr);
                Intent takeGoodsIntent = new Intent(this, TokenOrderActivity.class);
                DeliveryAddressBody takeSearchBody = new DeliveryAddressBody();
                takeSearchBody.setLatX(latX);
                takeSearchBody.setLngY(lngY);
                takeSearchBody.setStreet(takeAddressDistrict);
                takeSearchBody.setAddress(takeAddressStr);
                takeSearchBody.setConsignee(contactName);
                takeSearchBody.setFloorDoorNum(floorDoorNum);
                takeSearchBody.setTelNum(telNumber);
                takeGoodsIntent.putExtra("currentAddress", takeSearchBody);
                setIntent(takeGoodsIntent);
                startActivity(takeGoodsIntent);
                finish();
                break;
            case R.id.tv_confirm_point:
                // 确认选点
                String district = mTextDistrict.getText().toString().trim();
                String deliveryAddress = mTextAddress.getText().toString().trim();
                String deliveryNowTime = DateTimeUtils.getNowTime();
                // TODO: 访问接口，是否开通服务
                // sql不存在则插入
                String choosePointSql = "INSERT INTO historyAddress(name, address, latX, lngY, time)" +
                        "SELECT '" + district + "', '" + deliveryAddress + "', '" + latX + "'"
                        + ",'" + lngY + "','" + deliveryNowTime + "'" + "FROM DUAL"
                        + "WHERE NOT EXISTS(SELECT address FROM historyAddress address field = '" + deliveryAddress + "')";
                SQLiteHelper.with(this).insert(choosePointSql);
                // 发货地址
                Intent deliveryIntent = new Intent(this, TokenOrderActivity.class);
                DeliveryAddressBody searchBody = new DeliveryAddressBody();
                searchBody.setLatX(latX);
                searchBody.setLngY(lngY);
                searchBody.setStreet(district);
                searchBody.setAddress(deliveryAddress);
                deliveryIntent.putExtra("currentAddress", searchBody);
                setIntent(deliveryIntent);
                startActivity(deliveryIntent);
                finish();
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
        geocoderSearch = new GeocodeSearch(this);
        //设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                locationManager.startLocation(AddressMapActivity.this);
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
        //设置地图拖动监听
        aMap.setOnCameraChangeListener(this);
    }

    /**
     * 根据经纬度调用地图定位
     */
    @SuppressLint("SetTextI18n")
    private void setLocationCallBack() {
        locationManager = new LocationManager();
        //根据获取的经纬度，将地图移动到定位位置
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latX, lngY)));
        // 定位当前位置
        locationManager.setLocationCallBack((str, lat, lgt, aMapLocation) -> {
            if (Constants.isChooseCurrentAddress) {
                latX = lat;
                lngY = lgt;
                mTextAddress.setText(aMapLocation.getAddress());
            }
            DEFAULT_CITY = aMapLocation.getCity();
            setLocationCallBack();
            //
            LatLng la = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            setMarket(la, aMapLocation.getCity(), aMapLocation.getAddress());
        });
    }

    /**
     * 地图建筑物点击事件
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        // getAddressByLatLng(latLng);
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


    @SuppressLint("SetTextI18n")
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                // 逆转地里编码不是每次都可以得到对应地图上的poi
                DEFAULT_ADDRESS = result.getRegeocodeAddress().getFormatAddress();
                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                String city = result.getRegeocodeAddress().getCity();
                // 设置显示的值
                LatLonPoint point = result.getRegeocodeQuery().getPoint();
                latX = point.getLatitude();
                lngY = point.getLongitude();
                if (Constants.isChoosePoint) {
                    mTextDistrict.setText(pois.get(0) + "("
                            + result.getRegeocodeAddress().getDistrict() + ")");
                    mTextAddress.setText(result.getRegeocodeAddress().getFormatAddress());
                } else {
                    takeAddressView.setText(result.getRegeocodeAddress().getFormatAddress());
                    takeAddressView.setHint(pois.get(0).toString());
                }
                setMarket(latLng, city, DEFAULT_ADDRESS);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    //通过拖动地图进行位置定位改变，且mark位于屏幕中间不变
    private void setMarket(LatLng latLng, String title, String content) {
        if (mGPSMarker != null) {
            mGPSMarker.remove();
        }
        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        int width = (wm.getDefaultDisplay().getWidth()) / 2;
        int height = ((wm.getDefaultDisplay().getHeight()) / 2) - 80;
        markOptions = new MarkerOptions();
        markOptions.draggable(true);//设置Marker可拖动
        markOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.poi_marker))).anchor(0.5f, 0.7f);
        //设置一个角标
        mGPSMarker = aMap.addMarker(markOptions);
        //设置marker在屏幕的像素坐标
        mGPSMarker.setPosition(latLng);
        mGPSMarker.setTitle("当前定位");
        mGPSMarker.setSnippet(content);
        //设置像素坐标
        mGPSMarker.setPositionByPixels(width, height);
        //关闭infowindow显示
//        if (!TextUtils.isEmpty(content)) {
//            mGPSMarker.showInfoWindow();
//        }
        mMapView.invalidate();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // 移动过程中,GONE
        if (Constants.isChoosePoint) {
            chooseInputPage.setVisibility(View.GONE);
            setUpDownAnimation(chooseInputPage);
        } else {
            inputConsigneePage.setVisibility(View.GONE);
            setUpDownAnimation(inputConsigneePage);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        // 移动完成 visible
        if (Constants.isChoosePoint) {
            chooseInputPage.setVisibility(View.VISIBLE);
            setUpDownAnimation(chooseInputPage);
        } else {
            inputConsigneePage.setVisibility(View.VISIBLE);
            setUpDownAnimation(inputConsigneePage);
        }
        latLng = cameraPosition.target;
        getAddressByLatLng(latLng);
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
