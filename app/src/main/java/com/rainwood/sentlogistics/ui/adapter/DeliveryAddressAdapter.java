package com.rainwood.sentlogistics.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.DeliveryAddressBody;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/10 13:22
 * @Desc: 收货地址adapter
 */
public final class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.ViewHolder> {

    private List<DeliveryAddressBody> mDeliveryAddressList = new ArrayList<>();

    public void setDeliveryAddressList(List<DeliveryAddressBody> deliveryAddressList) {
        mDeliveryAddressList.clear();
        mDeliveryAddressList.addAll(deliveryAddressList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selected.setChecked(mDeliveryAddressList.get(position).isSelected());
        holder.deliveryName.setText(mDeliveryAddressList.get(position).getConsignee());
        holder.telNumber.setText(mDeliveryAddressList.get(position).getTelNum());
        holder.address.setText(mDeliveryAddressList.get(position).getAddress());

        // 点击事件 -- 选中
        holder.selected.setOnClickListener(v -> {
            for (DeliveryAddressBody addressBody : mDeliveryAddressList) {
                addressBody.setSelected(false);
            }
            mDeliveryAddressList.get(position).setSelected(true);
            notifyDataSetChanged();
        });

        holder.itemAddress.setOnClickListener(v -> mOnClickItemAddress.onClickItem(mDeliveryAddressList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mDeliveryAddressList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_address)
        private RelativeLayout itemAddress;
        @ViewInject(R.id.tv_name)
        private TextView deliveryName;
        @ViewInject(R.id.tv_tel_number)
        private TextView telNumber;
        @ViewInject(R.id.cb_selected)
        private CheckBox selected;
        @ViewInject(R.id.tv_address)
        private TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemAddress {
        /**
         * 选中
         *
         * @param addressBody
         * @param position
         */
        void onClickItem(DeliveryAddressBody addressBody, int position);
    }

    private OnClickItemAddress mOnClickItemAddress;

    public void setOnClickItemAddress(OnClickItemAddress onClickItemAddress) {
        mOnClickItemAddress = onClickItemAddress;
    }
}
