package com.rainwood.sentlogistics.view;

import com.rainwood.sentlogistics.base.IBaseCallback;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:40
 * @Desc: tabLayout + Fragment 中的TabLayout callback
 */
public interface ITabLayoutCallback extends IBaseCallback {


    /**
     * 获取消息公告结果
     * @param bulletinList
     */
    default void getBulletinTypes(List<String> bulletinList){}
}
