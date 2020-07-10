package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.IMapCallback;

/**
 * @Author: a797s
 * @Date: 2020/6/23 14:35
 * @Desc:
 */
public interface IMapPresenter extends IBasePresenter<IMapCallback> {

    /**
     * 获取城市列表及热门城市
     */
    void requestCityData();
}
