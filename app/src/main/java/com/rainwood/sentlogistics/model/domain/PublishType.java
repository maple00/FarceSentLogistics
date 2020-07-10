package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/9 14:43
 * @Desc: 服务类型
 */
public final class PublishType implements Serializable {

    /**
     * 类型
     */
    private String type;

    /**
     * 选中
     */
    private boolean selected;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
