package com.rainwood.sentlogistics.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.sentlogistics.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:05
 * @Desc: 图片文字组合
 */
public final class GroupIconText extends LinearLayout {

    @ViewInject(R.id.iv_icon)
    private ImageView iconIV;
    @ViewInject(R.id.tv_values)
    private TextView values;


    public GroupIconText(Context context) {
        this(context, null);
    }

    public GroupIconText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"CustomViewStyleable", "Recycle"})
    public GroupIconText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.group_text_icon, this, true);
        ViewBind.inject(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GroupIconText);
        // text
        String valueStr = typedArray.getString(R.styleable.GroupIconText_text);
        if (!TextUtils.isEmpty(valueStr)) {
            values.setText(valueStr);
        }
        int textSize = typedArray.getInteger(R.styleable.GroupIconText_textSize, 12);
        if (textSize != 0) {
            values.setTextSize(textSize);
        }
        // textColor
        int color = typedArray.getColor(R.styleable.GroupIconText_textColor, context.getColor(R.color.labelColor));
        if (color != 0) {
            values.setTextColor(color);
        }
        int spacing = typedArray.getInteger(R.styleable.GroupIconText_spacing, 0);
        if (spacing != 0) {
            values.setPadding(FontSwitchUtil.dip2px(context, spacing), 0, 0, 0);
        }
        // icon
        int icon = typedArray.getResourceId(R.styleable.GroupIconText_icon, R.drawable.ic_default_head);
        if (icon != -1) {
            iconIV.setImageResource(icon);
        }
        // 回收
        typedArray.recycle();
    }

    public void setValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            values.setText(value);
        }
    }

    public void setIcon(int resId) {
        if (resId != 0) {
            iconIV.setImageResource(resId);
        }
    }
}
