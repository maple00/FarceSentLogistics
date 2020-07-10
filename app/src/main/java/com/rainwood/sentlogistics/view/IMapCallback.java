package com.rainwood.sentlogistics.view;

import com.rainwood.citynavigation.CityBean;
import com.rainwood.sentlogistics.base.IBaseCallback;
import com.rainwood.sentlogistics.model.domain.HotCity;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/23 14:34
 * @Desc: 地图 callback
 */
public interface IMapCallback extends IBaseCallback {


    /**
     * 获取城市数据
     *
     * @param cityList
     * @param hotCityList
     */
    default void getCityData(List<CityBean> cityList, List<HotCity> hotCityList) {
    }

    ;
}
