package com.rainwood.sentlogistics.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:39
 * @Desc: 设置recyclerView的item之间间距辅助类
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space = 0;
    private int leftSpace = 0;
    private int rightSpace = 0;
    private int topSpace = 0;
    private int bottomSpace = 0;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int leftSpace, int rightSpace, int topSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space == 0 ? leftSpace : space;
        outRect.right = space == 0 ? rightSpace : space;
        outRect.bottom = space == 0 ? bottomSpace : space;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space == 0 ? topSpace : space;
    }
}