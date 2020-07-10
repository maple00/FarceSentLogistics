package com.rainwood.sentlogistics.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.BulletinData;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 14:10
 * @Desc: 消息公告内容 adapter
 */
public final class BulletinContentAdapter extends RecyclerView.Adapter<BulletinContentAdapter.ViewHolder> {

    private List<BulletinData> mBulletinList = new ArrayList<>();

    public void setBulletinList(List<BulletinData> bulletinList) {
        mBulletinList.clear();
        mBulletinList.addAll(bulletinList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bulletin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(mBulletinList.get(position).getContent());
        holder.time.setText(mBulletinList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mBulletinList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
