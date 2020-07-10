package com.rainwood.sentlogistics.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.helper.PresenterManager;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.model.domain.PublishType;
import com.rainwood.sentlogistics.network.action.StatusAction;
import com.rainwood.sentlogistics.persenter.ITokenOrderPresenter;
import com.rainwood.sentlogistics.ui.adapter.PublishTypeAdapter;
import com.rainwood.sentlogistics.ui.adapter.ShippingAddressAdapter;
import com.rainwood.sentlogistics.ui.dialog.CustomTimeDialog;
import com.rainwood.sentlogistics.ui.pop.CustomPopupWindow;
import com.rainwood.sentlogistics.ui.widget.LoadingView;
import com.rainwood.sentlogistics.ui.widget.MeasureGridView;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.sentlogistics.utils.LocationManager;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.utils.RandomUtil;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.sentlogistics.view.ITokenOrderCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 15:37
 * @Desc: 下单页面（App主要页面）
 */
public final class TokenOrderActivity extends BaseActivity implements ITokenOrderCallback, StatusAction {

    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
    // 高德地图
    @ViewInject(R.id.mv_goods_map)
    private MapView mMapView;
    @ViewInject(R.id.ll_parent_page)
    private LinearLayout parentPage;
    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content -- 地址相关
    @ViewInject(R.id.tv_current_street)
    private TextView currentStreet;
    @ViewInject(R.id.tv_current_address)
    private TextView currentAddress;
    @ViewInject(R.id.mgv_shipping_address)
    private MeasureGridView shippingAddressView;
    // -- 发货人信息相关
    @ViewInject(R.id.tv_contact)
    private TextView mTextContact;
    @ViewInject(R.id.tv_tel_number)
    private TextView mTextTelNum;
    @ViewInject(R.id.et_delivery_name)
    private EditText deliveryName;
    @ViewInject(R.id.et_delivery_tel)
    private EditText deliveryTelNum;
    // 货物相关
    @ViewInject(R.id.tv_take_goods_time)
    private TextView goodsTime;
    @ViewInject(R.id.tv_publish_type)
    private TextView goodsPublishType;
    @ViewInject(R.id.cb_current_day)
    private CheckBox mCheckCurrentDay;
    @ViewInject(R.id.cb_next_day)
    private CheckBox mCheckNextDay;
    @ViewInject(R.id.tv_goods_info_title)
    private TextView goodsInfoTitle;
    // 备注
    @ViewInject(R.id.et_order_note)
    private EditText orderNote;

    // 当前位置 start
    private double latX = 29.5928798073848;
    private double lngY = 106.59493934546349;
    private String location = "重庆市";
    private AMap aMap;
    private LocationManager locationManager;
    // 当前位置 end

