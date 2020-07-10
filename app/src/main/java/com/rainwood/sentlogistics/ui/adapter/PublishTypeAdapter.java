package com.rainwood.sentlogistics.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.PublishType;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/9 14:34
 * @Desc: 发布类型adapter
 */
public final class PublishTypeAdapter extends RecyclerView.Adapter<PublishTypeAdapter.ViewHolder> {

    private List<PublishType> mTypeList = new ArrayList<>();
    private Context mContext;

    public void setTypeList(List<PublishType> typeList) {
        mTypeList.clear();
        mTypeList.addAll(typeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_publish_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mTypeList.get(position).getType());
        holder.name.setTextColor(mTypeList.get(position).isSelected()
                ? mContext.getColor(R.color.colorPrimary) :
                mContext.getColor(R.color.fontColor));
        holder.mImageChecked.setVisibility(mTypeList.get(position).isSelected() ? View.VISIBLE : View.GONE);
        holder.itemPublish.setBackgroundResource(mTypeList.get(position).isSelected()
                ? R.drawable.shape_selected_publish : R.drawable.shape_unselected_publish);
        // 点击事件
        holder.itemPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemPublish.onClickItem(mTypeList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mTypeList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_publish)
        private RelativeLayout itemPublish;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.iv_checked)
        private ImageView mImageChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemPublish {
        /**
         * 选中
         *
         * @param publishType
         * @param position
         */
        void onClickItem(PublishType publishType, int position);
    }

    private OnClickItemPublish mOnClickItemPublish;

    public void setOnClickItemPublish(OnClickItemPublish onClickItemPublish) {
        mOnClickItemPublish = onClickItemPublish;
    }
}
