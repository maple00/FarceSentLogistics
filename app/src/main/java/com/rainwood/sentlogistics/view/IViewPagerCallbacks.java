package com.rainwood.sentlogistics.view;

import com.rainwood.sentlogistics.base.IBaseCallback;
import com.rainwood.sentlogistics.model.domain.BulletinData;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:55
 * @Desc: viewPager callbacks
 */
public interface IViewPagerCallbacks extends IBaseCallback {

    /**
     * 消息公告类型
     */
    String getBulletinType();

    void getBulletinData(List<BulletinData> bulletinList);
}
