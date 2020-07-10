package com.rainwood.sentlogistics.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.ActivityList;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:22
 * @Desc: 活动列表adapter
 */
public final class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<ActivityList> mEventLists;
    private Context mContext;

    public void setEventLists(List<ActivityList> eventLists) {
        mEventLists = eventLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mEventLists.get(position).getTitle());
        holder.content.setText(mEventLists.get(position).getSummary());
        holder.time.setText(mEventLists.get(position).getTime());
        Glide.with(mContext).load(mEventLists.get(position).getPhotoSrc())
                .error(mContext.getDrawable(R.drawable.ic_pre_loading))
                .placeholder(mContext.getDrawable(R.drawable.ic_pre_loading))
                .into(holder.image);
        // 点击事件
        holder.itemEvent.setOnClickListener(v -> mClickEventsListener.onClickItem(mEventLists.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mEventLists);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.ll_item_event)
        private LinearLayout itemEvent;
        @ViewInject(R.id.iv_image)
        private ImageView image;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickEventsListener {
        /**
         * 查看详情
         *
         * @param event
         * @param position
         */
        void onClickItem(ActivityList event, int position);
    }

    private OnClickEventsListener mClickEventsListener;

    public void setClickEventsListener(OnClickEventsListener clickEventsListener) {
        mClickEventsListener = clickEventsListener;
    }
}
