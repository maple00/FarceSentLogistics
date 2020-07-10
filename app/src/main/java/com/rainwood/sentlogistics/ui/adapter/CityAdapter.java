package com.rainwood.sentlogistics.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.citynavigation.CityBean;
import com.rainwood.sentlogistics.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;


/**
 * create by a797s in 2020/6/23 14:07
 *
 * @Description : 城市adapter
 * @Usage :
 **/
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private List<CityBean> mDatas;

    public void setDatas(List<CityBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CityAdapter.ViewHolder holder, final int position) {
        final CityBean cityBean = mDatas.get(position);
        holder.tvCity.setText(cityBean.getCity());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("选择了--- " + mDatas.get(position).getCity());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tvCity)
        TextView tvCity;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
