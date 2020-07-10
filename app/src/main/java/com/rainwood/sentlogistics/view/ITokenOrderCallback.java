package com.rainwood.sentlogistics.view;

import com.rainwood.sentlogistics.base.IBaseCallback;
import com.rainwood.sentlogistics.model.domain.PublishType;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:32
 * @Desc: 下单
 */
public interface ITokenOrderCallback extends IBaseCallback {

    /**
     * 下单的状态列表
     *
     * @param typeList
     */
    void getPublishTypeData(List<PublishType> typeList);
}
