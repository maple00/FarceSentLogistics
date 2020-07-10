package com.rainwood.sentlogistics.view;

import com.rainwood.sentlogistics.base.IBaseCallback;
import com.rainwood.sentlogistics.model.domain.ActivityList;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:51
 * @Desc:  列表view
 */
public interface ICommonListCallback extends IBaseCallback {

    /**
     * 活动列表
     */
    default void getActivityList(List<ActivityList> activityList){}
}
