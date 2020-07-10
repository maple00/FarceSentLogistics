package com.rainwood.sentlogistics.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:16
 * @Desc: 收货地址列表
 */
public final class ShippingAddressAdapter extends BaseAdapter {

    private List<DeliveryAddressBody> mDataList = new ArrayList<>();

    public void setDataList(List<DeliveryAddressBody> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtil.getSize(mDataList);
    }

    @Override
    public DeliveryAddressBody getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_goods, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.currentAddress.setText(getItem(position).getAddress());
        holder.currentStreet.setText(getItem(position).getStreet());
        holder.currentAddress.setVisibility(TextUtils.isEmpty(getItem(position).getAddress()) ? View.GONE : View.VISIBLE);
        // 至少有一个收货地址
        if (position == 0 || TextUtils.isEmpty(getItem(position).getAddress())) {
            holder.mImageDelete.setVisibility(View.GONE);
        } else {
            holder.mImageDelete.setVisibility(View.VISIBLE);
        }
        // 点击事件
        holder.mImageDelete.setOnClickListener(v -> mShippingListener.onClickDeleteItem(getItem(position), position));
        holder.addAddressView.setOnClickListener(v -> mShippingListener.onClickPlusAddress(getItem(position), position));
        // 为空可点击
        holder.addAddressView.setClickable(TextUtils.isEmpty(getItem(position).getAddress()));
        holder.addAddressView.setFocusableInTouchMode(TextUtils.isEmpty(getItem(position).getAddress()));
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_current_street)
        private TextView currentStreet;
        @ViewInject(R.id.tv_current_address)
        private TextView currentAddress;
        @ViewInject(R.id.iv_delete)
        private ImageView mImageDelete;
        @ViewInject(R.id.rl_shipping_address)
        private RelativeLayout addAddressView;
    }

    public interface OnClickShippingListener {
        /**
         * 删除地址
         *
         * @param addressData
         * @param position
         */
        void onClickDeleteItem(DeliveryAddressBody addressData, int position);

        void onClickPlusAddress(DeliveryAddressBody addressData, int position);
    }

    private OnClickShippingListener mShippingListener;

    public void setShippingListener(OnClickShippingListener shippingListener) {
        mShippingListener = shippingListener;
    }
}
