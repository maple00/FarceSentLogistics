package com.rainwood.sentlogistics.ui.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.model.domain.GoodsDataBody;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.sentlogistics.utils.LogUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/9 16:05
 * @Desc: 货物信息 adapter
 */
public final class GoodsDataAdapter extends RecyclerView.Adapter<GoodsDataAdapter.ViewHolder> {

    private List<GoodsDataBody> mDataBodyList = new ArrayList<>();
    private float length, width, height;
    // 默认货物数量为1
    private int currentNum = 1;

    public void setDataBodyList(List<GoodsDataBody> dataBodyList) {
        mDataBodyList.clear();
        mDataBodyList.addAll(dataBodyList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.goodsDelete.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.safePrice.setText(mDataBodyList.get(position).getSafePrice());
        holder.mTextAddress.setText(mDataBodyList.get(position).getAddress());
        // 长宽高监听 -- 非0开头的数字
        setViewListener(holder, holder.mEditLength, "length", position);
        setViewListener(holder, holder.mEditWidth, "width", position);
        setViewListener(holder, holder.mEditHeight, "height", position);
        // 货物label
        holder.goodsLabel.setText("货物" + (position + 1));
        // 货物内容监听
        setViewListener(holder, holder.goodsType, "type", position);
        setViewListener(holder, holder.goodsNum, "number", position);
        setViewListener(holder, holder.safePrice, "safePrice", position);
        setViewListener(holder, holder.mTextAddress, "address", position);
        // 货物数量加减
        holder.plusNum.setOnClickListener(v -> {
            holder.goodsNum.setText(String.valueOf(++currentNum));
            mDataBodyList.get(position).setNumber(String.valueOf(currentNum));
            mOnClickGoodsListener.onClickPlusReduceNum(mDataBodyList.get(position), position);
        });
        holder.reduceNum.setOnClickListener(v -> {
            if (currentNum < 2) {
                currentNum = 2;
            }
            holder.goodsNum.setText(String.valueOf(--currentNum));
            mDataBodyList.get(position).setNumber(String.valueOf(currentNum));
            mOnClickGoodsListener.onClickPlusReduceNum(mDataBodyList.get(position), position);
        });
        // 保价规格
        holder.safeRule.setOnClickListener(v -> mOnClickGoodsListener.onClickSafeRule(mDataBodyList.get(position), position));
        holder.mImageDoubt.setOnClickListener(v -> mOnClickGoodsListener.onClickSafeRule(mDataBodyList.get(position), position));
        // 选择保价
        holder.safePrice.setOnClickListener(v -> mOnClickGoodsListener.onClickSelectSafePrice(mDataBodyList.get(position), position));
        // 收货地址选择
        holder.mTextAddress.setOnClickListener(v -> mOnClickGoodsListener.onClickSelectAddress(mDataBodyList.get(position), position));
        // 删除
        holder.goodsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickGoodsListener.onClickItemDelete(mDataBodyList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtil.getSize(mDataBodyList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_goods_label)
        private TextView goodsLabel;
        @ViewInject(R.id.iv_delete)
        private ImageView goodsDelete;
        @ViewInject(R.id.et_length)
        private EditText mEditLength;
        @ViewInject(R.id.et_width)
        private EditText mEditWidth;
        @ViewInject(R.id.et_height)
        private EditText mEditHeight;
        @ViewInject(R.id.tv_goods_size_desc)
        private TextView goodsSizeDesc;
        @ViewInject(R.id.iv_goods)
        private ImageView mImageGoods;
        @ViewInject(R.id.et_goods_type)
        private EditText goodsType;
        @ViewInject(R.id.tv_reduce_num)
        private TextView reduceNum;
        @ViewInject(R.id.tv_goods_num)
        private TextView goodsNum;
        @ViewInject(R.id.tv_plus_num)
        private TextView plusNum;
        @ViewInject(R.id.iv_doubt)
        private ImageView mImageDoubt;
        @ViewInject(R.id.tv_safe_rule)
        private TextView safeRule;
        @ViewInject(R.id.tv_safe_price)
        private TextView safePrice;
        @ViewInject(R.id.tv_address)
        private TextView mTextAddress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    /**
     * itemContent 监听
     *
     * @param holder
     * @param view
     * @param flag
     */
    private void setViewListener(ViewHolder holder, View view, String flag, int position) {
        // 可编辑的窗口
        if (view instanceof EditText) {
            EditText editView = (EditText) view;
            editView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    switch (flag) {
                        case "length":
                            length = Float.parseFloat(TextUtils.isEmpty(editView.getText().toString()) ? "0" : editView.getText().toString());
                            break;
                        case "width":
                            width = Float.parseFloat(TextUtils.isEmpty(editView.getText().toString()) ? "0" : editView.getText().toString());
                            break;
                        case "height":
                            height = Float.parseFloat(TextUtils.isEmpty(editView.getText().toString()) ? "0" : editView.getText().toString());
                            break;
                    }
                    // 如果长宽高都填写完成了之后，测量大小
                    if (length != 0 && width != 0 && height != 0) {
                        // 都输入完成
                        holder.goodsSizeDesc.setVisibility(View.VISIBLE);
                        holder.mImageGoods.setVisibility(View.VISIBLE);
                        LogUtil.d("sxs", "-- 货物大小 --- " + height * width * length);
                        GoodsDataBody goodsDataBody = mDataBodyList.get(position);
                        goodsDataBody.setLength(length);
                        goodsDataBody.setWidth(width);
                        goodsDataBody.setHeight(height);
                        mOnClickGoodsListener.onItemContentListener(goodsDataBody, position);
                    } else {
                        // 未输入完成
                        holder.goodsSizeDesc.setVisibility(View.GONE);
                        holder.mImageGoods.setVisibility(View.GONE);
                    }
                }
            });
        }
        // TextView 文本域
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    GoodsDataBody goodsDataBody = mDataBodyList.get(position);
                    switch (flag) {
                        case "type":
                            goodsDataBody.setType(s.toString());
                            break;
                        case "number":
                            goodsDataBody.setNumber(s.toString());
                            break;
                        case "safePrice":
                            goodsDataBody.setSafePrice(s.toString());
                            break;
                        case "address":
                            goodsDataBody.setAddress(s.toString());
                            break;
                    }
                    mOnClickGoodsListener.onItemContentListener(goodsDataBody, position);
                }
            });
        }

    }

    public interface OnClickGoodsListener {

        /**
         * 货物数量加1
         *
         * @param goodsData
         * @param position
         */
        void onClickPlusReduceNum(GoodsDataBody goodsData, int position);

        /**
         * 保价规则
         *
         * @param goodsData
         * @param position
         */
        void onClickSafeRule(GoodsDataBody goodsData, int position);

        /**
         * 选择保价
         *
         * @param goodsData
         * @param position
         */
        void onClickSelectSafePrice(GoodsDataBody goodsData, int position);

        /**
         * 选择地址
         *
         * @param goodsData
         * @param position
         */
        void onClickSelectAddress(GoodsDataBody goodsData, int position);

        /**
         * 删除item
         *
         * @param goodsData
         * @param position
         */
        void onClickItemDelete(GoodsDataBody goodsData, int position);

        /**
         * item 内容监听
         */
        void onItemContentListener(GoodsDataBody goodsData, int position);
    }

    private OnClickGoodsListener mOnClickGoodsListener;

    public void setOnClickGoodsListener(OnClickGoodsListener onClickGoodsListener) {
        mOnClickGoodsListener = onClickGoodsListener;
    }
}
