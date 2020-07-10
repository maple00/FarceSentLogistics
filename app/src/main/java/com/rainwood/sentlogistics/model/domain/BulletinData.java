package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/7 14:14
 * @Desc: 消息公告
 */
public final class BulletinData implements Serializable {

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
