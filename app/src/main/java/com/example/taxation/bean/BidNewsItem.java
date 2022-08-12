package com.example.taxation.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 新闻列表的实体类
 */
public class BidNewsItem extends LitePalSupport {
    private int id;
    private String title; //标题
    private String mainInfo; //正文内容
    private String from; //来源
    private String date; //日期
    private int imgId; //保存图片id

    public BidNewsItem(int id, String title, String mainInfo, String from, String date, int imgId) {
        this.id = id;
        this.title = title;
        this.mainInfo = mainInfo;
        this.from = from;
        this.date = date;
        this.imgId = imgId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
