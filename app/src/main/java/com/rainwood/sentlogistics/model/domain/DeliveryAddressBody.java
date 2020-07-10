package com.rainwood.sentlogistics.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/7 17:18
 * @Desc: 收货地址
 */
public final class DeliveryAddressBody implements Serializable {
    @Override
    public String toString() {
        return "ShippingAddressData{" +
                "id='" + id + '\'' +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                ", latX=" + latX +
                ", lngY=" + lngY +
                '}';
    }

    /**
     * 地址id
     */
    private String id;

    /**
     * 街道
     */
    private String street;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 地址经纬度
     */
    private double latX;
    private double lngY;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 电话号码
     */
    private String telNum;

    /**
     * 楼层门牌号
     */
    private String floorDoorNum;

    /**
     * 选中
     */
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getFloorDoorNum() {
        return floorDoorNum;
    }

    public void setFloorDoorNum(String floorDoorNum) {
        this.floorDoorNum = floorDoorNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
