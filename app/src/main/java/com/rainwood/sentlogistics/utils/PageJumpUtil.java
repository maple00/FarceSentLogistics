package com.rainwood.sentlogistics.utils;

import android.content.Context;
import android.content.Intent;

import com.rainwood.sentlogistics.base.BaseActivity;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:10
 * @Desc: 页面跳转工具类
 */
public final class PageJumpUtil {

    private static Intent sIntent;

    /**
     * 活动列表跳转到活动详情
     *
     * @param context
     * @param clazz
     * @param activityId
     */
    public static void activities2Detail(Context context, Class<? extends BaseActivity> clazz, String activityId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "活动详情");
        sIntent.putExtra("detailId", activityId);
        sIntent.putExtra("menu", "activityDetail");
        context.startActivity(sIntent);
    }

}
