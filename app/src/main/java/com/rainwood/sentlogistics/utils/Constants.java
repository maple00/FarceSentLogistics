package com.rainwood.sentlogistics.utils;

/**
 * @Author: a797s
 * @Date: 2020/6/22 14:52
 * @Desc: 常量
 */
public final class Constants {

    /**
     * 网络请求失败
     */
    public static final String HTTP_MSG_RESPONSE_FAILED = "The request data failed and the response code is not 200,code = ";

    public static final String KEY_BULLETIN_TITLE = "key_bulletin_title";

    /**
     * 地图选址页面跳转code
     */
    public static final int CHOOSE_ADDRESS_IN_MAP_CODE = 0x1001;

    /**
     * 选择收货地址Code
     */
    public static final int CHOOSE_ADDRESS_IN_GOODS = 0x1002;

    /**
     * 跳转地图申明是发货地址还是收货地址 -- 默认是选择地点
     */
    public static boolean isChoosePoint = true;

    /**
     * 跳转地图是否选址当前位置 -- 默认当前位置
     */
    public static boolean isChooseCurrentAddress = true;

    /**
     * Base Uri
     */
    public static String BASE_URI = "https://www.yumukeji.cn/project/ruiruipai/library/";
}
