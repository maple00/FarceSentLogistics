package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/8 10:58
 * @Desc: 地址搜索
 */
public final class AddressSearchBody implements Serializable {

    /**
     * 含关键字的地名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离
     */
    private String distance;

    /**
     * 经纬度
     */
    private double latX;
    private double lngY;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    @Override
    public String toString() {
        return "AddressSearchBody{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                ", latX=" + latX +
                ", lngY=" + lngY +
                '}';
    }
}
