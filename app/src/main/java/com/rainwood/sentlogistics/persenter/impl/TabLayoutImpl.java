package com.rainwood.sentlogistics.persenter.impl;

import com.rainwood.sentlogistics.persenter.ITabLayoutPresenter;
import com.rainwood.sentlogistics.view.ITabLayoutCallback;

import java.util.Arrays;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:42
 * @Desc: tabLayout + Fragment 中的TabLayout impl
 */
public final class TabLayoutImpl implements ITabLayoutPresenter {

    private ITabLayoutCallback mTabLayoutCallback;

    /**
     * 获取消息公告分类
     */
    @Override
    public void requestBulletinType() {
        String[] types = {"消息", "公告"};
        mTabLayoutCallback.getBulletinTypes(Arrays.asList(types));
    }

    @Override
    public void registerViewCallback(ITabLayoutCallback callback) {
        mTabLayoutCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ITabLayoutCallback callback) {
        mTabLayoutCallback = null;
    }
}
