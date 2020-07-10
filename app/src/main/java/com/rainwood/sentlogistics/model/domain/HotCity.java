package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/23 14:46
 * @Desc: 热门城市
 */
public final class HotCity implements Serializable {

    /**
     * 名称
     */
    private String city;

    /**
     * 是否被选中
     */
    private boolean selected;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
