package com.rainwood.sentlogistics.base;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:21
 * @Desc: 状态接口基类
 */
public interface IBaseCallback {

    /**
     * 数据错误
     */
    default void onError() {
    }

    /**
     * 数据错误提示
     */
    default void onError(String tips) {
    }

    /**
     * 加载中
     */
    void onLoading();

    /**
     * 空数据
     */
    void onEmpty();
}
