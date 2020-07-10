package com.rainwood.sentlogistics.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.AddressSearchBody;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/8 10:44
 * @Desc: 搜索地址列表
 */
public final class SearchAddressListAdapter extends RecyclerView.Adapter<SearchAddressListAdapter.ViewHolder> {

    private List<AddressSearchBody> mSearchBodyList = new ArrayList<>();

    public void setSearchBodyList(List<AddressSearchBody> searchBodyList) {
        mSearchBodyList.clear();
        mSearchBodyList.addAll(searchBodyList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_distance, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.district.setText(mSearchBodyList.get(position).getName());
        holder.address.setText(mSearchBodyList.get(position).getAddress());
        holder.address.setVisibility(TextUtils.isEmpty(mSearchBodyList.get(position).getAddress())
                ? View.GONE : View.VISIBLE);
        holder.distance.setText("距我" + mSearchBodyList.get(position).getDistance() + "km");
        holder.distance.setVisibility(TextUtils.isEmpty(mSearchBodyList.get(position).getDistance()) ? View.GONE : View.VISIBLE);
        // 点击事件
        holder.itemAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickItemAddress.onClickItem(mSearchBodyList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mSearchBodyList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_address)
        private RelativeLayout itemAddress;
        @ViewInject(R.id.tv_name)
        private TextView district;
        @ViewInject(R.id.tv_distance)
        private TextView distance;
        @ViewInject(R.id.tv_address)
        private TextView address;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemAddress {
        /**
         * 选中地址
         *
         * @param searchBody
         * @param position
         */
        void onClickItem(AddressSearchBody searchBody, int position);
    }

    private OnClickItemAddress mClickItemAddress;

    public void setClickItemAddress(OnClickItemAddress clickItemAddress) {
        mClickItemAddress = clickItemAddress;
    }
}
