package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.IViewPagerCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:56
 * @Desc: viewPager presenter
 */
public interface IViewPagerPresenter extends IBasePresenter<IViewPagerCallbacks> {

    /**
     * 获取消息内容
     */
    void requestContentByType(String type);
}
