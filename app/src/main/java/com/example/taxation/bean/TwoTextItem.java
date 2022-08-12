package com.example.taxation.bean;

/**
 * 左半部分显示标题(textView)，右半部分显示信息(textView)
 */
public class TwoTextItem {

    private String title;
    private String info;

    public TwoTextItem(String title, String info) {
        this.title = title;
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
