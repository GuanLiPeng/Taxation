package com.example.taxation.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 招标列表的实体类
 */
public class BidInfoItem extends LitePalSupport {
    private String serialNumber; //编号
    private String classification; //分类
    private String info; //标题
    private String detailInfo; //正文
    private String address; //地址
    private String price; //价格
    private String date; //日期

    public BidInfoItem(String serialNumber,
                       String classification,
                       String info,
                       String detailInfo,
                       String address,
                       String price,
                       String date) {
        this.serialNumber = serialNumber;
        this.classification = classification;
        this.info = info;
        this.detailInfo = detailInfo;
        this.address = address;
        this.price = price;
        this.date = date;
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
