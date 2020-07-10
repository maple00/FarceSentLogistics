package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.ITabLayoutCallback;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:41
 * @Desc: tabLayout + Fragment 中的TabLayout presenter
 */
public interface ITabLayoutPresenter extends IBasePresenter<ITabLayoutCallback> {

    /**
     * 请求消息公告类型
     */
    void requestBulletinType();
}
