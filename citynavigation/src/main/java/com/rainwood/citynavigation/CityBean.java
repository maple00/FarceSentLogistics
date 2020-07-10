package com.rainwood.citynavigation;

import com.rainwood.citynavigation.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityBean extends BaseIndexPinyinBean implements Serializable {

    private String city;//城市名字

    public CityBean() {
    }

    public CityBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getTarget() {
        return city;
    }
}
