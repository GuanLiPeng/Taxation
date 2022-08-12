package com.example.taxation.bean;

/**
 * 详情页面分组列表的子节点
 */
public class GroupChildListItem {

    private String childTitle;
    private String startInfo;
    private String endInfo;

    public GroupChildListItem(String childTitle, String startInfo, String endInfo) {
        this.childTitle = childTitle;
        this.startInfo = startInfo;
        this.endInfo = endInfo;
    }

    public String getChildTitle() {
        return childTitle;
    }

    public void setChildTitle(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getStartInfo() {
        return startInfo;
    }

    public void setStartInfo(String startInfo) {
        this.startInfo = startInfo;
    }

    public String getEndInfo() {
        return endInfo;
    }

    public void setEndInfo(String endInfo) {
        this.endInfo = endInfo;
    }
}
