package com.rainwood.sentlogistics.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.ui.adapter.DeliveryAddressAdapter;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

import static com.rainwood.sentlogistics.utils.Constants.CHOOSE_ADDRESS_IN_GOODS;

/**
 * @Author: a797s
 * @Date: 2020/7/10 13:15
 * @Desc: 收货地址
 */
public final class DeliveryAddressAty extends BaseActivity {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.rv_address_list)
    private RecyclerView addressListView;

    private DeliveryAddressAdapter mAddressAdapter;
    private List<DeliveryAddressBody> mDeliveryAddressList;
    private static int tempPos = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_delivery_address;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        addressListView.setLayoutManager(new GridLayoutManager(this, 1));
        addressListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 适配器设置
        mAddressAdapter = new DeliveryAddressAdapter();
        addressListView.setAdapter(mAddressAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @SuppressWarnings("all")
    @Override
    protected void initData() {
        mDeliveryAddressList = (List<DeliveryAddressBody>) getIntent().getSerializableExtra("addressList");
        // 默认选择第一个地址
        for (int i = 0; i < mDeliveryAddressList.size(); i++) {
            if (mDeliveryAddressList.get(i).isSelected()) {
                tempPos = i;
                break;
            }
        }
        mDeliveryAddressList.get(tempPos).setSelected(true);
        mAddressAdapter.setDeliveryAddressList(mDeliveryAddressList);
    }

    @Override
    protected void initEvent() {
        // 选择地址
        mAddressAdapter.setOnClickItemAddress((addressBody, position) -> {
            for (DeliveryAddressBody body : mDeliveryAddressList) {
                body.setSelected(false);
            }
            mDeliveryAddressList.get(position).setSelected(true);
            // pageBack
            Intent intent = new Intent(DeliveryAddressAty.this, GoodsDataActivity.class);
            intent.putExtra("addressItem", addressBody);
            setResult(CHOOSE_ADDRESS_IN_GOODS, intent);
            finish();
        });
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
}
