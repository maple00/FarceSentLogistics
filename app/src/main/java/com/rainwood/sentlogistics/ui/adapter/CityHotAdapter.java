package com.rainwood.sentlogistics.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.HotCity;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 14:44
 * @Desc: 热门城市adapter
 */
public final class CityHotAdapter extends RecyclerView.Adapter<CityHotAdapter.ViewHolder> {

    private List<HotCity> mList;

    public void setList(List<HotCity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setVisibility(View.GONE);
        holder.cityName.setText(mList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_city)
        private LinearLayout itemCity;
        @ViewInject(R.id.iv_image)
        private ImageView image;
        @ViewInject(R.id.tv_name)
        private TextView cityName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
