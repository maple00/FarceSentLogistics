package com.rainwood.sentlogistics.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.base.BaseActivity;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.model.domain.GoodsDataBody;
import com.rainwood.sentlogistics.ui.adapter.GoodsDataAdapter;
import com.rainwood.sentlogistics.ui.dialog.MessageDialog;
import com.rainwood.sentlogistics.utils.Constants;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.sentlogistics.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/9 15:36
 * @Desc: 填写货物信息
 */
public final class GoodsDataActivity extends BaseActivity {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.rv_goods_list)
    private RecyclerView goodsListView;

    private GoodsDataAdapter mGoodsDataAdapter;
    private List<GoodsDataBody> mGoodsDataBodyList;
    private List<DeliveryAddressBody> mDeliveryAddressList;
    private static int clickPos;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_data;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        goodsListView.setLayoutManager(new GridLayoutManager(this, 1));
        goodsListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 适配器设置
        mGoodsDataAdapter = new GoodsDataAdapter();
        goodsListView.setAdapter(mGoodsDataAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @SuppressWarnings("all")
    @Override
    protected void initData() {
        // 获取添加的地址信息
        mDeliveryAddressList = (List<DeliveryAddressBody>) getIntent().getSerializableExtra("address");

        // 模拟货物商品
        mGoodsDataBodyList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            GoodsDataBody goodsDataBody = new GoodsDataBody();
            mGoodsDataBodyList.add(goodsDataBody);
        }
        mGoodsDataAdapter.setDataBodyList(mGoodsDataBodyList);
    }

    @Override
    protected void initEvent() {
        mGoodsDataAdapter.setOnClickGoodsListener(new GoodsDataAdapter.OnClickGoodsListener() {
            @Override
            public void onClickPlusReduceNum(GoodsDataBody goodsData, int position) {
                LogUtil.d("sxs", "数量加减--- " + goodsData.getNumber());
            }

            @Override
            public void onClickSafeRule(GoodsDataBody goodsData, int position) {
                toast("保价规格");
            }

            @Override
            public void onClickSelectSafePrice(GoodsDataBody goodsData, int position) {
                toast("选择保价");
            }

            @Override
            public void onClickSelectAddress(GoodsDataBody goodsData, int position) {
                if (ListUtil.getSize(mDeliveryAddressList) == 0) {
                    toast("请选选择收货地址");
                    return;
                }
                clickPos = position;
                Intent intent = new Intent(GoodsDataActivity.this, DeliveryAddressAty.class);
                intent.putExtra("addressList", (Serializable) mDeliveryAddressList);
                intent.putExtra("title", "收货地址");
                startActivityForResult(intent, Constants.CHOOSE_ADDRESS_IN_GOODS);
            }

            @Override
            public void onClickItemDelete(GoodsDataBody goodsData, int position) {
                new MessageDialog.Builder(GoodsDataActivity.this)
                        .setMessage("确定要删除该货物吗？")
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setNowShowConfirm(false)
                        .setShowImageClose(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                mGoodsDataBodyList.remove(position);
                                mGoodsDataAdapter.setDataBodyList(mGoodsDataBodyList);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }

            @Override
            public void onItemContentListener(GoodsDataBody goodsData, int position) {
                LogUtil.d("sxs", "货物内容 ---  " + goodsData.toString());
                if (goodsData.getLength() == 0 || goodsData.getWidth() == 0 || goodsData.getHeight() == 0
                        || TextUtils.isEmpty(goodsData.getAddress()) || TextUtils.isEmpty(goodsData.getType())) {
                    //toast("请填写货物" + (position + 1) + "的完整信息");
                    goodsListView.scrollToPosition(position);
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 选择地址
            if (requestCode == Constants.CHOOSE_ADDRESS_IN_GOODS && resultCode == Constants.CHOOSE_ADDRESS_IN_GOODS) {
                DeliveryAddressBody addressBody = (DeliveryAddressBody) data.getSerializableExtra("addressItem");
                if (addressBody != null) {
                    mGoodsDataBodyList.get(clickPos).setAddress(addressBody.getAddress());
                    mGoodsDataBodyList.get(clickPos).setLatX(addressBody.getLatX());
                    mGoodsDataBodyList.get(clickPos).setLngY(addressBody.getLngY());
                    mGoodsDataAdapter.setDataBodyList(mGoodsDataBodyList);
                    mGoodsDataAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_plus_goods, R.id.tv_plus_goods, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_plus_goods:
            case R.id.tv_plus_goods:
                mGoodsDataBodyList.add(new GoodsDataBody());
                mGoodsDataAdapter.setDataBodyList(mGoodsDataBodyList);
                break;
            case R.id.btn_save:
                toast("保存");
                break;
        }
    }
}
