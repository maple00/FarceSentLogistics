package com.rainwood.sentlogistics.persenter;

import com.rainwood.sentlogistics.base.IBasePresenter;
import com.rainwood.sentlogistics.view.ITokenOrderCallback;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:33
 * @Desc: 下单
 */
public interface ITokenOrderPresenter extends IBasePresenter<ITokenOrderCallback> {

    /**
     * 请求发布类型
     */
    void requestOrderPublishType();

}
