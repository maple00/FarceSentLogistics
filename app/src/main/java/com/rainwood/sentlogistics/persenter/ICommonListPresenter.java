package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.ICommonListCallback;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:50
 * @Desc: 列表接口
 */
public interface ICommonListPresenter extends IBasePresenter<ICommonListCallback> {

    /**
     * 请求活动列表
     */
    void requestEventsListData();
}
