package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.ICommonDetailCallback;

/**
 * @Author: a797s
 * @Date: 2020/6/23 16:19
 * @Desc: 详情presenter(活动详情)
 */
public interface ICommonDetailPresenter extends IBasePresenter<ICommonDetailCallback> {

    /**
     * 通过活动id请求活动详情
     */
    void requestDetailByAtyId(String activityId);
}
