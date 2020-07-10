package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/9 17:06
 * @Desc: 货物信息
 */
public final class GoodsDataBody implements Serializable {

    @Override
    public String toString() {
        return "GoodsDataBody{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", goodsTips='" + goodsTips + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", safeRule='" + safeRule + '\'' +
                ", safePrice='" + safePrice + '\'' +
                ", address='" + address + '\'' +
                ", latX=" + latX +
                ", lngY=" + lngY +
                '}';
    }

    /**
     * 长
     */
    private float length;

    /**
     * 宽
     */
    private float width;

    /**
     * 高
     */
    private float height;

    /**
     * 货物大小提示
     */
    private String goodsTips;

    /**
     * 货物实体大小图片路径
     */
    private String path;

    /**
     * 货物类型
     */
    private String type;

    /**
     * 货物数量
     */
    private String number;

    /**
     * 保价规格
     */
    private String safeRule;

    /**
     * 保价
     */
    private String safePrice;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货地址经纬度
     */
    private double latX;
    private double lngY;

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getGoodsTips() {
        return goodsTips;
    }

    public void setGoodsTips(String goodsTips) {
        this.goodsTips = goodsTips;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSafeRule() {
        return safeRule;
    }

    public void setSafeRule(String safeRule) {
        this.safeRule = safeRule;
    }

    public String getSafePrice() {
        return safePrice;
    }

    public void setSafePrice(String safePrice) {
        this.safePrice = safePrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatX() {
        return latX;
    }

    public void setLatX(double latX) {
        this.latX = latX;
    }

    public double getLngY() {
        return lngY;
    }

    public void setLngY(double lngY) {
        this.lngY = lngY;
    }
}
