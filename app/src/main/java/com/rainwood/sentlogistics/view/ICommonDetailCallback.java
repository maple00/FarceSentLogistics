package com.rainwood.sentlogistics.view;

import com.rainwood.sentlogistics.base.IBaseCallback;

/**
 * @Author: a797s
 * @Date: 2020/6/23 16:18
 * @Desc: 详情（活动详情）
 */
public interface ICommonDetailCallback extends IBaseCallback {

    /**
     * 获取活动详情数据
     */
    default void getActivityDetailData(String data){}
}