    private ITokenOrderPresenter mTokenOrderPresenter;
    private ShippingAddressAdapter mAddressAdapter;
    private List<DeliveryAddressBody> mDeliveryAddressBodyList;
    private int currentPos = 0;
    // 送达方式-- 默认为当日达
    private boolean isCurrentDelivery = true;
    private PublishTypeAdapter mPublishTypeAdapter;
    private List<PublishType> mTypeList;
    private CustomPopupWindow mPublishTypePopWindow;
    private RecyclerView mServiceTypeView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_token_order;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 初始化高德地图
        initMap();
        //创建适配器（收货地址）
        mAddressAdapter = new ShippingAddressAdapter();
        shippingAddressView.setAdapter(mAddressAdapter);
        mDeliveryAddressBodyList = new ArrayList<>();
        // 创建适配器（发布类型）
        mPublishTypeAdapter = new PublishTypeAdapter();
        // 设置必填内容
        setRequiredValue(mTextContact, getString(R.string.text_contact));
        setRequiredValue(mTextTelNum, getString(R.string.text_tel_number));
        setRequiredValue(goodsInfoTitle, getString(R.string.text_goods_info));
        showLoading();
    }

    @Override
    protected void initPresenter() {
        mTokenOrderPresenter = PresenterManager.getOurInstance().getTokenOrderPresenter();
        mTokenOrderPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求发布类型
        mTokenOrderPresenter.requestOrderPublishType();
    }

    @Override
    protected void initData() {
        mTypeList = new ArrayList<>();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从地图选址页面接收收货地址列表的数据
        //LogUtil.d("sxs", "--- currentPos--- " + currentPos);
        DeliveryAddressBody searchBody = (DeliveryAddressBody) intent.getSerializableExtra("currentAddress");
        if (searchBody != null) {
            // 接收了一个选址对象
            if (Constants.isChoosePoint) {
                // 发货地址
                currentStreet.setText(searchBody.getStreet());
                currentAddress.setText(searchBody.getAddress());
            } else {
                // 先移除在添加（等效覆盖）
                mDeliveryAddressBodyList.remove(currentPos);
                mDeliveryAddressBodyList.add(currentPos, searchBody);
                mAddressAdapter.setDataList(mDeliveryAddressBodyList);
            }
        }
    }

    @Override
    protected void initEvent() {
        mAddressAdapter.setShippingListener(new ShippingAddressAdapter.OnClickShippingListener() {
            @Override
            public void onClickDeleteItem(DeliveryAddressBody addressData, int position) {
                mDeliveryAddressBodyList.remove(position);
                mAddressAdapter.setDataList(mDeliveryAddressBodyList);
            }

            @Override
            public void onClickPlusAddress(DeliveryAddressBody addressData, int position) {
                currentPos = position;
                Constants.isChoosePoint = false;
                openActivity(AddressSearchAty.class);
            }
        });
        // 发布类型
        mPublishTypeAdapter.setOnClickItemPublish(new PublishTypeAdapter.OnClickItemPublish() {
            @Override
            public void onClickItem(PublishType publishType, int position) {
                for (PublishType type : mTypeList) {
                    type.setSelected(false);
                }
                publishType.setSelected(true);
                goodsPublishType.setText(publishType.getType());
                mPublishTypeAdapter.setTypeList(mTypeList);
                mPublishTypePopWindow.dismiss();
            }
        });
    }

    @SingleClick
    @OnClick({R.id.rl_mine_location, R.id.iv_page_back, R.id.git_plus_shipping_address, R.id.git_common_route,
            R.id.cb_next_day, R.id.tv_next_day, R.id.tv_current_day, R.id.cb_current_day, R.id.tv_take_goods_time,
            R.id.iv_arrow, R.id.tv_publish_type, R.id.iv_arrow1, R.id.tv_goods_list, R.id.iv_arrow2,
            R.id.btn_commit_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.rl_mine_location:
                Constants.isChoosePoint = true;
                openActivity(AddressSearchAty.class);
                break;
            case R.id.git_plus_shipping_address:
                if (ListUtil.getSize(mDeliveryAddressBodyList) != 0) {
                    String currentAddress = mDeliveryAddressBodyList.get(ListUtil.getSize(mDeliveryAddressBodyList) - 1).getAddress();
                    String currentStreet = mDeliveryAddressBodyList.get(ListUtil.getSize(mDeliveryAddressBodyList) - 1).getStreet();
                    if (TextUtils.isEmpty(currentStreet) && TextUtils.isEmpty(currentAddress)) {
                        toast("请先添加收货地址");
                        return;
                    }
                }
                DeliveryAddressBody addressData = new DeliveryAddressBody();
                addressData.setId(RandomUtil.getItemID(20));
                mDeliveryAddressBodyList.add(addressData);
                mAddressAdapter.setDataList(mDeliveryAddressBodyList);
                break;
            case R.id.git_common_route:
                toast("选择常用路线");
                break;
            case R.id.cb_next_day:
            case R.id.tv_next_day:
                isCurrentDelivery = false;
                mCheckCurrentDay.setChecked(false);
                mCheckNextDay.setChecked(true);
                break;
            case R.id.cb_current_day:
            case R.id.tv_current_day:
                isCurrentDelivery = true;
                mCheckCurrentDay.setChecked(true);
                mCheckNextDay.setChecked(false);
                break;
            case R.id.tv_take_goods_time:
            case R.id.iv_arrow:
                new CustomTimeDialog.Builder(this)
                        .setTitle(getString(R.string.time_title))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setIgnoreSecond()
                        .setNowShowConfirm(true)
                        .setShowImageClose(false)
                        .setCancel(getString(R.string.common_clear))
                        .setListener(new CustomTimeDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, String monthDay, int hour, int minute, int second) {
                                if ("今天".equals(monthDay)) {
                                    monthDay = DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_MONTH_DAY);
                                } else if ("明天".equals(monthDay)) {
                                    monthDay = DateTimeUtils.getLongTime(24, DateTimeUtils.DatePattern.ONLY_MONTH_DAY);
                                }
                                goodsTime.setText(monthDay + " " + (hour < 10 ? "0" : hour) + ":"
                                        + (minute < 10 ? "0" + minute : minute));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                goodsTime.setText("");
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_publish_type:
            case R.id.iv_arrow1:
                mPublishTypePopWindow = new CustomPopupWindow.Builder(this)
                        .setView(R.layout.dialog_service_type)
                        .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setBackGroundLevel(0.7f)
                        .setOutsideTouchable(true)
                        .setAnimationStyle(R.style.BottomAnimStyle)
                        .setListener((view1, layoutResId) -> {
                            mServiceTypeView = view1.findViewById(R.id.tv_service_list);
                            // 设置布局管理器
                            mServiceTypeView.setLayoutManager(new GridLayoutManager(TokenOrderActivity.this, 1));
                            mServiceTypeView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                            // 设置适配器
                            mServiceTypeView.setAdapter(mPublishTypeAdapter);
                            final int fixItemNum = 5;
                            //将修改高度的代码放到RecyclerView最后面执行，让RecyclerView先测量完毕
                            mServiceTypeView.post(() -> {
                                if (mServiceTypeView.getAdapter() == null || mServiceTypeView.getAdapter().getItemCount() <= fixItemNum) { //小于4个不固定高度
                                    return;
                                }
                                View view2 = mServiceTypeView.getChildAt(0);
                                if (view2 == null) {
                                    return;
                                }
                                int height = view2.getHeight() * fixItemNum;
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mServiceTypeView.getLayoutParams();
                                layoutParams.height = height;
                                mServiceTypeView.setLayoutParams(layoutParams);
                            });
                        }).create();
                mPublishTypePopWindow.showAtLocation(parentPage, Gravity.BOTTOM, 0, 0);
                mPublishTypePopWindow.setOnDismissListener(() -> mPublishTypePopWindow.dismiss());
                mPublishTypeAdapter.setTypeList(mTypeList);
                for (int i = 0; i < ListUtil.getSize(mTypeList); i++) {
                    if (mTypeList.get(i).isSelected()) {
                        int finalI = i;
                        post(() -> mServiceTypeView.scrollToPosition(finalI));
                        break;
                    }
                }
                break;
            case R.id.tv_goods_list:
            case R.id.iv_arrow2:
                if (ListUtil.getSize(mDeliveryAddressBodyList) == 0) {
                    toast("请先添加收货地址");
                    return;
                }
                // 跳转填写货物信息，需要带上本页页面的地址数据
                Intent intent = new Intent(this, GoodsDataActivity.class);
                intent.putExtra("address", (Serializable) mDeliveryAddressBodyList);
                intent.putExtra("title", "货物信息");
                startActivity(intent);
                //startActivity(getNewIntent(this, GoodsDataActivity.class, "货物信息", ""));
                break;
            case R.id.btn_commit_order:
                // 提交订单
                startActivity(getNewIntent(this, ConfirmOrderAty.class, "确认订单", ""));
                break;
        }
    }

    @Override
    public void getPublishTypeData(List<PublishType> typeList) {
        showComplete();
        mTypeList.addAll(typeList);
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
                locationManager.startLocation(TokenOrderActivity.this);
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
        //添加定位图标，并弹出标注弹框
        aMap.addMarker(locationManager.getMarkerOption(location, latX, lngY));
        locationManager.setLocationCallBack((str, lat, lgt, aMapLocation) -> {
            latX = lat;
            lngY = lgt;
            setLocationCallBack();
            currentStreet.setText(aMapLocation.getStreet());
            currentAddress.setText(aMapLocation.getAddress());
            LogUtil.d("sxs", "下单-当前地址-- " + aMapLocation.getStreet());
            LogUtil.d("sxs", "下单-当前地址-- " + aMapLocation.getAddress());
        });
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

    @Override
    public void onError(String tips) {
        toast(tips);
        showError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onLoading() {
        showLoading();
    }

    @Override
    public void onEmpty() {
        showEmpty();
    }

    @Override
    protected void release() {
        if (mTokenOrderPresenter != null) {
            mTokenOrderPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
