package com.example.taxation.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 查询招标状态页面所需要用到的实体类
 */
public class InquireInfoItem extends LitePalSupport {
    private String type; //所属于哪一个分类中的内容
    private boolean state; //当前状态（已通过，未通过...）

    private String serialNumber; //编号
    private String classification; //分类
    private String info; //标题
    private String detailInfo; //正文
    private String address; //地址
    private String price; //价格
    private String date; //日期

    public InquireInfoItem(String type,
                           boolean state,
                           String serialNumber,
                           String classification,
                           String info,
                           String detailInfo,
                           String address,
                           String price,
                           String date) {
        this.type = type;
        this.state = state;
        this.serialNumber = serialNumber;
        this.classification = classification;
        this.info = info;
        this.detailInfo = detailInfo;
        this.address = address;
        this.price = price;
        this.date = date;
    }

    public InquireInfoItem(String type,
                           boolean state,
                           BidInfoItem bidInfoItem) {
        this.type = type;
        this.state = state;
        this.serialNumber = bidInfoItem.getSerialNumber();
        this.classification = bidInfoItem.getClassification();
        this.info = bidInfoItem.getInfo();
        this.detailInfo = bidInfoItem.getDetailInfo();
        this.address = bidInfoItem.getAddress();
        this.price = bidInfoItem.getPrice();
        this.date = bidInfoItem.getDate();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
