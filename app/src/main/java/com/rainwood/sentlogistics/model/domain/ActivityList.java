package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/23 11:25
 * @Desc: 互动列表
 */
public final class ActivityList implements Serializable {

    /**
     * 活动id
     */
    private String id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动内容
     */
    private String summary;

    /**
     * 活动时间
     */
    private String time;

    /**
     * 互动列表缩略图
     */
    private String photoSrc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }
}
