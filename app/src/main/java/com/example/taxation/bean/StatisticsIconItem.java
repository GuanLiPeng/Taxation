package com.example.taxation.bean;

/**
 * 更多分类用的图标item
 */
public class StatisticsIconItem {
    int imgId;
    private String text;

    public StatisticsIconItem(int imgId, String text) {
        this.imgId = imgId;
        this.text = text;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
